package cn.satc.order.meican.service;

import cn.satc.order.meican.dto.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author T1940-林浩捷
 * @date 2021/4/26
 * @since
 */
@SpringBootTest
class PreorderServiceTest {

    @Autowired
    private PreorderService preorderService;

    @Autowired
    private OauthService oauthService;


    @Test
    public void testGetAccountsEntrance() {
        Member member = new Member();
        member.setUsername("15118658057");
        member.setPassword("13828148868Lhj");
        member = oauthService.loginByUsernameAndPassword(member);
        preorderService.getAccountsEntrance(member);
    }

    @Test
    public void testGetCalendarList() {
        Member member = new Member();
        member.setUsername("15118658057");
        member.setPassword("13828148868Lhj");
        member = oauthService.loginByUsernameAndPassword(member);
        preorderService.getCalendarList(member);
    }
}