package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.ServeManage;
import com.example.demo.entity.User;
import com.example.demo.mapper.ServeManageMapper;
import com.example.demo.service.ServerManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serverManage")
public class ServerManageController {
    @Autowired
    private ServerManageService serverManageService;
    @PostMapping
    public Boolean save(@RequestBody ServeManage serveManage){
        return serverManageService.saveServerManage(serveManage);
    }
    @GetMapping("/page")
    public IPage<ServeManage> findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam(defaultValue = "") String serveIp,
                                       @RequestParam(defaultValue = "") String serveName,
                                       @RequestParam(defaultValue = "") String serveType){
        IPage<ServeManage> page=new Page<>(pageNum,pageSize);
        QueryWrapper<ServeManage> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(serveIp),"serve_ip",serveIp);
        queryWrapper.like(StringUtils.isNotBlank(serveName),"serve_name",serveName);
        queryWrapper.like(StringUtils.isNotBlank(serveType),"serve_type",serveType);
        return serverManageService.page(page,queryWrapper);

    }


}
