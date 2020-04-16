package com.knowledge_farm.front.admin.controller;

import com.knowledge_farm.entity.Admin;
import com.knowledge_farm.front.admin.service.AdminServiceImpl;
import com.knowledge_farm.util.Md5Encode;
import com.knowledge_farm.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AdminController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 15:43
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminServiceImpl adminService;

    @RequestMapping("/gotoIndex")
    public String gotoIndex(){
        return "index";
    }

    /**
     * @Author 张帅华
     * @Description 管理员注销登录
     * @Date 21:37 2020/4/8 0008
     * @Param [session]
     * @return java.lang.String
     **/
    @RequestMapping("/logout")
    private String logout(HttpSession session){
        session.invalidate();
        return "login";
    }

    /**
     * @Author 张帅华
     * @Description 管理员登录
     * @Date 21:37 2020/4/8 0008
     * @Param [account, password, session]
     * @return java.lang.String
     **/
    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam("account") String account, @RequestParam("password") String password, HttpSession session){
        return this.adminService.login(account, password, session);
    }

    /**
     * @Author 张帅华
     * @Description 分页查询管理员信息
     * @Date 21:38 2020/4/8 0008
     * @Param [account, pageNumber, pageSize, exist, session, model]
     * @return java.lang.String
     **/
    @RequestMapping("/findAdminPage")
    public String list(@RequestParam(value = "account", required = false) String account,
                     @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                     @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                     @RequestParam(value = "exist") Integer exist,
                     HttpSession session, Model model){
        //清除修改查询的管理员信息
        session.removeAttribute("adminInfo");
        Page<Admin> page = this.adminService.findPageAdminByAccount(account, exist, pageNumber, pageSize);
        PageUtil<Admin> pageUtil = new PageUtil<>(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        model.addAttribute("adminPage", pageUtil);
        if(exist == 1){
            return "admin-list";
        }
        return "admin-del";
    }

    /**
     * @Author 张帅华
     * @Description 删除单个管理员
     * @Date 21:40 2020/4/8 0008
     * @Param [id, exist]
     * @return java.lang.String
     **/
    @RequestMapping("/deleteOneAdmin")
    @ResponseBody
    public String deleteOneAdmin(@RequestParam("id") Integer id){
        try {
            if(this.adminService.editStatusById(id, 0) != null){
                return "succeed";
            }
            return "notExist";
        }catch(Exception e){
            return "fail";
        }
    }

    /**
     * @Author 张帅华
     * @Description 删除批量管理员
     * @Date 21:40 2020/4/8 0008
     * @Param [ids, exist]
     * @return java.lang.String
     **/
    @RequestMapping("/deleteMultiAdmin")
    @ResponseBody
    public String deleteMultiAdmin(@RequestParam("deleteStr") String deleteStr){
        String deleteIds[] = deleteStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : deleteIds){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.adminService.editStatusListByIdlist(idList, 0);
            return "succeed";
        }catch (Exception e){
            return "fail";
        }
    }

    @RequestMapping("/recoveryOneAdmin")
    @ResponseBody
    public String recoveryOneAdmin(@RequestParam("id") Integer id){
        try {
            if(this.adminService.editStatusById(id, 1) != null){
                return "succeed";
            }
            return "notExist";
        }catch(Exception e){
            return "fail";
        }
    }

    @RequestMapping("/recoveryMultiAdmin")
    @ResponseBody
    public String recoveryMultiAdmin(@RequestParam("recoveryStr") String recoveryStr){
        String recoveryId[] = recoveryStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : recoveryId){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.adminService.editStatusListByIdlist(idList, 1);
            return "succeed";
        }catch (Exception e){
            return "fail";
        }
    }

    /**
     * @Author 张帅华
     * @Description 删除管理员
     * @Date 21:40 2020/4/8 0008
     * @Param [id]
     * @return java.lang.String
     **/
    @RequestMapping("/deleteThoroughAdmin")
    @ResponseBody
    public String deleteById(@RequestParam("id") Integer id){
        try {
            this.adminService.deleteById(id);
            return "succeed";
        }catch (Exception e){
            return "fail";
        }
    }

    /**
     * @Author 张帅华
     * @Description 添加管理员
     * @Date 21:41 2020/4/8 0008
     * @Param [admin]
     * @return java.lang.String
     **/
    @RequestMapping("/addAdmin")
    @ResponseBody
    public String add(@RequestParam("account") String account, @RequestParam("password") String password){
        Admin admin = new Admin();
        admin.setAccount(account);
        admin.setPassword(Md5Encode.getMD5(password.getBytes()));
        if(this.adminService.findByAccountAndExist(admin.getAccount(), null) == null){
             try {
                 this.adminService.add(admin);
                 return "succeed";
             }catch (Exception e){
                 return "fail";
             }
        }
        return "already";
    }

    /**
     * @Author 张帅华
     * @Description 获取管理员信息 根据id
     * @Date 21:42 2020/4/8 0008
     * @Param [id, session]
     * @return java.lang.String
     **/
    @RequestMapping("/getUpdateAdminInfo")
    @ResponseBody
    public String toEdit(@RequestParam("id") Integer id, HttpSession session){
        Admin admin = this.adminService.findById(id);
        if(admin != null){
            session.setAttribute("adminInfo", admin);
            return "succeed";
        }
        return "fail";
    }

    /**
     * @Author 张帅华
     * @Description 修改管理员账号
     * @Date 21:42 2020/4/8 0008
     * @Param [id, account]
     * @return java.lang.String
     **/
    @RequestMapping("/updateAdminAccount")
    @ResponseBody
    public String updateAdminAccount(@RequestParam("id") Integer id, @RequestParam("account") String account){
        if(this.adminService.findByAccountAndExist(account, null) == null){
            try {
                if(this.adminService.editAccountById(id, account) != null){
                    return "succeed";
                }
                return "notExist";
            }catch (Exception e){
                return "fail";
            }
        }
        return "already";
    }

    /**
     * @Author 张帅华
     * @Description 修改管理员密码
     * @Date 21:43 2020/4/8 0008
     * @Param [id, oldPassword, newPassword]
     * @return java.lang.String
     **/
    @RequestMapping("/updateAdminPassword")
    @ResponseBody
    public String updateAdminPassword(@RequestParam("id") Integer id,
                                   @RequestParam("oldPassword") String oldPassword,
                                   @RequestParam("newPassword") String newPassword){
        oldPassword = Md5Encode.getMD5(oldPassword.getBytes());
        newPassword = Md5Encode.getMD5(newPassword.getBytes());
        try {
            int result = this.adminService.editPasswordById(id, oldPassword, newPassword);
            if(result == -1){
                return "notExist";
            }else if(result == 0){
                return "PasswordError";
            }
            return "succeed";
        }catch (Exception e){
            return "fail";
        }
    }

}
