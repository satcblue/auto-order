package cn.satc.order.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "meican.filter")
@Data
public class OrderFilterConfigProperties {
    private List<String> restaurantDeny = Lists.newArrayList();
    private List<String> dishDeny = Lists.newArrayList();
    private List<String> dishNameDeny = Lists.newArrayList();
    private Integer maxPay = 1500;
}
