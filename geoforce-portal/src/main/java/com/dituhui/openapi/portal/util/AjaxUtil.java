package com.dituhui.openapi.portal.util;

import javax.servlet.http.HttpServletRequest;

public class AjaxUtil {
    public static boolean isAjax(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestType)) {
            return true;
        } else {
            return false;
        }
    }
}
