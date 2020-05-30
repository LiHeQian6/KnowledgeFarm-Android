package com.knowledge_farm.answer.repository;

import com.knowledge_farm.entity.SingleChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface SingleChoiceRepository  extends JpaRepository<SingleChoice,Integer>, JpaSpecificationExecutor<SingleChoice>, Serializable {
    SingleChoice findById(int id);
}
