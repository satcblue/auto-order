package cn.satc.order.meican.dto.model;

import cn.satc.order.meican.dto.Department;
import cn.satc.order.meican.dto.OpeningTime;
import cn.satc.order.meican.dto.Payment;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.List;


/**
 * @author sunhuadong
 * @date 2020/5/14 11:09 下午
 */
@Data
public class Corp {

    private String uniqueId;

    private String name;

    private String namespace;

    private boolean hasPostbox;

    private Long corpId;

    private boolean priceVisible;

    private boolean showPrice;

    private int priceLimitInCent;

    private long dishLimit;

    private boolean publicAccessible;

    private List<Department> departmentList;

    private List<Address> addressList;

    private String logoImageUrl;

    private String status;

    private boolean corpAdmin;

    private List<OpeningTime> openingTimeList;

    private boolean hasMealPointGroup;

    private List<String> mealPointList;

    private boolean alwaysOpen;

    private boolean useMultiCorpAddress;

    private boolean useCorpAddressRemark;

    private boolean useSpecialAccount;

    private boolean useCloset;

    private boolean remarkEnabled;

    private List<Payment> includedPayments;

    private List<Payment> excludedPayments;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
