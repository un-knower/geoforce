package com.dituhui.openapi.portal.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dituhui.openapi.portal.common.Constant;

public class EncodingFilter implements Filter {
    private String charEncoding = null;

    public EncodingFilter() {

    }

    public void init(FilterConfig fConfig) throws ServletException {
        charEncoding = fConfig.getInitParameter("encoding");
        System.out.println(charEncoding);
        if (charEncoding == null) {
            throw new ServletException("EncodingFilter不存在");
        }
    }

    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        boolean flag = true;
        if (!StringUtils.isEmpty(Constant.ALLOW_ORIGIN)) {
            HttpServletResponse httpResponse = (HttpServletResponse) res;
            //String []  allowDomain= {"http://132.12.11.11:8888","http://123.112.112.12:80","http://123.16.12.23",
            // "http://121.12.18.13:10195"};
            String[] allowDomain = Constant.ALLOW_ORIGIN.split(";");
            Set<String> allowedOrigins = new HashSet<String>(Arrays.asList(allowDomain));
            String originHeader = ((HttpServletRequest) req).getHeader("Origin");
            if (allowedOrigins.contains("*")) {
                httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            } else if (allowedOrigins.contains(originHeader)) {
                httpResponse.setHeader("Access-Control-Allow-Origin", originHeader);
            } else {
                flag = false;
            }
            if (flag) {
                httpResponse.setContentType("application/json;charset=UTF-8");
                httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                httpResponse.setHeader("Access-Control-Max-Age", "3600");
                httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, " +
                        "If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With," +
                        "userId,token");//表明服务器支持的所有头信息字段
                httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
                //如果要把Cookie发到服务器，需要指定Access-Control-Allow-Credentials字段为true;
                httpResponse.setHeader("XDomainRequestAllowed", "1");
            }
        }

        if (!charEncoding.equals(req.getCharacterEncoding())) {
            req.setCharacterEncoding(charEncoding);
        }
        res.setCharacterEncoding(charEncoding);
        chain.doFilter(req, res);
    }

    public void destroy() {
    }

}
