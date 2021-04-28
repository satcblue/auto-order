package cn.satc.order.notify.feishu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author T1940-林浩捷
 * @date 2021/4/28
 * @since 0.0.1
 */
@ConfigurationProperties("feishu.notify")
@Data
public class FeiShuNotifyProperties {
    private String webhook = null;
}
