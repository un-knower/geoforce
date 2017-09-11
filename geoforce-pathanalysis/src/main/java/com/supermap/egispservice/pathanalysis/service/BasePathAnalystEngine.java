package com.supermap.egispservice.pathanalysis.service;

import com.supermap.analyst.networkanalyst.*;
import com.supermap.data.*;
import com.supermap.egispservice.pathanalysis.constant.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BasePathAnalystEngine {
	@Autowired
	private Config config;
	
	private final Logger logger=Logger.getLogger(BasePathAnalystEngine.class);
	
	protected String weightName;
	protected double tolerance;

	protected static Workspace workspace;
	protected static Datasets datasets;
	protected static DatasetVector networkDataset;
	protected static DatasetVector turnTableDataset;
	protected static TransportationAnalyst transportationAnalyst;
	
	//API
	//protected String workspacePath;
	protected static Workspace APIworkspace;
	protected static Datasets APIdatasets;
	protected static DatasetVector APIdatasetVector;
	protected String apiworkspacePath;
	
	//protected String networkName;
	//protected String turnTableName;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initConfig(){
		this.weightName = config.getWeightName();
		this.tolerance = config.getTolerance();
		this.apiworkspacePath=config.getApiworkspacePath();
	}

	public boolean initOracle(){
		logger.info("开始--初始化object-java");
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
		if(transportationAnalyst == null){
			initOracle();
			//init();
		}
		return transportationAnalyst;
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
	public void destoryObjectJava() {
		if (this.workspace != null) {
			this.workspace.close();
			this.workspace.dispose();
		}
		if (this.APIworkspace != null) {
			this.APIworkspace.close();
			this.APIworkspace.dispose();
		}
		if(this.APIdatasetVector!=null){
			this.APIdatasetVector.close();
		}
	}
	
	//API的
	public boolean init(){
		logger.info("开始--初始化object-java");
		long startTime = System.currentTimeMillis();
		BasePathAnalystEngine.APIworkspace = new Workspace();
		WorkspaceConnectionInfo apiworkspaceConnectionInfo = new WorkspaceConnectionInfo();
		apiworkspaceConnectionInfo.setType(WorkspaceType.SMWU);
		apiworkspaceConnectionInfo.setServer(this.apiworkspacePath);
		BasePathAnalystEngine.APIworkspace.open(apiworkspaceConnectionInfo);
		if (BasePathAnalystEngine.APIworkspace.getDatasources().getCount() == 0) {
			logger.error("打开数据源失败");
			return false;
		}
		
		logger.info("APIworkspace获取："+(APIworkspace==null?"失败":"成功"));
		logger.info("APIworkspace::::"+APIworkspace.getDatasources().getCount());
		for(int i=0;i<APIworkspace.getDatasources().getCount();i++){
			logger.info("apidatasource---"+BasePathAnalystEngine.APIworkspace.getDatasources().get(i).getAlias());
		}
		
		Datasource apidatasource = BasePathAnalystEngine.APIworkspace.getDatasources().get(0);
		logger.info("apidatasource获取："+apidatasource.getAlias());
		BasePathAnalystEngine.APIdatasets = apidatasource.getDatasets();
		logger.info("APIdatasets获取："+(APIdatasets==null?"失败":"成功"));
		logger.info("APIdatasets::::"+APIdatasets.getCount()+"------"+APIdatasets.contains("NG_TA"));
		for(int i=0;i<APIdatasets.getCount();i++){
			logger.info("APIdatasets---"+APIdatasets.get(i).getName()+"....."+APIdatasets.get(i).getTableName());
		}
		BasePathAnalystEngine.APIdatasetVector = (DatasetVector) APIdatasets.get("NG_TA");
		long endTime = System.currentTimeMillis();
		logger.info("完成--初始化object-java" + (endTime - startTime) / 1000.0);
		return (null!=APIdatasetVector);
	}

}
