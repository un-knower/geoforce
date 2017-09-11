package com.supermap.egispservice.pathplan.util;

import javax.annotation.PostConstruct;

import com.supermap.data.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.analyst.networkanalyst.TransportationAnalyst;
import com.supermap.analyst.networkanalyst.TransportationAnalystParameter;
import com.supermap.analyst.networkanalyst.TransportationAnalystSetting;
import com.supermap.analyst.networkanalyst.WeightFieldInfo;
import com.supermap.analyst.networkanalyst.WeightFieldInfos;
import com.supermap.egispservice.pathplan.constant.Config;
@Component
public class BasePathAnalystEngine {
	@Autowired
	private Config config;
	
	private final Logger logger=Logger.getLogger(BasePathAnalystEngine.class);
	
	protected String workspacePath;
	protected String weightName;
	protected double tolerance;
	protected String networkName;
	protected String turnTableName;

	protected static Workspace workspace;
	protected static Datasets datasets;
	protected static DatasetVector networkDataset;
	protected static DatasetVector turnTableDataset;
	protected static TransportationAnalyst transportationAnalyst;
	

	@SuppressWarnings("unused")
	@PostConstruct
	private void initConfig(){
		this.workspacePath = config.getWorkspacePath();
		this.networkName = config.getNetworkName();
		this.turnTableName = config.getTurnTableName();
		this.weightName = config.getWeightName();
		this.tolerance = config.getTolerance();
	}

	public boolean init() {
		logger.info("开始--初始化object-java:udb");
		long startTime = System.currentTimeMillis();
		BasePathAnalystEngine.workspace = new Workspace();
		WorkspaceConnectionInfo workspaceConnectionInfo = new WorkspaceConnectionInfo();
		workspaceConnectionInfo.setType(WorkspaceType.SMWU);
		workspaceConnectionInfo.setServer(this.workspacePath);
		BasePathAnalystEngine.workspace.open(workspaceConnectionInfo);
		if (BasePathAnalystEngine.workspace.getDatasources().getCount() == 0) {
			logger.error("打开数据源失败");
			return false;
		}
		
		Datasource datasource = BasePathAnalystEngine.workspace.getDatasources().get(0);
		BasePathAnalystEngine.datasets = datasource.getDatasets();
		BasePathAnalystEngine.networkDataset = (DatasetVector) BasePathAnalystEngine.datasets.get(this.networkName);
		BasePathAnalystEngine.turnTableDataset = (DatasetVector) BasePathAnalystEngine.datasets.get(this.turnTableName);
		logger.info("网络数据集:" + this.networkName + " 转向表:" + this.turnTableName);
		//单个数据集 
		BasePathAnalystEngine.transportationAnalyst = createFindPathAnalyst(networkDataset, turnTableDataset);
		
		long endTime = System.currentTimeMillis();
		logger.info("完成--初始化object-java" + (endTime - startTime) / 1000.0);
		return (transportationAnalyst != null);
	}

	public boolean initOracle(){
		logger.info("开始--初始化object-java:oracle");
		long startTime = System.currentTimeMillis();
		BasePathAnalystEngine.workspace = new Workspace();

		DatasourceConnectionInfo info = new DatasourceConnectionInfo();
		info.setEngineType(EngineType.ORACLEPLUS);
		info.setServer(config.getApiDataSourceUrl());
		info.setUser(config.getApiUsername());
		info.setPassword(config.getApiPassword());

		// 打开数据源
		Datasource datasource = workspace.getDatasources().open(info);
		if (datasource == null) {
			logger.error("初始化数据源失败");
			logger.error(info.getEngineType());
			logger.error(info.getServer());
			logger.error(info.getUser());
			logger.error(info.getPassword());
		} else if (datasource.getDatasets().getCount() < 1) {
			logger.error("没有数据集信息,数据集数量:" + datasource.getDatasets().getCount());
			logger.error(info.getEngineType());
			logger.error(info.getServer());
			logger.error(info.getUser());
			logger.error(info.getPassword());
		} else {
			datasource.setAutoConnect(true);
			logger.debug(info.getServer() + " 连接成功");
		}
		BasePathAnalystEngine.datasets = datasource.getDatasets();
		BasePathAnalystEngine.networkDataset = (DatasetVector) BasePathAnalystEngine.datasets.get(config.getApiNetWorkName());
		BasePathAnalystEngine.turnTableDataset = (DatasetVector) BasePathAnalystEngine.datasets.get(config.getApiTurnTableName());
		logger.info("网络数据集:" + config.getApiNetWorkName() + " 转向表:" + config.getApiTurnTableName());
		//单个数据集
		BasePathAnalystEngine.transportationAnalyst = createFindPathAnalyst(networkDataset, turnTableDataset);

		long endTime = System.currentTimeMillis();
		logger.info("完成--初始化object-java" + (endTime - startTime) / 1000.0);
		return (transportationAnalyst != null);
	}

	public TransportationAnalyst getTransportationAnalyst(){
		if(transportationAnalyst == null && config.getSwap()==1){
			init();
		}else if(transportationAnalyst == null && config.getSwap()==2){
			initOracle();
		}
		return transportationAnalyst;
	}
	
	/**
	 * 创建分析参数
	 * 
	 * @param orderPoints
	 *            途经点
	 * @param withDetail
	 *            是否返回道路详细信息
	 * @return
	 */
	protected TransportationAnalystParameter createTransportAnalysParam(Point2Ds orderPoints, boolean withDetail,int weightNameIndex) {
		TransportationAnalystParameter parameter = new TransportationAnalystParameter();

		// 设置障碍点及障碍边
		int[] barrierEdges = new int[0];
		parameter.setBarrierEdges(barrierEdges);

		int[] barrierNodes = new int[0];
		parameter.setBarrierNodes(barrierNodes);

		parameter.setWeightName(config.getWeightName().split(",")[weightNameIndex]);

		parameter.setPoints(orderPoints);

		// 设置最佳路径分析的返回对象
		parameter.setRoutesReturn(true);
		parameter.setEdgesReturn(withDetail);
		parameter.setNodesReturn(withDetail);
		parameter.setPathGuidesReturn(withDetail);
		parameter.setStopIndexesReturn(withDetail);

		return parameter;
	}

	/**
	 * 构建分析对象
	 * 
	 * @param networkDataset
	 * @param turnTableDataset
	 * @return
	 */
	protected TransportationAnalyst createFindPathAnalyst(DatasetVector networkDataset, DatasetVector turnTableDataset) {
		try {
			// 设置网络分析基本环境，这一步骤需要设置　分析权重、节点、弧段标识字段、容限
			TransportationAnalystSetting setting = new TransportationAnalystSetting();
			setting.setNetworkDataset(networkDataset);
			setting.setEdgeIDField("SmEdgeID");//
			setting.setNodeIDField("SmNodeID");
			setting.setFNodeIDField("SmFNode");
			setting.setTNodeIDField("SmTNode");
			setting.setEdgeNameField("PathName");
			setting.setTolerance(tolerance);

			// 设置转向表
			if (turnTableDataset != null) {
				setting.setTurnDataset(turnTableDataset);
				setting.setTurnNodeIDField("NodeID");
				setting.setTurnFEdgeIDField("FEdgeID");
				setting.setTurnTEdgeIDField("TEdgeID");
				setting.setTurnWeightFields(new String[] { "TurnCost" });
			} else {
				logger.warn("转向表数据集为null");
			}

			// 设置交通规则
			setting.setRuleField(config.getDirection());
			// 正向单行线的字符串的数组
			setting.setFTSingleWayRuleValues(new String[] { "2" });
			// 逆向单行线的字符串的数组。
			setting.setTFSingleWayRuleValues(new String[] { "3" });
			// 设置双向行驶的值
			setting.setTwoWayRuleValues(new String[] { "0", "1" });
			// 设置禁行值
			setting.setProhibitedWayRuleValues(new String[] { "-1" });
//			setting.setProhibitedWayRuleValues(new String[] { "-2", "-1" });

			// 四维数据里length字段代表距离，单位是公里
			String [] weightNames=this.weightName.split(",");
			WeightFieldInfos weightFieldInfos = new WeightFieldInfos();
			for (int i = 0; i < weightNames.length; i++) {
				WeightFieldInfo weightFieldInfo = new WeightFieldInfo();
				String weight=weightNames[i].trim();
				weightFieldInfo.setFTWeightField(weight);
				weightFieldInfo.setTFWeightField(weight);
				weightFieldInfo.setName(weight);
				weightFieldInfos.add(weightFieldInfo);
			}
			setting.setWeightFieldInfos(weightFieldInfos);

			// 构造交通网络分析对象，加载环境设置对象
			TransportationAnalyst analyst = new TransportationAnalyst();
			analyst.setAnalystSetting(setting);
			logger.info("开始加载环境设置对象");
			long startTime = System.currentTimeMillis();
			boolean success = analyst.load();
			long endTime = System.currentTimeMillis();
			logger.info("完成加载环境设置对象,是否成功："+success+"， 时间：" + (endTime - startTime) / 1000.0);
			if (!success) {
				analyst = null;
			}

			return analyst;
		} catch (Exception ex) {
			throw new RuntimeException("构建路径分析对象:TransportationAnalyst失败", ex);
		}
	}

	/**
	 * 释放资源
	 */
	public synchronized static void destoryObjectJava() {
		if (workspace != null) {
			workspace.close();
			workspace.dispose();
		}
	}

}
