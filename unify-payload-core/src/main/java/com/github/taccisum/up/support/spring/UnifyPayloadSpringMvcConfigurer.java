package com.github.taccisum.up.support.spring;

import com.github.taccisum.up.Payload;
import com.github.taccisum.up.PayloadFormatter;
import com.github.taccisum.up.utils.ArrayListUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaojinfeng
 *
 * handle and unify format of return value
 */
public class UnifyPayloadSpringMvcConfigurer implements InitializingBean {
    private RequestMappingHandlerAdapter adapter;
    private PayloadFormatter formatter;

    public UnifyPayloadSpringMvcConfigurer(RequestMappingHandlerAdapter adapter, PayloadFormatter formatter) {
        this.adapter = adapter;
        this.formatter = formatter;
    }

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(adapter.getReturnValueHandlers());
        ArrayListUtils.findFirstAndReplace(
                handlers,
                RequestResponseBodyMethodProcessor.class,
                item -> new RequestResponseBodyMethodProcessorProxy((RequestResponseBodyMethodProcessor) item)
        );
        adapter.setReturnValueHandlers(handlers);
    }

    private class RequestResponseBodyMethodProcessorProxy implements HandlerMethodReturnValueHandler {
        private RequestResponseBodyMethodProcessor delegate;

        public RequestResponseBodyMethodProcessorProxy(RequestResponseBodyMethodProcessor delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean supportsReturnType(MethodParameter methodParameter) {
            if (!delegate.supportsReturnType(methodParameter)) {
                return false;
            }

            // 判断是否在方法或类上找到 @Payload
            if (AnnotationUtils.findAnnotation(methodParameter.getMethod(), Payload.class) != null) {
                return true;
            } else {
                Class<?> clazz = methodParameter.getContainingClass();
                return AnnotationUtils.findAnnotation(clazz, Payload.class) != null;
            }
        }

        @Override
        public void handleReturnValue(
                Object o,
                MethodParameter methodParameter,
                ModelAndViewContainer modelAndViewContainer,
                NativeWebRequest nativeWebRequest)
                throws Exception {
            Object target = o;
            if (formatter != null) {
                target = formatter.format(o);
            }
            delegate.handleReturnValue(target, methodParameter, modelAndViewContainer, nativeWebRequest);
        }
    }
}
