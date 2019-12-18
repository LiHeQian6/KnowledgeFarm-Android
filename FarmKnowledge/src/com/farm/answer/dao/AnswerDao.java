package com.farm.answer.dao;

import com.farm.model.Chinese;
import com.farm.model.English;
import com.jfinal.plugin.activerecord.Page;

public class AnswerDao {
	
	//Ӣ��һ�꼶�ϲ�
	public Page<English> getEnglishWordOneUp(int pageNumber, int pageSize){
		Page<English> englishPage = English.dao.paginate(pageNumber, pageSize, "select *","from english where grade=OneUp");
		return englishPage;
	}
	
	//Ӣ��һ�꼶�²�
	public Page<English> getEnglishWordOneDown(int pageNumber, int pageSize){
		Page<English> englishPage = English.dao.paginate(pageNumber, pageSize, "select *","from english grade=OneDown");
		return englishPage;
	}
	
	//����һ�꼶�ϲ�
	public Page<Chinese> getChineseWordOneUp(int pageNumber, int pageSize){
		Page<Chinese> chinesePage = Chinese.dao.paginate(pageNumber, pageSize, "select *","from chinese where grade=OneUp");
		return chinesePage;
	}
	
	//����һ�꼶�²�
	public Page<Chinese> getChineseWordOneDown(int pageNumber, int pageSize){
		Page<Chinese> chinesePage = Chinese.dao.paginate(pageNumber, pageSize, "select *","from chinese where grade=OneDown");
		return chinesePage;
	}
	
}
