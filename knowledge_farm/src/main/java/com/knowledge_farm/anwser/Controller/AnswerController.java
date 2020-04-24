package com.knowledge_farm.anwser.Controller;

import com.knowledge_farm.anwser.entity.Question3Num;
import com.knowledge_farm.anwser.service.AnswerService;
import com.knowledge_farm.entity.Chinese;
import com.knowledge_farm.entity.Chinese23;
import com.knowledge_farm.entity.English;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @program: farm
 * @description: answer
 * @author: 景光赞
 * @create: 2020-04-09 19:19
 **/
@Controller
@RequestMapping("/answer")
public class AnswerController {
    @Resource
    private AnswerService answerService;

    /**
     * @Description: 数学一年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/mathOneUp")
    public List OneUpMath() {
        List<Question3Num> list = new AnswerService().getQuestion3OneUpMath();

        return list;
    }
    /**
     * @Description: 数学一年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/mathOneDown")
    public List OneDownMath() {
        List<Question3Num> list = new AnswerService().getQuestion3OneDownMath();

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
    @RequestMapping("/ChineseOneUp")
    public Page<Chinese> findChineseOneUp(){
        int pageNumber = new Random().nextInt(3);
        int pageSize = 20;
        Page<Chinese> page =  answerService.findChineseOneUp(PageRequest.of(pageNumber, pageSize));
        return page;
    }
    /**
     * @Description: 语文一年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/ChineseOneDown")
    public Page<Chinese> findChineseOneDown(){
        int pageNumber = new Random().nextInt(3);
        int pageSize = 20;
        Page<Chinese> page =  answerService.findChineseOneDown(PageRequest.of(pageNumber, pageSize));
        return page;
    }
    /**
     * @Description: 语文二年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/ChineseTwoUp")
    public Page<Chinese23> findChineseTwoUp(){
        int pageNumber = new Random().nextInt(5);
        int pageSize = 20;
        Page<Chinese23> page =  answerService.findChineseTwoUp(PageRequest.of(pageNumber, pageSize));
        return page;
    }
    /**
     * @Description: 语文二年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/ChineseTwoDown")
    public Page<Chinese23> findChineseTwoDown(){
        int pageNumber = new Random().nextInt(5);
        int pageSize = 20;
        Page<Chinese23> page =  answerService.findChineseTwoDown(PageRequest.of(pageNumber, pageSize));
        return page;
    }
    /**
     * @Description: 语文三年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/ChineseThreeUp")
    public Page<Chinese23> findChineseThreeUp(){
        int pageNumber = new Random().nextInt(5);
        int pageSize = 20;
        Page<Chinese23> page =  answerService.findChineseThreeUp(PageRequest.of(pageNumber, pageSize));
        return page;
    }
    /**
     * @Description: 语文三年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/ChineseThreeDown")
    public Page<Chinese23> findChineseThreeDown(){
        int pageNumber = new Random().nextInt(5);
        int pageSize = 20;
        Page<Chinese23> page =  answerService.findChineseThreeDown(PageRequest.of(pageNumber, pageSize));
        return page;
    }

    /**
     * @Description: 英语一年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/englishOneUp")
    public Page<English> englishOneUp() {
        int pageNumber = new Random().nextInt(2);
        Page<English> page =  answerService.findEnglishOneUp(PageRequest.of(pageNumber, 25));
        return page;
    }
    /**
     * @Description: 英语一年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/englishOneDown")
    public Page<English> englishOneDown() {
        int pageNumber = new Random().nextInt(2);
        Page<English> page =  answerService.findEnglishOneDown(PageRequest.of(pageNumber, 23));
        return page;
    }
    /**
     * @Description: 英语二年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/englishTwoUp")
    public Page<English> englishTwoUp() {
        int pageNumber = new Random().nextInt(3);
        Page<English> page =  answerService.findEnglishTwoUp(PageRequest.of(pageNumber, 18));
        return page;
    }
    /**
     * @Description: 英语二年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/englishTwoDown")
    public Page<English> englishTwoDown() {
        int pageNumber = new Random().nextInt(3);
        Page<English> page =  answerService.findEnglishTwoDown(PageRequest.of(pageNumber, 18));
        return page;
    }
    /**
     * @Description: 英语三年级上册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/englishThreeUp")
    public Page<English> englishThreeUp() {
        int pageNumber = new Random().nextInt(3);
        Page<English> page =  answerService.findEnglishThreeUp(PageRequest.of(pageNumber, 20));
        return page;
    }
    /**
     * @Description: 英语三年级下册
     * @Param:
     * @return:
     * @Author: 景光赞
     * @Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("/englishThreeDown")
    public Page<English> englishThreeDown() {
        int pageNumber = new Random().nextInt(5);
        Page<English> page =  answerService.findEnglishThreeDown(PageRequest.of(pageNumber, 22));
        return page;
    }
}
