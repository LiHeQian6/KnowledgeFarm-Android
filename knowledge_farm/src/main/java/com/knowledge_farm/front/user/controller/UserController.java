package com.knowledge_farm.front.user.controller;

import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserVO;
import com.knowledge_farm.front.user.service.UserServiceImpl;
import com.knowledge_farm.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-15 20:59
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Resource
    private UserServiceImpl userService;

    @RequestMapping("/findUserPage")
    public String findUserPage(@RequestParam("accout") String accout,
                                         @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                         @RequestParam("exist") Integer exist,
                                         HttpSession session, Model model){
        session.removeAttribute("user");

        Page<User> page =  this.userService.findUserPage(accout, exist, pageNumber, pageSize);
        PageUtil<UserVO> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        List<UserVO> userVOS = new ArrayList<>();
        for(User user : page.getContent()){
            UserVO userVO = varyUserToUserVO(user);
            userVOS.add(userVO);
        }
        pageUtil.setList(userVOS);
        model.addAttribute("userPage", pageUtil);

        if(exist == 1){
            return "member-list";
        }else if(exist == 0){
            return "member-del";
        }else{
            return "member-land-list";
        }
    }

    @RequestMapping("/deleteOneUser")
    public String deleteOneUser(@RequestParam("userId") Integer userId){
        return this.userService.deleteOneUser(userId, 0);
    }

    @RequestMapping("/deleteMultiUser")
    public String deleteMultiUser(@RequestParam("deleteStr") String deleteStr) {
        String deleteId[] = deleteStr.split(",");
        String result = "";
        for(String id : deleteId){
            result = this.userService.deleteOneUser(Integer.parseInt(id), 0);
            if(!result.equals("succeed")){
                break;
            }
        }
        return result;
    }

    @RequestMapping("/recoveryOneUser")
    public String recoveryOneUser(@RequestParam("userId") Integer userId) {
        return this.userService.deleteOneUser(userId, 1);
    }

    @RequestMapping("/recoveryMultiUser")
    public String recoveryMultiUser(@RequestParam("recoveryStr") String recoveryStr) {
        String recoveryId[] = recoveryStr.split(",");
        String result = "";
        for(String id : recoveryId){
            result = this.userService.deleteOneUser(Integer.parseInt(id), 1);
            if(!result.equals("succeed")){
                break;
            }
        }
        return result;
    }

    @RequestMapping("/deleteThoroughUser")
    public String deleteThoroughUser(@RequestParam("userId") Integer userId) {
        return this.userService.deleteThoroughUser(userId);
    }

    @RequestMapping("/addUser")
    public String addUser(@RequestParam("nickName") String nickName,
                          @RequestParam("password") String password,
                          @RequestParam("email") String email,
                          @RequestParam("grade") Integer grade) {
        return this.userService.addUser(nickName, password, email, grade);
    }

    @RequestMapping("/getUpdateUserInfo")
    public String getUpdateUserInfo(@RequestParam("id") Integer id, Model model) {
        User user = this.userService.findUserById(id);
        if(user != null){
            UserVO userVO = varyUserToUserVO(user);
            model.addAttribute("user", userVO);
            return "succeed";
        }
        return "fail";
    }

    @RequestMapping("/updateUser")
    public String updateUser(){
        return "";
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
