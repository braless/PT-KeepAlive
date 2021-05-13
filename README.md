<div align="center"> 
<h1 align="center">PT多账户保活助手</h1>
<img src="https://img.shields.io/github/issues/braless/PT-KeepAlive?color=green">
<img src="https://img.shields.io/github/stars/braless/PT-KeepAlive?color=yellow">
<img src="https://img.shields.io/github/forks/braless/PT-KeepAlive?color=orange">
<img src="https://img.shields.io/github/license/braless/PT-KeepAlive?color=ff69b4">
<img src="https://img.shields.io/github/languages/code-size/braless/PT-KeepAlive?color=blueviolet">
</div>


# 声明
使用本服务如若造成封号,概不负责
# 简介
用的是WEB端的接口，用户只需要填写`COOKEIE`即可，每日模拟账号活动记录，

# 功能

+ PT站点保活
+ 支持推送运行结果至微信(通过 PUSHPLUS 酱)、ios Bark

# 使用方法

## 1.fork本项目

## 2.获取PT的Cookie

- 在网页中登录到相关pt站，点击个人昵称然后按下`F12`打开调试模式，在`cookie`中找到cookie，并复制其`Value`值。

![](./assets/获取BDUSS.gif)

## 3.将各自的Cookie添加到仓库的Secrets中

```ruby
MTCOOKIE,PUTSHTYPE,HDCOOKIE,SCHOOLCOOKIE,PTBACOOKIE,PT52COOKIE依次为馒头,推送方式,学校,HDTIME,1ptba,52pt
``` 

| Name  | Value       |
| ----- | ----------- |
| MTCOOKIE | xxxxxxxxxxx |
| ----- | ----------- |
| PUSHTYPE | xxxxxxxxxxx |
| ----- | ----------- |
| HDCOOKIE| xxxxxxxxxxx |
| ----- | ----------- |
| SCHOOLCOOKIE  | xxxxxxxxxxx|
| ----- | ----------- |
| PTBACOOKIE | xxxxxxxxxxx |
| ----- | ----------- |
| PT52COOKIE | xxxxxxxxxxx |

#### 4.以上各自的cookie 除了mt的和hdtime的cookie之外,其他PT站的个人信息页面需要传参id(id为地址栏的id数字)


**MTCOOKIE** 格式 "cookievalue"

**HDCOOKIE** 格式 "cookievalue"

**SCHOOLCOOKIE**格式如下: "xxxxxxxxxxuserid=123456"

- 点击下图的右上角new screct
- 一定要按顺序添加
- 比如有两个站 mt 和学校 那么只需要依次添加 MTCOOKIE ,PUSHTYPE ,SCHOOLCOOKIE
-注意value一定要带双引号
![image.png](https://i.loli.net/2021/05/12/qEoQ7peZXIbJrRz.png)
将上一步骤获取到的`COOKIE`粘贴到`Secrets`中

![](./assets/添加BDUSS.gif)

## 4.开启actions

默认`actions`是处于禁止的状态，需要手动开启。

![](./assets/开启actions.gif)

## 5.第一次运行actions

每天早上`8:30`将会自动运行
## ios添加 bark 推送

需在Secrets中添加[pushplus](http://www.pushplus.plus/doc/)的`SCKEY`，格式如下
- 其中PUSHTYPE 为推送类型 目前只有bark 和pushplus 格式如下:
- PUSHTYPE   bark=xxxxxxx 其中xxxxx为手机注册bark后提供的那个链接里面的token
- PUSHTYPE   pushplus=xxxxxxx 其中xxxxx为网页微信登录注册后页面提供的那个链接里面的token

![image.png](https://i.loli.net/2021/05/12/6Eb14ShMDjHzsVy.png)
## 2021-05-11
- 第一版
- 增加pushplus推送,可以推送至微信
- 增加iOS的bark推送，可以推送至iOS手机端app

