package com.knowledge_farm.anwser.repository;

import com.knowledge_farm.entity.Chinese;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface ChineseRepository extends JpaRepository<Chinese,Integer>, JpaSpecificationExecutor<Chinese>, Serializable {

    Page<Chinese> findByGrade(String grade, Pageable pageable);
}
