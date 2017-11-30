package com.dituhui.openapi.portal.common;

import java.util.Properties;

import com.dituhui.openapi.util.elasticsearch.AppPropertiesUtil;

public class Constant {

    public static final String SESSION_USER = "userInfo";

    private static Properties prop = AppPropertiesUtil.readPropertiesFile("config.properties", Constant.class);

    public static final String BASE_PATH = prop.get("image.base.path").toString();

    public static final String ALLOW_ORIGIN = prop.getProperty("allow.origin");

}
