package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.ServeManage;
import com.example.demo.mapper.ServeManageMapper;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

@Service
public class ServerManageService extends ServiceImpl<ServeManageMapper, ServeManage> {
    //jsch创建连接
    public Session getJSchSession(String ip , int port, String userName, String password){
        JSch jSch = new JSch();
        Session session = null;
        try {
            //创建连接
            session = jSch.getSession(userName,ip,port);
            session.setPassword(password);
            //是否使用密钥登录，一般默认为no
            session.setConfig("StrictHostKeyChecking", "no");
            //启用连接
            session.connect();
            System.out.println("==============服务器连接成功==============");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("==============服务器连接失败==============");
        }
        return session;
    }

    //jsch关闭连接
    public void closeJSchSession(Session session){
        if (session != null) {
            try {
                session.disconnect();
                System.out.println("===========服务器连接关闭成功===========");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("===========服务器连接关闭失败===========");
            }
        }
    }


}
