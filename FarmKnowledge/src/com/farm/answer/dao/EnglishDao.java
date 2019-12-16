package com.farm.answer.dao;

import com.farm.model.English;
import com.jfinal.plugin.activerecord.Page;

public class EnglishDao {
		
	public Page<English> getEnglishWords(int pageNumber, int pageSize){
		Page<English> englishPage = English.dao.paginate(pageNumber, pageSize, "select *","from english");
		return englishPage;
	}
	
}
