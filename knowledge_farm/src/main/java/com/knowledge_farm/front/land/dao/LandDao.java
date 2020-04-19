package com.knowledge_farm.front.land.dao;

import com.knowledge_farm.entity.Land;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @ClassName LandDao
 * @Description
 * @Author 张帅华
 * @Date 2020-04-19 08:40
 */
public interface LandDao extends JpaRepository<Land, Integer> {
    @Query("select l from Land l where l.user.id = ?1")
    public Page<Land> findALLByUser(Integer userId, Pageable pageable);

    public Land findLandById(Integer id);
}
