package com.chaosting.geoforce.saas.bak.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * 如果请求消息中包含gzip压缩数据，则进行解压
 * Created by Administrator on 2017/6/12.
 */
public class UnGzipFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String contentEncoding = ((HttpServletRequest) request).getHeader("Content-Encoding");
        if (null != contentEncoding && contentEncoding.indexOf("gzip") != -1) {
            chain.doFilter(new UnGzipRequestWrapper((HttpServletRequest) request), response);
        } else {
            chain.doFilter( request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
