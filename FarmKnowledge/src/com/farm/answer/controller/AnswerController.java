package com.farm.answer.controller;

import java.util.List;


import com.farm.answer.service.AnswerService;
import com.farm.entity.Question3Num;
import com.farm.model.Chinese;
import com.farm.model.English;
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
		int pageNumber = (int)(Math.random()*10);
		int pageSize = 20;
		Page<English> page =  new AnswerService().OneUpEnglish(pageNumber, pageSize);
		if(page != null) {
			renderJson(page.getList());
		}else {
			renderJson("[]");
		}
	}
	
	//һ�꼶�ϲ�����
	public void OneUpChinese() {
		int pageNumber = (int)(Math.random()*10);
		int pageSize = 20;
		Page<Chinese> page =  new AnswerService().OneUpChinese(pageNumber, pageSize);
		if(page != null) {
			renderJson(page.getList());
		}else {
			renderJson("[]");
		}
	}
}
