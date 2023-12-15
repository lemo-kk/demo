package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper,User> {
    public Boolean saveUser(User user){
        return saveOrUpdate(user);

    }
    /*
    @Autowired
    private UserMapper userMapper;
    public List selectPage(Integer pageNum, Integer pageSize,String userName) {
        return userMapper.selectPage(pageNum,pageSize,"%"+userName+"%");
    }
    */



}
