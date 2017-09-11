package com.supermap.egispweb.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.supermap.convert.impl.BaiduCoordinateConvertImpl;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egisp.addressmatch.beans.ReverseAddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.entity.PointExtcolEntity;
import com.supermap.egispservice.base.entity.PointExtcolValEntity;
import com.supermap.egispservice.base.entity.PointGroupEntity;
import com.supermap.egispservice.base.entity.PointPicEntity;
import com.supermap.egispservice.base.entity.PointStyleCustomEntity;
import com.supermap.egispservice.base.entity.PointStyleEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.pojo.ExportPointBean;
import com.supermap.egispservice.base.pojo.NetPointInfoResult;
import com.supermap.egispservice.base.service.PointExtcolService;
import com.supermap.egispservice.base.service.PointExtcolValService;
import com.supermap.egispservice.base.service.PointGroupService;
import com.supermap.egispservice.base.service.PointService;
import com.supermap.egispservice.base.service.PointStyleService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.egispservice.lbs.entity.DataWordbook;
import com.supermap.egispservice.lbs.service.CarService;
import com.supermap.egispservice.lbs.service.DataworkService;
import com.supermap.egispweb.common.Constant;
import com.supermap.egispweb.common.PointServiceConstants;
import com.supermap.egispweb.httpclient.DituhuiPointResult;
import com.supermap.egispweb.httpclient.HttpClientDituhui;
import com.supermap.egispweb.httpclient.PointAttributes;
import com.supermap.egispweb.pojo.netpoint.NetPointBean;
import com.supermap.egispweb.util.Config;
import com.supermap.egispweb.util.ExcelUtil;
import com.supermap.egispweb.util.FieldMap;
import com.supermap.egispweb.util.ImgCompressUtil;
import com.supermap.egispweb.util.ListSortUtil;
import com.supermap.egispweb.util.StringUtil;
import com.supermap.entity.Point;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Car;
//import com.supermap.lbsp.provider.hibernate.lbsp.DataWordbook;
import com.supermap.utils.FileUtil;

@Controller
@RequestMapping("pointService")
@SessionAttributes(types = { UserEntity.class }, value = { "user" })
public class PointServiceAction {
	
	private static final String NET_DIR_NAME = "net";
	private static final String DUTY_DIR_NAME = "duty";
	
	private static final String CUSTOMSTYLE_DIR_NAME = "style";//网点样式
	
	private static final String POINTPIC_DIR_NAME = "pointpic";//网点图片集
	
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");
	
	/*@Autowired
	protected CarConsumer carConsumer;*/
	@Autowired
	CarService carService;
	
	@Autowired
	protected DataworkService dataworkService;

	@Autowired
	protected Config config;
	
	@Autowired
	private IAreaService areaService;
	
	@Autowired
	private PointService pointService;
	
	@Autowired
	private PointExtcolService  pointExtcolService;
	
	@Autowired
	private PointExtcolValService  pointExtcolValService;
	
	@Autowired
	private PointGroupService  pointGroupService;
	
	@Autowired
	private PointStyleService  pointStyleService;
	
	@Autowired
	private IGeocodingService geocodingService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IAddressMatchService addressMatch;
	
	private static Logger LOGGER = Logger.getLogger(PointServiceAction.class);
	
	@RequestMapping(value="add")
	//@ResponseBody  Map<String, Object>
	public ResponseEntity add(MultipartFile netPicFile, MultipartFile dutyPicFile,
			@RequestParam(required = true) String name, @RequestParam(required = true) String smx,
			@RequestParam(required = true) String smy, String dutyName,
			String dutyPhone, String areaId,String iconStyle, @RequestParam(required = true) String address, HttpSession session,String carIds
			,String styleid,String stylename,String appearance,String appsize,String appcolor,
			String apppic,String customfileid,String groupid
			) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			BigDecimal smxD = BigDecimal.valueOf(Double.parseDouble(smx));
			BigDecimal smyD = BigDecimal.valueOf(Double.parseDouble(smy));
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String departmentId = user.getDeptId().getId();
			// 处理上传的网点照片
			String netPicPath = saveUploadFile(netPicFile, enterpriseId, NET_DIR_NAME);
			// 处理上传的负责人照片
			String dutyPicPath = saveUploadFile(dutyPicFile, enterpriseId, DUTY_DIR_NAME);
			
			//10.30增加逻辑，判断区划是否绑定过网点
			if(areaId!=null&&!areaId.equals("")){
				List<PointEntity> pointlist=this.pointService.queryByAreaId(areaId, enterpriseId, departmentId);
				if(pointlist!=null&&pointlist.size()>0){
					throw new Exception("该区划已绑定网点");
				}
			}
			
			//查询该用户id下的所有网点，判断是否超过10000条网点数据
			/*Map<String, Object> result = this.pointService.queryAllByPage(userId, null, null, null,enterpriseId, null, -1, 10, null);
			if(result!=null||result.get("records")!=null){
				List<NetPointInfoResult> existrecords=(List<NetPointInfoResult>) result.get("records");
				if(existrecords!=null&&existrecords.size()>=10000){
					throw new Exception("超过10000条数据，请联系商务");
				}
			}*/
			int existcount=this.pointService.getPointCountsByUserid(userId);
			if(existcount>=10000){
				throw new Exception("超过10000条数据，请联系商务");
			}
			
			
			String id = this.pointService.add(netPicPath,dutyPicPath,name, address, smxD, smyD, dutyName, dutyPhone, areaId, userId,
					enterpriseId, departmentId,iconStyle);
			boolean isSuccess = !StringUtil.isStringEmpty(id);
			
			//绑定样式
			//11.25增加 样式、分组数据
			if(isSuccess){
				/**
				 * 2016.6.2查询总账号的分组，子账号不允许增加修改或删除分组，分组、样式、字段都以总账号为准
				 */
				UserEntity topuser=this.userService.findTopUserByEid(enterpriseId);
				
				if((groupid==null||groupid.equals(""))&&(StringUtils.isNotEmpty(stylename)||StringUtils.isNotEmpty(appearance)
						||StringUtils.isNotEmpty(appsize)||StringUtils.isNotEmpty(appcolor)
						||StringUtils.isNotEmpty(apppic)||StringUtils.isNotEmpty(customfileid)
						)){//如果是单个网点样式
					PointStyleEntity style=new PointStyleEntity();
					//style.setStylename(stylename);
					style.setAppcolor(appcolor);
					style.setAppcustom(customfileid);
					style.setAppearance(appearance);
					style.setApppic(apppic);
					style.setAppsize(appsize);
					style.setDcode(topuser.getDeptId().getCode());
					style.setEid(enterpriseId);
					style.setUserid(topuser.getId());
					style.setCreat_time(new Date());
					if(StringUtils.isEmpty(stylename)){
						style.setDef1("1");
						stylename="默认样式-"+System.currentTimeMillis();
					}else {
						Map searmap=new HashMap();
						searmap.put("stylename", stylename);
						searmap.put("userid", userId);
						searmap.put("eid", enterpriseId);
						searmap.put("dcode", user.getDeptId().getCode());
						/*List<PointStyleEntity> exsitStyles=this.pointStyleService.findStyleByParam(searmap);
						if(exsitStyles!=null&&exsitStyles.size()>0){
							this.pointService.deletePoint(id);
							throw new Exception("样式名称已存在，请换一个试试");
						}*/
						searmap.remove("stylename");
						//List<PointStyleEntity> exsitStylesCount=this.pointStyleService.findStyleByParam(searmap);
						boolean falg=isCanSaveStyle(stylename,searmap);
						if(!falg){
							this.pointService.deletePoint(id);
							throw new Exception("最多只可创建10个样式，有更多需求请联系商务");
						}
						style.setDef1("0");
					}
					style.setStylename(stylename);
					PointEntity point=this.pointService.queryById(id);
					point.setStyleid(style);point.setGroupid(null);
					this.pointService.updatePoint(point);
				}
				if(groupid!=null&&!groupid.equals("")){//分组样式
					PointStyleEntity style=new PointStyleEntity();
					if(StringUtils.isNotEmpty(styleid)){//有样式id
						style=this.pointStyleService.findById(styleid);
						if(!style.getStylename().equals(stylename)){
							Map searmap=new HashMap();
							searmap.put("stylename", stylename);
							searmap.put("userid", userId);
							searmap.put("eid", enterpriseId);
							searmap.put("dcode", user.getDeptId().getCode());
							/*List<PointStyleEntity> exsitStyles=this.pointStyleService.findStyleByParam(searmap);
							if(exsitStyles!=null&&exsitStyles.size()>0){
								this.pointService.deletePoint(id);
								throw new Exception("样式名称已存在，请换一个试试");
							}*/
						}
					}else{//没有样式id
						Map searmap=new HashMap();
						searmap.put("stylename", stylename);
						searmap.put("userid", userId);
						searmap.put("eid", enterpriseId);
						searmap.put("dcode", user.getDeptId().getCode());
						/*List<PointStyleEntity> exsitStyles=this.pointStyleService.findStyleByParam(searmap);
						if(exsitStyles!=null&&exsitStyles.size()>0){
							this.pointService.deletePoint(id);
							throw new Exception("样式名称已存在，请换一个试试");
						}*/
						searmap.remove("stylename");
						/*List<PointStyleEntity> exsitStylesCount=this.pointStyleService.findStyleByParam(searmap);
						if(exsitStylesCount!=null&&exsitStylesCount.size()>=10){*/
						boolean falg=isCanSaveStyle(stylename,searmap);
						if(!falg){
							this.pointService.deletePoint(id);
							throw new Exception("最多只可创建10个样式，有更多需求请联系商务");
						}
					}
					style.setStylename(stylename);
					style.setAppcolor(appcolor);
					style.setAppcustom(customfileid);
					style.setAppearance(appearance);
					style.setApppic(apppic);
					style.setAppsize(appsize);
					style.setDcode(topuser.getDeptId().getCode());
					style.setEid(enterpriseId);
					style.setUserid(topuser.getId());
					style.setDef1("0");
					
					if(StringUtils.isNotEmpty(styleid)){
						this.pointStyleService.updatePointStyle(style);
						//解除之前的分组样式绑定关系
						PointGroupEntity group=this.pointGroupService.findByStyleid(styleid);
						if(group!=null&&!group.getId().equals("")){
							group.setStyleid(null);
							this.pointGroupService.updateGroup(group);
						}
					}else{
						style.setCreat_time(new Date());
						String styid=this.pointStyleService.addPointStyle(style);
						style.setId(styid);
					}
					PointGroupEntity group=this.pointGroupService.findByid(groupid);
					group.setStyleid(style);
					this.pointGroupService.updateGroup(group);
					
					PointEntity point=this.pointService.queryById(id);
					point.setGroupid(group);
					point.setStyleid(null);
					this.pointService.updatePoint(point);
				}
			}
			
			
			// 绑定车辆
			if(isSuccess && !StringUtils.isEmpty(carIds)){
				isSuccess = this.pointService.bindCar(id, carIds.split("_"));
				if(!isSuccess){
					this.pointService.deletePoint(id);
				}
			}
			resultObj = buildResult("添加网点" + (isSuccess ? "成功" : "[" + id + "]失败"), null, isSuccess);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    return new ResponseEntity(resultObj, headers, HttpStatus.OK);
		//return resultObj;
	}
	
	
	/**
	 * 
	 * <p>Title ：saveUploadFile</p>
	 * Description：		保存上传的文件
	 * @param multiFile
	 * @param enterpriseId
	 * @param dirName
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-27 上午10:06:33
	 * @throws IOException 
	 */
	private String saveUploadFile(MultipartFile multiFile,String enterpriseId,String dirName) throws IOException{
		String picPath = null;
		if (multiFile != null) {
			InputStream is = multiFile.getInputStream();
			if (null != is) {
				String picName = multiFile.getOriginalFilename();
				picName = randomFileName(picName);
				boolean isUploadSuccess = FileUtil.saveToFile(is, picName, PointServiceConstants.IMG_ROOT_PATH
						+ File.separator + enterpriseId + File.separator + dirName);
				if (isUploadSuccess) {
					picPath = enterpriseId + File.separator + dirName + File.separator + picName;
					LOGGER.info("## 上传图片成功["+picPath+"]");
				}
			}
		}
		return picPath;
	}
	
	
	/**
	 * 
	 * <p>Title ：randomFileName</p>
	 * Description：		对文件名进行随机化
	 * @param fileName
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-26 下午04:03:18
	 */
	private String randomFileName(String fileName){
		UUID uuid = UUID.randomUUID();
		return uuid+"_"+fileName;
	}
	
	
	
	@RequestMapping("delete")
	@ResponseBody
	public Map<String,Object> delete(@RequestParam(required=true)String id){
		Map<String,Object> map = null;
		try {
			boolean isDeleteSuccess = this.pointService.deletePoint(id);
			
			if(isDeleteSuccess){
				this.pointService.deletePointPictureByPointid(id);//如果网点删除了，对应删除该网点的图片集
			}
			map = buildResult("删除网点["+id+"]"+(isDeleteSuccess?"成功":"失败"), null, isDeleteSuccess);
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	@RequestMapping("update")
	//@ResponseBody Map<String, Object>
	public ResponseEntity update(MultipartFile netPicFile,MultipartFile dutyPicFile,@RequestParam(required = true) String id, String name, String address,
			String dutyName, String dutyPhone, String smx, String smy, String areaId,String iconStyle , HttpSession session,String carIds
			,String styleid,String stylename,String appearance,String appsize,String appcolor,
			String apppic,String customfileid,String groupid
			,String col1,String col2,String col3,String col4,String col5
			,String col6,String col7,String col8,String col9,String col10
			) {
		Map<String, Object> map = null;
		try {
			UserEntity user = (UserEntity) session.getAttribute("user");
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String departmentId = user.getDeptId().getId();
			
			PointEntity pe = new PointEntity();
			pe.setAreaId(areaId);
			pe.setId(id);
			if (!StringUtil.isStringEmpty(smx)) {
				BigDecimal smxD = BigDecimal.valueOf(Double.parseDouble(smx));
				pe.setSmx(smxD);
			}
			if (!StringUtil.isShort(smy)) {
				BigDecimal smyD = BigDecimal.valueOf(Double.parseDouble(smy));
				pe.setSmy(smyD);
			}
			pe.setAddress(address);
			pe.setName(name);
			pe.setDutyName(dutyName);
			pe.setDutyPhone(dutyPhone);
			pe.setIconStyle(iconStyle);
			
			// 处理上传的网点照片
			String netPicPath = saveUploadFile(netPicFile, enterpriseId, NET_DIR_NAME);
			// 处理上传的负责人照片
			String dutyPicPath = saveUploadFile(dutyPicFile, enterpriseId, DUTY_DIR_NAME);
			if(!StringUtils.isEmpty(netPicPath)){
				pe.setNetPicPath(netPicPath);
			}
			if(!StringUtils.isEmpty(dutyPicPath)){
				pe.setDutyPicPath(dutyPicPath);
			}
			
			
			//10.30增加逻辑，判断区划是否绑定过网点
			PointEntity oldpoint=this.pointService.queryById(id);
			if(areaId!=null&&!areaId.equals("")&&(oldpoint.getAreaId()==null||oldpoint.getAreaId().equals("")||!oldpoint.getAreaId().equals(areaId))){
				List<PointEntity> pointlist=this.pointService.queryByAreaId(areaId, enterpriseId, departmentId);
				if(pointlist!=null&&pointlist.size()>0){
					throw new Exception("该区划已绑定网点");
				}
			}
			pe.setGroupid(oldpoint.getGroupid());
			pe.setStyleid(oldpoint.getStyleid());
			
			boolean isSuccess = this.pointService.updatePoint(pe);
			
			
			//绑定样式
			//11.25增加 样式、分组数据
			if(isSuccess){
				/**
				 * 2016.6.2查询总账号的分组，子账号不允许增加修改或删除分组，分组、样式、字段都以总账号为准
				 */
				UserEntity topuser=this.userService.findTopUserByEid(enterpriseId);
				
				if((groupid==null||groupid.equals(""))&&(StringUtils.isNotEmpty(stylename)||StringUtils.isNotEmpty(appearance)
						||StringUtils.isNotEmpty(appsize)||StringUtils.isNotEmpty(appcolor)
						||StringUtils.isNotEmpty(apppic)||StringUtils.isNotEmpty(customfileid)
						)){//如果是单个网点样式
					PointStyleEntity style=new PointStyleEntity();
					if(StringUtils.isEmpty(stylename)){
						stylename="默认样式-"+System.currentTimeMillis();
					}
					if(StringUtils.isNotEmpty(styleid)){
						style=this.pointStyleService.findById(styleid);
						//解除之前的分组样式绑定关系
						PointGroupEntity group=this.pointGroupService.findByStyleid(styleid);
						if(group!=null&&!group.getId().equals("")){
							group.setStyleid(null);
							this.pointGroupService.updateGroup(group);
						}
					}
					style.setStylename(stylename);
					style.setAppcolor(appcolor);
					style.setAppcustom(customfileid);
					style.setAppearance(appearance);
					style.setApppic(apppic);
					style.setAppsize(appsize);
					style.setDcode(topuser.getDeptId().getCode());
					style.setEid(enterpriseId);
					style.setUserid(topuser.getId());
					style.setCreat_time(new Date());
					style.setDef1("1");
					PointEntity point=this.pointService.queryById(id);
					point.setStyleid(style);
					point.setGroupid(null);
					this.pointService.updatePoint(point);
				}
				if(groupid!=null&&!groupid.equals("")){//分组样式
					PointStyleEntity style=new PointStyleEntity();
					if(StringUtils.isNotEmpty(styleid)){
						style=this.pointStyleService.findById(styleid);
						if(!style.getStylename().equals(stylename)){
							Map searmap=new HashMap();
							searmap.put("stylename", stylename);
							searmap.put("userid", userId);
							searmap.put("eid", enterpriseId);
							searmap.put("dcode", user.getDeptId().getCode());
							/*List<PointStyleEntity> exsitStyles=this.pointStyleService.findStyleByParam(searmap);
							if(exsitStyles!=null&&exsitStyles.size()>0){
								throw new Exception("样式名称已存在，请换一个试试");
							}*/
						}
					}else{//没有样式id
						Map searmap=new HashMap();
						if(StringUtils.isNotEmpty(stylename)){
							searmap.put("stylename", stylename);
							searmap.put("userid", userId);
							searmap.put("eid", enterpriseId);
							searmap.put("dcode", user.getDeptId().getCode());
							/*List<PointStyleEntity> exsitStyles=this.pointStyleService.findStyleByParam(searmap);
							if(exsitStyles!=null&&exsitStyles.size()>0){
								throw new Exception("样式名称已存在，请换一个试试");
							}*/
							searmap.remove("stylename");
							/*List<PointStyleEntity> exsitStylesCount=this.pointStyleService.findStyleByParam(searmap);
							if(exsitStylesCount!=null&&exsitStylesCount.size()>=10){*/
							boolean falg=isCanSaveStyle(stylename,searmap);
							if(!falg){
								throw new Exception("最多只可创建10个样式，有更多需求请联系商务");
							}
						}
					}
					style.setStylename(stylename);
					style.setAppcolor(appcolor);
					style.setAppcustom(customfileid);
					style.setAppearance(appearance);
					style.setApppic(apppic);
					style.setAppsize(appsize);
					style.setDcode(topuser.getDeptId().getCode());
					style.setEid(enterpriseId);
					style.setUserid(topuser.getId());
					style.setDef1("0");
					
					//分组
					PointGroupEntity group=this.pointGroupService.findByid(groupid);
					
					if(StringUtils.isNotEmpty(styleid)){
						/*this.pointStyleService.updatePointStyle(style);
						//解除之前的分组样式绑定关系
						PointGroupEntity group=this.pointGroupService.findByStyleid(styleid);
						if(group!=null&&!group.getId().equals("")){
							group.setStyleid(null);
							this.pointGroupService.updateGroup(group);
						}*/
						//PointGroupEntity group=this.pointGroupService.findByid(groupid);
						if(group.getStyleid()!=null&&group.getStyleid().getId().equals(styleid)){
							this.pointStyleService.updatePointStyle(style);
						}
						
					}else{
						if(StringUtils.isNotEmpty(stylename)){
							style.setCreat_time(new Date());
							String styid=this.pointStyleService.addPointStyle(style);
							style.setId(styid);
							group.setStyleid(style);
						}
					}
					//PointGroupEntity group=this.pointGroupService.findByid(groupid);
					/*if(StringUtils.isNotEmpty(stylename)){
						group.setStyleid(style);
					}else {
						group.setStyleid(null);
					}*/
					this.pointGroupService.updateGroup(group);
					
					PointEntity point=this.pointService.queryById(id);
					point.setGroupid(group);
					point.setStyleid(null);
					this.pointService.updatePoint(point);
				}
			}
			
			
			if(isSuccess){
				String carIdss[] = null;
				if(StringUtils.isEmpty(carIds)){
					carIdss = new String[0];
				}else{
					carIdss = carIds.split("_");
					if(null == carIdss){
						carIdss = new String[0];
					}
				}
				this.pointService.bindCar(pe.getId(),carIdss);
				
				
				//更新自定义字段20160412
				PointExtcolValEntity valentity=new PointExtcolValEntity();
				List<PointExtcolValEntity> vallists=this.pointExtcolValService.findByPointidOrUserid(id, null);
				if(vallists!=null&&vallists.size()>0){
					valentity=vallists.get(0);
					valentity.setPointEntity(null);
					valentity.setPointid(id);
				}else{
					valentity.setPointid(id);
					valentity.setUserid(userId);
				}
				if(col1!=null){
					valentity.setCol1(col1);
				}
				if(col2!=null){
					valentity.setCol2(col2);
				}
				if(col3!=null){
					valentity.setCol3(col3);
				}
				if(col4!=null){
					valentity.setCol4(col4);
				}
				if(col5!=null){
					valentity.setCol5(col5);
				}
				if(col6!=null){
					valentity.setCol6(col6);
				}
				if(col7!=null){
					valentity.setCol7(col7);
				}
				if(col8!=null){
					valentity.setCol8(col8);
				}
				if(col9!=null){
					valentity.setCol9(col9);
				}
				if(col10!=null){
					valentity.setCol10(col10);
				}
				this.pointExtcolValService.save(valentity);
			}
			
			map = buildResult("更新网点[" + id + "]" + (isSuccess ? "成功" : "失败"), null, isSuccess);
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		//return map;
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    return new ResponseEntity(map, headers, HttpStatus.OK);
	}
	
	/**
	 * 1.增加admincode参数查询
	 * @param id
	 * @param areaId
	 * @param name
	 * @param dutyName
	 * @param pageNo
	 * @param pageSize
	 * @param admincode
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-12-8下午3:19:58
	 */
	@RequestMapping("queryAllPoint")
	@ResponseBody
	public Map<String, Object> query(String id, String areaId, String name, String groupid, String pageNo,
			String pageSize ,String  admincode,String level,
			HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String, Object> map = null;
		List<Map<String,Object>> maplist=null;
		FieldMap fieldmap=new FieldMap();
		try {
			if(user == null){
				throw new Exception("用户未登录");
			}
			int pageNoInt = 1;
			if (!StringUtil.isStringEmpty(pageNo)) {
				pageNoInt = Integer.parseInt(pageNo);
				if (pageNoInt <= 0) {
					pageNoInt = -1;
				}
			}
			int pageSizeInt = 10;
			if (!StringUtil.isStringEmpty(pageSize)) {
				pageSizeInt = Integer.parseInt(pageSize);
				if (pageSizeInt <= 0 || pageSizeInt > 20) {
					pageSizeInt = 10;
				}
			}
			// 如果提供了ID，则只查询指定结果
			if (!StringUtil.isStringEmpty(id)) {
				PointEntity pe = this.pointService.queryById(id);
				map = buildResult(null, pe, pe == null);
				return map;
			}
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			
			
			//如果admincode为空，则是查全国的
			Map<String, Object> result =null;
			
			if(StringUtils.isEmpty(admincode)){
				admincode=null;
				result=new HashMap<String,Object>();
				result = this.pointService.queryAllByAdmincode(userId, name, groupid,null,enterpriseId, dcode, pageNoInt, pageSizeInt, areaId,null);
			
				//查询省份的中心点坐标
				if(result!=null){
					List<Map<String,Object>> resultlist=new ArrayList<Map<String,Object>>();
					resultlist=(List<Map<String, Object>>) result.get("records");
					if(resultlist!=null&&resultlist.size()>0){
						List<Map<String,Object>> templist=new ArrayList<Map<String,Object>>();	
						Map<String,Object> tempmap=null;
						int nullcount=0;
						//for(Map<String,Object> m:resultlist){
						for(int i=0;i<resultlist.size();i++){
							Map<String,Object> m=resultlist.get(i);
							String codes=(m.get("admincode")==null||m.get("admincode").equals(""))?"":(String) m.get("admincode");
							if(codes!=null&&!codes.equals("")){
								Map<String,Object> geo=this.geocodingService.getAdminGeoByCode(codes,1);
								if(geo!=null){
									double x=(Double) geo.get("x");
									double y=(Double) geo.get("y");
									BigDecimal bx=new BigDecimal(x);
									BigDecimal by=new BigDecimal(y);
									x=bx.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
									y=by.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
									m.put("x", x);
									m.put("y", y);
								}else{
									m.put("x", null);
									m.put("y", null);
								}
								templist.add(m);
							}else{
								m.put("x", null);
								m.put("y", null);
								if(m.get("count")!=null&&!m.get("count").equals("")){
									nullcount+=Integer.parseInt(m.get("count").toString());
								}
								tempmap=new HashMap<String,Object>();
								tempmap=m;
							}
						}
						if(tempmap!=null){
							tempmap.put("count", nullcount);
							templist.add(tempmap);
						}
						result.put("records", templist);
					}
				}
			}else{
				if(level.equals("1")){
					if(admincode!=null&&!admincode.equals("")&&(
							admincode.indexOf("110")==0||admincode.indexOf("120")==0
							||admincode.indexOf("500")==0||admincode.indexOf("310")==0)){
						admincode=admincode.substring(0, 3);
					}
					else admincode=admincode.substring(0, 2);
				}else if(level.equals("2")){
					admincode=admincode.substring(0, 4);
				}else if(level.equals("3")){
					admincode=admincode;
				}
				result=new HashMap<String,Object>();
				result = this.pointService.queryAllByAdmincode(userId, name, groupid,null,enterpriseId, dcode, pageNoInt, pageSizeInt, areaId,admincode);
				//将自定义文件写入
				if(result!=null&&result.get("records")!=null){
					List<NetPointInfoResult> pointlist=(List<NetPointInfoResult>) result.get("records");
					maplist=new ArrayList<Map<String,Object>>();
					if(pointlist!=null&&pointlist.size()>0){
						for(NetPointInfoResult point:pointlist){
							Map<String,Object> pointmap=fieldmap.convertBean(point);
							
							/**
							 * 20160613查询每个网点的子账号名称 ,当前帐号不显示
							 */
							if(point.getUserId().equals(userId)){
								pointmap.put("username", null);
							}
							
							if(point.getStyleid()!=null){
								PointStyleEntity style=point.getStyleid();
								Map<String,Object> stylemap=fieldmap.convertBean(style);
								//查询自定义文件
								if(style!=null&&style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
									PointStyleCustomEntity custom=this.pointStyleService.findCustomfileByid(style.getAppcustom());
									if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
										stylemap.put("appcustom", custom.getFilepath());
										stylemap.put("def1", custom.getWidth()+","+custom.getHeight());
										stylemap.put("appcustomid", style.getAppcustom());
									}
								}else{
									stylemap.put("appcustom", "");
									stylemap.put("def1", "");
									stylemap.put("appcustomid", "");
								}
								pointmap.put("styleid", stylemap);
								maplist.add(pointmap);
							}else if(point.getGroupid()!=null&&point.getGroupid().getStyleid()!=null){
								Map<String,Object> groupmap=fieldmap.convertBean(point.getGroupid());
								
								PointStyleEntity style=point.getGroupid().getStyleid();
								Map<String,Object> stylemap=fieldmap.convertBean(style);
								//查询自定义文件
								if(style!=null&&style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
									PointStyleCustomEntity custom=this.pointStyleService.findCustomfileByid(style.getAppcustom());
									if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
										stylemap.put("appcustom", custom.getFilepath());
										stylemap.put("def1", custom.getWidth()+","+custom.getHeight());
										stylemap.put("appcustomid", style.getAppcustom());
									}
								}else{
									stylemap.put("appcustom", "");
									stylemap.put("def1", "");
									stylemap.put("appcustomid", "");
								}
								groupmap.put("styleid", stylemap);
								pointmap.put("groupid", groupmap);
								maplist.add(pointmap);
							}else {
								maplist.add(pointmap);
								continue;
							}
						}
					}
					result.put("records", maplist);
				}
				//session.setAttribute("pointlist", result);//将查询到的数据放到session中
			}
			if(result == null){
				return buildResult("无数据", null, true);
			}
			
			map = buildResult(null, result, true);
			
		} catch (Exception e) {
			map = buildResult(e.getMessage(), new String[]{}, true);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	@RequestMapping("bindCars")
	@ResponseBody
	public Map<String,Object> bindCars(@RequestParam(required=true)String id,@RequestParam(required=true)String carIds){
		Map<String,Object> map = null;
		try {
			boolean isSuccess = this.pointService.bindCar(id, carIds.split("_"));
			map = buildResult("绑定车辆"+(isSuccess?"成功":"失败"), null, isSuccess);
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	private Map<String,Object> buildResult(String info,Object result,boolean isSuccess){
		Map<String,Object> resultObj = new HashMap<String,Object>();
		resultObj.put("info", info);
		resultObj.put("result", result);
		resultObj.put("isSuccess", isSuccess);
		return resultObj;
	}
	
	/**
	 * 
	 * <p>Title ：queryCar</p>
	 * Description：		查询车辆信息
	 * @param request
	 * @param response
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-13 下午06:41:36
	 */
	@RequestMapping("queryCars")
	@ResponseBody
	public Map<String, Object> queryCar(HttpServletRequest request, HttpServletResponse response, String lisence,
			String sim, String type, HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		//Page page = getPage(request);
		HashMap<String, Object> param = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(lisence)) {
			param.put("license", lisence);
		}
		if (!StringUtils.isEmpty(sim)) {
			param.put("SIM", sim);
		}
		if (!StringUtils.isEmpty(type)) {
			param.put("type", type);
		}
		Map<String, Object> result = null;
		try {
			if (null != user) {
				InfoDeptEntity deptEntity = user.getDeptId();
				if (null != deptEntity) {
					param.put("deptcode", user.getDeptId().getCode());
					param.put("deptId", user.getDeptId().getId());
				} else {
					throw new Exception("未找到部门信息");
				}
			} else {
				throw new Exception("用户信息为空");
			}
			//Page resultPage = this.carConsumer.queryCarPage(page, param);
			param=getPageMap(request,param);
			Map resultPage=this.carService.queryCarPage(param);
			result = buildResult(null, resultPage, true);
		} catch (Exception e) {
			result = buildResult(e.getMessage(), null, false);
		}

		return result;
	}
	
	/**
	 * 得到page对象
	 * @param request
	 * @return
	 */
	protected Page getPage(HttpServletRequest request) {
		Page page = new Page();
		
		String size = request.getParameter("size");
		if(StringUtils.isBlank(size) || !StringUtil.isInteger(size)){
		}else {
			int pageSize = Integer.parseInt(size);
			if(pageSize>Constant.MAX_PAGE_SIZE){
				page.setPageSize(Constant.MAX_PAGE_SIZE);//每页最多50条记录
			}else if(pageSize <= 0){
				page.setPageSize(1);
			}else {
				page.setPageSize(pageSize);
			}
		}
		String curPage = request.getParameter("curPage");
		if (StringUtils.isBlank(curPage) || !StringUtil.isInteger(curPage)) {
		}else{
			int currentPage = Integer.parseInt(curPage);
			if(currentPage <= 0)
				currentPage = 1;
			page.setCurrentPageNum(currentPage);
		}
		return page;
	}
	
	protected HashMap getPageMap(HttpServletRequest request,HashMap m) {
		String size = request.getParameter("size");
		if(StringUtils.isBlank(size) || !StringUtil.isInteger(size)){
			m.put("pageSize", 10);
		}else {
			int pageSize = Integer.parseInt(size);
			if(pageSize>Constant.MAX_PAGE_SIZE){
				m.put("pageSize", Constant.MAX_PAGE_SIZE);//每页最多50条记录
			}else if(pageSize <= 0){
				m.put("pageSize", 1);
			}else {
				m.put("pageSize", pageSize);
			}
		}
		String curPage = request.getParameter("curPage");
		if (StringUtils.isBlank(curPage) || !StringUtil.isInteger(curPage)) {
		}else{
			int currentPage = Integer.parseInt(curPage);
			if(currentPage <= 0)
				currentPage = -1;
			m.put("pageNumber", currentPage);
		}
		return m;
	}
	
	
	@RequestMapping("queryCarTypes")
	@ResponseBody
	public Map<String,Object> queryCarType(String type,String status,String code){
		HashMap<String,Object> param = new HashMap<String,Object>();
		if(!StringUtils.isEmpty(type)){
			param.put("type", type);
		}
		if(!StringUtils.isEmpty(status)){
			param.put("status", status);
		}
		if(!StringUtils.isEmpty(code)){
			param.put("code", code);
		}
		Map<String,Object> resultMap = null;
		try {
			List<DataWordbook> result = this.dataworkService.getDataWordbookList(param);
			if(null != result && result.size() > 0){
				resultMap = buildResult(null, result, true);
			}else{
				resultMap = buildResult("未查询到任何内容", null, false);
			}
		} catch (Exception e) {
			resultMap = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * 
	 * <p>Title ：queryBindCars</p>
	 * Description：		根据网点ID查询绑定的车辆
	 * @param id
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-21 上午11:35:55
	 */
	@RequestMapping("queryBindCars")
	@ResponseBody
	public Map<String,Object> queryBindCars(@RequestParam(required=true)String id){
		Map<String,Object> resultMap = null;
		try {
			if(StringUtils.isEmpty(id)){
				throw new Exception("网点id不允许为空");
			}
			List<String> carIds = this.pointService.queryBindCar(id);
			LOGGER.info("## 查询到车辆信息："+((carIds==null||carIds.size()<=0)?"null":carIds.size()));
			if(null != carIds && carIds.size() > 0){
				//List<Car> cars = this.carConsumer.queryCar(carIds);
				List<Car> cars = this.carService.queryCar(carIds);
				resultMap = buildResult(null, cars, true);
			}else{
				resultMap = buildResult(null,null,true);
			}
		} catch (Exception e) {
			resultMap = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultMap;
	}
	
	
	@RequestMapping("getImg")
	@ResponseBody
	public void getImg(@RequestParam(required=true)String path,HttpServletResponse response){
		try {
			File f = new File(PointServiceConstants.IMG_ROOT_PATH + File.separator+path);
			LOGGER.info("## 查询图片["+path+"]");
			if(f.exists() && f.isFile()){
				FileInputStream is = new FileInputStream(f);
				byte[] buff = new byte[1024];
				OutputStream os = response.getOutputStream();
				int size = 0;
				while((size = is.read(buff)) > 0){
					os.write(buff,0,size);
				}
				os.flush();
				os.close();
				is.close();
			}else{
				throw new Exception(path+"不存在或指定的路径不是文件");
			}
		} catch (Exception e) {
			try {
				response.getWriter().write(e.getMessage());
				LOGGER.error(e.getMessage(), e);
			} catch (IOException e1) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		
		
	}
	
	@RequestMapping("import")
	@ResponseBody
	public void importNet(MultipartFile myFile, HttpServletResponse response, HttpSession session)
			throws Exception {
		UserEntity user=(UserEntity)session.getAttribute("user");
		response.setContentType("text/plain;charset=UTF-8");
		Map<String, Object> resultMap = null;
		if (null == myFile) {
			resultMap = buildResult("文件为空", null, false);
		} else {
			String userId = null;
			String enterpriseId = null;
			String departmentId = null;
			String dcode=null;

			String fileName = myFile.getOriginalFilename();
			InputStream inStream = null;
			try {
				if (null != user) {
					userId = user.getId();
					enterpriseId = user.getEid().getId();
					departmentId = user.getDeptId().getId();
					dcode=user.getDeptId().getCode();
				} else {
					throw new Exception("未找到用户信息");
				}
				inStream = myFile.getInputStream();
				
				/**
				 * 2016.9.26查询总账号
				 */
				UserEntity topuser=this.userService.findTopUserByEid(user.getEid().getId());
				if(topuser==null){
					throw new Exception("无总账号");
				}
				
				//查询用户已有的分组
				List<PointGroupEntity> grouplist=new ArrayList<PointGroupEntity>();
				grouplist=this.pointGroupService.queryAllGroups(null, null, topuser.getId(), topuser.getEid().getId(), topuser.getDeptId().getCode());
				
				LOGGER.info("解析excel开始时间："+sdf.format(new Date()));
				Map<String,Object> excelmap=ExcelUtil.readPointExcel(inStream, fileName,topuser.getId(),userId,this.pointExtcolService,config,grouplist);
				LOGGER.info("解析excel结束时间："+sdf.format(new Date()));
				String excelerrorMsg=excelmap.get("errorMsg").toString();
				
				//分组名称先写入数据库
				if(topuser.getId().equals(userId)){//当前帐号是总账号
					List<String> groupnamelist=(List<String>) excelmap.get("groupnamelist");
					List<PointGroupEntity> newgrouplist=null;
					if(groupnamelist!=null&&groupnamelist.size()>0){
						newgrouplist=new ArrayList<PointGroupEntity>();
						for(String name:groupnamelist){
							List<PointGroupEntity> list=this.pointGroupService.queryAllGroups(null, name, userId, enterpriseId, dcode);
							if(list!=null&&list.size()>0){
								continue;
							}else{
								PointGroupEntity group=new PointGroupEntity();
								group.setGroupname(name);
								group.setCreatTime(new Date());
								group.setDcode(dcode);
								group.setEid(enterpriseId);
								group.setUserid(userId);
								group.setStyleid(null);
								newgrouplist.add(group);
							}
						}
					}
					if(newgrouplist!=null&&newgrouplist.size()>0){
						this.pointGroupService.addPointGroups(newgrouplist);
					}
				}
				
				//
				
				LOGGER.info("网点转换开始时间："+sdf.format(new Date()));
				List<NetPointBean> netPoints = (List<NetPointBean>)excelmap.get("result");
				List<PointEntity> pointEntitys = buildPointEntitys(netPoints, userId, enterpriseId, departmentId,dcode,topuser);
				LOGGER.info("网点转换结束时间："+sdf.format(new Date()));
				if (null != pointEntitys) {
					/**
					 * 7.15 增加逻辑  juannyoh
					 * 网点每次导入不超过一千条
					 * 每个用户总网点数不超过5000条
					 */
					List<String> ids =new ArrayList<String>();
					String errorMsg="";
					
					Map<String,Object> importresultmap=new HashMap<String,Object>();
					
					if(pointEntitys.size()>0&&pointEntitys.size()<=1000){
						//查询该用户id下的所有网点
						Map<String, Object> result = this.pointService.queryAllByPage(userId, null, null, null,
								enterpriseId, null, -1, 10, null);
						if(result==null||result.get("records")==null){ //如果用户之前没传过数据。result为空，则直接写入
							LOGGER.info("写入数据库开始时间："+sdf.format(new Date()));
							importresultmap=this.pointService.importNetPoints(pointEntitys);
							ids = (List<String>) importresultmap.get("ids");//如果总数<=5000才写入数据库
							LOGGER.info("写入数据库结束时间："+sdf.format(new Date()));
						}
						else{ //如果用户之前传过数据，则判断总网点数有没有超过5000
							List<NetPointInfoResult> existrecords=(List<NetPointInfoResult>) result.get("records");
							int total=0;
							if(existrecords!=null&&existrecords.size()>0){
								total=pointEntitys.size()+existrecords.size(); //上传的网点数+已有的网点数
							}
							if(total<=10000){
								LOGGER.info("写入数据库开始时间："+sdf.format(new Date()));
								importresultmap=this.pointService.importNetPoints(pointEntitys);
								ids = (List<String>) importresultmap.get("ids");//如果总数<=5000才写入数据库
								LOGGER.info("写入数据库结束时间："+sdf.format(new Date()));
							}
							else{
								errorMsg="超过10000条数据，请联系商务";
							}
						}
					}
					else{
						errorMsg="每次导入不超过1000条，请检查";
					}
					List<String> namelist=new ArrayList<String>();
					String names="";
					if(importresultmap!=null){
						namelist=(List<String>) importresultmap.get("namelist");
						if(namelist!=null&&namelist.size()>0){
							names="网点名称重复："+namelist.toString();
						}
					}
					if(null != ids && ids.size() > 0){
						//this.pointService.startProcess(ids);
						excelerrorMsg+=names;
						session.setAttribute("processPoints", ids);
						resultMap = buildResult(excelerrorMsg, null, true);
					}else{
						errorMsg=errorMsg+"<br>"+names;
						resultMap = buildResult(errorMsg, null, false);
					}
				} else {
					if(excelerrorMsg!=null&&!excelerrorMsg.equals("")){
						throw new Exception("导入网点失败."+"<br>"+excelerrorMsg);
					}else{
						throw new Exception("网点数据为空，请检查数据及模版.");
					}
				}
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
	
	/**
	 * 
	 * <p>Title ：buildPointEntitys</p>
	 * Description：		构建批量导入的网点
	 * @param netPoints
	 * @param userId
	 * @param enterpriseId
	 * @param departmentId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-23 下午04:25:18
	 */
	private List<PointEntity> buildPointEntitys(List<NetPointBean> netPoints, String userId, String enterpriseId,
			String departmentId,String dcode,UserEntity topuser) {
		List<PointEntity> pointEntitys = null;
		Date createTime = new Date();
		if(null != netPoints && netPoints.size() > 0){
			pointEntitys = new ArrayList<PointEntity>();
			for(NetPointBean npb : netPoints){
				PointEntity pe = new PointEntity();
				if(StringUtils.isEmpty(npb.getAddress())){
					pe.setStatus(2);
					pe.setAddress(null);
				}else{
					pe.setAddress(npb.getAddress());
				}
				pe.setName(npb.getName());
				pe.setUserId(userId);
				pe.setEnterpriseId(enterpriseId);
				pe.setDepartmentId(departmentId);
				if(npb.getX() > 0 && npb.getY() > 0 && (npb.getX() < 180 && npb.getY() < 90)){
					pe.setSmx(BigDecimal.valueOf(npb.getX()));
					pe.setSmy(BigDecimal.valueOf(npb.getY()));
				}else{
					pe.setStatus(2);
				}
				pe.setDutyName(npb.getManager());
				pe.setDutyPhone(npb.getPhone());
				pe.setCreateTime(createTime);
				pe.setPointExtcolValEntity(npb.getPointExtcolValEntity());
				
				PointGroupEntity groupentity=npb.getGroupid();
				if(groupentity!=null){
					String groupname=groupentity.getGroupname();
					//查找在分组表中有没有这个分组
					List<PointGroupEntity> grouplist = this.pointGroupService.queryAllGroups(null, groupname, topuser.getId(), topuser.getEid().getId(), topuser.getDeptId().getCode());
					if(grouplist!=null&&grouplist.size()>0){
						PointGroupEntity pg=grouplist.get(0);
						pe.setGroupid(pg);
					}else{
						if(topuser.getId().equals(userId)){//如果不存在，且是总账号，则添加
							groupentity.setUserid(userId);
							groupentity.setEid(enterpriseId);
							groupentity.setDcode(dcode);
							groupentity.setCreatTime(new Date());
							pe.setGroupid(groupentity);
						}else pe.setGroupid(null);
					}
				}
				pointEntitys.add(pe);
			}
		}
		return pointEntitys;
	}
	
	/**
	 * 
	 * <p>Title ：queryForLine</p>
	 * Description：		查询某个区域下面的网点，网点名称可选
	 * @param areaId
	 * @param name
	 * @param page
	 * @param rows
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-31 下午02:44:57
	 */
	@RequestMapping("queryForLine")
	@ResponseBody
	public Map<String, Object> queryForLine(@RequestParam(required = true) String areaId, String name, String page,
			String rows, HttpSession session) {
		//,HttpSession session
		UserEntity user = (UserEntity) session.getAttribute("user");
		int pageNoInt = 1;
		if (!StringUtil.isStringEmpty(page)) {
			pageNoInt = Integer.parseInt(page);
			if (pageNoInt <= 0) {
				pageNoInt = 1;
			}
		}
		int pageSizeInt = 10;
		if (!StringUtil.isStringEmpty(rows)) {
			pageSizeInt = Integer.parseInt(rows);
			if (pageSizeInt <= 0 || pageSizeInt > 20) {
				pageSizeInt = 10;
			}
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String departmentId = user.getDeptId().getId();
			map = this.pointService.queryByAreaIdOrName(areaId, name, userId, enterpriseId, departmentId, pageNoInt, pageSizeInt);
		} catch (Exception e) {
			map = new HashMap<String,Object>();
			map.put("total", 0);
			map.put("page", 0);
			map.put("records", 0);
			map.put("rows", null);
			LOGGER.error(e.getMessage(), e);
		}
		
		return map;
	}
	
	
	/**
	 * 根据网点名称查询网点信息
	 */
	@RequestMapping("queryByName")
	@ResponseBody
	public Map<String, Object> queryByName( String netName, String rows, String page,HttpSession session) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			int pageNo = -1;
			if(!StringUtils.isEmpty(page)){
				pageNo = Integer.parseInt(page);
			}
			int pageSize = 10;
			if(pageNo != -1 && !StringUtils.isEmpty(rows)){
				pageSize = Integer.parseInt(rows);
				if(pageSize > 50 || pageSize < 1){
					pageSize = 10;
				}
			}
			LOGGER.info("## 查询网点[netName:"+netName+",page:"+pageNo+",pageSize:"+rows);
			UserEntity user = (UserEntity) session.getAttribute("user");
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			Map<String,Object> result = this.pointService.queryAllByPage(userId, netName, null, null, enterpriseId, dcode, pageNo, pageSize, null);
			if(null != result){
				Long totalCount = 0l;
				Integer pageCount = 0;
				if(-1 == pageNo){
					List<NetPointInfoResult> records = (List<NetPointInfoResult>) result.get("records");
					totalCount = new Long(records.size());
					pageCount = 1;
					pageNo = 1;
				}else{
					pageCount = (Integer) result.get("totalPages");
					totalCount =   (Long) result.get("totalCount");
				}
				resultMap.put("total",pageCount );
				resultMap.put("page", pageNo);
				resultMap.put("records",totalCount );
				resultMap.put("rows", result.get("records"));
			}else{
				resultMap.put("total", 0);
				resultMap.put("page", 0);
				resultMap.put("records", 0);
				resultMap.put("rows", null);
			}
		} catch (Exception e) {
			resultMap.put("total", 0);
			resultMap.put("page", 0);
			resultMap.put("records", 0);
			resultMap.put("rows", null);
			LOGGER.error(e.getMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * 
	 * <p>Title ：queryAreaByNetId</p>
	 * Description：		根据网点ID查询绑定的区域面
	 * @param netId
	 * @param session
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-23 上午10:01:39
	 */
	@RequestMapping("queryAreaByNetId")
	@ResponseBody
	public Object queryAreaByNetId(@RequestParam(required=true)String netId,HttpSession session){
		AreaEntity areaEntity = null;
		try {
			if(StringUtils.isEmpty(netId)){
				throw new Exception("netId is not allowed be empty!");
			}
			UserEntity user = (UserEntity) session.getAttribute("user");
			String dcode = user.getDeptId().getCode();
			PointEntity pointEntity = pointService.queryById(netId);
			 
			 if(null != pointEntity && !StringUtils.isEmpty(pointEntity.getAreaId())){
				  areaEntity = this.areaService.queryByIdOrNumber(pointEntity.getAreaId(), null, dcode, true);
			 }
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return "{\"errorInfo\":\""+e.getMessage()+"\"}";
		}
		return areaEntity==null?"{}":areaEntity;
	}
	
	@RequestMapping("queryCar")
	@ResponseBody
	public Map<String,Object> queryBindCars(@RequestParam(required=true)String netId,String carPlate,String page,String rows){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			if(StringUtils.isEmpty(netId)){
				throw new Exception("netId is not allowed be empty!");
			}
			int pageNo = -1;
			if(!StringUtils.isEmpty(page)){
				pageNo = Integer.parseInt(page);
				
			}
			int pageSize = 10;
			if(pageNo != -1 && !StringUtils.isEmpty(rows)){
				pageSize = Integer.parseInt(rows);
				if(pageSize > 50 || pageSize < 1){
					pageSize = 10;
				}
			}
			List<String> bindCarIds = this.pointService.queryBindCar(netId);
			List<String> subCarIds = null;
			List<Car> queryCars = new ArrayList<Car>();
			int totalCount = 0;
			int totalPage = 0;
			int tempPageSize = pageSize;
			if(null != bindCarIds && bindCarIds.size() > 0){
				LOGGER.info("## 查询到绑定车辆数量["+bindCarIds.size()+"]");
				if(pageNo >= 0){
					if(pageSize > bindCarIds.size()){
						pageSize = bindCarIds.size();
					}
					int indexEnd =  (pageNo - 1)*pageSize + pageSize;
					if(indexEnd > bindCarIds.size()){
						indexEnd = bindCarIds.size();
					}
					subCarIds = bindCarIds.subList((pageNo - 1) * pageSize, indexEnd);
				}else{
					subCarIds = bindCarIds;
				}
				if(null != subCarIds && subCarIds.size() > 0){
					//List<Car> cars = this.carConsumer.queryCar(subCarIds);
					List<Car> cars = this.carService.queryCar(subCarIds);
					if(null != cars && cars.size() > 0){
						if(!StringUtils.isEmpty(carPlate)){
							for(Car car : cars){
								String license = car.getLicense();
								if(license.contains(carPlate)){
									queryCars.add(car);
								}
							}
						}else{
							queryCars = cars;
						}
					}
				}
			}
			
			totalCount = bindCarIds.size();
			if(pageNo > -1){
				totalPage = totalCount / tempPageSize + 1;
				resultMap.put("total", totalPage);
				resultMap.put("page", pageNo);
				resultMap.put("records", totalCount);
				resultMap.put("rows", queryCars);
			}else{
				resultMap.put("total", 1);
				resultMap.put("page", 1);
				resultMap.put("records", totalCount);
				resultMap.put("rows", queryCars);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap.put("total", 0);
			resultMap.put("page", 0);
			resultMap.put("records", 0);
			resultMap.put("rows", null);
		}
		return resultMap;
	}
	
	
	/**
	 * 添加自定义字段
	 * @param defaultcoldesc
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-8-18下午2:02:47
	 */
	@RequestMapping("addPointExtcol")
	@ResponseBody
	public Map<String,Object> addPointExtcol(@RequestParam(required = true) String defaultcoldesc,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			PointExtcolEntity pointextcolentity =new PointExtcolEntity();
			pointextcolentity.setUserid(userId);
			pointextcolentity.setDefaultcol(defaultcoldesc);
			String id = this.pointExtcolService.addPointExtcol(pointextcolentity);
			boolean isSuccess = !StringUtil.isStringEmpty(id);
			
			//查出刚刚新增加的那一列
			PointExtcolEntity record=this.pointExtcolService.findByUserid(userId);
			String key="";
			Field[] fields = record.getClass().getDeclaredFields();
	        for(Field field:fields){
	        	field.setAccessible(true);
	        	if((field.get(record).equals(defaultcoldesc))&&!field.getName().equals("defaultcol")){
						key=field.getName();
						break;
				}
	        }
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("colkey", key);
			map.put("colvalue", defaultcoldesc);
			resultObj = buildResult("添加自定义字段" + (isSuccess ? "成功" : "[" + id + "]失败"), map, isSuccess);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 修改自定义字段描述
	 * 删除自定义字段，此时 coldesc传“”
	 * @param coldesc
	 * @param cols
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-8-18下午3:55:11
	 */
	@RequestMapping("updateOrdeletePointExtcol")
	@ResponseBody
	public Map<String,Object> updatePointExtcol(@RequestParam(required = true) String coldesc,@RequestParam(required = true) String cols,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			PointExtcolEntity pointextcolentity =new PointExtcolEntity();
			pointextcolentity.setUserid(userId);
			pointextcolentity.setDefaultcol(coldesc);
			LOGGER.info("删除自定义字段开始时间："+new Date());
			String id = this.pointExtcolService.updatePointExtcol(cols, coldesc, userId);
			LOGGER.info("删除自定义字段结束时间："+new Date());
			boolean isSuccess = !StringUtil.isStringEmpty(id);
			resultObj = buildResult("更新自定义字段" + (isSuccess ? "成功" : "[" + id + "]失败"), null, isSuccess);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 添加自定义字段的值
	 * @param colvalues
	 * @param cols
	 * @param pointid
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-8-19下午3:27:57
	 */
	@RequestMapping("addOrUpdatePointExtcolVal")
	@ResponseBody
	public Map<String,Object> addOrUpdatePointExtcolVal(@RequestParam(required = true) String colvalues,@RequestParam(required = true) String cols,@RequestParam(required = true) String pointid,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String id = this.pointExtcolValService.addPointExtcolVal(cols, colvalues, pointid, userId);
			boolean isSuccess = !StringUtil.isStringEmpty(id);
			resultObj = buildResult("修改字段值" + (isSuccess ? "成功" : "[" + id + "]失败"), null, isSuccess);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 用户自定义字段
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-8-20下午2:40:28
	 */
	@RequestMapping("getUserPointExtcol")
	@ResponseBody
	public Map<String,Object>  getUserPointExtcol(HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try{
			if (null == user) {
				throw new Exception("用户未登录");
			}
			
			/**
			 * 2016.6.2查询总账号的分组，子账号不允许增加修改或删除分组，分组、样式、字段都以总账号为准
			 */
			UserEntity topuser=this.userService.findTopUserByEid(user.getEid().getId());
			if(topuser==null){
				return buildResult("无总账号" , null, false);
			}
			
			PointExtcolEntity record=this.pointExtcolService.findByUserid(topuser.getId());//根据用户id查找到
			resultObj=new FieldMap().ChangeObjectToMap(record);//将查询的表头存在map中，用field、fieldvalue对应
			if(resultObj!=null){
				Map<String, Object> map=new LinkedHashMap<String,Object>();
				//查顺序
				String colorder=record.getColorder();
				if(colorder!=null){
					String colorders[]=colorder.split("!,");
					for(String s:colorders){
						for (Map.Entry<String, Object> entry : resultObj.entrySet()) {
							if(!s.equals("")&&s.equals(entry.getValue())){
								map.put(entry.getKey(), entry.getValue());
							}
						}
					}
				}
				else{
					for (Map.Entry<String, Object> entry : resultObj.entrySet()) {
							map.put(entry.getKey(), entry.getValue());
					}
				}
				map.put("configcols", record.getConfigcols());//配置哪些字段显示与否
				resultObj = buildResult("用户自定义字段：", map, true);
			}
			else {
				resultObj=new HashMap<String,Object>();
				resultObj.put("configcols", record==null?null:record.getConfigcols());
				resultObj = buildResult("用户自定义字段：", resultObj, true);
			}
		}catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	
	/**
	 *  判断colsname有没有与系统已有字段重复
	 * @param colsname
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-24下午5:25:15
	 */
	public boolean isColsnameRepeat(String colsname,String userid){
		boolean result=false;
		Map<String, Object> extcolheader = null;
		PointExtcolEntity record=this.pointExtcolService.findByUserid(userid);//根据用户id查找到
		extcolheader=new FieldMap().ChangeObjectToMap(record);//将查询的表头存在map中，用field、fieldvalue对应
		String baseheader=config.getHeaderBase();
		Iterator extcolit = extcolheader.values().iterator();
		String baseheaders[]=baseheader.split(",");//基础字段
		while(extcolit.hasNext()){
			String extcolname=(String) extcolit.next();
			if(colsname.equals(extcolname)){
				result=true;
				break;
			}
			else{
				for(String s:baseheaders){
					if(colsname.equals(s)){
						result=true;
						extcolit=null;
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 网点按单元格修改
	 * @param id
	 * @param cellkey
	 * @param cellvalue
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-8-26下午2:54:25
	 */
	@RequestMapping("updatePointByCell")
	@ResponseBody
	public Map<String,Object> updatePointByCell(@RequestParam(required=true)String id,@RequestParam(required=true)String cellkey,@RequestParam(required=true)String cellvalue,HttpSession session){
		Map<String,Object> resultObj = null;
		UserEntity user = (UserEntity) session.getAttribute("user");
		String baseEn[]=config.getHeaderBaseEn().split(",");//英文
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userid=user.getId();
			String index=ExcelUtil.isValueInArray(cellkey, baseEn);//判断是否在基础字段中
			PointEntity point=this.pointService.queryById(id);
			//PointExtcolValEntity  pointExtcolEntity=point.getPointExtcolValEntity();
			boolean base=false;
			String result="";
			//如果是基础字段则修改基础表
			if(index!=null&&!index.equals("")){
				if(cellkey.equals("groupid")){//如果是网点分组字段
					if(cellvalue!=null&&!cellvalue.equals("")){
						PointGroupEntity group=this.pointGroupService.findByid(cellvalue);//根据id查找分组
						point.setGroupid(group);
					}else{
						point.setGroupid(null);
					}
					point.setStyleid(null);
				}else{
					Field field=point.getClass().getDeclaredField(cellkey);
					field.setAccessible(true);
					field.set(point, cellvalue);
				}
				base=this.pointService.updatePoint(point);
			}
			else{
				/*Field field=pointExtcolEntity.getClass().getDeclaredField(cellkey);
				field.setAccessible(true);
				field.set(pointExtcolEntity, cellvalue);*/
				result=this.pointExtcolValService.addPointExtcolVal(cellkey, cellvalue, id, userid);
			}
			if(base==false&&result.equals("")){
				resultObj = buildResult("修改失败", null, false);
			}
			else{
				resultObj = buildResult("修改成功", null, true);
			}
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	
	/**
	 * 根据区域编号 （对应网点自定义字段）查询网点名称
	 * @param areaNumber
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-9-29上午9:01:52
	 */
	@RequestMapping("getPointnameByAreaNumber")
	@ResponseBody
	public Map<String,Object> getPointnameByAreaNumber(@RequestParam(required=true)String areanumber,String id,HttpSession session){
		Map<String,Object> resultObj = null;
		Map<String,Object> map = new HashMap<String,Object>();
		UserEntity user = (UserEntity) session.getAttribute("user");
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userid=user.getId();
			String coldesc="网点编号";//区划编号与网点编号对应    查出网点名称
			String cols=null;//自定义列id
			
			//根据用户id查找到自定义字段
			PointExtcolEntity record=this.pointExtcolService.findByUserid(userid);
			Field[] fields=PointExtcolEntity.class.getDeclaredFields();//获取所有字段
			if(record!=null){
				for(Field f:fields){
					f.setAccessible(true);
					if(f.get(record)!=null&&(f.get(record).toString().trim()).equals(coldesc)){
						cols=f.getName();
						break;
					}
				}
				
				List<PointExtcolValEntity> valentitys=this.pointExtcolValService.findByPointidOrUserid(null, userid);
				String pointid=null;
				Field f=null;
				if(cols!=null){
					f=PointExtcolValEntity.class.getDeclaredField(cols);//获取值字段
					f.setAccessible(true);
				}
				if(valentitys!=null&&f!=null&&valentitys.size()>0){
					for(PointExtcolValEntity valentity:valentitys){
						if(f.get(valentity)!=null&&(f.get(valentity).toString().trim()).equals(areanumber)){
							pointid=valentity.getPointid();
							break;
						}
					}
				}
				if(pointid!=null){
					PointEntity point=this.pointService.queryById(pointid);//根据网点id找到网点数据
					if(point!=null&&(point.getAreaId()==null||point.getAreaId().equals("")||point.getAreaId().equals(id))){
						map.put("pointname", point.getName());
						map.put("pointid", point.getId());
						
						//将该区划已绑定的网点名称查出来
						if(id!=null&&!id.equals("")&&(point.getAreaId()==null||point.getAreaId().equals("")||!point.getAreaId().equals(id))){
							List<PointEntity> pointlist=this.pointService.queryByAreaId(id, user.getEid().getId(), user.getDeptId().getId());
							List<String> namelists=new ArrayList<String>();
							if(pointlist!=null&&pointlist.size()>0){
								for(PointEntity entity:pointlist){
									namelists.add(entity.getName());
								}
							}
							map.put("oldpointnames", namelists);
						}
						else {
							map.put("oldpointnames", "");
						}
						
						resultObj = buildResult("已默认为网点名称", map, true);
					}
					else if(point!=null&&point.getAreaId()!=null&&!point.getAreaId().equals("")){
						resultObj = buildResult("编号已被占用，请检查", null, false);
					}
					else{
						resultObj = buildResult("编号不存在，请验证", null, false);
					}
				}
				else{//没找到对应编号的网点
					resultObj = buildResult("编号不存在，请验证", null, false);
				}
			}
			else{//用户都没有自定义字段
				resultObj = buildResult("编号不存在，请验证", null, false);
			}
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	
	/**
	 * 网点按字段排序
	 * @param sortField
	 * @param sortMode
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-2上午11:10:47
	 */
	/*@RequestMapping("pointListSort")
	@ResponseBody
	public Map<String,Object> pointListSort(@RequestParam(required=true)String sortField,@RequestParam(required=true)String sortMode,
			HttpSession session){
		Map<String,Object> map = null;
		try {
			Map<String,Object> sessionmap=(Map<String, Object>) session.getAttribute("pointlist");//通过session获取数据
			List<NetPointInfoResult> records=new ArrayList<NetPointInfoResult>();
			if(sessionmap!=null){
				records=(List<NetPointInfoResult>) sessionmap.get("records");
				//排序
				new ListSortUtil().sortList(records, sortField, sortMode);
				//
			}
			map = buildResult(null, sessionmap, true);
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}*/
	
	/**
	 * 添加分组
	 * @param groupname
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-11下午3:17:43
	 */
	@RequestMapping("addPointGroup")
	@ResponseBody
	public Map<String,Object> addPointGroup(@RequestParam(required = true) String groupname,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String eid=user.getEid().getId();
			String dcode=user.getDeptId().getCode();
			
			List<PointGroupEntity> groupnamelist=this.pointGroupService.queryAllGroups(null, groupname, userId, eid, dcode);
			if(groupnamelist!=null&&groupnamelist.size()>0){
				throw new Exception("分组名称已存在，请换一个试试");
			}
			
			List<PointGroupEntity> groupuserlist=this.pointGroupService.queryAllGroups(null, null, userId, eid, dcode);
			if(groupuserlist!=null&&groupuserlist.size()>=10){
				throw new Exception("最多只可创建10个分组，有更多需求请联系商务");
			}
			
			String id = this.pointGroupService.addGroup(groupname, userId, eid, dcode);
			
			boolean isSuccess = !StringUtil.isStringEmpty(id);
			resultObj = buildResult("添加分组" + (isSuccess ? "成功" : "[" + id + "]失败"), null, isSuccess);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 修改分组
	 * @param groupname
	 * @param groupid
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-11下午3:23:14
	 */
	@RequestMapping("updatePointGroup")
	@ResponseBody
	public Map<String,Object> updatePointGroup(@RequestParam(required = true) String groupname,
			@RequestParam(required = true) String groupid,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String eid=user.getEid().getId();
			String dcode=user.getDeptId().getCode();
			
			PointGroupEntity groupentity=this.pointGroupService.findByid(groupid);
			groupentity.setGroupname(groupname);
			groupentity.setUserid(userId);
			groupentity.setEid(eid);
			groupentity.setDcode(dcode);
			
			String id = this.pointGroupService.updateGroup(groupentity);
			
			boolean isSuccess = !StringUtil.isStringEmpty(id);
			resultObj = buildResult("修改分组" + (isSuccess ? "成功" : "[" + id + "]失败"), null, isSuccess);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 删除分组
	 * @param groupid
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-11下午3:25:37
	 */
	@RequestMapping("deletePointGroup")
	@ResponseBody
	public Map<String,Object> deletePointGroup(@RequestParam(required = true) String groupid,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			PointGroupEntity group=this.pointGroupService.findByid(groupid);
			String styleid=group.getStyleid()==null?null:group.getStyleid().getId();
			this.pointGroupService.deleteGroup(groupid);
			//同时删除样式
			if(StringUtils.isNotEmpty(styleid)){
				this.pointStyleService.deletePointStyle(styleid);
			}
			resultObj = buildResult("删除分组" + "成功" , null, true);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 查询所有分组
	 * @param groupid
	 * @param groupname
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-11下午3:34:55
	 */
	@RequestMapping("getAllPointGroup")
	@ResponseBody
	public Map<String,Object> getAllPointGroup(String groupid,String groupname,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		List<Map<String,Object>> maplist=null;
		FieldMap fieldmap=new FieldMap();
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			//String userid=user.getId();
			String eid=user.getEid().getId();
			//String dcode=user.getDeptId().getCode();
			
			/**
			 * 2016.6.2查询总账号的分组，子账号不允许增加修改或删除分组，分组、样式、字段都以总账号为准
			 */
			UserEntity topuser=this.userService.findTopUserByEid(eid);
			if(topuser==null){
				return buildResult("无总账号" , null, false);
			}
			
			List<PointGroupEntity> grouplist = this.pointGroupService.queryAllGroups(groupid, groupname, topuser.getId(), topuser.getEid().getId(), null);
			if(grouplist==null||grouplist.size()==0){
				//throw new Exception("查询分组失败");
				resultObj = buildResult("无分组" , null, false);
				return resultObj;
			}else{
				maplist=new ArrayList<Map<String,Object>>();
				for(PointGroupEntity group:grouplist){
					Map<String,Object> map=fieldmap.convertBean(group);
					if(group.getStyleid()!=null){
						PointStyleEntity style=group.getStyleid();
						Map<String,Object> stylemap=fieldmap.convertBean(style);
						//查询自定义文件
						if(style!=null&&style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
							PointStyleCustomEntity custom=this.pointStyleService.findCustomfileByid(style.getAppcustom());
							if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
								//style.setAppcustom(custom.getFilepath());
								//style.setDef1(custom.getWidth()+","+custom.getHeight());
								stylemap.put("appcustom", custom.getFilepath());
								stylemap.put("def1", custom.getWidth()+","+custom.getHeight());
								stylemap.put("appcustomid", style.getAppcustom());
							}
						}else{
							stylemap.put("appcustom", "");
							stylemap.put("def1", "");
							stylemap.put("appcustomid", "");
						}
						map.put("styleid", stylemap);
					}
					maplist.add(map);
				}
			}
			resultObj = buildResult("查询分组成功" , maplist, true);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 添加样式
	 * @param stylename
	 * @param appearance
	 * @param appsize
	 * @param appcolor
	 * @param apppic
	 * @param customFile
	 * @param groupid
	 * @param pointid
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-12上午8:54:02
	 */
	@RequestMapping("addPointStyle")
	@ResponseBody
	public Map<String,Object> addPointStyle(@RequestParam(required = true) String stylename,
			String appearance,String appsize,String appcolor,String apppic,String customfileid,
			String groupid,String pointid,HttpSession session
			,String allflag){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String eid=user.getEid().getId();
			String dcode=user.getDeptId().getCode();
			
			boolean selfflag=false;//标志是子集样式，还是普通样式
			
			if((stylename==null||stylename.equals(""))&&pointid!=null&&!pointid.equals("")&&(groupid==null||groupid.equals(""))){
				selfflag=true;
				stylename="默认样式-"+System.currentTimeMillis();
			}
			
			/**
			 * 判断名称是否重复 
			 * 如果是单个网点的样式（pointid不为空），则不用判断重复
			 */
			if(!selfflag){
				Map searmap=new HashMap();
				searmap.put("stylename", stylename);
				searmap.put("userid", userId);
				searmap.put("eid", eid);
				searmap.put("dcode", dcode);
				/*List<PointStyleEntity> exsitStyles=this.pointStyleService.findStyleByParam(searmap);
				if(exsitStyles!=null&&exsitStyles.size()>0){
					throw new Exception("样式名称已存在，请换一个试试");
				}*/
				
				searmap.remove("stylename");
				/*List<PointStyleEntity> exsitStylesCount=this.pointStyleService.findStyleByParam(searmap);
				if(exsitStylesCount!=null&&exsitStylesCount.size()>=10){*/
				boolean falg=isCanSaveStyle(stylename,searmap);
				if(stylename!=null&&!stylename.equals("默认样式")&&!falg){
					throw new Exception("最多只可创建10个样式，有更多需求请联系商务");
				}
			}
			PointStyleEntity styleentity=new PointStyleEntity();
			styleentity.setStylename(stylename);
			styleentity.setAppearance(appearance);
			styleentity.setAppsize(appsize);
			styleentity.setAppcolor(appcolor);
			styleentity.setApppic(apppic);
			styleentity.setAppcustom(customfileid);
			styleentity.setUserid(userId);
			styleentity.setEid(eid);
			styleentity.setDcode(dcode);
			styleentity.setCreat_time(new Date());
			if(!selfflag){
				styleentity.setDef1("0");
			}else styleentity.setDef1("1");
			
			String id =this.pointStyleService.addPointStyle(styleentity);
			styleentity.setId(id);
			/**
			 * 如果有分组名称，同时关联分组与样式关系
			 */
			if(groupid!=null&&!groupid.equals("")){
				PointGroupEntity groupentity=new PointGroupEntity();
				groupentity=this.pointGroupService.findByid(groupid);
				groupentity.setStyleid(styleentity);
				this.pointGroupService.updateGroup(groupentity);
			}
			/**
			 * 如果传了网点id，
			 * 1、有分组：同时更新网点与分组的关系
			 * 2、无分组：则相当于是单个网点样式，此时需关联网点与样式关系
			 */
			if(pointid!=null&&!pointid.equals("")){
				PointEntity point=new PointEntity();
				point=this.pointService.queryById(pointid);
				if(groupid!=null&&!groupid.equals("")){
					PointGroupEntity groupentity=new PointGroupEntity();
					groupentity=this.pointGroupService.findByid(groupid);
					point.setGroupid(groupentity);
					this.pointService.updatePoint(point);
				}else{
					point.setStyleid(styleentity);
					this.pointService.updatePoint(point);
				}
			}
			
			boolean isSuccess = !StringUtil.isStringEmpty(id);
			
			if(isSuccess){
				//如果是修改默认样式，则需要判断allflag 当allfalg为1的时候，修改该用户下所有没有分组的网点样式，否则就不需要修改
				if(StringUtils.isNotEmpty(styleentity.getStylename())&&styleentity.getStylename().equals("默认样式")){
					if(allflag.equals("1")){
						this.pointService.updatePointDefaultStyle(userId);//解绑网点与子集关系
						this.pointStyleService.deleteUnuseStyles(userId);//删除用户无用子集
						
					}
				}
			}
			
			resultObj = buildResult("添加样式" + (isSuccess ? "成功" : "[" + id + "]失败"), null, isSuccess);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 修改样式
	 * @param styleid
	 * @param stylename
	 * @param appearance
	 * @param appsize
	 * @param appcolor
	 * @param apppic
	 * @param customFile
	 * @param groupid
	 * @param pointid
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-12上午8:55:21
	 */
	@RequestMapping("updatePointStyle")
	@ResponseBody
	public Map<String,Object> updatePointStyle(@RequestParam(required = true) String styleid,String stylename,
			String appearance,String appsize,String appcolor,String apppic,String customfileid,
			String groupid,String pointid,HttpSession session,String allflag//allfalg 为1 表示修改无分组的所有网点样式，为0则不管
			){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String eid=user.getEid().getId();
			String dcode=user.getDeptId().getCode();
			
			String id=null;
			PointStyleEntity styleentity=new PointStyleEntity();
			
			boolean selfflag=false;//标志是子集样式，还是普通样式
			
			if((stylename==null||stylename.equals(""))&&pointid!=null&&!pointid.equals("")&&(groupid==null||groupid.equals(""))){
				selfflag=true;
				stylename="默认样式-"+System.currentTimeMillis();
			}
			
			styleentity.setAppcustom(customfileid);
			styleentity.setStylename(stylename);
			styleentity.setAppearance(appearance);
			styleentity.setAppsize(appsize);
			styleentity.setAppcolor(appcolor);
			styleentity.setApppic(apppic);
			styleentity.setUserid(userId);
			styleentity.setEid(eid);
			styleentity.setDcode(dcode);
			styleentity.setCreat_time(new Date());
			/**
			 * 判断名称是否重复 
			 * 如果是单个网点的样式（pointid不为空），则不用判断重复
			 */
			if(pointid==null||pointid.equals("")){//样式列表中样式修改
				if(styleid!=null&&!styleid.equals("")){
					PointStyleEntity oldstyle=this.pointStyleService.findById(styleid);//通过styleid查找到以前的样式
					String oldstylename=oldstyle.getStylename();//以前的样式名称
					oldstylename=oldstylename==null?"":oldstylename;
					if(!oldstylename.equals(stylename)){
						Map searmap=new HashMap();
						searmap.put("stylename", stylename);
						searmap.put("userid", userId);
						searmap.put("eid", eid);
						searmap.put("dcode", dcode);
						/*List<PointStyleEntity> exsitStyles=this.pointStyleService.findStyleByParam(searmap);
						if(exsitStyles!=null&&exsitStyles.size()>0){
							throw new Exception("样式名称已存在，请换一个试试");
						}*/
					}
					styleentity.setId(styleid);
					styleentity.setDef1("0");
					
					id = this.pointStyleService.updatePointStyle(styleentity);//修改样式
					
					//同时解绑以前的分组与该样式的关联关系
					PointGroupEntity oldgroup=this.pointGroupService.findByStyleid(styleid);
					if(oldgroup!=null){
						oldgroup.setStyleid(null);
						this.pointGroupService.updateGroup(oldgroup);
					}
					/**
					 * 如果有分组名称，同时关联分组与样式关系
					 */
					if(groupid!=null&&!groupid.equals("")){
						//绑定新的网点分组与样式关系
						PointGroupEntity groupentity=new PointGroupEntity();
						groupentity=this.pointGroupService.findByid(groupid);
						groupentity.setStyleid(styleentity);
						this.pointGroupService.updateGroup(groupentity);
						//同时解除网点与样式关系，绑定网点与分组的关系
						/*List<PointEntity> pointlist=this.pointService.findByStyleid(styleid);
						List<String> ids=null;
						if(pointlist!=null&&pointlist.size()>0){
							ids=new ArrayList<String>();
							for(PointEntity p:pointlist){
								ids.add(p.getId());
								p.setGroupid(groupentity);
								p.setStyleid(null);
								this.pointService.updatePoint(p);
							}
						}*/
					}
				}
				else{//样式列表中,默认样式
					styleentity.setDef1("0");
					styleentity.setStylename("默认样式");
					id=this.pointStyleService.addPointStyle(styleentity);
				}
				
			}else{//网点中样式修改
				if(styleid==null||styleid.equals("")){
					if(!selfflag){
						styleentity.setDef1("0");
					}else{
						styleentity.setDef1("1");
					}
					id = this.pointStyleService.addPointStyle(styleentity);//添加样式
					styleentity.setId(id);
					/**
					 * 如果有分组名称，同时关联分组与样式关系
					 */
					if(groupid!=null&&!groupid.equals("")){
						//绑定新的网点分组与样式
						PointGroupEntity groupentity=new PointGroupEntity();
						groupentity=this.pointGroupService.findByid(groupid);
						groupentity.setStyleid(styleentity);
						this.pointGroupService.updateGroup(groupentity);
						
						//修改网点与分组的关系
						PointEntity point=this.pointService.queryById(pointid);
						point.setGroupid(groupentity);
						this.pointService.updatePoint(point);
					}else{
						//修改网点与样式的关系
						PointEntity point=this.pointService.queryById(pointid);
						point.setGroupid(null);
						point.setStyleid(styleentity);
						this.pointService.updatePoint(point);
					}
				}else{
					styleentity.setId(styleid);
					if(groupid!=null&&!groupid.equals("")){
						styleentity.setDef1("0");
						PointStyleEntity oldstyle=this.pointStyleService.findById(styleid);//通过styleid查找到以前的样式
						String oldstylename=oldstyle.getStylename();//以前的样式名称
						oldstylename=oldstylename==null?"":oldstylename;
						Map searmap=new HashMap();
						searmap.put("userid", userId);
						searmap.put("eid", eid);
						searmap.put("dcode", dcode);
						/*List<PointStyleEntity> exsitStylesCount=this.pointStyleService.findStyleByParam(searmap);
						if(exsitStylesCount!=null&&exsitStylesCount.size()>=10){*/
						boolean falg=isCanSaveStyle(stylename,searmap);
						if(!falg){
							throw new Exception("最多只可创建10个样式，有更多需求请联系商务");
						}
						if(!oldstylename.equals(stylename)){
							searmap.put("stylename", stylename);
							/*List<PointStyleEntity> exsitStyles=this.pointStyleService.findStyleByParam(searmap);
							if(exsitStyles!=null&&exsitStyles.size()>0){
								throw new Exception("样式名称已存在，请换一个试试");
							}*/
						}
					}else{
						styleentity.setDef1("1");
					}
					id = this.pointStyleService.updatePointStyle(styleentity);//修改样式
					
					//同时解绑以前的分组与该样式的关联关系
					PointGroupEntity oldgroup=this.pointGroupService.findByStyleid(styleid);
					if(oldgroup!=null){
						oldgroup.setStyleid(null);
						this.pointGroupService.updateGroup(oldgroup);
					}
					
					/**
					 * 如果有分组名称，同时关联分组与样式关系
					 */
					if(groupid!=null&&!groupid.equals("")){
						//绑定新的网点分组与样式
						PointGroupEntity groupentity=new PointGroupEntity();
						groupentity=this.pointGroupService.findByid(groupid);
						groupentity.setStyleid(styleentity);
						this.pointGroupService.updateGroup(groupentity);
						
						//修改网点与分组的关系
						PointEntity point=this.pointService.queryById(pointid);
						point.setGroupid(groupentity);
						this.pointService.updatePoint(point);
					}else{
						//修改网点与样式的关系
						PointEntity point=this.pointService.queryById(pointid);
						point.setGroupid(null);
						point.setStyleid(styleentity);
						this.pointService.updatePoint(point);
					}
				}
			}
			boolean isSuccess = !StringUtil.isStringEmpty(id);
			if(isSuccess){
				//如果是修改默认样式，则需要判断allflag 当allfalg为1的时候，修改该用户下所有没有分组的网点样式，否则就不需要修改
				if(StringUtils.isNotEmpty(styleentity.getStylename())&&styleentity.getStylename().equals("默认样式")){
					if(allflag.equals("1")){
						this.pointService.updatePointDefaultStyle(userId);//解绑网点与子集关系
						this.pointStyleService.deleteUnuseStyles(userId);//删除用户无用子集
					}
				}
			}
			resultObj = buildResult("修改样式" + (isSuccess ? "成功" : "[" + id + "]失败"), null, isSuccess);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 删除样式
	 * @param styleid
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-12上午11:01:04
	 */
	@RequestMapping("deletePointStyle")
	@ResponseBody
	public Map<String,Object> deletePointStyle(@RequestParam(required = true) String styleid,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			
			//去掉分组与样式的关联关系
			PointGroupEntity group=this.pointGroupService.findByStyleid(styleid);
			if(group!=null&&group.getId()!=null){
				group.setStyleid(null);
				this.pointGroupService.updateGroup(group);
			}
			
			//删除网点与样式的关联关系
			List<PointEntity> pointlist=this.pointService.findByStyleid(styleid);
			List<String> ids=null;
			if(pointlist!=null&&pointlist.size()>0){
				ids=new ArrayList<String>();
				for(PointEntity p:pointlist){
					ids.add(p.getId());
				}
				this.pointService.updatePointStylesToNUll(ids);
			}
			
			
			this.pointStyleService.deletePointStyle(styleid);
			
			resultObj = buildResult("删除样式" + "成功" , null, true);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 查询所有的样式列表
	 * @param styleid
	 * @param stylename
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-12上午11:07:24
	 */
	@RequestMapping("getAllPointStyle")
	@ResponseBody
	public Map<String,Object> getAllPointStyle(String styleid,String stylename,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		List<Map<String,Object>> maplist=null;
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			//String userId=user.getId();
			String eid=user.getEid().getId();
			//String dcode=user.getDeptId().getCode();
			
			/**
			 * 2016.6.2查询总账号的分组，子账号不允许增加修改或删除分组，分组、样式、字段都以总账号为准
			 */
			UserEntity topuser=this.userService.findTopUserByEid(eid);
			if(topuser==null){
				return buildResult("无总账号" , null, false);
			}
			
			Map searmap=new HashMap();
			searmap.put("id", styleid);
			searmap.put("stylename", stylename);
			searmap.put("userid", topuser.getId());
			searmap.put("eid", topuser.getEid().getId());
			searmap.put("dcode", null);
			searmap.put("def1", "0");
			List<PointStyleEntity> stylelist=this.pointStyleService.findStyleByParam(searmap);
			if(stylelist==null||stylelist.size()==0){
				//throw new Exception("查询样式失败");
				resultObj = buildResult("无样式" , null, false);
				return resultObj;
			}
			else{
				maplist=new ArrayList<Map<String,Object>>();
				FieldMap fieldmap=new FieldMap();
				for(PointStyleEntity style:stylelist){
					
					Map<String,Object> map=fieldmap.convertBean(style);
					
					PointGroupEntity group=this.pointGroupService.findByStyleid(style.getId());
					if(group!=null){
						//style.setDef2(group.getId());
						map.put("def2", group.getId());
						map.put("groupname", group.getGroupname());
					}else{
						map.put("def2", "");
						map.put("groupname", "");
					}
					//查询自定义文件
					if(style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
						PointStyleCustomEntity custom=this.pointStyleService.findCustomfileByid(style.getAppcustom());
						if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
							//style.setAppcustom(custom.getFilepath());
							//style.setDef1(custom.getWidth()+","+custom.getHeight());
							map.put("appcustom", custom.getFilepath());
							map.put("def1", custom.getWidth()+","+custom.getHeight());
							map.put("appcustomid", style.getAppcustom());
						}
					}else{
						map.put("appcustom", "");
						map.put("def1", "");
						map.put("appcustomid", "");
					}
					maplist.add(map);
				}
			}
			resultObj = buildResult("查询样式成功" , maplist, true);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 查询用户默认样式
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-12下午2:34:22
	 */
	@RequestMapping("getUserDefaultPointStyle")
	@ResponseBody
	public Map<String,Object> getUserDefaultPointStyle(HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		List<Map<String,Object>> maplist=null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId=user.getId();
			String eid=user.getEid().getId();
			String dcode=user.getDeptId().getCode();
			
			/**
			 * 2016.6.2查询总账号的分组，子账号不允许增加修改或删除分组，分组、样式、字段都以总账号为准
			 */
			UserEntity topuser=this.userService.findTopUserByEid(eid);
			if(topuser==null){
				return buildResult("无总账号" , null, false);
			}
			
			Map searmap=new HashMap();
			searmap.put("userid", topuser.getId());
			searmap.put("eid", topuser.getEid().getId());
			searmap.put("dcode", null);
			searmap.put("def1", "0");
			searmap.put("stylename", "默认样式");
			List<PointStyleEntity> stylelist=this.pointStyleService.findStyleByParam(searmap);
			if(stylelist==null||stylelist.size()==0){
				resultObj = buildResult("该用户未创建默认样式" , null, false);
			}else{
				FieldMap fieldmap=new FieldMap();
				maplist=new ArrayList<Map<String,Object>>();
				for(PointStyleEntity style:stylelist){
					Map<String,Object> map=fieldmap.convertBean(style);
					//查询自定义文件
					if(style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
						PointStyleCustomEntity custom=this.pointStyleService.findCustomfileByid(style.getAppcustom());
						if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
							/*style.setAppcustom(custom.getFilepath());
							style.setDef1(custom.getWidth()+","+custom.getHeight());*/
							map.put("appcustom", custom.getFilepath());
							map.put("def1", custom.getWidth()+","+custom.getHeight());
							map.put("appcustomid", style.getAppcustom());
						}
					}
					maplist.add(map);
				}
				resultObj = buildResult("查询用户默认样式成功" , maplist, true);
			}
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 上传自定义文件
	 * @param multiFile
	 * @param session
	 * @return
	 * @throws IOException
	 * @Author Juannyoh
	 * 2015-11-18上午10:06:59
	 */
	@RequestMapping("saveCustomFile")
	@ResponseBody
	public Map<String,Object>  saveCustomFile(MultipartFile multiFile, HttpSession session) {
		String picPath = null;
		String id=null;
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		Map<String, Object> result = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String eid = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			int width=0,height=0;
			
			if (multiFile != null) {
				long maxsize=200*1024;
				if(multiFile.getSize()>maxsize){
					throw new Exception("上传图片大小最大不超过200KB");
				}
				InputStream is = multiFile.getInputStream();
				if(is!=null){
					BufferedImage sourceImg = javax.imageio.ImageIO.read(is); 
					width=sourceImg.getWidth();
					height=sourceImg.getHeight();
					if(width>60||height>60){
						throw new Exception("上传图片尺寸最大不超过60*60");
					}
				}
				is.close();
			}
			picPath=saveUploadFile(multiFile,eid,CUSTOMSTYLE_DIR_NAME);//上传文件
			if (picPath!=null&&!picPath.equals("")){
				LOGGER.info("## 上传自定义图片成功[" + picPath + "]");
				PointStyleCustomEntity customentity=new PointStyleCustomEntity();
				customentity.setUserid(userId);
				customentity.setEid(eid);
				customentity.setDcode(dcode);
				customentity.setFilepath(picPath);
				customentity.setUploadtime(new Date());
				customentity.setWidth(width);
				customentity.setHeight(height);
				id=this.pointStyleService.saveCustomFiles(customentity);
				if(id!=null&&!id.equals("")){
					result=new HashMap<String,Object>();
					result.put("customid", id);
					result.put("picPath", picPath);
					resultObj=buildResult("上传自定义图片成功" , result, true);
				}
				else resultObj=buildResult("上传自定义图片失败" , null, false);
			}
		} catch (Exception e) {
			 resultObj=buildResult("上传自定义图片失败:"+e.getMessage() , null, false);
		}
		return resultObj;
	}
	
	/**
	 * 获取所有的自定义文件
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-18上午10:18:37
	 */
	@RequestMapping("getUserAllCustomfiles")
	@ResponseBody
	public Map<String,Object> getUserAllCustomfiles(HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId=user.getId();
			
			/**
			 * 2016.6.2查询总账号的分组，子账号不允许增加修改或删除分组，分组、样式、字段都以总账号为准
			 */
			UserEntity topuser=this.userService.findTopUserByEid(user.getEid().getId());
			if(topuser==null){
				return buildResult("无总账号" , null, false);
			}
			
			List<PointStyleCustomEntity> stylelist=this.pointStyleService.findCustomsByUserid(topuser.getId());
			if(stylelist==null||stylelist.size()==0){
				resultObj = buildResult("该用户未上传过自定义文件" , null, false);
			}else{
				resultObj = buildResult("查询用户自定义文件成功" , stylelist, true);
			}
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 根据自定义文件id，删除自定义文件
	 * @param customid
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-18上午10:20:31
	 */
	@RequestMapping("deletePointStyleCustom")
	@ResponseBody
	public Map<String,Object> deletePointStyleCustom(@RequestParam(required = true) String customid,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			this.pointStyleService.deleteCustomfileByid(customid);
			resultObj = buildResult("删除自定义文件成功" , null, true);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 保存样式时，判断是否超过10个
	 * @param searmap
	 * @return
	 * @Author Juannyoh
	 * 2015-12-2下午3:01:30
	 */
	public boolean isCanSaveStyle(String stylename,Map searmap){
		boolean flag=true;
		List<PointStyleEntity> exsitStylesCount=this.pointStyleService.findStyleByParam(searmap);
		searmap.put("stylename", "默认样式");
		List<PointStyleEntity> defaultstyleCount=this.pointStyleService.findStyleByParam(searmap);
		int exsitcount=0;
		if(exsitStylesCount!=null&&exsitStylesCount.size()>0){
			exsitcount=exsitStylesCount.size();
		}
		if(defaultstyleCount!=null&&defaultstyleCount.size()>0){
			exsitcount=exsitcount;
		}else exsitcount=exsitcount+1;
		
		if(stylename!=null&&!stylename.equals("默认样式")&&exsitcount>=11){
			flag=false;
		}
		return flag;
	}
	
	
	/**
	 * 解析网点
	 * @param ids
	 * @param session
	 * @throws Exception
	 * @Author Juannyoh
	 * 2015-12-8下午5:04:23
	 */
	@RequestMapping("analysisPoint")
	@ResponseBody
	public Map<String,Object>  analysisPoint(@RequestParam(required = true)boolean iscontinue, HttpSession session)
			throws Exception {
		UserEntity user=(UserEntity)session.getAttribute("user");
		List<String> ids=(List<String>) session.getAttribute("processPoints");
		Map<String, Object> resultMap = null;
		if(ids!=null){
			//如果是继续的话，继续解析
			if(iscontinue){
				boolean flag=this.pointService.startProcessOver(ids);
				if(flag){
					resultMap = buildResult("数据解析完成", null, true);
				}
			}//否则删除已经写入的数据
			else{
				this.pointService.deletePointsByIds(ids);
				resultMap = buildResult("因不继续，后台已删除本次导入的数据", null, true);
			}
		}else resultMap = buildResult("无待处理的数据", null, true);
		//session.removeAttribute("processPoints");
		return resultMap;
	}
	
	
	@RequestMapping("getAllProcessingPoint")
	@ResponseBody
	public Map<String,Object>  getAllProcessingPoint(HttpSession session)
			throws Exception {
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String, Object> resultMap = null;
		
		if(user==null){
			throw new Exception("用户未登录");
		}
		String userid=user.getId();
		
		List<String> ids=this.pointService.getAllProcessingPointByUserid(userid);
		if(ids!=null&&ids.size()>0){
			session.setAttribute("processPoints", ids);
			resultMap = buildResult("有待处理的网点"+ids.size()+"个", ids.size(), true);
		}else resultMap = buildResult("无待处理的网点", null, false);
		return resultMap;
	}
	
	
	@RequestMapping("queryFailedPoints")
	@ResponseBody
	public Map<String, Object> queryFailedPoints(HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String, Object> map = null;
		List<Map<String,Object>> maplist=null;
		FieldMap fieldmap=new FieldMap();
		try {
			if(user == null){
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			
			Map<String, Object> result =new HashMap<String,Object>();
			result = this.pointService.queryFailedPoints(userId);
			//将自定义文件写入
			if(result!=null&&result.get("records")!=null){
				List<NetPointInfoResult> pointlist=(List<NetPointInfoResult>) result.get("records");
				maplist=new ArrayList<Map<String,Object>>();
				if(pointlist!=null&&pointlist.size()>0){
					for(NetPointInfoResult point:pointlist){
						Map<String,Object> pointmap=fieldmap.convertBean(point);
						
						/**
						 * 20160613查询每个网点的子账号名称 ,当前帐号不显示
						 */
						if(point.getUserId().equals(userId)){
							pointmap.put("username", null);
						}
						
						if(point.getStyleid()!=null){
							PointStyleEntity style=point.getStyleid();
							Map<String,Object> stylemap=fieldmap.convertBean(style);
							//查询自定义文件
							if(style!=null&&style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
								PointStyleCustomEntity custom=this.pointStyleService.findCustomfileByid(style.getAppcustom());
								if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
									stylemap.put("appcustom", custom.getFilepath());
									stylemap.put("def1", custom.getWidth()+","+custom.getHeight());
									stylemap.put("appcustomid", style.getAppcustom());
								}
							}else{
								stylemap.put("appcustom", "");
								stylemap.put("def1", "");
								stylemap.put("appcustomid", "");
							}
							pointmap.put("styleid", stylemap);
							maplist.add(pointmap);
						}else if(point.getGroupid()!=null&&point.getGroupid().getStyleid()!=null){
							Map<String,Object> groupmap=fieldmap.convertBean(point.getGroupid());
							
							PointStyleEntity style=point.getGroupid().getStyleid();
							Map<String,Object> stylemap=fieldmap.convertBean(style);
							//查询自定义文件
							if(style!=null&&style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
								PointStyleCustomEntity custom=this.pointStyleService.findCustomfileByid(style.getAppcustom());
								if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
									stylemap.put("appcustom", custom.getFilepath());
									stylemap.put("def1", custom.getWidth()+","+custom.getHeight());
									stylemap.put("appcustomid", style.getAppcustom());
								}
							}else{
								stylemap.put("appcustom", "");
								stylemap.put("def1", "");
								stylemap.put("appcustomid", "");
							}
							groupmap.put("styleid", stylemap);
							pointmap.put("groupid", groupmap);
							maplist.add(pointmap);
						}else {
							maplist.add(pointmap);
							continue;
						}
					}
				}
				result.put("records", maplist);
			}
			if(result == null){
				throw new Exception("");
			}
			map = buildResult(null, result, true);
			
		} catch (Exception e) {
			map = buildResult(e.getMessage(), new String[]{}, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	/**
	 * 根据网点id查找网点上传的图片
	 * @param pointid
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-2-22下午3:30:40
	 */
	@RequestMapping("getPointPicturesByPointid")
	@ResponseBody
	public Map<String,Object> getPointPicturesByPointid(@RequestParam(required = true)String pointid,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			List<PointPicEntity> piclist=this.pointService.findPointPicturesByPointid(pointid);
			if(piclist==null||piclist.size()==0){
				resultObj = buildResult("该网点未上传图片" , null, false);
			}else{
				resultObj = buildResult("查询网点图片成功" , piclist, true);
			}
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 删除网点图片
	 * @param picid
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-2-22下午3:35:14
	 */
	@RequestMapping("deletePointPicture")
	@ResponseBody
	public Map<String,Object> deletePointPicture(@RequestParam(required = true) String  picid,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			this.pointService.deletePointPictureByPicId(picid);
			resultObj = buildResult("删除网点图片成功" , null, true);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	
	/**
	 * 保存网点上传的图片
	 * @param multiFile  图片文件	
	 * @param pointid  网点id
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-2-22下午3:40:21
	 */
	@RequestMapping("savePointPicture")
	@ResponseBody
	public Map<String,Object>  savePointPicture(MultipartFile multiFile, @RequestParam(required = true)String pointid, HttpSession session) {
		String picPath = null;
		String id=null;
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		Map<String, Object> result = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String eid = user.getEid().getId();
			
			int width=0,height=0;
			
			if (multiFile != null) {
				long maxsize=2*1024*1024;
				if(multiFile.getSize()>maxsize){
					throw new Exception("上传图片大小最大不超过2M");
				}
				/**获取文件的后缀**/
				String originalFilename=multiFile.getOriginalFilename();
				String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1); 
				BufferedImage sourceImg=ImgCompressUtil.getNewImage(multiFile,1024,728);
				InputStream saveis=ImgCompressUtil.getImageStream(sourceImg,suffix);
				String regEx="[`~!@#$%^&*+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
				originalFilename = Pattern.compile(regEx).matcher(originalFilename).replaceAll("").trim();
				picPath=saveUploadPointPic(saveis,originalFilename,eid,POINTPIC_DIR_NAME);
				if(saveis!=null){
					saveis.close();
				}
			}
			if (picPath!=null&&!picPath.equals("")){
				LOGGER.info("## 上传自定义图片成功[" + picPath + "]");
				PointPicEntity picentity=new PointPicEntity();
				picentity.setPointid(pointid);
				picentity.setFilepath(picPath);
				picentity.setUploadtime(new Date());
				picentity.setWidth(width);
				picentity.setHeight(height);
				picentity=this.pointService.savePointPicture(picentity);
				id=picentity.getId();
				if(id!=null&&!id.equals("")){
					result=new HashMap<String,Object>();
					result.put("picid", picentity.getId());
					result.put("picPath", picPath);
					resultObj=buildResult("上传图片成功" , result, true);
				}
				else resultObj=buildResult("上传图片失败" , null, false);
			}
		} catch (Exception e) {
			if(e!=null&&e.getMessage()!=null){
				resultObj=buildResult("上传图片失败:"+e.getMessage() , null, false);
			}else {
				resultObj=buildResult("上传图片失败:文件格式不支持" , null, false);
			}
			 
		}
		return resultObj;
	}
	
	
	
	/**
	 * 保存图片文件
	 * @param is
	 * @param picName
	 * @param enterpriseId
	 * @param dirName
	 * @return
	 * @throws IOException
	 * @Author Juannyoh
	 * 2016-2-22下午4:48:33
	 */
	private String saveUploadPointPic(InputStream is,String picName,String enterpriseId,String dirName) throws IOException{
		String picPath = null;
		if (null != is) {
			picName = randomFileName(picName);
			boolean isUploadSuccess = FileUtil.saveToFile(is, picName, PointServiceConstants.IMG_ROOT_PATH
					+ File.separator + enterpriseId + File.separator + dirName);
			if (isUploadSuccess) {
				picPath = enterpriseId + File.separator + dirName + File.separator + picName;
				LOGGER.info("## 上传图片成功["+picPath+"]");
			}
		}
		return picPath;
	}
	
	
	/**
	 * 设置用户的配置字段，配置显示哪些
	 * @param cols
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-4-12上午10:21:08
	 */
	@RequestMapping("setUserDefaultPointCols")
	@ResponseBody
	public Map<String,Object> setUserDefaultPointCols(@RequestParam(required = true)String cols,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		PointExtcolEntity record=new PointExtcolEntity();
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String  userid=user.getId();
			record=this.pointExtcolService.findByUserid(user.getId());//根据用户id查找到
			if(record==null||record.getUserid()==null){
				record=new PointExtcolEntity();
				record.setUserid(userid);
			}
			record.setConfigcols(cols);
			record=this.pointExtcolService.saveConfigcols(record);
			
			resultObj = buildResult("设置成功" , cols, true);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	
	/**
	 * 点聚合 按 省、市、区
	 * @param id
	 * @param areaId
	 * @param name
	 * @param groupid
	 * @param pageNo
	 * @param pageSize
	 * @param admincode
	 * @param level
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3下午4:00:37
	 */
	@RequestMapping("queryAllForConvergeWeb")
	@ResponseBody
	public Map<String, Object> queryAllByAdmincodeForConverge(String id, String areaId, String name, String groupid, String pageNo,
			String pageSize,String  admincode,String level,HttpSession session,@RequestParam(defaultValue="true")boolean isForceConverge) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String, Object> map = null;
		List<Map<String,Object>> maplist=null;
		FieldMap fieldmap=new FieldMap();
		try {
			if(user == null){
				throw new Exception("用户未登录");
			}
			int pageNoInt = 1;
			if (!StringUtil.isStringEmpty(pageNo)) {
				pageNoInt = Integer.parseInt(pageNo);
				if (pageNoInt <= 0) {
					pageNoInt = -1;
				}
			}
			int pageSizeInt = 10;
			if (!StringUtil.isStringEmpty(pageSize)) {
				pageSizeInt = Integer.parseInt(pageSize);
				if (pageSizeInt <= 0 || pageSizeInt > 20) {
					pageSizeInt = 10;
				}
			}
			// 如果提供了ID，则只查询指定结果
			if (!StringUtil.isStringEmpty(id)) {
				PointEntity pe = this.pointService.queryById(id);
				map = buildResult(null, pe, pe != null);
				return map;
			}
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			
			//如果admincode为空，则是查全国的
			Map<String, Object> result = new HashMap<String,Object>();
			if(StringUtils.isNotEmpty(admincode)){
				if(level.equals("1")){
					if(admincode!=null&&!admincode.equals("")&&(
							admincode.indexOf("110")==0||admincode.indexOf("120")==0
							||admincode.indexOf("500")==0||admincode.indexOf("310")==0)){
						admincode=admincode.substring(0, 3);
					}
					else admincode=admincode.substring(0, 2);
				}else if(level.equals("2")){
					admincode=admincode.substring(0, 4);
				}else if(level.equals("3")){
					admincode=admincode;
				}
			}
			if(isForceConverge){
				result = this.pointService.queryAllByAdmincodeForConverge(userId, name, groupid,null,enterpriseId, dcode, pageNoInt, pageSizeInt, areaId,admincode,false);
			}else{
				result = this.pointService.queryAllByAdmincodeForNoConverge(userId, name, groupid,null,enterpriseId, dcode, pageNoInt, pageSizeInt, areaId,admincode,false);
			}
			
			if(result == null){
				return buildResult("无数据", null, true);
			}
			map = buildResult(null, result, true);
			
		} catch (Exception e) {
			map = buildResult(e.getMessage(), new String[]{}, true);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	/**
	 * APP端查询网点数据（省市区自动聚合）
	 * @param id
	 * @param areaId
	 * @param name
	 * @param groupid
	 * @param pageNo
	 * @param pageSize
	 * @param admincode
	 * @param level
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3下午5:21:48
	 */
	@RequestMapping("queryAllForConvergeApp")
	@ResponseBody
	public Map<String, Object> queryAllByAdmincodeForConvergeApp(String id, String name, String groupid, String pageNo,
			String pageSize,String  admincode,String level,HttpSession session,String key) {
		UserEntity user=null;
		if(StringUtils.isNotEmpty(key)){
			user=this.userService.findUserById(key);
		}else{
			user=(UserEntity)session.getAttribute("user");
		}
		
		Map<String, Object> map = null;
		try {
			if(user == null){
				throw new Exception("用户不存在");
			}
			int pageNoInt = 1;
			if (!StringUtil.isStringEmpty(pageNo)) {
				pageNoInt = Integer.parseInt(pageNo);
				if (pageNoInt <= 0) {
					pageNoInt = -1;
				}
			}
			int pageSizeInt = 10;
			if (!StringUtil.isStringEmpty(pageSize)) {
				pageSizeInt = Integer.parseInt(pageSize);
				if (pageSizeInt <= 0 || pageSizeInt > 50) {
					pageSizeInt = 50;
				}
			}
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			//如果admincode为空，则是查全国的
			Map<String, Object> result =null;
			result=new HashMap<String,Object>();
			if(StringUtils.isNotEmpty(admincode)){
				if(level.equals("1")){
					if(admincode!=null&&!admincode.equals("")&&(
							admincode.indexOf("110")==0||admincode.indexOf("120")==0
							||admincode.indexOf("500")==0||admincode.indexOf("310")==0)){
						admincode=admincode.substring(0, 3);
					}
					else admincode=admincode.substring(0, 2);
				}else if(level.equals("2")){
					admincode=admincode.substring(0, 4);
				}else if(level.equals("3")){
					admincode=admincode;
				}
			}
			result = this.pointService.queryAllByAdmincodeForNoConverge(userId, name, groupid,id,enterpriseId, dcode, pageNoInt, pageSizeInt, null,admincode,true);
			if(result == null){
				return buildResult("无数据", null, true);
			}
			map = buildResult(null, result, true);
			
		} catch (Exception e) {
			map = buildResult(e.getMessage(), new String[]{}, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	/**
	 * 移动端访问时。页面初始化用户信息到session
	 * @param key
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-6-4下午2:59:52
	 */
	@RequestMapping("initSessionUserByKey")
	@ResponseBody
	public Map<String,Object> initSessionUserByKey(@RequestParam(required=true)String key,HttpSession session){
		Map<String,Object> result=null;
		if(StringUtils.isNotEmpty(key)){
			UserEntity user=this.userService.findUserById(key);
			if(null==user){
				result= buildResult("用户不存在", null, false);
			}
			else{
				session.setAttribute("user", user);
				session.setAttribute("isLogined", true);
				session.setAttribute("tempLogined", true);
				result= buildResult("用户存在", user, true);
			}
		}
		return result;
	}
	
	/**
	 * 根据网点中用户id查找用户名称--只查询子账号
	 * @param pointuserid
	 * @param sessionuser
	 * @return
	 * @Author Juannyoh
	 * 2016-6-13下午1:41:19
	 */
	/*public String getUsernameByPointUserID(String pointuserid,String sessionuserId,List<UserEntity> userlist){
		String username=null;
		if(userlist!=null&&userlist.size()>0){
			for(UserEntity user:userlist){
				//查询网点对应的用户名
				if(!StringUtils.isNotEmpty(pointuserid)&&!pointuserid.equals(sessionuserId)){
					if(user.getId().equals(pointuserid)){
						username=user.getUsername();
						break;
					}
				}
			}
		}
		return username;
	}*/
	
	@RequestMapping("syncCdituhuiPoint")
	@ResponseBody
	public Map<String,Object> syncCdituhuiPoint(@RequestParam(required=true)String mapurl,HttpSession session){
		Map<String,Object> result =null;
		try {
			List<DituhuiPointResult> cpoints=HttpClientDituhui.syncDituhuiPoint(mapurl);
			UserEntity user=(UserEntity)session.getAttribute("user");
			if(null==user){
				throw new Exception("用户未登录");
			}
			//查总账号
			UserEntity topuser=this.userService.findTopUserByEid(user.getEid().getId());
			if(cpoints!=null&&cpoints.size()>0){
				LOGGER.info("采集的网点数量cpoints："+cpoints.size());
				Map<String,Object> resultmap=formatCPoint2Epoint(cpoints,user.getId(),user.getEid().getId(),user.getDeptId().getId(),topuser);
				List<PointEntity> epoints=resultmap.get("epointlist")==null?null:(List<PointEntity>)resultmap.get("epointlist");
				boolean alowflag=(Boolean) resultmap.get("alowflag");
				if(!alowflag){
					return buildResult("采集失败，请检查网点属性字段", null, false);
				}
				if(null!=epoints&&epoints.size()>0){
					LOGGER.info("转换的网点数量epoints："+epoints.size());
					
					/**
					 * 每个用户总网点数不超过10000条
					 */
					Map<String,Object> importresultmap=new HashMap<String,Object>();
					//查询该用户id下的所有网点
					Map<String, Object> existresult = this.pointService.queryAllByPage(user.getId(), null, null, null,
							user.getEid().getId(), null, -1, 10, null);
					List<NetPointInfoResult> existrecords=(List<NetPointInfoResult>) existresult.get("records");
					int total=0;
					if(existrecords!=null&&existrecords.size()>0){
						total=epoints.size()+existrecords.size(); //上传的网点数+已有的网点数
					}
					if(total>10000){
						return buildResult("超过10000条数据，请联系商务", null, false);
					}
					
					Map<String, Object>  map=this.pointService.importNetPoints(epoints);
					if(null!=map){
						List<String> ids=(List<String>) map.get("ids");
						if(null!=ids){
							result = buildResult("采集"+ids.size()+"个网点", ids.size(), true);
						}
					}else{
						result = buildResult("采集0个网点", 0, true);
					}
				}else{
					result = buildResult("采集0个网点", 0, true);
				}
			}else{
				result = buildResult("采集0个网点", 0, true);
			}
		} catch (Exception e) {
			result = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}
	
	public Map<String,Object> formatCPoint2Epoint(List<DituhuiPointResult> cpoints,String userId, String enterpriseId,
			String departmentId,UserEntity topuser){
		Map<String,Object> resultmap=new HashMap<String,Object>();
		List<PointEntity> epoints=null;
		boolean alowflag=true;
		try {
			PointExtcolEntity  extcol=this.pointExtcolService.findByUserid(topuser.getId());//获取总账号的自定义字段
			Map<String, Object> extcolheaderNew=new FieldMap().ChangeObjectToMap(extcol);//将查询的表头存在map中，用field、fieldvalue对应
			BaiduCoordinateConvertImpl bdconvert=new BaiduCoordinateConvertImpl();
			if(cpoints!=null&&cpoints.size()>0){
				epoints=new ArrayList<PointEntity>();
				PointExtcolValEntity pointExtcolValEntity=null;
				for(DituhuiPointResult cpoint:cpoints){
					//判断大众版网点是否已经采集过，如果已采集过那不重复采集
					boolean isexsit=this.pointService.isExsitCpoint(cpoint.getCid(), userId);
					if(isexsit){
						LOGGER.info(cpoint.getCid()+"此网点已采集过");
						continue;
					}
					if(!alowflag){
						break;
					}
					PointEntity epoint=new PointEntity();
					epoint.setName(cpoint.getTitle());
					epoint.setCreateTime(new Date());
					epoint.setDepartmentId(departmentId);
					epoint.setEnterpriseId(enterpriseId);
					epoint.setUserId(userId);
					epoint.setDutyName(cpoint.getCid());//将大众版的网点id存在废弃字段dutyname中
					
					//自定义字段
					List<PointAttributes> attris=cpoint.getAttributes();
					if(attris!=null&&attris.size()>0){
						pointExtcolValEntity=new PointExtcolValEntity();
						pointExtcolValEntity.setUserid(userId);
						epoint.setPointExtcolValEntity(pointExtcolValEntity);
						for(PointAttributes attr:attris){
							String key=isValueInMap(attr.getKey(), extcolheaderNew);//判断是否在自定义字段中
							if(!StringUtils.isEmpty(key)){
								Field field=pointExtcolValEntity.getClass().getDeclaredField(key);
								field.setAccessible(true);
								field.set(pointExtcolValEntity, attr.getValue());
							}else{
								alowflag=false;
								break;
							}
						}
					}
					
					//百度经纬度转超图墨卡托
					Point point=new Point(cpoint.getBdsmx(),cpoint.getBdsmy());
					point = bdconvert.convertV2(point);
					point=SuperMapCoordinateConvertImpl.smLL2MC(point);
					if(point!=null){
						epoint.setSmx(new BigDecimal(point.getLon()));
						epoint.setSmy(new BigDecimal(point.getLat()));
						ReverseAddressMatchResult rmr = (ReverseAddressMatchResult) this.addressMatch.addressMatchByCoor(point.getLon(), point.getLat(), 0, 500D);
						//坐标查询省市区
						Map<String,String> result = geocodingService.searchAdmincodeForCounty(point.getLon(), point.getLat());
						if(null!=result){
							if(null==rmr){
								epoint.setAddress(result.get("PROVINCE")+result.get("CITY2")+result.get("COUNTY"));
								epoint.setAdmincode(result.get("ADMINCODE"));
							}else{
								StringBuilder addrsb=new StringBuilder();
								if(StringUtils.isNotEmpty(rmr.getProvince())){
									addrsb.append(rmr.getProvince());
								}
								if(StringUtils.isNotEmpty(rmr.getCity())){
									addrsb.append(rmr.getCity());
								}
								if(StringUtils.isNotEmpty(rmr.getCounty())){
									addrsb.append(rmr.getCounty());
								}
								if(StringUtils.isNotEmpty(rmr.getAddress())){
									addrsb.append(rmr.getAddress());
								}
								epoint.setAddress(addrsb.toString());
								epoint.setAdmincode(result.get("ADMINCODE"));
							}
							epoint.setStatus(0);//可用
						}else{
							epoint.setStatus(1);//不可用
						}
					}else{
						epoint.setStatus(1);//不可用
					}
					epoints.add(epoint);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultmap.put("epointlist", epoints);
		resultmap.put("alowflag", alowflag);
		return resultmap;
	}
	
	/**
	 * 判断一个字符串是否在map中，是则返回key
	 * @param value
	 * @param map
	 * @return
	 * @Author Juannyoh
	 * 2015-8-25下午2:32:31
	 */
	public static String  isValueInMap(String value,Map<String,Object> map){
		String result=null;
		if(map !=null){
			for(String key : map.keySet()){
				if(value.equals(map.get(key))){
					result=key;
					break;
				}
			}
		}
		return result;
	}
	
	
	/**
	 * 路线规划 查询网点
	 * @param type
	 * @param value
	 * @param pageNo
	 * @param pageSize
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-10-13上午8:54:42
	 */
	@RequestMapping("queryPointForPathPlan")
	@ResponseBody
	public Map<String,Object> queryPointForPathPlan(@RequestParam(required=true)String type,@RequestParam(required=true)String value ,
			@RequestParam(value="page")int pageNo,@RequestParam(value="rows")int pageSize,
			HttpSession session){
		Map<String,Object> resultmap=new HashMap<String,Object>();
		try {
			UserEntity user=(UserEntity)session.getAttribute("user");
			List<ExportPointBean> pointlist=null;
			if(null==user){
				throw new Exception("用户未登录");
			}
			//按照类型搜索  1、网点名称；2、网点编号；3、网点分组
			if(type.equals("1")){
				pointlist=this.pointService.queryPointForPathPlan(user.getDeptId().getCode(), user.getEid().getId(), null, value, pageNo, pageSize, null);
			}else if(type.equals("2")){
				String coldesc="网点编号";//区划编号与网点编号对应    查出网点名称
				String cols=null;//自定义列id
				//根据用户id查找到自定义字段
				UserEntity topuser=this.userService.findTopUserByEid(user.getEid().getId());
				PointExtcolEntity record=this.pointExtcolService.findByUserid(topuser.getId());
				Field[] fields=PointExtcolEntity.class.getDeclaredFields();//获取所有字段
				if(record!=null){
					for(Field f:fields){
						f.setAccessible(true);
						if(f.get(record)!=null&&(f.get(record).toString().trim()).equals(coldesc)){
							cols=f.getName();
							break;
						}
					}
				}
				if(StringUtils.isEmpty(cols)){
					return buildResult("无网点编号字段", null, false);
				}else{
					pointlist=this.pointService.queryPointByColKeyForPathPlan(user.getDeptId().getCode(), cols, value, user.getEid().getId(),  pageNo, pageSize);
				}
			}else if(type.equals("3")){
				pointlist=this.pointService.queryPointForPathPlan(user.getDeptId().getCode(), user.getEid().getId(), value, null, pageNo, pageSize, null);
			}
			resultmap=buildResult(null, pointlist, true);
		} catch (Exception e) {
			LOGGER.info("####路线规划查询网点失败："+e.getMessage());
			resultmap=buildResult(e.getMessage(), null, false);
		}
		return resultmap;
	}
}
