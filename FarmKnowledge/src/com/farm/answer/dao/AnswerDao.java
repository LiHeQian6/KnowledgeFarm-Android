package com.farm.answer.dao;

import java.util.List;

import com.farm.model.Chinese;
import com.farm.model.English;
import com.jfinal.plugin.activerecord.Page;

	public class AnswerDao {
		//Ӣ��һ�꼶�ϲ�
		public Page<English> getEnglishWords(int pageNumber, int pageSize){
			Page<English> englishPage = English.dao.paginate(pageNumber, pageSize, "select *","from english");
			return englishPage;
		}
		
		//����һ�꼶�ϲ�
		public Page<Chinese> getChineseWords(int pageNumber, int pageSize){
			Page<Chinese> chinesePage = Chinese.dao.paginate(pageNumber, pageSize, "select *","from chinese");
			return chinesePage;
		}
}
