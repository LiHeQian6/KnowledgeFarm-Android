package com.knowledge_farm.user.controller;

import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserVO;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.util.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName UserController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-07 17:36
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserServiceImpl userService;
    @Value("${file.photoUrl}")
    private String photoUrl;
    @Value("${file.photoLocation}")
    private String photoLocation;
    @Value("${file.userPhotoFolderName}")
    private String userPhotoFolderName;
    @Value("${file.userPhotoLocation}")
    private String userPhotoLocation;
    @Value("${file.userDefaultFileName}")
    private String userDefaultFileName;

    /**
     * @Author 张帅华
     * @Description QQ登录 根据openId
     * @Date 16:59 2020/4/8 0008
     * @Param [openId]
     * @return java.lang.String
     **/
    @RequestMapping("/loginByOpenId")
    private Object loginByOpenId(@RequestParam("openId") String openId){
        Object obj = this.userService.loginByOpenId(openId);
        if(obj instanceof User){
            User user = (User) obj;
            user.setPassword("");
            if(!(user.getPhoto().substring(0,4)).equals("http")){
                user.setPhoto(this.photoUrl + user.getPhoto());
            }
            return user;
        }
        return obj;
    }

    /**
     * @Author 张帅华
     * @Description 查询用户信息 根据id
     * @Date 16:59 2020/4/8 0008
     * @Param [userId]
     * @return java.lang.String
     **/
    @RequestMapping("/findUserInfoByUserId")
    public Object findUserInfoByUserId(@RequestParam("userId") Integer userId){
        User user = this.userService.findUserById(userId);
        if(user != null){
            user.setPassword("");
            if(!(user.getPhoto().substring(0,4)).equals("http")){
                user.setPhoto(this.photoUrl + user.getPhoto());
            }
            return user;
        }
        return "{}";
    }

    /**
     * @Author 张帅华
     * @Description 第一次登录QQ，添加用户
     * @Date 17:07 2020/4/8 0008
     * @Param [openId, nickName, grade, email, password]
     * @return java.lang.String
     **/
    @RequestMapping("/addQQUser")
    public Object addQQUser(@RequestParam("openId") String openId,
                            @RequestParam("photo") String photo,
                            @RequestParam("nickName") String nickName,
                            @RequestParam("grade") Integer grade,
                            @RequestParam(value = "email", defaultValue = "") String email,
                            @RequestParam("password") String password){
        try {
            Object obj = this.userService.addQQUser(openId, photo, nickName, grade, email, password);
            if(obj instanceof User){
                User user = (User) obj;
                user.setPassword("");
                if(!(user.getPhoto().substring(0,4)).equals("http")){
                    user.setPhoto(this.photoUrl + user.getPhoto());
                }
                return user;
            }
            return obj;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    /**
     * @Author 张帅华
     * @Description 账号登陆
     * @Date 17:10 2020/4/8 0008
     * @Param [account, password]
     * @return java.lang.String
     **/
    @RequestMapping("/loginByAccount")
    public Object loginByAccount(@RequestParam("account") String account, @RequestParam("password") String password){
        Object obj = this.userService.loginByAccount(account, password);
        if(obj instanceof User){
            User user = (User) obj;
            user.setPassword("");
            if(!(user.getPhoto().substring(0,4)).equals("http")){
                user.setPhoto(this.photoUrl + user.getPhoto());
            }
            return user;
        }
        return obj;
    }

    /**
     * @Author 张帅华
     * @Description 注册账号
     * @Date 17:14 2020/4/8 0008
     * @Param [nickName, grade, email, password]
     * @return java.lang.String
     **/
    @RequestMapping("/registAccount")
    public Object registAccount(@RequestParam("nickName") String nickName,
                                @RequestParam("grade") Integer grade,
                                @RequestParam(value = "email", defaultValue = "") String email,
                                @RequestParam("password") String password){
        try {
            Object obj = this.userService.registAccount(nickName, grade, email, password);
            if(obj instanceof User){
                User user = (User) obj;
                user.setPassword("");
                if(!(user.getPhoto().substring(0,4)).equals("http")){
                    user.setPhoto(this.photoUrl + user.getPhoto());
                }
                return user;
            }
            return obj;
        } catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    /**
     * @Author 张帅华
     * @Description 发送验证码用于找回密码，并返回验证码
     * @Date 17:16 2020/4/8 0008
     * @Param [account, email]
     * @return java.lang.String
     **/
    @RequestMapping("/sendTestCodePassword")
    public String sendTestCodePassword(@RequestParam("account") String account, @RequestParam("email") String email){
        return this.userService.sendTestCodePassword(account,email);
    }
    
    /**
     * @Author 张帅华
     * @Description 找回密码（重新给账号设置密码）
     * @Date 17:17 2020/4/8 0008
     * @Param [account, password]
     * @return java.lang.String
     **/
    @RequestMapping("/resetUserPassword")
    public String resetUserPassword(@RequestParam("account") String account, @RequestParam("password") String password){
        try {
            this.userService.editPasswordByAccount(account, password);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 修改用户昵称
     * @Date 17:17 2020/4/8 0008
     * @Param [account, nickName]
     * @return java.lang.String
     **/
    @RequestMapping("/updateUserNickName")
    public String updateUserNickName(@RequestParam("account") String account,
                                     @RequestParam("nickName") String nickName){
        try {
            this.userService.editNickNameByAccount(account, nickName);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 修改用户年级
     * @Date 17:18 2020/4/8 0008
     * @Param [account, grade]
     * @return java.lang.String
     **/
    @RequestMapping("/updateUserGrade")
    public String updateUserGrade(@RequestParam("account") String account,
                                  @RequestParam("grade") Integer grade){
        try {
            this.userService.editGradeByAccount(account, grade);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 修改用户密码
     * @Date 17:18 2020/4/8 0008
     * @Param [account, password]
     * @return java.lang.String
     **/
    @RequestMapping("/updateUserPassword")
    public String updateUserPassword(@RequestParam("account") String account,
                                     @RequestParam("oldPassword") String oldPassword,
                                     @RequestParam("newPassword") String newPassword){
        User user = this.userService.findUserByAccount(account);
        try {
            if(user.getPassword().equals(oldPassword)){
                this.userService.editPasswordByAccount(account, newPassword);
                return Result.TRUE;
            }
            return Result.PASSWORD_ERROR;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 修改用户头像
     * @Date 17:18 2020/4/8 0008
     * @Param [file, id, photo]
     * @return java.lang.String
     **/
    @RequestMapping("/updatePhoto")
    public String updatePhoto(@RequestParam("upload") MultipartFile file,
                              @RequestParam("id") Integer id,
                              @RequestParam("photo") String photo) throws IOException {
        if(!file.getOriginalFilename().equals("")){
            String defaultFileName = this.userPhotoFolderName + "/" + this.userDefaultFileName;
            photo = photo.substring((this.photoUrl).length());//URLDecoder.decode(photo).
            if(!photo.equals(defaultFileName)){
                File file1 = new File(this.photoLocation + "/" + photo);
                if(file1.exists()){
                    file1.delete();
                }
            }
            String photoName = id + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + file.getOriginalFilename();
            FileCopyUtils.copy(file.getBytes(), new File(this.userPhotoLocation, photoName));
            photo = this.userPhotoFolderName + "/" + photoName;
            try {
                this.userService.editPhotoById(id, photo);
                return photo;
            }catch (Exception e){
                e.printStackTrace();
                return Result.FALSE;
            }
        }
        return Result.NULL;
    }
    /**
     * @Author 张帅华
     * @Description 验证是否已经绑定QQ
     * @Date 17:19 2020/4/8 0008
     * @Param [account]
     * @return java.lang.String
     **/
    @RequestMapping("/isBindingQQ")
    public String isBindingQQ(@RequestParam("account") String account){
        try {
            return this.userService.isBindingQQ(account);
        }catch (Exception e){
            e.printStackTrace();
            return Result.NULL;
        }
    }

    /**
     * @Author 张帅华
     * @Description 账号绑定QQ
     * @Date 17:20 2020/4/8 0008
     * @Param [account, openId]
     * @return java.lang.String
     **/
    @RequestMapping("/bindingQQ")
    public String bindingQQ(@RequestParam("account") String account, @RequestParam("openId") String openId){
        try {
            return this.userService.bindingQQ(account, openId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 账号解绑QQ
     * @Date 17:21 2020/4/8 0008
     * @Param [account]
     * @return java.lang.String
     **/
    @RequestMapping("unBindingQQ")
    public String unBindingQQ(@RequestParam("account") String account){
        try {
            this.userService.removeUserAuthority(account);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 发送验证码用于绑定邮箱，并返回验证码
     * @Date 18:15 2020/4/8 0008
     * @Param [email]
     * @return java.lang.String
     **/
    @RequestMapping("/sendTestCodeBingEmail")
    public String sendTestCodeBingEmail(@RequestParam("email") String email){
        if(this.userService.findUserByEmail(email) == null){
            if(Email.bindingMail(email)){
                return Email.getCode();
            }
            return Result.FAIL;
        }
        return Result.ALREADY;
    }

    /**
     * @Author 张帅华
     * @Description 绑定邮箱（直接设置邮箱）
     * @Date 18:31 2020/4/8 0008
     * @Param [account, email]
     * @return java.lang.String
     **/
    @RequestMapping("/bindingEmail")
    public String bindingEmail(@RequestParam("account") String account, @RequestParam("email") String email){
        try {
            this.userService.editEmail(account, email);
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 解绑邮箱
     * @Date 18:32 2020/4/8 0008
     * @Param
     * @return
     **/
    @RequestMapping("/unBindingEmail")
    public String unBindingEmail(@RequestParam("account") String account){
        try {
            this.userService.editEmail(account, "");
            return Result.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 添加浇水，施肥次数，减少奖励次数
     * @Date 18:35 2020/4/8 0008
     * @Param
     * @return
     **/
    @RequestMapping("/lessRewardCount")
    public String lessRewardCount(@RequestParam("userId") Integer userId,
                                  @RequestParam("water") Integer water,
                                  @RequestParam("fertilizer") Integer fertilizer,
                                  @RequestParam("subject") String subject){
        try {
            return "" + this.userService.lessRewardCount(userId, water, fertilizer, subject);
        }catch (Exception e){
            e.printStackTrace();
            return "-1";
        }
    }

    /**
     * @Author 张帅华
     * @Description 查询剩余奖励次数
     * @Date 23:39 2020/4/10 0010
     * @Param [userId, subject]
     * @return java.lang.String
     **/
    @RequestMapping("/getRewardCount")
    public String getRewardCount(@RequestParam("userId") Integer userId, @RequestParam("subject") String subject){
        User user = this.userService.findUserById(userId);
        if(user != null){
            switch (subject){
                case "chinese":
                    return "" + user.getChineseRewardCount();
                case "english":
                    return "" + user.getEnglishRewardCount();
                case "math":
                    return "" + user.getMathRewardCount();
                default:
                    return "-1";
            }
        }
        return "-2";
    }

    /**
     * @Author 张帅华
     * @Description 浇水
     * @Date 22:34 2020/4/8 0008
     * @Param [userId, landNumber]
     * @return java.lang.String
     **/
    @RequestMapping("/waterCrop")
    public String waterCrop(@RequestParam("userId") Integer userId, @RequestParam("landNumber") String landNumber){
        try {
            return this.userService.waterCrop(userId, landNumber);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }

    }

    @RequestMapping("/waterCrop2")
    public String test(){
        try {
            return this.userService.waterCrop2(userService.findUserById(109), "land1");
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }

    }

    /**
     * @Author 张帅华
     * @Description 施肥
     * @Date 19:08 2020/4/9 0009
     * @Param [userId, landNumber]
     * @return java.lang.String
     **/
    @RequestMapping("/fertilizerCrop")
    public String fertilizerCrop(@RequestParam("userId") Integer userId, @RequestParam("landNumber") String landNumber){
        try {
            return this.userService.fertilizerCrop(userId, landNumber);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 购买种子后添加到背包
     * @Date 9:42 2020/4/10 0010
     * @Param [userId, cropId, number]
     * @return java.lang.String
     **/
    @RequestMapping("/buyCrop")
    public String buyCrop(@RequestParam("userId") Integer userId, @RequestParam("cropId") Integer cropId, @RequestParam("number") Integer number){
        try {
            return this.userService.buyCrop(userId, cropId, number);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 种植作物
     * @Date 10:10 2020/4/10 0010
     * @Param [userId, cropId, landNumber]
     * @return java.lang.String
     **/
    @RequestMapping("/raiseCrop")
    public String raiseCrop(@RequestParam("userId") Integer userId, @RequestParam("cropId") Integer cropId, @RequestParam("landNumber") String landNumber){
        try {
            return this.userService.raiseCrop(userId, cropId, landNumber);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 收获
     * @Date 11:08 2020/4/10 0010
     * @Param [userId, landNumber]
     * @return java.lang.String
     **/
    @RequestMapping("/harvest")
    public String harvest(@RequestParam("userId") Integer userId, @RequestParam("landNumber") String landNumber){
        try {
            return this.userService.harvest(userId, landNumber);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    /**
     * @Author 张帅华
     * @Description 扩建土地
     * @Date 12:01 2020/4/10 0010
     * @Param [userId, landNumber, money]
     * @return java.lang.String
     **/
    @RequestMapping("/extensionLand")
    public String extensionLand(@RequestParam("userId") Integer userId, @RequestParam("landNumber") String landNumber, @RequestParam("needMoney") Integer money){
        try {
            return this.userService.extensionLand(userId, landNumber, money);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
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
