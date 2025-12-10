package com.hanserwei.security.domain.dataobject;

import com.hanserwei.security.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "sys_user",
        indexes = {
                @Index(name = "idx_sys_user_username", columnList = "username"),
                @Index(name = "idx_sys_user_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_sys_user_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_sys_user_email", columnNames = "email")
        }
)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(nullable = false, length = 120)
    private String password;

    @Column(length = 120)
    private String email;

    @Column(length = 40)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    // 可选：失败次数 + 锁定到期时间（登录保护）
    @Column(nullable = false)
    private int failedLoginAttempts = 0;

    @Column
    private Instant lockUntil;

    // 可选：最后登录
    @Column
    private Instant lastLoginAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id", columnDefinition = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "role_id", columnDefinition = "uuid"),
            uniqueConstraints = @UniqueConstraint(name = "uk_user_role", columnNames = {"user_id", "role_id"}),
            indexes = {
                    @Index(name = "idx_user_role_user", columnList = "user_id"),
                    @Index(name = "idx_user_role_role", columnList = "role_id")
            }
    )
    private Set<Role> roles = new HashSet<>();

}
