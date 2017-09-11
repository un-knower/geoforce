package com.supermap.egispweb.action;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.supermap.egisp.addressmatch.beans.AddressMatchParam;
import com.supermap.egisp.addressmatch.beans.AddressMatchResult;
import com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.pojo.order.OrderBean;
import com.supermap.egispweb.util.ExcelUtil;

@Controller
@RequestMapping("addressMatch")
public class AddressMatchAction {
	
	@Autowired
	private IAddressMatchService addressMatchService;
	
	private Logger  LOGGER=Logger.getLogger(AddressMatchAction.class);
	
	@RequestMapping("show")
	public String getAddressMatch(){
		return "geocode";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("analyzeExcelAddresses")
	@ResponseBody
	public void analyzeExcelAddresses(MultipartFile myFile, HttpServletResponse response, HttpSession session)throws Exception {
		UserEntity user=(UserEntity)session.getAttribute("user");
		response.setContentType("text/plain;charset=UTF-8");
		Map<String, Object> resultMap = null;
		if (null == myFile) {
			resultMap = buildResult("文件为空", null, false);
		} else {
			String fileName = myFile.getOriginalFilename();
			InputStream inStream = null;
			try {
				if (null == user) {
					throw new Exception("未找到用户信息");
				}
				
				/*String userId = user.getId();
				String enterpriseId = user.getEid().getId();
				String departmentId = user.getDeptId().getId();
				String dcode=user.getDeptId().getCode();*/
				
				inStream = myFile.getInputStream();
				
				Map<String,Object> addressmap=ExcelUtil.readAddressExcel(inStream, fileName);
				List<OrderBean> orderlist=(List<OrderBean>) addressmap.get("orderlist");
				List<AddressMatchParam> addresslist=(List<AddressMatchParam>) addressmap.get("addresslist");
				
				List<AddressMatchResult> addressmatchResultList=addressMatchService.addressMatch(addresslist, "SMC");//地址解析
				if(addressmatchResultList!=null&&addressmatchResultList.size()>0){
					for(int i=0,s=addressmatchResultList.size();i<s;i++){
						DefaultAddressMatchResult matchResult=(DefaultAddressMatchResult) addressmatchResultList.get(i);
						OrderBean order=orderlist.get(i);
						order.setSmx(matchResult.getX());
						order.setSmy(matchResult.getY());
					}
				}
				resultMap = buildResult(null, orderlist, true);
			} catch (Exception e) {
				resultMap = buildResult(e.getMessage(), null, false);
				LOGGER.error(e.getMessage(), e);
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		String json = mapper.writeValueAsString(resultMap);
		PrintWriter out = response.getWriter();
		out.write(json);
		out.close();
	}
	
	
	private Map<String,Object> buildResult(String info,Object result,boolean isSuccess){
		Map<String,Object> resultObj = new HashMap<String,Object>();
		resultObj.put("info", info);
		resultObj.put("result", result);
		resultObj.put("isSuccess", isSuccess);
		return resultObj;
	}

}
