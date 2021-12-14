package com.originfinding.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UrlRecord {
    String url;
    String title;//文章标题
    String content;//文章主体内容
    Date time;//文章时间
}
