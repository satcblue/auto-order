package cn.satc.order.meican.dto.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author sunhuadong
 * @date 2020/5/14 1:20 上午
 */
@Data
public class Dish {

    private Long id;

    private String name;

    private int priceInCent;

    private String priceString;

    private int originalPriceInCent;

    private Restaurant restaurant;

    private boolean isSection;

    private String actionRequiredLevel;

    private String actionRequiredReason;

    private Long dishSectionId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}