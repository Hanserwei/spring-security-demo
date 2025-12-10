package com.hanserwei.security.config;

import com.hanserwei.security.exception.GlobalExceptionHandler;
import com.hanserwei.security.service.UserDetailServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Resource
    private UserDetailServiceImpl userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 配置角色访问控制信息
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CORS
                .cors(AbstractHttpConfigurer::disable)
                // 配置请求授权
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                // 配置异常处理
                .exceptionHandling(e -> e.accessDeniedHandler(new GlobalExceptionHandler()))
                // 配置表单登录
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/hello", true)
                        .permitAll()
                )
                // 配置退出登录
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // 配置自定义的 UserDetailsService
                .userDetailsService(userDetailService);
        
        return http.build();
    }
}