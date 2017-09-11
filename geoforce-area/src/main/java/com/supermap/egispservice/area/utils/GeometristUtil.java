package com.supermap.egispservice.area.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.supermap.analyst.spatialanalyst.OverlayAnalyst;
import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EncodeType;
import com.supermap.data.EngineType;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Workspace;
import com.supermap.data.topology.TopologyPreprocessOptions;
import com.supermap.data.topology.TopologyValidator;
import com.supermap.egispservice.area.constants.AreaFieldNames;

/**
 * 
 * Title 包装几何算法类<br>
 * Description <br>
 * 
 * @Company SuperMap Software Co., Ltd.<br>
 * @Copyright Copyright (c) 2013<br>
 * 
 * @version 1.0.0,2013-7-4
 * @since JDK1.6
 * @author zhangguoping QQ:346258091 mailto:zhangguoping@supermap.com
 * 
 */
public class GeometristUtil {

	private static final Logger logger = Logger.getLogger(GeometristUtil.class);

	// private static double TOLERANCE = 1.0;
	private static double LINE_TOLERANCE = 1.0;// 悬线容限
	private static double AREA_TOLERANCE = 1.0;// 碎面容限
	private static double TOPO_TOLERANCE = 1.0;// 拓扑容限

	/**
	 * 判断单个面外围线是否含有相交部分
	 * 
	 * @param region
	 *            面数据
	 * @return 含有返回true，不含有返回false
	 */
	public static boolean isCross(GeoRegion region) {
		boolean cross = false;

		int partcount = region.getPartCount();
		for (int i = 0; i < partcount; i++) {
			Point2Ds point2Ds = region.getPart(i);
			int count = point2Ds.getCount();

			boolean cross1 = false;
			for (int j = 0; j < count - 3; j++) {
				boolean cross0 = false;
				for (int k = j + 2; k < count - 1; k++) {

					// 第一点不用比较尾部，因为首尾相连
					if (j == 0 && k == count - 2) {
						continue;
					}

					GeoLine line1 = new GeoLine(new Point2Ds(new Point2D[] { point2Ds.getItem(j), point2Ds.getItem(j + 1) }));
					GeoLine line2 = new GeoLine(new Point2Ds(new Point2D[] { point2Ds.getItem(k), point2Ds.getItem(k + 1) }));
					cross = Geometrist.hasCross(line1, line2);
					// logger.debug("line[" + j + "-" + (j + 1) +
					// "] cross line[" + k + "-" + (k + 1) + "]:" + cross);

					// 跳出三层循环
					if (cross) {
						cross0 = true;
						cross1 = true;
						break;
					}
				}

				if (cross0) {
					break;
				}

			}

			if (cross1) {
				break;
			}

		}

		return cross;
	}

	/**
	 * 清理面<br>
	 * 去除连带小碎面，分离小碎面，内部小碎面，悬挂线，重复点
	 * 
	 * @param region
	 * @return 全部消除后返回null
	 */
	public static GeoRegion clean(GeoRegion region) {
		if (region == null) {
			return null;
		}

		long time1 = System.currentTimeMillis();
		// 去碎面
		region = cleanRegion(region);
		long time2 = System.currentTimeMillis();

		// 去悬线、重复点
		region = cleanLine(region);
		long time3 = System.currentTimeMillis();

		// 极端情况下，去悬线后会产生新的小碎面
		region = cleanRegion(region);
		long time4 = System.currentTimeMillis();

		logger.debug("去悬线:" + (time3 - time2) + "ms " + "去碎面:" + ((time2 - time1) + (time4 - time3)) + "ms");

		return region;
	}

	private static GeoRegion cleanRegion(GeoRegion region) {

		if (region == null) {
			return null;
		}

		// 去碎面
		GeoRegion newRegion = new GeoRegion();
		int partCount = region.getPartCount();
		for (int i = 0; i < partCount; i++) {
			Point2Ds point2Ds = null;
			try {
				point2Ds = region.getPart(i);
			} catch (Exception e) {
				// 空面
				continue;
			}

			int count = point2Ds.getCount();
			if (count < 4) {
				// 点数目异常面
				continue;
			}

			Point2D first = point2Ds.getItem(0);
			Point2D end = point2Ds.getItem(count - 1);
			// 非封闭面
			if (first.getX() != end.getX() || first.getY() != end.getY()) {
				// 非封闭面
				continue;
			}

			GeoRegion tempRegion = new GeoRegion(point2Ds);
			double area = tempRegion.getArea();
			if (area > AREA_TOLERANCE) {
				newRegion.addPart(point2Ds);
				if (i > 0) {
					// 可能是碎面
					logger.warn("发现飞地,且面积超过容限:" + area);
				}
			}
		}

		return newRegion;

	}

	/**
	 * 清理重复点，悬挂线
	 * 
	 * @param region
	 * @return
	 */
	private static GeoRegion cleanLine(GeoRegion region) {
		// 无效面，原样返回
		if (region == null || region.getPartCount() < 1) {
			return region;
		}

		int count = region.getPartCount();// 总part数
		logger.debug("## 区域面的part数："+count);
		for (int p = 0; p < count; p++) {
			// 原始面
			Point2Ds point2Ds = region.getPart(p);
			final Point2D[] point2DArray = point2Ds.toArray();

			// 转换成list处理
			ArrayList<Point2D> point2DList = new ArrayList<Point2D>(Arrays.asList(point2DArray));

			// 去掉重复点 相邻和非相邻的
			boolean hasRevertRegion = false;// 是否含有外部碎面
			do {

				if (hasRevertRegion) {
					logger.debug("处理外部碎面");
				}
				hasRevertRegion = false;

				int n = point2DList.size();// 总点数
				if (n < 4) {
					// 面至少有4个点（三角形）
					continue;
				}

				// step1 去掉point2DList相邻点
				for (int i = 0; i < n - 1; i++) {
					Point2D pointi = point2DList.get(i);
					Point2D pointi1 = point2DList.get(i + 1);
					if (isDuplicate(pointi, pointi1)) {
						// 小于等于容限 合并第i和第i+1点
						if ((i + 1) == (n - 1)) {
							// 尾部删除 避免开口
							logger.debug("尾部删除:" + i);
							point2DList.remove(i);

						} else {
							logger.debug("删除:" + i);
							point2DList.remove(i + 1);
						}
						// 重置i和n
						n--;
						i--;
					}
				}

				// step1 去掉point2DList相同点(非相邻)
				n = point2DList.size();// 总点数
				GeoRegion partRegion = new GeoRegion(new Point2Ds(point2DList.toArray(new Point2D[0])));
				double partArea = partRegion.getArea();
				lablehasRevertRegion: for (int i = 0; i <= n - 4; i++) {
					for (int j = i + 2; j <= n - 2; j++) {
						Point2D pointi = point2DList.get(i);
						Point2D pointj = point2DList.get(j);
						if (isDuplicate(pointi, pointj)) {
							// 第i点和第j点重复，去掉一边，保留另一边
							// 根据面积判断移除左边还是右边
							List<Point2D> subList = point2DList.subList(i, j + 1);
							GeoRegion ranGeoRegion = getRangeRegion(subList);
							if (ranGeoRegion != null && ranGeoRegion.getPartCount() > 0) {

								double rangeArea = ranGeoRegion.getArea();
								double percent = rangeArea / partArea;
								logger.debug(rangeArea + "/" + partArea + ":" + percent);
								if (percent < 0.05) {

									// 去掉相邻重复点 第i点与第j+1点
									Point2D pointj1 = point2DList.get(j + 1);
									int upbound = j + 1;// 删除下标
									while (upbound <= n - 2 && isDuplicate(pointi, pointj1)) {
										upbound++;
										pointj1 = point2DList.get(upbound);
									}
									logger.debug("delete内部碎面:" + (i + 1) + "到" + upbound);

									// 去掉内部 第i+1点到第upbound点
									point2DList.subList(i + 1, upbound).clear();

									// 重置j和n
									n = n - (upbound - i - 1);
									j = i + 2;

								} else {

									logger.debug("delete外部碎面:0-" + (i - 1) + " " + (j + 1) + "-" + n);
									// 去掉外部，生成新的面
									Point2Ds newPoint2Ds = ranGeoRegion.getPart(0);
									Point2D[] newPoint2DArray = newPoint2Ds.toArray();

									// 转换成list处理
									point2DList = new ArrayList<Point2D>(Arrays.asList(newPoint2DArray));

									// 重新开始处理
									hasRevertRegion = true;
									break lablehasRevertRegion;

								}
							}
						}

					}
				}
			} while (hasRevertRegion);

			// 去悬线
			int n = point2DList.size();// 总点数
			if (n > 4) {

				GeoRegion partRegion = new GeoRegion(new Point2Ds(point2DList.toArray(new Point2D[0])));
				double partArea = partRegion.getArea();

				// 正向
				hasRevertRegion = false;// 是否含有外部碎面
				do {

					if (hasRevertRegion) {
						logger.debug("处理正向外部碎面");
					}
					hasRevertRegion = false;

					// 查找断点
					n = point2DList.size();
					lablehasRevertRegion: for (int i = 0; i <= n - 3; i++) {
						for (int j = i + 1; j <= n - 2; j++) {
							// 第一点不用比较尾部，因为首尾相连
							if (i == 0 && j == n - 2) {
								continue;
							}

							// 判断第i点
							// 第j,j+1线是否相交，或者距离小于容限，如果满足条件，则将第i点插入第j,j+1点之间
							double distance = Geometrist.distanceToLineSegment(point2DList.get(i), point2DList.get(j), point2DList.get(j + 1));
							if (distance <= LINE_TOLERANCE) {
								logger.debug("发现正向断点:" + i + " " + j + "-" + (j + 1));
								// 移除i + 1 到 j
								if (j == (i + 1)) {

									logger.debug("delete:" + j);

									// 只有1个点不用计算面积
									point2DList.remove(j);

									// 重置ijn i没有变化
									j = i + 1;
									n = point2DList.size();// 总点数

								} else {
									// objectJava removeRange方法有bug，会删除多余点
									// point2Ds.removeRange(i + 1, j);

									// 移除悬线时需根据面积判断移除左边还是右边
									List<Point2D> subList = point2DList.subList(i, j + 1);
									GeoRegion ranGeoRegion = getRangeRegion(subList);
									if (ranGeoRegion != null && ranGeoRegion.getPartCount() > 0) {
										double rangeArea = ranGeoRegion.getArea();
										double percent = rangeArea / partArea;
										if (percent < 0.05) {

											logger.debug("delete内部碎面:" + (i + 1) + "到" + j);

											// 去掉内部 第i+1点到第j+1点
											point2DList.subList(i + 1, j + 1).clear();

											// 重置ijn i没有变化
											j = i + 1;
											n = point2DList.size();// 总点数

										} else {

											logger.debug("delete外部碎面:0-" + (i - 1) + " " + (j + 1) + "-" + n);
											// 去掉外部，生成新的面
											Point2Ds newPoint2Ds = ranGeoRegion.getPart(0);
											Point2D[] newPoint2DArray = newPoint2Ds.toArray();

											// 转换成list处理
											point2DList = new ArrayList<Point2D>(Arrays.asList(newPoint2DArray));

											// 重新开始处理
											hasRevertRegion = true;
											break lablehasRevertRegion;

										}
									}

								}
							}
						}

					}
				} while (hasRevertRegion);

				// 反向
				hasRevertRegion = false;// 是否含有外部碎面
				do {

					if (hasRevertRegion) {
						logger.debug("处理反向外部碎面");
					}
					hasRevertRegion = false;

					// 查找断点
					n = point2DList.size();
					lablehasRevertRegion: for (int i = n - 1; i >= 2; i--) {
						for (int j = i - 1; j >= 1; j--) {
							// 第一点不用比较尾部，因为首尾相连
							if (i == n - 1 && j == 1) {
								continue;
							}

							// 判断第i点
							// 第j-1,j线是否相交，或者距离小于容限，如果满足条件，则将第i点插入第j-1,j点之间
							double distance = Geometrist.distanceToLineSegment(point2DList.get(i), point2DList.get(j - 1), point2DList.get(j));
							if (distance <= LINE_TOLERANCE) {
								logger.debug("发现反向断点:" + (j - 1) + "-" + j + " " + i);

								// 移除i + 1 到 j
								if (j == (i - 1)) {

									logger.debug("delete:" + j);

									// 只有1个点不用计算面积
									point2DList.remove(j);

									// 重置ijn i没有变化
									j = i - 1;
									n = point2DList.size();// 总点数

								} else {
									// objectJava removeRange方法有bug，会删除多余点
									// point2Ds.removeRange(i + 1, j);

									// 移除悬线时需根据面积判断移除左边还是右边
									List<Point2D> subList = point2DList.subList(j - 1, i);
									GeoRegion ranGeoRegion = getRangeRegion(subList);
									if (ranGeoRegion != null && ranGeoRegion.getPartCount() > 0) {
										double rangeArea = ranGeoRegion.getArea();
										double percent = rangeArea / partArea;
										if (percent < 0.05) {

											logger.debug("delete内部碎面:" + (j - 1) + "到" + i);

											// 去掉内部 第i+1点到第j+1点
											point2DList.subList(j, i).clear();

											// 重置ijn i没有变化
											j = i + 1;
											n = point2DList.size();// 总点数

										} else {

											logger.debug("delete外部碎面:0-" + (j - 2) + " " + (i + 1) + "-" + n);
											// 去掉外部，生成新的面
											Point2Ds newPoint2Ds = ranGeoRegion.getPart(0);
											Point2D[] newPoint2DArray = newPoint2Ds.toArray();

											// 转换成list处理
											point2DList = new ArrayList<Point2D>(Arrays.asList(newPoint2DArray));

											// 重新开始处理
											hasRevertRegion = true;
											break lablehasRevertRegion;

										}
									}
								}
							}
						}
					}
				} while (hasRevertRegion);

			}

			// 判断是否发生变化，变化后需要重置覆盖
			if (point2DArray.length != point2DList.size()) {
				region.setPart(p, new Point2Ds(point2DList.toArray(new Point2D[0])));
			}

		}

		return region;

	}

	/**
	 * 判断是否是重复点
	 * 
	 * @param pointi
	 * @param pointj
	 * @return
	 */
	private static boolean isDuplicate(Point2D pointi, Point2D pointj) {
		double distance = Math.pow(Math.pow(pointi.getX() - pointj.getX(), 2) + Math.pow(pointi.getY() - pointj.getY(), 2), 0.5);
		if (distance <= LINE_TOLERANCE) {
			return true;
		}
		return false;
	}

	/**
	 * 返回子面
	 * 
	 * @param subList
	 * @return
	 */
	private static GeoRegion getRangeRegion(List<Point2D> subList) {

		int size = subList.size();
		if (size < 3) {
			// 不能构成面
			return null;
		}
		Point2D[] newlistPoint2ds = new Point2D[size + 1];
		for (int k = 0; k < newlistPoint2ds.length; k++) {
			if (k < size) {
				newlistPoint2ds[k] = subList.get(k);
			} else {
				newlistPoint2ds[k] = subList.get(0);
			}
		}

		return new GeoRegion(new Point2Ds(newlistPoint2ds));

	}

	/*
	 * 内存数据源，支持并发
	 */
	static class RamDataset extends ThreadLocal<Datasets> {
		protected Datasets initialValue() {
			Workspace workspace = new Workspace();
			Datasources datasources = workspace.getDatasources();
			DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
			datasourceConnectionInfo.setEngineType(EngineType.UDB);
			datasourceConnectionInfo.setServer(":memory:");
			Datasource datsource = datasources.create(datasourceConnectionInfo);
			return datsource.getDatasets();
		}
	}

	private static RamDataset ramDatasets = new RamDataset();

	static class RamDatasetVector extends ThreadLocal<DatasetVector> {
		protected DatasetVector initialValue() {
			Workspace workspace = new Workspace();
			Datasources datasources = workspace.getDatasources();
			DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
			datasourceConnectionInfo.setEngineType(EngineType.UDB);
			datasourceConnectionInfo.setServer(":memory:");
			Datasource datsource = datasources.create(datasourceConnectionInfo);
			Datasets datasets = datsource.getDatasets();

			DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
			datasetVectorInfo.setEncodeType(EncodeType.NONE);
			datasetVectorInfo.setName("RamDatasetVector");
			datasetVectorInfo.setType(DatasetType.REGION);
			DatasetVector dVector = datasets.create(datasetVectorInfo);
			// 添加标记字段
			FieldInfos fieldInfos = dVector.getFieldInfos();
			FieldInfo fieldInfo = new FieldInfo();
			fieldInfo.setCaption("ram_smid");
			fieldInfo.setName("ram_smid");
			fieldInfo.setType(FieldType.INT16);
			fieldInfos.add(fieldInfo);

			return dVector;
		}
	}

	private static RamDatasetVector ramDatasetVector = new RamDatasetVector();

	/**
	 * 对面数据集进行拓扑处理<br>
	 * 添加和完善相邻面节点
	 * 
	 * @param updateDatasetVector
	 * @param region
	 */
	public static void adjoinRegionTopology(DatasetVector datasetVector, GeoRegion region) {
		if (datasetVector == null) {
			logger.error("adjoinRegionTopology: regionDatasetVector is null .");
			return;
		}
		if (region == null) {
			logger.error("adjoinRegionTopology: region is null .");
			return;
		}

		long time1 = System.currentTimeMillis();

		// 查找相邻面
		Recordset recordset = datasetVector.query(region.getBounds(), CursorType.DYNAMIC);
		long time2 = System.currentTimeMillis();

		String name = ramDatasets.get().getAvailableDatasetName("A" + System.currentTimeMillis());
		DatasetVector ramdatasetVector = ramDatasets.get().getDatasource().recordsetToDataset(recordset, name);
		long time3 = System.currentTimeMillis();
		int[] precisionOrders = { 1 };
		boolean result = TopologyValidator.preprocess(new DatasetVector[] { ramdatasetVector }, precisionOrders, LINE_TOLERANCE);
		logger.debug("TopologyValidator.preprocess:" + result);

		long time4 = System.currentTimeMillis();

		// 数据写回
		Recordset ramRecordset = ramdatasetVector.getRecordset(false, CursorType.STATIC);
		int ramsize = ramRecordset.getRecordCount();
		logger.debug("ramsize:" + ramsize);
		GeoRegion[] regions = new GeoRegion[ramsize];
		int i = 0;
		while (!ramRecordset.isEOF()) {
			GeoRegion ramRegion = (GeoRegion) ramRecordset.getGeometry();
			regions[i++] = ramRegion;
			ramRecordset.moveNext();
		}
		ramRecordset.close();

		recordset.moveFirst();
		while (!recordset.isEOF()) {
			long smid = recordset.getID();
			GeoRegion newRegion = (GeoRegion) recordset.getGeometry();

			GeoRegion nearestRegion = findNearestRegion(newRegion, regions);

			if (!Geometrist.isIdentical(newRegion, nearestRegion)) {
				logger.debug("保存：" + smid);
				recordset.edit();
				recordset.setGeometry(nearestRegion);
				recordset.setDateTime(AreaFieldNames.UPDATE_TIME, new Date());
				recordset.update();
			} else {
				logger.debug("未保存：" + smid);
			}

			recordset.moveNext();
		}

		long time5 = System.currentTimeMillis();

		// 释放资源
		recordset.close();
		ramDatasets.get().delete(name);

		long time6 = System.currentTimeMillis();
		logger.debug("获取相邻面:[" + (time2 - time1) + "]ms" + "存储相邻面:[" + (time3 - time2) + "]ms" + "拓扑处理:[" + (time4 - time3) + "]ms" + "更新数据:["
				+ (time5 - time4) + "]ms" + "释放资源:[" + (time6 - time5) + "]ms");

	}

	/**
	 * 相邻面拓扑处理
	 * 
	 * @param datasetVector
	 * @param smid
	 */
	public static void topology(DatasetVector datasetVector, int smid) {

		// if (datasetVector == null) {
		// logger.error("adjoinRegionTopology: regionDatasetVector is null .");
		// return;
		// }
		// if (smid < 0) {
		// logger.error("adjoinRegionTopology: invaid smid .");
		// return;
		// }
		//
		// // 查找面
		// Recordset recordset = datasetVector.query("smid=" + smid,
		// CursorType.STATIC);
		// int count = recordset.getRecordCount();
		// if (count < 1) {
		// logger.error("adjoinRegionTopology: smid not exists .");
		// recordset.close();
		// return;
		// } else {
		// GeoRegion region = (GeoRegion) recordset.getGeometry();
		// recordset.close();
		//
		//
		// GeoRegion newRegion = region.clone();
		// while (!recordset.isEOF()) {
		// long smid_log = recordset.getID();
		//
		// GeoRegion adjoinRegion = (GeoRegion) recordset.getGeometry();
		// boolean updateAdjoin = false;
		//
		// // 排除自身
		// if (!Geometrist.isIdentical(region, adjoinRegion)) {
		// logger.debug("发现相邻面:" + smid_log);
		// int adjoinPartCount = adjoinRegion.getPartCount();
		// int partCount = region.getPartCount();
		// // 循环查找
		// for (int i = 0; i < adjoinPartCount; i++) {
		// for (int j = 0; j < partCount; j++) {
		// // 查找相交点
		// Point2D[] common = intersectPolyLine(adjoinRegion.getPart(i),
		// region.getPart(j));
		// if (common != null && common.length > 0) {
		// logger.debug("插入相交点" + common.length + "个:" + smid_log);
		// // 插入相邻面
		// Point2Ds newPoint2d = insertPoint2Region(adjoinRegion.getPart(i),
		// common);
		// if (!updateAdjoin && adjoinRegion.getPart(i).getCount() !=
		// newPoint2d.getCount()) {
		// updateAdjoin = true;
		// } else {
		// logger.debug("相邻面插入相交点 无须插入:" + smid_log);
		// }
		// adjoinRegion.setPart(i, newPoint2d);
		//
		// // 插入自身面
		// newPoint2d = insertPoint2Region(newRegion.getPart(j), common);
		// newRegion.setPart(j, newPoint2d);
		// if (!updateSelf && newRegion.getPart(j).getCount() !=
		// newPoint2d.getCount()) {
		// updateSelf = true;
		// } else {
		// logger.debug("自身面插入相交点 无须插入:" + smid_log);
		// }
		//
		// } else {
		// logger.error("未发现相交点:" + smid_log);
		// }
		// }
		//
		// }
		//
		// // 保存相邻面
		// if (updateAdjoin) {
		// logger.debug("保存相邻面:" + smid_log);
		// recordset.edit();
		// recordset.setGeometry(adjoinRegion);
		// recordset.update();
		// }
		//
		// } else {
		// // 找到自身
		// logger.debug("发现自身面:" + smid_log);
		// smid = recordset.getID();
		// }
		//
		// recordset.moveNext();
		// }
		//
		// // 保存自身面
		// if (updateSelf && smid > 0) {
		// logger.debug("保存自身面:" + smid);
		// Recordset saverecordset = datasetVector.query("smid=" + smid,
		// CursorType.DYNAMIC);
		// if (!saverecordset.isEOF()) {
		// saverecordset.edit();
		// saverecordset.setGeometry(newRegion);
		// saverecordset.update();
		// }
		// saverecordset.close();
		// }
		//
		// } else {
		// logger.debug("没有相邻面");
		// }
		// recordset.close();

	}
	
	public static void adjoinRegionTopologyFor7c(DatasetVector datasetVector, int smid) throws Exception {

		Recordset eraseRecordset = datasetVector.query("smid=" + smid, CursorType.DYNAMIC);
		logger.info("开始拓扑预处理");
		Recordset[] recordSetArray = new Recordset[1];
		recordSetArray[0] = eraseRecordset;
		// 容限（米）
		double tolerance = 10;
		// 精度等级数组
		int[] precisionOrders = new int[1];
		// 拓扑预处理选项对象
		TopologyPreprocessOptions options = new TopologyPreprocessOptions();
		// 设置节点捕捉
		options.setVertexesSnapped(true);
		// 设置节点与线段间插入节点
		options.setVertexArcInserted(true);
		// 设置线段间插入节点
		options.setArcsInserted(true);
		// 记录集拓扑预处理
		if (TopologyValidator.preprocess(recordSetArray, precisionOrders, options, tolerance)) {
			logger.info("拓扑预处理成功");
		} else {
			logger.error("拓扑预处理失败");
			throw new Exception("拓扑预处理失败");
		}

	}

	/**
	 * 对面数据集进行拓扑处理<br>
	 * 添加和完善相邻面节点
	 * 
	 * @param updateDatasetVector
	 * @param smid
	 */
	public static void adjoinRegionTopology(DatasetVector datasetVector, int smid,String filter) {
		if (datasetVector == null) {
			logger.error("adjoinRegionTopology: regionDatasetVector is null .");
			return;
		}
		if (smid < 0) {
			logger.error("adjoinRegionTopology: invaid smid .");
			return;
		}

		// 查找面
		Recordset recordset = datasetVector.query("smid=" + smid, CursorType.STATIC);
		int count = recordset.getRecordCount();
		if (count < 1) {
			logger.error("adjoinRegionTopology: smid not exists .");
			recordset.close();
			return;
		} else {
			GeoRegion region = (GeoRegion) recordset.getGeometry();
			recordset.close();

			// 查找相邻面
			if(StringUtils.isNotEmpty(filter)){
				logger.debug("Topology Filter " + filter);
				recordset = datasetVector.query(region.getBounds(),filter, CursorType.DYNAMIC);
			}
			else{
				recordset = datasetVector.query(region.getBounds(), CursorType.DYNAMIC);
			}
			count = recordset.getRecordCount();
			logger.debug("found " + count);
			if (count > 0) {

				// 存入缓存数据集
				Recordset ramRecordset = ramDatasetVector.get().getRecordset(false, CursorType.DYNAMIC);
				if (ramRecordset.getRecordCount() > 0) {
					ramRecordset.deleteAll();
					ramRecordset.update();
				}

				while (!recordset.isEOF()) {
					int smid_ = recordset.getID();
					logger.debug("found smid:" + smid_);
					region = (GeoRegion) recordset.getGeometry();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("ram_smid", smid_);

					ramRecordset.addNew(region, map);
					ramRecordset.update();
					ramRecordset.moveNext();

					recordset.moveNext();
				}
				ramRecordset.close();

				int[] precisionOrders = { 1 };
				boolean result = TopologyValidator.preprocess(new DatasetVector[] { ramDatasetVector.get() }, precisionOrders, LINE_TOLERANCE);
				logger.debug("TopologyValidator.preprocess:" + result);

				// 数据写回
				recordset.moveFirst();
				while (!recordset.isEOF()) {
					int smid_ = recordset.getID();
					logger.debug("write back smid:" + smid_);
					region = (GeoRegion) recordset.getGeometry();

					logger.debug(ramDatasetVector.get().getRecordCount() + "个");
					ramRecordset = ramDatasetVector.get().query("ram_smid=" + smid_, CursorType.STATIC);
					if (!ramRecordset.isEOF()) {
						GeoRegion ramRegion = (GeoRegion) ramRecordset.getGeometry();
						if (!Geometrist.isIdentical(region, ramRegion)) {
							logger.debug("保存：" + smid_);
							recordset.edit();
							recordset.setGeometry(ramRegion);
							recordset.setDateTime(AreaFieldNames.UPDATE_TIME, new Date());
							recordset.update();

						} else {
							logger.debug("未保存：" + smid_);
						}
					} else {
						logger.debug("不存在：" + smid_);
					}
					ramRecordset.close();

					recordset.moveNext();
				}
				recordset.close();

			}

		}

	}

	/**
	 * 指定范围拓扑处理<br>
	 * 
	 * @param datasetVector
	 * @param bound
	 * @param smid
	 */
	public static void topologyAlignRegion(DatasetVector datasetVector, Rectangle2D bound, int smid) {
		if (datasetVector == null) {
			logger.error("topologyAlignRegion: regionDatasetVector is null .");
			return;
		}
		if (bound == null) {
			logger.error("topologyAlignRegion: bound is null .");
			return;
		}
		if (smid < 1) {
			logger.error("topologyAlignRegion: invaid smid .");
			return;
		}

		Recordset recordset = null;
		try {

			long time1 = System.currentTimeMillis();

			// 查找待处理面
			recordset = datasetVector.query("smid=" + smid, CursorType.DYNAMIC);
			int count = recordset.getRecordCount();

			long time2 = System.currentTimeMillis();

			logger.debug("查找待处理面:" + (time2 - time1) + "ms");

			if (count < 1) {
				logger.error("topologyAlignRegion: invaid smid .");
				return;
			}
			GeoRegion regionSrc = (GeoRegion) recordset.getGeometry();
			GeoRegion region = regionSrc.clone();

			Recordset alignRecordset = null;
			try {
				// 查找相邻面
				alignRecordset = datasetVector.query(bound, CursorType.DYNAMIC);
				count = alignRecordset.getRecordCount();
				long time3 = System.currentTimeMillis();
				logger.debug("查找相邻面:" + (time3 - time2) + "ms");
				logger.debug("found " + count);

				if (count < 1) {
					logger.error("align region not found .");
					return;
				} else {
					while (!alignRecordset.isEOF()) {
						int smid_ = alignRecordset.getID();
						if (smid_ != smid) {
							GeoRegion region_src = (GeoRegion) alignRecordset.getGeometry();
							GeoRegion region_ = region_src.clone();

							// 拓扑处理
							long timet1 = System.currentTimeMillis();
							topologyAlignRegion(region, region_);
							long timet2 = System.currentTimeMillis();
							logger.debug("topology region_:" + (timet2 - timet1) + " ms");

							if (!Geometrist.isIdentical(region_src, region_)) {
								// 保存
								alignRecordset.edit();
								alignRecordset.setGeometry(region_);
								alignRecordset.update();
							}
							region_.dispose();
							region_src.dispose();

						}

						alignRecordset.moveNext();
					}
				}

				long time4 = System.currentTimeMillis();
				logger.debug("拓扑:" + (time4 - time3) + "ms");

			} finally {
				if (alignRecordset != null) {
					alignRecordset.close();
				}
			}

			long time5 = System.currentTimeMillis();
			// 修改待处理面
			if (!Geometrist.isIdentical(regionSrc, region)) {
				// 保存
				recordset.edit();
				recordset.setGeometry(region);
				recordset.update();
			}
			regionSrc.dispose();
			region.dispose();

			long time6 = System.currentTimeMillis();
			logger.debug("拓扑2:" + (time6 - time5) + "ms");

		} finally {
			if (recordset != null) {
				recordset.close();
			}
		}

	}

	/**
	 * 相邻面拓扑处理
	 * 
	 * @param region1
	 * @param region2
	 */
	private static void topologyAlignRegion(GeoRegion region1, GeoRegion region2) {

		// 变量复用
		Point2Ds line1Point2Ds = new Point2Ds();
		Point2Ds line2Point2Ds = new Point2Ds();
		for (int p1 = 0; p1 < region1.getPartCount(); p1++) {

			for (int p2 = 0; p2 < region2.getPartCount(); p2++) {

				Point2Ds point2Ds1 = region1.getPart(p1);
				Point2Ds point2Ds2 = region2.getPart(p2);

				Point2D[] point2ds1 = point2Ds1.toArray();
				Point2D[] point2ds2 = point2Ds2.toArray();

				List<Point2D> list1 = new ArrayList<Point2D>(Arrays.asList(point2ds1));
				List<Point2D> list2 = new ArrayList<Point2D>(Arrays.asList(point2ds2));

				logger.debug("loop times:" + list1.size() + "*" + list2.size());
				for (int i = 0; i < list1.size() - 1; i++) {
					// 线段两点不能相等
					if (list1.get(i).getX() == list1.get(i + 1).getX() && list1.get(i).getY() == list1.get(i + 1).getY()) {
						continue;
					}

					for (int j = 0; j < list2.size() - 1; j++) {
						// 线段两点不能相等
						if (list2.get(j).getX() == list2.get(j + 1).getX() && list2.get(j).getY() == list2.get(j + 1).getY()) {
							continue;
						}
						line1Point2Ds.clear();
						line2Point2Ds.clear();
						line1Point2Ds = new Point2Ds(new Point2D[] { list1.get(i), list1.get(i + 1) });
						line2Point2Ds = new Point2Ds(new Point2D[] { list2.get(j), list2.get(j + 1) });

						GeoLine line1 = new GeoLine(line1Point2Ds);
						GeoLine line2 = new GeoLine(line2Point2Ds);

						double distance = Geometrist.distance(line1, line2);
						if (distance <= TOPO_TOLERANCE) {
							logger.debug(i + "," + j + " :" + distance);

							// 插入公共点
							GeoPoint point1i = new GeoPoint(list1.get(i));
							GeoPoint point1i1 = new GeoPoint(list1.get(i + 1));
							GeoPoint point2j = new GeoPoint(list2.get(j));
							GeoPoint point2j1 = new GeoPoint(list2.get(j + 1));

							// 计算端点之间距离
							// 端点合并 2并入1
							double distance1 = Geometrist.distance(point1i, point2j);
							double distance2 = Geometrist.distance(point1i, point2j1);
							double distance3 = Geometrist.distance(point1i1, point2j);
							double distance4 = Geometrist.distance(point1i1, point2j1);

							boolean union11 = false;
							boolean union12 = false;
							boolean union21 = false;
							boolean union22 = false;

							if (distance1 <= TOPO_TOLERANCE) {
								list2.set(j, list1.get(i));
								union11 = true;
							}
							if (distance2 <= TOPO_TOLERANCE) {
								list2.set(j + 1, list1.get(i));
								union12 = true;
							}
							if (distance3 <= TOPO_TOLERANCE) {
								list2.set(j, list1.get(i + 1));
								union21 = true;
							}
							if (distance4 <= TOPO_TOLERANCE) {
								list2.set(j + 1, list1.get(i + 1));
								union22 = true;
							}

							// 端点插入，可能2个端点都需要
							double distancep1 = Geometrist.distance(point1i, line2);
							double distancep2 = Geometrist.distance(point1i1, line2);
							double distancep3 = Geometrist.distance(point2j, line1);
							double distancep4 = Geometrist.distance(point2j1, line1);
							if (distancep1 <= TOPO_TOLERANCE) {
								if (!union11 && !union12) {
									list2.add(j + 1, list1.get(i));
									j++;// 循环后移
								}
							}
							if (distancep2 <= TOPO_TOLERANCE) {
								if (!union21 && !union22) {
									list2.add(j + 1, list1.get(i + 1));
									j++;// 循环后移
								}
							}
							if (distancep3 <= TOPO_TOLERANCE) {
								if (!union11 && !union21) {
									list1.add(i + 1, list2.get(j));
									i++;// 循环后移
								}
							}
							if (distancep4 <= TOPO_TOLERANCE) {
								if (!union12 && !union22) {
									list1.add(i + 1, list2.get(j + 1));
									i++;// 循环后移
								}
							}

							// 交叉点插入，分2种情况 1，平行重叠线 2，交叉线(非端点交叉)
							// 交叉线(非端点交叉)
							if (distancep1 > TOPO_TOLERANCE && distancep2 > TOPO_TOLERANCE && distancep3 > TOPO_TOLERANCE && distancep4 > TOPO_TOLERANCE) {
								Point2D interPoint2d = Geometrist.intersectLine(list1.get(i), list1.get(i + 1), list2.get(j), list2.get(j + 1), false);
								if (interPoint2d != null) {
									list1.add(i + 1, interPoint2d);
									// i++;// 循环后移

									list2.add(j + 1, interPoint2d);
									j++;// 循环后移
								}

							}

							point1i.dispose();
							point1i1.dispose();
							point2j.dispose();
							point2j1.dispose();

						}

						// 释放资源
						line1.dispose();
						line2.dispose();

					}

				}

				// 释放资源
				point2Ds1 = null;
				point2Ds2 = null;
				point2ds1 = null;
				point2ds2 = null;

				region1.setPart(p1, new Point2Ds(list1.toArray(new Point2D[0])));
				region2.setPart(p2, new Point2Ds(list2.toArray(new Point2D[0])));
			}
		}

	}

	/**
	 * 寻找最相似面
	 * 
	 * @param newRegion
	 * @param regions
	 * @return
	 */
	private static GeoRegion findNearestRegion(GeoRegion newRegion, GeoRegion[] regions) {
		double xorArea = Double.MAX_VALUE;
		GeoRegion result = null;
		for (int j = 0; j < regions.length; j++) {

			GeoRegion xorRegion = (GeoRegion) Geometrist.xOR(newRegion, regions[j]);
			if (xorRegion == null) {
				// 完全一致
				xorArea = 0;
				result = regions[j];
				break;
			} else {
				if (xorRegion.getArea() < xorArea) {
					xorArea = xorRegion.getArea();
					result = regions[j];
				}
			}
		}

		return result;
	}

	/**
	 * 分解区域面<br>
	 * 分解复合区域面，保留岛洞<br>
	 * parts岛洞没有顺序，可能在后面，也可以在前面
	 * 
	 * @param region
	 * @return
	 */
	public static GeoRegion[] decompose(GeoRegion region) {
		if (region == null || region.isEmpty()) {
			return null;
		}

		// 获取子对象
		int partcount = region.getPartCount();
		GeoRegion[] listGeoRegion = new GeoRegion[partcount];
		for (int i = 0; i < partcount; i++) {
			Point2Ds point2ds = region.getPart(i);
			listGeoRegion[i] = new GeoRegion(point2ds);
		}

		// 没有岛洞，直接返回parts分解结果
		if (!Geometrist.hasHollow(region)) {
			return listGeoRegion;
		}

		// 分解，保留岛洞
		// 查找岛洞
		ArrayList<HashSet<Integer>> groups = new ArrayList<HashSet<Integer>>();
		for (int i = 0; i < partcount; i++) {
			for (int j = i + 1; j < partcount; j++) {
				// 第i与第j是否包含或者被包含
				if (Geometrist.isWithin(listGeoRegion[j], listGeoRegion[i])) {
					// 岛洞 合并岛洞面
					addSegmentPart(groups, i, j);// 将i j分成一组加入group
				} else if (Geometrist.canContain(listGeoRegion[j], listGeoRegion[i])) {
					addSegmentPart(groups, i, j);// 将i j分成一组加入group
				}
			}
		}

		// 复合面分组，保留岛洞在同一组
		ArrayList<GeoRegion> list = new ArrayList<GeoRegion>();
		// 获取岛洞
		for (HashSet<Integer> group : groups) {
			if (group.size() > 0) {
				GeoRegion newGeoRegion = new GeoRegion();
				for (Integer integer : group) {
					newGeoRegion.addPart(listGeoRegion[integer].getPart(0).clone());
				}
				list.add(newGeoRegion);
			}
		}
		// 获取剩下的单独面
		for (int i = 0; i < partcount; i++) {
			if (!isContainSegmentPart(groups, i)) {
				GeoRegion newGeoRegion = new GeoRegion(listGeoRegion[i].getPart(0).clone());
				list.add(newGeoRegion);
			}
		}

		// 释放资源
		for (int i = 0; i < partcount; i++) {
			listGeoRegion[i].dispose();
		}

		return list.toArray(new GeoRegion[0]);
	}

	/**
	 * 判断分组里面是否包含i
	 * 
	 * @param groups
	 * @param i
	 * @return
	 */
	private static boolean isContainSegmentPart(ArrayList<HashSet<Integer>> groups, int i) {
		if (groups == null) {
			return false;
		}
		for (HashSet<Integer> hashSet : groups) {
			if (hashSet.contains(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将分组i，j加入group list<br>
	 * 规则：如果原来有一组包含i或者j，加入该组;如果不包含，则创建新组
	 * 
	 * 
	 * @param groups
	 * @param i
	 */
	private static void addSegmentPart(ArrayList<HashSet<Integer>> groups, int i, int j) {
		if (groups == null) {
			groups = new ArrayList<HashSet<Integer>>();
		}
		boolean contains = false;
		for (HashSet<Integer> hashSet : groups) {
			if (hashSet.contains(i) && hashSet.contains(j)) {
				hashSet.add(i);
				hashSet.add(j);
				contains = true;
			}
		}
		if (!contains) {
			HashSet<Integer> hashSet = new HashSet<Integer>();
			hashSet.add(i);
			hashSet.add(j);
			groups.add(hashSet);
		}
	}
	
	/**
	 * 清除碎面
	 * @param region
	 * @return
	 * @Author Juannyoh
	 * 2016-11-18下午5:46:56
	 */
	public static GeoRegion cleanPiecedArea(GeoRegion region) {
		if (region == null) {
			return null;
		}
		// 去碎面
		GeoRegion newRegion = new GeoRegion();
		int partCount = region.getPartCount();
		for (int i = 0; i < partCount; i++) {
			Point2Ds point2Ds = null;
			try {
				point2Ds = region.getPart(i);
			} catch (Exception e) {
				// 空面
				continue;
			}

			int count = point2Ds.getCount();
			if (count < 4) {
				// 点数目异常面
				continue;
			}

			Point2D first = point2Ds.getItem(0);
			Point2D end = point2Ds.getItem(count - 1);
			// 非封闭面
			if (first.getX() != end.getX() || first.getY() != end.getY()) {
				// 非封闭面
				continue;
			}
			
			GeoRegion middleRegion=new GeoRegion(region.getPart(i));
			double area = middleRegion.getArea();
			if (area > 500) {
				newRegion.addPart(point2Ds);
			}
			middleRegion.dispose();
			middleRegion=null;
		}
		
		return newRegion;
	}

	/**
	 * 采用内存数据集对数据进行擦除操作--主要针对行政区
	 * @param region
	 * @param intersectRecord
	 * @return
	 * @Author Juannyoh
	 * 2016-11-24下午6:50:25
	 */
	public static GeoRegion earseAdminGeo(GeoRegion region,Recordset intersectRecord) throws Exception{
		DatasetVector interdatasetVector =null;
		DatasetVector regiondatasetVector=null;
		DatasetVector earsedatasetVector=null;
		GeoRegion earseregion=null;
		Recordset  recordset=null;
		Recordset earserecordset=null;
		try {
			//相交的数据面dv
			DatasetVectorInfo datasetVectorInfo_inter = createRegionDatasetVector("interDV"+System.currentTimeMillis());
			interdatasetVector = ramDatasets.get().getDatasource().getDatasets().create(datasetVectorInfo_inter);
			interdatasetVector.append(intersectRecord);
			
			//行政区边界dv
			DatasetVectorInfo datasetVectorInfo_geo = createRegionDatasetVector("geoDV"+System.currentTimeMillis());;
			regiondatasetVector=ramDatasets.get().getDatasource().getDatasets().create(datasetVectorInfo_geo);
			recordset=regiondatasetVector.getRecordset(false, CursorType.DYNAMIC);
			recordset.addNew(region);
			recordset.update();
			
			//擦除后的结果dv
			DatasetVectorInfo datasetVectorInfo_result = createRegionDatasetVector("earseDV"+System.currentTimeMillis());
			earsedatasetVector=ramDatasets.get().getDatasource().getDatasets().create(datasetVectorInfo_result);
			
			//擦除操作
			OverlayAnalystParameter param=new OverlayAnalystParameter();
			OverlayAnalyst.erase(regiondatasetVector, interdatasetVector, earsedatasetVector, param);
			
			//擦除的结果
			earserecordset=earsedatasetVector.getRecordset(false, CursorType.STATIC);
			earserecordset.moveFirst();
			earseregion=(GeoRegion) earserecordset.getGeometry();
			
		} catch (Exception e) {
			logger.info("数据集擦除失败");
		}finally{
			closeRecordset(recordset,earserecordset);
			closeDatasetVector(interdatasetVector,regiondatasetVector,earsedatasetVector);
		}		
		
		return earseregion;
	}
	
	/**
	 * 关闭结果集
	 * @param datavs
	 * @Author Juannyoh
	 * 2016-11-25上午11:02:21
	 */
	public static void closeRecordset(Recordset...recordsets){
		for (int i = 0; i < recordsets.length; i++) {
			if (recordsets[i] != null) {
				recordsets[i].close();
				recordsets[i].dispose();
			}
		}
	}
	
	/**
	 * 关闭数据集
	 * @param datavs
	 * @Author Juannyoh
	 * 2016-11-25上午11:03:11
	 */
	public static void closeDatasetVector(DatasetVector...datavs){
		for (int i = 0; i < datavs.length; i++) {
			if (datavs[i] != null) {
				datavs[i].close();
			}
		}
	}
	
	/**
	 * 创建面数据集类型
	 * @param dvname
	 * @return
	 * @Author Juannyoh
	 * 2016-11-25上午11:02:56
	 */
	public static DatasetVectorInfo createRegionDatasetVector(String dvname){
		DatasetVectorInfo datasetVectorInfo_result = new DatasetVectorInfo();
		datasetVectorInfo_result.setEncodeType(EncodeType.NONE);
		datasetVectorInfo_result.setName(dvname);
		datasetVectorInfo_result.setType(DatasetType.REGION);
		return datasetVectorInfo_result;
	}
	
	/**
	 * 普通面擦除
	 * @param region
	 * @param intersectRecord
	 * @return
	 * @Author Juannyoh
	 * 2016-11-25上午8:58:12
	 */
	public static GeoRegion earseCommonRegion(GeoRegion region,Recordset intersectRecord){
		if(null==region||region.getArea()<500){
			return null;
		}
		if(null != intersectRecord && intersectRecord.getRecordCount() > 0){
			logger.info("## 存在重叠区域，需要对区域面进行循环裁剪");
			for(intersectRecord.moveFirst();!intersectRecord.isEOF();intersectRecord.moveNext()){
				Geometry geometry = intersectRecord.getGeometry();
				if(null != geometry){
					if(null == region){
						break;
					}
//					String name = intersectRecord.getString("name");
					region = (GeoRegion) Geometrist.erase(region, geometry);
					/*if(Geometrist.hasIntersection(geometry, region)||Geometrist.hasTouch(geometry, region)){//部分重叠
						if(region.getPartCount()==1){
							region = (GeoRegion) Geometrist.erase(region,geometry);
						}else{
							GeoRegion tempGeo=(GeoRegion) Geometrist.intersect(region, geometry);
							if(tempGeo!=null&&tempGeo.getPartCount()>0){
								tempGeo=GeometristUtil.cleanPiecedArea(tempGeo);
								if(tempGeo!=null&&tempGeo.getArea()>0){
									GeoRegion temp_ = (GeoRegion) Geometrist.erase(region,tempGeo);
									if(temp_==null||temp_.getArea()<500){
										region=null;
										break;
									}
									temp_=GeometristUtil.cleanPiecedArea(temp_);
									double beforeEraseSize = region.getArea();
									double temRegionSize = temp_.getArea();
									double eraseSize = beforeEraseSize - temRegionSize;
									logger.info(name+"#xg#部分重叠:tempGeoSize："+tempGeo.getArea()+"eraseSize:"+NumberFormat.getInstance().format(eraseSize));
									if(eraseSize > 0){
										region.dispose();
										region=temp_;
									}
								}
								geometry.dispose();
								geometry = null;
								tempGeo.dispose();
								tempGeo=null;
								if(region==null || region.getArea() < 500){
									break;
								}
							}
						}
					}
					else{//其他
						double beforeEraseSize = region.getArea();
						GeoRegion temp =(GeoRegion) Geometrist.erase(region,geometry);
						if(temp==null||temp.getArea()<500){
							region=null;
							break;
						}
						double temRegionSize = temp.getArea();
						double eraseSize = beforeEraseSize - temRegionSize;
						logger.info(name+"#xg#其他:"+":erase:"+NumberFormat.getInstance().format(eraseSize));
						if(eraseSize > 0){
							region.dispose();
							region=temp;
						}
						geometry.dispose();
						geometry = null;
						if(region==null || region.getArea() < 500){
							break;
						}
					}*/
				}
			}
		}
		return region;
	}
}
