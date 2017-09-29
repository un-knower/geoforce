package com.supermap.egispservice.area.utils;

import com.supermap.entity.PageEntity;
import com.supermap.utils.AppPropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public class DBUtil {
	private static Logger LOGGER = Logger.getLogger(DBUtil.class);

	private static long currentCount = 0L;
	private static Connection conn;
	private static PreparedStatement pStat;
	private static Properties prop = AppPropertiesUtil.readPropertiesFile("config.properties", DBUtil.class);
	private static long start;
	private static long count;

	static {
		openConnection();

		start = Long.parseLong(prop.getProperty("START", "0"));
		count = Long.parseLong(prop.getProperty("COUNT", "10000"));
	}

	private static void openConnection() {
		try {
			Class.forName(prop.getProperty("DB_DRIVER"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("DB_USERNAME"),
					prop.getProperty("DB_PASSWORD"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createPreparedStatement(String preparedSQL) {
		LOGGER.info("## 创建预定义的查询语句 : " + preparedSQL);
		if (conn == null)
			return;
		try {
			pStat = conn.prepareStatement(preparedSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Map<String, Object>> queryByPreparedStatement(String[] paramTypes, String[] paramValues,
			String[] returnFields) {
		List resultList = null;
		try {
			boolean isAssembleSuccess = assemblePSByValue(pStat, paramTypes, paramValues);

			if (isAssembleSuccess) {
				resultList = new ArrayList();
				ResultSet resultSet = pStat.executeQuery();
				while (resultSet.next()) {
					Map record = new HashMap();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < returnFields.length; ++i) {
						String itemValue = resultSet.getString(returnFields[i]);
						record.put(returnFields[i], itemValue);
						sb.append(itemValue + ",");
					}

					System.out.println(sb.toString());
					resultList.add(record);
				}
			} else {
				LOGGER.info("## 装配参数失败，未能执行查询");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (resultList != null) {
			currentCount += resultList.size();
		}
		System.out.println("currentCount = " + currentCount);
		return resultList;
	}

	private static boolean assemblePSByValue(PreparedStatement stat, String[] paramTypes, String[] paramValues)
			throws NumberFormatException, SQLException {
		boolean isAssembleSuccess = false;
		if (stat != null) {
			for (int i = 0; i < paramTypes.length; ++i) {
				if (paramTypes[i].equalsIgnoreCase("int")) {
					stat.setInt(i + 1, Integer.parseInt(paramValues[i]));
				} else if (paramTypes[i].equalsIgnoreCase("string")) {
					stat.setString(i + 1, paramValues[i]);
				} else if (paramTypes[i].equalsIgnoreCase("float")) {
					stat.setFloat(i + 1, Float.parseFloat(paramValues[i]));
				} else if (paramTypes[i].equalsIgnoreCase("long")) {
					stat.setLong(i + 1, Long.parseLong(paramValues[i]));
				} else {
					if (!(paramTypes[i].equalsIgnoreCase("double")))
						continue;
					stat.setDouble(i + 1, Double.parseDouble(paramValues[i]));
				}
			}
			isAssembleSuccess = true;
		} else {
			LOGGER.info("## 传入的预定义对象为空，不能进行参数的装配");
		}
		return isAssembleSuccess;
	}

	public static void closePStatement() {
		if (pStat == null)
			return;
		try {
			pStat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pStat = null;
		}
	}

	public static void closeConnection() {
		if (conn != null)
			synchronized (conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					conn = null;
				}
			}
	}

	public static synchronized Connection getConnection() {
		if (conn == null) {
			openConnection();
		}
		return conn;
	}

	public static synchronized PageEntity getPage() {
		PageEntity entity = new PageEntity();
		entity.setStart(start * count);
		entity.setEnd(start * count + count - 1L);
		start += 1L;
		LOGGER.info("## current progress = " + entity.getEnd());
		return entity;
	}
}