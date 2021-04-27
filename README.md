# 美餐自动点餐
该自动点餐依赖于github的Actions。现在是在每天的19：30完成第二天的订餐。
## 配置步骤
### github secrets 变量
USER_NAME： 登录美餐的手机号码

PASSWORD： 登录美餐的密码

以下服务如果没有开通，随便填值
APP_ID：腾讯云短信的AppId

SECRET_ID: 腾讯云短信的密钥id

SECRET_KEY: 腾讯云短信的密钥key

MESSAGE_TEMPLATE_ID: 腾讯云短信的短信模板

SIGN：腾讯云短信的签名

PHONE： 需要通知的手机号码（主要手机号码前国内要加 +86）
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