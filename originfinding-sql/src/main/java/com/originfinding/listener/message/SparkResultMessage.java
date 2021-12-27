package com.originfinding.listener.message;

import lombok.Data;

import java.util.Date;

@Data
public class SparkResultMessage {
    Integer id;//数据库id

    String url;//文章地址
    String sim3;
    String sim4;
    String sim5;
    Date updateTime;//上一次更新时间
    String keywords;//通过spark分词方法得到
    String parentUrl;//根据parentId转换为对应的url
}
