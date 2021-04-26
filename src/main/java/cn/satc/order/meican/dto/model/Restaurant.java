package cn.satc.order.meican.dto.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author sunhuadong
 * @date 2020/5/15 12:49 上午
 */
@Data

public class Restaurant {


    private String uniqueId;

    private String name;

    private String tel;

    private double latitude;

    private double longitude;

    private int rating;

    private String deliveryRangeMeter;

    private int minimumOrder;

    private String warning;

    private String openingTime;

    private boolean onlinePayment;

    private boolean open;

    private int availableDishCount;

    private int dishLimit;

    private int restaurantStatus;

    private boolean remarkEnabled;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
