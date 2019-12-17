package com.farm.answer.dao;

import java.util.List;

import com.farm.model.Chinese;
import com.farm.model.English;
import com.jfinal.plugin.activerecord.Page;

	public class AnswerDao {
		//英语一年级上册
		public Page<English> getEnglishWords(int pageNumber, int pageSize){
			Page<English> englishPage = English.dao.paginate(pageNumber, pageSize, "select *","from english");
			return englishPage;
		}
		
		//语文一年级上册
		public Page<Chinese> getChineseWords(int pageNumber, int pageSize){
			Page<Chinese> chinesePage = Chinese.dao.paginate(pageNumber, pageSize, "select *","from chinese");
			return chinesePage;
		}
}
