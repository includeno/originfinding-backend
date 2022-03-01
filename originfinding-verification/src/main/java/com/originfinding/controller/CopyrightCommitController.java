package com.originfinding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.originfinding.entity.CopyrightCommit;
import com.originfinding.entity.User;
import com.originfinding.enums.CopyrightCommitCode;
import com.originfinding.request.CopyrightCommitPageRequest;
import com.originfinding.request.CopyrightCommitRequest;
import com.originfinding.service.sql.CopyrightCommitService;
import com.originfinding.service.sql.UserService;
import com.originfinding.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
public class CopyrightCommitController {
    @Autowired
    Gson gson;

    @Autowired
    CopyrightCommitService copyrightCommitService;

    @Autowired
    UserService userService;

    @PostMapping("/copyrightcommit")
    public R add(CopyrightCommitRequest request){
        if(request==null||request.getUserId()==null||request.getPlatform()==null||request.getPlatformHash()==null||request.getUrl()==null){
            return R.build(CopyrightCommitCode.REQUIRED_INFO_ERROR,null);
        }
        log.warn("CopyrightCommitRequest:"+gson.toJson(request));

        log.info("step 1");
        //一个url只能允许一个有效
        QueryWrapper<CopyrightCommit> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("url",request.getUrl());
        Long count=copyrightCommitService.count(queryWrapper);
        log.info("step 2");
        if(count>0){
            return R.build(CopyrightCommitCode.URL_EXIST,null);
        }
        log.warn("CopyrightCommit.newInstance(request):"+gson.toJson(CopyrightCommit.newInstance(request)));
        boolean sqlresult=copyrightCommitService.save(CopyrightCommit.newInstance(request));
        log.info("step 3");
        if(sqlresult==true){
            return R.build(CopyrightCommitCode.OK,null);
        }
        else{
            return R.build(CopyrightCommitCode.DB_ERROR,null);
        }
    }

    @PutMapping("/copyrightcommit")
    public R update(CopyrightCommitRequest request){
        if(request==null||request.getId()==null||request.getPlatform()==null||request.getPlatformHash()==null||request.getStatus()==null||request.getUrl()==null){
            return R.build(CopyrightCommitCode.REQUIRED_INFO_ERROR,null);
        }
        if(request.getUserId()==null||request.getEmail()==null){
            return R.build(CopyrightCommitCode.EMAIL_ERROR,null);
        }
        else {
            //验证身份 只有用户本人或者管理员才能修改
            QueryWrapper<CopyrightCommit> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",request.getId());
            CopyrightCommit entity=copyrightCommitService.getOne(queryWrapper);
            if(entity==null){
                return R.build(CopyrightCommitCode.RECORD_ERROR,null);
            }
            if(!(request.getUserId().equals(entity.getUserId())||userService.selectRoles(request.getId()).stream().map(a->a.getCode()).filter(code->code.equals("admin")).count()>0)){
                return R.build(CopyrightCommitCode.AUTH_ERROR,null);
            }

            //更新数据
            entity.setPlatform(request.getPlatform());
            entity.setPlatformHash(request.getPlatformHash());
            entity.setComment(request.getComment());
            entity.setUpdateTime(new Date());
            boolean sqlresult=copyrightCommitService.updateById(entity);
            if(sqlresult==true){
                return R.build(CopyrightCommitCode.OK,null);
            }
            else{
                return R.build(CopyrightCommitCode.DB_ERROR,null);
            }
        }
    }

    @DeleteMapping ("/copyrightcommit")
    public R delete(CopyrightCommitRequest request){
        if(request==null||request.getId()==null||request.getUserId()==null||request.getEmail()==null){
            return R.build(CopyrightCommitCode.REQUIRED_INFO_ERROR,null);
        }
        else {
            //验证身份 只有用户本人或者管理员才能修改
            QueryWrapper<CopyrightCommit> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",request.getId());
            CopyrightCommit entity=copyrightCommitService.getOne(queryWrapper);
            if(entity==null){
                return R.build(CopyrightCommitCode.RECORD_ERROR,null);
            }
            if(!(request.getUserId().equals(entity.getUserId())||userService.selectRoles(request.getId()).stream().map(a->a.getCode()).filter(code->code.equals("admin")).count()>0)){
                return R.build(CopyrightCommitCode.AUTH_ERROR,null);
            }
            boolean sqlresult=copyrightCommitService.removeById(entity);
            if(sqlresult==true){
                return R.build(CopyrightCommitCode.OK,null);
            }
            else{
                return R.build(CopyrightCommitCode.DB_ERROR,null);
            }
        }
    }

    @GetMapping("/copyrightcommits/page")
    public R getUserListByPage(CopyrightCommitPageRequest copyrightCommitPageRequest){
        log.info("CopyrightCommitPageRequest:"+gson.toJson(copyrightCommitPageRequest));
        if(copyrightCommitPageRequest==null||copyrightCommitPageRequest.getPage()==null||copyrightCommitPageRequest.getSize()==null){
            copyrightCommitPageRequest=new CopyrightCommitPageRequest();
            copyrightCommitPageRequest.setPage(1);
            copyrightCommitPageRequest.setSize(10);
        }
        Page<CopyrightCommit> userIPage=new Page(copyrightCommitPageRequest.getPage(),copyrightCommitPageRequest.getSize());
        QueryWrapper<CopyrightCommit> queryWrapper=new QueryWrapper<>();
        if(copyrightCommitPageRequest.getUrl()!=null){
            queryWrapper.eq("url",copyrightCommitPageRequest.getUrl());
        }
        //普通用户 绑定userId
        if(copyrightCommitPageRequest.getUserId()!=null){
            queryWrapper.eq("user_id",copyrightCommitPageRequest.getUserId());
        }
        //管理员通过email查询
        else if(copyrightCommitPageRequest.getEmail()!=null){
            QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
            userQueryWrapper.eq("email",copyrightCommitPageRequest.getEmail());
            User user=userService.getOne(userQueryWrapper);
            queryWrapper.eq("user_id",user.getId());
        }
        if(copyrightCommitPageRequest.getStatus()!=null){
            queryWrapper.eq("status",copyrightCommitPageRequest.getStatus());
        }
        if(copyrightCommitPageRequest.getDeleted()!=null){
            queryWrapper.eq("deleted",copyrightCommitPageRequest.getDeleted());
        }

        queryWrapper.orderByDesc("create_time");
        Page<CopyrightCommit> res = copyrightCommitService.page(userIPage, queryWrapper);
        return R.success("OK",res);
    }
}
