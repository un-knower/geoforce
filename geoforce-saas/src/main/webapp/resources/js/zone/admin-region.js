$(function(){
	var adminRegion = new AdminRegion();
	adminRegion.getProcess(function(t, c){
		if(c !== t && t!=0) {
			var h = '行政区划导入中，【<span class="upload-process">'+c+'</span>/'+ t +'】';
			Dituhui.Modal.loading(h);
			adminRegion.startProcessTimer();
			adminRegion.autoload = true;
		}
	});
	var timer = null;
	$("#shuttle_province").on("click", "li", function(){		
		var me = $(this);
		var code = me.attr("code");
		clearTimeout(timer);	
		if( (isFirefox && isCtrlKeydown) || (!isFirefox && window.event.ctrlKey)) {
			//按住ctrl选中
			adminRegion.onSelect(me);
		}
		else {
			//不按住ctrl钻取
			timer = setTimeout(function(){	
				adminRegion.setCity(code);
			}, 300);
		}
		
	});
	$("#shuttle_province").on("dblclick", "li", function(){	
		var me = $(this);
		clearTimeout(timer);
		adminRegion.onSelect(me);
	});
	
	$("#shuttle_city").on("click", "li", function(){		
		var me = $(this);
		var code = me.attr("code");
		clearTimeout(timer);
			
		if( (isFirefox && isCtrlKeydown) || (!isFirefox && window.event.ctrlKey)) {
			//按住ctrl选中
			adminRegion.onSelect(me);
			return;
		}
		timer = setTimeout(function(){	
			adminRegion.setCounty(code);
		}, 300);		
	});
	$("#shuttle_city").on("dblclick", "li", function(){	
		var me = $(this);
		clearTimeout(timer);
		adminRegion.onSelect(me);
	});
	
	$("#shuttle_county").on("click", "li", function(){		
		var me = $(this);
		var code = me.attr("code");
		clearTimeout(timer);	
		if( (isFirefox && isCtrlKeydown) || (!isFirefox && window.event.ctrlKey)) {
			//按住ctrl选中
			adminRegion.onSelect(me);
			return;
		}
		timer = setTimeout(function(){	
			adminRegion.setTown(code);
		}, 300);
	});
	$("#shuttle_county").on("dblclick", "li", function(){	
		var me = $(this);
		clearTimeout(timer);
		adminRegion.onSelect(me);
	});
	
	$("#shuttle_town").on("dblclick", "li", function(){	
		var me = $(this);
		clearTimeout(timer);
			
		if( (isFirefox && isCtrlKeydown) || (!isFirefox && window.event.ctrlKey)) {
			//按住ctrl选中
			adminRegion.onSelect(me);
			return;
		}
		adminRegion.onSelect(me);
	});
	$("#shuttle_town").on("click", "li", function(){			
		if( (isFirefox && isCtrlKeydown) || (!isFirefox && window.event.ctrlKey)) {	
			var me = $(this);
			clearTimeout(timer);
			//按住ctrl选中
			adminRegion.onSelect(me);
			return;
		}
	});
	
	$("#selected_adminnames").on("click", "li .glyphicon", adminRegion.removeSelectedAdmin);
	$(".btn-import-adminregions").on("click", adminRegion.save);
	$(".data-import-regins .close-fade-out").on("click", function(){
		$(".data-import-regins").fadeOut();
		adminRegion.clear();
	})
})

function AdminRegion() {
	this.initProvice = function() {
		var pros = Dituhui.SMCity.provinces_zhixia.concat(
			Dituhui.SMCity.provinces, Dituhui.SMCity.province_other);
		var len = pros.length, h='';
		for( var i=0; i<len; i++ ) {
			var p = pros[i];	
			h += '<li code="'+ p.admincode +'" level="1" name="'+p.province+'"><span>'+p.province+'</span></li>';
			//h += '<a href="javascript:void(0);" option="toProvince" admincode="'+ p.admincode +'" level="1" zhixia="true"  data-value="'+ p.province +'">'+ p.province +'</a>';
		}
		$("#shuttle_province").html( h );
	}
	this.autoload = false;
	this.initProvice();
	
	var that = this;
	this.setCity = function(code) {
		$("#shuttle_city, #shuttle_county, #shuttle_town").html("");
		that.getChildren(code, 2, that.displayCity);
	}
	
	this.displayCity = function(data) {
		var len = data.length;
		if(len < 1) {
			return;
		}
		data = data.sort(function(a, b) { return a.ADMINCODE - b.ADMINCODE });		
		var h='';
		for( var i=0; i<len; i++ ) {
			var p = data[i];	
			var selected = that.isSelected(p.ADMINCODE, 2);
			h += '<li '+(selected?'class="active"':'')+' code="'+ p.ADMINCODE +'" level="2" name="'+p.CITY+'"><span>'+p.CITY+'</span></li>';
		}
		$("#shuttle_city").html( h );
	}
	
	this.setCounty = function(code) {
		$("#shuttle_county, #shuttle_town").html("");
		that.getChildren(code, 3, that.displayCounty);
	}
	
	this.displayCounty= function(data) {
		var len = data.length;
		if(len < 1) {
			return;
		}
		data = data.sort(function(a, b) { return a.ADMINCODE - b.ADMINCODE });		
		var h='';
		for( var i=0; i<len; i++ ) {
			var p = data[i];	
			var selected = that.isSelected(p.ADMINCODE, 3);
			h += '<li '+(selected?'class="active"':'')+' code="'+ p.ADMINCODE +'" level="3" name="'+p.COUNTY+'"><span>'+p.COUNTY+'</span></li>';
		}
		$("#shuttle_county").html( h );
	}
	
	this.setTown = function(code) {
		if(!Dituhui.User.hasTownAuthority) {
			return;
		}
		$("#shuttle_town").html("");
		that.getChildren(code, 4, that.displayTown);
	}
	
	this.displayTown= function(data) {
		var len = data.length;
		if(len < 1) {
			return;
		}
		data = data.sort(function(a, b) { return a.ADMINCODE - b.ADMINCODE });		
		var h='';
		for( var i=0; i<len; i++ ) {
			var p = data[i];
			var selected = that.isSelected(p.ADMINCODE, 4);
			h += '<li '+(selected?'class="active"':'')+' code="'+ p.ADMINCODE +'" level="4" name="'+p.TOWN+'"><span>'+p.TOWN+'</span></li>';
		}
		$("#shuttle_town").html( h );
	}
	
	this.getChildren = function(code, level, callback) {
		Dituhui.request({
			url: urls.server + "/orderService/getAdminElements?",
	        data: {
	        	admincode: code,
	        	level: level
	        },
	        dataType: 'json',
	        success: function(e){
	        	if(e.isSuccess && e.result) {
	        		callback(e.result);
	        	}
	        },
	        error: function(){}
		});
	}
	
	//已经选择的省
	this.selectedAdmins = [];
	/**
	 * 选择
	 */
	this.onSelect = function(me){
		if(me.hasClass("active")) {
			return;
		}
		var code = me.attr("code");
		var level = me.attr("level");
		var name = me.attr("name");
		
		var selected = [code, level, name];
		if( that.isAgain(selected) ) {
			return;
		}
		me.addClass('active');
		that.selectedAdmins.push(selected);
		that.refreshSelectedAdminnames();
	}
	
	/**
	 * 去重
	 * 类似四川省、成都市 金牛区 XXX乡镇 这种有包含关系的行政区域不能同时选择，
	 *	1、如果已经选了成都，再去选择四川省时给出提示：四川省包含已选的行政区划，不能同时选择
	 *	2、如果已经选择了四川省，再去选择成都市给出提示：已选的行政区划包含成都市，不能同时选择
	 */
	this.isAgain = function(selected) {
		var array = that.selectedAdmins;
		var len = array.length;
		if(len < 1) {
			return false;
		}
		if(array.indexOf(selected) > -1) {
			return true;
		}
		for(var i=len; i--;) {
			var code = array[i][0];
			var level = array[i][1];
			var scode = selected[0];
			var slevel = selected[1];
			if(level == slevel) {
				continue;
			}
			
			//已选上级，再选下级
			if(Number(slevel) > Number(level)) {
				if(level == 1 && code.substring(0,2) === scode.substring(0,2)) {
					Dituhui.showHint("已选的行政区划包含"+selected[2]+"，不能同时选择")
					return true;
				}
				else if(level == 2 && code.substring(0,4) === scode.substring(0,4)) {
					Dituhui.showHint("已选的行政区划包含"+selected[2]+"，不能同时选择")
					return true;
				}
				else if(level == 3 && code.substring(0,6) === scode.substring(0,6)) {
					Dituhui.showHint("已选的行政区划包含"+selected[2]+"，不能同时选择")
					return true;
				}
			}
			//已选下级，再选上级
			else {
				if(code.substring(0,2) === scode.substring(0,2)) {
					Dituhui.showHint(selected[2] + "包含已选的行政区划，不能同时选择");
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 是否已经选中过
	 */
	this.isSelected = function(code, level) {
		var array = that.selectedAdmins.concat();
		var len = array.length;
		if(len < 1) {
			return false;
		}
		for(var i=len; i--;) {
			if(array[i][0] === code && level == array[i][1]){
				return true;
			}
		}
		return false;
	}
	/**
	 * 展示已选择的行政区划
	 */
	this.refreshSelectedAdminnames = function() {
		var data = this.selectedAdmins;
		var len = data.length;
		if(len < 1) {
			$("#selected_adminnames").html("");
			return false;
		}
		var h = "";
		for(var i=0; i<len; i++) {
			var item = data[i];
			h += '<li index="'+i+'" name="'+item[2]+'" level="'+item[1]+'" code="'+item[0]+'">'
			   + '	<span class="text">'+(i+1)+'. '+item[2]+'</span>'
			   + '	<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>'
			   + '</li>';
		}
		$("#selected_adminnames").html(h);
	}
	/**
	 * 删除一个选中的行政区划
	 */
	this.removeSelectedAdmin = function() {
		var me = $(this).parent("li");
		var index = Number(me.attr("index"));
		
		var code = me.attr("code");
		var level = me.attr("level");
		$('.shuttle li[code="'+code+'"]').removeClass("active");
		
		that.selectedAdmins.splice(index, 1);
		that.refreshSelectedAdminnames();
	}
	/**
	 * 保存
	 */
	this.save = function() {
		var data = that.selectedAdmins.concat();
		if(data.length === 0) {
			Dituhui.showHint("请选择行政区划");
			return;
		}
		var string = data.join(";");
		
		Dituhui.request({
			url: urls.server + "/areaService/saveSelectedAdmins?",
	        data: {
	        	adminsLevels: string,
	        },
	        dataType: 'json',
	        success: function(e){
	        	that.stopProcessTimer();
	        	if(e.isSuccess) {
	        		var h = e.info ? e.info : '行政区划导入成功';
					Dituhui.Modal.loaded_right(h, function(){
						Dituhui.Modal.hide();
						Map.search();
						map.removeAllPopup();
						Map.clearSelectedFeatures();
					});
	        	}
	        	else {
	        		var info = e.info;
					if(info && info !== "") {
						Dituhui.Modal.loaded_wrong(info, function(){
							$('.data-modal').addClass("hide");
						});
					}
					else {
						Dituhui.showHint("导入行政区划失败，请稍候重试");					
					}
	        	}
	        },
	        error: function(){}
		});
		
		$(".data-import-regins").fadeOut();
		var h = '行政区划导入中，【<span class="upload-process">1</span>/'+ data.length +'】';
		Dituhui.Modal.loading(h);
		that.startProcessTimer();
		that.clear();
	}
	
	var processTimer = null;
	this.startProcessTimer = function() {
		processTimer = setInterval(that.getProcess, 1000);
	}
	this.stopProcessTimer = function() {
		clearInterval(processTimer);
	}
	this.getProcess = function(callback) {
		Dituhui.request({
	        url: urls.server + "/areaService/getDataProcess?",
	        type: "GET",
	        success: function(e){
	            if(e.isSuccess) {
	            	var cur = e.result.current;
	                $('.upload-process').html(cur==0?1:cur);
	                if(cur === e.result.total && e.result.total!=0) {
                		that.stopProcessTimer();
                		if(that.autoload) {
                			var h = e.info ? e.info : '行政区划导入成功';
							Dituhui.Modal.loaded_right(h, function(){
								Dituhui.Modal.hide();
								Map.search();
								map.removeAllPopup();
								Map.clearSelectedFeatures();
							});
							that.autoload = false;
                		}
	                }
	                if(typeof(callback)==="function") {
	                	callback(e.result.total, e.result.current);
	                }
	            }
	            else {
	                
	            }
	        },
	        error: function(){
	        }
	    });
	}
	this.clear = function(){
		that.selectedAdmins = [];
		that.refreshSelectedAdminnames();
		$("#shuttle_city").html("");
		$("#shuttle_county").html("");
		$("#shuttle_town").html("");
		$('.shuttle li.active').removeClass("active");
	}
}













