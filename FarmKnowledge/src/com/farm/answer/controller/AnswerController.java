package com.farm.answer.controller;

import java.util.List;
import java.util.Random;

import com.farm.answer.service.AnswerService;
import com.farm.entity.Question3Num;
import com.farm.model.Chinese;
import com.farm.model.EnglishOneDown;
import com.farm.model.EnglishOneUp;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class AnswerController extends Controller{
	//һ�꼶�ϲ���ѧ
	public void OneUpMath() {
		List<Question3Num> list = new AnswerService().getQuestion3OneUpMath();
		
		renderJson(list);
	}
	//һ�꼶�²���ѧ
	public void OneDownMath() {
		List<Question3Num> list = new AnswerService().getQuestion3OneDownMath();
		
		renderJson(list);
	}
	
	//һ�꼶�ϲ�Ӣ��
	public void OneUpEnglish() {
		int pageNumber = new Random().nextInt(2)+1;
		int pageSize;
		
		if(pageNumber == 1) {
			pageSize = 20;
		}else {
			pageSize = 25;
		}
		
		Page<EnglishOneUp> page =  new AnswerService().OneUpEnglish(pageNumber, pageSize);
		if(page != null) {
			renderJson(page.getList());
		}else {
			renderJson("[]");
		}
	}
	
	//һ�꼶�²�Ӣ��
	public void OneDownEnglish() {
		int pageNumber = new Random().nextInt(2)+1;
		int pageSize;
		
		if(pageNumber == 1) {
			pageSize = 20;
		}else {
			pageSize = 25;
		}
		
		Page<EnglishOneDown> page = new AnswerService().OneDownEnglish(pageNumber, pageSize);
		if(page != null) {
			renderJson(page.getList());
		}else {
			renderJson("[]");
		}
	}
	
	//һ�꼶�ϲ�����
	public void OneUpChinese() {
		int pageNumber = new Random().nextInt(3)+1;
		int pageSize = 20;
		Page<Chinese> page =  new AnswerService().OneUpChinese(pageNumber, pageSize);
		if(page != null) {
			renderJson(page.getList());
		}else {
			renderJson("[]");
		}
	}
}
