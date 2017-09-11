package com.supermap.egispservice.area.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.data.GeoRegion;
import com.supermap.data.Point2Ds;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.PageQueryResult;
import com.supermap.egispservice.area.Point2D;
import com.supermap.egispservice.area.exceptions.AreaException;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.area.utils.StringUtil;
import com.supermap.egispservice.geocoding.service.IGeocodingService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AreaServiceImplTest {

	@Autowired
	private IAreaService areaService;
	
	@Autowired
	private IGeocodingService geocodingService;

	 private static Logger LOGGER =
	 Logger.getLogger(AreaServiceImplTest.class);

	// @Test
	public void testAddArea() {
		Point2D[] point2Ds = new Point2D[5];
		point2Ds[0] = new Point2D(3.5263666687332886, 391.63286006572019);
		point2Ds[1] = new Point2D(73.773439642879282, 148.10967375534753);
		point2Ds[2] = new Point2D(254.46452179304362, 240.21139165478337);
		point2Ds[3] = new Point2D(174.07064938929875, 392.80364461528927);
		point2Ds[4] = new Point2D(3.5263666687332886, 391.63286006572019);

		try {
			String id = areaService.addArea("中环厅06", "zht", "1234",
					"admincode", "dkfjkdf", "supermap", "cloud", point2Ds,null,null,null,null);
			System.out.println("areaId : " + id);
		} catch (AreaException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void testAddAreaWithSplit() {
		Point2D[] point2Ds = new Point2D[5];
		point2Ds[0] = new Point2D(65.287276828806171, 461.65110375495726);
		point2Ds[1] = new Point2D(111.44301956886397, 316.02177752339549);
		point2Ds[2] = new Point2D(324.71438257464843, 328.75439621030807);
		point2Ds[3] = new Point2D(254.46452179304362, 464.03846975875331);
		point2Ds[4] = new Point2D(65.287276828806171, 461.65110375495726);

		try {
			String id = areaService.addArea("中环厅02", "zht", "1234",
					"admincode", "dkfjkdf", "supermap", "cloud", point2Ds,null,null,null,null);
			System.out.println("areaId : " + id);
		} catch (AreaException e) {

			e.printStackTrace();
		}
	}

	// @Test
	public void testAddAreaWithSplit2() {
		Point2D[] point2Ds = new Point2D[4];
		point2Ds[0] = new Point2D(171.90268768939995, 346.714743970277);
		point2Ds[1] = new Point2D(186.22576713205802, 258.7301131082346);
		point2Ds[2] = new Point2D(387.0046486050328, 291.72434968150054);
		point2Ds[3] = new Point2D(171.90268768939995, 346.714743970277);

		try {
			String id = areaService.addArea("中环厅03", "zht", "1234",
					"admincode", "dkfjkdf", "supermap", "cloud", point2Ds,null,null,null,null);
			System.out.println("areaId : " + id);
		} catch (AreaException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <p>
	 * Title ：testUpdateAreaRegion
	 * </p>
	 * Description：更新区域范围 Author：Huasong Huang CreateTime：2014-9-12 下午02:04:10
	 */
	// @Test
	public void testUpdateAreaRegion() {
		Point2D[] point2Ds = new Point2D[5];
		point2Ds[0] = new Point2D(164.42666006456773, 367.68782458274063);
		point2Ds[1] = new Point2D(161.10165947966493, 250.5454962838586);
		point2Ds[2] = new Point2D(265.96706254198295, 189.67240865256179);
		point2Ds[3] = new Point2D(300.49591476981936, 346.714743970277);
		point2Ds[4] = new Point2D(164.42666006456773, 367.68782458274063);

		try {
			// String id = areaService.addArea("中环厅03", "zht", "1234",
			// "dkfjkdf", "supermap", "cloud", point2Ds);
			boolean isUpdateSuccess = areaService.updateAreaRegion(
					"4fd061bfbb0c4d4a9adf222c89b3684a", point2Ds, null);
			System.out.println("update : " + isUpdateSuccess);
		} catch (AreaException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void testExistAreaNameOrNum() {
		// try {
		// // areaService.existAreaNameOrNumber("中环厅03", null, "supermap",
		// "cloud");
		// } catch (AreaException e) {
		// LOGGER.error(e.getMessage(), e);
		// }
	}

	// @Test
	public void testQueryByPoint() {
		try {
			List<AreaEntity> areaEntitys = areaService.queryAreaByPoint(
					new Point2D[] { new Point2D(12949415.9131182700000,
							4874946.6308587880000000) }, "supermap", "cloud");
			System.out.println(areaEntitys.get(0).getAreaNumber());
		} catch (AreaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Test
	public void testQueryAreaByNamePage() {
		try {
			PageQueryResult result = this.areaService.queryAreaByNamePage(
					"40288e9f483f48e501483f48eb060000", "北京", 1, 10, false);
			System.out.println(result);
		} catch (AreaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testQueryByEnOrDe() throws AreaException {
		// 、List<AreaEntity> aes = this.areaService.queryByEnOrDe(null, null,
		// "admincode","8a04a77b4cbc865c014cc0b8df9c0019", "00090000", true);
		// 、System.out.println(aes.size());

		// this.areaService.updateAreaAttribution("30d404ae01244bdfb013cf0c98ed267a",
		// "0001", "0001", null, null);

		// this.areaService.exportUDB2Byte(new
		// String[]{"72c62a1aa6414be2a4fcdcc9784d5c88"}, null);
		// boolean
		// ff=this.areaService.updateAreaOwner("eb527df19f1c44bea1945083e8c54217",
		// "40288f70574c1c130157653c18130005",
		// "8a04a77b4e4949f0014e855f638d084b", "002820000001");
		// System.out.println(ff);
		//带生成的城市列表
		List<String> list = new ArrayList<String>();
		
		List<Map<String, Object>> citylist=this.geocodingService.getAdminElement("310000", 3);
		
		if(null!=citylist&&citylist.size()>0){
			for(Map<String, Object> citymap:citylist){
				List<Map<String, Object>> elementlist=this.geocodingService.getAdminElement(StringUtil.convertObjectToString(citymap.get("ADMINCODE")), 4);
				if(elementlist!=null&&elementlist.size()>0){
					for(Map<String, Object> map:elementlist){
						list.add(StringUtil.convertObjectToString(map.get("ADMINCODE")));
					}
				}
			}
		}
		
		System.out.println(list.size());

		for (int i = 0; i < list.size(); i++) {
			try {
				String id = this.areaService.saveReverseSelectionArea(list.get(i),
						4, "8a04a77b54effacc015509b65abf02da",
						"8a04a77b54effacc015509b65a7a02d9", "01793000");
			} catch (Exception e) {
				continue;
			}
		}
//		
		long btime = System.currentTimeMillis();
		
//		String id = this.areaService.saveReverseSelectionArea("120000",
//				1, "8a04a77b4e4949f0014e855f63c4084c",
//				"8a04a77b4e4949f0014e855f638d084b", "00282000");
//		String id = this.areaService.saveReverseSelectionArea("370000",
//				1, "40288e9f483f48e501483f48eb060000",
//				"40288e9f48625c010148625c07160000", "00001000");
//		LOGGER.info("生成的区划id："+id);
		
		
//		LOGGER.info("删除区划："+this.areaService.deleteRegion("5afd34e269004fb9888a5a464168eb8a"));
		
		
//		AreaEntity area=this.areaService.queryByIdOrNumber(id, null, null, true);
//		List<Integer> partlists=new ArrayList<Integer>();
//		int parts[]=area.getParts();
//		for(int part:parts){
//			partlists.add(part);
//		}
//		GeoRegion region=getCompoundRegion(area.getPoints(),partlists);
//		LOGGER.info("生成的区划："+region.getArea());
		
//		long etime = System.currentTimeMillis();
//		LOGGER.info( "生成的区划 耗时:" + (etime - btime));
	}
	
	
	
	
	
	
	
	
	private GeoRegion getCompoundRegion(com.supermap.egispservice.area.Point2D[] point2Ds,List<Integer> parts){
		int j = 0;
		GeoRegion region = new GeoRegion();
		for (int i = 0; i < parts.size(); i++) {
			Point2Ds point2Dss = new Point2Ds();
			int count = parts.get(i);
			com.supermap.egispservice.area.Point2D[] partItems = new com.supermap.egispservice.area.Point2D[count];
			int index = 0;
			for (; j < point2Ds.length; ) {
				partItems[index] = point2Ds[j];
				index++;
				j++;
				if (index >= count) {
					break;
				}
			}
			point2Dss.addRange(convert(partItems));
			region.addPart(point2Dss);
		}
		return region;
	}
	private com.supermap.data.Point2D[] convert(com.supermap.egispservice.area.Point2D[] point2ds){
		com.supermap.data.Point2D[] point2Ds = null;
		if(null != point2ds){
			point2Ds = new com.supermap.data.Point2D[point2ds.length];
			for(int i=0;i<point2ds.length;i++){
				point2Ds[i] = new com.supermap.data.Point2D(point2ds[i].getX(),point2ds[i].getY());
			}
		}
		return point2Ds;
	}
	
	
	
//	@Test
	public void TestchangeStatus(){
		try {
			boolean flag = this.areaService.changeStatus("05afb842638c409eb075afe7104d09dd", 0);
			System.out.println(flag);
		} catch (AreaException e) {
			e.printStackTrace();
		}
		
	}
	
//	@Test
	public void TestfindRelationAreaAttrs(){
		try {
			List<Map<String,String>> resultlist=this.areaService.findRelationAreaAttrs("","4467605d47734dacbea5c69bf691b53c");
			System.out.println(resultlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@Test
	public void testQueryByEnOrDe2() throws AreaException {
		 List<AreaEntity> aes = this.areaService.queryByEnOrDe(null, null,"110","8a04a77b4e4949f0014e855f638d084b", "00282000", false);
		 System.out.println(aes.size());
//		 List<AreaEntity> aes1=this.areaService.queryByEnOrDeAndDate(null, null,"110","8a04a77b4e4949f0014e855f638d084b", "00282000", false, null, null);
//		 System.out.println(aes1.size());
		 
//		 AreaEntity  area=this.areaService.queryByIdOrNumber("1f2e7ec0904d47d1a82837c8b0eca813", null, null, true);
//		 System.out.println(area);
		 
		 List<AreaEntity> list=this.areaService.queryByEnOrDeAndDate(null, null, null, "8a04a77b4e4949f0014e855f638d084b", "00282000", false, null, null);
		 System.out.println(list.size());
	}
	
//	@Test
	public void testExportUDB(){
		try {
			this.areaService.exportUDB2Byte(null, "40288e9f48625c010148625c07160000", null, null, null, null, true);
		} catch (AreaException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void testfindRelationAreaAttrs(){
		this.areaService.findRelationAreaAttrs("d7f2e7a54ef345dc9c9c4aa467a6bbc0","61a9a8fb933343fcac0858f3af962725");
	}
	
//	@Test
	public void testqueryOneByName(){
		try {
			List<AreaEntity>  list=this.areaService.queryOneByName("cxh4tt", null,null,"8a04a77b4e4949f0014e855f638d084b", "00282000", false);
			System.out.println(list.size()+":"+list);
		} catch (AreaException e) {
			e.printStackTrace();
		}
	}
	
	
}
