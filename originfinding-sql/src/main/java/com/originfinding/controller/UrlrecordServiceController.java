package com.originfinding.controller;

import com.originfinding.entity.SimRecord;
import com.originfinding.service.SimRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/service")
public class UrlrecordServiceController {
    @Autowired
    SimRecordService UrlrecordService;

    @PostMapping("/Urlrecord")
    public boolean insertUrlrecord(SimRecord SimRecord){
        SimRecord.setCreateTime(new Date());
        return UrlrecordService.save(SimRecord);
    }
    @GetMapping("/Urlrecord")
    public SimRecord getUrlrecord(Integer id){
        return UrlrecordService.getById(id);
    }
    @PutMapping("/Urlrecord")
    public boolean updateUrlrecord(SimRecord SimRecord){
        return UrlrecordService.save(SimRecord);
    }
    @DeleteMapping("/Urlrecord")
    public boolean deleteUrlrecord(SimRecord SimRecord){
        return UrlrecordService.removeById(SimRecord.getId());
    }
}