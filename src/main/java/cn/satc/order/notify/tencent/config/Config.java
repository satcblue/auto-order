package cn.satc.order.notify.tencent.config;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.system.SystemUtil;
import cn.satc.order.notify.tencent.SmsMessage;
import com.google.common.base.Preconditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SystemPropertyUtils;

@Configuration
public class Config {

    public static final String APP_ID = "TENCENT_SMS_APP_ID";
    public static final String SECRET_ID = "TENCETN_SECRET_ID";
    public static final String SECRET_KEY = "TENCENT_SECRET_KEY";
    public static final String TEMPLATE_ID = "TENCENT_SMS_MESSAGE_TEMPLATE_ID";
    public static final String SIGN = "TENCENT_SMS_SIGN";



    @Bean
    public SmsConfig smsConfig() {
        String appId = System.getProperty(APP_ID);
        String secretId = System.getProperty(SECRET_ID);
        String secretKey = System.getProperty(SECRET_KEY);
        String templateId = System.getProperty(TEMPLATE_ID);
        String sign = System.getProperty(SIGN);
        return new SmsConfig(appId, secretId, secretKey, templateId, sign);
    }

    @Bean
    public SmsMessage smsMessage(SmsConfig smsConfig) {
        return new SmsMessage(smsConfig);
    }
}
