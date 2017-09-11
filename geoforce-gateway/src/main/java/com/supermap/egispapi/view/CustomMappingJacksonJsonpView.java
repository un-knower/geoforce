package com.supermap.egispapi.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.supermap.egispapi.constants.TemplateNames;

/**
 * 
 * <p>Title: CustomMappingJacksonJsonpView</p>
 * Description:JSONP视图
 *
 * @author Huasong Huang
 * CreateTime: 2015-4-8 上午11:10:00
 */
public class CustomMappingJacksonJsonpView extends MappingJacksonJsonView {
	
	/** 
     * Default content type. Overridable as bean property. 
     */  
    public static final String DEFAULT_CONTENT_TYPE = "application/javascript";  
  
    @Override  
    public String getContentType() {  
        return DEFAULT_CONTENT_TYPE;  
    }  
  
    @SuppressWarnings("unchecked")
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {  
        if ("GET".equals(request.getMethod().toUpperCase())) {  
            Map<String, String[]> params = request.getParameterMap();  
            
            if (params.containsKey("callbacks")) {  
                response.getOutputStream().write(new String(params.get("callbacks")[0] + "(").getBytes());  
                super.render((Map<String, ?>)model.get(TemplateNames.FINAL_RESULT_NAME), request, response);  
                response.getOutputStream().write(new String(");").getBytes());  
                response.setContentType(DEFAULT_CONTENT_TYPE);  
            } else {  
                super.render(model, request, response);  
            }  
        } else {  
            super.render(model, request, response);  
        }  
    }  
	

}
