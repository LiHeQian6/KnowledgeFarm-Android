package com.knowledge_farm.answer.Controller;

import com.knowledge_farm.answer.entity.Mix;
import com.knowledge_farm.answer.entity.Multiple;
import com.knowledge_farm.answer.entity.Question3Num;
import com.knowledge_farm.answer.service.AnswerService;
import com.knowledge_farm.entity.Completion;
import com.knowledge_farm.entity.Question;
import com.knowledge_farm.entity.QuestionTitle;
import com.knowledge_farm.entity.QuestionType;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @program: farm
 * @description: answer
 * @author: 景光赞
 * @create: 2020-04-09 19:19
 **/
@Api(description = "前台答题接口")
@Controller
@RequestMapping("/answer")
public class AnswerController {
    @Resource
    private AnswerService answerService;

    /**
     * @description: 宠物对战题目
     * @author :景光赞
     * @date :2020/5/14 16:03
     * @param :[grade]
     * @return :java.util.List
     */
    @ResponseBody
    @RequestMapping("/fightQuestions")
    public List petFightingQuestions(@RequestParam("grade")int grade){
        List<Question> list = answerService.findByGrade(grade);

        return list;
    }
    @ResponseBody
    @RequestMapping("/fightQuestions1/{grade}")
    public List petFightingQuestions1(@PathVariable int grade){
        List<Question> list = answerService.findByGrade(grade);

        return list;
    }

    /**
     * @description: 一年级数学上册
     * @author :景光赞
     * @date :2020/4/30 11:11
     * @param :[]
     * @return :java.util.List
     */
    @ResponseBody
    @GetMapping("/mathOneUp")
    public List OneUpMath() {
        List<Question> list = answerService.getQuestion3OneUpMath();
        list.addAll(answerService.findQuestion2(1,"Math",new QuestionType(1,"单选题")));
        list.addAll(answerService.findQuestion2(1,"Math",new QuestionType(3,"判断题")));
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 数学一年级下册
     * @Param:
     * @return: java.util.List
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/mathOneDown")
    public List OneDownMath() {
        List<Question> list = answerService.getQuestion3OneDownMath();
        list.addAll(answerService.findQuestion2(2,"Math",new QuestionType(1,"单选题")));
        list.addAll(answerService.findQuestion2(2,"Math",new QuestionType(3,"判断题")));
        Collections.shuffle(list);
        return list;
    }
    /**
     * @description: 二年级上册数学
     * @author :景光赞
     * @date :2020/5/2 18:41
     * @param :[]
     * @return :java.util.List
     */
    @ResponseBody
    @GetMapping("/mathTwoUp")
    public List twoUpMath(){
        int pageNumber = new Random().nextInt(10);
        Page<Question> page = answerService.findQuestion(3,"Math",PageRequest.of(pageNumber,11));
        List<Question> list = new ArrayList<>(page.getContent());
        list.addAll(get99Multiple());
        Collections.shuffle(list);
        return list;
    }
    /**
     * @description: 二年级下册数学
     * @author :景光赞
     * @date :2020/5/2 18:41
     * @param :[]
     * @return :java.util.List
     */
    @ResponseBody
    @GetMapping("/mathTwoDown")
    public List twoDownMath(){
        int pageNumber = new Random().nextInt(10);
        Page<Question> page = answerService.findQuestion(4,"Math",PageRequest.of(pageNumber,12));
        List<Question> list = new ArrayList<>(page.getContent());
        list.addAll(get99Multiple());
        Collections.shuffle(list);
        return list;
    }
    /**
     * @description: 九九乘法题
     * @author :景光赞
     * @date :2020/5/2 18:44
     * @param :[]
     * @return :java.util.List<com.knowledge_farm.answer.entity.Multiple>
     */
    @ResponseBody
    @GetMapping("/math99Multiple")
    public List<Question> get99Multiple(){
        return answerService.get99Multiple();
    }
    /**
     * @description: 三年级数学上册2、3位数乘 1 位数乘除法. 20道
     * @author :景光赞
     * @date :2020/5/2 18:44
     * @param :[]
     * @return :java.util.List<com.knowledge_farm.answer.entity.Multiple>
     */
    @ResponseBody
    @GetMapping("/mathThreeUp")
    public List<Question> mathThreeUp23Num(){
        List<Question> list = answerService.get23Multiple();
        list.addAll(answerService.findQuestion2(5,"Math",new QuestionType(1,"单选题")));
        list.addAll(answerService.findQuestion2(5,"Math",new QuestionType(3,"判断题")));
        Collections.shuffle(list);
        return list;
    }

    /**
     * @description: 三年级数学下册：2位数乘2位数
     * @author :景光赞
     * @date :2020/5/2 18:44
     * @param :[]
     * @return :java.util.List<com.knowledge_farm.answer.entity.Multiple>
     */
    @ResponseBody
    @GetMapping("/mathThreeDown")
    public List<Question> mathThreeDown(){
        List<Question> list = answerService.doubleMutiple();
        list.addAll(answerService.findQuestion2(6,"Math",new QuestionType(1,"单选题")));
        list.addAll(answerService.findQuestion2(6,"Math",new QuestionType(3,"判断题")));
        Collections.shuffle(list);
        return list;
    }

    /**
    * @Description: 语文一年级上册
    * @Param:
    * @return:
    * @Author: 景光赞
    * @Date: 2020/4/9
    */
    @ResponseBody
    @GetMapping("/ChineseOneUp")
    public List<Question> findChineseOneUp(){
        int pageNumber = new Random().nextInt(6);
        Page<Question> page =  answerService.findQuestion(1,"Chinese",PageRequest.of(pageNumber, 10));
        List<Question> list = new ArrayList(page.getContent());
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 语文一年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/ChineseOneDown")
    public List<Question> findChineseOneDown(){
        int pageNumber = new Random().nextInt(6);
        Page<Question> page =  answerService.findQuestion(2,"Chinese",PageRequest.of(pageNumber, 10));
        List<Question> list = new ArrayList(page.getContent());
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 语文二年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/ChineseTwoUp")
    public List<Question> findChineseTwoUp(){
        int pageNumber = new Random().nextInt(10);
        Page<Question> page =  answerService.findQuestion(3,"Chinese",PageRequest.of(pageNumber, 10));
        List<Question> list = new ArrayList(page.getContent());
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 语文二年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/ChineseTwoDown")
    public List<Question> findChineseTwoDown(){
        int pageNumber = new Random().nextInt(10);
        Page<Question> page =  answerService.findQuestion(4,"Chinese",PageRequest.of(pageNumber, 10));
        List<Question> list = new ArrayList(page.getContent());
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 语文三年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/ChineseThreeUp")
    public List<Question> findChineseThreeUp(){
        int pageNumber = new Random().nextInt(10);
        Page<Question> page =  answerService.findQuestion(5,"Chinese",PageRequest.of(pageNumber, 10));
        List<Question> list = new ArrayList(page.getContent());
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 语文三年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/ChineseThreeDown")
    public List<Question> findChineseThreeDown(){
        int pageNumber = new Random().nextInt(10);
        Page<Question> page =  answerService.findQuestion(6,"Chinese",PageRequest.of(pageNumber, 10));
        List<Question> list = new ArrayList(page.getContent());
        Collections.shuffle(list);
        return list;
    }

    @ResponseBody
    @GetMapping("/englishTk")
    public List<Question> getEnglishTk(List<Question> list){
        int i = 3;
        while (i-->0){
            String question_title = list.get(i).getQuestionTitle().getTitle();
            int size = question_title.length();
            int grade = list.get(i).getGrade();
            Question question = new Completion(new QuestionTitle(question_title),"English",new QuestionType(2,"填空题"),grade,new Random().nextInt(size)+1+"");
            list.set(i,question);
        }
        return list;
    }
    /**
     * @Description: 英语一年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/englishOneUp")
    public List<Question> englishOneUp() {
        int pageNumber = new Random().nextInt(5);
        Page<Question> page =  answerService.findQuestion(1,"English",PageRequest.of(pageNumber, 10));
        List<Question> list = getEnglishTk(new ArrayList(page.getContent()));
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 英语一年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/englishOneDown")
    public List<Question> englishOneDown() {
        int pageNumber = new Random().nextInt(5);
        Page<Question> page =  answerService.findQuestion(2,"English",PageRequest.of(pageNumber, 9));
        List<Question> list = getEnglishTk(new ArrayList(page.getContent()));
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 英语二年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/englishTwoUp")
    public List<Question> englishTwoUp() {
        int pageNumber = new Random().nextInt(5);
        Page<Question> page =  answerService.findQuestion(3,"English",PageRequest.of(pageNumber, 11));
        List<Question> list = getEnglishTk(new ArrayList(page.getContent()));
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 英语二年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/englishTwoDown")
    public List<Question> englishTwoDown() {
        int pageNumber = new Random().nextInt(5);
        Page<Question> page =  answerService.findQuestion(4,"English",PageRequest.of(pageNumber, 11));
        List<Question> list = getEnglishTk(new ArrayList(page.getContent()));
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 英语三年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/englishThreeUp")
    public List<Question> englishThreeUp() {
        int pageNumber = new Random().nextInt(5);
        Page<Question> page =  answerService.findQuestion(5,"English",PageRequest.of(pageNumber, 10));
        List<Question> list = getEnglishTk(new ArrayList(page.getContent()));
        Collections.shuffle(list);
        return list;
    }
    /**
     * @Description: 英语三年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @GetMapping("/englishThreeDown")
    public List<Question> englishThreeDown() {
        int pageNumber = new Random().nextInt(9);
        Page<Question> page =  answerService.findQuestion(6,"English",PageRequest.of(pageNumber, 12));
        List<Question> list = getEnglishTk(new ArrayList(page.getContent()));
        Collections.shuffle(list);
        return list;
    }

}
