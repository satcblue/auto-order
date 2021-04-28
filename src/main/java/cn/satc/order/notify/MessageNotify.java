package cn.satc.order.notify;

/**
 * @author T1940-林浩捷
 * @date 2021/4/28
 * @since 0.0.1
 */
public interface MessageNotify {
    /**
     * 发送
     * @param templateParams 模板变量
     * @param target 目的地
     */
    void notify(String[] templateParams, String ...target);
}
