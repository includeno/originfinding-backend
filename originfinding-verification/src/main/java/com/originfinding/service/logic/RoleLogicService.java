package com.originfinding.service.logic;

public interface RoleLogicService {

    //增加角色
    public boolean addRole(String code,String name);

    //删除角色
    public boolean deleteRole(String code);
}
