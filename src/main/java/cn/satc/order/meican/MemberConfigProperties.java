package cn.satc.order.meican;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author T1940-林浩捷
 * @date 2021/4/28
 * @since 0.0.1
 */
@ConfigurationProperties(prefix = "meican.member")
@Data
public class MemberConfigProperties {
    private String username = "";
    private String password = "";
    private String notifyPhone = "";
}
