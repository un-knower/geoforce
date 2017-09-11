package com.supermap.egispservice.pathplan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.analyst.networkanalyst.DirectionType;
import com.supermap.analyst.networkanalyst.PathGuide;
import com.supermap.analyst.networkanalyst.PathGuideItem;
import com.supermap.analyst.networkanalyst.SideType;
import com.supermap.analyst.networkanalyst.TransportationAnalystResult;
import com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator;
import com.supermap.data.GeoLineM;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PointM;
import com.supermap.data.PointMs;
import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.pathplan.constant.Config;
import com.supermap.egispservice.pathplan.service.INavigateAnalystEngineerService;
import com.supermap.egispservice.pathplan.util.UpdateDataPathAnalystEngine;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestDataGenernate extends TestCase {
	@Autowired
	private UpdateDataPathAnalystEngine updateDataPathAnalystEngine;
	@Autowired
	private Config config;
	@Autowired
	private INavigateAnalystEngineerService navigateAnalystEngineerService;
	
	@Test
	public void testDataGenernate() throws Exception{
		try {
			// todo: 变量可改为读取配置文件
			String BASE_PATH = "D:/cb/iObjects7/data2/";
			String workspacePath = "D:/cb/iObjects7/data1/shanghai2012.smwu";
			String networkName = "BuildNetwork";
			String turnTableName = "TurnTable_impact";

			// 测试数据
			int orderCount = 744;
			GroupType groupType = GroupType.RadialGroup;
			String orderInputFile = BASE_PATH + "shanghai.txt";
			Point2Ds orderPoints = readOrdersFromFile(orderInputFile, orderCount);
			// 一个网点
			Point2D netPoint = genernateNet();
			Point2Ds netPoints = new Point2Ds();
			netPoints.add(netPoint);
			// 每辆车允许30单货物。orderCount和carLoad会决定生成多少条线路。
			int carLoad = 30;

			// 将订单数据写入json文件
			String orderOutputFile = BASE_PATH + "sh_order.json";
			writeOrdersToFile(orderInputFile, orderOutputFile, orderCount);
			System.out.println("order(json)保存在:" + orderOutputFile);
			// 将网点数据写入json文件
			String netOutputFile = BASE_PATH + "sh_net.json";
			writeNetFoFile(netOutputFile);
			System.out.println("net(json)保存在:" + netOutputFile);

			// 返回详细路线信息
			boolean withDetail = true;
			// 禁止走高架桥
			boolean prohibitViaduct = false;

			JSONObject linesJsonObject = null;

			// 计算
			if (!groupType.equals(GroupType.NoneGroup)) {
				// 一个网点中心，根据orderPoints和carLoad安排多辆车
				ArrayList<Point2Ds> orderPointsList = navigateAnalystEngineerService.sortOrders(netPoint, orderPoints, carLoad, groupType);
				System.out.println("orderPointsList.size() = " + orderPointsList.size());

				List<JSONObject> jsonPointList = new ArrayList<JSONObject>();
				int[] parts = new int[orderPointsList.size()];

				double totalLength = 0;
				for (int i = 0; i < orderPointsList.size(); i++) {
					Point2Ds groupOrderPoints = orderPointsList.get(i);
					// 分析
					System.out.println("开始分析第" + i + "组路线规划");
					TransportationAnalystResult result = navigateAnalystEngineerService.navigateAnalyst(groupOrderPoints, netPoints, prohibitViaduct, withDetail);
					if (result != null) {
						double length = fetchResult(result, jsonPointList, parts, i);
						totalLength += length;

						// todo? 需要输出转向信息描述
						// showResult(result);

						result.dispose();
					} else {
						System.out.println("result is null");
					}
				}
				System.out.println("totalLength = " + totalLength);

				linesJsonObject = convertLineToJson(jsonPointList, parts);
			} else {
				// 一个或多个网点中心，不根据carLoad安排多辆车，每个网点一辆车
				List<JSONObject> jsonPointList = new ArrayList<JSONObject>();
				int[] parts = new int[1];

				// 分析
				TransportationAnalystResult result = navigateAnalystEngineerService.navigateAnalyst(orderPoints, netPoints, prohibitViaduct, withDetail);
				if (result != null) {
					double length = fetchResult(result, jsonPointList, parts, 0);
					System.out.println("length = " + length);

					// todo? 需要输出转向信息描述
					// showResult(result);

					result.dispose();
				} else {
					System.out.println("result is null");
				}

				linesJsonObject = convertLineToJson(jsonPointList, parts);
//				} else {
//					// 一个或多个网点中心，不根据carLoad安排多辆车，每个网点多辆车
			}

			String linesFilePath = BASE_PATH + "path.json";
			writeToFile(linesFilePath, linesJsonObject);
			System.out.println("规划路线保存结果:" + linesFilePath);

			// 销毁对象
			navigateAnalystEngineerService.closeObjectJava();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// 从TransportationAnalystResult提取道路轨迹
	public double fetchResult(TransportationAnalystResult result, List<JSONObject> jsonPointList, int[] parts, int partIndex){
		double length = 0;

		GeoLineM[] linems = result.getRoutes();
		if (linems == null) {
			System.out.println("result.getRoutes() == null");
		} else if (linems.length == 0) {
			System.out.println("result.getRoutes().length == 0");
		} else {
			System.out.println("linems.length = " + linems.length);
			for (int i = 0; i < linems.length; i++) {
				GeoLineM line = linems[i];

				int linePartCount = line.getPartCount();
				System.out.println("linePartCount = " + linePartCount);
				// 假设linePartCount都是1
				PointMs part = line.getPart(0);
				int pointCount = part.getCount();
				System.out.println("pointCount = " + pointCount);
				parts[partIndex] += pointCount;
				for (int j = 0; j < pointCount; j++) {
					PointM pointM = part.getItem(j);
					JSONObject jsonPoint = new JSONObject();
					// 将经纬度转为墨卡托点
					jsonPoint.accumulate("x", CoordinateTranslator.lngToMercator(pointM.getX()));
					jsonPoint.accumulate("y", CoordinateTranslator.latToMercator(pointM.getY()));
					jsonPointList.add(jsonPoint);
				}
			}

			// 计算长度
			length = navigateAnalystEngineerService.calculateLength(result);
		}

		return length;
	}

	public void showResult(TransportationAnalystResult result, String fileName) {
//			String fileName = BASE_PATH + "road.txt";
		PathGuide[] pathGuides = result.getPathGuides();
		int k = 0;
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(fileName));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		StringBuilder show = new StringBuilder();
		int i = 0;
		double totalLength = 0;
		String countLength = "";
		for (PathGuide pathGuide : pathGuides) {
			i++;
			show.append("路线--").append(k++);
			double length = 0;
			for (int j = 1; j < pathGuide.getCount(); j++) {
				PathGuideItem item = pathGuide.get(j);
				// 导引子项为站点的添加方式
				if (item.isStop()) {
					String side = "无";
					if (item.getSideType() == SideType.LEFT)
						side = "左侧";
					if (item.getSideType() == SideType.RIGHT)
						side = "右侧";
					if (item.getSideType() == SideType.MIDDLE)
						side = "道路上";
					String dis = NumberFormat.getInstance().format(item.getDistance());
					if (item.getIndex() == -1 && item.getID() == -1) {
						continue;
					}
					if (j != pathGuide.getCount() - 1) {
						show.append("到达[").append(item.getIndex()).append("号路由点],在道路").append(side).append(dis).append("\n");
					} else {
						show.append("到达终点,在道路").append(side).append(dis).append("\n");
					}
				}
				// 导引子项为弧段的添加方式
				if (item.isEdge()) {
					String direct = "直行";
					if (item.getDirectionType() == DirectionType.EAST)
						direct = "东";
					if (item.getDirectionType() == DirectionType.WEST)
						direct = "西";
					if (item.getDirectionType() == DirectionType.SOUTH)
						direct = "南";
					if (item.getDirectionType() == DirectionType.NORTH)
						direct = "北";
					String weight = NumberFormat.getInstance().format(item.getWeight());
					String roadName = item.getName();
					if (weight.equals("0") && roadName.equals("")) {
						show.append("朝").append(direct).append("行走").append(item.getLength()).append("\n");
					} else {
						String roadString = roadName.equals("") ? "匿名路段" : roadName;
						show.append("沿着[").append(roadString).append("(EdgId:").append(item.getID()).append(")],朝").append(direct).append("行走")
								.append(item.getLength()).append("\n");
					}

					length += item.getLength();
				}
				if (show.length() > 4056) {
					try {
						br.write(show.toString());
						show = null;
						show = new StringBuilder();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			totalLength += length;
			countLength += length + "_";
		}
		countLength += totalLength;
		show.append(countLength);
		try {
			br.write(show.toString());
			br.close();
			System.out.println("路径详细保存 在:" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static JSONObject convertLineToJson(List<JSONObject> pointList, int[] parts) {
		if (pointList == null || pointList.size() == 0)
			throw new RuntimeException("pointList is null/empty");

		JSONObject pointJson = new JSONObject();
		pointJson.accumulate("point2Ds", pointList);
		pointJson.accumulate("parts", parts);
		JSONObject resultJson = new JSONObject();
		resultJson.accumulate("result", pointJson);
		return resultJson;
	}

	private static void writeToFile(String filePath, JSONObject jsonObject) {
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(filePath));
			write.write(jsonObject.toString());
			write.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static Point2Ds genernateNetsFromOrderPoints(Point2Ds point2DsNodes, int count) {
		Point2Ds point2Ds = new Point2Ds();
		for (int i = 0; i < count; i++) {
			Point2D item = point2DsNodes.getItem(i);
			point2Ds.add(item);
		}
		return point2Ds;
	}

	private static Point2D genernateNet() {
		return new Point2D(121.1944, 31.1478);
	}

	public static void writeNetFoFile(String outPut) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		BufferedWriter write = new BufferedWriter(new FileWriter(outPut));
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		JSONObject item = new JSONObject();
		item.accumulate("address", "上海青浦区赵巷镇G318与赵重公路交叉路口");
		item.accumulate("carId", 1);
		item.accumulate("carNo", "沪A.12345");
		item.accumulate("cargoArea", "配送区域范围（暂无）");
		item.accumulate("city", "上海");
		item.accumulate("company", "上海XX物流门店");
		item.accumulate("contactPerson", "XXX");
		item.accumulate("netId", 1);
		item.accumulate("netName", "赵巷镇");
		item.accumulate("owner", "xxx");
		item.accumulate("ownerPhone", String.format("13100000%03d", 1));
		item.accumulate("province", "上海");
		item.accumulate("telephone", "xxx");
		// "SMX":121.1944,"SMY":31.1478,,"x":"13491293.5287","y":"3651960.8396"
		item.accumulate("x", "13491293.5287");
		item.accumulate("y", "3651960.8396");
		jsonObjects.add(item);

		JSONObject results = new JSONObject();
		results.accumulate("result", jsonObjects);
		write.write(results.toString());
		write.close();
	}

	public static Point2Ds readOrdersFromFile(String input, int orderCount) throws Exception, FileNotFoundException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(input), "GBK");
		BufferedReader read = new BufferedReader(isr);

		String line = null;
		String realId = null;
		JSONObject orderJson = null;
		Point2Ds point2Ds = new Point2Ds();
		List<String> idAreadyAdd = new ArrayList<String>();

		int count = 0;
		while ((line = read.readLine()) != null) {
			if (line.contains("null") || !line.contains("上海")) {
				continue;
			}
			if (count++ > orderCount) {
				break;
			}
			line = line.substring(1);
			line = line.replaceAll("},", ",");
			line = line.replaceAll(",\\{", ",");
			orderJson = JSONObject.fromObject(line);
			String address = orderJson.getString("address");
			if (!address.contains("上海")) {
				continue;
			}
			String province = String.valueOf(orderJson.get("NSHENG"));
			if (province != null && !province.contains("上海")) {
				continue;
			}
			Object object = orderJson.get("id");
			if (object instanceof JSONArray) {
				JSONArray rids = (JSONArray) object;
				realId = rids.getString(0);
			}
			if (idAreadyAdd.contains(realId)) {
				continue;
			}

			idAreadyAdd.add(realId);
			// 路径规划只能以经纬度进行计算所以取SMX SMY ,x y为墨卡托点
			Point2D point2d = new Point2D(orderJson.getDouble("SMX"), orderJson.getDouble("SMY"));
			point2Ds.add(point2d);
		}
		read.close();
		System.out.println("订单总数 ：" + point2Ds.getCount());
		return point2Ds;
	}

	public static void writeOrdersToFile(String input, String output, int orderCount) throws Exception, FileNotFoundException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(input), "GBK");
		BufferedWriter write = new BufferedWriter(new FileWriter(output));
		BufferedReader read = new BufferedReader(isr);

		write.write("{\"records\":[");

		int id = 0;
		String line = null;
		String realId = null;
		JSONObject res = null;
		JSONObject orderJson = null;
		StringBuilder writeBuilder = new StringBuilder();
		List<String> idAreadyAdd = new ArrayList<String>();

		int count = 0;
		while ((line = read.readLine()) != null) {
			if (line.contains("null") || !line.contains("上海")) {
				continue;
			}
			if (count++ > orderCount) {
				break;
			}

			line = line.substring(1);
			line = line.replaceAll("},", ",");
			line = line.replaceAll(",\\{", ",");
			orderJson = JSONObject.fromObject(line);
			String address = orderJson.getString("address");
			if (!address.contains("上海")) {
				continue;
			}
			String province = String.valueOf(orderJson.get("NSHENG"));
			if (province != null && !province.contains("上海")) {
				continue;
			}
			Object object = orderJson.get("id");
			if (object instanceof JSONArray) {
				JSONArray rids = (JSONArray) object;
				realId = rids.getString(0);
			}
			if (idAreadyAdd.contains(realId)) {
				continue;
			}
			idAreadyAdd.add(realId);

			res = new JSONObject();
			res.accumulate("id", id++);
			res.accumulate("region_id", 4);
			res.accumulate("address", address);
			res.accumulate("province", province);
			res.accumulate("phone", "13800138277");
			res.accumulate("y", orderJson.get("y"));
			res.accumulate("contact_person", "xxx");
			res.accumulate("x", orderJson.get("x"));
			res.accumulate("city", orderJson.get("NDI"));
			res.accumulate("county", orderJson.get("NXIAN"));
			writeBuilder.append(res.toString());

			// 最后一次循环不加逗号
			if (count < orderCount) {
				writeBuilder.append(",");
			}
		}
		read.close();
		write.write(writeBuilder.toString());
		write.write("]}");
		write.close();
	}
}
