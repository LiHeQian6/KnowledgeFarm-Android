package com.knowledge_farm.anwser.repository;

import com.knowledge_farm.entity.Chinese23;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface Chinese23Repository extends JpaRepository<Chinese23,Integer>, JpaSpecificationExecutor<Chinese23>, Serializable {

    Page<Chinese23> findByGrade(String grade, Pageable pageable);
}
