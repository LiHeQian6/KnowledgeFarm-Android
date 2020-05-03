package com.knowledge_farm.answer.Controller;

import com.knowledge_farm.answer.entity.Mix;
import com.knowledge_farm.answer.entity.Multiple;
import com.knowledge_farm.answer.entity.Question3Num;
import com.knowledge_farm.answer.service.AnswerService;
import com.knowledge_farm.entity.Chinese;
import com.knowledge_farm.entity.Chinese23;
import com.knowledge_farm.entity.English;
import com.knowledge_farm.entity.Math23;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @description: 一年级数学上册
     * @author :景光赞
     * @date :2020/4/30 11:11
     * @param :[]
     * @return :java.util.List
     */
    @ResponseBody
    @GetMapping("/mathOneUp")
    public List OneUpMath() {
        List<Question3Num> list = new AnswerService().getQuestion3OneUpMath();

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
        List<Question3Num> list = new AnswerService().getQuestion3OneDownMath();

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
        int pageNumber = new Random().nextInt(5);
        Page<Math23> page = answerService.mathTwoUp(PageRequest.of(pageNumber,20));
        List<Math23> list = new ArrayList<>(page.getContent());
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
        int pageNumber = new Random().nextInt(5);
        Page<Math23> page = answerService.mathTwoDown(PageRequest.of(pageNumber,22));
        List<Math23> list = new ArrayList<>(page.getContent());
        Collections.shuffle(list);
        return list;
    }
    /**
     * @description: 九九乘法题 10道
     * @author :景光赞
     * @date :2020/5/2 18:44
     * @param :[]
     * @return :java.util.List<com.knowledge_farm.answer.entity.Multiple>
     */
    @ResponseBody
    @GetMapping("/math99Multiple")
    public List<Multiple> get99Multiple(){
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
    @GetMapping("/mathThreeUp23Num")
    public List<Multiple> mathThreeUp23Num(){
        return answerService.get23Multiple();
    }
    /**
     * @description: 三年级数学上下册通用：四则运算  20 道
     * @author :景光赞
     * @date :2020/5/2 18:44
     * @param :[]
     * @return :java.util.List<com.knowledge_farm.answer.entity.Mix>
     */
    @ResponseBody
    @GetMapping("/mathThreeMix")
    public List<Mix> mathThreeMix(){
        return answerService.getMix();
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
    public List<Multiple> mathThreeDown(){
        return answerService.doubleMutiple();
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
    public List<Chinese> findChineseOneUp(){
        int pageNumber = new Random().nextInt(3);
        Page<Chinese> page =  answerService.findChineseOneUp(PageRequest.of(pageNumber, 20));
        List<Chinese> list = new ArrayList(page.getContent());
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
    public List<Chinese> findChineseOneDown(){
        int pageNumber = new Random().nextInt(3);
        Page<Chinese> page =  answerService.findChineseOneDown(PageRequest.of(pageNumber, 20));
        List<Chinese> list = new ArrayList(page.getContent());
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
    public List<Chinese23> findChineseTwoUp(){
        int pageNumber = new Random().nextInt(5);
        Page<Chinese23> page =  answerService.findChineseTwoUp(PageRequest.of(pageNumber, 20));
        List<Chinese23> list = new ArrayList(page.getContent());
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
    public List<Chinese23> findChineseTwoDown(){
        int pageNumber = new Random().nextInt(5);
        Page<Chinese23> page =  answerService.findChineseTwoDown(PageRequest.of(pageNumber, 20));
        List<Chinese23> list = new ArrayList(page.getContent());
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
    public List<Chinese23> findChineseThreeUp(){
        int pageNumber = new Random().nextInt(5);
        Page<Chinese23> page =  answerService.findChineseThreeUp(PageRequest.of(pageNumber, 20));
        List<Chinese23> list = new ArrayList(page.getContent());
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
    public List<Chinese23> findChineseThreeDown(){
        int pageNumber = new Random().nextInt(5);
        Page<Chinese23> page =  answerService.findChineseThreeDown(PageRequest.of(pageNumber, 20));
        List<Chinese23> list = new ArrayList(page.getContent());
        Collections.shuffle(list);
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
    public List<English> englishOneUp() {
        int pageNumber = new Random().nextInt(3);
        Page<English> page =  answerService.findEnglishOneUp(PageRequest.of(pageNumber, 17));
        List<English> list = new ArrayList(page.getContent());
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
    public List<English> englishOneDown() {
        int pageNumber = new Random().nextInt(3);
        Page<English> page =  answerService.findEnglishOneDown(PageRequest.of(pageNumber, 16));
        List<English> list = new ArrayList(page.getContent());
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
    public List<English> englishTwoUp() {
        int pageNumber = new Random().nextInt(3);
        Page<English> page =  answerService.findEnglishTwoUp(PageRequest.of(pageNumber, 18));
        List<English> list = new ArrayList(page.getContent());
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
    public List<English> englishTwoDown() {
        int pageNumber = new Random().nextInt(3);
        Page<English> page =  answerService.findEnglishTwoDown(PageRequest.of(pageNumber, 18));
        List<English> list = new ArrayList(page.getContent());
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
    public List<English> englishThreeUp() {
        int pageNumber = new Random().nextInt(4);
        Page<English> page =  answerService.findEnglishThreeUp(PageRequest.of(pageNumber, 15));
        List<English> list = new ArrayList(page.getContent());
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
    public Page<English> englishThreeDown() {
        int pageNumber = new Random().nextInt(6);
        Page<English> page =  answerService.findEnglishThreeDown(PageRequest.of(pageNumber, 18));
        return page;
    }

}
