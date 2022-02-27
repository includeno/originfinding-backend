package com.originfinding.service.sql;

import com.baomidou.mybatisplus.extension.service.IService;
import com.originfinding.entity.User;
import com.originfinding.vo.RolePermissionVo;
import com.originfinding.vo.UserRoleVo;

import java.util.List;

public interface UserService extends IService<User> {

    public List<UserRoleVo> selectRoles(Integer id);

    public List<RolePermissionVo> selectPermissions(List<Integer> roleIds);
}
