package com.originfinding.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UrlRecord {
    String url;
    String title;//文章标题
    String content;//文章主体内容
    String tag;//文章显示标注的tag 不一定存在
    Date time;//文章时间
}
