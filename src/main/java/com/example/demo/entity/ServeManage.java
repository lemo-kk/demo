package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "server_manage")
public class ServeManage {
    @TableId(value = "id",type = IdType.AUTO)
    private  Integer id;
    private  String serveIp  ;
    private  String serveName;
    private  String serveType;
    private  String username;
    private  String password;
    private  String mark;
    private  String newRootPassword;
    private  String deployApps;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServeIp() {
        return serveIp;
    }

    public void setServeIp(String serveIp) {
        this.serveIp = serveIp;
    }

    public String getServeName() {
        return serveName;
    }

    public void setServeName(String serveName) {
        this.serveName = serveName;
    }

    public String getServeType() {
        return serveType;
    }

    public void setServeType(String serveType) {
        this.serveType = serveType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getNewRootPassword() {
        return newRootPassword;
    }

    public void setNewRootPassword(String newRootPassword) {
        this.newRootPassword = newRootPassword;
    }

    public String getDeployApps() {
        return deployApps;
    }

    public void setDeployApps(String deployApps) {
        this.deployApps = deployApps;
    }
}
