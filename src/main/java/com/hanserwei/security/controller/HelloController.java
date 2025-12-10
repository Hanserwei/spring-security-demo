package com.hanserwei.security.controller;

@RestController
public class HelloController {
 
    @GetMapping("/hello")
    public String hello(){
 
        return "晓凡，你好！";
    }
}