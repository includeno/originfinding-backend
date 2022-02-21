package com.originfinding.message;

import com.originfinding.entity.UrlRecord;
import lombok.Data;

import java.util.Date;

@Data
public class SpiderResultMessage extends UrlRecord {
    String message;
    Integer code;

    String simhash;

    public static SpiderResultMessage copyUrlRecord(UrlRecord record){
        SpiderResultMessage spiderResultMessage = new SpiderResultMessage();
        spiderResultMessage.setUrl(record.getUrl());
        spiderResultMessage.setContent(record.getContent());
        spiderResultMessage.setTitle(record.getTitle());
        spiderResultMessage.setTime(record.getTime());
        spiderResultMessage.setView(record.getView());
        spiderResultMessage.setValid(record.getValid());
        return spiderResultMessage;
    }
}
