package com.originfinding.service.sql;

import com.baomidou.mybatisplus.extension.service.IService;
import com.originfinding.entity.User;
import com.originfinding.enums.UserCode;

public interface UserService extends IService<User> {

    //创建
    public UserCode createNewUser(String username, String password, String email);
    public UserCode createNewAuditor(String username,String password,String email);
    public UserCode createNewAdmin(String username,String password,String email);

    //查看
    public boolean isNameAvaible(String username);
    public boolean isEmailAvaible(String username);
    //修改

    //逻辑删除
}
