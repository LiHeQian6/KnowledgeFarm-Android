package com.knowledge_farm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KnowledgeFarmApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("http://".length());
        System.out.println("http://".substring(3,5));
    }

}
