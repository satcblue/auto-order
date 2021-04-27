package cn.satc.order.meican.dto;

import cn.satc.order.meican.dto.model.Dish;
import cn.satc.order.meican.dto.model.Restaurant;
import cn.satc.order.meican.dto.request.AddOrderParam;
import lombok.Data;

/**
 * @author T1940-林浩捷
 * @date 2021/4/26
 * @since 0.0.1
 */
@Data
public class DishOrder {
    private Dish dish;
    private AddOrderParam addOrderParam;
}
