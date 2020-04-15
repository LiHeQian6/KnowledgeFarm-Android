package com.atguigu.farm.entity;

/**
 * @ClassName Mapping
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 15:39
 */
public class Mapping {
    /**
     * user userauthority 双向一对一
     * user land 双向一对一
     * land usercrop 单向一对一
     * crop usercrop 单向多对一
     * user userbag 单向一对多
     * crop userbag 单向多对一
     * user user(friend) userfriend 多对多 中间关系表
     *
     **/

}
