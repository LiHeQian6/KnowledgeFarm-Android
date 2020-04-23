package com.knowledge_farm.front.admin.controller;

import com.knowledge_farm.entity.Admin;
import com.knowledge_farm.entity.Result;
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

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "admin-add";
    }

    @RequestMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, Model model){
        Admin admin = this.adminService.findById(id);
        if(admin != null){
            admin.setPassword("");
            model.addAttribute("adminInfo", admin);
        }
        return "admin-edit";
    }

    @RequestMapping("toPassword")
    public String toPassword(@RequestParam("id") Integer id, Model model){
        Admin admin = this.adminService.findById(id);
        if(admin != null){
            admin.setPassword("");
            model.addAttribute("adminInfo", admin);
        }
        return "admin-password";
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
        session.removeAttribute("admin");
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
        Object obj = this.adminService.login(account, password);
        if(obj instanceof Admin){
            Admin admin = (Admin) obj;
            admin.setPassword("");
            session.setAttribute("admin", admin);
            obj = Result.SUCCEED;
        }
        return (String) obj;
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
                       Model model){
        Page<Admin> page = this.adminService.findPageAdminByAccount(account, exist, pageNumber, pageSize);
        for(Admin admin : page.getContent()){
            admin.setPassword("");
        }
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
            this.adminService.editStatusById(id, 0);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
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
            this.adminService.editStatusListByIdList(idList, 0);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @RequestMapping("/recoveryOneAdmin")
    @ResponseBody
    public String recoveryOneAdmin(@RequestParam("id") Integer id){
        try {
            this.adminService.editStatusById(id, 1);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
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
            this.adminService.editStatusListByIdList(idList, 1);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
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
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
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
        if(this.adminService.findByAccountAndExist(account, null) == null){
            Admin admin = new Admin();
            admin.setAccount(account);
            admin.setPassword(Md5Encode.getMD5(password.getBytes()));
            try {
                this.adminService.add(admin);
                return Result.SUCCEED;
            }catch (Exception e){
                e.printStackTrace();
                return Result.FAIL;
            }
        }
        return Result.ALREADY;
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
    public String updateAdminAccount(@RequestParam("id") Integer id, @RequestParam("account") String account, @RequestParam("oldAccount") String excludeAccount){
        if(this.adminService.findByAccountExcludeAccount(account, excludeAccount) == null){
            try {
                this.adminService.editAccountById(id, account);
                return Result.SUCCEED;
            }catch (Exception e){
                e.printStackTrace();
                return Result.FAIL;
            }
        }
        return Result.ALREADY;
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
    public String updateAdminPassword(@RequestParam("id") Integer id, @RequestParam("password") String password){
        password = Md5Encode.getMD5(password.getBytes());
        try {
            this.adminService.editPasswordById(id, password);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

}
