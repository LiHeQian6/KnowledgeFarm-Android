package com.knowledge_farm.answer.repository;

import com.knowledge_farm.entity.QuestionTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface QuestionTitleRepository extends JpaRepository<QuestionTitle,Integer>, JpaSpecificationExecutor<QuestionTitle>, Serializable {

}
