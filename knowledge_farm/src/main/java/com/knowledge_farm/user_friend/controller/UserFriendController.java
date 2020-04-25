package com.knowledge_farm.user_friend.controller;

import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserVO;
import com.knowledge_farm.user_friend.service.UserFriendServiceImpl;
import com.knowledge_farm.util.PageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName UserFriendController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 15:25
 */
@RestController
@RequestMapping("/userfriend")
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
    public PageUtil<User> findUserFriend(@RequestParam("userId") Integer userId,
                                         @RequestParam(value = "account", required = false) String account,
                                         @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
        Page<User> page = this.userFriendService.findUserFriendPageByAccount(userId, account, pageNumber, pageSize);
        PageUtil<User> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
//        for(User user : page.getContent()){
//            user.setPassword("");
//            if(!(user.getPhoto().substring(0,4)).equals("http")){
//                user.setPhoto(this.photoUrl + user.getPhoto());
//            }
//        }
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
    @RequestMapping("/findAllUser")
    public PageUtil<User> findAllUser(@RequestParam(value = "account", required = false) String account,
                                        @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize) {
        Page<User> page = this.userFriendService.findAllUserByAccount(account, pageNumber, pageSize);
        PageUtil<User> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
//        for(User user : page.getContent()){
//            user.setPassword("");
//            if(!(user.getPhoto().substring(0,4)).equals("http")){
//                user.setPhoto(this.photoUrl + user.getPhoto());
//            }
//        }
        pageUtil.setList(page.getContent());
        return pageUtil;
    }

    @RequestMapping("/addUserFriend")
    public String addUserFriend(@RequestParam("userId") Integer userId, @RequestParam("account") String account){
        try {
            this.userFriendService.addUserFriend(userId, account);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @RequestMapping("/deleteUserFriend")
    public String deleteUserFriend(@RequestParam("userId") Integer userId, @RequestParam("account") String account){
        try {
            this.userFriendService.deleteUserFriend(userId, account);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

}
