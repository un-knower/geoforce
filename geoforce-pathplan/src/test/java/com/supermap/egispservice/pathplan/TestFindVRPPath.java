package com.supermap.egispservice.pathplan;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.pathplan.constant.Config;
import com.supermap.egispservice.pathplan.service.IDataGenerateService;
import com.supermap.egispservice.pathplan.service.INavigateAnalystEngineerService;
import com.supermap.egispservice.pathplan.util.UpdateDataPathAnalystEngine;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestFindVRPPath extends TestCase {
	@Autowired
	private UpdateDataPathAnalystEngine updateDataPathAnalystEngine;
	@Autowired
	private Config config;
	@Autowired
	private INavigateAnalystEngineerService navigateAnalystEngineerService;
	@Autowired
	private IDataGenerateService dataGenerateService;

	@Test
	public void testVRPPath() throws Exception {
		List<Point> orderPoints = new ArrayList<Point>();
		orderPoints.add(new Point(121.228479, 31.108227));
		orderPoints.add(new Point(121.230899, 31.108528));
		orderPoints.add(new Point(121.235373, 31.111953));
		orderPoints.add(new Point(121.235898, 31.109476));
		orderPoints.add(new Point(121.236148, 31.108343));
		List<Point> netPoints = new ArrayList<Point>();
		netPoints.add(new Point(121.227782, 31.112766));
		//dataGenerateService.generateDataByVRPPath(netPoints, orderPoints, "40288cbc48f818080148f8946d460001", false);
	}

	@Test
	public void testConvertCoord() throws Exception {
		// 网点
		System.out.println(CoordinateTranslator.lngToMercator(121.6513060326) + "," + CoordinateTranslator.latToMercator(31.426794352299));
		// 订单
		System.out.println(CoordinateTranslator.lngToMercator(121.62702893257) + "," + CoordinateTranslator.latToMercator(31.429764391112));
		System.out.println(CoordinateTranslator.lngToMercator(121.64129530377) + "," + CoordinateTranslator.latToMercator(31.42678061981));
		System.out.println(CoordinateTranslator.lngToMercator(121.6513060326) + "," + CoordinateTranslator.latToMercator(31.426794352299));
		System.out.println(CoordinateTranslator.lngToMercator(121.66132212586) + "," + CoordinateTranslator.latToMercator(31.426808084785));
		System.out.println(CoordinateTranslator.lngToMercator(121.67133821911) + "," + CoordinateTranslator.latToMercator(31.426826394763));
	}

}
