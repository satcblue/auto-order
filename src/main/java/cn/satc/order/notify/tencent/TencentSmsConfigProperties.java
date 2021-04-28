package cn.satc.order.notify.tencent;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author T1940-林浩捷
 * @date 2021/4/28
 * @since 0.0.1
 */
@ConfigurationProperties(prefix = "tencent.sms")
@Data
public class TencentSmsConfigProperties {
    private String appId = null;
    private String secretId = null;
    private String secretKey = null;
    private String templateId = null;
    private String sign = null;
}
