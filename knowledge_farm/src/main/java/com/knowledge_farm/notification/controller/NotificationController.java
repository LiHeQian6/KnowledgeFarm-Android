package com.knowledge_farm.notification.controller;

import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.notification.service.NotificationService;
import com.knowledge_farm.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    @Value("${file.photoUrl}")
    private String photoUrl;

    @ApiOperation(value = "根据通知类型查询接收到的消息", notes = "返回值：Page（Notification）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "typeId", value = "通知类型", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "pageNumber", value = "页码", dataType = "int", paramType = "form", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", paramType = "form", defaultValue = "4")
    })
    @PostMapping("/findReceivedNotificationByType")
    public PageUtil<Notification> findReceivedNotificationByType(@RequestParam("userId") Integer userId,
                                                                 @RequestParam("typeId") Integer typeId,
                                                                 @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
        Page<Notification> page = this.notificationService.findReceivedByNotificationType(userId, typeId, pageNumber, pageSize);
        PageUtil<Notification> pageUtil = new PageUtil<>(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        return pageUtil;
    }

    @ApiOperation(value = "查询接收到的申请添加好友类型消息", notes = "返回值：Page（Notification）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "pageNumber", value = "页码", dataType = "int", paramType = "form", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", paramType = "form", defaultValue = "4")
    })
    @GetMapping("/findReceivedAddFriendNotification")
    public PageUtil<Notification> findReceivedAddFriendNotification(@RequestParam("userId") Integer userId,
                                                                 @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
        Page<Notification> page = this.notificationService.findReceivedAddFriendNotification(userId, pageNumber, pageSize);
        PageUtil<Notification> pageUtil = new PageUtil<>(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        return pageUtil;
    }

    @ApiOperation(value = "根据通知类型查询已发送的消息", notes = "返回值：Page（Notification）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "typeId", value = "通知类型", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "pageNumber", value = "页码", dataType = "int", paramType = "form", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", paramType = "form", defaultValue = "4")
    })
    @PostMapping("/findSendNotificationByType")
    public PageUtil<Notification> findSendNotificationByType(@RequestParam("userId") Integer userId,
                                                             @RequestParam("typeId") Integer typeId,
                                                             @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
        Page<Notification> page = this.notificationService.findSendByNotificationType(userId, typeId, pageNumber, pageSize);
        PageUtil<Notification> pageUtil = new PageUtil<>(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
//        for(Notification notification : page.getContent()){
//            User from = notification.getFrom();
//            User to = notification.getTo();
//            to.setPassword("");
//            from.setPassword("");
//            if(!(from.getPhoto().substring(0,4)).equals("http")){
//                from.setPhoto(this.photoUrl + from.getPhoto());
//            }
//            if(!(to.getPhoto().substring(0,4)).equals("http")){
//                to.setPhoto(this.photoUrl + to.getPhoto());
//            }
//        }
        pageUtil.setList(page.getContent());
        return pageUtil;
    }

//    @ApiOperation(value = "查询是否有新消息", notes = "返回值：List（boolean）")
//    @GetMapping("/isHavingNewNotification")
//    public List<Boolean> isHavingNewNotification(){
//
//    }

    @ApiOperation(value = "添加加好友的消息记录", notes = "返回值：(String)true：成功 || (String)false；失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "发起加好友的用户id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "account", value = "被加好友的用户账号", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/addUserFriendNotification")
    public String addUserFriendNotification(@RequestParam("userId") Integer userId,
                                            @RequestParam("account") String account,
                                            HttpServletRequest request){
        try {
            Notification notification = this.notificationService.addUserFriendNotification(userId, account);
            request.setAttribute("addFriendNotification", notification);
            return Result.TRUE;
        }catch (Exception e){
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "根据id删除消息记录", notes = "返回值：(String)true：成功 || (String)false；失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "消息记录的id字符串(若有多个id，用逗号分隔开)", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/deleteNotification")
    public String deleteNotification(@RequestParam("ids") String notificationIds){
        try {
            String ids[] = notificationIds.split(",");
            List<Integer> idList = new ArrayList<>();
            for(String id : ids){
                idList.add(Integer.parseInt(id));
            }
            this.notificationService.deleteNotification(idList);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "删除指定类型的消息记录", notes = "返回值：(String)true：成功 || (String)false；失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "typeId", value = "消息类型", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/deleteNotificationByType")
    public String deleteNotificationByType(@RequestParam("userId") Integer userId, @RequestParam("typeId") Integer typeId){
        try {
            this.notificationService.deleteNotificationByType(userId, typeId);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "根据id修改消息状态", notes = "返回值：(String)true：成功 || (String)false；失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "消息记录的id字符串(若有多个id，用逗号分隔开)", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "haveRead", value = "消息状态", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/editNotificationReadStatus")
    public String editNotificationReadStatus(@RequestParam("ids") String notificationIds, @RequestParam("haveRead") Integer haveRead){
        String ids[] = notificationIds.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : ids){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.notificationService.editNotificationReadStatus(idList, haveRead);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

}
