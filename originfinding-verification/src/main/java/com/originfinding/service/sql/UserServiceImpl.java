package com.originfinding.service.sql;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.originfinding.entity.User;
import com.originfinding.enums.RoleCode;
import com.originfinding.enums.UserCode;
import com.originfinding.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {

    private UserCode register(User user){
        String username=user.getUsername();
        String email=user.getEmail();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        int usernameCount=this.count(queryWrapper);
        if(usernameCount>0){
            //已被人注册，无法注册成功
            return UserCode.USER_NAME_EXISTS;
        }

        queryWrapper=new QueryWrapper();
        queryWrapper.eq("email",email);
        int emailCount=this.count(queryWrapper);
        if(emailCount>0){
            //已被人注册，无法注册成功
            return UserCode.EMAIL_EXISTS;
        }

        boolean result=this.save(user);
        if(result==true){
            return UserCode.OK;
        }
        else {
            return UserCode.DB_ERROR;
        }

    }

    @Override
    public UserCode createNewUser(String username, String password, String email) {
        User user=new User();
        user.setRole(RoleCode.User.getCode());
        return this.register(user);
    }

    @Override
    public UserCode createNewAuditor(String username, String password, String email) {
        User user=new User();
        user.setRole(RoleCode.Auditor.getCode());
        return this.register(user);
    }

    @Override
    public UserCode createNewAdmin(String username, String password, String email) {
        User user=new User();
        user.setRole(RoleCode.Admin.getCode());
        return this.register(user);
    }

    @Override
    public boolean isNameAvaible(String username) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        int count=this.count(queryWrapper);
        return count>0;
    }

    @Override
    public boolean isEmailAvaible(String email) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("email",email);
        int count=this.count(queryWrapper);
        return count>0;
    }
}
