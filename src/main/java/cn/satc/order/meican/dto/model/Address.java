package cn.satc.order.meican.dto.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author sunhuadong
 * @date 2020/5/14 1:20 上午
 */
@Data
public class Address {

    private String uniqueId;

    private String address;

    private String corpAddressCode;

    private String pickUpLocation;

    private Corp corp;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}