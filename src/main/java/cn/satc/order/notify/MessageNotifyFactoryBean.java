package cn.satc.order.notify;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.satc.order.notify.tencent.SmsMessage;
import cn.satc.order.notify.tencent.TencentSmsConfigProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author 林浩捷
 */
@Configuration
public class MessageNotifyFactoryBean implements FactoryBean<MessageNotify> {


    private TencentSmsConfigProperties tencentSmsConfigProperties;

    @Override
    public MessageNotify getObject() throws Exception {
        if (!ObjectUtil.hasNull(ReflectUtil.getFieldsValue(tencentSmsConfigProperties))) {
            SmsMessage smsMessage = new SmsMessage();
            smsMessage.setTencentSmsConfigProperties(tencentSmsConfigProperties);
            return smsMessage;
        }
        return new FakeMessageNotify();

    }

    @Override
    public Class<?> getObjectType() {
        return MessageNotify.class;
    }

    @Autowired
    public void setTencentSmsConfigProperties(TencentSmsConfigProperties tencentSmsConfigProperties) {
        this.tencentSmsConfigProperties = tencentSmsConfigProperties;
    }
}
