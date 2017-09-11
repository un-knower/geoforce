package com.supermap.egispweb.action.car;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.lbsp.provider.hibernate.lbsp.Car;
import com.supermap.lbsp.provider.hibernate.lbsp.CarDismsg;

//@Controller
public class CarDisMsgAction extends BaseAction{
	static Logger logger = Logger.getLogger(CarDisMsgAction.class.getName());
	
	/**
	 * 页面调整
	 * @return
	 */
	@RequestMapping(value="/com/supermap/toDisMsg")
	public String toDisMsg(){
		logger.info("toDisMsg");
		return "car/carDisMsg";
	}
	
	
	/**
	 * 消息发送
	 * @param request
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/sendMsg")
	@ResponseBody
	public HashMap<String, Object> sendMsg(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		HashMap<String,Object> m = new HashMap<String, Object>();
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String type = request.getParameter("type");
		String carId = request.getParameter("carIds");
		if(title == null || "".equals("title")){
			m.put("flag", "err_01");
			return m;
		}
		if(content == null || "".equals(content)){
			m.put("flag", "err_02");
			return m;
		}
		if(type == null || "".equals(type)){
			m.put("flag", "err_03");
			return m;
		}
		if(carId == null ||"".equals(carId)){
			m.put("flag", "err_04");
			return m;
		}
		String[] carIds = carId.split(",");
		for (int i = 0; i < carIds.length; i++) {
			
			Car car = carConsumer.getCar(carIds[i]);
			CarDismsg carMessage = new CarDismsg();
			carMessage.setContent(content);
			carMessage.setTitle(title);
			carMessage.setType(Short.valueOf(type));
			carMessage.setCarId(car.getId());
			carMessage.setStatus((short)1);
			carMessage.setSendDate(new Date());
			carMessage.setUserId(userEntity.getId());
			carMessage.setDeptId(userEntity.getDeptId().getId());
			carDisMsgConsumer.addCarMessage(carMessage);
			//调协议
			
		}
		
		m.put("flag", "ok");
		return m;
	}

}
