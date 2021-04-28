package cn.satc.order.notify;

import org.springframework.stereotype.Component;

/**
 * @author T1940-林浩捷
 * @date 2021/4/28
 * @since 0.0.1
 */
@Component
public class FakeMessageNotify implements MessageNotify {

    @Override
    public void notify(String[] templateParams) {

    }
}
