package com.chaosting.geoforce.saas.bak.filter;

import com.alibaba.dubbo.common.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 解压gzip请求包装类
 */
public class UnGzipRequestWrapper extends HttpServletRequestWrapper {
    public static final Logger LOGGER = LoggerFactory.getLogger(UnGzipRequestWrapper.class);
    private HttpServletRequest request;

    private Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
    ;

    public UnGzipRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String getParameter(String name) {

        if (null == paramsMap.get("parameterMap")) {
            paramsMap.putAll(request.getParameterMap());
            paramsMap.put("parameterMap", true);
        }
        String contentEncoding = request.getHeader("Content-Encoding");
        // 如果对内容进行了压缩，则解压
        try {
            if (null != contentEncoding && contentEncoding.indexOf("gzip") != -1 && null == paramsMap.get("gzip")) {
                paramsMap.put("gzip", true);
                GZIPInputStream gzipInputStream = new GZIPInputStream(request.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(gzipInputStream, "UTF-8"));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String splitMapFlag = "&";
                String spiltKeyValueFlag = "=";
                //把原有parameterMap添加到新map中
                String[] mapArr = sb.toString().split(splitMapFlag);
                String[] arr = null;
                for (String tagValue : mapArr) {
                    arr = tagValue.split(spiltKeyValueFlag, 2);
                    if (arr.length == 2) {
                        if (paramsMap.containsKey(arr[0]) && paramsMap.get(arr[0]) != null) {
                            String[] array = (String[]) paramsMap.get(arr[0]);
                            array = Arrays.copyOf(array, array.length + 1);
                            array[array.length - 1] = URL.decode(arr[1]);
                            paramsMap.put(arr[0], array);
                        } else {
                            paramsMap.put(arr[0], new String[]{URL.decode(arr[1])});
                        }
                    }
                }

            }
        } catch (IOException e) {
            LOGGER.info("gzip解压失败");
        }
        return null == paramsMap.get(name) ? null : ((String[]) paramsMap.get(name))[0];
    }

    @Override
    public String[] getParameterValues(String name) {

        return (String[]) paramsMap.get(name);
    }

}
