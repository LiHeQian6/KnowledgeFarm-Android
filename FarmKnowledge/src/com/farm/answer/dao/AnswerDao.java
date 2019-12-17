package com.farm.answer.dao;

import com.farm.model.Chinese;
import com.farm.model.EnglishOneDown;
import com.farm.model.EnglishOneUp;
import com.jfinal.plugin.activerecord.Page;

public class AnswerDao {
	
	//英语一年级上册
	public Page<EnglishOneUp> getEnglishWordOneUp(int pageNumber, int pageSize){
		Page<EnglishOneUp> englishPage = EnglishOneUp.dao.paginate(pageNumber, pageSize, "select *","from englishoneup");
		return englishPage;
	}
	
	//英语一年级下册
	public Page<EnglishOneDown> getEnglishWordOneDown(int pageNumber, int pageSize){
		Page<EnglishOneDown> englishPage = EnglishOneDown.dao.paginate(pageNumber, pageSize, "select *","from englishonedown");
		return englishPage;
	}
	
	//语文一年级上册
	public Page<Chinese> getChineseWords(int pageNumber, int pageSize){
		Page<Chinese> chinesePage = Chinese.dao.paginate(pageNumber, pageSize, "select *","from chinese");
		return chinesePage;
	}
}
