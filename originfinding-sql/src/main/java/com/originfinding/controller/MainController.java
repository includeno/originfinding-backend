package com.originfinding.controller;

import com.originfinding.request.SubmitRequest;
import com.originfinding.util.ClassPair;
import com.originfinding.util.MatchHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class MainController {


    //getByURL 查询URL原创度情况 查询redis内存在的记录 不存在则 查询数据库内存在的记录


    //submit 提交批处理请求
    @PostMapping("/submit")
    public String submit(SubmitRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //url过滤 只筛选符合条件的
        HashSet<String> set=new HashSet<>();
        for(String url: request.getList()){
            for(ClassPair pair: MatchHelper.methodList){
                try {
                    boolean result= (boolean) pair.getM().invoke(pair.getC().getDeclaredConstructor().newInstance(),url);
                    if(result==true){
                        //输入的消息去重
                        set.add(url);
                        break;
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        //添加数据库中url记录
        List<String> list=set.stream().collect(Collectors.toList());

        //发送kafka消息

        return "ok";
    }
}
