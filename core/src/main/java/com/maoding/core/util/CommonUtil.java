package com.maoding.core.util;


import java.util.*;

/**
 * Created by Idccapp21 on 2016/8/6.
 */
public class CommonUtil {

    public static List removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        System.out.println( " remove duplicate " + list);
        return list;
    }

    public static int doubleCompare(Double d1, Double d2, Integer decimal){
        if (Math.abs(d1-d2) < 1/Math.pow(10,decimal))
            return 0;
        else if (d1 > d2)
            return 1;
        else
            return -1;
    }
    public static int doubleCompare(Double d1, Double d2) {
        final Integer DEFAULT_DECIMAL = 6;
        return doubleCompare(d1,d2,DEFAULT_DECIMAL);
    }
}
