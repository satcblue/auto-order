package cn.satc.order.notify.tencent.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * 腾讯云短信配置
 * @author T1940-林浩捷
 * @date 2021/4/26
 * @since 0.0.1
 */
@Getter
@ToString
@Component
public class SmsConfig {

    private static final String APP_ID = "tencent.sms.appId";
    private static final String SECRET_ID = "tencent.sms.secretId";
    private static final String SECRET_KEY = "tencent.sms.secretKey";
    private static final String TEMPLATE_ID = "tencent.sms.messageTemplateId";

    private final String appId;
    private final String secretId;
    private final String secretKey;
    private final String templateId;

    public SmsConfig() {
        this.appId = System.getProperty(APP_ID);
        this.secretId = System.getProperty(SECRET_ID);
        this.secretKey = System.getProperty(SECRET_KEY);
        this.templateId = System.getProperty(TEMPLATE_ID);
        Preconditions.checkArgument(CharSequenceUtil.isNotBlank(appId), "短信应用id不能为空");
        Preconditions.checkArgument(CharSequenceUtil.isNotBlank(secretId), "密钥id不能为空");
        Preconditions.checkArgument(CharSequenceUtil.isNotBlank(secretKey), "密钥key不能为空");
        Preconditions.checkArgument(CharSequenceUtil.isNotBlank(templateId), "短信模板id不能为空");

    }
}
