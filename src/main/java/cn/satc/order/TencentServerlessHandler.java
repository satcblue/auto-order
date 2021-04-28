package cn.satc.order;

import com.qcloud.scf.runtime.AbstractSpringHandler;

/**
 * @author T1940-林浩捷
 * @date 2021/4/28
 * @since 0.0.1
 */
public class TencentServerlessHandler extends AbstractSpringHandler {
    @Override
    public void startApp() {
        AutoOrderApplication.main(new String[]{""});
    }
}
