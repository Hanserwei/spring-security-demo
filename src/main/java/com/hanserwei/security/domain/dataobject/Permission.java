package com.hanserwei.security.domain.dataobject;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "sys_permission",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_sys_permission_code", columnNames = "code")
    }
)
public class Permission {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String code; // 例如 user:read / user:write / order:pay

    @Column(nullable = false, length = 80)
    private String name;

    @Column(length = 255)
    private String description;

    // 可选：资源与动作拆开（更结构化）
    @Column(length = 80)
    private String resource; // user/order...

    @Column(length = 40)
    private String action;   // read/write...

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    // getter/setter 省略
}
