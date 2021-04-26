package cn.satc.order.meican.dto;

import cn.satc.order.meican.dto.model.Corp;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author sunhuadong
 * @date 2020/5/14 1:20 上午
 */
@Data
public class UserTab {

    private String uniqueId;

    private String name;

    private Corp corp;

    private Long lastUsedTime;

    private String latitude;

    private String longitude;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}