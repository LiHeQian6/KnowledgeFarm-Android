package com.atguigu.farm.front.dao;

import com.atguigu.farm.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @ClassName AdminDao
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 15:44
 */
public interface AdminDao extends JpaRepository<Admin, Integer> {
    public Admin findAdminById(Integer id);

    public Admin findAdminByAccount(String account);

    public Admin findAdminByAccountAndExist(String account, Integer exist);

    public Admin findAdminByAccountAndPassword(String account, String password);

    @Query("select ad from Admin ad where ad.exist = ?1")
    public Page<Admin> findPageAdmin(Integer exist, Pageable pageable);

    @Query("select ad from Admin ad where ad.account like ?1 and ad.exist = ?2")
    public Page<Admin> findPageAdminByAccount(String account, Integer exist, Pageable pageable);
}
