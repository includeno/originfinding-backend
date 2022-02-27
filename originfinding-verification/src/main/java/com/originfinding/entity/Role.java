package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class Role {
    @TableId(type = IdType.AUTO)
    Integer roleId;
    String code;
    String rolename;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    @TableLogic
    Integer deleted;
}
