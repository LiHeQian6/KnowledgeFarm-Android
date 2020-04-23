package com.knowledge_farm.front.user.controller;

import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserVO;
import com.knowledge_farm.front.user.service.FrontUserServiceImpl;
import com.knowledge_farm.util.Md5Encode;
import com.knowledge_farm.util.PageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-15 20:59
 */
@Controller
@RequestMapping("/admin/user")
public class FrontUserController {
    @Resource
    private FrontUserServiceImpl frontUserService;
    @Value("${file.photoLocation}")
    private String photoLocation;
    @Value("${file.userPhotoFolderName}")
    private String userPhotoFolderName;
    @Value("${file.userPhotoLocation}")
    private String userPhotoLocation;
    @Value("${file.userDefaultFileName}")
    private String userDefaultFileName;

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "member-add";
    }

    @RequestMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, Model model) {
        User user = this.frontUserService.findUserById(id);
        if(user != null){
            user.setPassword("");
            model.addAttribute("user", user);
        }
        return "member-edit";
    }

    @RequestMapping("toPassword")
    public String toPassword(@RequestParam("id") Integer id, Model model){
        User user = this.frontUserService.findUserById(id);
        if(user != null){
            user.setPassword("");
            model.addAttribute("user", user);
        }
        return "member-password";
    }

    @RequestMapping("/findUserPage")
    public String findUserPage(@RequestParam(value = "account", required = false) String account,
                               @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                               @RequestParam("exist") Integer exist,
                               Model model){
        Page<User> page =  this.frontUserService.findUserPage(account, exist, pageNumber, pageSize);
        PageUtil<User> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        for(User user : page.getContent()){
            user.setPassword("");
        }
        pageUtil.setList(page.getContent());
        model.addAttribute("userPage", pageUtil);

        if(exist == 1){
            return "member-list";
        }else if(exist == 0){
            return "member-del";
        }
        return "";
    }

    @RequestMapping("/deleteOneUser")
    @ResponseBody
    public String deleteOneUser(@RequestParam("userId") Integer userId){
        try {
            this.frontUserService.deleteOneUser(userId, 0);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    @RequestMapping("/deleteMultiUser")
    @ResponseBody
    public String deleteMultiUser(@RequestParam("deleteStr") String deleteStr) {
        String deleteIds[] = deleteStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : deleteIds){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.frontUserService.editStatusListByIdList(idList, 0);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    @RequestMapping("/recoveryOneUser")
    @ResponseBody
    public String recoveryOneUser(@RequestParam("userId") Integer userId) {
        try {
            this.frontUserService.deleteOneUser(userId, 1);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    @RequestMapping("/recoveryMultiUser")
    @ResponseBody
    public String recoveryMultiUser(@RequestParam("recoveryStr") String recoveryStr) {
        String recoveryId[] = recoveryStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : recoveryId){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.frontUserService.editStatusListByIdList(idList, 1);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    @RequestMapping("/deleteThoroughUser")
    @ResponseBody
    public String deleteThoroughUser(@RequestParam("userId") Integer userId) {
        try {
            this.frontUserService.deleteThoroughUser(userId);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public String addUser(@RequestParam("nickName") String nickName,
                          @RequestParam("password") String password,
                          @RequestParam("email") String email,
                          @RequestParam("grade") Integer grade) {
        try {
            this.frontUserService.addUser(nickName, password, email, grade);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public String updateUser(User editUser, @RequestParam("oldAccount") String excludeAccount, @RequestParam("upload") MultipartFile file){
        try {
            if(this.frontUserService.findUserByAccountAndExcludeAccount(editUser.getAccount(), excludeAccount) == null){
                User user = this.frontUserService.findUserById(editUser.getId());
                varyUserToUser(user, editUser);
                if(!file.getOriginalFilename().equals("")){
                    if(!user.getPhoto().equals(this.userPhotoFolderName + "/" + this.userDefaultFileName)){
                        File file1 = new File(this.photoLocation + "/" + user.getPhoto());
                        if(file1.exists()){
                            file1.delete();
                        }
                    }
                    String photoName = user.getId() + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + file.getOriginalFilename();
                    FileCopyUtils.copy(file.getBytes(), new File(this.userPhotoLocation, photoName));
                    user.setPhoto(this.userPhotoFolderName + "/" + photoName);
                }
                this.frontUserService.save(user);
                return Result.SUCCEED;
            }
            return Result.ALREADY;
        }catch (Exception e){
            return Result.FAIL;
        }
    }

    @RequestMapping("/updateUserPassword")
    @ResponseBody
    public String updateUserPassword(@RequestParam("account") String account, @RequestParam("password") String password){
        password = Md5Encode.getMD5(password.getBytes());
        try {
            this.frontUserService.editPasswordByAccount(account, password);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
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

    public User varyUserToUser(User user, User editUser){
        user.setNickName(editUser.getNickName());
        user.setAccount(editUser.getAccount());
        user.setEmail(editUser.getEmail());
        user.setGrade(editUser.getGrade());
        user.setLevel(editUser.getLevel());
        user.setExperience(editUser.getExperience());
        user.setMoney(editUser.getMoney());
        user.setMathRewardCount(editUser.getMathRewardCount());
        user.setEnglishRewardCount(editUser.getEnglishRewardCount());
        user.setChineseRewardCount(editUser.getChineseRewardCount());
        user.setWater(editUser.getWater());
        user.setFertilizer(editUser.getFertilizer());
        user.setOnline(editUser.getOnline());
        user.setPhoto(editUser.getPhoto());
        return user;
    }

}
