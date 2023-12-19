package com.example.demo.controller.dto;
import lombok.Data;

import java.awt.*;
import java.util.List;


//UserDTO用来接受前端登录时传递的用户名和密码
@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String token;
    //把当前登录用户的角色以及他的菜单项带出来
    private String role;
    private List<Menu> menus;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}
