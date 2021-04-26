package cn.satc.order.notify.tencent;

import cn.satc.order.notify.tencent.config.SmsConfig;
import com.google.common.base.Preconditions;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import org.springframework.stereotype.Component;

/**
 * @author T1940-林浩捷
 * @date 2021/4/25
 * @since 0.0.1
 */
@Component
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
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppid(this.appId);
        req.setSign("satcblue");
        req.setTemplateID(this.templateId);
        req.setPhoneNumberSet(phone);
        req.setTemplateParamSet(templateParams);
        try {
            this.smsClient.SendSms(req);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}
