package com.originfinding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.originfinding.entity.User;
import com.originfinding.vo.RolePermissionVo;
import com.originfinding.vo.UserRoleVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper extends BaseMapper<User> {

    public List<UserRoleVo> selectRoles(Integer id);

    public List<RolePermissionVo> selectPermissions(List<Integer> roleIds);

}
