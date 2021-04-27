package cn.satc.order.meican;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.satc.order.meican.dto.Calendar;
import cn.satc.order.meican.dto.DishOrder;
import cn.satc.order.meican.dto.MultiCorpAddress;
import cn.satc.order.meican.dto.model.Dish;
import cn.satc.order.meican.dto.model.Member;
import cn.satc.order.meican.dto.model.Restaurant;
import cn.satc.order.meican.dto.request.AddOrderParam;
import cn.satc.order.meican.dto.response.DishResponse;
import cn.satc.order.meican.dto.response.RestaurantResponse;
import cn.satc.order.meican.service.OauthService;
import cn.satc.order.meican.service.PreorderService;
import cn.satc.order.notify.tencent.SmsMessage;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author T1940-林浩捷
 * @date 2021/4/26
 * @since 0.0.1
 */
@Component
public class OrderMain {

    private PreorderService preorderService;
    private SmsMessage smsMessage;
    private OauthService oauthService;

    private OrderMain orderMain;


    public void order(Member member) {
        if (member == null || CharSequenceUtil.isBlank(member.getCookies())) {
            return;
        }

        Date nextDay = Date.from(ZonedDateTime.of(LocalDateTimeUtil.beginOfDay(LocalDateTime.now().plusDays(1)),
                ZoneOffset.ofHours(8)).toInstant());

        // 2. 获取明天可点餐时间段(北京时间)
        List<Calendar> calendars = preorderService.getCalendarList(member, nextDay);
        // 过滤非可以点单的时间段
        calendars = calendars.stream()
                .filter(calendar -> "AVAILABLE".equals(calendar.getStatus()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(calendars)) {
            sendMsg(new String[]{"已经点餐或不用点餐", "已经点餐或不用点餐"});
            return;
        }

        List<String> notifyMessage = new ArrayList<>();
        Multimap<Calendar, DishOrder> dishOrderMultimap = MultimapBuilder
                .ListMultimapBuilder.hashKeys().arrayListValues().build();

        for (Calendar calendar : calendars) {
            String tabUnionId = calendar.getUserTab().getUniqueId();
            String targetTime = DateUtil.format(new Date(calendar.getTargetTime()), "yyyy-MM-dd HH:MM");
            String nameSpace = calendar.getUserTab().getCorp().getNamespace();

            // 获取可选餐厅
            RestaurantResponse restaurantResponse = preorderService.getRestaurant(member,
                    tabUnionId, targetTime);
            List<Restaurant> restaurantList = restaurantResponse.getRestaurantList();
            if (CollUtil.isEmpty(restaurantList)) {
                notifyMessage.add("没有可选择的餐厅");
                continue;
            }

            // 获取餐品送达位置信息
            MultiCorpAddress multiCorpAddress = preorderService
                    .getMultiCorpAddress(member, calendar.getUserTab().getCorp().getNamespace());

            String corpAddressUniqueId = multiCorpAddress.getAddressList().get(0).getFinalValue().getUniqueId();

            // 获取餐厅下的菜单
            for (Restaurant restaurant : restaurantList) {
                DishResponse dishResponse = preorderService.getDishResponse(member, tabUnionId, targetTime,
                        restaurant.getUniqueId());
                List<Dish> dishList = dishResponse.getDishList();
                if (CollUtil.isEmpty(dishList)) {
                    continue;
                }
                dishList.stream()
                        .filter(dish -> !dish.isSection())
                        .filter(dish -> dish.getPriceInCent() <= 1500)
                        .forEach(dish -> {
                            AddOrderParam addOrderParam = new AddOrderParam();
                            JSONObject orderJsonObject = new JSONObject();
                            orderJsonObject.put("count", 1);
                            orderJsonObject.put("dishId", dish.getId());
                            JSONArray orderJsonArray = new JSONArray();
                            orderJsonArray.add(orderJsonObject);
                            addOrderParam.setOrder(orderJsonArray.toJSONString());

                            JSONObject remarkJsonObject = new JSONObject();
                            remarkJsonObject.put("dishId", dish.getId());
                            remarkJsonObject.put("remark", "");
                            JSONArray remarkJsonArray = new JSONArray();
                            remarkJsonArray.add(remarkJsonObject);
                            addOrderParam.setRemarks(remarkJsonArray.toJSONString());

                            addOrderParam.setCorpAddressRemark("");
                            addOrderParam.setCorpAddressUniqueId(corpAddressUniqueId);
                            addOrderParam.setTargetTime(targetTime);
                            addOrderParam.setTabUniqueId(tabUnionId);
                            addOrderParam.setUserAddressUniqueId(nameSpace);
                            DishOrder dishOrder = new DishOrder();
                            dishOrder.setDish(dish);
                            dishOrder.setAddOrderParam(addOrderParam);
                            dishOrderMultimap.put(calendar, dishOrder);
                        });
            }
        }

        dishOrderMultimap.keySet().forEach(calendar -> {
            DishOrder dos = dishOrderMultimap.get(calendar).stream()
                    .filter(Objects::nonNull)
                    .filter(dishOrder -> dishOrder.getDish() != null)
                    .filter(dishOrder -> dishOrder.getAddOrderParam() != null)
                    .filter(dishOrder -> StrUtil.isNotBlank(dishOrder.getDish().getName()))
                    .filter(dishOrder -> !dishOrder.getDish().getName().contains("辣"))
                    .findAny().orElse(null);
            if (dos == null) {
                notifyMessage.add("请手动点餐");
            }
            AddOrderParam addOrderParam = dos.getAddOrderParam();
            preorderService.addOrder(member, addOrderParam);
            notifyMessage.add(dos.getDish().getName());
        });
        // 3. 点餐
        // 获取餐厅列表


        // 4. 提醒
        sendMsg(notifyMessage.toArray(new String[0]));
    }

    private void sendMsg(String[] msg) {
        if (smsMessage == null) {
            return;
        }
        // 模板值数量不匹配
        if (msg == null || msg.length < 2) {
            return;
        }
        String phone = System.getProperty("SATC_ORDER_PHONE");
        if (CharSequenceUtil.isBlank(phone)) {
            return;
        }
        smsMessage.notify(Arrays.copyOf(msg, 2), phone);
    }

    @Autowired
    public void setPreorderService(PreorderService preorderService) {
        this.preorderService = preorderService;
    }

    @Autowired
    public void setSmsMessage(SmsMessage smsMessage) {
        this.smsMessage = smsMessage;
    }

    @Autowired
    public void setOauthService(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @Autowired
    public void setOrderMain(OrderMain orderMain) {
        this.orderMain = orderMain;
    }

    @PostConstruct
    public void init() {
        Member member = new Member();
        member.setUsername(System.getProperty("MEI_CAN_USERNAME"));
        member.setPassword(System.getProperty("MEI_CAN_PASSWORD"));
        this.oauthService.loginByUsernameAndPassword(member);
        this.orderMain.order(member);
//        System.exit(0);
    }
}
