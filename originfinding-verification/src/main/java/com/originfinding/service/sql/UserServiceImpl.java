package com.originfinding.service.sql;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.originfinding.entity.User;
import com.originfinding.mapper.UserMapper;
import com.originfinding.vo.RolePermissionVo;
import com.originfinding.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {
    @Autowired
    UserMapper userMapper;

    @Override
    public List<UserRoleVo> selectRoles(Integer id) {
        return userMapper.selectRoles(id);
    }

    @Override
    public List<RolePermissionVo> selectPermissions(List<Integer> roleIds) {
        return userMapper.selectPermissions(roleIds);
    }


}
