package cn.satc.order.meican;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("meican.oauth2")
@Data
public class MeiCanOauth2ConfigProperties {
    private String clientId;
    private String clientSecret;
}
