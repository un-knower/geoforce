package com.chaosting.geoforce.saas.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * 支持前端jsonP访问
 */
@ControllerAdvice
public class JsonPAdvice extends AbstractJsonpResponseBodyAdvice{
    public JsonPAdvice() {
        super("callbacks","jsonp", "callback");
    }
}
