package com.knowledge_farm.notification.controller;

import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.notification.service.NotificationService;
import com.knowledge_farm.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName Controller
 * @Description
 * @Author 张帅华
 * @Date 2020-04-21 16:46
 */
@Api(description = "前台通知接口")
@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Resource
    private NotificationService notificationService;

    @ApiOperation(value = "根据通知类型查询接收到的消息", notes = "返回值：Page（Notification）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeId", value = "通知类型", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "pageNumber", value = "页码", dataType = "int", paramType = "form", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", paramType = "form", defaultValue = "4")
    })
    @PostMapping("/findReceivedNotificationByType")
    public PageUtil<Notification> findReceivedNotificationByType(@RequestParam("typeId") Integer typeId,
                                                                 @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                                 HttpSession session){
        PageUtil<Notification> pageUtil = new PageUtil<>(pageNumber, pageSize);
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId != null) {
                Page<Notification> page = this.notificationService.findReceivedByNotificationType(userId, typeId, pageNumber, pageSize);
                pageUtil.setTotalCount((int) page.getTotalElements());
                pageUtil.setList(page.getContent());
                return pageUtil;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        pageUtil.setTotalCount(0);
        pageUtil.setList(new ArrayList<>());
        return pageUtil;
    }

    @ApiOperation(value = "根据通知类型查询已发送的消息", notes = "返回值：Page（Notification）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeId", value = "通知类型", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "pageNumber", value = "页码", dataType = "int", paramType = "form", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", paramType = "form", defaultValue = "4")
    })
    @PostMapping("/findSendNotificationByType")
    public PageUtil<Notification> findSendNotificationByType(@RequestParam("typeId") Integer typeId,
                                                             @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                             HttpSession session){
        PageUtil<Notification> pageUtil = new PageUtil<>(pageNumber, pageSize);
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId != null) {
            Page<Notification> page = this.notificationService.findSendByNotificationType(userId, typeId, pageNumber, pageSize);
            pageUtil.setTotalCount((int) page.getTotalElements());
            pageUtil.setList(page.getContent());
            return pageUtil;
        }
        pageUtil.setTotalCount(0);
        pageUtil.setList(new ArrayList<>());
        return pageUtil;
    }

    @ApiOperation(value = "查询是否有新消息", notes = "返回值：List（boolean）：1、system；2、receive；3、send；4、message")
    @GetMapping("/isHavingNewNotification")
    public List<Boolean> isHavingNewNotification(HttpSession session){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId != null) {
                return this.notificationService.isHavingNewNotification(userId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @ApiOperation(value = "添加加好友的消息记录", notes = "返回值：(String)true：成功 || (String)false；失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "被加好友的用户账号", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/addUserFriendNotification")
    public String addUserFriendNotification(@RequestParam("account") String account, HttpSession session){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId != null) {
                this.notificationService.addUserFriendNotification(userId, account);
                return Result.TRUE;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.FALSE;
    }

//    @ApiOperation(value = "根据id删除消息记录", notes = "返回值：(String)true：成功 || (String)false；失败")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "ids", value = "消息记录的id字符串(若有多个id，用逗号分隔开)", dataType = "String", paramType = "query", required = true)
//    })
//    @GetMapping("/deleteNotification")
//    public String deleteNotification(@RequestParam("ids") String notificationIds){
//        try {
//            String ids[] = notificationIds.split(",");
//            List<Integer> idList = new ArrayList<>();
//            for(String id : ids){
//                idList.add(Integer.parseInt(id));
//            }
//            this.notificationService.deleteNotification(idList);
//            return Result.TRUE;
//        }catch (Exception e){
//            e.printStackTrace();
//            return Result.FALSE;
//        }
//    }

    @ApiOperation(value = "删除指定类型的消息记录", notes = "返回值：(String)true：成功 || (String)false；失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeId", value = "消息类型", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/deleteNotificationByType")
    public String deleteNotificationByType(@RequestParam("typeId") Integer typeId, HttpSession session){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId != null) {
                this.notificationService.deleteNotificationByType(userId, typeId);
                return Result.TRUE;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.FALSE;
    }

//    @ApiOperation(value = "根据id修改消息状态", notes = "返回值：(String)true：成功 || (String)false；失败")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "ids", value = "消息记录的id字符串(若有多个id，用逗号分隔开)", dataType = "string", paramType = "query", required = true),
//            @ApiImplicitParam(name = "haveRead", value = "消息状态", dataType = "int", paramType = "query", required = true)
//    })
//    @GetMapping("/editNotificationReadStatus")
//    public String editNotificationReadStatus(@RequestParam("ids") String notificationIds, @RequestParam("haveRead") Integer haveRead){
//        String ids[] = notificationIds.split(",");
//        List<Integer> idList = new ArrayList<>();
//        for(String id : ids){
//            idList.add(Integer.parseInt(id));
//        }
//        try {
//            this.notificationService.editNotificationReadStatus(idList, haveRead);
//            return Result.TRUE;
//        }catch (Exception e){
//            e.printStackTrace();
//            return Result.FALSE;
//        }
//    }

    @ApiOperation(value = "修改消息状态", notes = "返回值：(String)true：成功 || (String)false；失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "对方用户的id字符串(若有多个id，用逗号分隔开)", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "haveRead", value = "消息状态", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "typeId", value = "消息类型", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "flag", value = "1：表示ids是接收方id（to） || 0：表示ids是发送方id(from)", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/editNotificationReadStatus")
    public String editNotificationReadStatus(@RequestParam("ids") String notificationIds,
                                             @RequestParam("haveRead") Integer haveRead,
                                             @RequestParam("typeId") Integer typeId,
                                             @RequestParam("flag") Integer flag,
                                             HttpSession session){
        String ids[] = notificationIds.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : ids){
            idList.add(Integer.parseInt(id));
        }
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId != null) {
                this.notificationService.editNotificationReadStatus(idList, userId, haveRead, typeId, flag);
                return Result.TRUE;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.FALSE;
    }

}
