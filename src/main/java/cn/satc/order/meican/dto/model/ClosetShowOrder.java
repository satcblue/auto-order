package cn.satc.order.meican.dto.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Date;

/**
 * @author sunhuadong
 * @date 2020/5/19 2:33 上午
 */
@Data
public class ClosetShowOrder {
    private Long id;

    private Member member;

    private String closetShow;

    private Date created;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
