package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    @TableId(type = IdType.AUTO)
    Integer id;
    String username;
    String password;
    String salt;
    String email;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    @TableLogic
    Integer deleted;
}
