package com.github.taccisum.up;

/**
 * @author taccisum - liaojinfeng6938@dingtalk.com
 * @since 2021-04-15
 */
public interface PayloadFormatter {
    /**
     * @param origin origin object
     * @return target object
     */
    Object format(Object origin);
}
