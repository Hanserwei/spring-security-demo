package com.hanserwei.security.domain.dataobject;

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
    name = "sys_role",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_sys_role_code", columnNames = "code")
    }
)
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 64)
    private String code;

    @Column(nullable = false, length = 64)
    private String name;

    @Column()
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sys_role_permission",
        joinColumns = @JoinColumn(name = "role_id", columnDefinition = "uuid"),
        inverseJoinColumns = @JoinColumn(name = "permission_id", columnDefinition = "uuid"),
        uniqueConstraints = @UniqueConstraint(name = "uk_role_permission", columnNames = {"role_id", "permission_id"}),
        indexes = {
            @Index(name = "idx_role_perm_role", columnList = "role_id"),
            @Index(name = "idx_role_perm_perm", columnList = "permission_id")
        }
    )
    private Set<Permission> permissions = new HashSet<>();

}
