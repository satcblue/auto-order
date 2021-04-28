package cn.satc.order.notify.tencent;

import cn.hutool.core.util.StrUtil;
import cn.satc.order.notify.MessageNotify;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author T1940-林浩捷
 * @date 2021/4/25
 * @since 0.0.1
 */
@Slf4j
public class SmsMessage implements MessageNotify {

    private SmsClient smsClient;
    private String appId;
    private String templateId;
    private  String sign;

    @Override
    public void notify(String[] templateParams, String ...phone) {
        List<String> ss = Lists.newArrayList();
        for (String pa : templateParams) {
            ss.add(StrUtil.subWithLength(pa, 0, 12));
        }
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppid(this.appId);
        req.setSign(sign);
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

    public void setTencentSmsConfigProperties(TencentSmsConfigProperties tencentSmsConfigProperties) {
        this.appId = tencentSmsConfigProperties.getAppId();
        this.templateId = tencentSmsConfigProperties.getTemplateId();
        Credential cred = new Credential(tencentSmsConfigProperties.getSecretId(), tencentSmsConfigProperties.getSecretKey());
        this.smsClient = new SmsClient(cred, "ap-guangzhou");
        this.sign = tencentSmsConfigProperties.getSign();
    }
}
