package com.supermap.egispapi.action;

import com.supermap.egispapi.constants.CodeConstants;
import com.supermap.egispapi.constants.TemplateNames;
import com.supermap.egispapi.pojo.PathAnalysisParam;
import com.supermap.egispservice.base.entity.PointXY;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.pathanalysis.service.IPathAnalysisService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author CaoBin mailto:caobin@supermap.com
 * @version 1.0
 * @description 路径分析API
 * @company SuperMap Software Co., Ltd.
 * @createDate 2015/7/14
 */
@Controller
@RequestMapping("/pathanlysis")
public class PathAnalysisAction {

    private static Logger LOGGER = Logger.getLogger(PathAnalysisAction.class);
    private static final String CHARSET = ";charset=UTF-8";
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private IPathAnalysisService pathAnalysisService;
    /**
     * 路径分析服务
     */
    @RequestMapping(value="/findpath",produces= MediaType.APPLICATION_JSON_VALUE + CHARSET)
    @ResponseBody
    public Object findpath(@RequestParam(required=true)String param){
        ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);

        try {
            PathAnalysisParam pathAnalysisParam=objectMapper.readValue(param, PathAnalysisParam.class);

            if(null == pathAnalysisParam || pathAnalysisParam.getStartPoint()==null|| pathAnalysisParam.getEndPoint()==null){
                modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.PARAM_NOT_ALLOW_NULL);
                modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"需要起点、终点信息。");
                return modelAndView;
            }
            //模式类型
            WeightNameType weightNameType=WeightNameType.MileAndTruck;//距离最短
            if(pathAnalysisParam.getType()==2){//时间最短
                weightNameType=WeightNameType.TimeAndTruck;
            }


            Map<String,Object> m = pathAnalysisService.bestPathAnalyst(pathAnalysisParam.getStartPoint(), pathAnalysisParam.getEndPoint(), pathAnalysisParam.getTargetPoints(), true, weightNameType);
            if(pathAnalysisParam.getTextInfo()==0){
                m.remove("text");
            }
            if(pathAnalysisParam.getPathInfo()==0){
                m.remove("path");
            }
            modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, m);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
            modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
        }

        return modelAndView;
    }
    
    /**
     * 根据经纬度查找最近的一个垂直点
     * @param param
     * @return
     * @Author Juannyoh
     * 2016-1-7下午5:26:12
     */
    @RequestMapping(value="/findnearestpoint",produces= MediaType.APPLICATION_JSON_VALUE + CHARSET)
    @ResponseBody
    public Object findNearestPoint(@RequestParam(required=true)String param){
        ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
        try {
           PointXY pathAnalysisParam=objectMapper.readValue(param, PointXY.class);
            if(null == pathAnalysisParam || (pathAnalysisParam.getX()<=0||pathAnalysisParam.getX()>180)|| (pathAnalysisParam.getY()<=0||pathAnalysisParam.getY()>90)){
                modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.PARAM_NOT_ALLOW_NULL);
                modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"经纬度不符合规范值");
                return modelAndView;
            }
            Map<String,Object> m = pathAnalysisService.findNearestPoint(pathAnalysisParam);
            modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, m);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
            modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
        }
        return modelAndView;
    }

}

