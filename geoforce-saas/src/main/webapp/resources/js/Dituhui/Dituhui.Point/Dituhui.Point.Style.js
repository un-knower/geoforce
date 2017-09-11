
/** 
 * 网点样式
 */
Dituhui.Point.Style = {};/** 
 * 默认样式
 */
Dituhui.Point.Style.defaultStyle = {
    color: 'e51c23',
    back: 'default',
    ico: '',
    size: '12',
    name: '默认样式',
    img: '',
    id: '',
    colorpath: 'red',
    groupid: '0',
    width: '', //自定义图片的宽
    height: '' //自定义图片的高
};
/** 
 * 还原默认样式
 */
Dituhui.Point.Style.setDefaultStyle = function(){
    Dituhui.Point.Style.defaultStyle = {
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
}
/** 
 * 其他自定义样式
 */
Dituhui.Point.Style.data = [];
/** 
 * 颜色及色值对应分类
 */
Dituhui.Point.Style.colors = [
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
Dituhui.Point.Style.getColor = function(color) {
    var colors = Dituhui.Point.Style.colors.concat();
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
Dituhui.Point.Style.getStyleSize = function(iconsize, back, backcolor) {
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
 * 新增样式
 */
Dituhui.Point.Style.add = function(param, success, failed) {
    if( !param ) {
        return;
    }
    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/pointService/addPointStyle?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : "新增样式失败");
            }
        },
        error: function(){
            Dituhui.hideMask();  
            failed("新增样式失败");
        }
    })
}
/** 
 * 修改样式
 */
Dituhui.Point.Style.edit = function(param, success, failed) {
    if( !param ) {
        return;
    }
    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/pointService/updatePointStyle?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : "修改样式失败");
            }
        },
        error: function(){
            Dituhui.hideMask();  
            failed("修改样式失败");
        }   
    });
}

/** 
 * 删除样式
 */
Dituhui.Point.Style.remove = function(id, success, failed) {
    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/pointService/deletePointStyle?",
        data: {styleid: id},
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : '自定义样式删除失败');
            }
        },
        error: function(){
            Dituhui.hideMask();  
            failed('自定义样式删除失败');          
        }        
    });
}

/** 
 * 查询样式
 */
Dituhui.Point.Style.search = function() {
    Dituhui.showMask();
    Dituhui.Point.Style.data = [];
    Dituhui.request({
        url: urls.server + "/pointService/getAllPointStyle?",        
        success: function(e){
            Dituhui.hideMask();
            var data = [];
            if(e.isSuccess && e.result && e.result.length>0) {
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
                        name: item.def2 ? item.groupname : item.stylename,
                        img: item.appcustom ? item.appcustom.replace(/\\/g, '/') : '',
                        width: item.def1 ? item.def1.split(',')[0] : '',
                        height: item.def1 ? item.def1.split(',')[1] : '',
                        groupid: item.def2 ? item.def2 : '0'
                    }
                    style.colorpath = Dituhui.Point.Style.getColor(style.color).path;
                    if(item.stylename === '默认样式') {
                        style.groupid = '0';
                        Dituhui.Point.Style.defaultStyle = style;
                        continue;
                    }
                    data_styles.push(style);
                }
                Dituhui.Point.Style.data = data_styles.concat();
            }
            Dituhui.Point.Style.display();
        },
        error: function(){
            Dituhui.hideMask();  
            Dituhui.Point.Style.display();
        }   
    });
}

/** 
 * 样式显示至div中
 */
Dituhui.Point.Style.display = function(){
    var div = $('.tab-icon-list');

    var h = '';
    var item = Dituhui.Point.Style.defaultStyle;

    h += Dituhui.Point.Style.getEditIconStyleHtml(item, true);

    var data = Dituhui.Point.Style.data.concat();
    for(var i=data.length; i--;) {
        item = data[i];
        h += Dituhui.Point.Style.getEditIconStyleHtml(item, false);        
    }
    if(data.length < 10 && Dituhui.User.isTop) {
        h += Dituhui.Point.Style.getAddIconStyleHtml();
    }
    div.html(h);    
    if(Dituhui.User.isTop) {
    	$('div[data-option="edit-icon-style"]').unbind('click').click(Page.showEditIconStyle);
    }
}
/** 
 * 默认样式显示至div中
 */
Dituhui.Point.Style.getEditIconStyleHtml = function(item, isDefault) {
    if(isDefault) {
        item.name = '默认样式';
    }
    var style;
    var styleString = '', spanStyleString = '', spanClass = '';
    if(item.img) {
        style = Dituhui.Point.Style.getStyleSize( item.height,'transparent','fff');        
        styleString  = 'width:' + item.width + 'px;';
        styleString += 'height:' + style.size.h + 'px;';
        styleString += 'margin:' + style.offset.margin + ';';
        styleString += 'background-image:url('+ urls.server +'/pointService/getImg?path='+  (item.img) +');';
    }    
    else {
        style = Dituhui.Point.Style.getStyleSize( item.size, item.back, item.color);
        styleString  =  'width:' + style.size.w + 'px;';
        styleString += 'height:' + style.size.h + 'px;';
        styleString += 'margin:' + style.offset.margin + ';';
        styleString += 'background-image:url('+ urls.server +'/resources/assets/map/'+ item.colorpath + '/'+ item.back +'.svg);';
        spanStyleString =  'font-size:' + item.size + 'px;';    
        spanStyleString += 'color:#' + style.color + ';';    
        spanStyleString += 'line-height:' + style.offset.line_height + 'px;';
        spanClass = 'iconfont '+ item.ico;
    }
    var h = '';
    h += '<div class="branch-icon"' +  (isDefault ? ' data-attr="default"' : '')  +' data-option="edit-icon-style" title="点击编辑样式" >';
    h += '  <div class="icon-branch" data-color="'+ item.color +'" data-back="'+item.back+'" data-ico="'+ item.ico +'" data-size="'+ item.size +'" data-name="'+ Dituhui.setStringEsc(item.name) +'" data-img="'+ item.img +'" data-id="'+ item.id +'" data-groupid="'+ item.groupid +'" style="'+ styleString +'">';    
    h += '      <span class="'+ spanClass +'" style="'+ spanStyleString +'"></span>';
    h += '  </div>';
    h += '  <div class="bottom">'+ Dituhui.setStringEsc(item.name) +'</div>';
    h += '</div>';
    return h;
}
/** 
 * 新增样式显示至div中
 */
Dituhui.Point.Style.getAddIconStyleHtml = function() {
    var h ='';
    h += '<div class="add-branch-icon" title="新建样式" data-option="edit-icon-style" data-type="add">';
    h += '  <span class="glyphicon glyphicon-plus add-icon-style"></span>';
    h += '  <div class="bottom">新建样式</div>';
    h += '</div>';
    return h;
}
/** 
 * font-label
 */
Dituhui.Point.Style.fontLabel = {
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
Dituhui.Point.Style.getFontLabel = function(attr) {
    var l = Dituhui.Point.Style.fontLabel;
    if( l[attr] ) {
        return l[attr];
    }
    return '';
}




/** 
 * 网点分组
 */
Dituhui.Point.Group = {};
Dituhui.Point.Group.data = [];
Dituhui.Point.Group.search = function(isSelected) {
    Dituhui.showMask();
    Dituhui.Point.Group.data = [];
    Dituhui.request({
        url: urls.server + "/pointService/getAllPointGroup?",
        success: function(e){
            Dituhui.hideMask();
            var data = [];
            if(e.isSuccess && e.result && e.result.length>0) {               
                Dituhui.Point.Group.data = e.result;
            }
            Dituhui.Point.Group.display(isSelected);
        },
        error: function(){
            Dituhui.hideMask();  
            Dituhui.Point.Group.display();
        }
    });
}
/** 
 * 网点分组的选择框
 */
Dituhui.Point.Group.display = function(isSelected){
    var data = Dituhui.Point.Group.data.concat();

    var select = $('select.branch-group');
    var h =  '<option value="0">无</option>';
    var h1 = '<option value="-1" selected="true">全部分组</option>';
        h1 += h;
    for(var i=data.length; i--; ) {
        var item = data[i];

        /*Dituhui.Point.Group.remove(item.id);
        continue;*/

        var defaultSelected = '';
        if(i == data.length - 1 && isSelected) {
            defaultSelected = 'selected="true"';
            $('input[name="groupid"]').val( item.id );
        }
        h += '<option value="'+ item.id +'" '+ defaultSelected +'>'+ Dituhui.setStringEsc(item.groupname) +'</option>';
        h1 += '<option value="'+ item.id +'">'+ Dituhui.setStringEsc(item.groupname) +'</option>';
    }
    if(data.length < 10 && Dituhui.User.isTop) {
        h += '<option value="add-new">新建分组</option>';
    }
    if(data.length > 0 && Dituhui.User.isTop) {
    	h += '<option value="remove-group">删除分组</option>';
    }
        
    select.html(h);
    $('select.select-group').html(h1);
    if(isSelected && $('.tab-edit').is(':visible')) {
    	var groupname = $('#txt_branchGroup option:selected').text();
    	$('#txt_styleName').val(groupname);
    	$('.icon-demo + .bottom').html(groupname);
    }
}
/** 
 * 添加网点分组
 */
Dituhui.Point.Group.add = function(param) {
    Dituhui.showMask();
    Dituhui.request({        
        url: urls.server + "/pointService/addPointGroup?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
                $('.data-branchgroup-add').fadeOut('fast');
                Dituhui.Point.Group.search(true);
            }
            else {
                Dituhui.showHint( e.info ? e.info : '分组添加失败');
            }            
        },
        error: function(){
            Dituhui.hideMask();  
            Dituhui.showHint('分组添加失败');
        }   
    })
}
/** 
 * 删除分组
 */
Dituhui.Point.Group.remove = function(id) {
    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/pointService/deletePointGroup?",
        data: {groupid: id},
        success: function(e){
            Dituhui.hideMask();
            Dituhui.Modal.hide();
            if(e.isSuccess) {
                Dituhui.showPopover('分组删除成功');
                Dituhui.Point.Group.search();
            }
            else {
                Dituhui.showHint(e.info ? e.info : '分组删除失败');
            }
        },
        error: function(){
            Dituhui.Modal.hide();
            Dituhui.hideMask();  
            Dituhui.showHint('分组删除失败');          
        }        
    });
}
/** 
 * 根据ID查询分组所含的样式
 */
Dituhui.Point.Group.getStyle = function(id, success, failed) {
    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/pointService/getAllPointGroup?",
        data: {groupid: id},
        success: function(e){
            Dituhui.hideMask();
            var data = [];
            if(e.isSuccess && e.result && e.result.length > 0) {       
                var data = e.result[0];
                if(data.styleid && data.styleid.id) {
                    success(data.styleid);
                }  
                else {
                	success(Dituhui.Point.Style.defaultStyle, true);
                }
            }
            else {
                $('#txt_styleName').val('');
                success(Dituhui.Point.Style.defaultStyle, true);
            }
        },
        error: function(){
            Dituhui.hideMask();  
            success(Dituhui.Point.Style.defaultStyle, true);
        }   
    })
}

/** 
 * 网点自定义图片
 */
Dituhui.Point.CustomIcon = {};
Dituhui.Point.CustomIcon.data = [];
Dituhui.Point.CustomIcon.search = function() {
    Dituhui.showMask();
    Dituhui.Point.CustomIcon.data = [];
    Dituhui.request({
        url: urls.server + "/pointService/getUserAllCustomfiles?",
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess && e.result && e.result.length > 0) {
                Dituhui.Point.CustomIcon.data = e.result;
            }
            Dituhui.Point.CustomIcon.display();        
        },
        error: function(){
            Dituhui.hideMask();  
            Dituhui.Point.CustomIcon.display();    
        }  
    });
}

/** 
 * 展示自定义图片
 */
Dituhui.Point.CustomIcon.display = function() {
    var selectedId = $('.icon-selector .custom li.action').attr('data-id');
    var data = Dituhui.Point.CustomIcon.data.concat();
    var h = '';
    for( var i=data.length; i--; ) {
        var item = data[i];
        var path = (item.filepath.replace(/\\/g, '/'));        
        h += '<li '+ (selectedId == item.id ? 'class="action"' : '') +' data-id="'+ item.id +'" data-width="'+ item.width +'" data-height="'+ item.height +'" data-path="'+ path +'">';
        h += '  <img class="ico" src="'+ urls.server +'/pointService/getImg?path='+ path +'" title="选择">';
        h += '  <a class="delete" data-id="'+ item.id +'" title="删除"></a>';
        h += '  <a class="selected" href="javascript:void(0);"></a>';
        h += '</li>';
    }

    if(data.length < 10) {
        h += '<li class="upload-custom-icon" title="上传自定义图片"><span class="glyphicon glyphicon-plus"></span></li>';
    }
    
    $('.icon-selector .custom ul').html(h);

    $('.icon-selector .custom ul li a.delete').unbind('click').click(function(){
        Dituhui.Modal.alert('确定删除该图标 ? 删除后不可恢复。', function(){
            Dituhui.Point.CustomIcon.remove( $(this).attr('data-id') );
        }, {'data-id': $(this).attr('data-id')});
    });

    $('.icon-selector .custom ul li img.ico').unbind('click').click(Page.selectCustomIco);

    $('li.upload-custom-icon').click(function(){
        $('#txt_import_custom_icon').click();
    });
}

/** 
 * 删除自定义图片
 */
Dituhui.Point.CustomIcon.remove = function(id) {
    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/pointService/deletePointStyleCustom?",
        data: {customid: id},
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
                Dituhui.showPopover('自定义图片删除成功');
                $('.data-modal').hide();
                Dituhui.Point.CustomIcon.search();
            }
            else {
                Dituhui.showHint(e.info ? e.info : '自定义图片删除失败');
            }
        },
        error: function(){
            Dituhui.hideMask();  
            Dituhui.showHint('自定义图片删除失败');          
        }   
    });
}

/** 
 * 区划文字样式
 */
Dituhui.Point.Style.getLabelStyle = function(lbl, y, ly){ 
    var width = 68;
    
    var style = {};
    if(!Dituhui.User.special) {
    	if(lbl && lbl.length > 5) {
	        lbl = lbl.substr(0, 5) + '...';
	        width = 80;
	    }
    	style = {
	        // fill: true,
	        // fillColor: "#fff",
	        // fillOpacity: 1,
	        // stroke: true,
	        // strokeColor:"#999999",
	
	        externalGraphic: urls.server + '/resources/css/imgs/point/label_back1.png',
	        graphicWidth: width,
	        graphicHeight: 30,
	        graphicYOffset: y,
	
	        // backgroundGraphic: urls.server + '/resources/css/imgs/point/label_back.png',
	        // backgroundWidth: '50',
	        // backgroundHeight: '30',
	        // backgroundYOffset: '-' + y,
	
	        label: lbl,
	        labelYOffset: ly,
	        labelXOffset: 0,
	        fontColor: "#212121",
	        fontSize: "12",
	        fontFamily: "微软雅黑"
	    }
    }
    else {
    	style = {
    		label: lbl,
	        labelYOffset: ly,
	        labelXOffset: 0,
	        fontColor: "#e60050",
	        fontSize: "13",
	        fontFamily: "微软雅黑"
    	}
    }
    return style;
}