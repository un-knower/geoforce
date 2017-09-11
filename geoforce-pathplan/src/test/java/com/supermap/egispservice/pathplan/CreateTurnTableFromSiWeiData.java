package com.supermap.egispservice.pathplan;

import java.util.Arrays;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.Recordset.BatchEditor;

/**
 * 
 * 
 * Title 将四维的数据转换为supermap的数据<br>
 * 将四维的数据转换为supermap的数据 <br>
 * 在加载的时候还需要设置转向信息，这就需要使用到C表。C表中找到CondType字段值为1的记录，将这些数据另存为一个纯属性表数据集中。
 * 创建一个新的CondType1字段
 * ，注意类型为32位整型，将该字段全部设置为-1，再创建三个字段，分别为NodeID，FEdgeID和TEdgeID字段，字段类型都为32位整型。
 * 开始设置转向表，需要写段程序实现，实现步骤下面开始描述。
 * 取一条记录，使用C表中原始的字段inLinkID的值在网络数据集中查找与ID字段中相同的值的记录，
 * 将对应的SmEdgeID，SmFNode和SmTNode字段的值
 * ，同样查找outLinkID在网络数据中的SmEdgeID，SmFNode和SmTNode字段的值
 * ，然后向该记录的NodeID，FEdgeID和TEdgeID字段中填值
 * ，其中FEdgeID为inLinkID查询出来SmEdgeID中的值，TEdgeID为outLinkID查询中的SmEdgeID的值
 * ，而NodeID的值为inLinkID与outLinkID查询出来的SmFNode和SmTNode值中相同的值
 * ，可以参考下图。依次设置完属性表中的记录。加载的时候设置转向信息（转向表的具体使用请参考帮助文档）。
 * 
 * @Company SuperMap Software Co., Ltd.<br>
 * @Copyright Copyright (c) 2013<br>
 * 
 * @version 1.0.0,2014-6-30
 * @since JDK1.6+
 * @author caozhongping
 * 
 */
public class CreateTurnTableFromSiWeiData {

	public static void genertor(DatasetVector dataset,DatasetVector siWeiDataset) {
		// Datasource datasource =
		// datasources.open(getDatasourceConnectionInfo());
		// DatasetVector dataset = (DatasetVector)
		// datasource.getDatasets().get(networkDatasetName);//网络数据集
		// DatasetVector datasetTurn = (DatasetVector)
		// datasource.getDatasets().get(siWeiRtable);//转向表数据集 “NewDataset_1”为
		// C表中找到CondType字段值为1的记录，将这些数据另存为一个纯属性表数据集中

		QueryParameter parameter = new QueryParameter();
		parameter.setCursorType(CursorType.DYNAMIC);
		Recordset recordset = siWeiDataset.query(parameter);
		recordset.moveFirst();
		BatchEditor editor = recordset.getBatch();
		editor.begin();
		System.out.println("start");
		int count = 0;
		
		while (!recordset.isEOF()) {
			if (count % 100 == 0) {
				System.out.println("count:" + count);
			}
			
			Object obj = recordset.getFieldValue("inLinkID");
			String in = obj.toString();
			obj = recordset.getFieldValue("outLinkID");
			String out = obj.toString();
			
			QueryParameter para = new QueryParameter();
			para.setCursorType(CursorType.STATIC);
			para.setHasGeometry(false);
			para.setAttributeFilter("ID = " + in + " Or ID = " + out);
			Recordset recordset1 = dataset.query(para);
			recordset1.moveFirst();
			String[] strs = new String[4];
			int n = 0;
			
			while (!recordset1.isEOF()) {
				obj = recordset1.getFieldValue("ID");
				if (obj.toString().compareToIgnoreCase(in) == 0) {
					obj = recordset1.getFieldValue("SmEdgeID");
					recordset.setFieldValue("FEdgeID", obj);
					obj = recordset1.getFieldValue("SmFNode");
					strs[n] = obj.toString();
					n++;
					obj = recordset1.getFieldValue("SmTNode");
					strs[n] = obj.toString();
				} else if (obj.toString().compareToIgnoreCase(out) == 0) {
					obj = recordset1.getFieldValue("SmEdgeID");
					recordset.setFieldValue("TEdgeID", obj);
					obj = recordset1.getFieldValue("SmFNode");
					strs[n] = obj.toString();
					n++;
					obj = recordset1.getFieldValue("SmTNode");
					strs[n] = obj.toString();
				}
				recordset1.moveNext();
				n++;
			}
			recordset1.dispose();
			if (n != 4) {
				recordset.moveNext();
				System.out.println("n != 4");
				continue;
			}
			
			boolean bFound = false;
			for (int j = 0; j < 4; j++) {
				if (bFound) {
					break;
				}
				for (int k = j+1; k < 4; k++) {
					if (strs[j].compareToIgnoreCase(strs[k]) == 0) {
						obj = strs[j];
						recordset.setFieldValue("NodeID", obj);
						bFound = true;
						break;
					}
				}
			}
			if (!bFound) {
				String nodeId = Arrays.toString(strs);
				System.out.println("not found ID = " + in + " Or ID = " + out+" arr:"+nodeId);
				recordset.moveNext();
				continue;
			}
			recordset.moveNext();
			count++;
		}
		System.out.println("over");
		editor.update();
		recordset.dispose();
	}
}
