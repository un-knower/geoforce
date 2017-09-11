package com.supermap.egispboss.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.supermap.egispboss.constants.EgispBossConstants;
import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispboss.util.CommonUtil.DateType;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.service.PointService;

/**
 * 区划服务
 * @author Administrator
 */
public class AreaServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(AreaServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if(!CommonUtil.isStringEmpty(method)){
			LOGGER.info("## 收到请求[method,"+method+"]");
			if(method.equals("exportAreas")){
				exportAreas(req,resp);//导出区划
			}else if(method.equals("queryAreasCount")){
				queryAreasCount(req,resp);//查询条件下的区划数量
			}else if(method.equals("deleteAreas")){
				deleteAreas(req,resp);//删除条件下的区划
			}
		}else{
			write(req, resp, "参数[method]为空", null, false);
		}
	}
	
	
	/**
	 * 导出所有区划
	 * @Author Juannyoh
	 * 2016-9-18下午4:08:44
	 */
	public void exportAreas(HttpServletRequest req, HttpServletResponse resp){
		
		resp.reset();//清空缓存区，防止存在某些字符使得下载的文件格式错误
		resp.setContentType("application/zip;charset=UTF-8");//指定文件类型，其他类型可参见w3school的文档，也可参见下文的附录标准
		resp.setHeader("Content-Disposition", "attachment;filename=AreaData.zip");// attachment后面一定是分号
		
		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String bdate=req.getParameter("bdate");//开始时间
		String edate=req.getParameter("edate");//结束时间
		String groupids=req.getParameter("groupids");//分组id，多个以逗号分割
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		try {
			if(StringUtils.isEmpty(eid)||StringUtils.isEmpty(parentuserid)){
				throw new Exception("总账号信息错误");
			}
			
			IAreaService areaService=(IAreaService) getBean("areaService");
			//查询区划信息
			if(!StringUtils.isEmpty(levels)&&!StringUtils.isEmpty(admincode)){
				if(levels.equals("1")){
					admincode=admincode.substring(0, 2);
				}else if(levels.equals("2")){
					admincode=admincode.substring(0, 4);
				}
			}
			//时间
			Date btimed=null;
			Date etimed=null;
			//时间转换
			if(!StringUtils.isEmpty(bdate)){
				btimed=CommonUtil.dateConvert(bdate.trim()+" 00:00:00", DateType.TIMESTAMP);
			}
			if(!StringUtils.isEmpty(edate)){
				etimed=CommonUtil.dateConvert(edate.trim()+" 23:59:59", DateType.TIMESTAMP);
			}
			
			String[] childs=null;
			String[] groups=null;
			if(!StringUtils.isEmpty(childuserids)){
				childs=childuserids.split(",");
			}
			if(!StringUtils.isEmpty(groupids)){
				groups=groupids.split(",");
			}
//			List<AreaEntity> arealist=areaService.queryByUseridsEboss(admincode, eid, childs, btimed, etimed, groups, false);
//			List<String> areaids=getAreaids(arealist);
//			if(null!=areaids&&areaids.size()>0){
//				String areaIds[] = new String[areaids.size()];
//				areaIds=areaids.toArray(areaIds);
//				byte[]  bytes = areaService.exportUDB2Byte(areaIds, null);
				byte[]  bytes = areaService.exportUDB2Byte(admincode, eid, childs, btimed, etimed, groups, true);
				String folder = getUUID();
				File f =Byte2File(bytes,EgispBossConstants.AREA_EXPORT_DIR,folder+".zip");
				LOGGER.info("## 导出文件路径["+EgispBossConstants.AREA_EXPORT_DIR+File.separator+folder+".zip"+"]");
				if(f.exists() && f.isFile()){
					FileInputStream is = new FileInputStream(f);
					byte[] buff = new byte[1024*1024];
					OutputStream os = resp.getOutputStream();
					int size = 0;
					while((size = is.read(buff)) > 0){
						os.write(buff,0,size);
					}
					os.flush();
					os.close();
					is.close();
					f.delete();
				}else{
					throw new Exception(EgispBossConstants.AREA_EXPORT_DIR+File.separator+folder+".zip"+"不存在或指定的路径不是文件");
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	/**
	 * 查询区划数量
	 * @Author Juannyoh
	 * 2016-9-20上午10:34:50
	 */
	public void queryAreasCount(HttpServletRequest req, HttpServletResponse resp){
		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String bdate=req.getParameter("bdate");//开始时间
		String edate=req.getParameter("edate");//结束时间
		String groupids=req.getParameter("groupids");//分组id，多个以逗号分割
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		try {
			if(StringUtils.isEmpty(eid)||StringUtils.isEmpty(parentuserid)){
				throw new Exception("总账号信息错误");
			}
			
			IAreaService areaService=(IAreaService) getBean("areaService");
			//查询区划信息
			if(!StringUtils.isEmpty(levels)&&!StringUtils.isEmpty(admincode)){
				if(levels.equals("1")){
					admincode=admincode.substring(0, 2);
				}else if(levels.equals("2")){
					admincode=admincode.substring(0, 4);
				}
			}
			//时间
			Date btimed=null;
			Date etimed=null;
			//时间转换
			if(!StringUtils.isEmpty(bdate)){
				btimed=CommonUtil.dateConvert(bdate.trim()+" 00:00:00", DateType.TIMESTAMP);
			}
			if(!StringUtils.isEmpty(edate)){
				etimed=CommonUtil.dateConvert(edate.trim()+" 23:59:59", DateType.TIMESTAMP);
			}
			
			String[] childs=null;
			String[] groups=null;
			if(!StringUtils.isEmpty(childuserids)){
				childs=childuserids.split(",");
			}
			if(!StringUtils.isEmpty(groupids)){
				groups=groupids.split(",");
			}
			List<AreaEntity> arealist=areaService.queryByUseridsEboss(admincode, eid, childs, btimed, etimed, groups, false);
			int count=-1;
			if(null!=arealist&&arealist.size()>0){
				count=arealist.size();
			}
			if(count>=0){
				write(req, resp, null, count, true);
			}else{
				write(req, resp, "无数据", 0, true);
			}
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	
	/**
	 * 删除区划
	 * @Author Juannyoh
	 * 2016-9-20上午10:36:29
	 */
	public void deleteAreas(HttpServletRequest req, HttpServletResponse resp){
		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String bdate=req.getParameter("bdate");//开始时间
		String edate=req.getParameter("edate");//结束时间
		String groupids=req.getParameter("groupids");//分组id，多个以逗号分割
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		try {
			if(StringUtils.isEmpty(eid)||StringUtils.isEmpty(parentuserid)){
				throw new Exception("总账号信息错误");
			}
			
			IAreaService areaService=(IAreaService) getBean("areaService");
			//查询区划信息
			if(!StringUtils.isEmpty(levels)&&!StringUtils.isEmpty(admincode)){
				if(levels.equals("1")){
					admincode=admincode.substring(0, 2);
				}else if(levels.equals("2")){
					admincode=admincode.substring(0, 4);
				}
			}
			//时间
			Date btimed=null;
			Date etimed=null;
			//时间转换
			if(!StringUtils.isEmpty(bdate)){
				btimed=CommonUtil.dateConvert(bdate.trim()+" 00:00:00", DateType.TIMESTAMP);
			}
			if(!StringUtils.isEmpty(edate)){
				etimed=CommonUtil.dateConvert(edate.trim()+" 23:59:59", DateType.TIMESTAMP);
			}
			
			String[] childs=null;
			String[] groups=null;
			if(!StringUtils.isEmpty(childuserids)){
				childs=childuserids.split(",");
			}
			if(!StringUtils.isEmpty(groupids)){
				groups=groupids.split(",");
			}
			List<AreaEntity> arealist=areaService.queryByUseridsEboss(admincode, eid, childs, btimed, etimed, groups, false);
			List<String> areaids=getAreaids(arealist);
			if(null!=areaids&&areaids.size()>0){
				boolean resilt=areaService.deleteRegions(areaids);
				if(resilt){
					PointService pointService=(PointService) getBean("pointService");
					for(String areaid:areaids){
						List<PointEntity> pointlist=pointService.queryByAreaId(areaid, null,null);
						 if(pointlist!=null&&pointlist.size()>0){
							for(PointEntity entity:pointlist){
								entity.setAreaId("");
								pointService.updatePoint(entity);
							} 
						 }
					}
					write(req, resp, null, areaids.size(), true);
				}else{
					write(req, resp, "删除失败", null, false);
				}
			}else write(req, resp, null, areaids.size(), true);
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	
	public List<String> getAreaids(List<AreaEntity> list){
		List<String> areaids=null;
		if(null!=list&&list.size()>0){
			areaids=new ArrayList<String>();
			for(AreaEntity area:list){
				areaids.add(area.getId());
			}
		}
		return areaids;
	}
	
	/** 
     * 根据byte数组，生成文件 
     */  
    public static File Byte2File(byte[] bfile, String filePath,String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(filePath);  
            if(!dir.exists()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath+"\\"+fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile); 
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }
        }
        return file;
    }
    
    
    public static  String getUUID(){
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString().replaceAll("\\-", "");
		return uuidString;
	}
			
}
