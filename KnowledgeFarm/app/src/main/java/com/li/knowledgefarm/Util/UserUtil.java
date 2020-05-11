package com.li.knowledgefarm.Util;

import com.li.knowledgefarm.entity.User;

public class UserUtil {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserUtil.user = user;
    }
}
