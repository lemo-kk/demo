package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.PubDictionary;
import com.example.demo.entity.ServeManage;
import com.example.demo.entity.ServerConnectionInfo;
import com.example.demo.service.PubDictionaryService;
import com.example.demo.service.ServerManageService;
import com.example.demo.utils.JSchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/serverManage")
public class ServerManageController {
    @Autowired
    private ServerManageService serverManageService;
    @Autowired
    private JSchUtil jSchUtil;
    @Autowired
    private PubDictionaryService pubDictionaryService;
    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return  serverManageService.removeById(id);
    }
    @PostMapping("/del/batch/")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        System.out.println(ids);

        return serverManageService.removeByIds(ids);
    }
    @PostMapping("/sshConnect")
    public Result connectToServer(@RequestBody ServerConnectionInfo info) {
        try {
            jSchUtil.loginLinux(info.getUsername(),info.getPassword(),info.getServeIp(), info.getLoginPort());
            jSchUtil.closeAll();
            return  Result.success();
        }catch (Exception e){
            String message=e.getMessage();
            return Result.error(message);
        }
    }

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
    @GetMapping("/severTypeList")
    public List<Map<String, Object>> findApplicationTypeList() {
        QueryWrapper<PubDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id","fwqlx");
        queryWrapper.select("id", "name");
        return pubDictionaryService.listMaps(queryWrapper);
    }


}
