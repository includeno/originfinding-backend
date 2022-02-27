package com.originfinding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.originfinding.entity.Role;
import com.originfinding.enums.RoleCode;
import com.originfinding.service.sql.RoleService;
import com.originfinding.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/roles")
    public R getRoleList(){
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        List<Role> list = roleService.list(queryWrapper);
        return R.success(RoleCode.OK.getMessage(),list);
    }

    @GetMapping("/role")
    public R getRole(String rolename){
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("rolename",rolename);//查询所有未删除的角色
        Role role=roleService.getOne(queryWrapper);
        return R.success(RoleCode.OK.getMessage(),role);
    }
}
