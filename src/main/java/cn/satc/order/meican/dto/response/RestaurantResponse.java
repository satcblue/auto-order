package cn.satc.order.meican.dto.response;


import cn.satc.order.meican.dto.model.Restaurant;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.List;

/**
 * @author sunhuadong
 * @date 2020/5/14 0:53 上午
 */
@Data
public class RestaurantResponse {
    private boolean noMore;
    private String targetTime;
    private List<Restaurant> restaurantList;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}