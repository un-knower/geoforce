package com.supermap.egispservice.area.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.FieldInfos;
import com.supermap.data.GeoLine;
import com.supermap.data.Recordset;
import com.supermap.data.Workspace;


/**
 * 
 * <p>Title: DatasetUtils</p>
 * Description:		数据集工具类
 *
 * @author Huasong Huang
 * CreateTime: 2014-12-9 下午03:05:16
 */
public class DatasetUtils {

	private static final Logger logger = Logger.getLogger(DatasetUtils.class);

	public static Map<String, Object> getAttibuteMap(Recordset originalRecordset, String[] include) {
		// 保存属性
		FieldInfos fieldInfos = originalRecordset.getFieldInfos();
		Map<String, Object> attibutesMap = new HashMap<String, Object>();
		if (include == null) {
			return attibutesMap;
		}
		int fieldCount = fieldInfos.getCount();
		for (int i = 0; i < fieldCount; i++) {
			if (!fieldInfos.get(i).isSystemField()) {
				String nameString = fieldInfos.get(i).getName();

				// 判断是否在include列表里面
				boolean in = false;
				for (int j = 0; j < include.length; j++) {
					if (include[j].equalsIgnoreCase(nameString)) {
						in = true;
						break;
					}
				}
				if (!in) {
					continue;
				}

				Object valueObject = null;
				try {
					valueObject = originalRecordset.getFieldValue(nameString);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				// 空值判断 map不能有null值
				if (nameString != null && valueObject != null) {
					attibutesMap.put(nameString, valueObject);
				}
			}
		}
		return attibutesMap;
	}
	
	public synchronized static String recordsetToUDB(Recordset recordset, String outputDir) throws Exception {
		File dir = new File(outputDir);
		if (outputDir != null) {
			FileUtils.forceMkdir(dir);
		}
		String folder = CommonUtil.getUUID();
		dir = new File(dir, folder);
		dir.mkdir();
		String fullPath = dir.getAbsolutePath() + File.separator + "output.udb";

		// 创建数据源
		Workspace workspace = new Workspace();
		DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
		datasourceConnectionInfo.setEngineType(EngineType.UDB);
		datasourceConnectionInfo.setServer(fullPath);
		Datasources datasources = workspace.getDatasources();
		Datasource datsource = datasources.create(datasourceConnectionInfo);

		datsource.recordsetToDataset(recordset, recordset.getDataset().getName());
		workspace.save();
		workspace.close();
		workspace.dispose();

		String zipfilename = outputDir + File.separator + folder + ".zip";
		ZipExtend zip = new ZipExtend(zipfilename);
		zip.createZipOut();
		zip.packToolFiles(dir.getAbsolutePath(), "");
		zip.closeZipOut();

		return zipfilename;

	}
	

}
