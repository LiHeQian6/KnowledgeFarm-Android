package com.knowledge_farm.answer.repository;

import com.knowledge_farm.entity.English;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface EnglishRepository extends JpaRepository<English,Integer>, JpaSpecificationExecutor<English>, Serializable {

    Page<English> findByGrade(String grade, Pageable pageable);
}
