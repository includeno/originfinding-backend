package com.originfinding.response;

import com.originfinding.entity.UrlRecord;
import lombok.Data;

@Data
public class SpiderResponse extends Result {

    public UrlRecord record;
}
