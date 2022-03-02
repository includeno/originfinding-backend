package com.originfinding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.originfinding.entity.Copyright;
import com.originfinding.entity.User;
import com.originfinding.enums.CopyrightCode;
import com.originfinding.request.CopyrightPageRequest;
import com.originfinding.service.sql.CopyrightService;
import com.originfinding.service.sql.UserService;
import com.originfinding.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CopyrightController {
    @Autowired
    Gson gson;

    @Autowired
    UserService userService;

    @Autowired
    CopyrightService copyrightService;

    @GetMapping("/copyrights/page")
    public R getCopyRightListByPage(CopyrightPageRequest copyrightPageRequest){
        if(copyrightPageRequest==null||copyrightPageRequest.getPage()==null||copyrightPageRequest.getSize()==null){
            copyrightPageRequest=new CopyrightPageRequest();
            copyrightPageRequest.setPage(1);
            copyrightPageRequest.setSize(10);
        }
        Page<Copyright> copyrightIPage=new Page(copyrightPageRequest.getPage(),copyrightPageRequest.getSize());
        QueryWrapper<Copyright> queryWrapper=new QueryWrapper<>();
        if(copyrightPageRequest.getUrl()!=null){
            queryWrapper.eq("url",copyrightPageRequest.getUrl());
        }
        //查询审核员id
        if(copyrightPageRequest.getAuditId()!=null){
            queryWrapper.eq("audit_id",copyrightPageRequest.getAuditId());
        }
        //查询审核员邮箱
        else if(copyrightPageRequest.getEmail()!=null){
            QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
            userQueryWrapper.eq("email",copyrightPageRequest.getEmail());
            User user=userService.getOne(userQueryWrapper);
            if(user!=null){
                queryWrapper.eq("audit_id",user.getId());
            }
        }
        if(copyrightPageRequest.getStatus()!=null){
            //查询所有建立关联的记录 >=0
            if(copyrightPageRequest.getStatus()>0){
                queryWrapper.ge("request_id",0);
            }
            //查询所有未建立关联的记录 <=-1
            else {
                queryWrapper.le("request_id",-1);
            }
        }
        queryWrapper.orderByDesc("create_time");

        Page<Copyright> res = copyrightService.page(copyrightIPage, queryWrapper);
        if(res!=null){
            return R.build(CopyrightCode.OK,res);
        }
        else{
            return R.build(CopyrightCode.DB_ERROR,res);
        }
    }
}
