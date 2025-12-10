package com.hanserwei.security.domain.repository;

import com.hanserwei.security.domain.dataobject.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
}
