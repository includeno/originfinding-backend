package com.originfinding.controller;

import com.originfinding.entity.SimRecord;
import com.originfinding.mapper.UrlrecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/mapper")
public class UrlrecordMapperController {
    @Autowired
    UrlrecordMapper UrlrecordMapper;

    @PostMapping("/Urlrecord")
    public int insertUrlrecord(SimRecord SimRecord){
        SimRecord.setCreateTime(new Date());
        return UrlrecordMapper.insert(SimRecord);
    }
    @GetMapping("/Urlrecord")
    public SimRecord getUrlrecord(Integer id){
        return UrlrecordMapper.selectById(id);
    }
    @PutMapping("/Urlrecord")
    public int updateUrlrecord(SimRecord SimRecord){
        return UrlrecordMapper.updateById(SimRecord);
    }
    @DeleteMapping("/Urlrecord")
    public int deleteUrlrecord(SimRecord SimRecord){
        return UrlrecordMapper.deleteById(SimRecord.getId());
    }
}