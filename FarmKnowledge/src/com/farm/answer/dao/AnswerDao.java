package com.farm.answer.dao;

import com.farm.model.Chinese;
import com.farm.model.EnglishOneDown;
import com.farm.model.EnglishOneUp;
import com.jfinal.plugin.activerecord.Page;

public class AnswerDao {
	
	//Ӣ��һ�꼶�ϲ�
	public Page<EnglishOneUp> getEnglishWordOneUp(int pageNumber, int pageSize){
		Page<EnglishOneUp> englishPage = EnglishOneUp.dao.paginate(pageNumber, pageSize, "select *","from englishoneup");
		return englishPage;
	}
	
	//Ӣ��һ�꼶�²�
	public Page<EnglishOneDown> getEnglishWordOneDown(int pageNumber, int pageSize){
		Page<EnglishOneDown> englishPage = EnglishOneDown.dao.paginate(pageNumber, pageSize, "select *","from englishonedown");
		return englishPage;
	}
	
	//����һ�꼶�ϲ�
	public Page<Chinese> getChineseWords(int pageNumber, int pageSize){
		Page<Chinese> chinesePage = Chinese.dao.paginate(pageNumber, pageSize, "select *","from chinese");
		return chinesePage;
	}
}
