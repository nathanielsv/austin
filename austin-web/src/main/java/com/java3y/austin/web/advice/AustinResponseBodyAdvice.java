package com.java3y.austin.web.advice;

import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.web.annotation.AustinResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author kl
 * @version 1.0.0
 * @description 统一返回结构
 * @date 2023/2/9 19:00
 */
@ControllerAdvice(basePackages = "com.java3y.austin.web.controller")
public class AustinResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final String RETURN_CLASS = "BasicResultVO";

    /**
     * 加了注解AustinResult，会被切入
     * @param methodParameter the return type
     * @param aClass the selected converter type
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.getContainingClass().isAnnotationPresent(AustinResult.class) || methodParameter.hasMethodAnnotation(AustinResult.class);
    }

    /**
     * 被切面 如果返回值是BasicResultVO，直接返回
     * 如果不是，包装一层再返回
     * @param data the body to be written
     * @param methodParameter the return type of the controller method
     * @param mediaType the content type selected through content negotiation
     * @param aClass the converter type selected to write to the response
     * @param serverHttpRequest the current request
     * @param serverHttpResponse the current response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter, MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (Objects.nonNull(data) && Objects.nonNull(data.getClass())) {
            String simpleName = data.getClass().getSimpleName();
            if (RETURN_CLASS.equalsIgnoreCase(simpleName)) {
                return data;
            }
        }
        return BasicResultVO.success(data);
    }
}
