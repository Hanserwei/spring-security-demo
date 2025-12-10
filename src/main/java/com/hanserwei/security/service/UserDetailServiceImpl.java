package com.hanserwei.security.service;

import com.hanserwei.security.domain.dataobject.Permission;
import com.hanserwei.security.domain.dataobject.Role;
import com.hanserwei.security.domain.dataobject.User;
import com.hanserwei.security.domain.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("开始加载用户: {}", username);

        // 从数据库查询用户，同时获取角色和权限
        User user = userRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> {
                    log.warn("用户不存在: {}", username);
                    return new UsernameNotFoundException("用户名或密码错误");
                });

        // 检查账户是否被锁定
        if (user.getLockUntil() != null && user.getLockUntil().isAfter(Instant.now())) {
            log.warn("账户被锁定: {}, 锁定到期时间: {}", username, user.getLockUntil());
            throw new UsernameNotFoundException("账户已被锁定，请稍后再试");
        }

        // 构建权限列表（角色 + 权限）
        List<GrantedAuthority> authorities = buildAuthorities(user.getRoles());

        log.info("用户 {} 加载成功，拥有权限: {}", username, authorities);

        // 返回 Spring Security 的 UserDetails 对象
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    /**
     * 构建用户的权限列表
     * 包括角色（ROLE_xxx）和权限（permission:code）
     */
    private List<GrantedAuthority> buildAuthorities(Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (roles == null || roles.isEmpty()) {
            return authorities;
        }

        for (Role role : roles) {
            // 添加角色（Spring Security 的角色需要 ROLE_ 前缀）
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));

            // 添加该角色对应的所有权限
            Set<Permission> permissions = role.getPermissions();
            if (permissions != null && !permissions.isEmpty()) {
                for (Permission permission : permissions) {
                    authorities.add(new SimpleGrantedAuthority(permission.getCode()));
                }
            }
        }

        return authorities;
    }
}
