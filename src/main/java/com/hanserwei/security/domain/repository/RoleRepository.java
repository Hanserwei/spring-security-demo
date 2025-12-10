package com.hanserwei.security.domain.repository;

import com.hanserwei.security.domain.dataobject.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
