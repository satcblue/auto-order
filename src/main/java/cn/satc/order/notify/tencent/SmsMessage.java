package cn.satc.order.notify.tencent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.satc.order.meican.MemberConfigProperties;
import cn.satc.order.notify.MessageNotify;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * @author T1940-林浩捷
 * @date 2021/4/25
 * @since 0.0.1
 */
@Slf4j
@Component
public class SmsMessage implements MessageNotify {

    private SmsClient smsClient;
    private String appId;
    private String templateId;
    private  String sign;
    private boolean canExecute;
    private String[] phone;

    private static final int TEMPLATE_PARAMS_LENGTH = 2;

    @Override
    public void notify(@Nonnull String[] templateParams) {
        if (! canExecute || phone.length == 0 || templateParams.length < TEMPLATE_PARAMS_LENGTH) {
            return;
        }
        templateParams = Arrays.copyOf(templateParams, TEMPLATE_PARAMS_LENGTH);
        List<String> ss = Lists.newArrayList();
        for (String pa : templateParams) {
            ss.add(CharSequenceUtil.subWithLength(pa, 0, 12));
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

    @Autowired
    public void setTencentSmsConfigProperties(TencentSmsConfigProperties tencentSmsConfigProperties) {
        if (ObjectUtil.hasNull(ReflectUtil.getFieldsValue(tencentSmsConfigProperties))) {
            canExecute = false;
            return;
        }
        canExecute = true;
        this.appId = tencentSmsConfigProperties.getAppId();
        this.templateId = tencentSmsConfigProperties.getTemplateId();
        Credential cred = new Credential(tencentSmsConfigProperties.getSecretId(), tencentSmsConfigProperties.getSecretKey());
        this.smsClient = new SmsClient(cred, "ap-guangzhou");
        this.sign = tencentSmsConfigProperties.getSign();
    }
    @Autowired
    public void setMemberConfigProperties(MemberConfigProperties memberConfigProperties) {
        this.phone = memberConfigProperties.getNotifyPhone().split(",");
    }
}
