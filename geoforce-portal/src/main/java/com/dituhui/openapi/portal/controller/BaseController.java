package com.dituhui.openapi.portal.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.dituhui.openapi.portal.common.Constant;

/**
 * Created by 9527 on 2017/11/13.
 */
public class BaseController {
    protected String error = "{\"status\": \"404\",\"info\": \"404\",\"data\":null}";

    protected Map<String, Object> buildResponseMap(boolean success, int status,
                                                   String info, Map<String, Object> result) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isSuccess", success);
        map.put("status", status);
        map.put("info", info);
        map.put("result", result);
        return map;
    }

    public List<String> writeInDisk(MultipartFile[] files, String disk) {
        List<String> suffixList = new ArrayList<String>();
        List<String> requestUrl = new ArrayList<>();
        suffixList.add(".jpg");
        suffixList.add(".JPG");
        suffixList.add(".png");
        suffixList.add(".PNG");
        suffixList.add(".BMP");
        suffixList.add(".bmp");
        suffixList.add(".JPEG");
        suffixList.add(".jpeg");
        suffixList.add(".GIF");
        suffixList.add(".gif");
        for (int i = 0; i < files.length; i++) {
            MultipartFile imageFile = files[i];
            if (null == imageFile) {
                continue;
            }
            String originName = imageFile.getOriginalFilename();
            //防止未上传文件
            if (null == originName || "".equals(originName)) {
                continue;
            }
            String suffix = originName.substring(originName.lastIndexOf("."));
            if (!suffixList.contains(suffix)) {
                continue;
            }
            String newName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String[] ym = sdf.format(new Date()).split("-");
            String basePath = Constant.BASE_PATH + "/" + disk + "/" + ym[0] + "/" + ym[1] + "/";
            File dirFile = new File(basePath);
            if (!dirFile.exists()) {
                try {
                    dirFile.mkdirs();
                } catch (Exception e) {
                    continue;
                }
            }
            String path = basePath + newName;
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    continue;
                }
            }
            try {
                imageFile.transferTo(file);
                requestUrl.add("resource/" + disk + "/" + ym[0] + "/" + ym[1] + "/" + newName);
            } catch (IOException e) {
                continue;
            }
        }
        return requestUrl;
    }

}
