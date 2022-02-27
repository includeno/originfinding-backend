package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class CopyrightRequest {

    @TableId(type = IdType.AUTO)
    Integer id;

    Integer userId;
    String url;
    String platform;
    String platformHash;
    String comment;
    Integer status;//CopyrightStatus内定义

    @TableField(fill = FieldFill.INSERT)
    Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    @Version
    Integer version;
}
