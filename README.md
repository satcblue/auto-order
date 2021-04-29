# 美餐自动点餐
该自动点餐依赖于github的Actions。现在是在每天的19：30完成第二天的订餐。
## 配置步骤
1. Fork 一份到自己的仓库。（页面右上角）
2. 进行项目配置，需要配置的参数见 [github secrets 变量](#github-secrets-变量)。
1. 美餐账号 username 为绑定的手机号
2. 飞书webhook配置
3. 腾讯云短信配置
3. 配置完之后，进入Actions，手动运行工作流一次，确保自己配置的参数没有错误和使得定时任务能正常执行。

### github secrets 变量
[如何设置github secrets?](https://docs.github.com/cn/actions/reference/encrypted-secrets)

必填，美餐账号：
```
USER_NAME： 登录美餐的手机号码
PASSWORD： 登录美餐的密码
```

以下参数：如果没有开通可不填（用于提醒）

飞书提醒：
```
FEI_SHU_NOTIFY_WEBHOOK: 飞书机器人的webhook
```
腾讯云短信提醒：
```
APP_ID：腾讯云短信的AppId
SECRET_ID: 腾讯云短信的密钥id
SECRET_KEY: 腾讯云短信的密钥key
MESSAGE_TEMPLATE_ID: 腾讯云短信的短信模板
SIGN：腾讯云短信的签名
PHONE： 需要通知的手机号码（手机号码前国内要加 +86）
```


### 腾讯云短信模板
```
订餐成功。午餐：{1}；晚餐：{2}。
```

### 点餐过滤配置
修改代码文件下的 resource/application.yml
```yaml
meican:
 filter:
   # 需要过滤的餐品名称（包含）
    dish-name-deny:
      - 辣
      - 123145
   # 最大支付金额
    max-pay: 1500
   # 餐厅黑名单
    restaurant-deny:
      - 1234
      - 12345
   # 餐品黑名单（全名）
    dish-deny:
      - 1234
      - 12345
```