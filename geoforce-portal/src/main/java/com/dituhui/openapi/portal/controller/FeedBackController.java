package com.dituhui.openapi.portal.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dituhui.openapi.base.service.IFeedbackService;
import com.dituhui.openapi.entity.Feedback;

@RestController
@RequestMapping("feedback")
public class FeedBackController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(FeedBackController.class);
    @Reference
    private IFeedbackService feedBackService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Map<String, Object> save(Feedback feedback, @RequestParam(value = "files", required = false)
            MultipartFile[] files) {
        try {
            List<String> imageList = writeInDisk(files, "feedback");
            feedback.setImageList(imageList);
            feedback.setCreateTime(new Date());
            feedBackService.save(feedback);
            return buildResponseMap(true, 200, null, null);
        } catch (Exception e) {
            LOGGER.info("错误信息\t", e);
        }
        return buildResponseMap(false, 500, "error", null);
    }


}
