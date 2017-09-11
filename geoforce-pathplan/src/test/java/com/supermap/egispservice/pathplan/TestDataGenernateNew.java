package com.supermap.egispservice.pathplan;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.pathplan.constant.Config;
import com.supermap.egispservice.pathplan.service.IDataGenerateService;
import com.supermap.egispservice.pathplan.service.INavigateAnalystEngineerService;
import com.supermap.egispservice.pathplan.util.UpdateDataPathAnalystEngine;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestDataGenernateNew extends TestCase {
	@Autowired
	private UpdateDataPathAnalystEngine updateDataPathAnalystEngine;
	@Autowired
	private Config config;
	@Autowired
	private INavigateAnalystEngineerService navigateAnalystEngineerService;
	@Autowired
	private IDataGenerateService dataGenerate;

	@Test
	public void testDataGenernate() throws Exception {
		List<Point> orderPoints = new ArrayList<Point>();
		orderPoints.add(new Point(121.228479, 31.108227));
		orderPoints.add(new Point(121.230899, 31.108528));
		orderPoints.add(new Point(121.235373, 31.111953));
		orderPoints.add(new Point(121.235898, 31.109476));
		orderPoints.add(new Point(121.236148, 31.108343));
		// orderPoints.add(new Point(121.7770681423611, 31.42889539930556));//tolerance=0.91
		// orderPoints.add(new Point(1.3509610595009439E7, 3645934.6537104603));
		List<Point> netPoints = new ArrayList<Point>();
		netPoints.add(new Point(121.227782, 31.112766));
		// 订单
		Point2Ds orderPoint2Ds = new Point2Ds();
		for (Point point : orderPoints) {
			Point2D point2d = new Point2D(point.getLon(), point.getLat());
			orderPoint2Ds.add(point2d);
		}
		// 网点
		Point2D point2d = null;
		for (Point point : netPoints) {
			point2d = new Point2D(point.getLon(), point.getLat());
			break;
		}

		dataGenerate.generateDataByMTSPPath(point2d, orderPoint2Ds, "40288cbc48f818080148f8946d460001", false, false);
	}
}
