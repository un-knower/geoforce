package com.supermap.egispservice.pathplan.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.Recordset.BatchEditor;
import com.supermap.egispservice.pathplan.constant.Config;
@Component
public class UpdateDataPathAnalystEngine extends BasePathAnalystEngine {
	public String fromToEndIsProhibited;
	@Autowired
	private Config config;
	private final Logger logger=Logger.getLogger(UpdateDataPathAnalystEngine.class);
	
	@PostConstruct
	private void initConfig(){
		this.fromToEndIsProhibited = config.getFromToEndIsProhibited();
	}

	
	public void updateTurnTableFromCP(String cpDatasetName) {
		logger.info("开始更新转向表");

		DatasetVector cpDataset = (DatasetVector) this.datasets.get(cpDatasetName);
		if (cpDataset == null) {
			logger.error("CP 数据集为空，更新转向表失败。 cpDatasetName = " + cpDatasetName);
			return;
		}

		Map<String, CondTypeOne> map = new HashMap<String, CondTypeOne>();

		QueryParameter queryParameter = new QueryParameter();
		queryParameter.setCursorType(CursorType.STATIC);
		// 当condType==1时为禁止通行
		queryParameter.setAttributeFilter("CondType = '" + fromToEndIsProhibited + "'");
		queryParameter.setResultFields(new String[] { "outLinkID", "inLinkID", "CondType" });
		Recordset cpRecordset = cpDataset.query(queryParameter);

		int count = cpRecordset.getRecordCount();
		int index = 0;
		for (cpRecordset.moveFirst(); !cpRecordset.isEOF(); cpRecordset.moveNext()) {
			logger.info("cpDataset: total:" + count + ",current:" + index++ + "," + (double) index * 100 / count + "%");
			String inLinkID = cpRecordset.getString("inLinkID");
			String outLinkID = cpRecordset.getString("outLinkID");
			String condType = cpRecordset.getString("CondType");

			queryParameter = new QueryParameter();
			queryParameter.setCursorType(CursorType.STATIC);
			queryParameter.setAttributeFilter(String.format("ID in(%s,%s)", inLinkID, outLinkID));
			queryParameter.setResultFields(new String[] { "ID", "SmEdgeID", "SMFnode", "SMTnode" });
			Recordset networkRecordset = networkDataset.query(queryParameter);
			String FEdgeID = networkRecordset.getString("SmEdgeID");
			String nodeID = networkRecordset.getString("SMTnode");
			networkRecordset.moveNext();
			String TEdgeID = networkRecordset.getString("SmEdgeID");
			networkRecordset.close();
			networkRecordset.dispose();

			// 临时保存
			CondTypeOne one = new CondTypeOne(nodeID, FEdgeID, TEdgeID, condType);
			String key = String.format("%s-%s-%s", nodeID, FEdgeID, TEdgeID);
			map.put(key, one);
		}
		cpRecordset.close();
		cpRecordset.dispose();
		long startTime = System.currentTimeMillis();
		logger.info(map.size() + " 条记录需要更新");

		QueryParameter turnTableParam = new QueryParameter();
		turnTableParam.setCursorType(CursorType.DYNAMIC);
		Recordset turnTableRecordset = turnTableDataset.query(turnTableParam);
		BatchEditor editor = turnTableRecordset.getBatch();
		editor.begin();
		turnTableRecordset.moveFirst();
		count = turnTableRecordset.getRecordCount();
		index = 0;
		while (!turnTableRecordset.isEOF()) {
			logger.info("turnTableRecordset:total:" + count + ",current:" + index++ + "," + (double) index * 100 / count + "%");
			String nodeID_TurnTable = turnTableRecordset.getString("NodeID");
			String FEdgeID_TurnTable = turnTableRecordset.getString("FEdgeID");
			String TEdgeID_TurnTable = turnTableRecordset.getString("TEdgeID");
			// 这里构建只能这样构建key表示从弧段FEdgeID到TEdgeID的走向
			String key = String.format("%s-%s-%s", nodeID_TurnTable, FEdgeID_TurnTable, TEdgeID_TurnTable);
			CondTypeOne one = map.get(key);
			if (one != null) {
				String nodeID = one.getNodeID();
				String FEdgeID = one.getfEdgeID();
				String TEdgeID = one.gettEdgeID();
				// 更新转向表的值
				if (nodeID.equals(nodeID_TurnTable) && FEdgeID_TurnTable.equals(FEdgeID) && TEdgeID_TurnTable.equals(TEdgeID)) {
					// -1表示禁行
					int value = -1;
					turnTableRecordset.setFieldValue("TurnCost", value);
				} else {
					// 设置为默认值0
					int value = 0;
					turnTableRecordset.setFieldValue("TurnCost", value);
				}
			} else {
				// 设置为默认值0
				int value = 0;
				turnTableRecordset.setFieldValue("TurnCost", value);
			}
			turnTableRecordset.moveNext();
		}
		editor.update();
		turnTableRecordset.close();
		turnTableRecordset.dispose();
		logger.info("转向表更新完成,耗时[秒]:" + (System.currentTimeMillis() - startTime) / 1000.0);
	}

	/**
	 * 将高架桥的direction更新为负数,原来的direction为2 3 都表示可以通行 如果勾选不允许走高架桥时需要将Elevated==1的direction改为负数 但是勾选高架桥后需要还原,所以将direction-4还原时改为 direction+4
	 * 并且在加减的时候必选线判断原来的值是为正还是为负
	 * 
	 * @param networkDataset
	 * @param isSelectElevated
	 *            是否选择高架桥
	 */
	private void updateDirectionWhenChangeElevated(DatasetVector networkDataset, boolean prohibitViaduct) {
		logger.info("开始更新高架桥禁行信息");
		QueryParameter query = new QueryParameter();
		query.setHasGeometry(false);
		query.setCursorType(CursorType.DYNAMIC);
		query.setResultFields(new String[] { "direction", "elevated" });
		query.setAttributeFilter("direction in('2','3','-2','-1') AND elevated='1' ");

		Recordset res = networkDataset.query(query);
		if (res.isEmpty()) {
			res.dispose();
			throw new RuntimeException("can't find recordset for:elevated = 1");
		}
		BatchEditor editor = res.getBatch();
		editor.begin();
		res.moveFirst();
		while (!res.isEOF()) {
			int direction = res.getInt32("direction");
			// 已经更新为高架桥禁行
			if (direction < 0) {
				// 此时选择高架时需要将被更新为负数的值还原
				if (!prohibitViaduct) {
					res.setFieldValue("direction", direction + 4);
				}
			} else {
				// 如果不选择高架需要将direction-4 表示禁行
				if (prohibitViaduct) {
					res.setFieldValue("direction", direction - 4);
				}
			}
			res.moveNext();
		}
		editor.update();
		res.dispose();
		logger.info("更新高架桥禁行信息完成,是否禁止走高架 :" + prohibitViaduct);
	}



}
