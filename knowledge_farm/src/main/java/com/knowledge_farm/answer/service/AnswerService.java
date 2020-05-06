package com.knowledge_farm.answer.service;

import com.knowledge_farm.answer.entity.Mix;
import com.knowledge_farm.answer.entity.Multiple;
import com.knowledge_farm.answer.entity.Question3Num;
import com.knowledge_farm.answer.repository.Chinese23Repository;
import com.knowledge_farm.answer.repository.ChineseRepository;
import com.knowledge_farm.answer.repository.EnglishRepository;
import com.knowledge_farm.answer.repository.Math23Repository;
import com.knowledge_farm.entity.Chinese;
import com.knowledge_farm.entity.Chinese23;
import com.knowledge_farm.entity.English;
import com.knowledge_farm.entity.Math23;
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
    @Resource
    private Chinese23Repository chinese23Repository;
    @Resource
    private Math23Repository math23Repository;

    //查询一年级上册语文题
    public Page<Chinese> findChineseOneUp(Pageable pageable){
        return chineseRepository.findByGrade("OneUp",pageable);
    }
    //查询一年级下册语文题
    public Page<Chinese> findChineseOneDown(Pageable pageable){
        return chineseRepository.findByGrade("OneDown",pageable);
    }
    //查询二年级上册语文题
    public Page<Chinese23> findChineseTwoUp(Pageable pageable){
        return chinese23Repository.findByGrade("TwoUp",pageable);
    }
    //查询二年级下册语文题
    public Page<Chinese23> findChineseTwoDown(Pageable pageable){
        return chinese23Repository.findByGrade("TwoDown",pageable);
    }
    //查询三年级上册语文题
    public Page<Chinese23> findChineseThreeUp(Pageable pageable){
        return chinese23Repository.findByGrade("ThreeUp",pageable);
    }
    //查询三年级下册语文题
    public Page<Chinese23> findChineseThreeDown(Pageable pageable){
        return chinese23Repository.findByGrade("ThreeDown",pageable);
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
    //二年级上册数学
    public Page<Math23> mathTwoUp(Pageable pageable){
        return math23Repository.findAllByGrade("TwoUp",pageable);
    }
    //二年级上册数学
    public Page<Math23> mathTwoDown(Pageable pageable){
        return math23Repository.findAllByGrade("TwoDown",pageable);
    }

    //数学乘法——九九乘法表
    public List<Multiple> get99Multiple(){
        List<Multiple> list  = new ArrayList<Multiple>();
        for(int i = 1;i<10;i++){
            for (int j=1;j<=i;j++){
                list.add(new Multiple(j,"×",i,j*i));
            }
        }
        return list;
    }
    //数学乘除法——2,3位数乘以一位数，除以一位数
    public List<Multiple> get23Multiple(){
        Random random = new Random();
        List<Multiple> list  = new ArrayList<Multiple>();
        int i = 20;
        while (i-->0){
            String signal = random.nextBoolean()?"×":"÷";
            int doubleNum = random.nextInt(390)+10;
            int singleNum = random.nextInt(8)+2;
            int num = 0;
            if(signal.equals("×")){
                num = doubleNum*singleNum;
            }else {
                num = doubleNum;
                doubleNum = doubleNum*singleNum;
            }
            list.add(new Multiple(doubleNum,signal,singleNum,num));
        }
        return list;
    }

    //四则运算(三元）
    public List<Mix> getMix() {
        Random random = new Random();
        List<Mix> list = new ArrayList<Mix>();
        int i = 20;
        while(i-->0){
            int num1 = random.nextInt(290)+10;
            int num2 = 0;
            int num3 = 0;
            int num = 0;
            String brackets1 = "false";
            String brackets2 = "false";
            String signal1 = randomSignal();
            String signal2 = "null";
            if(signal1.equals("+")||signal1.equals("-")){
                brackets1 = random.nextBoolean()?"false":"true";
                signal2 = random.nextBoolean()?"×":"÷";
                num2 = random.nextInt(80)+10;
                num3 = random.nextInt(8)+2;
                int num12 = 0;
                int num23 = 0;
                if(signal1.equals("+")){
                    if(brackets1.equals("false")){
                        num23 = num2 * num3;
                        num = num23 + num1;
                        if(signal2.equals("÷")){
                            num2 = num23;
                            num23 = num2/num3;
                            num = num23 + num1;
                        }
                    }else {
                        num12 = num1 + num2;
                        num = num12 * num3;
                        if(signal2.equals("÷")){
                            int remain = num12 % num3;
                            if(remain == 0){
                                num = num12/num3;
                            }else {
                                num2 += num3 - remain;
                                num = (num1 + num2)/num3;
                            }
                        }
                    }

                }else {
                    if(brackets1.equals("false")){
                        num23 = num2 * num3;
                        if(num1<num23){
                            num1 = num23+random.nextInt(80)+10;
                        }
                        num = num1 - num23;
                        if(signal2.equals("÷")){
                            num2 = num23;
                            num23 = num2/num3;
                            if(num1<num23){
                                num1 = num23+random.nextInt(80)+10;
                            }
                            num = num1 - num23;
                        }
                    }else {
                        if(num1<num2){
                            num1 = num2+random.nextInt(90)+10;
                        }
                        num12 = num1 - num2;
                        num = num12 * num3;
                        if(signal2.equals("÷")){
                            int remain = num12 % num3;
                            if(remain == 0){
                                num = num12/num3;
                            }else {
                                num1 += num3 - remain;
                                num = (num1 - num2)/num3;
                            }
                        }
                    }
                }
            }else {
                brackets2 = random.nextBoolean()?"false":"true";
                signal2 = random.nextBoolean()?"+":"-";
                num1 = random.nextInt(80)+10;
                num2 = random.nextInt(8)+2;
                num3 = random.nextInt(290)+10;
                int num12 = 0;
                int num23 = 0;
                if(signal1.equals("×")){
                    if(brackets2.equals("false")){
                        num12 = num1 * num2;
                        num = num12 + num3;
                        if(signal2.equals("-")){
                            num1 += num3/num2;
                            num  = num1 * num2 - num3;
                        }
                    }else {
                        num1 = random.nextInt(8)+2;
                        num2 = random.nextInt(290)+10;
                        num3 = random.nextInt(290)+10;
                        num = num1 * (num2 + num3);
                        if(signal2.equals("-")){
                            num2 = random.nextInt(390)+50;
                            num3 = random.nextInt(num2)+20;
                            num = num1 * (num2 - num3);
                        }
                    }
                }else {
                    num1 = random.nextInt(390)+11;
                    num2 = random.nextInt(8)+2;
                    if(brackets2.equals("false")){
                        int remain = num1 % num2;
                        if(remain == 0) {
                            num = num1 / num2;
                        }else {
                            num1 += num2 - remain;
                        }
                        num12 = num1/num2;
                        num = num12 + num3;
                        if(signal2.equals("-")){
                            num3 = random.nextInt(num12)+1;
                            num = num12 - num3;
                        }
                    }else {
                        num1 = random.nextInt(590)+10;
                        num2 = random.nextInt(9)+1;
                        num3 = random.nextInt(9-num2);
                        num23 = num2 + num3;
                        int remain = num1 % num23;
                        if(remain == 0) {
                            num = num1 / num23;
                        }else {
                            num1 += num23 - remain;
                            num = num1 / num23;
                        }
                        if(signal2.equals("-")){
                            num3 = random.nextInt(290)+1;
                            num2 = random.nextInt(10)+num3;
                            num23 = num2 - num3;
                            remain = num1 % num23;
                            if(remain == 0) {
                                num = num1 / num23;
                            }else {
                                num1 += num23 - remain;
                                num = num1 / num23;
                            }
                        }
                    }
                }
            }
            list.add(new Mix(num1,signal1,num2,signal2,num3,brackets1,brackets2,num));
        }
        return list;
    }

    //两位数乘法————三年级下册
    public List<Multiple> doubleMutiple(){
        Random random = new Random();
        List<Multiple> list  = new ArrayList<Multiple>();
        int i = 20;
        while (i-->0){
            String signal = "×";
            int num1 = random.nextInt(90)+10;
            int num2 = random.nextInt(90)+10;
            int num = num1 * num2;
            list.add(new Multiple(num1,signal,num2,num));
        }
        return list;
    }


    //随机符号
    public String randomSignal(){
        int a = new Random().nextInt(4);
        switch (a){
            case 0:
                return "+";
            case 1:
                return "-";
            case 2:
                return "×";
            case 3:
                return "÷";
        }
        return "";
    }
}
