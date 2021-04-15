package com.github.taccisum.up.support.spring;

import com.github.taccisum.up.Payload;
import com.github.taccisum.up.PayloadFormatter;
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
 * handle & unify format of return value
 */
public class ReturnValueConfigurer implements InitializingBean {
    private RequestMappingHandlerAdapter adapter;
    private PayloadFormatter formatter;

    public ReturnValueConfigurer(RequestMappingHandlerAdapter adapter, PayloadFormatter formatter) {
        this.adapter = adapter;
        this.formatter = formatter;
    }

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(adapter.getReturnValueHandlers());
        for (HandlerMethodReturnValueHandler item : handlers) {
            int index = handlers.indexOf(item);
            if (RequestResponseBodyMethodProcessor.class.isAssignableFrom(item.getClass())) {
                handlers.add(index, new RequestResponseBodyMethodProcessorProxy((RequestResponseBodyMethodProcessor) item));
                handlers.remove(item);
                break;
            }
        }
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

            // 是否在方法或类上找到 @Payload
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
            delegate.handleReturnValue(formatter.format(o), methodParameter, modelAndViewContainer, nativeWebRequest);
        }
    }
}
