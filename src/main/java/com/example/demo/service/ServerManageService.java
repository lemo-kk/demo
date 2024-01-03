package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.ServeManage;
import com.example.demo.mapper.ApplicationManageMapper;
import com.example.demo.mapper.ServeManageMapper;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ServerManageService extends ServiceImpl<ServeManageMapper, ServeManage> {
    public  boolean saveServerManage(ServeManage serveManage){
        return  saveOrUpdate(serveManage);
    }

}
