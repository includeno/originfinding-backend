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
    Integer view=-1;//文章浏览量 -1代表无法获取数据
    Date time;//文章时间

    String simhash="";
    Date createTime;
    Date updateTime;
    Integer valid=1;//记录 有效1 无效0

    //特征指标
    Integer simlevelfirst =-1;
    Integer simlevelsecond =-1;
    Integer simparentId=-1;//如果是原创的直接为-1 短文本为-2 最相似的父亲 时间在文章时间之前
    Integer earlyparentId=-1;//如果是原创的直接为-1 短文本为-2 相似度在一定范围内的最早的父亲

    //人工审核标记
    Integer manulsymbol =0;//人工审核标记关联的原创文章id 0表示未设置 1表示人工标识为原创
    Integer version=1;
}
