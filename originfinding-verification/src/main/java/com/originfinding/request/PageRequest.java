package com.originfinding.request;

import lombok.Data;

@Data
public class PageRequest {
    Integer size;//显示多少条记录
    Integer start;//当前页数
}
