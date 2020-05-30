//package com.knowledge_farm.front.version_upload.controller;
//
//import com.knowledge_farm.entity.Result;
//import org.apache.commons.io.FileUtils;
//import org.apache.tomcat.util.http.fileupload.ProgressListener;
//import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
//import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.commons.CommonsMultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.math.BigDecimal;
//import java.util.Calendar;
//
///**
// * @ClassName VersionUploadController
// * @Description
// * @Author 张帅华
// * @Date 2020-05-28 18:31
// */
//@Controller
//@RequestMapping("/admin/versionUpload")
//public class VersionUploadController {
//    Logger logger = LoggerFactory.getLogger(getClass());
//    @Value("${file.apkFileLocation}")
//    private String apkFileLocation;
//
//
//    @RequestMapping("/toTest")
//    public String toTest(){
//        return "test";
//    }
//
//    @PostMapping("/test")
//    @ResponseBody
//    public String test(@RequestParam("file")MultipartFile file, HttpSession session, Model model){
//        logger.info("开始上传文件...");
//
//        long start_time = Calendar.getInstance().getTimeInMillis();
//        try {
//            // 创建DiskFileItemFactory工厂类
//            DiskFileItemFactory factory = new  DiskFileItemFactory();
//            // 创建一个文件上传解析器
//            ServletFileUpload upload = new ServletFileUpload(factory);
//            // 监听文件上传进度
//            upload.setProgressListener(new ProgressListener() {
//                public void update(long pBytesRead, long pContentLength, int pItems) {
//                    session.setAttribute("fileSize", pContentLength);
//                    session.setAttribute("loadSize", pBytesRead);
//                }
//            });
//            // 解决上传文件的中文乱码
//            upload.setHeaderEncoding("UTF-8");
//            // 获取上传文件的文件路径(不同浏览器不同，有的是文件名，有的是全路径)
//            String fileName = file.getOriginalFilename();
//            if (fileName == null || "".equals(fileName.trim())) {
//                return Result.NULL;
//            }
//            // 获取上传文件的输入流
//            InputStream is = file.getInputStream();
//            // 创建一个文件输出流
//            FileOutputStream fos = new FileOutputStream(this.apkFileLocation + "/" + fileName);
//            // 创建一个缓存区
//            byte[] buff = new byte[1024];
//            int len = 0;
//            while ((len = is.read(buff)) > 0) {// 循环读取数据
//                fos.write(buff, 0, len);// 使用文件输出流将缓存区的数据写入到指定的目录中
//            }
//            // 关闭输出流
//            fos.close();
//            // 关闭输入流
//            is.close();
//        }catch (Exception e){
//            e.printStackTrace();
//            return Result.FAIL;
//        }
//
//        long end_time = Calendar.getInstance().getTimeInMillis();
//        long lt = end_time - start_time;
//        logger.info("文件上传成功,共用时" + (end_time - start_time) + "ms");
//        model.addAttribute("time", lt);
//        return Result.SUCCEED;
//    }
//
//    @GetMapping("/test1")
//    @ResponseBody
//    public String test1(HttpSession session){
//        Object obj1 = session.getAttribute("loadSize");
//        long loadSize = 0L;
//        if (obj1 != null) {
//            loadSize = Long.valueOf(obj1.toString());
//        }
//        Object obj2 = session.getAttribute("fileSize");
//        long fileSize = 1L;
//        if (obj2 != null) {
//            fileSize = Long.valueOf(obj2.toString());
//        }
//
//        logger.info("loadSize:" + loadSize);
//        logger.info("fileSize:" + fileSize);
//        // 计算上传比例
//        double rate = loadSize * 1.0 / fileSize;
//        BigDecimal bd = new BigDecimal(rate);
//        rate = bd.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
//        logger.info("rate:" + rate);
//        logger.info("\n");
//        // 返回
//        return rate + "";
//    }
//
//    @PostMapping(value = "/upload")
//    @ResponseBody
//    public String upload(@RequestParam("file") MultipartFile file) {
//        try{
//            if(file.getSize() <= 0){
//                return Result.NULL;
//            }
//            long start_time = Calendar.getInstance().getTimeInMillis();
//            FileCopyUtils.copy(file.getBytes(), new File(this.apkFileLocation, file.getOriginalFilename()));
//            long end_time = Calendar.getInstance().getTimeInMillis();
//            long lt = (end_time - start_time) / 1000;
//            logger.info("文件上传成功,共用时" + lt + "s");
//            return lt + "s";
//        }catch(Exception e){
//            e.printStackTrace();
//            return Result.FAIL;
//        }
//    }
//
//}
