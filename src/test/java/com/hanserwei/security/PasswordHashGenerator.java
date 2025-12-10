package com.hanserwei.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用于生成BCrypt密码哈希的工具类
 * 运行这个测试可以生成密码的BCrypt哈希值
 */
public class PasswordHashGenerator {

    @Test
    public void generatePasswordHash() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        
        // 生成4次，每次都不同（BCrypt会自动加盐）
        System.out.println("=== BCrypt Hash for password: " + password + " ===");
        for (int i = 0; i < 4; i++) {
            String hash = encoder.encode(password);
            System.out.println("Hash " + (i + 1) + ": " + hash);
        }
        
        // 验证生成的哈希值是否正确
        String testHash = encoder.encode(password);
        System.out.println("\n=== Verification ===");
        System.out.println("Test Hash: " + testHash);
        System.out.println("Matches '123456': " + encoder.matches(password, testHash));
        System.out.println("Matches 'wrong': " + encoder.matches("wrong", testHash));
    }
}
