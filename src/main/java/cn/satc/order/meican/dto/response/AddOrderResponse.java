package cn.satc.order.meican.dto.response;

import cn.satc.order.meican.dto.Order;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author sunhuadong
 * @date 2020/5/18 1:14 下午
 */
@Data
public class AddOrderResponse {
    private String message;
    private Order order;
    private String status;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
