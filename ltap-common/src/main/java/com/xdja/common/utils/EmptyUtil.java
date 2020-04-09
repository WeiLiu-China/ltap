package com.xdja.common.utils;


import java.util.*;

/**
 *
 * @author wzh_jj
 */
public class EmptyUtil {

    public static final String STRING = "";
    public static final Set<?> SET = new HashSet<>();
    public static final List<?> LIST = new ArrayList<>();
    public static final Map<?, ?> MAP = new HashMap<>();


    public static boolean isNotEmpty(CharSequence charSequence) {
        return !isEmpty(charSequence);
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    public static boolean isNotEmpty(Map<?, ?> map){
        return !isEmpty(map);
    }
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    public static <T> boolean isEmpty(T[] objects) {
        return objects == null || objects.length == 0;
    }
    public static <T> boolean isNotEmpty(T[] objects) {
        return !isEmpty(objects);
    }
}
