package cn.satc.order.notify.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.satc.order.notify.MessageNotify;
import cn.satc.order.notify.FakeMessageNotify;
import cn.satc.order.notify.tencent.SmsMessage;
import cn.satc.order.notify.tencent.TencentSmsConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public MessageNotify smsMessage(TencentSmsConfigProperties tencentSmsConfigProperties) {
        if (ObjectUtil.hasNull(ReflectUtil.getFieldsValue(tencentSmsConfigProperties))) {
            return new FakeMessageNotify();
        }
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setTencentSmsConfigProperties(tencentSmsConfigProperties);
        return smsMessage;
    }
}
