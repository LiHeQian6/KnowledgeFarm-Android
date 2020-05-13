package com.knowledge_farm.answer.repository;

import com.knowledge_farm.entity.Question;
import com.knowledge_farm.entity.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface QuestionRepository extends JpaRepository<Question,Integer>, JpaSpecificationExecutor<Question>, Serializable {

    Page<Question> findByGradeAndSubject(int grade,String subject, Pageable pageable);

    Page<Question> findByGradeAndSubjectAndQuestionType(int grade, String subject, QuestionType type, Pageable pageable);
}
