package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Websocket")
public class WebSocketController {
    @Autowired
    private WebSocketService webSocketService;//注入websocket服务

    @PostMapping("/ExecuteRestartCommand")
    public Result ExecuteRestartCommand(){
        return Result.success();
    }

}
