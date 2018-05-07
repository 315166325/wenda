package com.nowcoder.util;

import com.nowcoder.model.User;

public class ParseObjectToString {
    public static String parseUserToString(User user){
        return user.getId()+" "+user.getName()+" "+user.getHeadUrl()+" "+user.getPassword();
    }
}
