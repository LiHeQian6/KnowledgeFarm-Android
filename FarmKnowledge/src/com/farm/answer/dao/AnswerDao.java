package com.farm.answer.dao;

import com.farm.model.Chinese;
import com.farm.model.English;
import com.jfinal.plugin.activerecord.Page;

public class AnswerDao {
	
	//英语一年级上册
	public Page<English> getEnglishWordOneUp(int pageNumber, int pageSize){
		Page<English> englishPage = English.dao.paginate(pageNumber, pageSize, "select *","from english where grade=OneUp");
		return englishPage;
	}
	
	//英语一年级下册
	public Page<English> getEnglishWordOneDown(int pageNumber, int pageSize){
		Page<English> englishPage = English.dao.paginate(pageNumber, pageSize, "select *","from english grade=OneDown");
		return englishPage;
	}
	
	//语文一年级上册
	public Page<Chinese> getChineseWordOneUp(int pageNumber, int pageSize){
		Page<Chinese> chinesePage = Chinese.dao.paginate(pageNumber, pageSize, "select *","from chinese where grade=OneUp");
		return chinesePage;
	}
	
	//语文一年级下册
	public Page<Chinese> getChineseWordOneDown(int pageNumber, int pageSize){
		Page<Chinese> chinesePage = Chinese.dao.paginate(pageNumber, pageSize, "select *","from chinese where grade=OneDown");
		return chinesePage;
	}
	
}
