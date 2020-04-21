package com.knowledge_farm.user_friend.controller;

import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserVO;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_friend.service.UserFriendServiceImpl;
import com.knowledge_farm.util.PageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserFriendController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 15:25
 */
@RestController
@RequestMapping("/userfriend")
@PropertySource(value = {"classpath:photo.properties"})
public class UserFriendController {
    @Resource
    private UserFriendServiceImpl userFriendService;
    @Value("${file.photoUrl}")
    private String photoUrl;

    /**
     * @Author 张帅华
     * @Description 查询用户的好友列表
     * @Date 23:17 2020/4/10 0010
     * @Param [userId, account, pageNumber, pageSize]
     * @return com.atguigu.farm.util.UserVOPage<com.atguigu.farm.entity.User>
     **/
    @RequestMapping("/findUserFriend")
    public PageUtil<UserVO> findUserFriend(@RequestParam("userId") Integer userId,
                                           @RequestParam(value = "account", required = false) String account,
                                           @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                           @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
        Page<User> page = this.userFriendService.findUserFriendPageByAccount(userId, account, pageNumber, pageSize);
        PageUtil<UserVO> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());

        List<UserVO> userVOS = new ArrayList<>();
        for(User user : page.getContent()){
            UserVO userVO = varyUserToUserVO(user);
            userVO.setPhoto(this.photoUrl + userVO.getPhoto());
            userVOS.add(userVO);
        }

        pageUtil.setList(userVOS);
        return pageUtil;
    }

    /**
     * @Author 张帅华
     * @Description 查询所有人
     * @Date 23:28 2020/4/10 0010
     * @Param [account, pageNumber, pageSize]
     * @return com.atguigu.farm.util.UserVOPage<com.atguigu.farm.entity.UserVO>
     **/
    @RequestMapping("/findAllUser")
    public PageUtil<UserVO> findAllUser(@RequestParam(value = "account", required = false) String account,
                                        @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize) {
        Page<User> page = this.userFriendService.findAllUserByAccount(account, pageNumber, pageSize);
        PageUtil<UserVO> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());

        List<UserVO> userVOS = new ArrayList<>();
        for(User user : page.getContent()){
            UserVO userVO = varyUserToUserVO(user);
            userVO.setPhoto(this.photoUrl + userVO.getPhoto());
            userVOS.add(userVO);
        }

        pageUtil.setList(userVOS);
        return pageUtil;
    }

    @RequestMapping("/findReceivedNotificationByType")
    public PageUtil<Notification> findReceivedNotificationByType(@RequestParam("userId") Integer userId,
                                                            @RequestParam("typeId") Integer typeId,
                                                            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
        Page<Notification> page = this.userFriendService.findReceivedNotificationByType(userId, typeId, pageNumber, pageSize);
        PageUtil<Notification> pageUtil = new PageUtil<>(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        for(Notification notification : page.getContent()){
            User from = notification.getFrom();
            User to = notification.getTo();
            from.setPhoto(this.photoUrl + from.getPhoto());
            if(!to.getPhoto().contains("http://")){
                to.setPhoto(this.photoUrl + to.getPhoto());
            }
        }
        pageUtil.setList(page.getContent());
        return pageUtil;
    }

    @RequestMapping("/addUserFriendNotification")
    public String addUserFriendNotification(@RequestParam("userId") Integer userId, @RequestParam("account") String account){
        return this.userFriendService.addUserFriendNotification(userId, account);
    }

    @RequestMapping("/addUserFriend")
    public String addUserFriend(@RequestParam("userId") Integer userId, @RequestParam("account") String account){
        return this.userFriendService.addUserFriend(userId, account);
    }

    @RequestMapping("/deleteUserFriend")
    public String deleteUserFriend(@RequestParam("userId") Integer userId, @RequestParam("account") String account){
        return this.userFriendService.deleteUserFriend(userId, account);
    }

    /**
     * @Author 张帅华
     * @Description User -> UserVO
     * @Date 23:29 2020/4/10 0010
     * @Param [user]
     * @return com.atguigu.farm.entity.UserVO
     **/
    public UserVO varyUserToUserVO(User user){
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setAccount(user.getAccount());
        userVO.setNickName(user.getNickName());
        userVO.setPhoto(user.getPhoto());
        userVO.setEmail(user.getEmail());
        userVO.setLevel(user.getLevel());
        userVO.setExperience(user.getExperience());
        userVO.setGrade(user.getGrade());
        userVO.setMoney(user.getMoney());
        userVO.setMathRewardCount(user.getMathRewardCount());
        userVO.setEnglishRewardCount(user.getEnglishRewardCount());
        userVO.setChineseRewardCount(user.getChineseRewardCount());
        userVO.setWater(user.getWater());
        userVO.setFertilizer(user.getFertilizer());
        userVO.setOnline(user.getOnline());
        userVO.setExist(user.getExist());
        return userVO;
    }

}
