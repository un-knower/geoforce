package com.supermap.egispapi.interceptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egispapi.constants.Config;
import com.supermap.egispapi.constants.TemplateNames;
import com.supermap.egispapi.pojo.DistributeParam;
import com.supermap.egispapi.service.IUserInfoCache;
import com.supermap.egispservice.base.entity.APIFendanEntity;
import com.supermap.egispservice.base.entity.APILogEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.pojo.DistributeAddress;
import com.supermap.egispservice.base.pojo.LogisticsAPIResult;
import com.supermap.egispservice.base.service.APIFendanService;
import com.supermap.egispservice.base.service.APILogService;
import com.supermap.entity.Point;

public class LogInterceptor implements HandlerInterceptor{

	private static final Logger STATISTICS_LOGGER = Logger.getLogger("statistics");
	private static final Logger LOGGER = Logger.getLogger(LogInterceptor.class);
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//分单日志--通过坐标分单
	private static final Logger STATISTICSXY_LOGGER = Logger.getLogger("statisticsXY");
	
	@Autowired
	private Config config;
	
	@Autowired
	private APILogService apiLogService;
	
	@Autowired
	private APIFendanService apiFendanService;
	
	@Autowired
	private IUserInfoCache userInfoCache;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object arg2, ModelAndView mav)
			throws Exception {
		String uri = req.getRequestURI();
		// 只对配置的URI才日志记录
		if(config.isNeedStatistics(uri)){
//			UserEntity ue = (UserEntity) req.getSession().getAttribute(req.getParameter("key"));
			UserEntity ue = (UserEntity) userInfoCache.findByKey(req.getParameter("key"));
			if(ue==null) return ;
			StringBuilder logBuilder = new StringBuilder();
			Long requestTime = (Long) req.getAttribute("requestTime");
			long currentTime = System.currentTimeMillis();
			logBuilder.append("uri=").append(uri).append("\r\n");
			logBuilder.append("key=").append(req.getParameter("key")).append("\r\n");
			logBuilder.append("eid=").append(ue.getEid().getId()).append("\r\n");
			logBuilder.append("deptId=").append(ue.getDeptId().getId()).append("\r\n");
			logBuilder.append("dcode=").append(ue.getDeptId().getCode()).append("\r\n");			
			logBuilder.append("param=").append(req.getParameter("param")).append("\r\n");
			Map<String,Object> map = mav.getModel();
			logBuilder.append("result=").append(map == null?null:JSONObject.fromObject(map).toString()).append("\r\n");
			logBuilder.append("completeTime=").append(SDF.format(new Date(currentTime))).append("\r\n");
			logBuilder.append("requestTime=").append(SDF.format(new Date(requestTime))).append("\r\n");
			logBuilder.append("consumeTime=").append(currentTime - requestTime).append("\r\n");
			logBuilder.append("------------------------------------------------------------------------------");
			STATISTICS_LOGGER.info(logBuilder.toString());
			
			//解析有返回结果的，写入到日志表中
			try {
				Map<String ,Object> result=new HashMap<String,Object>();
				if(map!=null&&map.get(TemplateNames.FINAL_RESULT_NAME)!=null){
					String key=req.getParameter("key");
					String eid=ue.getEid().getId();
					String deptid=ue.getDeptId().getId();
					String dcode=ue.getDeptId().getCode();
					String param=req.getParameter("param");
					Date completeTime=new Date(currentTime);
					Date requestTime_=new Date(requestTime);
					String consumeTime=(currentTime - requestTime)+"";
					Object obj=map.get(TemplateNames.FINAL_RESULT_NAME);
					Map<String,Object> objmap=(Map<String, Object>) obj;
					result=objmap;
					saveFendanLog(uri, key, eid, deptid,dcode, param, result,
							 completeTime, requestTime_, consumeTime);
				}
			} catch (Exception e) {
				return ;
			}
			//启动线程
			//String home=System.getProperty("catalina.home");
			//LogInterceptor.LOGReader logreader=new LogInterceptor().new LOGReader(home+"\\logs\\egispapi\\statistics\\statistics.log");
			//LOGReader logreader=new LOGReader(home+"\\logs\\egispapi\\statistics\\statistics.log");
			//Thread thread=new Thread(logreader);
			//thread.start();
		}else if(config.isNeedStatisticsXY(uri)){
			UserEntity ue = (UserEntity) userInfoCache.findByKey(req.getParameter("key"));
			if(ue==null) return ;
			StringBuilder logBuilder = new StringBuilder();
			Long requestTime = (Long) req.getAttribute("requestTime");
			long currentTime = System.currentTimeMillis();
			logBuilder.append("uri=").append(uri).append("\r\n");
			logBuilder.append("key=").append(req.getParameter("key")).append("\r\n");
			logBuilder.append("eid=").append(ue.getEid().getId()).append("\r\n");
			logBuilder.append("deptId=").append(ue.getDeptId().getId()).append("\r\n");
			logBuilder.append("dcode=").append(ue.getDeptId().getCode()).append("\r\n");			
			logBuilder.append("param=").append(req.getParameter("param")).append("\r\n");
			Map<String,Object> map = mav.getModel();
			logBuilder.append("result=").append(map == null?null:JSONObject.fromObject(map).toString()).append("\r\n");
			logBuilder.append("completeTime=").append(SDF.format(new Date(currentTime))).append("\r\n");
			logBuilder.append("requestTime=").append(SDF.format(new Date(requestTime))).append("\r\n");
			logBuilder.append("consumeTime=").append(currentTime - requestTime).append("\r\n");
			logBuilder.append("------------------------------------------------------------------------------");
			STATISTICSXY_LOGGER.info(logBuilder.toString());
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {
		return true;
	}
	
	
	/**
	 * 写分单日志表
	 * @param uri
	 * @param key
	 * @param eid
	 * @param deptid
	 * @param dcode
	 * @param param
	 * @param resultmap
	 * @param completeTime
	 * @param requestTime
	 * @param consumeTime
	 * @Author Juannyoh
	 * 2015-11-6下午1:42:27
	 */
	public void saveFendanLog(String uri,String key,String eid,String deptid,
			String dcode,String param,Map<String,Object> resultmap,
			Date completeTime,Date requestTime,String consumeTime){
		List<APIFendanEntity> fendanlist=new ArrayList<APIFendanEntity>();
		List<APILogEntity> loglist=new ArrayList<APILogEntity>();
		try {
			//写入日志表
			APILogEntity entity=new APILogEntity();
			entity.setUri(uri);
			entity.setUserid(key);
			entity.setDeptid(deptid);
			entity.setDcode(dcode);
			entity.setEid(eid);
			
			//解析参数
			DistributeParam dp= objectMapper.readValue(param, DistributeParam.class);
			//entity.setParam(param);
			List<DistributeAddress> addressparam = dp==null?null:dp.getAddresses();
			
			//坐标类型
			String coorType=dp.getType();
			
			
	 		int sumcount=addressparam==null?0:addressparam.size();//总的地址参数数据量
			
			//entity.setResult(resultmap == null?null:JSONObject.fromObject(resultmap).toString());
			entity.setCompleteTime(completeTime);
			entity.setRequestTime(requestTime);
			entity.setConsumeTime(consumeTime);
			
			int successcount=0;
			int failcount=0;
			
			if(resultmap!=null){
				List<LogisticsAPIResult> dr=(List<LogisticsAPIResult>) resultmap.get("results");
				if(dr!=null){
					for(int i=0;i<dr.size();i++){
						LogisticsAPIResult result=dr.get(i);
						DistributeAddress  address=addressparam.get(i);
						APIFendanEntity fendan=new APIFendanEntity();
						if(result.getResultType().equals("1")||result.getResultType().equals("2")){// 1 表示找到了坐标和区域，即分单成功//2 表示有坐标，无区划
							if(result.getResultType().equals("1")){
								successcount++;
							}
							double smx=result.getX();
							double smy=result.getY();
							Point p = new Point(smx,smy);
							// 如果需要返回的坐标类型为经纬度，则进行坐标转换。
							if(coorType != null && coorType.equalsIgnoreCase("SMLL")){
								p = SuperMapCoordinateConvertImpl.smLL2MC(p);
							}
							fendan.setSmx(new BigDecimal(p.getLon()));
							fendan.setSmy(new BigDecimal(p.getLat()));
							fendan.setResulttype(result.getResultType());
						}else{
							fendan.setResulttype("3");//无坐标
						}
						fendan.setOrderNum(address.getId());
						fendan.setAddress(address.getAddress());
						fendan.setEid(eid);
						fendan.setUserid(key);
						fendan.setDeptid(deptid);
						fendan.setDcode(dcode);
						fendan.setAreaid(result.getAreaName());
						fendan.setFendanTime(requestTime);
						fendanlist.add(fendan);
					}
					failcount=sumcount-successcount;
				}
			}
			entity.setSuccesscount(successcount);
			entity.setFailcount(failcount);
			entity.setSumcount(sumcount);
			loglist.add(entity);
			this.apiLogService.saveLogs(loglist);
			this.apiFendanService.saveFendanAPIs(fendanlist);
			
		}catch (Exception e) {
			LOGGER.info("保存分单API结果异常："+e.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	private class LOGReader implements Runnable {
		
		LOGReader(String filename) {
			this.filename = filename;
		}
		private String filename;
		private long filelength = 0;
		private int count = 0;
		

		public void run() {
			while (true) {
				try {
					File f = new File(filename);
					long nowlength = f.length();
					long readlength = nowlength - filelength;
					if (readlength == 0) {
						Thread.sleep(10000);
						continue;
					}
					RandomAccessFile rf = new RandomAccessFile(f, "r");
					// 移动文件指针到上次读的最后
					rf.seek(filelength);
					filelength = nowlength;
					byte[] b = new byte[(int) readlength];
					rf.read(b, 0, b.length);
					rf.close();
					BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(b)));
					String str = null;
					count++;
					System.out.println("第" + count + "次读到的内容:");
					String key=null;
					String uri=null;
					String eid=null;
					String deptid=null;
					String dcode=null;
					String param=null;
					String result=null;
					Map<String,Object> resultmodel=new HashMap<String,Object>();
					Map<String,Object> resultmap=new HashMap<String,Object>();
					String completeTime=null;
					String requestTime=null;
					String consumeTime=null;
					while ((str = br.readLine()) != null) {
						System.out.println(str);
						if(str.contains("uri=")){
							uri=str.substring(str.lastIndexOf("=")+1, str.length());
						}
						else if(str.contains("key=")){
							key=str.substring(str.lastIndexOf("=")+1, str.length());
						}
						else if(str.contains("eid=")){
							eid=str.substring(str.lastIndexOf("=")+1, str.length());
						}
						else if(str.contains("deptId=")){
							deptid=str.substring(str.lastIndexOf("=")+1, str.length());
						}
						else if(str.contains("dcode=")){
							dcode=str.substring(str.lastIndexOf("=")+1, str.length());
						}
						else if(str.contains("param=")){
							param=str.substring(str.lastIndexOf("=")+1, str.length());
						}
						else if(str.contains("result=")){
							result=str.substring(str.lastIndexOf("=")+1, str.length());
							JSONObject jb = JSONObject.fromObject(result);
							resultmodel = (Map<String,Object>)jb;
						}
						else if(str.contains("completeTime=")){
							completeTime=str.substring(str.lastIndexOf("=")+1, str.length());
						}
						else if(str.contains("requestTime=")){
							requestTime=str.substring(str.lastIndexOf("=")+1, str.length());
						}
						else if(str.contains("consumeTime=")){
							consumeTime=str.substring(str.lastIndexOf("=")+1, str.length());
						}
						if(str.contains("------")&&uri!=null){
							if(resultmodel!=null&&resultmodel.get(TemplateNames.FINAL_RESULT_NAME)!=null){
								resultmap=(Map<String, Object>) resultmodel.get(TemplateNames.FINAL_RESULT_NAME);
								saveFendanLog(uri, key, eid, deptid,dcode, param, resultmap,
										 SDF.parse(completeTime), SDF.parse(requestTime), consumeTime);
							}
						}
					}
					br.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Object[] getJsonToArray(String str) {
        JSONArray jsonArray = JSONArray.fromObject(str);
        return jsonArray.toArray();
    }
	

	

}
