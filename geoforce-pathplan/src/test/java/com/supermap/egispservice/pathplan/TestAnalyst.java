package com.supermap.egispservice.pathplan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.analyst.networkanalyst.DirectionType;
import com.supermap.analyst.networkanalyst.PathGuide;
import com.supermap.analyst.networkanalyst.PathGuideItem;
import com.supermap.analyst.networkanalyst.TransportationAnalystResult;
import com.supermap.analyst.networkanalyst.TurnType;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.pathplan.service.IPathPlanService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestAnalyst extends TestCase {
	@Autowired
	private IPathPlanService pathPlanService;
	
	@Test
	public void testNavigateAnalystEngineer() throws Exception{

		double[] nums = { 121.6228125, 31.431883680555551, 121.6370681423611, 31.42889539930556 };
		boolean withDetail = false;
		List<Point> points=new ArrayList<Point>();
		points.add(new Point(nums[0], nums[1]));
		points.add(new Point(nums[2], nums[3]));
		Map<String, Object> m= pathPlanService.getLength(points, withDetail);
	}
	
	public static String[] showResult(TransportationAnalystResult result) {
		PathGuide[] pathGuides = result.getPathGuides();
		StringBuilder show = new StringBuilder("从起点出发<br/>");
		double distance = 0;
		for (PathGuide pathGuide : pathGuides) {
			for (int j = 1; j < pathGuide.getCount(); j++) {
				PathGuideItem item = pathGuide.get(j);
				if (item.isEdge()) {
					double length = item.getLength();
					String name = item.getName();
					name = name.isEmpty() ? "匿名路段 " : name;
					show.append("沿").append(name).append("朝").append(getDirectionString(item)).append("行驶").append(length).append("<br/>");
					distance += length;
				}
				if (item.isStop()) {
					show.append("第").append(item.getIndex()).append("路口").append(getTurnTypeString(item.getTurnType()));
				}
			}
		}
		return new String[] { show.toString(), String.valueOf(distance) };
	}

	private static String getDirectionString(PathGuideItem item) {
		if (item.getDirectionType() == DirectionType.EAST)
			return "东";
		if (item.getDirectionType() == DirectionType.SOUTH)
			return "南";
		if (item.getDirectionType() == DirectionType.WEST)
			return "西";
		return "北";
	}

	private static String getTurnTypeString(TurnType item) {
		if (item == TurnType.LEFT)
			return "左转进入";
		if (item == TurnType.RIGHT)
			return "右转进入";
		if (item == TurnType.LEFT)
			return "左转进入";
		return "";
	}

}
