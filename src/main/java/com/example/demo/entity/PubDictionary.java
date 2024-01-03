package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "pub_dictionary")
public class PubDictionary {
    @TableId(value = "id",type = IdType.AUTO)
    private  Integer id;
    private Integer type_id;
    private String type_name;
    private String name;

}
