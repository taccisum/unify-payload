package com.github.taccisum.up.utils;

import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author taccisum - liaojinfeng6938@dingtalk.com
 * @since 2021-04-15
 */
public class ArrayListUtilsTest {
    @Test
    public void findAndReplace() {
        ArrayList<Object> ls = new ArrayList<>();
        ls.add(1);
        ls.add("abc");
        ls.add(true);
        ArrayListUtils.findFirstAndReplace(ls, String.class, s -> s + " hello");
        assertThat(ls.get(1)).isEqualTo("abc hello");
        assertThat(ls.size()).isEqualTo(3);
    }
}