package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class Copyright {
    @TableId(type = IdType.AUTO)
    Integer id;

    Integer auditId;//审核用户ID
    String url;
    Integer requestId;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    @Version
    Integer version;
}
