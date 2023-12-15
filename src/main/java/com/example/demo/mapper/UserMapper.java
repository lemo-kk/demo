package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
@Mapper
public interface UserMapper {
        @Select("select * from sys_user   where username like #{userName} limit  #{pageNum},#{pageSize}")
        List<User> selectPage(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,@Param("userName") String  userName);
        @Select("select count(*) from sys_user where username like #{userName}")
        Integer selectTotal(String userName);

}
*/
public interface UserMapper extends BaseMapper<User>{

}
