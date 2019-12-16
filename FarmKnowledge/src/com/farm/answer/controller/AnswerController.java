package com.farm.answer.controller;


import java.util.List;


import com.farm.answer.service.AnswerService;
import com.farm.entity.Question3Num;
import com.farm.model.English;
import com.jfinal.core.Controller;
public class AnswerController extends Controller{
	//一年级上册数学
	public void OneUpMath() {
		List<Question3Num> list = new AnswerService().getQuestion3OneUpMath();
		
		renderJson(list);
	}
	//一年级下册数学
	public void OneDownMath() {
		List<Question3Num> list = new AnswerService().getQuestion3OneDownMath();
		
		renderJson(list);
	}
	
	//一年级上册英语
	public void OneUpEnglish() {
		List<English> listWord = new AnswerService().OneUpEnglish();
		
		renderJson(listWord);
	}

}
