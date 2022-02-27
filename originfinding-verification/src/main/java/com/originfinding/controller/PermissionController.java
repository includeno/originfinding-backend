package com.originfinding.controller;

import com.google.gson.Gson;
import com.originfinding.entity.Permission;
import com.originfinding.service.sql.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @Autowired
    Gson gson;

    @GetMapping("/permission")
    public Permission getPermission(Integer id){
        return permissionService.getById(id);
    }

    @PutMapping("/permission")
    public boolean updatePermission(Permission permission,Integer id){
        Permission temp=permissionService.getById(id);

        if(permission.getPermissionname()!=null){
            temp.setPermissionname(permission.getPermissionname());
        }
        if(permission.getOperation()!=null){
            String operations=permission.getOperation();
            List<String> op=new ArrayList<>();
            for(String t:operations.split(",")){
                op.add(t);
            }
            temp.setOperation(gson.toJson(op));
        }
        return permissionService.updateById(temp);
    }
}
