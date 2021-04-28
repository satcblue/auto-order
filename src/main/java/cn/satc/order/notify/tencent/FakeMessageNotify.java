package cn.satc.order.notify.tencent;

import cn.satc.order.notify.MessageNotify;

/**
 * @author T1940-林浩捷
 * @date 2021/4/28
 * @since 0.0.1
 */
public class FakeMessageNotify implements MessageNotify {
    @Override
    public void notify(String[] templateParams, String... target) {
        // do nothing
    }
}
