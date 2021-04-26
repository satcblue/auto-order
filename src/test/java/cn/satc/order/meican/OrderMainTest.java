package cn.satc.order.meican;

import cn.satc.order.meican.dto.model.Member;
import cn.satc.order.meican.service.OauthService;
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
class OrderMainTest {

    @Autowired
    private OrderMain orderMain;

    @Autowired
    private OauthService oauthService;

    @Test
    public void testOrder() {
        Member member = new Member();
        member.setUsername("15118658057");
        member.setPassword("13828148868Lhj");
        oauthService.loginByUsernameAndPassword(member);
        orderMain.order(member);
    }
}