package com.chaosting.geoforce.saas.bak.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chaosting.geoforce.saas.bak.controller.util.JedisUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.chaosting.geoforce.saas.bak.exception.ParameterException;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Value("${gateway.test.open}")
    private String open;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        System.out.println("AuthInterceptor preHandle");

        //公共参数检查：ak,output,callback等，有问题抛异常，由BaseController中的全局异常去处理
        String ak = request.getParameter("ak");
        String output = request.getParameter("output");
        if (ak == null) {
            throw new ParameterException("ak");
        }
        if ("".equals(ak.trim())) {
            throw new AuthenticationException(ak);
        }
        if (!(output == null || output.equals("json") || output.equals("geojson"))) {
            throw new ParameterException("output值不是json或geojson");
        }

        if (StringUtils.isBlank(open) || !open.equals(Boolean.TRUE.toString())) {

            //如果ak有登录过，且subject中的prncipal与当前ak一样，说明ak没问题，可以不做认证；否则要做认证//认证失败交由BaseController中的全局异常去处理
            Subject subject = SecurityUtils.getSubject();
            if (!(subject.isAuthenticated() && subject.getPrincipal().equals(ak))) {
                subject.login(new UsernamePasswordToken(ak, ""));
            }
            //无论做不做认证，之后都需要鉴权。只要AuthRealm中的seesion中的Api中，包含有当前用户请求的路径，说明有权限；否则无权限。//鉴权失败交由BaseController中的全局异常去处理
            if (!subject.isPermitted("api:all")) {
                throw new AuthorizationException();
            }
        }

        return true;
    }

}
