package com.farm.answer.controller;

import java.util.List;

import com.farm.answer.service.AnswerService;
import com.farm.entity.Question3Num;
import com.jfinal.core.Controller;

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

}
