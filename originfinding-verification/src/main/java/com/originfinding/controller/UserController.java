package com.originfinding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.originfinding.entity.Role;
import com.originfinding.entity.User;
import com.originfinding.entity.UserRole;
import com.originfinding.enums.LoginCode;
import com.originfinding.enums.RegisterCode;
import com.originfinding.enums.UserCode;
import com.originfinding.request.UserLoginRequest;
import com.originfinding.request.UserPageRequest;
import com.originfinding.request.UserRegisterRequest;
import com.originfinding.request.UserUpdateRequest;
import com.originfinding.response.UserInfoResponse;
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
    Gson gson;

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

    @PostMapping("/user/login")
    public R login(UserLoginRequest loginRequest){
        log.info("UserLoginRequest: "+gson.toJson(loginRequest));
        String email=loginRequest.getEmail();
        String password=loginRequest.getPassword();
        LoginCode code = userLogicService.login(email, password);
        if(code.equals(LoginCode.EMAIL_DOESNT_EXIST)||code.equals(LoginCode.PASSWORD_ERROR)){
            return R.error("????????????????????????",null);
        }
        else if(code.equals(LoginCode.DB_ERROR)){
            return R.error(code.getMessage(),null);
        }
        else{
            //??????????????????user???roles???permissions
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
        if(userRegisterRequest.getRoleId()==null||userRegisterRequest.getRoleId().equals(-1)){
            QueryWrapper<Role> roleQueryWrapper=new QueryWrapper<Role>();
            roleQueryWrapper.eq("rolename","????????????");
            Role role=roleService.getOne(roleQueryWrapper);
            userRegisterRequest.setRoleId(role.getRoleId());
        }
        RegisterCode code= userLogicService.register(userRegisterRequest.getUsername(), userRegisterRequest.getPassword(), userRegisterRequest.getEmail(), userRegisterRequest.getRoleId());
        if(code.equals(RegisterCode.EMAIL_EXISTS)||code.equals(RegisterCode.DB_ERROR)){
            return R.error(code.getMessage(),null);
        }
        return R.success(code.getMessage(), null);
    }

    @PostMapping("/user/registercommon")
    public R registercommon(UserRegisterRequest userRegisterRequest){
        QueryWrapper<Role> roleQueryWrapper=new QueryWrapper<Role>();
        roleQueryWrapper.eq("rolename","????????????");
        Role role=roleService.getOne(roleQueryWrapper);
        RegisterCode code= userLogicService.register(userRegisterRequest.getUsername(), userRegisterRequest.getPassword(), userRegisterRequest.getEmail(), role.getRoleId());
        if(code.equals(RegisterCode.EMAIL_EXISTS)||code.equals(RegisterCode.DB_ERROR)){
            return R.error(code.getMessage(),null);
        }
        return R.success(code.getMessage(), null);
    }

    @GetMapping("/user")
    public R getUser(Integer id){
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("id",id);
        userQueryWrapper.ge("deleted",-2);
        User user=userService.getOne(userQueryWrapper);

        List<UserRoleVo> roles=userService.selectRoles(user.getId());

        UserInfoResponse response=new UserInfoResponse();
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setDeleted(user.getDeleted());
        response.setCreateTime(user.getCreateTime());
        response.setUpdateTime(user.getUpdateTime());
        response.setRoles(roles);
        if(user!=null){
            return R.build(UserCode.OK,response);
        }
        else{
            return R.build(UserCode.DB_ERROR,null);
        }
    }

    //id,username, password,email,roleId
    @PutMapping("/user")
    public R updateUser(UserUpdateRequest request){
        if(request==null||request.getId()==null){
            return R.build(UserCode.REQUIRED_INFO_ERROR,false);
        }
        User user=userService.getById(request.getId());
        user.setUpdateTime(new Date());
        if(request.getUsername()!=null){
            user.setUsername(request.getUsername());
        }
        if(request.getPassword()!=null){
            user.setPassword(request.getPassword());
        }
        if(request.getPassword()!=null){
            user.setPassword(request.getPassword());
        }
        if(request.getRoleId()!=null){
            QueryWrapper<UserRole> userRoleQueryWrapper=new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_id",request.getId());
            UserRole userRole=userRoleService.getOne(userRoleQueryWrapper);
            userRoleService.updateById(userRole);
        }
        boolean updateResult=userService.updateById(user);
        if(updateResult==true){
            return R.build(UserCode.OK,true);
        }
        else {
            return R.build(UserCode.UPDATE_DB_ERROR,false);
        }
    }

    @DeleteMapping ("/user")
    public R deleteUser(User user){
        if(user.getId()==null){
            return R.build(UserCode.REQUIRED_INFO_ERROR,null);
        }
        boolean deleteResult=userService.removeById(user);
        if(deleteResult==true){
            return R.build(UserCode.OK,true);
        }
        else {
            return R.build(UserCode.DELETE_DB_ERROR,false);
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
        if(userPageRequest.getDeleted()!=null){
            queryWrapper.eq("deleted",userPageRequest.getDeleted());
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
