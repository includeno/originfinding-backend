package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class Copyright {
    @TableId(type = IdType.AUTO)
    Integer id;

    Integer userId;
    String url;
    Integer requestId;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    @Version
    Integer version;
}
