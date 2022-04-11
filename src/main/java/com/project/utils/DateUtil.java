package com.project.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    public static String toPattern(String pattern, Date date){
        if(pattern == null || pattern.isEmpty() || date == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}
