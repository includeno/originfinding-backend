package com.originfinding.response;

import lombok.Data;

import java.util.Date;

@Data
public class SpiderResponse {
    String url;
    String title;//文章标题
    String content;//文章主体内容
    Date time;//文章时间
}