package com.originfinding.algorithm;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class WordPair {
    String word;
    Integer count;

    public WordPair(String word, Integer count) {
        this.word = word;
        this.count = count;
    }
}
