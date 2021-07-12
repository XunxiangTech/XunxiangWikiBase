package com.xunxiang.xunxiangwikibase.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Value("${welcome.hello:TEST}") //优先读配置项，没有就读：后的默认值
    private String welcome;

    @GetMapping({"/","/index"})
    public String welcome(){
        return welcome;
    }
}
