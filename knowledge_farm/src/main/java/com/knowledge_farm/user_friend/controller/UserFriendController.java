package com.knowledge_farm.user_friend.controller;

import com.knowledge_farm.annotation.Task;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.notification.service.NotificationService;
import com.knowledge_farm.user_friend.service.UserFriendServiceImpl;
import com.knowledge_farm.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName UserFriendController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 15:25
 */
@Api(description = "前台好友接口")
@RestController
@RequestMapping("/userfriend")
public class UserFriendController {
    @Resource
    private UserFriendServiceImpl userFriendService;
    @Resource
    private NotificationService notificationService;
    @Value("${file.photoUrl}")
    private String photoUrl;

    /**
     * @Author 张帅华
     * @Description 查询用户的好友列表
     * @Date 23:17 2020/4/10 0010
     * @Param [userId, account, pageNumber, pageSize]
     * @return com.atguigu.farm.util.UserVOPage<com.atguigu.farm.entity.User>
     **/
    @ApiOperation(value = "查询用户的好友列表", notes = "返回值：Page（User）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "account", value = "查询的账号", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageNumber", value = "页码", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", paramType = "query", defaultValue = "4")
    })
    @GetMapping("/findUserFriend")
    public PageUtil<User> findUserFriend(@RequestParam("userId") Integer userId,
                                         @RequestParam(value = "account", required = false) String account,
                                         @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
        Page<User> page = this.userFriendService.findUserFriendPageByAccount(userId, account, pageNumber, pageSize);
        PageUtil<User> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        return pageUtil;
    }

    /**
     * @Author 张帅华
     * @Description 查询所有人
     * @Date 23:28 2020/4/10 0010
     * @Param [account, pageNumber, pageSize]
     * @return com.atguigu.farm.util.UserVOPage<com.atguigu.farm.entity.UserVO>
     **/
    @ApiOperation(value = "查询所有人", notes = "返回值：Page（User）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "account", value = "查询的账号", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageNumber", value = "页码", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", paramType = "query", defaultValue = "4")
    })
    @GetMapping("/findAllUser")
    public PageUtil<User> findAllUser(@RequestParam("userId") Integer userId,
                                      @RequestParam(value = "account", required = false) String account,
                                      @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                      @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize) {
        Page<User> page = this.userFriendService.findAllUserByAccount(userId, account, pageNumber, pageSize);
        PageUtil<User> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        return pageUtil;
    }

    @ApiOperation(value = "添加好友", notes = "返回值：(String)true || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sendAccount", value = "发送方用户账号", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "account", value = "要添加好友的账号", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "notificationId", value = "申请添加好友的消息Id", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/addUserFriend")
    public String addUserFriend(@RequestParam("sendAccount") String sendAccount,
                                @RequestParam("account") String account,
                                @RequestParam("notificationId") Integer notificationId){
        try {
            this.userFriendService.addUserFriend(sendAccount, account, notificationId);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "拒绝添加好友的申请", notes = "返回值：(String)true || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "发送方用户账号", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "notificationId", value = "拒绝添加好友的消息Id", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/refuseUserFriend")
    public String refuseUserFriend(@RequestParam("account") String account, @RequestParam("notificationId") Integer notificationId){
        try {
            this.userFriendService.refuseUserFriend(notificationId);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "删除好友", notes = "返回值：(String)true || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "account", value = "要删除好友的账号", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/deleteUserFriend")
    public String deleteUserFriend(@RequestParam("userId") Integer userId, @RequestParam("account") String account){
        try {
            this.userFriendService.deleteUserFriend(userId, account);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "给好友浇水", notes = "返回值：(String)true || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "friendId", value = "好友Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "landNumber", value = "要浇水的土地号", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/waterForFriend")
    public String waterForFriend(@RequestParam("userId") Integer userId,
                                 @RequestParam("friendId") Integer friendId,
                                 @RequestParam("landNumber") String landNumber,
                                 HttpServletRequest request){
        try {
            int result = this.userFriendService.waterForFriend(userId, friendId, landNumber);
            switch (result){
                case -1:
                    return Result.FALSE;
                case 0:
                    this.notificationService.addWaterForFriendNotification(userId, friendId);
                    return Result.TRUE;
                default:
                    request.setAttribute("StartUserCropGrowJob", new Integer[]{friendId, result, Integer.parseInt(landNumber.substring(4))});
                    this.notificationService.addWaterForFriendNotification(userId, friendId);
                    return Result.TRUE;
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "给好友施肥", notes = "返回值：(String)true || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "friendId", value = "好友Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "landNumber", value = "要施肥的土地号", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("fertilizerForFriend")
    public String fertilizerForFriend(@RequestParam("userId") Integer userId,
                                      @RequestParam("friendId") Integer friendId,
                                      @RequestParam("landNumber") String landNumber){
        try {
            String result = this.userFriendService.fertilizerForFriend(userId, friendId, landNumber);
            this.notificationService.addFertilizerForFriendNotification(userId, friendId);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @GetMapping("/test")
    public String waterForFriend2(
                                 HttpServletRequest request){
        try {
            int result = this.userFriendService.waterForFriend(109, 124, "land1");
            switch (result){
                case -1:
                    return Result.FALSE;
                case 0:
                    this.notificationService.addWaterForFriendNotification(109, 124);
                    return Result.TRUE;
                default:
                    request.setAttribute("StartUserCropGrowJob", new Integer[]{124, result, Integer.parseInt("land1".substring(4))});
                    this.notificationService.addWaterForFriendNotification(109, 124);
                    return Result.TRUE;
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

}
