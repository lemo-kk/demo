package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    /*
    @Autowired
    private  UserMapper userMapper;
    //分页查询
    //接口路径user/page?pageNum=1&pageSize=10
    //RequestParam接受前台传过来的第几页，每页显示数
    @GetMapping("/page")
    public Map<String,Object> findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String userName) {
        pageNum = (pageNum - 1) * pageSize;
        List<User> data = userService.selectPage(pageNum, pageSize,userName);
        Integer total = userMapper.selectTotal(userName);
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

     */
    @PostMapping
    //使用mybtis-plus，返回boolean型
    public Boolean save(@RequestBody User user)
    {
        return userService.saveUser(user);
    }
    @GetMapping
    public List<User> findAll(){
        return userService.list();
    }
    //使用mybtis-plus实现删除
    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return  userService.removeById(id);
    }
    @GetMapping("/{id}")
    public User findOne(@PathVariable Integer id){
        return  userService.getById(id);
    }
    //使用mybtis-plus实现模糊查询并分页
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,
                                @RequestParam(defaultValue = "") String nickname,
                                @RequestParam(defaultValue = "") String address){
        IPage<User> page=new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username),"username",username);
        queryWrapper.like(StringUtils.isNotBlank(nickname),"nickname",nickname);
        queryWrapper.like(StringUtils.isNotBlank(address),"address",address);
        return userService.page(page,queryWrapper);
    }
    //使用mybtis-plus实现批量删除
    @PostMapping("/del/batch/")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        System.out.println(ids);

        return userService.removeByIds(ids);
    }
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        System.out.println(userDTO);
        String username=userDTO.getUsername();//先对userDTO进行是否为空的校验
        String password=userDTO.getPassword();
        System.out.println(username+","+password);
        //调用hutool工具中的StrUtil函数实现用户名和密码是否为空的判断
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto=userService.login(userDTO);
        return Result.success(dto);
    }







}

