package com.knowledge_farm.user.controller;

import com.knowledge_farm.annotation.Task;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.util.Email;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@Api(description = "前台用户接口")
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
    @ApiOperation(value = "QQ登陆", notes = "返回值：User || notExist：openId不存在(返回后前端进行QQ新用户注册) || notEffect：openId不可用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "openId", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/loginByOpenId")
    public Object loginByOpenId(@RequestParam("openId") String openId){
        Object obj = this.userService.loginByOpenId(openId);
        return obj;
    }

    /**
     * @Author 张帅华
     * @Description 查询用户信息 根据id
     * @Date 16:59 2020/4/8 0008
     * @Param [userId]
     * @return java.lang.String
     **/
    @ApiOperation(value = "根据id查询用户信息", notes = "返回值：User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "userId", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/findUserInfoByUserId")
    public Object findUserInfoByUserId(@RequestParam("userId") Integer userId){
        User user = this.userService.findUserById(userId);
        if(user != null){
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
    @ApiOperation(value = "QQ新注册用户", notes = "返回值：User || (String)fail：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "openId", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "photo", value = "头像", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String", paramType = "form", defaultValue = ""),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/addQQUser")
    public Object addQQUser(@RequestParam("openId") String openId,
                            @RequestParam("photo") String photo,
                            @RequestParam("nickName") String nickName,
                            @RequestParam("grade") Integer grade,
                            @RequestParam(value = "email", defaultValue = "") String email,
                            @RequestParam("password") String password){
        try {
            Object obj = this.userService.addQQUser(openId, photo, nickName, grade, email, password);
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
    @ApiOperation(value = "账号登陆", notes = "返回值：User || (String)PasswordError：密码错误 || (String)notEffect：账号不可用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "password", value = "面膜", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/loginByAccount")
    public Object loginByAccount(@RequestParam("account") String account, @RequestParam("password") String password){
        Object obj = this.userService.loginByAccount(account, password);
        return obj;
    }

    /**
     * @Author 张帅华
     * @Description 注册账号
     * @Date 17:14 2020/4/8 0008
     * @Param [nickName, grade, email, password]
     * @return java.lang.String
     **/
    @ApiOperation(value = "注册账号", notes = "返回值： User || (String)fail：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String", paramType = "form", required = false),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/registAccount")
    public Object registAccount(@RequestParam("nickName") String nickName,
                                @RequestParam("grade") Integer grade,
                                @RequestParam(value = "email", defaultValue = "") String email,
                                @RequestParam("password") String password){
        try {
            Object obj = this.userService.registAccount(nickName, grade, email, password);
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
    @ApiOperation(value = "发送验证码用于找回密码，并返回验证码", notes = "返回值：(String)验证码 || (String)fail：发送失败 || EmailError：邮箱错误 || notBindingEmail：未绑定邮箱 || notExistAccount：不存在该账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/sendTestCodePassword")
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
    @ApiOperation(value = "找回密码（重新给账号设置密码）", notes = "返回值： (String)true：成功 || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/resetUserPassword")
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
    @ApiOperation(value = "修改用户昵称", notes = "返回值： (String)true：成功 || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/updateUserNickName")
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
    @ApiOperation(value = "修改用户年级", notes = "返回值： (String)true：成功 || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataType = "int", paramType = "form", required = true)
    })
    @PostMapping("/updateUserGrade")
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
    @ApiOperation(value = "修改用户密码", notes = "返回值： (String)true：成功 || (String)false：失败 || (String)PasswordError：旧密码错误")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/updateUserPassword")
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
    @ApiOperation(value = "修改用户头像", notes = "返回值： (String)photo：头像 || (String)false：失败 || (String)null：图片为空")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "upload", value = "图片", dataType = "MultipartFile", paramType = "form", required = true),
            @ApiImplicitParam(name = "id", value = "用户Id", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "photo", value = "头像", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/updatePhoto")
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
    @ApiOperation(value = "验证是否已经绑定QQ", notes = "返回值： (String)true：已绑定 || (String)false：未绑定 || (String)null：账号不存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/isBindingQQ")
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
    @ApiOperation(value = "账号绑定QQ", notes = "返回值： (String)true：成功 || (String)false：失败 || (String)already：邮箱已被绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "openId", value = "openId", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/bindingQQ")
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
    @ApiOperation(value = "账号解绑QQ", notes = "返回值： (String)true：成功 || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("unBindingQQ")
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
    @ApiOperation(value = "发送验证码用于绑定邮箱，并返回验证码", notes = "返回值： (String)验证码 || (String)fail：发送失败 || already：邮箱已被绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/sendTestCodeBingEmail")
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
    @ApiOperation(value = "绑定邮箱（直接设置邮箱）", notes = "返回值： (String)true：成功 || (String)fail：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/bindingEmail")
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
    @ApiOperation(value = "解绑邮箱", notes = "返回值： (String)true：成功 || (String)fail：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/unBindingEmail")
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
    @ApiOperation(value = "添加浇水，施肥次数，减少奖励次数", notes = "返回值： (String)剩余奖励次数 || (String)-1：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "water", value = "水的次数", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "fertilizer", value = "肥料的次数", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "subject", value = "学科", dataType = "String", paramType = "form", required = true)
    })
    @PostMapping("/lessRewardCount")
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
    @ApiOperation(value = "查询剩余奖励次数", notes = "返回值： (String)剩余奖励次数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "subject", value = "学科", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/getRewardCount")
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
    @ApiOperation(value = "浇水", notes = "返回值： (String)true：成功 || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "landNumber", value = "土地号", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/waterCrop")
    public String waterCrop(@RequestParam("userId") Integer userId, @RequestParam("landNumber") String landNumber, HttpServletRequest request){
        try {
            int result = this.userService.waterCrop(userId, landNumber);
            switch (result){
                case -1:
                    return Result.FALSE;
                case 0:
                    return Result.TRUE;
                default:
                    request.setAttribute("StartUserCropGrowJob", new Integer[]{userId, result, Integer.parseInt(landNumber.substring(4))});
                    return Result.TRUE;
            }
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
    @ApiOperation(value = "施肥", notes = "返回值： (String)true：成功 || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "landNumber", value = "土地号", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/fertilizerCrop")
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
    @ApiOperation(value = "购买种子后添加到背包", notes = "返回值： (String)true：成功 || (String)false：失败 || (String)notEnoughMoney：钱不够")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "cropId", value = "作物Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "number", value = "数量", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/buyCrop")
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
    @ApiOperation(value = "种植作物", notes = "返回值： (String)true：成功 || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "cropId", value = "作物Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "number", value = "数量", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/raiseCrop")
    public String raiseCrop(@RequestParam("userId") Integer userId, @RequestParam("cropId") Integer cropId, @RequestParam("landNumber") String landNumber, HttpServletRequest request){
        try {
            int result = this.userService.raiseCrop(userId, cropId, landNumber);
            if(result != -1){
                request.setAttribute("StartUserCropGrowJob", new Integer[]{userId, result, Integer.parseInt(landNumber.substring(4))});
                return Result.TRUE;
            }
            return Result.FALSE;
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
    @ApiOperation(value = "收获", notes = "返回值：  (String)up：升级 || (String)true：成功（未升级） || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "landNumber", value = "土地号", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/harvest")
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
    @ApiOperation(value = "扩建土地", notes = "返回值： (String)true：成功 || (String)false：失败 || (String)notEnoughMoney：钱不够")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "form", required = true),
            @ApiImplicitParam(name = "landNumber", value = "土地号", dataType = "String", paramType = "form", required = true),
            @ApiImplicitParam(name = "needMoney", value = "开扩土地所需金币", dataType = "int", paramType = "form", required = true)
    })
    @PostMapping("/extensionLand")
    public String extensionLand(@RequestParam("userId") Integer userId, @RequestParam("landNumber") String landNumber, @RequestParam("needMoney") Integer money){
        try {
            return this.userService.extensionLand(userId, landNumber, money);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "购买宠物", notes = "返回值： (String)true：成功 || (String)false：失败 || (String)own：该宠物已拥有，不可再次购买 || (String)notEnoughMoney：钱不够")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "petId", value = "宠物Id", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/buyPet")
    public String buyPet(@RequestParam("userId")Integer userId,@RequestParam("petId")Integer petId){
        try{
            return userService.buyPet(userId,petId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "购买宠物饲料或工具", notes = "返回值： (String)true：成功 || (String)false：失败 || (String)notEnoughMoney：钱不够")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "petUtilId", value = "宠物饲料或工具Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "number", value = "数量", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/buyPetUtil")
    public String buyPetUtil(@RequestParam("userId") Integer userId,
                             @RequestParam("petUtilId") Integer petUtilId,
                             @RequestParam("number") Integer number){
        try{
            return userService.buyPetUtil(userId, petUtilId, number);
        }catch (Exception e){
            e.printStackTrace();
            return Result.FALSE;
        }
    }

    @ApiOperation(value = "喂养宠物", notes = "返回值： (String)true：成功 || (String)false：失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "userPetHouseId", value = "宠物仓库Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "petUtilBagId", value = "宠物饲料或工具背包Id", dataType = "int", paramType = "query", required = true),
    })
    @GetMapping("feedPet")
    public String feedPet(@RequestParam("userId") Integer userId,
                          @RequestParam("userPetHouseId") Integer userPetHouseId,
                          @RequestParam("petUtilBagId") Integer petUtilBagId){
        return this.userService.feedPet(userId, userPetHouseId, petUtilBagId);
    }

}
