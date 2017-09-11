package com.supermap.egisp.ts;

import org.apache.log4j.Logger;

import com.supermap.analyst.networkanalyst.TransportationAnalyst;
import com.supermap.analyst.networkanalyst.TransportationAnalystSetting;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.egisp.ts.constants.TSConstants;

public class TransportationAnalystManager {

	private TransportationAnalystManager(){}
	
	private static TransportationAnalyst  analyst = null;
	
	private static Datasource datasource = null;
	
	private  static Logger LOGGER = Logger.getLogger(TransportationAnalystManager.class);

	private static volatile boolean IS_ANALYST_INIT_SUCCESS = false;
	
	static{
		if(TSConstants.SWAP.equals("1")){//UDB
			init();
		}else if(TSConstants.SWAP.equals("2")){//ORACLE
			initDB();
		}
	}
	
	private static void init(){
		LOGGER.info("## 初始化路劲分析器。。。");
		// 加载网络数据集
		loadNetworkDataset();
		Dataset dataset = null;
		if(datasource != null){
			dataset = datasource.getDatasets().get(TSConstants.NETWORK_DATASET_NAME);
			if(null != dataset){
				LOGGER.info("## 创建TransportationAnalystSetting。。。");
				TransportationAnalystSetting setting = buildAnalystSetting(dataset);
				analyst = new TransportationAnalyst();
				analyst.setAnalystSetting(setting);
				LOGGER.info("## TransportationAnalyst 开始执行加载。。。");
				IS_ANALYST_INIT_SUCCESS = analyst.load();
				LOGGER.info("## 加载TransportationAnalyst "+(IS_ANALYST_INIT_SUCCESS?"成功":"失败"));
			}else{
				LOGGER.warn("## 未找到网络数据集["+TSConstants.NETWORK_DATASET_NAME+"]");
			}
		}else{
			LOGGER.warn("## 初始化数据源失败["+TSConstants.NETWORK_DATASOURCE_PATH+"]");
		}
	}

	private static void initDB(){
		LOGGER.info("## 初始化路劲分析器。。。");
		// 加载网络数据集
		loadNetworkDatasetForDB();
		Dataset dataset = null;
		if(datasource != null){
			dataset = datasource.getDatasets().get(TSConstants.API_NETWORKNAME);
			if(null != dataset){
				LOGGER.info("## 创建TransportationAnalystSetting。。。");
				TransportationAnalystSetting setting = buildAnalystSetting(dataset);
				analyst = new TransportationAnalyst();
				analyst.setAnalystSetting(setting);
				LOGGER.info("## TransportationAnalyst 开始执行加载。。。");
				IS_ANALYST_INIT_SUCCESS = analyst.load();
				LOGGER.info("## 加载TransportationAnalyst "+(IS_ANALYST_INIT_SUCCESS?"成功":"失败"));
			}else{
				LOGGER.warn("## 未找到网络数据集["+TSConstants.API_NETWORKNAME+"]");
			}
		}else{
			LOGGER.warn("## 初始化数据源失败["+TSConstants.API_DATASOURCE_URL+"]");
		}
	}

	
	public static TransportationAnalyst getAnalyst(){
		return analyst;
	}
	
	/**
	 * 
	 * <p>Title ：isAnalystInitSuccess</p>
	 * Description：		只有该方法返回true表示Analyst可用
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-16 下午05:17:12
	 */
	public static boolean isAnalystInitSuccess(){
		return IS_ANALYST_INIT_SUCCESS;
	}

	/**
	 * 
	 * <p>Title ：buildAnalystSetting</p>
	 * Description：		构建网络分析设置
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-16 下午03:27:50
	 */
	private static TransportationAnalystSetting buildAnalystSetting(Dataset dataset) {
		TransportationAnalystSetting transportationAnalystSetting = new TransportationAnalystSetting();
		transportationAnalystSetting.setNetworkDataset((DatasetVector) dataset);
		transportationAnalystSetting.setEdgeIDField(TSConstants.EDGE_ID_NAME);
		transportationAnalystSetting.setNodeIDField(TSConstants.NODE_ID_NAME);
		transportationAnalystSetting.setFNodeIDField(TSConstants.FNODE_ID_NAME);
		transportationAnalystSetting.setTNodeIDField(TSConstants.TNODE_ID_NAME);
		if(TSConstants.NETWORT_TYPE == 1){
			transportationAnalystSetting.setTolerance(100);
		}else{
			transportationAnalystSetting.setTolerance(0.01d);
		}
		
		// WeightFieldInfo weightInfo = new WeightFieldInfo();
		// weightInfo.setFTWeightField ( "smLength");
		// weightInfo.setTFWeightField ( "smLength");
		// transportationAnalystSetting.getWeightFieldInfos().add(weightInfo);
		return transportationAnalystSetting;
	}


	/**
	 * 
	 * <p>Title ：loadNetworkDataset</p>
	 * Description：		加载网络数据集，只支持
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-16 下午02:52:12
	 */
	private static void loadNetworkDataset() {
		Workspace workspace = new Workspace();
		Datasources datasources = workspace.getDatasources();
		DatasourceConnectionInfo dci = new DatasourceConnectionInfo();
		dci.setAutoConnect(true);
		dci.setReadOnly(true);
		dci.setEngineType(EngineType.UDB);
		dci.setServer(TSConstants.NETWORK_DATASOURCE_PATH);
		datasource = datasources.open(dci);
		
	}

	private static void loadNetworkDatasetForDB() {
		Workspace workspace = new Workspace();
		Datasources datasources = workspace.getDatasources();
		DatasourceConnectionInfo dci = new DatasourceConnectionInfo();
		dci.setAutoConnect(true);
		dci.setReadOnly(true);
		dci.setEngineType(EngineType.ORACLEPLUS);
		dci.setServer(TSConstants.API_DATASOURCE_URL);
		dci.setUser(TSConstants.API_USERNAME);
		dci.setPassword(TSConstants.API_PASSWORD);
		datasource = datasources.open(dci);
	}

}
