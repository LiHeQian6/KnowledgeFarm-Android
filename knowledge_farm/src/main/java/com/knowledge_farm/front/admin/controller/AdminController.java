package com.knowledge_farm.front.admin.controller;

import com.knowledge_farm.entity.Admin;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.front.admin.service.AdminServiceImpl;
import com.knowledge_farm.util.PageUtil;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AdminController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 15:43
 */
@Api(description = "后台管理员接口")
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminServiceImpl adminService;

    @GetMapping("toLogin")
    public String toLogin() {
        return "login";
    }

    @GetMapping("/toIndex")
    public String toIndex() {
        return "index";
    }

    @GetMapping("/toAdd")
    public String toAdd() {
        return "admin-add";
    }

    @GetMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, HttpServletRequest request) {
        Admin admin = this.adminService.findById(id);
        if (admin != null) {
            request.setAttribute("adminInfo", admin);
        }
        return "admin-edit";
    }

    @GetMapping("toPassword")
    public String toPassword(@RequestParam("id") Integer id, HttpServletRequest request) {
        Admin admin = this.adminService.findById(id);
        if (admin != null) {
            request.setAttribute("adminInfo", admin);
        }
        return "admin-password";
    }

    @GetMapping("/logout")
    private String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:toLogin";
    }

    @GetMapping("generateTestCode")
    public void generateTestCode(HttpServletResponse response, HttpSession session) throws IOException {
        //获得随机数
        String code = "";
        for (int i = 0; i < 4; i++) {
            code = code + " " + (int) (10 * Math.random());//要传入session的验证码值
        }
        //创建验证码图片
        BufferedImage buffer = new BufferedImage(50, 20, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Graphics graphics = buffer.getGraphics();
        Color c = new Color(0, 128, 128);
        graphics.fillRect(0, 0, 50, 20);
        graphics.setColor(c);
        graphics.drawString(code, 5, 15);
        ImageIO.write(buffer, "png", byteArrayOutputStream);
        graphics.dispose();
        byteArrayOutputStream.flush();
        byte imageInByte[] = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        response.setContentType("text/html;charset=GBK");
        response.getOutputStream().write(imageInByte);
        session.setAttribute("testCode", code);
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam("account") String account,
                        @RequestParam("password") String password,
                        @RequestParam("testCode") String testCode,
                        HttpSession session) {
        String trueTestCode = (String) session.getAttribute("testCode");
        String realTestCode = "";
        for (int i = 0; i < testCode.length(); i++) {
            realTestCode = realTestCode + " " + testCode.charAt(i);
        }
        if (trueTestCode != null && trueTestCode.equals(realTestCode)) {
            Object obj = this.adminService.login(account, password);
            if (obj instanceof Admin) {
                Admin admin = (Admin) obj;
                session.setAttribute("admin", admin);
                obj = Result.SUCCEED;
            }
            session.removeAttribute("testCode");
            return (String) obj;
        }
        return Result.FALSE;
    }

    /**
     * @return java.lang.String
     * @Author 张帅华
     * @Description 分页查询管理员信息
     * @Date 21:38 2020/4/8 0008
     * @Param [account, pageNumber, pageSize, exist, session, model]
     **/
    @GetMapping("/findAdminPage")
    public String list(@RequestParam(value = "account", required = false) String account,
                       @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                       @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                       @RequestParam(value = "exist") Integer exist,
                       HttpServletRequest request) {
        Page<Admin> page = this.adminService.findPageAdminByAccount(account, exist, pageNumber, pageSize);
        PageUtil<Admin> pageUtil = new PageUtil<>(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        request.setAttribute("adminPage", pageUtil);
        if (exist == 1) {
            return "admin-list";
        }
        return "admin-del";
    }

    /**
     * @return java.lang.String
     * @Author 张帅华
     * @Description 删除单个管理员
     * @Date 21:40 2020/4/8 0008
     * @Param [id, exist]
     **/
    @PostMapping("/deleteOneAdmin")
    @ResponseBody
    public String deleteOneAdmin(@RequestParam("id") Integer id) {
        try {
            this.adminService.editStatusById(id, 0);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    /**
     * @return java.lang.String
     * @Author 张帅华
     * @Description 删除批量管理员
     * @Date 21:40 2020/4/8 0008
     * @Param [ids, exist]
     **/
    @PostMapping("/deleteMultiAdmin")
    @ResponseBody
    public String deleteMultiAdmin(@RequestParam("deleteStr") String deleteStr) {
        String deleteIds[] = deleteStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String id : deleteIds) {
            idList.add(Integer.parseInt(id));
        }
        try {
            this.adminService.editStatusListByIdList(idList, 0);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/recoveryOneAdmin")
    @ResponseBody
    public String recoveryOneAdmin(@RequestParam("id") Integer id) {
        try {
            this.adminService.editStatusById(id, 1);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/recoveryMultiAdmin")
    @ResponseBody
    public String recoveryMultiAdmin(@RequestParam("recoveryStr") String recoveryStr) {
        String recoveryId[] = recoveryStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String id : recoveryId) {
            idList.add(Integer.parseInt(id));
        }
        try {
            this.adminService.editStatusListByIdList(idList, 1);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    /**
     * @return java.lang.String
     * @Author 张帅华
     * @Description 删除管理员
     * @Date 21:40 2020/4/8 0008
     * @Param [id]
     **/
    @PostMapping("/deleteThoroughAdmin")
    @ResponseBody
    public String deleteById(@RequestParam("id") Integer id) {
        try {
            this.adminService.deleteById(id);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    /**
     * @return java.lang.String
     * @Author 张帅华
     * @Description 添加管理员
     * @Date 21:41 2020/4/8 0008
     * @Param [admin]
     **/
    @PostMapping("/addAdmin")
    @ResponseBody
    public String add(@RequestParam("account") String account, @RequestParam("password") String password) {
        if (this.adminService.findByAccountAndExist(account, null) == null) {
            Admin admin = new Admin();
            admin.setAccount(account);
            admin.setPassword(password);
            try {
                this.adminService.add(admin);
                return Result.SUCCEED;
            } catch (Exception e) {
                e.printStackTrace();
                return Result.FAIL;
            }
        }
        return Result.ALREADY;
    }

    /**
     * @return java.lang.String
     * @Author 张帅华
     * @Description 修改管理员账号
     * @Date 21:42 2020/4/8 0008
     * @Param [id, account]
     **/
    @PostMapping("/updateAdminAccount")
    @ResponseBody
    public String updateAdminAccount(@RequestParam("id") Integer id, @RequestParam("account") String account, @RequestParam("oldAccount") String excludeAccount) {
        if (this.adminService.findByAccountExcludeAccount(account, excludeAccount) == null) {
            try {
                this.adminService.editAccountById(id, account);
                return Result.SUCCEED;
            } catch (Exception e) {
                e.printStackTrace();
                return Result.FAIL;
            }
        }
        return Result.ALREADY;
    }

    /**
     * @return java.lang.String
     * @Author 张帅华
     * @Description 修改管理员密码
     * @Date 21:43 2020/4/8 0008
     * @Param [id, oldPassword, newPassword]
     **/
    @PostMapping("/updateAdminPassword")
    @ResponseBody
    public String updateAdminPassword(@RequestParam("id") Integer id, @RequestParam("password") String password) {
        try {
            this.adminService.editPasswordById(id, password);
            return Result.SUCCEED;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }

}