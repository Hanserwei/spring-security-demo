package com.hanserwei.security.domain.repository;

import com.hanserwei.security.domain.dataobject.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * 根据用户名查询用户，同时获取用户的角色和权限信息
     */
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.roles r
            LEFT JOIN FETCH r.permissions
            WHERE u.username = :username
            """)
    Optional<User> findByUsernameWithRolesAndPermissions(@Param("username") String username);

    /**
     * 根据用户名查询用户（简单查询）
     */
    Optional<User> findByUsername(String username);
}
