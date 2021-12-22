package com.originfinding.listener.message;

import lombok.Data;

import java.util.Date;

@Data
public class QueueMessage {
    Integer id;//数据库id

    String url;//文章地址
    String title;//文章标题
    String tag;//文章显示的标签
    Date time;//文章时间

    String simhash;
    String keywords;//通过分词方法得到

}
