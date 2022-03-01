package com.originfinding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.originfinding.entity.Role;
import com.originfinding.entity.User;
import com.originfinding.enums.LoginCode;
import com.originfinding.enums.RegisterCode;
import com.originfinding.request.UserLoginRequest;
import com.originfinding.request.UserPageRequest;
import com.originfinding.request.UserRegisterRequest;
import com.originfinding.response.UserLoginResponse;
import com.originfinding.service.logic.UserLogicService;
import com.originfinding.service.sql.PermissionService;
import com.originfinding.service.sql.RoleService;
import com.originfinding.service.sql.UserRoleService;
import com.originfinding.service.sql.UserService;
import com.originfinding.util.R;
import com.originfinding.vo.RolePermissionVo;
import com.originfinding.vo.UserRoleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserLogicService userLogicService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    Gson gson;

    @PostMapping("/user/login")
    public R login(UserLoginRequest loginRequest){
        log.info("UserLoginRequest: "+gson.toJson(loginRequest));
        String email=loginRequest.getEmail();
        String password=loginRequest.getPassword();
        LoginCode code = userLogicService.login(email, password);
        if(code.equals(LoginCode.EMAIL_DOESNT_EXIST)||code.equals(LoginCode.PASSWORD_ERROR)){
            return R.error("用户名或密码错误",null);
        }
        else if(code.equals(LoginCode.DB_ERROR)){
            return R.error(code.getMessage(),null);
        }
        else{
            //关联查询所有user、roles、permissions
            UserLoginResponse response=new UserLoginResponse();

            QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
            userQueryWrapper.eq("email",email);
            User user=userService.getOne(userQueryWrapper);
            response.setUser(user);

            List<UserRoleVo> roles=userService.selectRoles(user.getId());
            response.setRoles(roles);
            List<Integer> ids=roles.stream().map(t->t.getId()).collect(Collectors.toList());
            if(ids.size()==0){
                ids=new ArrayList<>();
                List<RolePermissionVo> permissions=new ArrayList<>();
                response.setPermissions(permissions);
            }
            else{
                List<RolePermissionVo> permissions = userService.selectPermissions(ids);
                response.setPermissions(permissions);
            }
            response.setToken("Authorization:"+Math.random()*3000);
            response.setExpireAt(new Date());

            return R.success(code.getMessage(),response);
        }
    }

    @PostMapping("/user/register")
    public R register(UserRegisterRequest userRegisterRequest){
        RegisterCode code= userLogicService.register(userRegisterRequest.getUsername(), userRegisterRequest.getPassword(), userRegisterRequest.getEmail(), userRegisterRequest.getRoleId());
        if(code.equals(RegisterCode.EMAIL_EXISTS)||code.equals(RegisterCode.DB_ERROR)){
            return R.error(code.getMessage(),null);
        }
        return R.success(code.getMessage(), null);
    }

    @PostMapping("/user/registercommon")
    public R registercommon(UserRegisterRequest userRegisterRequest){
        QueryWrapper<Role> roleQueryWrapper=new QueryWrapper<Role>();
        roleQueryWrapper.eq("rolename","普通用户");
        Role role=roleService.getOne(roleQueryWrapper);
        RegisterCode code= userLogicService.register(userRegisterRequest.getUsername(), userRegisterRequest.getPassword(), userRegisterRequest.getEmail(), role.getRoleId());
        if(code.equals(RegisterCode.EMAIL_EXISTS)||code.equals(RegisterCode.DB_ERROR)){
            return R.error(code.getMessage(),null);
        }
        return R.success(code.getMessage(), null);
    }

    @PutMapping("/user")
    public R updateUser(User user){
        boolean updateResult=userService.updateById(user);
        if(updateResult==true){
            return R.success("ok",true);
        }
        else {
            log.error("updateUser db_error");
            return R.error("db_error",false);
        }
    }

    @DeleteMapping ("/user")
    public R deleteUser(User user){
        boolean deleteResult=userService.removeById(user);
        if(deleteResult==true){
            return R.success("ok",true);
        }
        else {
            log.error("deleteUser db_error");
            return R.error("db_error",false);
        }
    }

    @GetMapping("/users/page")
    public R getUserListByPage(UserPageRequest userPageRequest){
        log.info("userPageRequest:"+gson.toJson(userPageRequest));
        if(userPageRequest==null||userPageRequest.getPage()==null||userPageRequest.getSize()==null){
            userPageRequest=new UserPageRequest();
            userPageRequest.setPage(1);
            userPageRequest.setSize(10);
        }
        Page<User> userIPage=new Page(userPageRequest.getPage(),userPageRequest.getSize());
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if(userPageRequest.getUsername()!=null){
            queryWrapper.eq("username",userPageRequest.getUsername());
        }
        if(userPageRequest.getEmail()!=null){
            queryWrapper.eq("email",userPageRequest.getEmail());
        }
        queryWrapper.orderByDesc("create_time");
        Page<User> res = userService.page(userIPage, queryWrapper);
        return R.success("OK",res);
    }

    @GetMapping("/users/roles")
    public List<UserRoleVo> getAllRoles(Integer id){
        return userService.selectRoles(id);
    }

    @GetMapping("/users/permissions")
    public List<RolePermissionVo> getAllPermissions(Integer id){
        List<UserRoleVo> userRoleVoList=userService.selectRoles(id);

        List<Integer> ids=userRoleVoList.stream().map(t->t.getId()).collect(Collectors.toList());
        return userService.selectPermissions(ids);
    }
}
