package com.knowledge_farm.front.user.controller;

import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.front.user.service.FrontUserServiceImpl;
import com.knowledge_farm.util.PageUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName UserController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-15 20:59
 */
@Api(description = "后台用户接口")
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
    @Value("#{${excel.grades}}")
    private Map<Integer,String> grades = new HashMap<>();
    @Value("${level.experienceList}")
    private List<Integer> experienceList = new ArrayList<>();

    @GetMapping("/toAdd")
    public String toAdd(Model model){
        model.addAttribute("grades", grades);
        return "member-add";
    }

    @GetMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, HttpServletRequest request) {
        User user = this.frontUserService.findUserById(id);
        if(user != null){
            request.setAttribute("user", user);
        }
        request.setAttribute("grades", grades);
        return "member-edit";
    }

    @GetMapping("toPassword")
    public String toPassword(@RequestParam("id") Integer id, HttpServletRequest request){
        User user = this.frontUserService.findUserById(id);
        if(user != null){
            request.setAttribute("user", user);
        }
        return "member-password";
    }

    @GetMapping("/findUserPage")
    public String findUserPage(@RequestParam(value = "account", required = false) String account,
                               @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                               @RequestParam("exist") Integer exist,
                               HttpServletRequest request){
        Page<User> page =  this.frontUserService.findUserPage(account, exist, pageNumber, pageSize);
        PageUtil<User> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        request.setAttribute("userPage", pageUtil);
        request.setAttribute("grades", grades);
        if(exist == 1){
            return "member-list";
        }else if(exist == 0){
            return "member-del";
        }
        return "";
    }

    @PostMapping("/deleteOneUser")
    @ResponseBody
    public String deleteOneUser(@RequestParam("userId") Integer userId){
        try {
            this.frontUserService.deleteOneUser(userId, 0);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/deleteMultiUser")
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
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/recoveryOneUser")
    @ResponseBody
    public String recoveryOneUser(@RequestParam("userId") Integer userId) {
        try {
            this.frontUserService.deleteOneUser(userId, 1);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/recoveryMultiUser")
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
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/deleteThoroughUser")
    @ResponseBody
    public String deleteThoroughUser(@RequestParam("userId") Integer userId) {
        try {
            this.frontUserService.deleteThoroughUser(userId);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/addUser")
    @ResponseBody
    public String addUser(@RequestParam("nickName") String nickName,
                          @RequestParam("password") String password,
                          @RequestParam("email") String email,
                          @RequestParam("grade") Integer grade,
                          HttpServletRequest request) {
        try {
            String account = this.frontUserService.addUser(nickName, password, email, grade);
            request.setAttribute("account", account);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/updateUser")
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
                    String photoName = user.getId() + "_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + "_" + file.getOriginalFilename();
                    FileCopyUtils.copy(file.getBytes(), new File(this.userPhotoLocation, photoName));
                    user.setPhoto(this.userPhotoFolderName + "/" + photoName);
                }
                this.frontUserService.save(user);
                return Result.SUCCEED;
            }
            return Result.ALREADY;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/updateUserPassword")
    @ResponseBody
    public String updateUserPassword(@RequestParam("account") String account, @RequestParam("password") String password){
        try {
            this.frontUserService.editPasswordByAccount(account, password);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    public User varyUserToUser(User user, User editUser){
        Integer experience = editUser.getExperience();
        user.setNickName(editUser.getNickName());
        user.setAccount(editUser.getAccount());
        user.setEmail(editUser.getEmail());
        user.setGrade(editUser.getGrade());
        user.setExperience(experience);
        user.setMoney(editUser.getMoney());
        user.setMathRewardCount(editUser.getMathRewardCount());
        user.setEnglishRewardCount(editUser.getEnglishRewardCount());
        user.setChineseRewardCount(editUser.getChineseRewardCount());
        user.setWater(editUser.getWater());
        user.setFertilizer(editUser.getFertilizer());
        user.setOnline(editUser.getOnline());
        for(int i = 0;i < experienceList.size()-1;i++){
            if(experience >= experienceList.get(i) && experience < experienceList.get(i+1)){
                user.setLevel(i + 1);
                break;
            }
        }
        return user;
    }

}
