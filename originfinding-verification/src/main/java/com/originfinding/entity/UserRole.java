package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class UserRole {

    @TableId(type = IdType.AUTO)
    Integer id;
    Integer userId;
    Integer roleId;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    @TableLogic
    Integer deleted;
}
