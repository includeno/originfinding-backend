package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class RolePermission {

    @TableId(type = IdType.AUTO)
    Integer id;
    Integer roleId;
    Integer permissionId;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    @TableLogic
    Integer deleted;
}
