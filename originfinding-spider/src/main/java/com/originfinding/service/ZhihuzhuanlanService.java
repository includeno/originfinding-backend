package com.originfinding.service;

import com.originfinding.util.GlobalDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ZhihuzhuanlanService implements ContentService{
    public static final String pattern="https://zhuanlan.zhihu.com/p/(.*)";
    public static final Pattern re = Pattern.compile(pattern);
    public static final String host="zhuanlan.zhihu.com";

    @Override
    public void wait(WebDriver chrome, String url){
        try {
            //等待弹窗加载完毕
            WebDriverWait wait = new WebDriverWait(chrome, 10, 1);
            //如果有注册弹窗 关闭Modal-closeButton
            WebElement searchInput = wait.until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver text) {
                    return text.findElement(By.className("Modal-closeButton"));
                }
            });
            searchInput.click();
            //如果有关注弹窗 关闭Modal-closeButton
            List<WebElement> elements = chrome.findElements(By.className("Modal-closeButton"));
            for(int i=0;i<elements.size();i++){
                elements.get(i).click();
            }
            log.info("wait completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMainContent(WebDriver chrome, String url) {
        WebElement content = chrome.findElement(By.className("RichText"));
        String ans=content.getText();
        if(ans!=null&&!ans.equals("")){
            log.info("getMainContent completed"+ans);
            return ans;
        }
        else{
            log.error("getMainContent error "+ans);
            return "";
        }
    }

    @Override
    public String getTitle(WebDriver chrome, String url) {
        WebElement content = chrome.findElement(By.className("Post-Title"));
        String ans=content.getText();
        if(ans!=null&&!ans.equals("")){
            log.info("getTitle completed"+ans);
            return ans;
        }
        else{
            log.error("getTitle error "+ans);
            return "";
        }
    }

    @Override
    public Date getTime(WebDriver chrome, String url){
        //class ContentItem-time
        //发布于 2019-10-20 10:23

        WebElement content = chrome.findElement(By.className("ContentItem-time"));
        String ans=content.getText();
        Date res=new Date();
        if(ans!=null&&!ans.equals("")){
            res= GlobalDateUtil.convert(ans);
        }
        log.info("getTime completed "+res.toString());
        return res;
    }

    @Override
    public boolean match(String url) {
        String cur=getHost(url);
        if(cur.equals(host)&&re.matcher(url).matches()){
            return true;
        }
        else {
            return false;
        }
    }
}

