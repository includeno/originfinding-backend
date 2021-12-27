package com.originfinding.service;

import com.originfinding.config.SeleniumConfig;
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
public class CnblogsService implements ContentService {
    public static final String[] patterns = new String[]{
            "https://www.cnblogs.com/(.*)/p/(.*)",//https://www.cnblogs.com/frankdeng/p/9310684.html
    };

    @Override
    public boolean match(String url) {
        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern);
            if (p.matcher(url).matches()) {
                log.info("cnblog matches");
                return true;
            }
        }
        return false;
    }

    @Override
    public WebDriver getDriver() {
        WebDriver chrome = SeleniumConfig.getWebDriver(true);
        return chrome;
    }

    @Override
    public void wait(WebDriver chrome, String url) {
        WebDriverWait wait = new WebDriverWait(chrome, 30, 1);
        WebElement searchInput = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver text) {
                return text.findElement(By.id("post_detail"));
            }
        });
        log.info("wait article completed");
    }

    @Override
    public String getMainContent(WebDriver chrome, String url) {
        WebElement content = chrome.findElement(By.id("cnblogs_post_body"));
        String ans = content.getText();
        if (ans != null && !ans.equals("")) {
            log.info("getMainContent completed " + ans);
            return ans;
        } else {
            log.error("getMainContent error " + ans);
            return "";
        }
    }

    @Override
    public String getTitle(WebDriver chrome, String url) {
        WebElement content = chrome.findElement(By.id("cb_post_title_url"));
        String ans = content.getText();
        if (ans != null && !ans.equals("")) {
            log.info("getTitle completed " + ans);
            return ans;
        } else {
            log.error("getTitle error " + ans);
            return "";
        }
    }

    @Override
    public String getTag(WebDriver chrome, String url) {
        //id BlogPostCategory->多个a text
        String ans = "";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            WebElement list = chrome.findElement(By.id("BlogPostCategory"));
            List<WebElement> tags = list.findElements(By.tagName("a"));

            if (tags != null && tags.size() > 0) {
                for (WebElement element : tags) {
                    stringBuffer.append(element.getText() + " ");
                }
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            e.printStackTrace();
            log.error("getTag error when finding BlogPostCategory");
        }

        try {
            WebElement list = chrome.findElement(By.id("EntryTag"));
            List<WebElement> tags = list.findElements(By.tagName("a"));

            if (tags != null && tags.size() > 0) {
                for (WebElement element : tags) {
                    stringBuffer.append(element.getText() + " ");
                }
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            e.printStackTrace();
            log.error("getTag error when finding EntryTag");
        }
        ans = stringBuffer.toString();
        log.info("getTag completed " + ans);
        return ans;
    }

    @Override
    public Date getTime(WebDriver chrome, String url) {
        //class time
        WebElement content = chrome.findElement(By.id("post-date"));
        String ans = content.getText();
        Date res = new Date();
        if (ans != null && !ans.equals("")) {
            res = GlobalDateUtil.convert(ans);
        }
        log.info("getTime completed " + res.toString());
        return res;
    }


}
