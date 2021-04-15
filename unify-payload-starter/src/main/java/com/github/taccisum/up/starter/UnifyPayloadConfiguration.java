package com.github.taccisum.up.starter;

import com.github.taccisum.up.PayloadFormatter;
import com.github.taccisum.up.support.spring.UnifyPayloadSpringMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * @author taccisum - liaojinfeng6938@dingtalk.com
 * @since 2021-04-15
 */
@ConditionalOnClass(RequestMappingHandlerAdapter.class)
public class UnifyPayloadConfiguration {
    @Bean
    public UnifyPayloadSpringMvcConfigurer returnValueConfigurer(
            @Autowired RequestMappingHandlerAdapter adapter,
            @Autowired(required = false) PayloadFormatter formatter
    ) {
        return new UnifyPayloadSpringMvcConfigurer(adapter, formatter);
    }
}
