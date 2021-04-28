package cn.satc.order.notify.tencent.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.satc.order.notify.MessageNotify;
import cn.satc.order.notify.tencent.FakeMessageNotify;
import cn.satc.order.notify.tencent.SmsMessage;
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
