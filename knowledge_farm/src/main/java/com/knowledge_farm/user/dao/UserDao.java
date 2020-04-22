package com.knowledge_farm.user.dao;

import com.knowledge_farm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @ClassName UserDao
 * @Description
 * @Author 张帅华
 * @Date 2020-04-07 17:39
 */
public interface UserDao extends JpaRepository<User, Integer> {
//    @EntityGraph(value = "Category.Graph", type = EntityGraph.EntityGraphType.FETCH)
//    @Query("select u from User u where u.id = ?1")
    public User findUserById(Integer id);

    public User findUserByAccount(String account);

    public User findUserByAccountAndExist(String account, Integer exist);

    @Query("select u from User u where u.account = ?1 and u.account <> ?2")
    public User findUserByAccountAndExcludeAccount(String account, String excludeAccount);

    public User findUserByAccountAndPassword(String account, String password);

    public User findUserByEmail(String email);

    @Query("select u from User u")
    public Page<User> findAllUser(Pageable pageable);

    @Query("select u from User u where u.exist = ?1")
    public Page<User> findAllUserAndExist(Integer exist, Pageable pageable);

    @Query("select u from User u where u.account = ?1")
    public Page<User> findAllUserByAccount(String account, Pageable pageable);

    @Query("select u from User u where u.account = ?1 and u.exist = ?2")
    public Page<User> findAllUserByAccountAndExist(String account, Integer exist, Pageable pageable);

    @Query("update User u set u.mathRewardCount=3,u.englishRewardCount=3,u.chineseRewardCount=3")
    @Modifying
    public int updateUserRewardCount();
}
