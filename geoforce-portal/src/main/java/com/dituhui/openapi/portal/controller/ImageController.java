package com.dituhui.openapi.portal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dituhui.openapi.portal.common.Constant;

/**
 * 图片输出
 */
@RestController
public class ImageController {

    @RequestMapping(value = "imagescon/{img}.{suffix}")
    public void images(HttpServletResponse response, @PathVariable("img") String img, @PathVariable("suffix") String
            suffix) {
        try {
            File image = new File(Constant.BASE_PATH + "/" + img + "." + suffix);
            FileInputStream inputStream = new FileInputStream(image);
            int length = inputStream.available();
            byte data[] = new byte[length];
            response.setContentLength(length);
            response.setContentType("image/" + suffix);
            inputStream.read(data);
            OutputStream toClient = response.getOutputStream();
            toClient.write(data);
            toClient.flush();
        } catch (Exception r) {

        }
    }

    //格式 根目录 + 类型 + 年 + 月 + 文件名称
    @RequestMapping(value = "imagesAssets/{year}/{month}/{name}.{suffix}")
    public void images(HttpServletResponse response,
                       @PathVariable("year") String year,
                       @PathVariable("month") String month,
                       @PathVariable("name") String name,
                       @PathVariable("suffix") String suffix) {
        try {
            String path = year + "/" + month + "/" + name;
            File image = new File(Constant.BASE_PATH + "/" + path + "." + suffix);
            FileInputStream inputStream = new FileInputStream(image);
            int length = inputStream.available();
            byte data[] = new byte[length];
            response.setContentLength(length);
            response.setContentType("image/" + suffix);
            inputStream.read(data);
            OutputStream toClient = response.getOutputStream();
            toClient.write(data);
            toClient.flush();
        } catch (Exception r) {

        }
    }
}
