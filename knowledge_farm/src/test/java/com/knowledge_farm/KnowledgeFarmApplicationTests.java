package com.knowledge_farm;

import com.knowledge_farm.front.notification.service.FrontNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
class KnowledgeFarmApplicationTests {
    @Resource
    private FrontNotificationService frontNotificationService;

    @Test
    void contextLoads() {

    }

}
