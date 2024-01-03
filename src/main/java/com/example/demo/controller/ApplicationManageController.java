package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.ApplicationManage;
import com.example.demo.entity.PubDictionary;
import com.example.demo.entity.ServeManage;
import com.example.demo.service.ApplicationManageService;
import com.example.demo.service.PubDictionaryService;
import com.example.demo.service.ServerManageService;
import com.example.demo.utils.JSchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ApplicationManager")
public class ApplicationManageController {
    @Autowired
    private ApplicationManageService applicationManageService;
    @Autowired
    private ServerManageService serverManageService;
    @Autowired
    private PubDictionaryService pubDictionaryService;
    @Autowired
    private JSchUtil jSchUtil;

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return  applicationManageService.removeById(id);
    }
    @PostMapping("/del/batch/")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        System.out.println(ids);

        return applicationManageService.removeByIds(ids);
    }
    @GetMapping("/ipList")
    public List<Map<String, Object>> findIpList() {
        QueryWrapper<ServeManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "serve_ip");
        return serverManageService.listMaps(queryWrapper);
    }
    @GetMapping("/applicationTypeList")
    public List<Map<String, Object>> findApplicationTypeList() {
        QueryWrapper<PubDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id","yylx");
        queryWrapper.select("id", "name");
        return pubDictionaryService.listMaps(queryWrapper);
    }
    @PostMapping("/executeRestartCommand")
    public Result executeRestartCommand(@RequestBody Map<String,Object> requestBody){
        Integer serveManageId=(Integer) requestBody.get("serveManageId");
        String applicationPostPath=(String) requestBody.get("applicationPostPath");
        String cdApplicationPostPath="cd "+applicationPostPath;
        String applicationPostCmd=(String) requestBody.get("applicationPostCmd");

        //根据serveManageId查询服务器信息
        ServeManage serve = serverManageService.getById(serveManageId);
        if (serve==null){
            return Result.error("未找到服务器信息，服务器ID："+serveManageId);
        }
        System.out.println("serve."+serve);
        try {
            //调用SSH连接到服务器并执行命令
            jSchUtil.loginLinux(serve.getUsername(),serve.getPassword(),serve.getServeIp(),serve.getLoginPort());
            String result11=jSchUtil.executeCommand("locale charmap");
            System.out.println(result11);
            String result=jSchUtil.execCmdByShell(Arrays.asList(cdApplicationPostPath,applicationPostCmd));
            jSchUtil.closeAll();
            String convertedOutput = jSchUtil.convertToUTF8(result, result11);
            System.out.println(convertedOutput);
            return Result.success(convertedOutput);
        }catch (Exception e){
            String message=e.getMessage();
            return Result.error(message);
        }
    }


    @GetMapping("/page")
    public IPage<ApplicationManage> findPage(@RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize,
                                             @RequestParam(defaultValue = "") String serveManageId,
                                             @RequestParam(defaultValue = "") String applicationName,
                                             @RequestParam(defaultValue = "") String applicationType) {
        IPage<ApplicationManage> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ApplicationManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(serveManageId), "serve_manage_id", serveManageId);
        queryWrapper.like(StringUtils.isNotBlank(applicationName), "application_name", applicationName);
        queryWrapper.like(StringUtils.isNotBlank(applicationType), "application_type", applicationType);
        return applicationManageService.page(page, queryWrapper);

    }

    @PostMapping
    public Result addApplicationManageData(@RequestBody Map<String, Object> requestBody) {

        Object serveManageIdd = requestBody.getOrDefault("serveManageId", null);
        Integer id = (Integer) requestBody.get("id");
        String applicationName = (String) requestBody.get("applicationName");
        String applicationType = (String) requestBody.get("applicationType");
        String applicationPath = (String) requestBody.get("applicationPath");
        String applicationRestart = (String) requestBody.get("applicationRestart");
        String applicationMark = (String) requestBody.get("applicationMark");
        String applicationLogPath = (String) requestBody.get("applicationLogPath");

        List<Integer> serveManageIds = new ArrayList<>();

        if (serveManageIdd != null) {
            if (serveManageIdd instanceof List) {
                serveManageIds = (List<Integer>) serveManageIdd;
            } else if (serveManageIdd instanceof Integer) {
                serveManageIds.add((Integer) serveManageIdd);
            }
        }

        try {
            for (Integer serveManageId : serveManageIds) {
                ApplicationManage applicationManage = new ApplicationManage();
                applicationManage.setServeManageId(serveManageId.longValue());
                applicationManage.setId(id);
                applicationManage.setApplicationName(applicationName);
                applicationManage.setApplicationType(applicationType);
                applicationManage.setApplicationPath(applicationPath);
                applicationManage.setApplicationRestart(applicationRestart);
                applicationManage.setApplicationMark(applicationMark);
                applicationManage.setApplicationLogPath(applicationLogPath);
                System.out.println(applicationManage);
                applicationManageService.SaveApplicationManage(applicationManage);
            }

            return Result.success();
        } catch (Exception e) {
            String data = e.getMessage();
            System.out.println(data);
            return Result.error(data);
        }
    }
}
