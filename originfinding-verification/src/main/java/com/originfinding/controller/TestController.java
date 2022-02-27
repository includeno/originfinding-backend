package com.originfinding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.entity.Role;
import com.originfinding.entity.User;
import com.originfinding.service.sql.RoleService;
import com.originfinding.service.sql.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    Gson gson;

    @GetMapping("/getuser")
    public User getOne(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("email","123");
        User user=userService.getOne(queryWrapper);
        log.warn(gson.toJson(user));

        List<User> ans2 = userService.list(queryWrapper);
        ans2.forEach(System.out::println);

        System.out.println("1111");
        userService.getBaseMapper().selectList(queryWrapper).forEach(System.out::println);
        return user;
    }

    @GetMapping("/getrole")
    public Role getRole(){
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("role_id",1);
        Role role=roleService.getOne(queryWrapper);
        System.out.println(role);

        return role;
    }
}
