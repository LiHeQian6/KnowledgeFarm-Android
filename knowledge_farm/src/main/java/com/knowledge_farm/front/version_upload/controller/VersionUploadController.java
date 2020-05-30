package com.knowledge_farm.front.version_upload.controller;

import com.knowledge_farm.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Calendar;

/**
 * @ClassName VersionUploadController
 * @Description
 * @Author 张帅华
 * @Date 2020-05-28 18:31
 */
@Controller
@RequestMapping("/admin/versionUpload")
public class VersionUploadController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${file.apkFileLocation}")
    private String apkFileLocation;

    @GetMapping("/toVersionUpload")
    public String toTest(){
        return "version-upload";
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        try{
            if(file.getSize() <= 0){
                return Result.NULL;
            }
            long start_time = Calendar.getInstance().getTimeInMillis();
            FileCopyUtils.copy(file.getBytes(), new File(this.apkFileLocation, file.getOriginalFilename()));
            long end_time = Calendar.getInstance().getTimeInMillis();
            long lt = end_time - start_time;
            logger.info("文件上传成功,共用时" + lt + "ms");
            return lt + "ms";
        }catch(Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

}
