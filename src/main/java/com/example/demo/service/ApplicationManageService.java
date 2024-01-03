package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Result;
import com.example.demo.entity.ApplicationManage;
import com.example.demo.mapper.ApplicationManageMapper;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class ApplicationManageService extends ServiceImpl<ApplicationManageMapper,ApplicationManage> {
    WebSocketService webSocketService;
    public boolean SaveApplicationManage(ApplicationManage applicationManage){

        return saveOrUpdate(applicationManage);
    }

}
