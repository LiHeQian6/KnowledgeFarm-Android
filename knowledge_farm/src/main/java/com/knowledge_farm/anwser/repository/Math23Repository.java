package com.knowledge_farm.anwser.repository;

import com.knowledge_farm.entity.Math23;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface Math23Repository extends JpaRepository<Math23,Integer>, JpaSpecificationExecutor<Math23>, Serializable {

    Page<Math23> findAllByGrade(String grade, Pageable pageable);
}
