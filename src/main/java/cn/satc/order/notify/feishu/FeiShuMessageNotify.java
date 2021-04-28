package cn.satc.order.notify.feishu;

import cn.hutool.core.util.StrUtil;
import cn.satc.order.exception.BusinessException;
import cn.satc.order.notify.MessageNotify;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author T1940-林浩捷
 * @date 2021/4/28
 * @since 0.0.1
 */
@Component
@Slf4j
public class FeiShuMessageNotify implements MessageNotify {

    private static final String template = "订餐成功。午餐：{}；晚餐：{}。";

    private FeiShuNotifyProperties feiShuNotifyProperties;
    private OkHttpClient okHttpClient;

    @Override
    public void notify(String[] templateParams) {
        if (templateParams.length < 2) {
            return;
        }
        if (StrUtil.isBlank(feiShuNotifyProperties.getWebhook())) {
            return;
        }
        String text = StrUtil.format(template, Arrays.copyOf(templateParams, 2));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", text);
        FormBody formBody = new FormBody.Builder()
                .add("msg_type", "text")
                .add("content", jsonObject.toJSONString())
                .build();
        Request request = new Request.Builder()
                .url(feiShuNotifyProperties.getWebhook()).post(formBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            log.info(JSONObject.toJSONString(response));
        } catch (IOException e) {
            throw new RuntimeException("飞书webhook通知异常");
        }
    }


    @Autowired
    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Autowired
    public void setFeiShuNotifyProperties(FeiShuNotifyProperties feiShuNotifyProperties) {
        this.feiShuNotifyProperties = feiShuNotifyProperties;
    }
}
