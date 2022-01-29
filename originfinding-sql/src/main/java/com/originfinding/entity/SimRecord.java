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
    Integer valid=1;//记录 有效1 无效0

    //特征指标
    String simlevelfirst ="";
    String simlevelsecond ="";
    Integer simparentId=-1;//如果是原创的直接为-1 短文本为-2 最相似的父亲 时间在文章时间之前
    Integer earlyparentId=-1;//如果是原创的直接为-1 短文本为-2 相似度在一定范围内的最早的父亲

    //手动标记 优先度最高
    Integer manulparentId=-1;//手动标记关联的原创文章id -1表示未设置 -2表示已设置为原创 正数表示关联的原创文章id
}
