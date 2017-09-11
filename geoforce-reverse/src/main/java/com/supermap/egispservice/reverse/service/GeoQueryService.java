package com.supermap.egispservice.reverse.service;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.io.GeohashUtils;
import com.spatial4j.core.shape.Point;
import com.supermap.convert.impl.BaiduCoordinateConvertImpl;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egispservice.base.entity.AddressInfo;
import com.supermap.egispservice.base.entity.POIInfo;
import com.supermap.egispservice.base.entity.PointParam;
import com.supermap.egispservice.base.entity.PointXY;
import com.supermap.egispservice.reverse.utils.Config;
import com.supermap.egispservice.reverse.utils.CoorUtils;
import com.supermap.egispservice.reverse.utils.LuceneUtils;

/**
  * @ClassName: GeoQueryService
  * @Description: 
  *   基于GeoHash的查询服务  
  * @author huanghuasong
  * @date 2016-2-17 下午2:52:22
  *
 */
@Component
public class GeoQueryService implements IGeoQueryService {

	
	@Autowired
	private LuceneUtils luceneUtils;
	
	private static Logger LOGGER = Logger.getLogger(GeoQueryService.class);
	
	private static SpatialContext ctx;

	public static SpatialStrategy strategy = null;
	
	static{
		ctx = SpatialContext.GEO;
		int maxLevels = 11;
		SpatialPrefixTree grid = new GeohashPrefixTree(ctx, maxLevels);
		strategy = new RecursivePrefixTreeStrategy(grid, "coorField");
//		strategy = new TermQueryPrefixTreeStrategy(grid,"coorField");
	}
	
	/**
	  * <p>Title: queryNearestPOI</p>
	  * Description: 
	  *     查询最近的POI
	  * @param lon
	  * @param lat
	  * @return
	  * @see com.dituhui.query.service.IGeoQueryService#queryNearestPOI(double, double)
	 */
	public AddressInfo queryNearestPOI(double lon, double lat) {
		return queryNearestPOI(lon, lat,Config.QUERY_RANGE);
	}

	/**
	 * 
	  * <p>Title: queryNearestPOI</p>
	  * Description: 
	  *     根据坐标查找最近的POI
	  * @param lon
	  * @param lat
	  * @param tolerance	查找范围
	  * @return
	  * @see com.dituhui.query.service.IGeoQueryService#queryNearestPOI(double, double, double)
	 */
	public AddressInfo queryNearestPOI(double lon, double lat, double tolerance) {
		if(tolerance > Config.QUERY_RANGE_MAX){
			tolerance = Config.QUERY_RANGE_MAX;
		}
		int level = Config.RANGE_BUFF_TE;
		if(tolerance >= Config.RANGE_BUFF_TH_THRESHOD){
			level = Config.RANGE_BUFF_TH;
		}else if(tolerance >= Config.RANGE_BUFF_HU_THRESHOD && tolerance < Config.RANGE_BUFF_TH_THRESHOD){
			level = Config.RANGE_BUFF_HU;
		}else if(tolerance < Config.RANGE_BUFF_HU_THRESHOD && tolerance > Config.RANGE_BUFF_TE_THRESHOD){
			level = Config.RANGE_BUFF_TE;
		}else if(tolerance < Config.RANGE_BUFF_TE_THRESHOD){
			level = Config.RANGE_BUFF_MI;
		}

		IndexSearcher searcher = luceneUtils.getIndexSearcher();
		POIInfo poiResult = null;
		// 创建排序规则
//		Point pt = ctx.makePoint(lon, lat);
//		ValueSource valueSource = strategy.makeDistanceValueSource(pt,DistanceUtils.DEG_TO_KM);
//		Sort distSort = null;
		try {
//			distSort = new Sort(valueSource.getSortField(false)).rewrite(searcher);
			// 创建查询
			TopDocs docs = null;
			String prefix = GeohashUtils.encodeLatLon(lat, lon, level);
			PrefixQuery query = new PrefixQuery(new Term("coorField",prefix));
			
			LOGGER.info("## start to execute the query ...");
			long start = System.currentTimeMillis();
			docs = searcher.search(query, 999);
			long getResultTime = System.currentTimeMillis();
			LOGGER.info("## query consume times ["+(getResultTime - start)+"],["+docs.totalHits+"]");
			
			if (docs.totalHits > 0) {
				List<POIInfo> pois = new LinkedList<POIInfo>();
				ScoreDoc[] scoreDocs = docs.scoreDocs;
				for(int i=0;i<scoreDocs.length;i++){
					POIInfo poi = new POIInfo();
					// 结果打印
					Document document = searcher.doc(scoreDocs[i].doc);
					String id = document.get("id");
					String name = document.get("name");
					String address = document.get("address");
					String admincode = document.get("admincode");
					String province = document.get("province");
					String city = document.get("city");
					String county = document.get("county");
					String coors = document.get("coorField");
					String town = document.get("town");
					Point p = null;
					if(!StringUtils.isEmpty(coors)){
						p = GeohashUtils.decode(coors, ctx);
						poi.setLat(p.getY());
						poi.setLon(p.getX());
						double distance = CoorUtils.getDistance(p.getX(), p.getY(), lon, lat);
						poi.setDistance(distance);
					}
					poi.setAddress(address);
					poi.setAdmincode(admincode);
					poi.setCity(city);
					poi.setCounty(county);
					poi.setId(id);
					poi.setTown(town);
					
					poi.setName(name);
					poi.setProvince(province);
					pois.add(poi);
					LOGGER.debug("RESULT : "+poi);
				}
				poiResult = Collections.min(pois);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		AddressInfo ai = new AddressInfo();
		if(null != poiResult){
			ai.setCountry("中国");
			ai.setAddress(poiResult.getName());
			ai.setCity(poiResult.getCity());
			ai.setDistrict(poiResult.getCounty());
			ai.setProvince(poiResult.getProvince());
			ai.setStreet(poiResult.getAddress());
			PointXY p = new PointXY();
			p.setX(poiResult.getLon());
			p.setY(poiResult.getLat());
			ai.setPoint(p);
		}
		return ai;
	}

	
	/**
	 * 
	  * <p>Title: batchQueryNearestPOI</p>
	  * Description: 批量逆地址解析
	  *     
	  * @param points
	  * @param type
	  * @return
	  * @see com.supermap.egispservice.reverse.service.IGeoQueryService#batchQueryNearestPOI(java.util.List, java.lang.String)
	 */
	public List<AddressInfo> batchQueryNearestPOI(List<PointParam> points,String type) {
		List<AddressInfo> resultList = new LinkedList<AddressInfo>();
		for(PointParam p : points){
			String code = p.getCode();
			PointXY point = p.getPoint();
			com.supermap.entity.Point llp = new com.supermap.entity.Point(point.getX(),point.getY());
			if(!BaiduCoordinateConvertImpl.isLLPoint(llp)){
				llp = SuperMapCoordinateConvertImpl.smMCToLatLon(llp);
			}
			AddressInfo ai = this.queryNearestPOI(llp.getLon(),llp.getLat());
			if(null == ai){
				ai = new AddressInfo();
				ai.setCountry(null);
			}
			ai.setCode(code);
			if(!StringUtils.isEmpty(type) && type.equalsIgnoreCase(Config.TYPE_SMC)){
				point = ai.getPoint();
				if(null != point && point.getX() > 0.0d){
					llp = new com.supermap.entity.Point(point.getX(),point.getY());
					llp = SuperMapCoordinateConvertImpl.smLL2MC(llp);
					point.setX(llp.getLon());
					point.setY(llp.getLat());
				}
				ai.setPoint(point);
			}
			resultList.add(ai);
		}
		return resultList;
	}

}
