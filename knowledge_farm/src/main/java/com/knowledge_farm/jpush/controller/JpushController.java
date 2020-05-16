package com.knowledge_farm.jpush.controller;

import com.knowledge_farm.jpush.service.JpushService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @ClassName JpushController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-29 18:22
 */
@Controller
public class JpushController {
    @Resource
    private JpushService jpushService;

}
