package com.hanserwei.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // ========== 公共接口 ==========
    
    @GetMapping("/hello")
    public String hello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "你好，" + auth.getName() + "！当前拥有的权限：" + auth.getAuthorities();
    }

    // ========== 用户管理接口 ==========
    
    @GetMapping("/user/list")
    @PreAuthorize("hasAuthority('user:read')")
    public String listUsers() {
        return "用户列表 - 需要 user:read 权限";
    }
    
    @GetMapping("/user/create")
    @PreAuthorize("hasAuthority('user:write')")
    public String createUser() {
        return "创建用户 - 需要 user:write 权限";
    }

    // ========== 角色管理接口 ==========
    
    @GetMapping("/role/list")
    @PreAuthorize("hasAuthority('role:read')")
    public String listRoles() {
        return "角色列表 - 需要 role:read 权限";
    }
    
    @GetMapping("/role/create")
    @PreAuthorize("hasAuthority('role:write')")
    public String createRole() {
        return "创建角色 - 需要 role:write 权限";
    }

    // ========== 订单管理接口 ==========
    
    @GetMapping("/order/list")
    @PreAuthorize("hasAuthority('order:read')")
    public String listOrders() {
        return "订单列表 - 需要 order:read 权限";
    }
    
    @GetMapping("/order/pay")
    @PreAuthorize("hasAuthority('order:pay')")
    public String payOrder() {
        return "支付订单 - 需要 order:pay 权限";
    }

    // ========== 学生管理接口 ==========
    
    @GetMapping("/student/list")
    @PreAuthorize("hasAuthority('student:find')")
    public String listStudents() {
        return "学生列表 - 需要 student:find 权限";
    }
    
    @GetMapping("/student/delete")
    @PreAuthorize("hasAuthority('student:del')")
    public String deleteStudent() {
        return "删除学生 - 需要 student:del 权限";
    }

    // ========== 角色级别的访问控制 ==========
    
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "管理员控制台 - 仅限 ADMIN 角色访问";
    }
    
    @GetMapping("/normal/dashboard")
    @PreAuthorize("hasRole('NORMAL')")
    public String normalDashboard() {
        return "普通用户控制台 - 仅限 NORMAL 角色访问";
    }
    
    // ========== 组合权限示例 ==========
    
    @GetMapping("/admin/full-control")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('user:write')")
    public String fullControl() {
        return "完全控制 - 需要 ADMIN 角色且拥有 user:write 权限";
    }
}