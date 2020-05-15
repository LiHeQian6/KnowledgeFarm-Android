package com.knowledge_farm.answer.repository;

import com.knowledge_farm.entity.Question;
import com.knowledge_farm.entity.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer>, JpaSpecificationExecutor<Question>, Serializable {

    Page<Question> findByGradeAndSubject(int grade,String subject, Pageable pageable);

    List<Question> findByGradeAndSubjectAndQuestionType(int grade, String subject, QuestionType type);

    List<Question> findByGrade(int grade);
    Page<Question> findByGradeAndSubjectAndQuestionType(int grade, String subject, QuestionType type, Pageable pageable);

    @Query("select q from Question q where q.questionType.id = ?1")
    List<Question> findAllByQuestionType(Integer questionType);

    Question findQuestionById(Integer id);

    @Query("select qt from QuestionType qt where qt.id = ?1")
    QuestionType findQuestionTypeById(Integer id);

    @Query("select qt from QuestionType qt")
    List<QuestionType> findAllQuestionType();

    Page<Question> findAll(Specification<Question> specification, Pageable pageable);

    @Query("delete from Question q where q.id in ?1")
    @Modifying
    int deleteAllById(List<Integer> idList);



}
