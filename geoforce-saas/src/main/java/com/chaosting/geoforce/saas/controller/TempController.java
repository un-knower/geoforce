package com.chaosting.geoforce.saas.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.dubbo.config.annotation.Reference;
/*import com.chaosting.geoforce.area.w.service.IAreaService;
import com.chaosting.geoforce.coordinate.service.ICoordinateService;
import com.chaosting.geoforce.district.service.IDistrictService;
import com.chaosting.geoforce.district.vo.District;
import com.chaosting.geoforce.district.vo.DistrictDictItem;*/

@RestController
@RequestMapping("/temp")
public class TempController {

	/*@Reference
	private IDistrictService districtService;

	@Reference
	private IAreaService areaService;

	@Reference
	private ICoordinateService coordinateService;

	// ===========================================================================//

	@RequestMapping(value = "/coordinate/convert", method = RequestMethod.GET)
	public List<com.chaosting.geoforce.util.gis.Point> convert(HttpServletRequest request, List<com.chaosting.geoforce.util.gis.Point> points, Integer from, Integer to) {
		getContextInfo(request);

		return coordinateService.convert(points, null == from ? 0 : from, null == to ? 0 : to);
	}*/

	// ===========================================================================//

//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/area/addArea", method = RequestMethod.POST)
//	public Object addArea(HttpServletRequest request, String area_no, String name, Integer type, Boolean geo_bdr,
//			Boolean clip, String points, String parts, String info, String operator_id, String top_user_id) {
//		getContextInfo(request);
//
//		JSONArray pointArray = JSONArray.fromObject(points);
//		List<Point> pointList = (List<Point>) JSONArray.toCollection(pointArray, Point.class);
//
//		JSONArray partArray = JSONArray.fromObject(parts);
//		List<Integer> partList = (List<Integer>) JSONArray.toCollection(partArray, Integer.class);
//
//		AreaGeo areaGeo = new AreaGeo();
//		areaGeo.setParts(partList);
//		areaGeo.setPoints(pointList);
//
//		return areaService.addArea(area_no, name, type, geo_bdr, clip, areaGeo, info, operator_id, top_user_id);
//	}
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/area/queryByFilter", method = RequestMethod.GET)
//	public Object queryByFilter(HttpServletRequest request, String layer_codes, Integer type, String filters,
//			Boolean has_geo, String top_user_id) {
//		getContextInfo(request);
//		List<QueryFilterModel> filterList = null;
//		List<String> layerList = null;
//		if (filters != null && !"".equals(filters)) {
//			JSONArray array = JSONArray.fromObject(filters);
//			filterList = (List<QueryFilterModel>) JSONArray.toCollection(array, QueryFilterModel.class);
//		}
//		if (!StringUtils.isEmpty(layer_codes)) {
//			JSONArray array = JSONArray.fromObject(layer_codes);
//			layerList = (List<String>) JSONArray.toCollection(array, String.class);
//		}
//
//		return areaService.queryByFilter(layerList, null == type ? 0 : type, filterList, has_geo, top_user_id);
//	}
//
//	@RequestMapping(value = "/area/deleteArea", method = RequestMethod.GET)
//	public Object deleteArea(HttpServletRequest request, String top_user_id, String area_no, Integer type,
//			Integer version, String operator_id) {
//		getContextInfo(request);
//
//		return areaService.deleteArea(top_user_id, area_no, type, version, operator_id);
//	}
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/area/updateAreaRegion", method = RequestMethod.POST)
//	public Object updateAreaRegion(HttpServletRequest request, String area_no, String parts, String points, Short type,
//			Short version, Boolean clip, String operator_id, String top_user_id) {
//		getContextInfo(request);
//
//		JSONArray pointArray = JSONArray.fromObject(points);
//		List<Point> pointList = (List<Point>) JSONArray.toCollection(pointArray, Point.class);
//
//		JSONArray partArray = JSONArray.fromObject(parts);
//		List<Integer> partList = (List<Integer>) JSONArray.toCollection(partArray, Integer.class);
//
//		AreaGeo areaGeo = new AreaGeo();
//		areaGeo.setParts(partList);
//		areaGeo.setPoints(pointList);
//
//		return areaService.updateAreaRegion(area_no, areaGeo, type, version, clip, operator_id, top_user_id);
//	}
//
//	@RequestMapping(value = "/area/areaUpdateInfos", method = RequestMethod.POST)
//	public Object areaUpdateInfos(HttpServletRequest request, String area_no, Integer version, String area_name,
//			Integer status, Short type, String info, String operator_id, String top_user_id) {
//		getContextInfo(request);
//
//		return areaService.areaUpdateInfos(area_no, type, version, area_name, status, info, operator_id, top_user_id);
//	}
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/area/splitByLine", method = RequestMethod.POST)
//	public Object splitByLine(HttpServletRequest request, String area_no, String cno, String line, Integer version,
//			String operator_id, String top_user_id) {
//		getContextInfo(request);
//
//		JSONArray linesArray = JSONArray.fromObject(line);
//		List<Point> lineList = (List<Point>) JSONArray.toCollection(linesArray, Point.class);
//		Point[] linePoints = lineList.toArray(new Point[lineList.size()]);
//
//		return areaService.splitByLine(area_no, cno, linePoints, version, operator_id, top_user_id);
//	}
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/area/splitByRegion", method = RequestMethod.POST)
//	public Object splitByRegion(HttpServletRequest request, String area_no, String cno, String points, String parts,
//			Integer version, String operator_id, String top_user_id) {
//		getContextInfo(request);
//
//		JSONArray pointArray = JSONArray.fromObject(points);
//		List<Point> pointList = (List<Point>) JSONArray.toCollection(pointArray, Point.class);
//
//		JSONArray partArray = JSONArray.fromObject(parts);
//		List<Integer> partList = (List<Integer>) JSONArray.toCollection(partArray, Integer.class);
//
//		AreaGeo areaGeo = new AreaGeo();
//		areaGeo.setParts(partList);
//		areaGeo.setPoints(pointList);
//
//		return areaService.splitByRegion(area_no, cno, areaGeo, version, operator_id, top_user_id);
//	}
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/area/areaMerge", method = RequestMethod.POST)
//	public Object areaMerge(HttpServletRequest request, String areaNos, String operator_id, String top_user_id) {
//		getContextInfo(request);
//
//		JSONArray areaArray = JSONArray.fromObject(areaNos);
//		List<String> areaList = (List<String>) JSONArray.toCollection(areaArray, Point.class);
//
//		return areaService.areaMerge(areaList, operator_id, top_user_id);
//	}

	// ===========================================================================//

	private void getContextInfo(HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
		WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		WebApplicationContext mvcContext = (WebApplicationContext) servletContext
				.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.springMvc");
		System.out.println("======springContext=========");
		String[] names = springContext.getBeanDefinitionNames();
		for (String name : names) {
			System.out.println(name);
		}
		System.out.println("=======mvcContext========");
		names = mvcContext.getBeanDefinitionNames();
		for (String name : names) {
			System.out.println(name);
		}
		System.out.println("===============");
	}
}
