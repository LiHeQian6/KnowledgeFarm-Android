package com.knowledge_farm.front.version_upload.dao;

import com.knowledge_farm.entity.Version;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName VersionDao
 * @Description
 * @Author 张帅华
 * @Date 2020-05-30 21:25
 */
public interface VersionDao extends JpaRepository<Version, Integer> {
    Page<Version> findAll(Specification<Version> specification, Pageable pageable);
    Version findVersionById(Integer id);

    @Query("select distinct v.versionName from Version v")
    List<String> findAllVersionName();

    @Query("select v from Version v where v.id = (select max(v2.id) from Version v2)")
    Version findLastVersion();
}
