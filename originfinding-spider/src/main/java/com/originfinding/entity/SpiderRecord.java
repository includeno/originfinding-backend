package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

//数据库实体
@Data
@Accessors(chain = true)
public class SpiderRecord {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    String url="";//文章地址
    String title="";//文章标题
    String tag="";//文章显示的标签
    String content="";//文章内容
    Date time;//文章时间

    Date createTime;
    Date updateTime;
    Integer valid;//记录 有效1 无效0
}
