package cn.satc.order.meican.dto.response;

import cn.satc.order.meican.dto.model.Dish;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.List;

/**
 * @author sunhuadong
 * @date 2020/5/15 1:20 下午
 */
@Data
public class DishResponse {
    private List<Dish> dishList;
    private String uniqueId;
    private boolean showPrice;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
