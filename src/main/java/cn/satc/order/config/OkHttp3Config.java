package cn.satc.order.config;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author sunhuadong
 * @date 2020/5/11 0:45 上午
 */
@Configuration
public class OkHttp3Config {

    @Resource
    private RetryInterceptor retryInterceptor;

    private OkHttpClient okHttpClient = null;

    @Bean
    OkHttpClient okHttpClient() {
        this.okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .addInterceptor(retryInterceptor)
                .build();
        return this.okHttpClient;
    }

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            // 信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PreDestroy
    public void destroy() throws IOException {
        if (this.okHttpClient == null) {
            return;
        }
        this.okHttpClient.dispatcher().executorService().shutdown();   //清除并关闭线程池
        this.okHttpClient.connectionPool().evictAll();                 //清除并关闭连接池
        Cache cache = this.okHttpClient.cache();
        if ( cache != null) {
            cache.close();
        }
    }
}
