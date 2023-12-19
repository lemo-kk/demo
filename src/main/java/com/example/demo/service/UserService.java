package com.example.demo.service;
import cn.hutool.core.bean.BeanUtil;
import com.example.demo.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Constants;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;



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
    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());
        System.out.println(queryWrapper);
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");//这里假设查询了多于1条记录，就让他报系统错误
        }
        if(one!=null){  //以下是登录判断业务
            BeanUtil.copyProperties(one,userDTO,true);
            //设置token
            String token= TokenUtils.genToken(one.getId().toString(),one.getPassword().toString());
            userDTO.setToken(token);
            return userDTO;//返回登录类userDTO
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

}

