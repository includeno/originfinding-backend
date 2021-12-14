package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

//数据库实体
@Data
@Accessors(chain = true)
public class SimRecord {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    String url;//文章地址
    String simhash;
    Date time;//文章时间
    Date createTime;
    Date updateTime;

    //特征指标
    String sim3;
    String sim4;
    String sim5;
//    String sim6;
//    String sim7;
    Integer parentId;//如果是原创的直接为-1


}
