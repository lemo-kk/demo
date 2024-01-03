package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "server_manage")
public class ServeManage {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String serveIp;
    private String serveName;
    private String serveType;
    private String username;
    private String password;
    private String mark;
    private String newRootPassword;
    private String deployApps;
    private Integer loginPort;
}


