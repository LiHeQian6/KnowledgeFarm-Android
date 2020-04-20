package com.knowledge_farm.anwser.service;

import com.knowledge_farm.anwser.entity.Question3Num;
import com.knowledge_farm.anwser.repository.ChineseRepository;
import com.knowledge_farm.anwser.repository.EnglishRepository;
import com.knowledge_farm.entity.Chinese;
import com.knowledge_farm.entity.English;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @program: farm
 * @description: answer
 * @author: 景光赞
 * @create: 2020-04-09 19:20
 **/
@Service
@Transactional(readOnly = true)
public class AnswerService {
    @Resource
    private ChineseRepository chineseRepository;
    @Resource
    private EnglishRepository englishRepository;

    //查询一年级上册语文题
    public Page<Chinese> findChineseOneUp(Pageable pageable){
        return chineseRepository.findByGrade("OneUp",pageable);
    }
    //查询一年级下册语文题
    public Page<Chinese> findChineseOneDown(Pageable pageable){
        return chineseRepository.findByGrade("OneDown",pageable);
    }
    //查询二年级上册语文题
    public Page<Chinese> findChineseTwoUp(Pageable pageable){
        return chineseRepository.findByGrade("TwoUp",pageable);
    }
    //查询二年级下册语文题
    public Page<Chinese> findChineseTwoDown(Pageable pageable){
        return chineseRepository.findByGrade("TwoDown",pageable);
    }
    //查询三年级上册语文题
    public Page<Chinese> findChineseThreeUp(Pageable pageable){
        return chineseRepository.findByGrade("ThreeUp",pageable);
    }
    //查询三年级下册语文题
    public Page<Chinese> findChineseThreeDown(Pageable pageable){
        return chineseRepository.findByGrade("ThreeDown",pageable);
    }

    //查询一年级上册英语题
    public Page<English> findEnglishOneUp(Pageable pageable){
        return englishRepository.findByGrade("OneUp",pageable);
    }
    //查询一年级下册英语题
    public Page<English> findEnglishOneDown(Pageable pageable){
        return englishRepository.findByGrade("OneDown",pageable);
    }
    //查询二年级上册英语
    public Page<English> findEnglishTwoUp(Pageable pageable){
        return englishRepository.findByGrade("TwoUp",pageable);
    }
    //查询二年级下册英语
    public Page<English> findEnglishTwoDown(Pageable pageable){
        return englishRepository.findByGrade("TwoDown",pageable);
    }
    //查询二年级上册英语
    public Page<English> findEnglishThreeUp(Pageable pageable){
        return englishRepository.findByGrade("ThreeUp",pageable);
    }
    //查询二年级下册英语
    public Page<English> findEnglishThreeDown(Pageable pageable){
        return englishRepository.findByGrade("ThreeDown",pageable);
    }
    //一年级上册数学
    public List<Question3Num> getQuestion3OneUpMath(){
        Random random = new Random();
        List<Question3Num> list  = new ArrayList<Question3Num>();
        int i = 10;
        while(i-->0) {
            String signal1 = random.nextBoolean()? "+": "-";
            String s = random.nextBoolean()? "+": "-";
            String signal2 = random.nextBoolean()? s :"";
            int num1 = random.nextInt(20);
            int num2 = random.nextInt(20);
            int num3 = 0;
            if(!signal2.equals("")) {
                num3 = random.nextInt(20);
            }
            int result = 0;
            int num12 = 0;
            if(signal2.equals("")) {
                if(signal1.equals("+")) {
                    num2 = random.nextInt(20-num1);
                    result = num1 + num2;
                }else {
                    num1 = random.nextInt(15)+5;
                    num2 = random.nextInt(num1+1);
                    result = num1 - num2;
                }
            }else {
                if(signal1.equals("-")) {
                    num1 = random.nextInt(15)+5;
                    num2 = random.nextInt(num1+1);
                    num12 = num1 - num2;
                    if(signal2.equals("-")) {
                        num3 = random.nextInt(num12+1);
                        result = num12 - num3;
                    }else {
                        num3 = random.nextInt(20-num12);
                        result = num12 + num3;
                    }
                }else {
                    num2 = random.nextInt(20-num1);
                    num12 = num1 + num2;
                    if(signal2.equals("-")) {
                        num3 = random.nextInt(num12+1);
                        result = num12 - num3;
                    }else {
                        num3 = random.nextInt(20-num12);
                        result = num12 + num3;
                    }
                }
            }
            list.add(new Question3Num(num1,signal1,num2,signal2,num3,result,"false"));
        }
        return list;
    }

    //一年级下册数学
    public List<Question3Num> getQuestion3OneDownMath(){
        Random random = new Random();
        List<Question3Num> list  = new ArrayList<Question3Num>();
        int i = 10;
        while(i-->0) {
            String signal1 = random.nextBoolean()? "+": "-";
            String x = random.nextBoolean()? "+": "-";
            String signal2 = random.nextBoolean()? x:"";
            int num1 = random.nextInt(100);
            int num2 = random.nextInt(100);
            int num3 = 0;
            if(!signal2.equals("")) {
                num3 = random.nextInt(100);
            }
            int result = 0;
            int num12 = 0;
            if(signal2.equals("")) {
                if(signal1.equals("+")) {
                    num2 = random.nextInt(100-num1);
                    result = num1 + num2;
                }else {
                    num1 = random.nextInt(80)+20;
                    num2 = random.nextInt(num1+1);
                    result = num1 - num2;
                }
            }else {
                if(signal1.equals("-")) {
                    num1 = random.nextInt(80)+20;
                    num2 = random.nextInt(num1+1);
                    num12 = num1 - num2;
                    if(signal2.equals("-")) {
                        num3 = random.nextInt(num12+1);
                        result = num12 - num3;
                    }else {
                        num3 = random.nextInt(100-num12);
                        result = num12 + num3;
                    }
                }else {
                    num12 = num1 + num2;
                    if(signal2.equals("-")) {
                        num3 = random.nextInt(num12+1);
                        result = num12 - num3;
                    }else {
                        num3 = random.nextInt(100-num12);
                        result = num12 + num3;
                    }
                }
            }
            list.add(new Question3Num(num1,signal1,num2,signal2,num3,result,"false"));
        }
        return list;
    }
}
