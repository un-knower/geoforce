package com.supermap.egispservice.area.utils;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.data.Workspace;
import com.supermap.utils.AppPropertiesUtil;

/**
 * supermap objects 帮助类
 * 
 * @author changwei
 * 
 */

@Component("areaHelper")
public class AreaHelper {

	/**
	 * 工作空间
	 */
	private Workspace workspace = null;
	/**
	 * 数据源
	 */
	private Datasource datasource = null;
	/**
	 * 数据源URL
	 */
	static String DATASOURCE_URL = null;
	/**
	 * 数据库名称
	 */
	static String DATABASE = null;
	/**
	 * DB Driver
	 */
	static String DBDRIVER = null;
	/**
	 * DB URL
	 */
	static String DBURL = null;
	/**
	 * 用户名
	 */
	static String DBUSER = null;
	/**
	 * 密码
	 */
	static String DBPASSWORD = null;
	
	/**
	 * 数据源类型
	 * 1 oracle
	 * 2 udb
	 */
	static String  DATASOURCE_TYPE=null;
	/**
	 * UDB数据源URL
	 */
	static String DATASOURCE_URL_UDB = null;
	
	private static Logger LOGGER = Logger.getLogger(AreaHelper.class);

	static {
		try {

			Properties prop = AppPropertiesUtil.readPropertiesFile("config.properties", AreaHelper.class);
			DATASOURCE_URL = prop.getProperty("DATASOURCE_URL");
			DATABASE = prop.getProperty("DATABASE");
			DBDRIVER = prop.getProperty("DBDRIVER");
			DBURL = prop.getProperty("DBURL");
			DBUSER = prop.getProperty("USERNAME");
			DBPASSWORD = prop.getProperty("PASSWORD");
			DATASOURCE_TYPE=prop.getProperty("DATASOURCE_TYPE");
			DATASOURCE_URL_UDB=prop.getProperty("DATASOURCE_URL_UDB");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	
	
	/**
	 * 初始化AreaHelper
	 * 
	 * @param workspaceUrl
	 * @param workspaceType
	 */
	public AreaHelper() {
		workspace = new Workspace();
		// 初始化数据源连接信息
		DatasourceConnectionInfo info = new DatasourceConnectionInfo();
		if(null!=DATASOURCE_TYPE&&DATASOURCE_TYPE.equals("2")){
			info.setEngineType(EngineType.UDB);
			info.setServer(DATASOURCE_URL_UDB);
//			info.setReadOnly(true);
		}else{
			//info.setEngineType(EngineType.ORACLEPLUS);
			info.setEngineType(EngineType.MYSQL);
			info.setServer(DATASOURCE_URL);
			info.setDatabase(DATABASE);
			info.setUser(DBUSER);
			info.setPassword(DBPASSWORD);
		}
		// 打开数据源
		datasource = workspace.getDatasources().open(info);
		// 获取矢量数据集
		// datasetVector = (DatasetVector) datasource.getDatasets().get(
		// datasetName);
		if (datasource == null) {
			System.out.println("初始化数据源失败");
			System.out.println(info.getEngineType());
			System.out.println(info.getServer());
			System.out.println(info.getUser());
			System.out.println(info.getPassword());
		} else if (datasource.getDatasets().getCount() < 1) {
			System.out.println("没有数据集信息,数据集数量:" + datasource.getDatasets().getCount());
		} else {
			datasource.setAutoConnect(true);
		}
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	

	/**
	 * 关闭工作空间
	 */
	public void closeWorkspace() {
		closeDatasources();
		workspace.close();
		workspace.dispose();
	}

	

	/**
	 * 关闭数据集合
	 */
	public void closeDatasources() {
		workspace.getDatasources().closeAll();
	}

	/**
	 * 获得一个数据集
	 * 
	 * @param datasetName
	 * @return
	 */
	public DatasetVector getDatasetVector(String datasetName) {
		if (!datasource.isConnected()) {
			datasource.connect();
		}
		return (DatasetVector) datasource.getDatasets().get(datasetName);
	}

	public static void closeRecordset(Recordset... recordsets) {
		for (int i = 0; i < recordsets.length; i++) {
			if (recordsets[i] != null) {
				recordsets[i].close();
				recordsets[i].dispose();
			}
		}
	}

	public static void closeRecordset(Recordset[] recordsets, Recordset... recordsets2) {
		closeRecordset(recordsets2);
		for (int i = 0; i < recordsets.length; i++) {
			closeRecordset(recordsets[i]);
		}
	}

	public static void closeGeometry(Geometry... geos) {
		for (int i = 0; i < geos.length; i++) {
			if (geos[i] != null) {
				geos[i].dispose();
			}
		}
	}
	
	
	
}
