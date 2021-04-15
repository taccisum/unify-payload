package com.github.taccisum.up.utils;

import java.util.List;
import java.util.function.Function;

/**
 * @author taccisum - liaojinfeng6938@dingtalk.com
 * @since 2021-04-15
 */
public abstract class ArrayListUtils {
    /**
     * 找到第一个匹配的 item 并替换
     */
    public static <T> void findFirstAndReplace(List<T> ls, Function<T, Boolean> matcher, Function<T, T> func) {
        for (T item : ls) {
            if (matcher.apply(item)) {
                int index = ls.indexOf(item);
                ls.add(index, func.apply(item));
                ls.remove(item);
                break;
            }
        }
    }

    public static <T> void findFirstAndReplace(List<T> ls, Class<? extends T> clazz, Function<T, T> func) {
        findFirstAndReplace(
                ls,
                item -> clazz.isAssignableFrom(item.getClass()),
                func
        );
    }
}
