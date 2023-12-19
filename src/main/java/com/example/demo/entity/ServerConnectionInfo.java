package com.example.demo.entity;

public class ServerConnectionInfo {
    private String serveIp;
    private String username;
    private String password;
    private int loginPort;

    public String getServeIp() {
        return serveIp;
    }

    public void setServeIp(String serveIp) {
        this.serveIp = serveIp;
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

    public int getLoginPort() {
        return loginPort;
    }

    public void setLoginPort(int loginPort) {
        this.loginPort = loginPort;
    }
}
