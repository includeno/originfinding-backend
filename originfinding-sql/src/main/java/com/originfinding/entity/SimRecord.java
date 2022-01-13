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

    String url="";//文章地址
    String title="";//文章标题
    String tag="";//文章显示的标签
    Date time;//文章时间

    String tfidftag;//根据频率计算的标签
    String ldatag;//根据LDA计算的标签

    String simhash="";
    Date createTime;
    Date updateTime;

    //特征指标
    String sim3="";
    String sim4="";

    Integer simparentId;//如果是原创的直接为-1 短文本为-2 最相似的父亲 时间在文章时间之前
    Integer earlyparentId;//如果是原创的直接为-1 短文本为-2 相似度在一定范围内的最早的父亲
}
