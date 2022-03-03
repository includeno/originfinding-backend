package com.originfinding.service.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.originfinding.entity.User;
import com.originfinding.entity.UserRole;
import com.originfinding.enums.LoginCode;
import com.originfinding.enums.RegisterCode;
import com.originfinding.service.sql.RoleService;
import com.originfinding.service.sql.UserRoleService;
import com.originfinding.service.sql.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class UserLogicServiceImpl implements UserLogicService{

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleService userRoleService;

    @Transactional
    @Override
    public RegisterCode register(String username, String password, String email,Integer roleId) {
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setDeleted(0);
        Date time = new Date();
        user.setCreateTime(time);
        user.setUpdateTime(time);

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("email",email);
        long emailCount=userService.count(queryWrapper);
        if(emailCount>0){
            //email已被人注册，无法注册成功
            return RegisterCode.EMAIL_EXISTS;
        }
        //保存用户基础信息
        boolean userSaveResult=userService.save(user);
        log.warn("save user:"+userSaveResult);

        if(userSaveResult==false){
            return RegisterCode.DB_ERROR;
        }
        //查询刚刚保存的用户id

        QueryWrapper userqueryWrapper=new QueryWrapper();
        userqueryWrapper.eq("email",email);
        userqueryWrapper.eq("deleted",0);
        User cur=userService.getOne(userqueryWrapper);

        UserRole userRole=new UserRole();
        userRole.setUserId(cur.getId());
        userRole.setRoleId(roleId);

        userRole.setCreateTime(time);
        userRole.setUpdateTime(time);
        userRole.setDeleted(0);

        //关联 用户-角色
        boolean userRoleSaveResult=userRoleService.save(userRole);
        if(userSaveResult==true&&userRoleSaveResult==true){
            return RegisterCode.OK;
        }
        else {
            return RegisterCode.DB_ERROR;
        }
    }

    @Override
    public boolean isNameAvaible(String username) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        long count=userService.count(queryWrapper);
        return count>0;
    }

    @Override
    public boolean isEmailAvaible(String email) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("email",email);
        long count=userService.count(queryWrapper);
        return count>0;
    }

    @Override
    public LoginCode login(String email, String password) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("email",email);
        User user=userService.getOne(queryWrapper);
        if(user==null){
            return LoginCode.EMAIL_DOESNT_EXIST;
        }
        if(user.getEmail().equals(email)&&user.getPassword().equals(password)){
            return LoginCode.OK;
        }
        return LoginCode.PASSWORD_ERROR;
    }
}
