package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysPermission {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    String menuCode;
    String menuName;
    String permissionCode;
    String permissionName;
}
