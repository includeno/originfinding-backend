package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class Permission {
    @TableId(type = IdType.AUTO)
    Integer permissionId;
    String operation;
    String permissionname;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    @TableLogic
    Integer deleted;
}
