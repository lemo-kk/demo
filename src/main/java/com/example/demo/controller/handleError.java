package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;



public class handleError {
    @RequestMapping("/error")
    public String handleError() {
        // 处理错误请求的逻辑
        return "error-page"; // 返回一个错误页面或者错误信息
    }
}
