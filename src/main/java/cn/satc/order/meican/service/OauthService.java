package cn.satc.order.meican.service;

import cn.satc.order.exception.BusinessException;
import cn.satc.order.meican.MeiCanOauth2ConfigProperties;
import cn.satc.order.meican.constant.UrlConstant;
import cn.satc.order.meican.dto.model.Member;
import cn.satc.order.meican.dto.response.TokenResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author sunhuadong
 * @date 2020/5/11 11:47 上午
 */
@Service
@Slf4j
public class OauthService {

    @Resource
    private OkHttpClient okHttpClient;

    private MeiCanOauth2ConfigProperties meiCanOauth2ConfigProperties;

    public Member loginByUsernameAndPassword(@Nonnull Member member) {
        FormBody formBody = new FormBody.Builder()
                .add("client_id", meiCanOauth2ConfigProperties.getClientId())
                .add("client_secret", meiCanOauth2ConfigProperties.getClientSecret())
                .add("grant_type", "password")
                .add("meican_credential_type", "password")
                .add("username", member.getUsername())
                .add("password", member.getPassword())
                .add("username_type", "mobile")
                .build();
        Request request = new Request.Builder().url(UrlConstant.OAUTH_TOKEN_URL).post(formBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Headers headers = response.headers();
            List<Cookie> cookieList = Cookie.parseAll(request.url(), headers);
            StringBuilder stringBuilder = new StringBuilder();
            if (!cookieList.isEmpty()) {
                for (Cookie cookie : cookieList) {
                    String name = cookie.name();
                    String value = cookie.value();
                    stringBuilder.append(name).append("=").append(value).append("; ");
                }
            }
            String cookies = stringBuilder.toString();
            ResponseBody responseBody = response.body();
            if (Objects.nonNull(responseBody)) {
                String body = responseBody.string();
                TokenResponse tokenResponse = JSON.parseObject(body, TokenResponse.class);
                String token = JSON.toJSONString(tokenResponse);
                return member.setCookies(cookies).setToken(token);
            }
        } catch (IOException e) {
            log.error("请求出错:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    @Autowired
    public void setMeiCanOauth2ConfigProperties(MeiCanOauth2ConfigProperties meiCanOauth2ConfigProperties) {
        this.meiCanOauth2ConfigProperties = meiCanOauth2ConfigProperties;
    }
}
