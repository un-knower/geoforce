package com.supermap.egispapi.action;

import com.supermap.egispapi.constants.CodeConstants;
import com.supermap.egispapi.constants.TemplateNames;
import com.supermap.egispapi.pojo.ReverseParam;
import com.supermap.egispservice.base.entity.AddressInfo;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.reverse.service.IGeoQueryService;
import com.supermap.egispservice.reverse.service.IReverseService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CaoBin mailto:caobin@supermap.com
 * @version 1.0
 * @description 反向地址解析API
 * @company SuperMap Software Co., Ltd.
 * @createDate 2015-9-7
 */
@Controller
@RequestMapping("/reverse")
public class ReverseAction {

    private static Logger log = Logger.getLogger(ReverseAction.class);
    private static final String CHARSET = ";charset=UTF-8";
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private IReverseService reverseService;
    
    @Autowired
    private IGeoQueryService geoQueryService;
    /**
     * 反向地址解析
     */
    @RequestMapping(value="/geocoder",produces= MediaType.APPLICATION_JSON_VALUE + CHARSET)
    @ResponseBody
    public Object geocoder(@RequestParam(required=true)String param){
        ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);

        try {
            ReverseParam reverseParam =objectMapper.readValue(param, ReverseParam.class);
            if(null == reverseParam || reverseParam.getPoints() == null || reverseParam.getPoints().size() <= 0){
            	throw new NullPointerException("points参数不允许为空");
            }
            List<AddressInfo> addressInfo = null;
            if(0 != reverseParam.getFrom()){
            	addressInfo = reverseService.pointToAddress(reverseParam.getPoints(),reverseParam.getType());
            }else{
            	addressInfo = geoQueryService.batchQueryNearestPOI(reverseParam.getPoints(), reverseParam.getType());
            }
            modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, addressInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
            modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
        }

        return modelAndView;
    }

}
