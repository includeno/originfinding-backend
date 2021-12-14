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
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CsdnService implements ContentService{
    public static final String pattern="https://blog.csdn.net/(.*)/article/details/(.*)";//正则匹配表达式
    public static final Pattern re = Pattern.compile(pattern);
    public static final String host="blog.csdn.net";

    @Override
    public void wait(WebDriver chrome, String url){
        //等待弹窗加载完毕
        WebDriverWait wait = new WebDriverWait(chrome, 10, 1);

        WebElement searchInput = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver text) {
                return text.findElement(By.tagName("article"));
            }
        });
        try {
            TimeUnit.MILLISECONDS.sleep(1072 +  123);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        WebElement text = driver.findElement(By.xpath("//a[contains(text(),'下一页')]"));
//        text.click();
    }

    @Override
    public String getMainContent(WebDriver chrome, String url) {
        WebElement content = chrome.findElement(By.tagName("article"));
        String ans=content.getText();
        if(ans!=null&&ans.equals("")){
            return ans;
        }
        else{
            return "";
        }
    }

    @Override
    public String getTitle(WebDriver chrome, String url) {
        WebElement content = chrome.findElement(By.id("articleContentId"));
        String ans=content.getText();
        if(ans!=null&&ans.equals("")){
            return ans;
        }
        else{
            return "";
        }
    }

    @Override
    public Date getTime(WebDriver chrome, String url) {
        //class time
        WebElement content = chrome.findElement(By.className("time"));
        String ans=content.getText();
        Date res=new Date();
        if(ans!=null&&!ans.equals("")){
            res=GlobalDateUtil.convert(ans);
        }
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
