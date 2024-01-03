package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "server_manage")
public class ServerConnectionInfo {
    private String serveIp;
    private String username;
    private String password;
    private int loginPort;


}
