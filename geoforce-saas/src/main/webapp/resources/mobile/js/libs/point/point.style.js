Point.Style = {
	data: []
};
Point.Style.search = function() {	
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/getAllPointStyle?",
        success: function(e){
            if(e.isSuccess && e.result) {
                var re = [];
                if( e.result.length === 0 ) {
                    
                }
                else {
                    var data_styles = [];                
	                var datas = e.result;
	                for(var i=datas.length; i--;) {
	                    var item = datas[i];
	                    var style = {
	                        id: item.id,
	                        color: item.appcolor ? item.appcolor : 'e51c23',
	                        back: item.appearance ? item.appearance : 'default',
	                        ico: item.apppic ? item.apppic : '',
	                        size: item.appsize ? item.appsize : '16',
	                        name: item.stylename,
	                        img: item.appcustom ? item.appcustom.replace(/\\/g, '/') : '',
	                        width: item.def1 ? item.def1.split(',')[0] : '',
	                        height: item.def1 ? item.def1.split(',')[1] : '',
	                        groupid: item.def2 ? item.def2 : '0'
	                    }
	                    style.colorpath = Point.Style.getColor(style.color).path;
	                    if(item.stylename === '默认样式') {
	                        Point.Style.defaultStyle = style;
	                        continue;
	                    }
	                    data_styles.push(item);
	                }
	                Point.Style.data = data_styles.concat();
                }
            }
            else {
                
            }
        },
        error: function(){
            
        }
    });
}
Point.Style.getStyleById = function(id) {
	var data = Point.Style.data;
	for(var i=data.length; i--; ) {
		var item = data[i];
		if( id === item.id ) {
			return item;
		}
	}
	return Point.Style.defaultStyle;
}
/*
 * 获取聚合样式
 */
Point.Style.getCluster = function(count) {
	var color = 'blue', w=82;
	if(count <= 100) {
		color = 'green';
	}
	else if( count > 100 && count <= 500 ) {
		color = 'blue';
		w = 102;
	}
	else if(count > 500 && count <= 1000) {
		color = 'indigo';
		w = 122;
	}
	else if( count > 1000 && count <= 5000 ) {
		color = 'orange';
		w = 142;
	}
	else if(count > 5000 && count <= 8000) {
		color = 'red';
		w = 162;
	}
	else {
		color = 'purple';
		w = 182;
	}
    return {
        externalGraphic: urls.server + '/resources/images/point/'+color+'.png',
        graphicWidth: w,
        graphicHeight: w,
        fontColor: '#ffffff',
        fontFamily: 'microsoft yahei',
        cursor: 'pointer',
        fontSize: '12'
    }
}

/** 
 * 默认样式
 */
Point.Style.defaultStyle = {
    color: 'e51c23',
    back: 'default',
    ico: '',
    size: '12',
    name: '默认样式',
    img: '',
    id: '',
    colorpath: 'red',
    groupid: '0',
    width: '',
    height: ''
}

Point.Style.getBranchStyle = function(branch) {
    var defaultStyle = Point.Style.defaultStyle;

    var icon, style = {}, styleid = false;

    styleid = Point.Style.getStyleById(branch);

    if(branch) {
        if(styleid.appcustom) {
            var h = styleid.def1 ? styleid.def1.split(',')[1] : 24;
            var w = styleid.def1 ? styleid.def1.split(',')[0] : 24;
            icon = Point.Style.getStyleSize(h,'transparent','fff'); 
            style.externalGraphic = urls.server + '/pointService/getImg?path='+ styleid.appcustom;
            style.graphicWidth = w;
            style.graphicHeight = h;
        }
        else {
            icon = Point.Style.getStyleSize( 
                styleid.appsize, 
                styleid.appearance ? styleid.appearance : 'transparent', 
                styleid.appcolor
            );
            style.isUnicode = styleid.apppic ? true : false;
            style.label = Point.Style.getFontLabel(styleid.apppic);
            style.fontSize = styleid.appsize;
            style.labelYOffset = 5;

            var color_path = Point.Style.getColor(styleid.appcolor);
            style.externalGraphic = urls.server + '/resources/assets/map/'+ color_path.path +'/'+ styleid.appearance +'.svg';    
            style.graphicWidth = icon.size.w;
            style.graphicHeight = icon.size.h;             
        }
    }
    else { 
        var s = Point.Style.defaultStyle;
        if(s.img != '') {
            icon = Point.Style.getStyleSize( s.height,'transparent','fff');
            style.externalGraphic = urls.server + '/pointService/getImg?path='+  (s.img);
            style.graphicWidth = s.width;
            style.graphicHeight = s.height;
        }
        else {
            icon = Point.Style.getStyleSize( s.size, s.back, s.color);
            style.externalGraphic = urls.server + '/resources/assets/map/'+ s.colorpath +'/'+ s.back +'.svg';
            style.isUnicode = s.ico ? true : false;
            style.label = Point.Style.getFontLabel(s.ico);
            style.fontSize = s.size;    
            style.graphicWidth = icon.size.w;
            style.graphicHeight = icon.size.h;        
        }
    }

    style.fontColor = '#' + icon.color;
    style.fontFamily = "iconfont";
    style.cursor = 'pointer';
    style.labelXOffset = icon.offset.x;
    style.labelYOffset = icon.offset.y;
    return style;
}



/** 
 * 颜色及色值对应分类
 */
Point.Style.colors = [
    {color: 'e51c23', path: 'red'},
    {color: '9c27b0', path: 'purple'},
    {color: '0066ff', path: 'blue'},
    {color: '259b24', path: 'green'},
    {color: 'ff8800', path: 'orange'},
    {color: '795548', path: 'brown'},
    {color: '10a0ad', path: 'indigo'},
    {color: '000', path: 'black'}
];
/** 
 * 获取颜色
 */
Point.Style.getColor = function(color) {
    var colors = Point.Style.colors.concat();
    for(var i=colors.length; i--; ) {
        var c = colors[i];
        if(c.color == color) {
            return c;
        }
    }
    return colors[0];
}




/** 
 * 根据尺寸计算外观大小及图案偏移
 * @param - iconsize - Number - 图案的尺寸
 * @param - back - String - 外观的名称
 * @param - backcolor - String - 外观的颜色
 */
Point.Style.getStyleSize = function(iconsize, back, backcolor) {
    //外观的尺寸
    var size = {
        w: 16,
        h: 16
    };
    //图案的偏移量
    var offset = {
        x: 0,
        y: 0,
        margin: '0 auto', //示例里面外观的margin
        line_height: '16' //示例里面图案的行高
    };    
    //图标中的
    var color = 'fff'; 

    iconsize = Number(iconsize);
    switch(back) {
        //传统
        case 'default':
            size = {
                w: Math.ceil(iconsize*1.5),
                h: Math.ceil(iconsize*1.5*1.6)
            }
            offset = {
                x: 0,
                y: iconsize*0.5,
                margin: (128-size.h)*0.5 + 'px auto 0',
                line_height: size.h*0.7
            }
            break;
        //圆形
        case 'circle':
            size = {
                w: Math.ceil(iconsize*2),
                h: Math.ceil(iconsize*2)
            }
            offset = {
                x: 0,
                y: 0,
                margin: (128-size.h)*0.5 + 'px auto 0',
                line_height: size.h
            }
            break;
        //方形
        case 'square':
            size = {
                w: Math.ceil(iconsize*1.5),
                h: Math.ceil(iconsize*1.5*1.4)
            }
            offset = {
                x: 0,
                y: Math.round(iconsize/3),
                margin: (128-size.h)*0.5 + 'px auto 0',
                line_height: size.h*0.75
            }
            break;
        //菱形
        case 'diamond':
            size = {
                w: Math.ceil(iconsize*2),
                h: Math.ceil(iconsize*2)
            }
            offset = {
                x: 0,
                y: 0,
                margin: (128-size.h)*0.5 + 'px auto 0',
                line_height: size.h
            }
            break;
        //旗帜
        case 'flag':
            size = {
                w: Math.ceil(iconsize*3),
                h: Math.ceil(iconsize*21/8)
            }
            offset = {
                x: Math.round(iconsize*0.2),
                y: Math.round(iconsize*0.3),
                margin: (128-size.h)*0.5 + 'px auto 0',
                line_height: size.h*0.75
            }
            break;
        case 'transparent':
            color = backcolor;
            size = {
                w: iconsize,
                h: iconsize
            }
            offset = {
                x: 0,
                y: 0,
                margin: (128-size.h)*0.5 + 'px auto 0',
                line_height: size.h
            }
            break;
    }
    return {
        size: size, 
        offset: offset,
        color: color
    };
}
/** 
 * font-label
 */
Point.Style.fontLabel = {
    'icon-font01':  "&#xe600;",
    'icon-font02':  "&#xe601;",
    'icon-font03':  "&#xe602;",
    'icon-font04':  "&#xe603;",
    'icon-font05':  "&#xe604;",
    'icon-font06':  "&#xe605;",
    'icon-font07':  "&#xe606;",
    'icon-font08':  "&#xe607;",
    'icon-font09':  "&#xe608;",
    'icon-font10':  "&#xe609;",
    'icon-font11':  "&#xe60a;",
    'icon-font12':  "&#xe60b;",
    'icon-font13':  "&#xe60c;",
    'icon-font14':  "&#xe60d;",
    'icon-font15':  "&#xe60e;",
    'icon-font16':  "&#xe60f;",
    'icon-font17':  "&#xe610;",
    'icon-font18':  "&#xe611;",
    'icon-font26':  "&#xe612;",
    'icon-font27':  "&#xe613;",
    'icon-font28':  "&#xe614;",
    'icon-font29':  "&#xe615;",
    'icon-font30':  "&#xe616;",
    'icon-font31':  "&#xe617;",
    'icon-font19':  "&#xe618;",
    'icon-font20':  "&#xe619;",
    'icon-font21':  "&#xe61a;",
    'icon-font22':  "&#xe61b;",
    'icon-font23':  "&#xe61c;",
    'icon-font24':  "&#xe61d;",
    'icon-font25':  "&#xe61e;",
    'icon-font35':  "&#xe61f;",
    'icon-font36':  "&#xe620;",
    'icon-font38':  "&#xe621;",
    'icon-font39':  "&#xe622;",
    'icon-font41':  "&#xe623;",
    'icon-font42':  "&#xe624;",
    'icon-font44':  "&#xe625;",
    'icon-font45':  "&#xe626;",
    'icon-font47':  "&#xe627;",
    'icon-font48':  "&#xe628;",
    'icon-font50':  "&#xe629;",
    'icon-font51':  "&#xe62a;",
    'icon-font53':  "&#xe62b;",
    'icon-font54':  "&#xe62c;",
    'icon-font56':  "&#xe62d;",
    'icon-font32':  "&#xe62e;",
    'icon-font33':  "&#xe62f;",
    'icon-font34':  "&#xe630;",
    'icon-font37':  "&#xe631;",
    'icon-font40':  "&#xe632;",
    'icon-font43':  "&#xe633;",
    'icon-font46':  "&#xe634;",
    'icon-font49':  "&#xe635;",
    'icon-font52':  "&#xe636;",
    'icon-font55':  "&#xe637;"
};
/** 
 * 获取网点显示的font-label
 */
Point.Style.getFontLabel = function(attr) {
    var l = Point.Style.fontLabel;
    if( l[attr] ) {
        return l[attr];
    }
    return '';
}



/** 
 * 区划文字样式
 */
Point.Style.getLabelStyle = function(lbl, y, ly){ 
    var width = 68;
    if(lbl && lbl.length > 5) {
        lbl = lbl.substr(0, 5) + '...';
        width = 80;
    }
    var style = {
        externalGraphic: urls.server + '/resources/css/imgs/point/label_back1.png',
        graphicWidth: width,
        graphicHeight: 30,
        graphicYOffset: y,
        label: lbl,
        labelYOffset: ly,
        labelXOffset: 0,
        fontColor: "#212121",
        fontSize: "12",
        fontFamily: "微软雅黑"
    }
    return style;
}

/** 
 * 当前定位
 */
Point.Style.locationStyle = function(){ 
    var style = {
        externalGraphic: '../css/imgs/map/ball.png',
        graphicWidth: 16,
        graphicHeight: 16
    }
    return style;
}







