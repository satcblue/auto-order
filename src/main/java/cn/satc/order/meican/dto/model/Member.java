package cn.satc.order.meican.dto.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author sunhuadong
 * @date 2020/5/12 11:52 下午
 */
@Data
@Accessors(chain = true)
public class Member {

    private String username;

    private String password;

    private String cookies;

    private String token;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
