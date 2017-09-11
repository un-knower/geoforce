package com.supermap.egisp.ts.constants;

import java.util.Properties;

import com.supermap.utils.AppPropertiesUtil;

public class TSConstants {

	private static Properties prop = AppPropertiesUtil.readPropertiesFile("ts.properties", TSConstants.class);
	
	
	public static String NETWORK_DATASOURCE_PATH = prop.getProperty("ts.network.datasource.path");
	
	
	public static String NETWORK_DATASET_NAME = prop.getProperty("ts.network.dataset.name");
	
	public static String EDGE_ID_NAME = prop.getProperty("field.edge.id.name");
	public static String NODE_ID_NAME = prop.getProperty("field.node.id.name");
	public static String FNODE_ID_NAME = prop.getProperty("field.fnode.id.name");
	public static String TNODE_ID_NAME = prop.getProperty("field.tnode.id.name");

	public static String API_DATASOURCE_URL = prop.getProperty("api.datasource.url");
	public static String API_USERNAME = prop.getProperty("api.username");
	public static String API_PASSWORD = prop.getProperty("api.password");
	public static String API_NETWORKNAME = prop.getProperty("api.networkname");
	public static String SWAP = prop.getProperty("swap");
	public static final int NETWORT_TYPE = Integer.parseInt(prop.getProperty("ts.type","2"));

	
	
}
