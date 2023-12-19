package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.ServerConnectionInfo;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/ssh")
public class SSHController {

    @PostMapping("/connect")
    public boolean connectToServer(@RequestBody ServerConnectionInfo info) {

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(info.getUsername(), info.getServeIp(), info.getLoginPort());
            session.setPassword(info.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
/*
            // 执行远程命令
            String command = "df -h"; // 例如，打印磁盘空间信息
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // 获取结果
            InputStream in = channel.getInputStream();
            channel.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            // 断开连接
            channel.disconnect();

 */
            session.disconnect();
            System.out.println("true");
            return true;
        } catch (Exception e) {

            System.out.println("false,"+e.getMessage()+info.getServeIp()+","+info.getLoginPort());
            return false;
        }
    }

}
