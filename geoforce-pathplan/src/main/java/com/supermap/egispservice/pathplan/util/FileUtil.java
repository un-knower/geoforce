package com.supermap.egispservice.pathplan.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator;
import com.supermap.data.Point2D;
import com.supermap.egispservice.pathplan.pojo.PathPlanOrder;

public class FileUtil {
	// 删除文件夹
	// param folderPath 文件夹完整绝对路径
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 
	 * <p>Title ：getPutianData</p>
	 * Description：		获取莆田测试数据
	 * @param path
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-2 上午10:32:20
	 */
	/**
	 * <p>Title ：getPutianData</p>
	 * Description：
	 * @param path
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-2 上午11:09:34
	 */
	public static final List<PathPlanOrder> getPutianData(String path,boolean isNeedConvert){
		List<PathPlanOrder> pathPlanOrders = new LinkedList<PathPlanOrder>();
		try {
			File f = getLastUpateFile(path, null);
			// 文件名称为时间格式（时间为最终送达的统一截止时间）：yyyyMMddHHmm
			String name = f.getName();
			Date completedTime = data_filename_sdf.parse(name);
			Calendar completedCal = Calendar.getInstance();
			completedCal.setTime(completedTime);
			int completedMin = completedCal.get(Calendar.MINUTE);
			
			List<String> lines = FileUtils.readLines(f);
			for(String line : lines){
				String lineItems[] = line.split(",");
				PathPlanOrder ppo = new PathPlanOrder();
				//分列信息：order_id,id,smx,smy,date,start_time,end_time,buffer,fix_consume_time
				ppo.setOrderId(lineItems[0]);
				ppo.setId(lineItems[1]);
				double smx = Double.valueOf(lineItems[2]);
				double smy = Double.valueOf(lineItems[3]);
				Point2D pp = new Point2D();
				pp.setX(smx);
				pp.setY(smy);
				ppo.setPp(pp);
				com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator.Point point = new com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator.Point();
				point.setX(smx);
				point.setY(smy);
				if(isNeedConvert){
					CoordinateTranslator.mercatorToLngLat(point);
				}
				Point2D p = new Point2D(point.getX(),point.getY());
				ppo.setP(p);
				ppo.setOrderDate(lineItems[4]);
				Date startSendTime = sdf.parse(lineItems[4]+" "+lineItems[5]);
				ppo.setStartSendTime(startSendTime);
				Date endSendTime = sdf.parse(lineItems[4]+" "+lineItems[6]);
				ppo.setEndSendTime(endSendTime);
				ppo.setId(lineItems[9]);
				ppo.setCarCost(Integer.parseInt(lineItems[10]));
				Calendar endSendCal = Calendar.getInstance();
				endSendCal.setTime(endSendTime);
				int endSendMin = endSendCal.get(Calendar.MINUTE);
//				if(endSendMin != completedMin){
//					ppo.setFixedSend(true);
//				}
				pathPlanOrders.add(ppo);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pathPlanOrders;
	}
	
	public static File getLastUpateFile(String dir, final String regex) {
		File f = new File(dir);
		File result = null;
		if (f.exists() && f.isDirectory()) {
			File filterFiles[] = null;
			if (regex != null && !"".equals(regex)) {
				filterFiles = f.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.matches(regex);
					}
				});
			}else {// 如果未提供相应regex，则返回当前目录的最近修改的文件
				filterFiles = f.listFiles();
			}
			if(null != filterFiles && filterFiles.length > 0){
				int lstModifyIndex = 0;
				long lastModifyTime = -1;
				for(int i=0;i<filterFiles.length;i++){
					long lastModify = filterFiles[i].lastModified();
					if(lastModify > lastModifyTime){
						lastModifyTime = lastModify;
						lstModifyIndex = i;
					}
				}
				result = filterFiles[lstModifyIndex];
				
			}
		}else{
			LOGGER.error("## 文件["+dir+"]不是目录或者不存在。");
		}
		return result;
	}
	private static final Logger LOGGER = Logger.getLogger(FileUtil.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat data_filename_sdf = new SimpleDateFormat("yyyyMMddHHmm");
	
}
