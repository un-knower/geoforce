if('undefined' == typeof(SMap)){
	SMap = {
		author:"闵益飞"
	};
}

/**
 * 城市选择控件
 * author:闵益飞
 * data:2011-7-15
 */
SMap.CitySelect = function(option){
	this._init(option);
}
SMap.CitySelect.prototype = {
	//配置信息
	options:{
		offset:{x:0,y:0},
		popupClass:"popupLayer",
		selCityAClass:"popupSelectCityTagA",
		titleClass:"popupTitle",
		closeClass:"popupClose",
		selCityClass:"popupSelectCity",
		triggerClass:"popupSelectCityDiv",
		popupNoSelectLiClass:"popupNoSelectLi",
		popupSelectContentWrapClass: "popupSelectContentWrap",
		popupSelectContentTdClass: "popupSelectContentTd",
		popupSelectContentClass:"popupSelectContent",
		popupSelectContentTitleClass:"popupSelectContent_title"
	},
	//全局索引号
	index:0,
	//城市显示对象
	selCity:null,
	//促发对象
	trigger:null,
	//弹出层
	popupLayer:null,
	//选择完成事件
	selectEvent:function(){
		//do nothing
	},	
	//初始化参数	
	_init:function(option){
		//默认值
		var defaultData = option.defaultData;
		
		this.trigger = jQuery("#"+option.id);
		this.trigger.addClass(this.options.triggerClass);
		//城市显示对象
		this.selCity = jQuery(document.createElement("div")).attr("id", "selectCityDiv");
		if(defaultData && defaultData.text && defaultData.value){
			this.selCity.html(defaultData.text);
		}
		this.selCity.addClass(this.options.selCityClass);
		//城市选择对象
		var selCityA = jQuery(document.createElement("a"));		
		selCityA.html("[选择城市]").attr("href","javascript:void(0)").addClass(this.options.selCityAClass).css("color", "#3D6DCC");
		
		var that = this;
		//设置城市选择对象激发事件
		selCityA.click(function(event){
			that.show();
			event.stopPropagation();
		})
		this.trigger.append(this.selCity);
		this.trigger.append(selCityA);
		//河北省1.2742716173664672E7,4584517.177225902, 黑龙江1.409993832222803E7,5739205.612534873, 江苏1.32206687450949E7,3771343.2153001367,青海1.1330104897386795E7,4386409.225337919,宁夏1.1828657585866282E7,4646203.109348809,香港1.2708536637211505E7,2545344.2450692523,台湾1.3527079094407598E7,2881954.339704872
		
		
		var divPosition = {left:this.trigger.offset().left,top:this.trigger.offset().top};
		//城市列表弹窗口
		this.popupLayer = jQuery(document.createElement("div")).addClass(this.options.popupClass);
		
		var divTitle = jQuery(document.createElement("div")).addClass(this.options.titleClass);
		divTitle.html("城市列表");
		var divClose = jQuery(document.createElement("div")).addClass(this.options.closeClass);
		divClose.html("关闭");
		divClose.click(function(event){
			that.hide();
			event.stopPropagation();
		})
		this.popupLayer.append(divTitle).append(divClose);
		var divContent = jQuery(document.createElement("div"));
		divContent.addClass(this.options.popupSelectContentWrapClass);	
		
		
		/* 加入热点城市 */
		var div_hot = jQuery(document.createElement("div"));
		div_hot.addClass("divHotCitys");
		
		var length_citys_hot = citys_hot.length;
		for(var i = 0; i < length_citys_hot; i++){
			var city_hot = citys_hot[i];
			
			var a = jQuery(document.createElement("a"));
			a.attr("href","javascript:void(0)").css("line-height", "21px");	
			
			var level = 1;
			
			if(city_hot.city == "全国"){
				a.css("font-weight", "bold");
				level = -1;
			}
			
			a.bind("click",
				{ 	
					value:city_hot.admincode,
					text:city_hot.city,
					parentCode:city_hot.province,
					x:city_hot.x,
					y:city_hot.y,
					level: level,
					zoomLevel:city_hot.level
				},
				function(e){
					that.selCity.html(e.data.text)
					that.selectEvent(e.data);
					that.popupLayer.hide();
					e.stopPropagation();
				}
			);
			a.html(city_hot.city);
			a.addClass("popupNoSelectItem");
								
			div_hot.append(a);
		}
		
		/*
		var table = jQuery(document.createElement("table"));
		table.addClass(this.options.popupSelectContentTitleClass);
		
		var tr = jQuery(document.createElement("tr"));
		
		var td = jQuery(document.createElement("td")).attr("colspan", "2").css("padding-bottom", "4px");
		td.append(jQuery(document.createElement("a")).html("全国")
		.attr("href","javascript:void(0)")
		.css("color", "#3D6DCC").css("text-decoration", "none").css("font-weight", "bold").css("line-height", "21px")
		.bind("click",{},function(e){
						that.selCity.html("全国");
						that.selectEvent({value:'0',text:"全国",x:1.3023822823483625E7,y:3892550.351786297,level:-1});
						that.popupLayer.hide();
						e.stopPropagation();
					}));
		// td.append(jQuery("<span>(含34个省、自治区、直辖市、特别行政区、340个地级市)</span>")
		// .css({"color":"#999", "font-size":"12px"}));
		tr.append(td);
		
		
		var tdContent = jQuery(document.createElement("table"));
		tdContent.addClass(this.options.popupSelectContentTdClass);
			
		var length_citys_hot = citys_hot.length;
		for(var i = 0; i < length_citys_hot; i++){
			var city_hot = citys_hot[i];
			
			var nobr = jQuery(document.createElement("nobr"));
			var a = jQuery(document.createElement("a"));
			a.attr("href","javascript:void(0)").css("line-height", "21px");	
			
			if(city_hot.city == "全国"){
				a.css("font-weight", "bold")
			}
			a.bind("click",
				{value:city_hot.admincode,text:city_hot.city,parentCode:city_hot.province,x:city_hot.x,y:city_hot.y,level: level,zoomLevel:city_hot.level},
				function(e){
					that.selCity.html(e.data.text)
					that.selectEvent(e.data);
					that.popupLayer.hide();
					e.stopPropagation();
				}
			);
			nobr.append(a);
			a.html(city_hot.city);
			a.addClass("popupNoSelectItem");
								
			tdContent.append(nobr);
		}
		tr.append(tdContent);
		table.append(tr);
		*/
	
		
		var table_content = jQuery(document.createElement("table"));
		table_content.addClass(this.options.popupSelectContentClass);
		
		var temp_provinces = [];
		var temp_provinces_alpha = [];
		var temp_proince_other = null;
		for(var i=provinces.length-1;i>0;i--){
			var province = provinces[i];
			if(province[0] == "热点城市"){
				temp_provinces.push(province);
			}
			else if(province[0] == "其它"){
				temp_proince_other = province;
			}
			else{
				temp_provinces_alpha.push(province);	
			}
		}
		temp_provinces_alpha.sort( createComparsionByAlphaFunction(0) );
		temp_provinces = temp_provinces.concat(temp_provinces_alpha);
		temp_provinces.push(temp_proince_other);
		
		var length_temp_provinces = temp_provinces.length;
		for(var i=0; i<length_temp_provinces; i++){		
			var province = temp_provinces[i];
			var tr = jQuery(document.createElement("tr"));
			var td = jQuery(document.createElement("td")).addClass(this.options.popupNoSelectLiClass);
			if(province[5] == 0){
				td.html(province[0]);
			}else{
				td.append(jQuery(document.createElement("a")).html(province[0])
				.attr("href","javascript:void(0)")
				.bind("click",{value:province[1],text:province[0],x:province[3],y:province[4],level:province[5]},function(e){
					that.selCity.html(e.data.text);
					that.selectEvent(e.data);
					that.popupLayer.hide();
					e.stopPropagation();
				}));
			}
			tr.append(td);
			
			
			var tdContent = jQuery(document.createElement("td"));
			tdContent.addClass(this.options.popupSelectContentTdClass);
			
			// for(var j=citys.length-1;j>0;j--){	
			var length_citys = citys.length;
			var temp_citys = [];
			var j = 0;
			for(j=0;j<length_citys;j++){	
				var city = citys[j];
				var city_parentCode = city.admincode.slice(0, 2) + "0000";
				if(city.province == '其它'){
					city_parentCode = '2';
				} else if (city.province == "热点城市") {
					city_parentCode = '1';
				}
				if(city_parentCode == province[1]){
					temp_citys.push( citys[j] );
				}
			}
			temp_citys.sort( createComparsionFunction('admincode') );
			var length_temp_citys = temp_citys.length;
			for(j=0; j < length_temp_citys; j++){
				var city = temp_citys[j];
				var nobr = jQuery(document.createElement("nobr"));
				var a = jQuery(document.createElement("a"));
				a.attr("href","javascript:void(0)");	
				var level = 2;
				if(city.city == '台湾'){
					level = 0;
				}
				else if(city.province == "热点城市"){
					level = 1;
				}					
				a.bind("click",
				{value:city.admincode,text:city.city,parentCode:city.province,x:city.x,y:city.y,level: level,zoomLevel:city.level},
				function(e){
					that.selCity.html(e.data.text)
					that.selectEvent(e.data);
					that.popupLayer.hide();
					e.stopPropagation();
				});
				nobr.append(a);
				a.html(city.city);
				a.addClass("popupNoSelectItem");
				//var span = jQuery(document.createElement("span"));
				//span.html(city[0]);					
				//nobr.append(span);					
				tdContent.append(nobr);
			}
			tr.append(tdContent);
			table_content.append(tr);
		}	
		divContent.append(div_hot);
		divContent.append(table_content);
		this.popupLayer.append(divContent);
		this.popupLayer.click(function(event){
			that.show();
			event.stopPropagation();
		})
		that.hide();
		jQuery(document.body).append(this.popupLayer).bind("click",function(){
			that.hide();
		});
	},
	//显示选择列表
	show:function(){
		var offset = this.trigger.offset();
		//offset.x = 
		//alert("x:"+offset.x+",Y:"+offset.y);
		//alert(offset.x)
		this.popupLayer.css("left",offset.left).css("top",offset.top+this.trigger.innerHeight());	
		this.popupLayer.show();
	},
	hide:function(){
		this.popupLayer.hide();
	},
	//设置选择完成后执行事件
	setAfterSelect:function(afterEvent){
		this.selectEvent = afterEvent;	
	}	
}
SMap.CitySelect.prototype.constructor = SMap.CitySelect;


/**
 * HashMap类库
 */
SMap.HashMap = function(){
	this.init();
}
SMap.HashMap.prototype = {
	//大小
	_size:0,
	//对象集合
	_entry:null,
	//初始化
	init:function(){
		this._size = 0;
		this._entry = new Object()
	},
	//添加
    add: function (key, value) {     	
        if (!this.containsKey(key)) {
            this._size++;
        }
        this._entry[key] = value;
    },
    //获取
    get: function (key) {    
        return this.containsKey(key) ? this._entry[key] : null;
    },
    //移除
    remove: function (key) {    
        if (this.containsKey(key) && (delete this._entry[key])) {
            this._size--;
        }
    },
    //通过key查看是否已经存在
    containsKey: function (key) {    
        return (key in this._entry);
    },
    //查看是否还有该值
    containsValue: function (value) {    
        for (var prop in this._entry) {
            if (this._entry[prop] == value) {
                return true;
            }
        }
        return false;
    },
    //大小
    size: function () {   
        return this._size;
    },
    //清空
    clear: function () {    
        this._size = 0;
        this._entry = new Object();
    },
    //取得所有值
    values: function () {    
        var values = new Array();
        for (var prop in this._entry) {
            values.push(this._entry[prop]);
        }
        return values;
    }
}
SMap.HashMap.prototype.constructor = SMap.HashMap;


/**
 * 数组按属性大小排序
 */
function createComparsionFunction(propertyName){
    return function(object1, object2){
        var value1 = object1[propertyName];
        var value2 = object2[propertyName];
        if (value1 < value2) {
            return -1;
        } 
        else if (value1 > value2) {
            return 1;
        } 
        else {
            return 0;
        }
    }
}
/**
 * 数组按属性拼音排序
 */
function createComparsionByAlphaFunction(index){
    return function(object1, object2){
        var a = object1[index];
        var b = object2[index];
        return a.localeCompare(b);
    }
}