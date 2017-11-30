package com.dituhui.openapi.portal.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.dituhui.openapi.portal.common.Constant;

public class RequestUtil {

    public static boolean isAjax(HttpServletRequest request) {
        if (!StringUtils.isEmpty(Constant.ALLOW_ORIGIN))
            return true;
        final List<String> ajaxRequestParams = Arrays.asList("pragma", "cache-control", "origin");
        Enumeration<String> enumeration = request.getHeaderNames();
        int count = 0;
        while (enumeration.hasMoreElements()) {
            String element = enumeration.nextElement();
            System.out.println(element);
            if (ajaxRequestParams.contains(element)) {
                count++;
            }
        }
        return count == ajaxRequestParams.size() ? true : false;
    }
}
