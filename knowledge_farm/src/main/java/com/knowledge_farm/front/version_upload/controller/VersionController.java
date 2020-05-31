package com.knowledge_farm.front.version_upload.controller;

import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.Version;
import com.knowledge_farm.front.version_upload.service.FrontVersionService;
import com.knowledge_farm.util.ApkInfoUtil;
import com.knowledge_farm.util.PageUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName VersionUploadController
 * @Description
 * @Author 张帅华
 * @Date 2020-05-28 18:31
 */
@Api(description = "后台版本接口")
@Controller
@RequestMapping("/admin/version")
public class VersionController {
    @Resource
    private FrontVersionService frontVersionService;
    Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${file.apkFileLocation}")
    private String apkFileLocation;

    @GetMapping("/toAdd")
    public String toAdd(){
        return "version-add";
    }

    @GetMapping("/findVersionPage")
    public String findVersionPage(@RequestParam(value = "versionName", required = false) String versionName,
                                  @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                  @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                  Model model){
        Page<Version> page = this.frontVersionService.findVersionPage(versionName, pageNumber, pageSize);
        PageUtil<Version> pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        List<String> versionNameList = this.frontVersionService.findAllVersionName();
        model.addAttribute("versionNames", versionNameList);
        model.addAttribute("versionPage", pageUtil);
        return "version-list";
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam("description") String description, @RequestParam("file") MultipartFile file) {
        String fileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + "_" + file.getOriginalFilename();
        try{
            if(file.getSize() <= 0){
                return Result.NULL;
            }
            FileCopyUtils.copy(file.getBytes(), new File(this.apkFileLocation, fileName));

            File convertFile = new File(this.apkFileLocation + "/" + fileName);
            Version version = new Version();
            version.setDescription(description);
            version.setFileName(fileName);
            version.setUploadTime(new Date());
            Iterator<Map.Entry<String, Object>> it = ApkInfoUtil.readAPK(convertFile).entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, Object> m = it.next();
                System.out.println("key:" + m.getKey() + "; value:" + m.getValue());
                switch (m.getKey()){
                    case "versionCode":
                        version.setVersionCode("" + m.getValue());
                        break;
                    case "versionName":
                        version.setVersionName("" + m.getValue());
                        break;
                    case "packageName":
                        version.setPackageName("" + m.getValue());
                        break;
                }
            }
            this.frontVersionService.save(version);
            return Result.SUCCEED;
        }catch(Exception e){
            e.printStackTrace();
            File file1 = new File(this.apkFileLocation + "/" + fileName);
            if(file1.exists()){
                file1.delete();
            }
            return Result.FAIL;
        }
    }

    @GetMapping("/downloadVersion")
    @ResponseBody
    public Object downloadVersion(@RequestParam("id") Integer id) throws IOException {
        try {
            Version version = this.frontVersionService.findVersionById(id);
            FileSystemResource file = new FileSystemResource(this.apkFileLocation + "/" + version.getFileName());
            if(file.exists()){
                HttpHeaders headers = new HttpHeaders();
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", "KnowledgeFarm.apk"));
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");

                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(new InputStreamResource(file.getInputStream()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "服务器文件错误";
    }

    @PostMapping("/deleteOneVersion")
    @ResponseBody
    public String deleteOneVersion(@RequestParam("id") Integer id){
        try {
            List<Integer> idList = new ArrayList<>();
            idList.add(id);
            this.frontVersionService.delete(idList);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.FAIL;
    }

    @PostMapping("/deleteMultiVersion")
    @ResponseBody
    public String deleteMultiVersion(@RequestParam("deleteStr") String deleteStr){
        String deleteIds[] = deleteStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : deleteIds){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.frontVersionService.delete(idList);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

}
