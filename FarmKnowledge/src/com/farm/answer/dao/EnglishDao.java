package com.farm.answer.dao;

import java.util.List;


import com.farm.model.English;

	public class EnglishDao {
		
		public List<English> getEnglishWords(){
			List<English> list = English.dao.find("select * from english");
			
			return list;
			
	}
}
