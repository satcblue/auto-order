package cn.satc.order.meican.service;

import cn.hutool.core.date.DateUtil;
import cn.satc.order.exception.BusinessException;
import cn.satc.order.meican.constant.UrlConstant;
import cn.satc.order.meican.dto.*;
import cn.satc.order.meican.dto.model.Corp;
import cn.satc.order.meican.dto.model.Dish;
import cn.satc.order.meican.dto.model.Member;
import cn.satc.order.meican.dto.request.AddOrderParam;
import cn.satc.order.meican.dto.request.CartUpdateParam;
import cn.satc.order.meican.dto.response.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author sunhuadong
 * @date 2020/5/14 1:13 上午
 */
@Service
@Slf4j
public class PreorderService {

    @Resource
    private OkHttpClient okHttpClient;

    public AccountsEntrance getAccountsEntrance(@Nonnull Member member) {
        Request request = new Request.Builder()
                .url(UrlConstant.ACCOUNTS_ENTRANCE_URL)
                .header("Cookie", member.getCookies()).get().build();
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                if (!Objects.equals(responseBody.contentType(), MediaType.parse("text/html; charset=utf-8"))) {
                    String body = responseBody.string();
                    BaseResponse<AccountsEntrance> accountsEntranceBaseResponse = JSON.parseObject(body, new TypeReference<BaseResponse<AccountsEntrance>>() {
                    });
                    return accountsEntranceBaseResponse.getData();
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.getAccountsEntrance Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    public List<Calendar> getCalendarList(Member member) {
        return getCalendarList(member, new Date());
    }

    public List<Calendar> getCalendarList(Member member, Date date) {
        return getCalendarList(member, date, date);
    }

    public List<Calendar> getCalendarList(Member member, Date beginDate, Date endDate) {
        HttpUrl httpUrl = new Request.Builder().url(UrlConstant.CALENDAR_ITEMS_LIST_URL).build().url().newBuilder()
                .addEncodedQueryParameter("beginDate", DateUtil.format(beginDate, "yyyy-MM-dd"))
                .addEncodedQueryParameter("endDate", DateUtil.format(endDate, "yyyy-MM-dd"))
                .addEncodedQueryParameter("withOrderDetail", "false")
                .build();
        Request request = new Request.Builder().url(httpUrl).header("Cookie", member.getCookies()).get().build();
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                if (Objects.equals(responseBody.contentType(), MediaType.parse("application/json"))) {
                    String body = responseBody.string();
                    CalendarResponse calendarResponse = JSON.parseObject(body, CalendarResponse.class);
                    return getCalendarList(calendarResponse);
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.getCalendar Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    private List<Calendar> getCalendarList(CalendarResponse calendarResponse) {
        List<Calendar> calendars = Lists.newArrayList();
        calendarResponse.getDateList().forEach(date -> calendars.addAll(date.getCalendarItemList()));
        return calendars;
    }

    /**
     *
     * @param member 有cookie就行
     * @param tabUniqueId 为Calendar->userTab->uniqueId的值
     * @param targetTime 为 Calendar -> targetTime 的 yyyy-MM-dd HH:MM 格式
     * @return
     */
    public RestaurantResponse getRestaurant(Member member, String tabUniqueId, String targetTime) {
        HttpUrl httpUrl = new Request.Builder().url(UrlConstant.RESTAURANTS_LIST_URL).build().url().newBuilder()
                .addEncodedQueryParameter("tabUniqueId", tabUniqueId)
                .addEncodedQueryParameter("targetTime", targetTime)
                .build();
        Request request = new Request.Builder().url(httpUrl).header("Cookie", member.getCookies()).get().build();
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                if (!Objects.equals(responseBody.contentType(), MediaType.parse("text/html; charset=utf-8"))) {
                    String body = responseBody.string();
                    return JSON.parseObject(body, RestaurantResponse.class);
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.getRestaurant Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    public Corp getCorp(Member member, String namespace) {
        HttpUrl httpUrl = new Request.Builder().url(UrlConstant.CORPS_SHOW_URL).build().url().newBuilder()
                .addQueryParameter("namespace", namespace)
                .build();
        Request request = new Request.Builder().url(httpUrl).header("Cookie", member.getCookies()).get().build();
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                if (!Objects.equals(responseBody.contentType(), MediaType.parse("text/html; charset=utf-8"))) {
                    String body = responseBody.string();
                    return JSON.parseObject(body, Corp.class);
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.getCorp Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    /**
     * 获取指定时间的菜单
     *
     * @param member      member
     * @param tabUniqueId tabUniqueId
     * @param targetTime  targetTime
     * @param restaurantUniqueId re
     * @return return
     */
    public DishResponse getDishResponse(Member member, String tabUniqueId, String targetTime, String restaurantUniqueId) {
        HttpUrl httpUrl = new Request.Builder().url(UrlConstant.RESTAURANTS_DISH_LIST_URL).build().url().newBuilder()
                .addEncodedQueryParameter("tabUniqueId", tabUniqueId)
                .addEncodedQueryParameter("targetTime", targetTime)
                .addEncodedQueryParameter("restaurantUniqueId", restaurantUniqueId)
                .build();
        Request request = new Request.Builder().url(httpUrl).header("Cookie", member.getCookies()).get().build();
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                if (!Objects.equals(responseBody.contentType(), MediaType.parse("text/html; charset=utf-8"))) {
                    String body = responseBody.string();
                    return JSON.parseObject(body, DishResponse.class);
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.getDishResponse Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    public CartQueryResponse cartQuery(Member member, String closeTime, String tabUUID) {
        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("closeTime", closeTime)
                .addEncoded("tabUUID", tabUUID)
                .build();
        Request request = new Request.Builder().url(UrlConstant.CART_QUERY_URL).header("Cookie", member.getCookies()).post(requestBody).build();
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                if (!Objects.equals(responseBody.contentType(), MediaType.parse("text/html; charset=utf-8"))) {
                    String body = responseBody.string();
                    return JSON.parseObject(body, CartQueryResponse.class);
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.cartQuery Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    public AddOrderResponse addOrder(Member member, AddOrderParam param) {
        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("corpAddressRemark", param.getCorpAddressRemark())
                .addEncoded("corpAddressUniqueId", param.getCorpAddressRemark())
                .addEncoded("order", param.getOrder())
                .addEncoded("remarks", param.getRemarks())
                .addEncoded("tabUniqueId", param.getTabUniqueId())
                .addEncoded("targetTime", param.getTargetTime())
                .addEncoded("userAddressUniqueId", param.getUserAddressUniqueId())
                .build();
        Request request = new Request.Builder().url(UrlConstant.ORDERS_ADD_URL).header("Cookie", member.getCookies()).post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 400) {
                throw new BusinessException(BusinessException.BusinessStatus.networkException);
            }
            ResponseBody responseBody = response.body();
            if (Objects.nonNull(responseBody)) {
                if (!Objects.equals(responseBody.contentType(), MediaType.parse("text/html; charset=utf-8"))) {
                    String body = responseBody.string();
                    return JSON.parseObject(body, AddOrderResponse.class);
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.addOrder Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    public MultiCorpAddress getMultiCorpAddress(Member member, String namespace) {
        HttpUrl httpUrl = new Request.Builder().url(UrlConstant.GET_MULTI_CORP_ADDRESS_URL).build().url().newBuilder()
                .addEncodedQueryParameter("namespace", namespace)
                .build();
        Request request = new Request.Builder().url(httpUrl).header("Cookie", member.getCookies()).get().build();
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                if (!Objects.equals(responseBody.contentType(), MediaType.parse("text/html; charset=utf-8"))) {
                    String body = responseBody.string();
                    BaseResponse<MultiCorpAddress> multiCorpAddressBaseResponse = JSON.parseObject(body, new TypeReference<BaseResponse<MultiCorpAddress>>() {
                    });
                    return multiCorpAddressBaseResponse.getData();
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.getMultiCorpAddress Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    public Order getOrder(Member member, String uniqueId, String type, boolean progressMarkdownSupport) {
        HttpUrl httpUrl = new Request.Builder().url(UrlConstant.ORDERS_SHOW_URL).build().url().newBuilder()
                .addEncodedQueryParameter("uniqueId", uniqueId)
                .addEncodedQueryParameter("type", type)
                .addEncodedQueryParameter("progressMarkdownSupport", String.valueOf(progressMarkdownSupport))
                .addEncodedQueryParameter("x", String.valueOf(System.currentTimeMillis()))
                .build();
        Request request = new Request.Builder().url(httpUrl).header("Cookie", member.getCookies()).get().build();
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                if (!Objects.equals(responseBody.contentType(), MediaType.parse("text/html; charset=utf-8"))) {
                    String body = responseBody.string();
                    return JSON.parseObject(body, Order.class);
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.getOrder Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }

    public ClosetOrder getClosetOrder(Member member, String uniqueId) {
        HttpUrl httpUrl = new Request.Builder().url(UrlConstant.ORDERS_CLOSET_SHOW_URL).build().url().newBuilder()
                .addEncodedQueryParameter("uniqueId", uniqueId)
                .build();
        Request request = new Request.Builder().url(httpUrl).header("Cookie", member.getCookies()).get().build();
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                if (!Objects.equals(responseBody.contentType(), MediaType.parse("text/html; charset=utf-8"))) {
                    String body = responseBody.string();
                    BaseResponse<ClosetOrder> closetOrderBaseResponse = JSON.parseObject(body, new TypeReference<BaseResponse<ClosetOrder>>() {
                    });
                    return closetOrderBaseResponse.getData();
                }
                throw new BusinessException(BusinessException.BusinessStatus.cookieExpire);
            }
        } catch (IOException e) {
            log.error("PreorderService.getClosetOrder Exception:{}", e.getMessage());
        }
        throw new BusinessException(BusinessException.BusinessStatus.networkException);
    }
}
