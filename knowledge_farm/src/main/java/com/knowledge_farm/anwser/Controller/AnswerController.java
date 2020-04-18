package com.knowledge_farm.anwser.Controller;

import com.knowledge_farm.anwser.entity.Question3Num;
import com.knowledge_farm.anwser.service.AnswerService;
import com.knowledge_farm.entity.Chinese;
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
        int pageSize;

        if(pageNumber == 1) {
            pageSize = 20;
        }else {
            pageSize = 25;
        }
        Page<English> page =  answerService.findEnglishOneDown(PageRequest.of(pageNumber, pageSize));
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
        int pageSize;

        if(pageNumber == 1) {
            pageSize = 20;
        }else {
            pageSize = 25;
        }
        Page<English> page =  answerService.findEnglishOneDown(PageRequest.of(pageNumber, pageSize));
        return page;
    }
}
