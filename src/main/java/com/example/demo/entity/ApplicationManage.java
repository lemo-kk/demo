package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "application_manage")
public class ApplicationManage {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Long serveManageId ;
    private String applicationName;
    private String applicationType;
    private String applicationPath;
    private String applicationRestart;
    private String applicationMark;
    private String applicationLogPath;



}
