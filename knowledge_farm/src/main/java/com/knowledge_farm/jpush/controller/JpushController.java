package com.knowledge_farm.jpush.controller;

import com.knowledge_farm.jpush.service.JpushService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @ClassName Controller
 * @Description
 * @Author 张帅华
 * @Date 2020-04-20 10:37
 */
@RestController
public class JpushController {
    @Resource
    private JpushService jpushService;

    @RequestMapping("/send")
    public String send(){
        this.jpushService.sendPushWithCallback("test_second", "Can you receive the message?", new HashMap<String, String>(), "alias1");
        return "ok";
    }

}
