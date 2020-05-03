package com.knowledge_farm.front.notification.controller;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.knowledge_farm.entity.JpushConfig;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.front.notification.service.FrontNotificationService;
import com.knowledge_farm.jpush.service.JpushService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @ClassName FrontNotificationController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-30 16:45
 */
@Api(description = "后台通知接口")
@Controller
@RequestMapping("/admin/notification")
public class FrontNotificationController {
    @Resource
    private FrontNotificationService frontNotificationService;
    private static final Logger LOG = LoggerFactory.getLogger(JpushService.class);
    @Resource
    private JpushConfig jpushConfig;// 注入配置信息
    @Resource
    private JpushService jpushService;

    @GetMapping("/toNotificationCustomMessage")
    public String toNotificationCustomMessage(){
        return "notification-custom-message";
    }

    @GetMapping("/toNotification")
    public String toNotification(){
        return "notification-push";
    }

    @ResponseBody
    @PostMapping("/sendCustomMessage")
    public String sendCustomMessage(@RequestParam("title") String title,
                                    @RequestParam("content") String content,
                                    @RequestParam("extra") String extra,
                                    @RequestParam("alias") String alias) {
        List<String> list = new ArrayList<>();
        String aliasArr[] = new String[0];
        for(String arr : alias.split(",")){
            if(!arr.equals("")){
                list.add(arr);
            }
        }
        if(list.size() != 0){
            aliasArr = new String[list.size()];
            for(int i = 0;i < list.size();i++){
                aliasArr[i] = list.get(i);
            }
        }
        Type type = new TypeToken<Map>(){}.getType();
        Map extrasMap = new Gson().fromJson(extra, type);

        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setTimeToLive(Long.valueOf(jpushConfig.getLiveTime()));
        JPushClient jpushClient = new JPushClient(jpushConfig.getMasterSecret(), jpushConfig.getAppkey(), null, clientConfig);
        PushPayload payload = this.jpushService.buildCustomPushPayload(title, content, extrasMap, aliasArr);
        try {
            PushResult result = this.frontNotificationService.sendCustomMessage(jpushClient, payload, title, content, extrasMap, aliasArr);
            LOG.info("极光推送结果 - " + result + ",接收推送的别名列表:" + String.join(",", aliasArr));
            return Result.SUCCEED;
        } catch (APIConnectionException e) {
            LOG.error("极光推送连接错误，请稍后重试 ", e);
            LOG.error("Sendno: " + payload.getSendno());
            return Result.FALSE;
        } catch (APIRequestException e) {
            LOG.error("极光服务器响应出错，请修复！ ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.info("以下存在不能识别的别名: " + String.join(",", aliasArr));
            LOG.error("Sendno: " + payload.getSendno());
            return Result.NOT_EXIST;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @ResponseBody
    @PostMapping("/sendPush")
    public String sendPush(@RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @RequestParam("extra") String extra,
                           @RequestParam("alias") String alias) {
        List<String> list = new ArrayList<>();
        String aliasArr[] = new String[0];
        for(String arr : alias.split(",")){
            if(!arr.equals("")){
                list.add(arr);
            }
        }
        if(list.size() != 0){
            aliasArr = new String[list.size()];
            for(int i = 0;i < list.size();i++){
                aliasArr[i] = list.get(i);
            }
        }
        Type type = new TypeToken<Map>(){}.getType();
        Map extrasMap = new Gson().fromJson(extra, type);

        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setTimeToLive(Long.valueOf(jpushConfig.getLiveTime()));
        JPushClient jpushClient = new JPushClient(jpushConfig.getMasterSecret(), jpushConfig.getAppkey(), null, clientConfig);
        PushPayload payload = this.jpushService.buildPushPayload(title, content, extrasMap, aliasArr);
        try {
            PushResult result = this.frontNotificationService.sendPush(jpushClient, payload, title, content, extrasMap, aliasArr);
            LOG.info("极光推送结果 - " + result + ",接收推送的别名列表:" + String.join(",", aliasArr));
            return Result.SUCCEED;
        } catch (APIConnectionException e) {
            LOG.error("极光推送连接错误，请稍后重试 ", e);
            LOG.error("Sendno: " + payload.getSendno());
            return Result.FALSE;
        } catch (APIRequestException e) {
            LOG.error("极光服务器响应出错，请修复！ ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.info("以下存在不能识别的别名: " + String.join(",", aliasArr));
            LOG.error("Sendno: " + payload.getSendno());
            return Result.NOT_EXIST;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

}
