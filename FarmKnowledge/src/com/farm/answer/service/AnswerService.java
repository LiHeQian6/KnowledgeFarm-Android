package com.farm.answer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.farm.answer.dao.AnswerDao;
import com.farm.entity.Question3Num;
import com.farm.model.Chinese;
import com.farm.model.EnglishOneDown;
import com.farm.model.EnglishOneUp;
import com.jfinal.plugin.activerecord.Page;

public class AnswerService {
	
	//һ�꼶�ϲ�����
	public Page<Chinese> OneUpChinese(int pageNumber, int pageSize){
		return new AnswerDao().getChineseWords(pageNumber, pageSize);
	}
	
	//һ�꼶�ϲ�Ӣ��
	public Page<EnglishOneUp> OneUpEnglish(int pageNumber, int pageSize){
		return new AnswerDao().getEnglishWordOneUp(pageNumber, pageSize);
	}
	
	//Ӣ��һ�꼶�²�
	public Page<EnglishOneDown> OneDownEnglish(int pageNumber, int pageSize){
		return new AnswerDao().getEnglishWordOneDown(pageNumber, pageSize);
	}

	//һ�꼶�ϲ���ѧ
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
	
	//һ�꼶�²���ѧ
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
