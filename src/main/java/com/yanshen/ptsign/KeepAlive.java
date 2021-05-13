package com.yanshen.ptsign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * <h3>Braless</h3>
 * <p></p>
 *
 * @author : 多pt账户模拟登录保活,目前支持馒头,学校 HDtime,
 * @date : 2021-05-11 15:32
 **/
public class KeepAlive {
    private final static Logger logger = LoggerFactory.getLogger(KeepAlive.class);
    private static String MTURL = "https://kp.m-team.cc/mybonus.php";
    private static String HDTIME = "https://hdtime.org/mybonus.php";
    private static String SCHOOLURL = "https://pt.btschool.club/userdetails.php?id=";
    private static String PT52 = "https://52pt.site/userdetails.php?id=";
    private static String PTBAURL = "https://1ptba.com/userdetails.php?id=";

    public static void main(String[] args) throws InterruptedException {

        //args数组为[MTCOOKIE,PUTSHTYPE,HDCOOKIE,SCHOOLCOOKIE,PTBACOOKIE,PT52COOKIE]
        KeepAlive keepAlice = new KeepAlive();
        String putshType = "";
        String MTCOOKIE = "";
        if (args.length > 0) {
            MTCOOKIE = args[0];
            putshType = args[1];
        }
        //对m-team 保活
        if (MTCOOKIE.length() > 0) {
            keepAlice.getInfo(MTURL, MTCOOKIE, "M-team状态通知", putshType);
        }
        //对HDTime站点保活
        if (args.length > 2) {
            String HDCOOKIE = args[2];
            if (HDCOOKIE.length() > 0) {
                keepAlice.getInfo(HDTIME, HDCOOKIE, "HDtime状态通知", putshType);
            }
        }

        //注意以下PT站点需要带入userid
        //对ptschool战点保活
        if (args.length > 3) {
            String SCHOOLCOOKIE = args[3];
            if (SCHOOLCOOKIE.length() > 0) {
                keepAlice.getInfo(SCHOOLURL + SCHOOLCOOKIE.split("userid=")[1], SCHOOLCOOKIE.split("userid=")[0], "BTSCHOOL状态通知", putshType);
            }
        }
        //对1ptba站点保活
        if (args.length > 4) {
            String PTBACOOKIE = args[4];
            if (PTBACOOKIE.length() > 0) {
                keepAlice.getInfo(PTBAURL + PTBACOOKIE.split("userid=")[1], PTBACOOKIE.split("userid=")[0], "1PTBa状态通知", putshType);
            }
        }
        //对52pt站点保活
        if (args.length > 5) {
            String PT52COOKIE = args[5];
            if (PT52COOKIE.length() > 0) {
                keepAlice.getInfo(PT52 + PT52COOKIE.split("userid=")[1], PT52COOKIE.split("userid=")[0], "52PT状态通知", putshType);
            }
        }

    }

    /**
     * 通用信息获取
     *
     * @param url
     * @param cookie
     * @param title
     */
    public void getInfo(String url, String cookie, String title, String pushInfo) {

        try {
            logger.info("------开始获取" + title);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = null;
            HttpHeaders header = new HttpHeaders();
            header.set("cookie", cookie);
            header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<String>(header), String.class);
            //logger.info("获取" + title + "个人信息！接口返回信息为：{}", response.getBody());
            String info = response.getBody();
            String total = "";
            String account = "";
            //正则匹配去掉不相关数据
            if (title.startsWith("HDtime状态通知")) {
                total = info.substring(info.indexOf("分享率"), info.indexOf("可连接"));
                account = total.replaceAll("[ a-z<>$&^*%#/=\"_'';T&nbsp;<br/>]", "")
                        .replace(" ", "").replace("\"", "").replace(".?63812", "").replaceAll("[\\s\\t\\n\\r]", "");
            } else {
                total = info.substring(info.indexOf("分享率"), info.indexOf("限制"));
                account = total.replaceAll("[ a-z<>$&^*%#/=\"_'';?T]", "")
                        .replace(" ", "").replace("\"", "");
            }
            //默认截取字符串
            String endposition = "邀请";
            String startposition = "使用";
            if (title.equals("M-team状态通知")) {
                //馒头是繁体面板 所以要设置繁体字截取
                endposition = "邀請";

            }
            if (title.equals("HDtime状态通知")) {
                //HDtime页面为游戏
                startposition = "游戏";
                endposition = "邀请";
            }
            if (title.equals("52PT状态通知")) {
                //52PT页面为用途
                startposition = "用途";
                endposition = "邀请";
            }
            String mgValue = info.substring(info.indexOf(startposition), info.indexOf(endposition)).
                    replaceAll("[a-z<>$&^*%=/_'';,T<br/>&nbsp;]", "").
                    replace("]", "")
                    .replace("使用:", "").replace("游戏:", "")
                    .replace(" ", "")
                    .replace("用途", "")
                    .replaceAll("[\\s\\t\\n\\r]", "")
                    .replace("签到赚魔力", "")
                    .replace("\"", "")
                    .replace("[", "");
            logger.info("获取账户魔力值成功!当前账户魔力值:{}", mgValue);
            logger.info("获取账户信息成功!当前账户信息:{}", account);
            push(title, "账户魔力值:" + mgValue + "账户信息:" + account, pushInfo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("账户信息异常:{}", title);
            push(title, "账户信息异常啦!去电脑上看一看吧", pushInfo);
        }
    }

    /**
     * ios bark推送
     *
     * @param title
     * @param message
     */
    public void push(String title, String message, String pushInfo) {
        // 推送的方式和所需信息
        String pushType = pushInfo.split("=")[0];
        String pushKey = pushInfo.split("=")[1];
        ResponseEntity<String> response = null;
        RestTemplate restTemplate = new RestTemplate();
        switch (pushType) {
            case "bark": {
                String url = "https://api.day.app/" + pushKey + "/" + title + "/" + message;
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class);
                logger.info("iOS_Bark推送成功");
                break;
            }
            case "pushplus": {
                String url = "http://www.pushplus.plus/send?token=" + pushKey + "&title=" + title + "&content=" + message;
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class);
                logger.info("pushplus 推送成功");
                break;
            }
        }
    }

}
