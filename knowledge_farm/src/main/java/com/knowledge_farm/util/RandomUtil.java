package com.knowledge_farm.util;

/**
 * @ClassName RandomUtil
 * @Description
 * @Author 张帅华
 * @Date 2020-04-07 19:52
 */
public class RandomUtil {
    public static String generateAccount(){
        String account = "";
        for(int n = 1;n < 9;n++) {
            account += (int)(Math.random()*10);
        }
        return account;
    }

}
