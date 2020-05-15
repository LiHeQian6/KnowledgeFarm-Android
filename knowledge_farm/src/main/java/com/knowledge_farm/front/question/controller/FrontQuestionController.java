package com.knowledge_farm.front.question.controller;

import com.knowledge_farm.entity.PetUtil;
import com.knowledge_farm.entity.PetUtilType;
import com.knowledge_farm.entity.Question;
import com.knowledge_farm.entity.QuestionType;
import com.knowledge_farm.front.question.service.FrontQuestionService;
import com.knowledge_farm.util.PageUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FrontQuestionController
 * @Description
 * @Author 张帅华
 * @Date 2020-05-14 16:25
 */
@Controller
@RequestMapping("/admin/question")
public class FrontQuestionController {
    @Resource
    private FrontQuestionService frontQuestionService;

    @GetMapping("/toTest")
    public String toTest(){
        return "test";
    }

    @GetMapping("/toAdd")
    public String toAdd(@RequestParam("questionTypeId") Integer questionId, Model model){
        List<QuestionType> questionTypeList = this.frontQuestionService.findAllQuestionType();
        for(QuestionType questionType : questionTypeList){
            if(questionType.getId() == questionId){
                model.addAttribute("questionType", questionType);
            }
        }
        List<String> gradeList = new ArrayList<>();
        gradeList.add("一年级上");
        gradeList.add("一年级下");
        gradeList.add("二年级上");
        gradeList.add("二年级下");
        gradeList.add("三年级上");
        gradeList.add("三年级下");
        List<String> subjectList = new ArrayList<>();
        subjectList.add("Math");
        subjectList.add("English");
        subjectList.add("Chinese");
        model.addAttribute("grades", gradeList);
        model.addAttribute("subjects", subjectList);
        return "question-add";
    }

    @GetMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, Model model){
        Question question = this.frontQuestionService.findQuestionById(id);
        if(question != null){
            List<String> gradeList = new ArrayList<>();
            gradeList.add("一年级上");
            gradeList.add("一年级下");
            gradeList.add("二年级上");
            gradeList.add("二年级下");
            gradeList.add("三年级上");
            gradeList.add("三年级下");
            model.addAttribute("question", question);
            model.addAttribute("grades", gradeList);
        }
        return "question-edit";
    }




    @GetMapping(value = "/exportExcelModel")
    public void exportExcelModel(@RequestParam("questionTypeId") Integer questionTypeId, HttpServletResponse response) {
        try {
            //设置文件名(避免文件名中文乱码，将UTF8打散重组成ISO-8859-1编码方式)
            String fileName = new String ("知识农场题目.xlsx".getBytes("UTF8"),"ISO-8859-1");
            XSSFWorkbook workbook = this.frontQuestionService.exportExcelModel(questionTypeId);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/exportExcelData")
    public void exportExcelData(@RequestParam("questionTypeId") Integer questionTypeId, HttpServletResponse response) {
        try {
            //设置文件名(避免文件名中文乱码，将UTF8打散重组成ISO-8859-1编码方式)
            String fileName = new String ("知识农场题目.xlsx".getBytes("UTF8"),"ISO-8859-1");
            XSSFWorkbook workbook = this.frontQuestionService.exportExcelData(questionTypeId);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/importExcelToEdit")
    @ResponseBody
    public String importExcelToEdit(@RequestParam("questionTypeId") Integer questionTypeId, @RequestParam("file") MultipartFile file){
        try {
            return this.frontQuestionService.importExcelToEdit(questionTypeId, file);
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }

    @PostMapping("/importExcelToAdd")
    @ResponseBody
    public String importExcelToAdd(@RequestParam("questionTypeId") Integer questionTypeId, @RequestParam("file") MultipartFile file){
        try {
            return this.frontQuestionService.importExcelToAdd(questionTypeId, file);
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }

}
