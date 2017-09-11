package com.supermap.egispapi.action;

import com.supermap.egispapi.constants.CodeConstants;
import com.supermap.egispapi.constants.TemplateNames;
import com.supermap.egispapi.pojo.PathPlanParam;
import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.pathplan.service.IPathPlanServiceAPI;
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
import java.util.Map;

/**
 * @author CaoBin mailto:caobin@supermap.com
 * @version 1.0
 * @description 路线规划API
 * @company SuperMap Software Co., Ltd.
 * @createDate 2015/7/7
 */
@Controller
@RequestMapping("/pathplan")
public class PathPlanAction {

    private static Logger LOGGER = Logger.getLogger(PathPlanAction.class);
    private static final String CHARSET = ";charset=UTF-8";
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private IPathPlanServiceAPI pathPlanServiceAPI;
    /**
     * 路线规划服务
     */
    @RequestMapping(value="/vrppath",produces= MediaType.APPLICATION_JSON_VALUE + CHARSET)
    @ResponseBody
    public Object vrpPath(@RequestParam(required=true)String param){
        ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);

        try {
            PathPlanParam pathPlanParam=objectMapper.readValue(param,PathPlanParam.class);
            if(null == pathPlanParam || pathPlanParam.getStartPoint()==null|| pathPlanParam.getTargetPoints()==null || pathPlanParam.getTargetPoints().size() <= 0){
                modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.PARAM_NOT_ALLOW_NULL);
                modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"需要起点、至少一个目标点。");
                return modelAndView;
            }
            //起点
            List<Point> netPoints = new ArrayList<Point>();
            netPoints.add(convertPoint(pathPlanParam.getStartPoint()));

            //途经点
            List<Point> orderPoints = convertPointList(pathPlanParam.getTargetPoints());

            //模式类型
            WeightNameType weightNameType=WeightNameType.MileAndTruck;//距离最短
            if(pathPlanParam.getType()==2){//时间最短
                weightNameType=WeightNameType.TimeAndTruck;
            }


            Map<String,Object> m = pathPlanServiceAPI.generateDataByVRPPathForAPI(netPoints, orderPoints, true, GroupType.NoneGroup, weightNameType);
            if(pathPlanParam.getTextInfo()==0){
                m.remove("pathGuides");
            }
            modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, m);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
            modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
        }

        return modelAndView;
    }

    private List<Point>  convertPointList(List<com.supermap.egispapi.pojo.Point> points){
        List<Point> newPoints=new ArrayList<Point>();
        for (int i = 0; i < points.size(); i++) {
            com.supermap.egispapi.pojo.Point point = points.get(i);
            newPoints.add(convertPoint(point));
        }
        return newPoints;
    }

    private Point convertPoint(com.supermap.egispapi.pojo.Point point){
        return new Point(point.getX(),point.getY());
    }
}
