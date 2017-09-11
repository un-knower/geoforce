
/** 
 * 企业版在线接口
 * 命名空间:Dituhui
 */
Dituhui.Zone = Dituhui.Zone || {};

/** 
 * 边界样式
 */
Dituhui.Zone.BoundryStyle = {
    fill: false,
    strokeColor: "#ff6666",
    strokeWidth: 3
}

/** 
 * 区划样式
 */
Dituhui.Zone.getRegionStyle = function(){
    var style = {
        fill: true,
        fillColor: "#b0fff3",
        fillOpacity: 0.2,
        strokeColor: "#0090ff",
        strokeOpacity: 0.8,
        strokeWidth: 2,
        fontColor: "#006cff",
        fontOpacity: "1",
        fontFamily: "microsoft yahei",
        fontSize: "14px",
        fontWeight: "bold",
        fontStyle: "normal",
        labelSelect: "true",
        cursor: "pointer"
    }
    return style;
}

/** 
 * 不可编辑的区划样式
 */
Dituhui.Zone.getNonEditableRegionStyle = function(){
    var style = {
        fill: true,
        fillColor: "#959595",
        fillOpacity: 0.5,
        strokeColor: "#0000ff",
        strokeOpacity: 0.8,
        strokeWidth: 2
    }
    return style;
}


/** 
 * 区划文字样式
 */
Dituhui.Zone.getRegionTextStyle = function(status){
	var fillcolor = "#006cff", fontcolor = "#fff";	
	if(typeof(status) !== "undefined") {
		switch(status) {
			case 1:
				fillcolor = "#ff0000";
				break;
			
			case 2.5:
				fillcolor = "#ffffff";
				fontcolor = "#212121";
				break;
				
			case 2:
				fillcolor = "#603811";
				break;
			default:
				break;
		}
	}
    var style = {
        fontColor: fontcolor,
        fontWeight: "normal",
        fontSize: "14px",
        fontFamily: "microsoft yahei",
        fill: true,
        fillColor: fillcolor,
        fillOpacity: 1,
        stroke: true,
        strokeColor:"#323232"
    }
    return style;
}

/**
 * 停用的区划文字显示红底白字
 */
Dituhui.Zone.getRegionTextStyleGroups = function() {
	return [
		{
			start: 0,
			end: 0.5,
			style: Dituhui.Zone.getRegionTextStyle()
		},
		{
			start: 0.5,
			end: 1.5,
			style: Dituhui.Zone.getRegionTextStyle(1)
		},
		{
			start: 1.6,
			end: 2.1,
			style: Dituhui.Zone.getRegionTextStyle(2)
		},
		{
			start: 2.2,
			end: 3,
			style: Dituhui.Zone.getRegionTextStyle(2.5)
		},
	]
}

/** 
 * 区划选中样式
 */
Dituhui.Zone.getRegionSelectStyle = function(){    
    var style = {
        fill: true,
        fillColor: "#ff4568",
        fillOpacity: 0.4,   
        strokeColor: "#0090ff",
        strokeOpacity: 0.8,
        strokeWidth: 2,
        fontColor: "#006cff",
        fontOpacity: "1",
        cursor: "pointer"
    }
    return style;
}

/** 
 * 区划选中样式
 */
Dituhui.Zone.getRegionRelatedSelectStyle = function(){
    var style = {
        fill: true,
        fillColor: "#b0fff3",
        fillOpacity: 0.6,
        strokeColor: "#0090ff",
        strokeOpacity: 1,
        strokeWidth: 3,
        labelSelect: "true",
        cursor: "pointer"
    }
    return style;
}

/** 
 * 线拆分样式
 */
Dituhui.Zone.getSplitLineStyle = function(){    
    var style = {
        fillColor: "blue",
		strokeColor: "blue",
		pointRadius: 4,
		strokeWidth: 2,
		strokeDashstyle: "dash"
    }
    return style;
}

/** 
 * 面拆分样式
 */
Dituhui.Zone.getSplitAreaStyle = function(){    
    var style = {
        fillColor: "white",
        fillOpacity: 0.2,
		strokeColor: "blue",
		pointRadius: 4,
		strokeWidth: 2,
		strokeDashstyle: "dash"
    }
    return style;
}

/** 
 * 沿路画区-打点样式
 */
Dituhui.Zone.getRouteRegionPointStyle = function(label){    
    var style = {
        strokeColor: "#0090ff",
        strokeOpacity:1,
        strokeWidth:1,
        pointRadius:10,
        graphicName:"circle",
        fillColor:'#FFFFFF',
        fillOpacity: 1,
        cursor: 'pointer',
        graphicTitle: label,
        label: label
    }
    return style;
}

/** 
 * 区划捕捉 点样式
 */
Dituhui.Zone.getSnapPointStyle = function(){    
    var style = {
        strokeColor: "#0090ff",
        strokeOpacity:1,
        strokeWidth:1,
        pointRadius:5,
        graphicName:"circle",
        fillColor:'#0090ff',
        fillOpacity: 1,
        cursor: 'pointer'
    }
    return style;
}

/** 
 * 沿路画区-线路样式
 */
Dituhui.Zone.getPathRegionLineStyle = function(){    
    var style = {
        fillColor: "blue",
		strokeColor: "blue",
		pointRadius: 4,
		strokeWidth: 4,
		strokeDashstyle: "solid",
		strokeOpacity: 0.6
    }
    return style;
}

/** 
 * 区划修改样式
 */
Dituhui.Zone.getRegionEditStyle = function(){
    var style = {
        cursor:"inherit",
		fillColor:"blue",
		fillOpacity:0.3,
		fontColor:"#000000",
		hoverFillColor:"white",
		hoverFillOpacity:0.3,
		hoverPointRadius:1,
		hoverPointUnit:"%",
		hoverStrokeColor:"red",
		hoverStrokeOpacity:1,
		hoverStrokeWidth:0.2,
		labelAlign:"cm",
		labelOutlineColor:"white",
		labelOutlineWidth:3,
		pointRadius:6,
		pointerEvents:"visiblePainted",
		strokeColor:"blue",
		strokeDashstyle:"solid",
		strokeLinecap:"round",
		strokeOpacity:1,
		strokeWidth:1
    }
    return style;
}

/** 
 * 解析区划面数据，生成geometry
 */
Dituhui.Zone.DrawRegion = function(parts, point2Ds, enc) {
	if(typeof(enc) === "undefined") {
		enc = false;
	}
    var length_parts = ( parts.length == 0 ? 1 : parts.length) ;
    var length_point2Ds = point2Ds.length;

    //取点的索引
    var idxPoint = 0;
    var count = 0;

    var regions = [];

    for(var k=0; k < length_parts; k++) {
        //每个小多边形的点数
        var pntCount = parts[k];
        
        //得到区片
        var points = [];
        var index=-1;
        var a = 951753,b=852456;
//      a = a.toString(2);
//      b = b.toString(2);
        for (var j=idxPoint; j < idxPoint + pntCount; j++) {
            var item = point2Ds[j];
            if(!enc) {
            	var pp = new SuperMap.Geometry.Point(Number(item.x), Number(item.y) ); 	
            }
            else {
            	var x = item.x ^ a;
	            var y = item.y ^ b;
	            var pp = new SuperMap.Geometry.Point( Number(x)/100, Number(y)/100 ); 
            }
            
            
            /*for (var l=0; l < points.length; l++) {
                if (j == idxPoint + pntCount - 1 
                	  && l != 0 
                	  && points[l].x == pp.x && points[l].y == pp.y){
                    if (points[l].x != points[0].x && points[l].y != points[0].y) {
                        index=l;
                    }
                }
            }*/
            points.push(pp);
        }
        
        if (index != -1) {
//          regions.push( new SuperMap.Geometry.Polygon( new SuperMap.Geometry.LinearRing( points.splice(index) ) ) );
            regions.push( new SuperMap.Geometry.LinearRing( points.splice(index) ) );
        }
        idxPoint = idxPoint + pntCount;
        
        /*if(k !== 241 && k != 472) {
    		continue;
    	}
    	if(k==241) {
    		console.log("parts: 241")
    		console.dir(points);
    	}
    	if(k==472) {
    		console.log("parts: 241");
    		console.dir(points);
    	}*/
        
//      regions.push( new SuperMap.Geometry.Polygon([new SuperMap.Geometry.LinearRing( points )]));
        regions.push( new SuperMap.Geometry.LinearRing( points ) );
    }
//  var georegion = new SuperMap.Geometry.MultiPolygon(regions);    
    var georegion = new SuperMap.Geometry.Polygon(regions);    
    return georegion;
}

/**
 * 查询
 */
Dituhui.Zone.search = function(param, success, failed) {
    if( !param || !param.admincode || !param.level) {
        return;
    }
    Dituhui.Zone.searchRequest = Dituhui.request({
        url: urls.server + "/areaService/queryAllAreaByLevel?",
        data: param,
        success: function(e){
            if(e.isSuccess && e.result) {
                var re = [];
                if( e.result.length === 0 ) {
                    success(re);
                }
                else {
                    success( e.result );
                }
            }
            else {
                failed();
            }
        },
        error: function(){
            failed();            
        }
    });
} 

/**
 * 根据ID查询区划
 */
Dituhui.Zone.searchById = function(id, success, failed) {
    if( !id ) {
        return;
    }
    Dituhui.request({
        url: urls.server + "/areaService/queryArea?",
        data: {id: id},
        success: function(e){
            if(e.isSuccess && e.result) {
                success( e.result );                
            }
            else {
                failed();
            }
        },
        error: function(){
            failed();            
        }
    });
}


/**
 * 获取区划归属
 */
Dituhui.Zone.getAdminnameByCode = function(code, success, failed) {     
    Dituhui.request({
        url: urls.server + "/orderService/getAdminByCode?",
        data: {admincode: code},
        success: function(e){
            if(e.isSuccess && e.result && e.result.PROVINCE) {
                var adminname = e.result.PROVINCE;
                adminname += ( code.substr(2, 2) !== "00" && e.result.CITY) ? e.result.CITY : "";
                adminname += ( code.substr(4, 2) !== "00" && e.result.COUNTY) ? e.result.COUNTY : "";
                adminname += ( code.substr(6, 6) !== "000000" && e.result.TOWN) ? e.result.TOWN : "";
                success(adminname);
            }
            else {
                failed();
            }
        },
        error: function(){
            failed();  
        }
    });
} 


/**
 * 反选行政区边界，并与用户区划面做裁剪，形成新的区划保存
 */
Dituhui.Zone.saveReverseSelectionArea = function(code, level, success, failed) {     
    Dituhui.request({
        url: urls.server + "/areaService/saveReverseSelectionArea?",
        data: {
        	admincode: code,
        	level: level
        },
        success: function(e){
           	Dituhui.hideMask();
            if(e.isSuccess) {
           		success(e.result);
           	}
            else {
            	failed( e.info || false )
            }
        },
        error: function(){
        	Dituhui.hideMask();
            failed(false);            
        }
    });
} 

/**
 * 转换区划绑定的网点
 */
Dituhui.Zone.getBranchName = function(pointnames) {
    // pointnames = ['999'];
    // console.log(typeof(pointnames))
    if(typeof(pointnames) === "array") {
        var len = pointnames ? pointnames.length : 0;
        var branch_name = "";
        if(len > 0){
            for(var k=0; k<len; k++) {
                branch_name += pointnames[k];
                if(k < (len-1)){
                    branch_name += ",";
                }
            }
        }
        return branch_name;
    }
    return pointnames;
}

/**
 * 新增/修改
 * @param - type - Number 0:新增 , 1：修改
 * @param - attr - Object 修改时原有属性
 */
Dituhui.Zone.getEditRegionHtml = function(type, attr) {
	var title = type == 0 ? "新增区划" : Dituhui.setStringEsc(attr.name);
	var name = type == 0 ? "" : Dituhui.setStringEsc(attr.name);
	var number = type == 0 ? "" : Dituhui.setStringEsc(attr.areaNumber);
	var height = Dituhui.User.hasTownAuthority ? 180 : 160; 
	height += 10;
	var h =  '<div class="map-popup" style="min-height: '+height+'px;">';
		h += '<div class="map-popup-content">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ title +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
        h += '      <a class="popup-sure" title="确定"></a>';
        h += '  </div>';
        h += '  <div class="content" style="max-height:'+ (height-30) +'px">';
        h += '      <div class="attr">';
        h += '          <span class="label">区划名称：</span>';
        h += '          <input class="input" id="txt_name" name="name" value="'+ name +'" type="text" maxlength="50">';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">区划编号：</span>';
        h += '          <input class="input" id="txt_number" name="number" value="'+ number +'" type="text" maxlength="50">';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">区划归属：</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <select class="select" id="select_region_province"></select>';
        h += '          <select class="select" id="select_region_city"></select>';
        h += '          <select class="select" id="select_region_county"></select>';
        h += '      </div>';
        if(Dituhui.User.hasTownAuthority) {	
	        h += '      <div class="attr">';
	        h += '          <select class="select" id="select_region_town"></select>';
	        h += '      </div>';
        }
        if( type == 1 ) {
        	h += '      <div class="attr">';
	        h += '          <span class="label">区划状态：</span>';
	        h += '          <select class="popup-select-area-status longer">'
	          +  '				<option value="0" '
	          +(attr.area_status==0? 'selected="selected"' : '')+'>正常</option>'
	          +  '				<option value="1" '+(attr.area_status==1? 'selected="selected"' : '')+'>停用</option>'
	          +  '				<option value="2" '+(attr.area_status==2? 'selected="selected"' : '')+'>超区</option>'
	          +  '			</select>';
	        h += '      </div>';
	        h += '      <div class="attr '+(attr.area_status==0||attr.area_status===2?'hide': '')+' relate-region-content">';
	        h += '          <span class="label">关联区划：</span>';
	        h += '          <input class="input-disable longer" id="txt_relation_areaname" name="relation_areaname" value="'+ (attr.relation_areaname || '') +'" type="text" readonly="readonly"><span>可选择已有区划</span>';
	        h += '          <input class="hide" id="txt_relation_areaid" name="relation_areaid" value="'+ (attr.relation_areaid || '') +'" type="text" maxlength="50">';
	        h += '      </div>';
        }
        if(Dituhui.User.regionTableExtend) {        	
			var wgzCode = type == 0 ? "" : Dituhui.setStringEsc(attr.wgzCode);
        	h += '      <div class="attr">';
	        h += '          <span class="label">网格组编码：</span>';
	        h += '          <input class="input" id="txt_wgzCode" name="wgzCode" value="'+ wgzCode +'" type="text" maxlength="20">';
	        h += '      </div>';
			var wgzName = type == 0 ? "" : Dituhui.setStringEsc(attr.wgzName);
        	h += '      <div class="attr">';
	        h += '          <span class="label">网格组名称：</span>';
	        h += '          <input class="input" id="txt_wgzName" name="wgzName" value="'+ wgzName +'" type="text" maxlength="50">';
	        h += '      </div>';
			var lineCode = type == 0 ? "" : Dituhui.setStringEsc(attr.lineCode);
        	h += '      <div class="attr">';
	        h += '          <span class="label">线路编号：</span>';
	        h += '          <input class="input" id="txt_lineCode" name="lineCode" value="'+ lineCode +'" type="text" maxlength="20">';
	        h += '      </div>';
			var lineName = type == 0 ? "" : Dituhui.setStringEsc(attr.lineName);
        	h += '      <div class="attr">';
	        h += '          <span class="label">线路名称：</span>';
	        h += '          <input class="input" id="txt_lineName" name="lineName" value="'+ lineName +'" type="text" maxlength="50">';
	        h += '      </div>';
        }
        
        h += '  </div>';
        h += '</div>';
        h += '</div>';
    return h;
}

/*
 * 添加/修改区划属性时初始化区划归属
 */
Dituhui.Zone.initRegionPopupAdmincode = function(attr) {
	var smcity = $('.smcity');
	var code = smcity.attr('admincode');
	if(smcity.attr('level') == 2) {
		code = code.substr(0, 4)+'00';
	}
	
	if(attr != null && attr.admincode) {
		code = attr.admincode;
	}
	
	var select_province = $("#select_region_province");
	var select_city = $("#select_region_city");
	var select_county = $("#select_region_county");
	
	var p = '<option value="-1" disabled selected>请选择省</option>';
	
	var pros = Dituhui.SMCity.provinces_zhixia.concat();
	for(var i=0,len=pros.length; i<len; i++) {
		var item = pros[i];
		p += '<option value="'+ item.admincode +'">'+ item.province +'</option>';
	}
	pros = Dituhui.SMCity.provinces.concat();
	for(var i=0,len=pros.length; i<len; i++) {
		var item = pros[i];
		p += '<option value="'+ item.admincode +'">'+ item.province +'</option>';
	}
	select_province.html(p);
	select_city.html('<option value="-1" disabled selected>请选择市</option>');
	select_county.html('<option value="-1" disabled selected>请选择区县</option>');
	
	if(Dituhui.User.hasTownAuthority) {
		$("#select_region_town").html('<option value="-1" disabled selected>请选择乡镇</option>');
	}
	if(!code) {
		select_province.val("-1");
		return;
	}
	else {
		select_province.val(code.substr(0, 2) + "0000");	
		Dituhui.Zone.selectRegionProvince(null, code);
	}
}

/*
 * 添加区划弹窗中选择省
 */
Dituhui.Zone.selectRegionProvince = function(event, pro_code) {
	var code = $("#select_region_province").val();
	if(code == "-1") {
		return;
	}
	
	$("#select_region_city").html('<option value="-1" disabled selected>请选择市</option>');
	$("#select_region_county").html('<option value="-1" disabled selected>请选择区县</option>');
	
	Dituhui.showMask();
	Dituhui.request({
		url: urls.server + "/orderService/getAdminElements?",
        data: {
        	admincode: code,
        	level: 2
        },
        dataType: 'json',
        success: function(e){
        	Dituhui.hideMask();
        	if(e.isSuccess && e.result) {
        		var citys = e.result;
				var p = '<option value="-1" disabled selected>请选择市</option>';
				for(var i=0,len=citys.length; i<len; i++) {
					var item = citys[i];
					item.ADMINCODE = (item.ADMINCODE + "").substr(0,4) + "00";					
					p += '<option value="'+ item.ADMINCODE +'">'+ item.CITY +'</option>';
				}
				$("#select_region_city").html(p);
				if(pro_code) {
					if(Dituhui.SMCity.isZhixia(pro_code.substr(0,2) + "0000")) {
						$("#select_region_city option:nth-child(2)").attr('selected', true);
					}
					else {
						$("#select_region_city option[value='"+pro_code.substr(0,4)+"00']").attr('selected', true);
					}					
					Dituhui.Zone.selectRegionCity(null, pro_code);
				}
        	}
        	else {
        		Dituhui.hideMask();
        	}
        },
        error: function(){
        	Dituhui.hideMask();
        }
	});
}

/*
 * 添加区划弹窗中选择市
 */
Dituhui.Zone.selectRegionCity = function(event, pro_code) {
	var code = $("#select_region_city").val();
	if(code == "-1") {
		return;
	}
	Dituhui.showMask();
	Dituhui.request({
		url: urls.server + "/orderService/getAdminElements?",
        data: {
        	admincode: code,
        	level: 3
        },
        dataType: 'json',
        success: function(e){
        	Dituhui.hideMask();
        	if(e.isSuccess && e.result) {
        		var citys = e.result;
				var p = '<option value="-1" disabled selected>请选择区县</option>';
				for(var i=0,len=citys.length; i<len; i++) {
					var item = citys[i];
					p += '<option value="'+ item.ADMINCODE +'">'+ item.COUNTY +'</option>';
				}
				$("#select_region_county").html(p);
				if(pro_code) {
					$("#select_region_county option[value='"+pro_code.substr(0,6)+"']")
					.attr('selected', true);
					Dituhui.Zone.selectRegionCounty(null, pro_code);
				}
        	}
        	else {
        		Dituhui.hideMask();
        	}
        },
        error: function(){
        	Dituhui.hideMask();
        }
	});
}

/*
 * 添加区划弹窗中选择区县
 */
Dituhui.Zone.selectRegionCounty = function(event, pro_code) {
	var code = $("#select_region_county").val();
	if(code == "-1") {
		return;
	}
	Dituhui.showMask();
	Dituhui.request({
		url: urls.server + "/orderService/getAdminElements?",
        data: {
        	admincode: code,
        	level: 4
        },
        dataType: 'json',
        success: function(e){
        	Dituhui.hideMask();
        	if(e.isSuccess && e.result) {
        		var citys = e.result;
				var p = '<option value="-1" disabled selected>请选择区县</option>';
				for(var i=0,len=citys.length; i<len; i++) {
					var item = citys[i];
					p += '<option value="'+ item.ADMINCODE +'">'+ item.TOWN +'</option>';
				}
				$("#select_region_town").html(p);
				if(pro_code) {
					$("#select_region_town option[value='"+pro_code+"']").attr('selected', true);
				}
        	}
        	else {
        		Dituhui.hideMask();
        	}
        },
        error: function(){
        	Dituhui.hideMask();
        }
	});
}

/**
 * 根据区划编号获取网点名称
 */
Dituhui.Zone.getBranchNameByAreaNumber = function(number, success, failed) {
	if( !number) {
        return;
    }    
    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/pointService/getPointnameByAreaNumber?",
        data: {areanumber: number},
        dataType: 'json',
        success: function(e){
        	Dituhui.hideMask();
            if(e.isSuccess) {
                success(e);
            }
            else {
                failed(e.info ? e.info : false);
            }
        },
        error: function(){
        	Dituhui.hideMask();
            failed(false);            
        }
    });
}

/**
 * 根据区划属性获取订单信息窗中的内容
 */
Dituhui.Zone.getAttrRegionHtml = function(me) {
    var h =  '<div class="map-popup" style="min-height: 100px;">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ me.name +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
    if(me.style_status !== 2.5) {
    	h += '      <a class="popup-edit" title="编辑属性"></a>';
    }
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <div class="attr">';
        h += '          <span class="label">区划名称：</span>';
        h += '          <span class="text" >'+ Dituhui.setStringEsc(me.name) +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">区划编号：</span>';
        h += '          <span class="text" >'+ Dituhui.setStringEsc(me.areaNumber) +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">区划归属：</span>';
        h += '          <span class="text region-adminname"></span>';
        h += '      </div>';
        /*h += '      <div class="attr">';
        h += '          <span class="label">绑定网点：</span>';
        h += '          <span class="text">'+ me.branch_name +'</span>';
        h += '      </div>';*/
        h += '      <div class="attr">';
        h += '          <span class="label">区划状态：</span>';
        var statusLabel = me.area_status==0?'正常':(me.area_status==1 ? '停用' : '超区');
        h += '          <span class="text">'+ statusLabel +'</span>';
        h += '      </div>';
        if(me.area_status == 1) {
        	h += '  <div class="attr">';
	        h += '      <span class="label">关联区划：</span>';
	        h += '      <span class="text">'+ (me.relation_areaname || '') +'</span>';
	        h += '  </div>';
        }
        if(Dituhui.User.regionTableExtend) {
        	h += '  <div class="attr">';
	        h += '      <span class="label">网格组编号：</span>';
	        h += '      <span class="text">'+ (me.wgzCode || '') +'</span>';
	        h += '  </div>';
        	h += '  <div class="attr">';
	        h += '      <span class="label">网格组名称：</span>';
	        h += '      <span class="text">'+ (me.wgzName || '') +'</span>';
	        h += '  </div>';
        	h += '  <div class="attr">';
	        h += '      <span class="label">线路编号：</span>';
	        h += '      <span class="text">'+ (me.lineCode || '') +'</span>';
	        h += '  </div>';
        	h += '  <div class="attr">';
	        h += '      <span class="label">线路名称：</span>';
	        h += '      <span class="text">'+ (me.lineName || '') +'</span>';
	        h += '  </div>';
        }
        h += '  </div>';
        h += '</div>';
    return h;
}


/**
 * 删除
 */
Dituhui.Zone.remove = function(id, success, failed) {
    if( !id) {
        return;
    }    
    Dituhui.request({
        type: 'GET',
        async: true,
        url: urls.server + "/areaService/deleteArea?",
        data: {id: id},
        dataType: 'json',
        success: function(e){
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : "");
            }
        },
        error: function(){
            failed("");
        }
    });
}


/**
 * 新增
 */
Dituhui.Zone.add = function(param, success, failed){
	Dituhui.showMask();
	Dituhui.request({
        url: urls.server + "/areaService/add?",
        data: param,
        success: function(e){
        	Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : "新增区划失败");
            }
        },
        error: function(){
        	Dituhui.hideMask();
            failed("新增区划失败");
        }
    });
}

/**
 * 更改所属用户
 */
Dituhui.Zone.setUserBelonging = function(areaid, userid, success, failed){
	Dituhui.showMask();
	Dituhui.request({
        url: urls.server + "/areaService/updateOwner?",
        data: {
        	userid: userid,
        	id: areaid
        },
        success: function(e){
        	Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : "更新区划所属用户失败");
            }
        },
        error: function(){
        	Dituhui.hideMask();
            failed("更新区划所属用户失败");
        }
    });
}



/**
 * 从region中提取point2ds
 * 
 * @param - feature - 
 * @param - type - feature的类型
 */
Dituhui.Zone.getPoint2DsFromRegion = function(feature, type, isBaidu) {
	var georegion = feature.geometry;
	
	var points = [];
	
	var count = georegion.components.length;	
	if(type == "region"){
		for (var i=0; i < count; i++) {
			var part = georegion.components[i].components;
			var countk = part.length;
			for (var j=0; j < countk; j++)
			{
				points.push(part[j]);
			}
		}
	}
	else if(type == "line") {
		points = georegion.components.concat();
	}
	
	
	var counth = points.length;
	var arrPoints = "";
	for (var h=0; h < counth; h++)
	{
		var item = points[h];
		var x = item.x;
		var y = item.y;
		
		if(isBaidu) {
			var coord = Baidu.restoreCoord(x, y);
            x = coord.x;
            y = coord.y;
		}
		
		arrPoints += "{\"x\":" + x + ",\"y\":" + y + "}";
		if(h < counth -1) {
			arrPoints += ","
		}
	}
	
	var r = "[" + arrPoints + "]";
	return r;
}
		
/**
 * 从region中提取parts
 * 
 * @param - feature - 
 * @param - type - feature的类型
 */
Dituhui.Zone.getPartsFromRegion = function(feature, type) {
	var georegion = feature.geometry;
	var parts = [];
	
	var i = 0;
	var r = "[";
	
	var count = georegion.components.length;	
	for (var i=0; i < count; i++)
	{
		var len = georegion.components[i].components.length;
		r += len;
		if( i < count - 1) {
			r += ",";
		}
	}
	
	r += "]";
	return r;
}


/**
 * 合并
 */
Dituhui.Zone.merge = function(ids, success, failed){
	Dituhui.showMask();
	Dituhui.request({
        url: urls.server + "/areaService/union?",
        data: {
        	ids: ids
        },
        success: function(e){
        	Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : "区划合并失败");
            }
        },
        error: function(){
        	Dituhui.hideMask();
            failed("区划合并失败");            
        }
    });
}


/**
 * 线拆分
 */
Dituhui.Zone.lineSplit = function(param, success, failed){
	Dituhui.showMask();
	Dituhui.request({
        url: urls.server + "/areaService/lineSplit?",
        data: param,
        success: function(e){
        	Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : "区划拆分失败");
            }
        },
        error: function(){
        	Dituhui.hideMask();
            failed("区划拆分失败");            
        }
    });
}

/**
 * 更新节点
 */
Dituhui.Zone.update = function(param, success, failed){
	Dituhui.showMask();
	Dituhui.request({
        url: urls.server + "/areaService/updateArea?",
        data: param,
        success: function(e){
        	Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : "修改区划失败");
            }
        },
        error: function(){
        	Dituhui.hideMask();
            failed("修改区划失败");            
        }
    });
}

/**
 * 更新属性
 */
Dituhui.Zone.updateAttr = function(param, success, failed){
	Dituhui.showMask();
	Dituhui.request({
        url: urls.server + "/areaService/updateAttr?",
        data: param,
        success: function(e){
        	Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : null);
            }
        },
        error: function(){
        	Dituhui.hideMask();
            failed();            
        }
    });
}


/**
 * 获取沿路画区的线路
 */
Dituhui.Zone.getPathFromCloud = function(param, success, failed){
	Dituhui.request({
        url: urls.server + "/tsService/getPath?",
        data: param,
        success: function(e){
            if(e.success) {
                success(e);
            }
            else {
                failed(e.info ? e.info : null);
            }
        },
        error: function(){
            failed();            
        }
    });
}

/**
 * 区划停用
 */
Dituhui.Zone.updateStatus = function(param, success, failed){
	Dituhui.request({
        url: urls.server + "/areaService/updateAreaStatus?",
        data: param,
        success: function(e){
            if(e.isSuccess) {
                success(e);
            }
            else {
                failed(e.info ? e.info : null);
            }
        },
        error: function(){
            failed();
        }
    });
}

/**
 * 绘制经纬度线条
 */
Dituhui.Zone.drawLineLonLat = function(point2Ds, isBaidu) {
	var pointLines = [], isLonlat = false;
	if(point2Ds[0].x && point2Ds[0].x < 181 ) {
		isLonlat = true;
	}
	
	for (var i=0,len=point2Ds.length; i<len; i++) {
		var coord;
		if(typeof(isBaidu) === 'undefined' || !isBaidu) {
			var ll = new SuperMap.Geometry.Point(point2Ds[i].x, point2Ds[i].y)
	        coord = isLonlat ?
	        	Dituhui.latLonToMeters(ll)
	        	:
	        	ll;
	    }
		else {
			var xy = Baidu.getCoord(point2Ds[i].x, point2Ds[i].y, isLonLat ? "lonlat":"meters");
	        coord = new SuperMap.Geometry.Point(xy.x, xy.y);
		}
		pointLines.push(coord);
	}
	var geoline = new SuperMap.Geometry.LineString(pointLines);
	return geoline;
}













