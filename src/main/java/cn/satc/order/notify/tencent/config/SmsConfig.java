package cn.satc.order.notify.tencent.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云短信配置
 * @author T1940-林浩捷
 * @date 2021/4/26
 * @since 0.0.1
 */
@Getter
@ToString
public class SmsConfig {
    private final String appId;
    private final String secretId;
    private final String secretKey;
    private final String templateId;
    private final String sign;
    public SmsConfig(String appId, String secretId, String secretKey, String templateId, String sign) {
        Preconditions.checkArgument(CharSequenceUtil.isNotBlank(appId), "短信应用id不能为空");
        Preconditions.checkArgument(CharSequenceUtil.isNotBlank(secretId), "密钥id不能为空");
        Preconditions.checkArgument(CharSequenceUtil.isNotBlank(secretKey), "密钥key不能为空");
        Preconditions.checkArgument(CharSequenceUtil.isNotBlank(templateId), "短信模板id不能为空");
        Preconditions.checkArgument(CharSequenceUtil.isNotBlank(sign), "短信签名不能为空");
        this.appId = appId;
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.templateId = templateId;
        this.sign = sign;
    }
}
