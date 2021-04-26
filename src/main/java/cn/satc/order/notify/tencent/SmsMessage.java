package cn.satc.order.notify.tencent;

import cn.hutool.core.util.StrUtil;
import cn.satc.order.notify.tencent.config.SmsConfig;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author T1940-林浩捷
 * @date 2021/4/25
 * @since 0.0.1
 */
@Component
@Slf4j
public class SmsMessage {

    private final SmsClient smsClient;
    private final String appId;
    private final String templateId;

    public SmsMessage(SmsConfig smsConfig) {
        this.appId = smsConfig.getAppId();
        this.templateId = smsConfig.getTemplateId();
        Credential cred = new Credential(smsConfig.getSecretId(), smsConfig.getSecretKey());
        this.smsClient = new SmsClient(cred, "ap-guangzhou");
    }

    public void notify(String[] templateParams, String ...phone) {
        List<String> ss = Lists.newArrayList();
        for (String pa : templateParams) {
            ss.add(StrUtil.subWithLength(pa, 0, 12));
        }
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppid(this.appId);
        req.setSign("satcblue");
        req.setTemplateID(this.templateId);
        req.setPhoneNumberSet(phone);
        req.setTemplateParamSet(ss.toArray(new String[0]));
        try {
            SendSmsResponse sendSmsResponse = this.smsClient.SendSms(req);
            log.info(JSON.toJSONString(sendSmsResponse));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}
