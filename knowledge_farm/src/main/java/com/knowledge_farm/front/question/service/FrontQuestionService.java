package com.knowledge_farm.front.question.service;

import com.knowledge_farm.answer.repository.QuestionRepository;
import com.knowledge_farm.entity.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @ClassName FrontQuestionService
 * @Description
 * @Author 张帅华
 * @Date 2020-05-14 16:12
 */
@Service
@Transactional(readOnly = true)
@PropertySource(value = {"classpath:excel.properties"})
public class FrontQuestionService {
    @Resource
    private QuestionRepository questionRepository;
    @Value("${excel.singleChoiceHeader}")
    private String[] singleChoiceHeader;
    @Value("${excel.completionHeader}")
    private String[] completionHeader;
    @Value("${excel.judgmentHeader}")
    private String[] judgmentHeader;
    @Value("${excel.subjects}")
    private String[] subjects;
    Logger logger = LoggerFactory.getLogger(getClass());

    public List<QuestionType> findAllQuestionType(){
        return this.questionRepository.findAllQuestionType();
    }
    public Question findQuestionById(Integer id){
        return this.questionRepository.findQuestionById(id);
    }
//    public Page<Question> findAllQuestion(String questionTitle, Integer questionTypeId, Integer  grade){
//        if(questionTitle != null){
//
//        }
//        return this.questionRepository.findAll();
//    }

    /**
     * @Author 张帅华
     * @Description 导出excel标题头
     * @Date 14:18 2020/5/13 0013
     * @Param []
     * @return org.apache.poi.xssf.usermodel.XSSFWorkbook
     **/
    public XSSFWorkbook exportExcelModel(Integer questionType){
        //新建work
        XSSFWorkbook workbook = new XSSFWorkbook();
        //设置标题单元格样式和普通单元格样式
        CellStyle cellStyleHeader = cellHeaderStyle(workbook);
        switch (questionType){
            case 1:
                //构造单选题表
                exportSingleChoiceExcelModel(workbook, cellStyleHeader);
                break;
            case 2:
                //构造填空题表
                exportCompletionExcelModel(workbook, cellStyleHeader);
                break;
            case 3:
                //构造判断题表
                exportJudgmentExcelModel(workbook, cellStyleHeader);
                break;
        }
        return workbook;
    }
    /**
     * @Author 张帅华
     * @Description 导出excel数据
     * @Date 14:18 2020/5/13 0013
     * @Param []
     * @return org.apache.poi.xssf.usermodel.XSSFWorkbook
     **/
    public XSSFWorkbook exportExcelData(Integer questionType){
        List<Question> questionList = this.questionRepository.findAllByQuestionType(questionType);
        //新建work
        XSSFWorkbook workbook = new XSSFWorkbook();
        //设置标题单元格样式和普通单元格样式
        CellStyle cellStyleHeader = cellHeaderStyle(workbook);
        CellStyle cellStyle = cellStyle(workbook);
        switch (questionType){
            case 1:
                //构造单选题表
                exportSingleChoiceExcelData(workbook, questionList, cellStyleHeader, cellStyle);
                break;
            case 2:
                //构造填空题表
                exportCompletionExcelData(workbook, questionList, cellStyleHeader, cellStyle);
                break;
            case 3:
                //构造判断题表
                exportJudgmentExcelData(workbook, questionList, cellStyleHeader, cellStyle);
                break;
        }
        return workbook;
    }
    /**
     * @Author 张帅华
     * @Description 导入excel文件进行修改
     * @Date 14:19 2020/5/13 0013
     * @Param [file]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String importExcelToEdit(Integer questionType, MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        if(!name.equals("")){
            //获取文件输入流
            InputStream inputStream = file.getInputStream();
            //根据Excel版本创建Work
            Workbook book = null;
            if(isExcel2003(name)) {
                book = new HSSFWorkbook(inputStream);
            }
            if(isExcel2007(name)) {
                book = new XSSFWorkbook(inputStream);
            }
            inputStream.close();

            switch (questionType){
                case 1:
                    Sheet sheet0 = book.getSheetAt(0);
                    if(sheet0.getLastRowNum() == 0){
                        return "上传文件数据为空";
                    }
                    return importSingleChoiceToEdit(sheet0);
                case 2:
                    Sheet sheet1 = book.getSheetAt(0);
                    if(sheet1.getLastRowNum() == 0){
                        return "上传文件数据为空";
                    }
                    return importCompletionToEdit(sheet1);
                case 3:
                    Sheet sheet2 = book.getSheetAt(0);
                    if(sheet2.getLastRowNum() == 0){
                        return "上传文件数据为空";
                    }
                    return importJudgmentToEdit(sheet2);
            }
//            if(sheet0.getLastRowNum() == 0 && sheet1.getLastRowNum() == 0 && sheet2.getLastRowNum() == 0){
//                return "上传文件数据为空";
//            }
//            String result1 = importSingleChoiceToEdit(sheet0);
//            if(result1.equals("succeed")){
//                String result2 = importCompletionToEdit(sheet1);
//                if(result2.equals("succeed")){
//                    String result3 = importJudgmentToEdit(sheet2);
//                    return result3;
//                }
//                return result2;
//            }
//            return result1;
        }
        return "上传文件为空";
    }
    /**
     * @Author 张帅华
     * @Description 导入excel文件进行添加
     * @Date 10:59 2020/5/14 0014
     * @Param [file]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String importExcelToAdd(Integer questionType, MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        if(!name.equals("")) {
            //获取文件输入流
            InputStream inputStream = file.getInputStream();
            //根据Excel版本创建Work
            Workbook book = null;
            if (isExcel2003(name)) {
                book = new HSSFWorkbook(inputStream);
            }
            if (isExcel2007(name)) {
                book = new XSSFWorkbook(inputStream);
            }
            inputStream.close();

            switch (questionType){
                case 1:
                    Sheet sheet0 = book.getSheetAt(0);
                    if(sheet0.getLastRowNum() == 0){
                        return "上传文件数据为空";
                    }
                    return importSingleChoiceToAdd(sheet0);
                case 2:
                    Sheet sheet1 = book.getSheetAt(0);
                    if(sheet1.getLastRowNum() == 0){
                        return "上传文件数据为空";
                    }
                    return importCompletionToAdd(sheet1);
                case 3:
                    Sheet sheet2 = book.getSheetAt(0);
                    if(sheet2.getLastRowNum() == 0){
                        return "上传文件数据为空";
                    }
                    return importJudgmentToAdd(sheet2);
            }
//            if(sheet0.getLastRowNum() == 0 && sheet1.getLastRowNum() == 0 && sheet2.getLastRowNum() == 0){
//                return "上传文件数据为空";
//            }
//            String result1 = importSingleChoiceToAdd(sheet0);
//            if(result1.equals("succeed")){
//                String result2 = importCompletionToAdd(sheet1);
//                if(result2.equals("succeed")){
//                    String result3 = importJudgmentToAdd(sheet2);
//                    return result3;
//                }
//                return result2;
//            }
//            return result1;
        }
        return "上传文件为空";
    }

    public void exportSingleChoiceExcelModel(XSSFWorkbook workbook, CellStyle cellStyleHeader) {
        Sheet sheet = workbook.createSheet("单选题");
        sheet.setColumnWidth(1, 6000);//给第5列设置宽度(tel栏)
        Row row = sheet.createRow(0);
        for (int i = 0; i < this.singleChoiceHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(this.singleChoiceHeader[i]);
            cell.setCellStyle(cellStyleHeader);
        }
    }
    public void exportCompletionExcelModel(XSSFWorkbook workbook, CellStyle cellStyleHeader) {
        Sheet sheet = workbook.createSheet("填空题");
        sheet.setColumnWidth(1,6000);//给第5列设置宽度(tel栏)
        Row row = sheet.createRow(0);
        for(int i = 0;i < this.completionHeader.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(this.completionHeader[i]);
            cell.setCellStyle(cellStyleHeader);
        }
    }
    public void exportJudgmentExcelModel(XSSFWorkbook workbook, CellStyle cellStyleHeader) {
        Sheet sheet = workbook.createSheet("判断题");
        sheet.setColumnWidth(1, 6000);//给第5列设置宽度(tel栏)
        Row row = sheet.createRow(0);
        for (int i = 0; i < this.judgmentHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(this.judgmentHeader[i]);
            cell.setCellStyle(cellStyleHeader);
        }
    }

    public void exportSingleChoiceExcelData(XSSFWorkbook workbook, List<Question> questionList, CellStyle cellStyleHeader, CellStyle cellStyle){
        Sheet sheet = workbook.createSheet("单选题");
        sheet.setColumnWidth(1,6000);//给第5列设置宽度(tel栏)
        Row row = sheet.createRow(0);
        for(int i = 0;i < this.singleChoiceHeader.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(this.singleChoiceHeader[i]);
            cell.setCellStyle(cellStyleHeader);
        }
        int rowNum = 1;
        //在表中存放查询到的数据放入对应的列
        for (Question question : questionList) {
            SingleChoice singleChoice = (SingleChoice) question;
            Row row1 = sheet.createRow(rowNum);
            List<Cell> cellList = new ArrayList<>();
            for(int i = 0;i < this.singleChoiceHeader.length;i++){
                Cell cell = row1.createCell(i);
                cell.setCellStyle(cellStyle);
                cellList.add(cell);
            }
            cellList.get(0).setCellValue(singleChoice.getId());
            cellList.get(1).setCellValue(singleChoice.getQuestionTitle().getTitle());
            cellList.get(2).setCellValue(singleChoice.getSubject());
            cellList.get(3).setCellValue(singleChoice.getQuestionType().getName());
            cellList.get(4).setCellValue(changeGrade1(singleChoice.getGrade()));
            cellList.get(5).setCellValue(singleChoice.getAnswer());
            cellList.get(6).setCellValue(singleChoice.getChoice1());
            cellList.get(7).setCellValue(singleChoice.getChoice2());
            cellList.get(8).setCellValue(singleChoice.getChoice3());
            rowNum++;
        }
    }
    public void exportCompletionExcelData(XSSFWorkbook workbook, List<Question> questionList, CellStyle cellStyleHeader, CellStyle cellStyle){
        Sheet sheet = workbook.createSheet("填空题");
        sheet.setColumnWidth(1,6000);//给第5列设置宽度(tel栏)
        Row row = sheet.createRow(0);
        for(int i = 0;i < this.completionHeader.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(this.completionHeader[i]);
            cell.setCellStyle(cellStyleHeader);
        }
        int rowNum = 1;
        //在表中存放查询到的数据放入对应的列
        for (Question question : questionList) {
            Completion completion = (Completion) question;
            Row row1 = sheet.createRow(rowNum);
            List<Cell> cellList = new ArrayList<>();
            for(int i = 0;i < this.completionHeader.length;i++){
                Cell cell = row1.createCell(i);
                cell.setCellStyle(cellStyle);
                cellList.add(cell);
            }
            cellList.get(0).setCellValue(completion.getId());
            cellList.get(1).setCellValue(completion.getQuestionTitle().getTitle());
            cellList.get(2).setCellValue(completion.getSubject());
            cellList.get(3).setCellValue(completion.getQuestionType().getName());
            cellList.get(4).setCellValue(changeGrade1(completion.getGrade()));
            cellList.get(5).setCellValue(completion.getAnswer());
            rowNum++;
        }
    }
    public void exportJudgmentExcelData(XSSFWorkbook workbook, List<Question> questionList, CellStyle cellStyleHeader, CellStyle cellStyle){
        Sheet sheet = workbook.createSheet("判断题");
        sheet.setColumnWidth(1,6000);//给第5列设置宽度(tel栏)
        Row row = sheet.createRow(0);
        for(int i = 0;i < this.judgmentHeader.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(this.judgmentHeader[i]);
            cell.setCellStyle(cellStyleHeader);
        }
        int rowNum = 1;
        //在表中存放查询到的数据放入对应的列
        for (Question question : questionList) {
            Judgment judgment = (Judgment) question;
            Row row1 = sheet.createRow(rowNum);
            List<Cell> cellList = new ArrayList<>();
            for(int i = 0;i < this.judgmentHeader.length;i++){
                Cell cell = row1.createCell(i);
                cell.setCellStyle(cellStyle);
                cellList.add(cell);
            }
            cellList.get(0).setCellValue(judgment.getId());
            cellList.get(1).setCellValue(judgment.getQuestionTitle().getTitle());
            cellList.get(2).setCellValue(judgment.getSubject());
            cellList.get(3).setCellValue(judgment.getQuestionType().getName());
            cellList.get(4).setCellValue(changeGrade1(judgment.getGrade()));
            cellList.get(5).setCellValue((judgment.getAnswer() == 1 ? "正确" : "错误"));
            cellList.get(6).setCellValue((judgment.getChoice() == 1 ? "正确" : "错误"));
            rowNum++;
        }
    }

    public String importSingleChoiceToEdit(Sheet sheet){
        int rowLength = sheet.getLastRowNum();
        logger.info("sheet1总数据行数有多少行"+rowLength);
        if(rowLength == 0) {
            return "succeed";
        }
        //工作表的列
        Row row0 = sheet.getRow(0);
        //总列数
        int colLength = row0.getLastCellNum();
        logger.info("sheet1总列数有多少列"+colLength);
        int count1 = 0;
        int count2 = 0;
        for(int i = 1;i <= rowLength;i++) {
            count1 = i + 1;
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            for(int n = 0;n < this.singleChoiceHeader.length;n++){
                count2 = n + 1;
                Cell cell = row.getCell(n);
                //判空
                if(cell == null){
                    cell = row.createCell(n);
                    cell.setCellValue("");
                }
                //判断数据格式和输入内容是否正确
                cell.setCellType(CellType.STRING);
                Integer formatResult = isFormatTrueForSheet1(cell.getStringCellValue(), n);
                if(formatResult == 0){
                    return "sheet1第"+count1+"行的第"+count2+"列的数据格式错误";
                }else if(formatResult == -1){
                    return "sheet1第"+count1+"行的第"+count2+"列的数据输入错误";
                }else if(formatResult == -2){
                    return "sheet1第"+count1+"行的第"+count2+"列的数据不能为空";
                }
                cellList.add(cell);
            }
            updateSingleChoice(cellList);
        }
        return "succeed";
    }
    public String importCompletionToEdit(Sheet sheet){
        int rowLength = sheet.getLastRowNum();
        logger.info("sheet2总数据行数有多少行"+rowLength);
        if(rowLength == 0) {
            return "succeed";
        }
        //工作表的列
        Row row0 = sheet.getRow(0);
        //总列数
        int colLength = row0.getLastCellNum();
        logger.info("sheet2总列数有多少列"+colLength);
        int count1 = 0;
        int count2 = 0;
        for(int i = 1;i <= rowLength;i++) {
            count1 = i + 1;
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            for(int n = 0;n < this.completionHeader.length;n++){
                count2 = n + 1;
                Cell cell = row.getCell(n);
                //判空
                if(cell == null){
                    cell = row.createCell(n);
                    cell.setCellValue("");
                }
                //判断数据格式和输入内容是否正确
                cell.setCellType(CellType.STRING);
                Integer formatResult = isFormatTrueForSheet2(cell.getStringCellValue(), n);
                if(formatResult == 0){
                    return "sheet2第"+count1+"行的第"+count2+"列的数据格式错误";
                }else if(formatResult == -1){
                    return "sheet2第"+count1+"行的第"+count2+"列的数据输入错误";
                }else if(formatResult == -2){
                    return "sheet2第"+count1+"行的第"+count2+"列的数据不能为空";
                }
                cellList.add(cell);
            }
            updateCompletion(cellList);
        }
        return "succeed";
    }
    public String importJudgmentToEdit(Sheet sheet){
        int rowLength = sheet.getLastRowNum();
        logger.info("sheet3总数据行数有多少行"+rowLength);
        if(rowLength == 0) {
            return "succeed";
        }
        //工作表的列
        Row row0 = sheet.getRow(0);
        //总列数
        int colLength = row0.getLastCellNum();
        logger.info("sheet3总列数有多少列"+colLength);
        int count1 = 0;
        int count2 = 0;
        for(int i = 1;i <= rowLength;i++) {
            count1 = i + 1;
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            for(int n = 0;n < this.judgmentHeader.length;n++){
                count2 = n + 1;
                Cell cell = row.getCell(n);
                //判空
                if(cell == null){
                    cell = row.createCell(n);
                    cell.setCellValue("");
                }
                //判断数据格式和输入内容是否正确
                cell.setCellType(CellType.STRING);
                Integer formatResult = isFormatTrueForSheet3(cell.getStringCellValue(), n);
                if(formatResult == 0){
                    return "sheet3第"+count1+"行的第"+count2+"列的数据格式错误";
                }else if(formatResult == -1){
                    return "sheet3第"+count1+"行的第"+count2+"列的数据输入错误";
                }else if(formatResult == -2){
                    return "sheet3第"+count1+"行的第"+count2+"列的数据不能为空";
                }
                cellList.add(cell);
            }
            updateJudgment(cellList);
        }
        return "succeed";
    }
    public void updateSingleChoice(List<Cell> cellList){
        Integer questionId = Integer.parseInt(cellList.get(0).getStringCellValue());
        SingleChoice question = (SingleChoice)this.questionRepository.findQuestionById(questionId);
        question.getQuestionTitle().setTitle(cellList.get(1).getStringCellValue());
        question.setSubject(cellList.get(2).getStringCellValue());
        List<QuestionType> questionTypeList = this.questionRepository.findAllQuestionType();
        for(QuestionType questionType : questionTypeList){
            if(questionType.getName().equals(cellList.get(3).getStringCellValue())){
                question.setQuestionType(questionType);
                break;
            }
        }
        question.setGrade(changeGrade2(cellList.get(4).getStringCellValue()));
        question.setAnswer(cellList.get(5).getStringCellValue());
        question.setChoice1(cellList.get(6).getStringCellValue());
        question.setChoice2(cellList.get(7).getStringCellValue());
        question.setChoice3(cellList.get(8).getStringCellValue());
    }
    public void updateCompletion(List<Cell> cellList){
        Integer questionId = Integer.parseInt(cellList.get(0).getStringCellValue());
        Completion question = (Completion)this.questionRepository.findQuestionById(questionId);
        question.getQuestionTitle().setTitle(cellList.get(1).getStringCellValue());
        question.setSubject(cellList.get(2).getStringCellValue());
        List<QuestionType> questionTypeList = this.questionRepository.findAllQuestionType();
        for(QuestionType questionType : questionTypeList){
            if(questionType.getName().equals(cellList.get(3).getStringCellValue())){
                question.setQuestionType(questionType);
                break;
            }
        }
        question.setGrade(changeGrade2(cellList.get(4).getStringCellValue()));
        question.setAnswer(cellList.get(5).getStringCellValue());
    }
    public void updateJudgment(List<Cell> cellList){
        Integer questionId = Integer.parseInt(cellList.get(0).getStringCellValue());
        Judgment question = (Judgment)this.questionRepository.findQuestionById(questionId);
        question.getQuestionTitle().setTitle(cellList.get(1).getStringCellValue());
        question.setSubject(cellList.get(2).getStringCellValue());
        List<QuestionType> questionTypeList = this.questionRepository.findAllQuestionType();
        for(QuestionType questionType : questionTypeList){
            if(questionType.getName().equals(cellList.get(3).getStringCellValue())){
                question.setQuestionType(questionType);
                break;
            }
        }
        question.setGrade(changeGrade2(cellList.get(4).getStringCellValue()));
        question.setAnswer((cellList.get(5).getStringCellValue().equals("正确") ? 1 : 0));
        question.setChoice((cellList.get(6).getStringCellValue().equals("正确") ? 1 : 0));
    }

    public String importSingleChoiceToAdd(Sheet sheet){
        int rowLength = sheet.getLastRowNum();
        logger.info("sheet1总数据行数有多少行"+rowLength);
        if(rowLength == 0) {
            return "succeed";
        }
        //工作表的列
        Row row0 = sheet.getRow(0);
        //总列数
        int colLength = row0.getLastCellNum();
        logger.info("sheet1总列数有多少列"+colLength);
        int count1 = 0;
        int count2 = 0;
        List<SingleChoice> singleChoiceList = new ArrayList<>();
        for(int i = 1;i <= rowLength;i++) {
            count1 = i + 1;
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            cellList.add(row.getCell(0));
            for(int n = 1;n < this.singleChoiceHeader.length;n++){
                count2 = n + 1;
                Cell cell = row.getCell(n);
                //判空
                if(cell == null){
                    cell = row.createCell(n);
                    cell.setCellValue("");
                }
                //判断数据格式和输入内容是否正确
                cell.setCellType(CellType.STRING);
                Integer formatResult = isFormatTrueForSheet1(cell.getStringCellValue(), n);
                if(formatResult == 0){
                    return "sheet1第"+count1+"行的第"+count2+"列的数据格式错误";
                }else if(formatResult == -1){
                    return "sheet1第"+count1+"行的第"+count2+"列的数据输入错误";
                }else if(formatResult == -2){
                    return "sheet1第"+count1+"行的第"+count2+"列的数据不能为空";
                }
                cellList.add(cell);
            }
            singleChoiceList.add(saveSingleChoice(cellList));
        }
        this.questionRepository.saveAll(singleChoiceList);
        return "succeed";
    }
    public String importCompletionToAdd(Sheet sheet){
        int rowLength = sheet.getLastRowNum();
        logger.info("sheet2总数据行数有多少行"+rowLength);
        if(rowLength == 0) {
            return "succeed";
        }
        //工作表的列
        Row row0 = sheet.getRow(0);
        //总列数
        int colLength = row0.getLastCellNum();
        logger.info("sheet2总列数有多少列"+colLength);
        int count1 = 0;
        int count2 = 0;
        List<Completion> completionList = new ArrayList<>();
        for(int i = 1;i <= rowLength;i++) {
            count1 = i + 1;
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            cellList.add(row.getCell(0));
            for(int n = 1;n < this.completionHeader.length;n++){
                count2 = n + 1;
                Cell cell = row.getCell(n);
                //判空
                if(cell == null){
                    cell = row.createCell(n);
                    cell.setCellValue("");
                }
                //判断数据格式和输入内容是否正确
                cell.setCellType(CellType.STRING);
                Integer formatResult = isFormatTrueForSheet2(cell.getStringCellValue(), n);
                if(formatResult == 0){
                    return "sheet2第"+count1+"行的第"+count2+"列的数据格式错误";
                }else if(formatResult == -1){
                    return "sheet2第"+count1+"行的第"+count2+"列的数据输入错误";
                }else if(formatResult == -2){
                    return "sheet2第"+count1+"行的第"+count2+"列的数据不能为空";
                }
                cellList.add(cell);
            }
            completionList.add(saveCompletion(cellList));
        }
        this.questionRepository.saveAll(completionList);
        return "succeed";
    }
    public String importJudgmentToAdd(Sheet sheet){
        int rowLength = sheet.getLastRowNum();
        logger.info("sheet3总数据行数有多少行"+rowLength);
        if(rowLength == 0) {
            return "succeed";
        }
        //工作表的列
        Row row0 = sheet.getRow(0);
        //总列数
        int colLength = row0.getLastCellNum();
        logger.info("sheet3总列数有多少列"+colLength);
        int count1 = 0;
        int count2 = 0;
        List<Judgment> judgmentList = new ArrayList<>();
        for(int i = 1;i <= rowLength;i++) {
            count1 = i + 1;
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            cellList.add(row.getCell(0));
            for(int n = 1;n < this.judgmentHeader.length;n++){
                count2 = n + 1;
                Cell cell = row.getCell(n);
                //判空
                if(cell == null){
                    cell = row.createCell(n);
                    cell.setCellValue("");
                }
                //判断数据格式和输入内容是否正确
                cell.setCellType(CellType.STRING);
                Integer formatResult = isFormatTrueForSheet3(cell.getStringCellValue(), n);
                if(formatResult == 0){
                    return "sheet3第"+count1+"行的第"+count2+"列的数据格式错误";
                }else if(formatResult == -1){
                    return "sheet3第"+count1+"行的第"+count2+"列的数据输入错误";
                }else if(formatResult == -2){
                    return "sheet3第"+count1+"行的第"+count2+"列的数据不能为空";
                }
                cellList.add(cell);
            }
            judgmentList.add(saveJudgment(cellList));
        }
        this.questionRepository.saveAll(judgmentList);
        return "succeed";
    }
    public SingleChoice saveSingleChoice(List<Cell> cellList){
        SingleChoice question = new SingleChoice();
        QuestionTitle questionTitle = new QuestionTitle();
        questionTitle.setTitle(cellList.get(1).getStringCellValue());
        question.setSubject(cellList.get(2).getStringCellValue());
        List<QuestionType> questionTypeList = this.questionRepository.findAllQuestionType();
        for(QuestionType questionType : questionTypeList){
            if(questionType.getName().equals(cellList.get(3).getStringCellValue())){
                question.setQuestionType(questionType);
                break;
            }
        }
        question.setGrade(changeGrade2(cellList.get(4).getStringCellValue()));
        question.setAnswer(cellList.get(5).getStringCellValue());
        question.setChoice1(cellList.get(6).getStringCellValue());
        question.setChoice2(cellList.get(7).getStringCellValue());
        question.setChoice3(cellList.get(8).getStringCellValue());
        question.setQuestionTitle(questionTitle);
        questionTitle.setQuestion(question);
        return question;
    }
    public Completion saveCompletion(List<Cell> cellList){
        Completion question = new Completion();
        QuestionTitle questionTitle = new QuestionTitle();
        questionTitle.setTitle(cellList.get(1).getStringCellValue());
        question.setSubject(cellList.get(2).getStringCellValue());
        List<QuestionType> questionTypeList = this.questionRepository.findAllQuestionType();
        for(QuestionType questionType : questionTypeList){
            if(questionType.getName().equals(cellList.get(3).getStringCellValue())){
                question.setQuestionType(questionType);
                break;
            }
        }
        question.setGrade(changeGrade2(cellList.get(4).getStringCellValue()));
        question.setAnswer(cellList.get(5).getStringCellValue());
        question.setQuestionTitle(questionTitle);
        questionTitle.setQuestion(question);
        return question;
    }
    public Judgment saveJudgment(List<Cell> cellList){
        Judgment question = new Judgment();
        QuestionTitle questionTitle = new QuestionTitle();
        questionTitle.setTitle(cellList.get(1).getStringCellValue());
        question.setSubject(cellList.get(2).getStringCellValue());
        List<QuestionType> questionTypeList = this.questionRepository.findAllQuestionType();
        for(QuestionType questionType : questionTypeList){
            if(questionType.getName().equals(cellList.get(3).getStringCellValue())){
                question.setQuestionType(questionType);
                break;
            }
        }
        question.setGrade(changeGrade2(cellList.get(4).getStringCellValue()));
        question.setAnswer((cellList.get(5).getStringCellValue().equals("正确") ? 1 : 0));
        question.setChoice((cellList.get(6).getStringCellValue().equals("正确") ? 1 : 0));
        question.setQuestionTitle(questionTitle);
        questionTitle.setQuestion(question);
        return question;
    }

    public Integer isFormatTrue(String value, Integer no){
        if(value == null || value.equals("")){
            return -2;
        }
        switch (no) {
            case 0:
                return isInteger(value) ? 1 : 0;
            case 1:
                return 1;
            case 2:
                for (String subject : this.subjects) {
                    if (subject.equals(value)) {
                        return 1;
                    }
                }
                return -1;
            case 3:
                List<QuestionType> questionTypeList = this.questionRepository.findAllQuestionType();
                for (QuestionType questionType : questionTypeList) {
                    if (questionType.getName().equals(value)) {
                        return 1;
                    }
                }
                return -1;
            case 4:
                return changeGrade2(value) != 0 ? 1 : -1;
            default:
                return -3;
        }
    }
    public Integer isFormatTrueForSheet1(String value, Integer no){
        Integer result = isFormatTrue(value, no);
        if(result == -3){
            switch (no){
                case 5:
                    if(value == null || value.equals("")){
                        return -2;
                    }
                    return 1;
                case 6:
                    return 1;
                case 7:
                    return 1;
                case 8:
                    return 1;
                default:
                    return -3;
            }
        }
        return result;
    }
    public Integer isFormatTrueForSheet2(String value, Integer no){
        Integer result = isFormatTrue(value, no);
        if(result == -3){
            switch (no){
                case 5:
                    if(value == null || value.equals("")){
                        return -2;
                    }
                    return 1;
                default:
                    return -3;
            }
        }
        return result;
    }
    public Integer isFormatTrueForSheet3(String value, Integer no){
        Integer result = isFormatTrue(value, no);
        if(result == -3){
            switch (no){
                case 5:
                    if(value == null || value.equals("")){
                        return -2;
                    }
                    return (value.equals("正确") || value.equals("错误")) ? 1 : -1;
                case 6:
                    if(value == null || value.equals("")){
                        return -2;
                    }
                    return (value.equals("正确") || value.equals("错误")) ? 1 : -1;
                default:
                    return -3;
            }
        }
        return result;
    }
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public CellStyle cellHeaderStyle(XSSFWorkbook workbook){
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        XSSFFont font = workbook.createFont();
        font.setBold(true);//加粗
        font.setFontHeight((short)220);//字体大小
        style.setFont(font);
        return style;
    }
    public CellStyle cellStyle(XSSFWorkbook workbook){
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//背景色
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    public String changeGrade1(Integer grade){
        switch (grade){
            case 1:
                return "一年级上";
            case 2:
                return "一年级下";
            case 3:
                return "二年级上";
            case 4:
                return "二年级下";
            case 5:
                return "三年级上";
            case 6:
                return "三年级下";
            default:
                return "年级错误";
        }
    }
    public Integer changeGrade2(String grade){
        switch (grade){
            case "一年级上":
                return 1;
            case "一年级下":
                return 2;
            case "二年级上":
                return 3;
            case "二年级下":
                return 4;
            case "三年级上":
                return 5;
            case "三年级下":
                return 6;
            default:
                return 0;
        }
    }
}
