/**
 * Created by zr on 2018/3/5.
 */
var map;
var currentMarker = null;//当前的标注
var polygonMouseTool = null;//画面鼠标工具
var overlayObjs = [];//覆盖物组数组
var polygonEditor = null;//多边形编辑器
var polygonTexts = [];//面对象的名称集合
var polygonInfoWin = null;//面对象的信息窗体
var hmStatus = 0;//画面选中的状态（0：未选中；1：表示选中）
var mergeGon = [];//待合并面的集合
var splitGon = null;//待拆分的面
var ctrlStatus = 0;//ctrl键的状态

//设置鼠标样式
var setCursor = function(){
    map.setDefaultCursor("crosshair");
};

//画点的信息窗体
var showMarkerInfoWindow = function(marker,position,fun){
    var _thisMarker = marker;
    var labelContent = _thisMarker.getLabel();

    AMapUI.loadUI(['overlay/SimpleInfoWindow'], function(SimpleInfoWindow) {
        if(labelContent != undefined){//如果label里的内容不为空，则表示查看标注
            var hdInfoWindow = new SimpleInfoWindow({
                infoTitle: '<strong id="title">查看标注</strong><a class="glyphicon glyphicon-trash" id="delete" title="删除"></a><a id="edit" class="glyphicon glyphicon-edit" title="编辑"></a><div style="clear: both"></div>',
                infoBody: '<div>'+
                '<div style="margin-bottom: 5px;"><label style="margin-right: 3px;float: left">名称:</label><span id="name" style="float: right;padding-left: 5px" >'+labelContent.content+'</span><div style="clear: both"></div></div>'+
                '<div><label style="margin-right: 3px;float: left">业务属性:</label><span id="attr_name" style="float: right;padding-left: 5px">'+labelContent.attr_name+'</span><div style="clear: both"></div></div>'+
                '<div><button id="show_save" style="float: right;margin:5px 0 0 0;color: #333;">确定</button><div style="clear: both"></div></div>'+
                '</div>',
                offset: new AMap.Pixel(0, -31),
            });
        }else{//如果label里的内容为空，表示添加标注
            var hdInfoWindow = new SimpleInfoWindow({
                infoTitle: '<strong>添加标注</strong>',
                infoBody: '<div>'+
                '<div style="margin-bottom: 5px;"><label style="margin-right: 3px;float: left">名称</label><input style="float: right;padding-left: 5px" id="name" type="text"/><div style="clear: both"></div></div>'+
                '<div><label style="margin-right: 3px;float: left">业务属性</label><input style="float: right;padding-left: 5px" id="attr_name" type="text"/><div style="clear: both"></div></div>'+
                '<div><button id="reset" style="float: right;margin:5px 0 0 0;color: #333;">取消</button><button id="save" style="float: right;margin:5px 5px 0 0;color: #333;">保存</button><div style="clear: both"></div></div>'+
                '</div>',
                offset: new AMap.Pixel(0, -31),
            });
        }
        //信息窗体显示
        hdInfoWindow.open(map,position);

        //保存
        function save(name,attr_name){
            if(name != "" && attr_name != ""){
                hdInfoWindow.close();
                _thisMarker.setLabel({
                    offset: new AMap.Pixel(25, 5),
                    content:name,
                    attr_name:attr_name
                });
                //添加鼠标点击事件
                map.on('click',fun);
                setCursor();
            }
        }
        //label内容为空时的保存操作
        hdInfoWindow.get$InfoBody().on('click', '#save', function(event) {
            //阻止冒泡
            event.stopPropagation();
            var name = $("#name").val();
            var attr_name = $("#attr_name").val();
            save(name,attr_name);
        });

        //label内容不为空时的保存操作（即查看标注时的确定操作）
        hdInfoWindow.get$InfoBody().on('click', '#show_save', function(e) {
            //阻止冒泡
            e.stopPropagation();
            var name = $("#name").html();
            var attr_name = $("#attr_name").html();
            save(name,attr_name);
        });

        //取消
        function resetMarker(){
            _thisMarker.setMap(null);
            map.clearInfoWindow();
            map.on('click',fun);
            setCursor();
        }
        //取消操作
        hdInfoWindow.get$InfoBody().on('click',"#reset",function(e){
            e.stopPropagation();
            resetMarker();
        });

        //删除
        function deleteMarker(){
            bootbox.confirm({
                title:"删除标注",
                closeButton:false,
                message: "确定删除该标注？",
                buttons: {
                    confirm: {
                        label: '确定',
                        className: 'btn-default'
                    },
                    cancel: {
                        label: '取消',
                        className: 'btn-default'
                    }
                },
                callback: function(result){
                    /* result is a boolean; true = OK, false = Cancel*/
                    if (result == true){
                        _thisMarker.setMap(null);
                        hdInfoWindow.close();
                        map.on('click',fun);
                        setCursor();
                    }
                }
            });
        }
        //删除操作
        hdInfoWindow.get$InfoTitle().on('click',"#delete",function(e){
            e.stopPropagation();
            deleteMarker();
        });

        //编辑操作
        hdInfoWindow.get$InfoTitle().on('click',"#edit",function(e){
            e.stopPropagation();
            hdInfoWindow.setContent(
                '<div class="amap-ui-smp-ifwn-container">'+
                '<div class="amap-ui-smp-ifwn-content-body">'+
                '<h3 class="amap-ui-infowindow-title amap-ui-smp-ifwn-info-title"><strong id="title">编辑标注</strong><a class="glyphicon glyphicon-trash" id="delete" title="删除"></a>'+
                '<div style="clear: both"></div></h3>'+
                '<div class="amap-ui-infowindow-body amap-ui-smp-ifwn-info-content"><div><div style="margin-bottom: 5px;"><label style="margin-right: 3px;float: left">名称</label><input style="float: right;padding-left: 5px" id="name" type="text" value="'+labelContent.content+'"><div style="clear: both"></div></div><div><label style="margin-right: 3px;float: left">业务属性</label><input style="float: right;padding-left: 5px" id="attr_name" type="text" value="'+labelContent.attr_name+'"><div style="clear: both"></div></div><div><button id="save" style="float: right;margin:5px 0 0 0;color: #333;">确定</button><div style="clear: both"></div></div></div></div>'+
                '</div>'+
                '<div class="amap-ui-smp-ifwn-combo-sharp"></div>'+
                '</div>'
            );
            $("#save").click(function(){
                var name = $("#name").val();
                var attr_name = $("#attr_name").val();
                save(name,attr_name);
            });
            $("#delete").click(function(){
                deleteMarker();
            });
        });
    });
};

/*面操作start*/
//画面的信息窗体（参数有3个，当前显示窗体的对象、窗体显示的位置和地图事件方法）
var showOverlayInfoWindow = function(overlayObj,position,mouseTool){

    var _thisOverlay = overlayObj;
    var _thisPolygon = _thisOverlay.getOverlays()[_thisOverlay.getOverlays().length-1];

    var labelContent = _thisPolygon.getExtData();

    AMapUI.loadUI(['overlay/SimpleInfoWindow'], function(SimpleInfoWindow) {
        if(labelContent.content != undefined){//如果扩展属性ExtData的content里的内容不为空，则表示查看面
            var hmInfoWindow = new SimpleInfoWindow({
                infoTitle: '<strong id="title">查看面</strong><a class="glyphicon glyphicon-trash" id="delete" title="删除"></a><a id="edit" class="glyphicon glyphicon-edit" title="编辑"></a><div style="clear: both"></div>',
                infoBody: '<div>'+
                '<div style="margin-bottom: 5px;"><label style="margin-right: 3px;float: left">名称:</label><span id="name" style="float: right;padding-left: 5px" >'+labelContent.content+'</span><div style="clear: both"></div></div>'+
                '<div><label style="margin-right: 3px;float: left">业务属性:</label><span id="attr_name" style="float: right;padding-left: 5px">'+labelContent.attr_name+'</span><div style="clear: both"></div></div>'+
                '<div><button id="show_save" style="float: right;margin:5px 0 0 0;color: #333;">确定</button><div style="clear: both"></div></div>'+
                '</div>'
            });
        }else{//如果扩展属性ExtData的content里的内容为空，表示添加面
            var hmInfoWindow = new SimpleInfoWindow({
                infoTitle: '<strong>添加面</strong>',
                infoBody: '<div>'+
                '<div style="margin-bottom: 5px;"><label style="margin-right: 3px;float: left">名称</label><input style="float: right;padding-left: 5px" id="name" type="text"/><div style="clear: both"></div></div>'+
                '<div><label style="margin-right: 3px;float: left">业务属性</label><input style="float: right;padding-left: 5px" id="attr_name" type="text"/><div style="clear: both"></div></div>'+
                '<div><button id="reset" style="float: right;margin:5px 0 0 0;color: #333;">取消</button><button id="save" style="float: right;margin:5px 5px 0 0;color: #333;">保存</button><div style="clear: both"></div></div>'+
                '</div>'
            });
        }
        polygonInfoWin = hmInfoWindow;
        //信息窗体显示
        hmInfoWindow.open(map,position);

        //保存
        function saveOverlay(name,attr_name,status){
            if(name != "" && attr_name != ""){
                hmInfoWindow.close();
                _thisPolygon.setExtData({
                    content:name,
                    attr_name:attr_name
                });
                _thisOverlay.setOptions({
                    fillOpacity:"0.35"
                });

                if(status == 0){
                    showOverlayText(_thisOverlay,name,mouseTool);
                    removePolygonTool(mouseTool);
                    _thisOverlay.on('click',function(){
                        if(ctrlStatus == 1){
                            if(splitGon != null){
                                mergeGon.push(splitGon);
                            }
                            if(mergeGon.length == 0){
                                mergeGon.push(_thisOverlay);
                            }else if(mergeGon.length<2){
                                for(var i = 0;i<mergeGon.length;i++){
                                    if(mergeGon[i] != _thisOverlay){
                                        mergeGon.push(_thisOverlay);
                                    }
                                }
                            }
                        }
                        overlayClick(_thisOverlay);
                    });
                }
                hmStatus = 0;
                $("#hm-edit").hide();
            }
        }
        //label内容为空时的保存操作
        hmInfoWindow.get$InfoBody().on('click', '#save', function(event) {
            //阻止冒泡
            event.stopPropagation();
            var name = $("#name").val();
            var attr_name = $("#attr_name").val();
            //内容为空，为0
            var status = 0;
            saveOverlay(name,attr_name,status);
        });

        //label内容不为空时的保存操作（即查看标注时的确定操作）
        hmInfoWindow.get$InfoBody().on('click', '#show_save', function(e) {
            //阻止冒泡
            e.stopPropagation();
            var name = $("#name").html();
            var attr_name = $("#attr_name").html();
            //内容不为空，为1
            var status = 1;
            saveOverlay(name,attr_name,status);
        });

        //取消
        function resetPolygon(){
            _thisOverlay.setMap(null);
            //删除最后一个对象
            overlayObjs.pop();
            map.clearInfoWindow();
            removePolygonTool(mouseTool);
        }
        //取消操作
        hmInfoWindow.get$InfoBody().on('click',"#reset",function(e){
            e.stopPropagation();
            resetPolygon();
        });

        //删除
        function deleteOverlay(){
            bootbox.confirm({
                title:"删除面",
                closeButton:false,
                message: "确定删除该面？",
                buttons: {
                    confirm: {
                        label: '确定',
                        className: 'btn-default'
                    },
                    cancel: {
                        label: '取消',
                        className: 'btn-default'
                    }
                },
                callback: function(result){
                    /* result is a boolean; true = OK, false = Cancel*/
                    if (result == true){
                        _thisOverlay.setMap(null);
                        hmInfoWindow.close();
                        if(polygonEditor != null){
                            for(var i = 0;i<polygonEditor.length;i++){
                                polygonEditor[i].close();
                            }
                        }
                        //删除面集合的当前对象
                        for(var i = 0; i< overlayObjs.length;i++){
                            if(overlayObjs[i] == _thisOverlay){
                                //删除当前对象
                                overlayObjs.splice(i,1);
                                //删除当前面对象所对应的名称
                                polygonTexts[i].setMap(null);
                                polygonTexts.splice(i,1);
                            }
                        }
                    }
                }
            });
        }
        //删除操作
        hmInfoWindow.get$InfoTitle().on('click',"#delete",function(e){
            e.stopPropagation();
            deleteOverlay();
        });

        //编辑操作
        /*面编辑工具对象*/
        var polygonEditors = [];
        hmInfoWindow.get$InfoTitle().on('click',"#edit",function(e){
            e.stopPropagation();
            //构造折线编辑对象，并开启折线的编辑状态
            _thisOverlay.eachOverlay(function(overlay){
                map.plugin(["AMap.PolyEditor"],function(){
                    var polygonEditor = new AMap.PolyEditor(map,overlay);
                    polygonEditor.open();
                    polygonEditors.push(polygonEditor);
                    _thisOverlay.on('click',function(){
                        polygonEditor.close();
                    });
                });
            });

            polygonEditor = polygonEditors;

            hmInfoWindow.setContent(
                '<div class="amap-ui-smp-ifwn-container">'+
                '<div class="amap-ui-smp-ifwn-content-body">'+
                '<h3 class="amap-ui-infowindow-title amap-ui-smp-ifwn-info-title"><strong id="title">编辑面</strong><a class="glyphicon glyphicon-trash" id="delete" title="删除"></a>'+
                '<div style="clear: both"></div></h3>'+
                '<div class="amap-ui-infowindow-body amap-ui-smp-ifwn-info-content"><div><div style="margin-bottom: 5px;"><label style="margin-right: 3px;float: left">名称</label><input style="float: right;padding-left: 5px" id="name" type="text" value="'+labelContent.content+'"><div style="clear: both"></div></div><div><label style="margin-right: 3px;float: left">业务属性</label><input style="float: right;padding-left: 5px" id="attr_name" type="text" value="'+labelContent.attr_name+'"><div style="clear: both"></div></div><div><button id="save" style="float: right;margin:5px 0 0 0;color: #333;">确定</button><div style="clear: both"></div></div></div></div>'+
                '</div>'+
                '<div class="amap-ui-smp-ifwn-combo-sharp"></div>'+
                '</div>'
            );
            $("#save").click(function(){
                var name = $("#name").val();
                var attr_name = $("#attr_name").val();
                for(var i = 0;i<overlayObjs.length;i++){
                    if(overlayObjs[i] == _thisOverlay){
                        polygonTexts[i].setText(name);
                    }
                }
                saveOverlay(name,attr_name);
                for(var i = 0;i<polygonEditors.length;i++){
                    polygonEditors[i].close();
                }
                polygonEditor = null;
            });
            $("#delete").click(function(){
                deleteOverlay();
            });
        });
    });
};

//添加text
var showOverlayText = function(polygon,name,mouseTool){
    var _thisPolygon = polygon;
    var textInfo;
    textInfo = new AMap.Text({
        map:map,
        position:_thisPolygon.getOverlays()[_thisPolygon.getOverlays().length-1].getBounds().getCenter(),
    });
    textInfo.setText(name);
    textInfo.on('click',function(){
        textClick(_thisPolygon,mouseTool);
    });
    polygonTexts.push(textInfo);
};

//取消画面操作（参数：鼠标画面工具对象）
var removePolygonTool = function(mouseTool){
    if(mouseTool){
        mouseTool.close();
        var src = $("#hm").find("img").attr("src").split("_");
        $("#hm").removeClass("active").find("img").attr("src",src[0]+".png");
        polygonMouseTool = null;
        $("#hm-act").hide();
        //未选中"画面"
        hmStatus = 0;
    }
};

//text的点击事件（参数是面集合对象）
var textClick = function(overlay,mouseTool){
    if(splitGon == null && mergeGon.length == 0){
        $("#hm-edit").hide();
        var overlayObj = overlay;
        if(hmStatus == 0){
            overlayObj.setOptions({
                fillOpacity:"0.35"
            });
            var position = overlayObj.getOverlays()[overlayObj.getOverlays().length-1].getPath()[0];
            showOverlayInfoWindow(overlayObj,[position['lng'],position['lat']],mouseTool);
        }
    }
};

//点击面（参数：当前待操作的面集合）
var overlayClick = function(overlay){
    if(hmStatus != 0){
        return false;
    }else{
        if(polygonInfoWin.getIsOpen() == true){
            polygonInfoWin.close();
        }

        if(polygonEditor != null){
            for(var i = 0;i<polygonEditor.length;i++){
                polygonEditor[i].close();
            }
        }
        var _thisOverlay = overlay;
        var _thisPolygon = _thisOverlay.getOverlays()[_thisOverlay.getOverlays().length-1];
        if(hmStatus == 0){
            $("#hm-edit").show();
            //ctrl按下操作
            if(ctrlStatus == 1){
                for(var i = 0;i<mergeGon.length;i++){
                    if(mergeGon[i] == _thisOverlay){
                        if(_thisPolygon.getOptions().fillOpacity == "0.6" && mergeGon.length<2){
                            _thisOverlay.setOptions({
                                fillOpacity:"0.35"
                            });
                            mergeGon.splice(i,1);
                        }else if(_thisPolygon.getOptions().fillOpacity == "0.35"){
                            _thisOverlay.setOptions({
                                fillOpacity:"0.6"
                            });
                        }
                    }
                }
            }
            //没有Ctrl操作
            if(ctrlStatus == 0){
                //把合并的面集合设置为空
                mergeGon = [];
                _thisOverlay.setOptions({
                    fillOpacity:"0.6"
                });
                splitGon = _thisOverlay;
                for(var i = 0;i<overlayObjs.length;i++){
                    if(overlayObjs[i] != _thisOverlay){
                        overlayObjs[i].setOptions({
                            fillOpacity:"0.35"
                        });
                    }
                }
            }
        }
    }
};

var splitRegion = function(points,splitPoints,overlay){

    var _thisOverlay = overlay;
    $.ajax({
        url:"http://api.jituonline.com/v1/geocalculator/splitRegion?ak=9291cae6d60d4275b718100787dc73c1",
        dataType:"jsonp",
        jsonp:"callback",
        data:{
            "points":points,
            "splitPoints":splitPoints,
            // "coordType":"gcj02ll"
        },
        success:function(e){
            //返回成功
            if(e && e.info == "OK" && e.result.region1.parts.length != 0){
                for(var i = 0;i<overlayObjs.length;i++){
                    if(overlayObjs[i] == _thisOverlay){
                        overlayObjs[i].setMap(null);
                        overlayObjs.splice(i,1);
                        polygonTexts[i].setMap(null);
                        polygonTexts.splice(i,1);
                    }
                }
                $.each(e.result,function(indexGrandPa,itemGrandPa){
                    if(itemGrandPa != true){
                        var pathArr = [];
                        var polygonArr = [];
                        $.each(itemGrandPa['points'],function(indexFather,itemFather){
                            pathArr.push([itemFather.x,itemFather.y]);
                        });

                        if(itemGrandPa['parts'].length>0){
                            for(var i = 0;i<itemGrandPa['parts'].length;i++){
                                polygonArr[i] = pathArr.splice(0,itemGrandPa['parts'][i]);
                            }
                        }
                        drawPolygon(indexGrandPa,polygonArr);
                    }
                });
                splitGon = null;
            }
        }
    });

};

//合并面
var unionGon = function(points,unionPoints,overlayObj1,overlayObj2){
    $.ajax({
        url:"http://api.jituonline.com/v1/geocalculator/union?ak=9291cae6d60d4275b718100787dc73c1",
        dataType:"jsonp",
        jsonp:"callback",
        data:{
            points:points,
            unionPoints:unionPoints,
            // coordType:"gcj02ll"
        },
        success:function(e){
            if(e && e.info == "OK" && e.result){
                var pathArr = [];
                var polygonArr = [];

                $.each(e.result.points,function(index,item){
                    pathArr.push([item.x,item.y]);
                });
                if(e.result['parts'].length>0){
                    for(var i = 0;i<e.result['parts'].length;i++){
                        polygonArr[i] = pathArr.splice(0,e.result['parts'][i]);
                    }
                }

                for(var i = 0;i<overlayObjs.length;i++){
                    if(overlayObjs[i] == overlayObj1 || overlayObjs[i] == overlayObj2){
                        polygonTexts[i].setMap(null);
                    }
                }
                overlayObj1.setMap(null);
                overlayObj2.setMap(null);
                drawPolygon(0,polygonArr);
            }else{
                overlayObj1.setOptions({
                    fillOpacity:"0.35"
                });
                overlayObj2.setOptions({
                    fillOpacity:"0.35"
                });
                mergeGon = [];
            }
        }
    });
};

//重新渲染面
/****
 *param:pathArr(路径参数)
 ****/
var drawPolygon = function(index,pathArr){
    mergeGon = [];
    splitGon = null;
    //定义覆盖物集合
    var overlayGroup = new AMap.OverlayGroup();
    var position;
    for(var i = 0;i<pathArr.length;i++){
        var polygon = new AMap.Polygon({
            path: pathArr[i],//设置多边形边界路径
            strokeOpacity: 0.9, //线透明度
            strokeWeight: 2,    //线宽
            fillOpacity: 0.35,//填充透明度

            strokeColor: '#1791fc', //线颜色
            fillColor: "#1791fc", //填充色
        });

        if(i == pathArr.length-1) {
            position = polygon.getBounds().getCenter();
            if(index == "region1"){
                polygon.setExtData({
                    content:"区划1",
                    attr_name:"区划1"
                });
            }
            if(index == "region2"){
                polygon.setExtData({
                    content:"区划2",
                    attr_name:"区划2"
                });
            }
            if(index == 0){
                polygon.setExtData({
                    content:"区划0",
                    attr_name:"区划0"
                });
            }
        }
        overlayGroup.addOverlay(polygon);
    }
    if(index == "region1"){
        var textInfo = new AMap.Text({
            map:map,
            position:position,
            text:"区划1"
        });
        textInfo.on('click',function(){
            textClick(overlayGroup);
        });
        polygonTexts.push(textInfo);
    }else if(index == "region2"){
        var textInfo = new AMap.Text({
            map:map,
            position:position,
            text:"区划2"
        });
        textInfo.on('click',function(){
            textClick(overlayGroup);
        });
        polygonTexts.push(textInfo);
    }else if(index == 0){
        var textInfo = new AMap.Text({
            map:map,
            position:position,
            text:"区划0"
        });
        textInfo.on('click',function(){
            textClick(overlayGroup);
        });
        polygonTexts.push(textInfo);
    }
    overlayGroup.on('click',function(){
        if(ctrlStatus == 1){
            if(splitGon != null){
                mergeGon.push(splitGon);
            }
            if(mergeGon.length == 0){
                mergeGon.push(overlayGroup);
            }else if(mergeGon.length<2){
                for(var i = 0;i<mergeGon.length;i++){
                    if(mergeGon[i] != overlayGroup){
                        mergeGon.push(overlayGroup);
                    }
                }
            }
        }
        overlayClick(overlayGroup);
    });
    overlayObjs.push(overlayGroup);
    overlayGroup.setMap(map);

    $("#hm-edit").hide();
}
/*面操作end*/

//初始化地图
var initMap = function(){
    map = new AMap.Map('map',{
        resizeEnable: true,
        zoom:15
    });
};

//dom事件
$(function(){
    $("body").show();
    $(".main").css({
        height:window.innerHeight - 60
    });
    initMap();

    var currentAct = "";//当前的动作（画点，画线，画面）
    var menuChange = function(id){
        //工具栏操作
        //如果不是画点操作，currentMarker里的内容注销并设置为初始值null
        if(id != "hd" && currentMarker != null){
            currentMarker.setMap(null);
            currentMarker = null;
        }
        //如果不是画面操作，polygonMouseTool的内容注销并设置为初始值null
        if(id != "hm"){
            //清空面集合中的数据，清空面名称的数据
            overlayObjs = [];
            polygonTexts = [];
            mergeGon = [];
            splitGon = null;
            if(polygonEditor != null){
                polygonEditor = null;
            }
            if(polygonInfoWin != null){
                polygonInfoWin.close();
            }
        }
        var act = $("#toolBar .toolbox .btn-toolbar.active");
        if(!$("#"+id).hasClass("active")){
            if (act.length > 0){
                var oldSrc = $("#toolBar .toolbox .btn-toolbar.active").find("img").attr("src").split("_");
                $("#toolBar .toolbox .btn-toolbar.active").find("img").attr("src",oldSrc[0]+".png");
            }
            //判断上一次操作与本次操作是否相同，不同则初始化地图
            if(currentAct != "" && currentAct != $("#"+id).attr("id")){
                initMap();
            }
            $("#"+id).addClass("active")
                .siblings().removeClass("active");
            var src = $("#toolBar .toolbox .btn-toolbar.active").find("img").attr("src");

            $("#toolBar .toolbox .btn-toolbar.active").find("img").attr("src",src);
        }

        //判断当前的操作
        currentAct = $("#toolBar .toolbox .btn-toolbar.active").attr("id");
    };
    var overSrc;//移入的图片路径
    var outSrc;//移出的图片路径
    $("#toolBar .toolbox .btn-toolbar").hover(function(){
        if(!$(this).hasClass("active")){
            outSrc = $(this).find("img").attr("src");
            overSrc = outSrc.split(".")[0]+"_active.png";
            $(this).find("img").attr("src",overSrc);
        }
    },function(){
        if (!$(this).hasClass("active")){
            $(this).find("img").attr("src",outSrc);
        }
    });

    /*画点start*/
    $("#hd").click(function(){
        var id = $(this).attr("id");
        if(currentMarker == null){
            menuChange(id);
            setCursor();
            //callBackFn用于切换地图的点击事件
            var callBcakFn = function(e){
                var marker = new AMap.Marker({
                    map:map,
                    position:[e.lnglat.getLng(),e.lnglat.getLat()]
                });
                currentMarker = marker;

                marker.on('click',function(){
                    if(currentMarker != marker && currentMarker.getLabel() == null){
                        currentMarker.setMap(null);
                    }
                    map.setDefaultCursor();
                    map.off('click',callBcakFn);
                    showMarkerInfoWindow(marker,marker.getPosition(),callBcakFn);
                });
                showMarkerInfoWindow(marker,marker.getPosition(),callBcakFn);
                map.off('click',callBcakFn);
                map.setDefaultCursor();
            };
        }

        $("#hd-act").show();
        if($("#hx-act")){
            $("#hx-act").hide();
        }
        $("#hm-act").hide();

        map.on('click',callBcakFn);
    });
    /*画点end*/
    /*画线start*/
    $("#hx").click(function(){
        var id = $(this).attr("id");
        if($("#hx-act")){
            $("#hx-act").show();
        }
        $("#hd-act").hide();
        $("#hm-act").hide();
        menuChange(id);
    });
    /*画线end*/
    /*画面start*/
    $("#hm").click(function(){
        var id = $(this).attr("id");
        if($("#hx-act")){
            $("#hx-act").hide();
        }
        $("#hd-act").hide();
        var mousetool;
        if(polygonMouseTool == null && $("#hm-edit span.active").length == 0){
            if(polygonInfoWin == null || polygonInfoWin.getIsOpen() == false){
                mergeGon = [];
                splitGon = null;
                for(var i = 0;i<overlayObjs.length;i++){
                    overlayObjs[i].setOptions({
                        fillOpacity:"0.35"
                    });
                }
                hmStatus = 1;
                $("#hm-act").show();
                $("#hm-edit").hide();
                menuChange(id);
                setCursor();
                //鼠标工具插件
                map.plugin(['AMap.MouseTool'],function(){
                    mousetool = new AMap.MouseTool(map);
                    polygonMouseTool = mousetool;
                    mousetool.polygon();
                    mousetool.on('draw',function(e){

                        var polygonObj = e.obj;

                        var overlayGroup = new AMap.OverlayGroup(polygonObj);
                        //将画完的数组对象存入面集合中
                        overlayObjs.push(overlayGroup);

                        var position = [polygonObj.getPath()[0]['lng'],polygonObj.getPath()[0]['lat']];

                        overlayGroup.setMap(map);
                        showOverlayInfoWindow(overlayGroup,position,mousetool);
                        mousetool.close();
                        map.setDefaultCursor();
                    });
                });
            }
        }
    });
    /*画面end*/

    /*面的拆分合并*/
    $("#lineSplit").click(function(){
        if($("#hm-edit span.active").length == 0 && mergeGon.length != 2){
            if(splitGon != null){
                //线拆分
                /* *
                * *parts:复合面的组成部分
                * *points:每个复合面的组成路径
                * *overlayPoints:传入后台的待拆分的面参数
                * */
                var parts = [];
                var points = [];
                var overlayPoints = [];
                splitGon.eachOverlay(function (overlay) {
                    var point = [];
                    $.each(overlay.getPath(),function(i,item){
                        point.push(item['lng']+','+item['lat']);
                    });
                    if(point[0] != point[point.length-1]){
                        point.push(point[0]);
                    }
                    for(var i = 0;i<point.length;i++){
                        points.push(point[i]);
                    }
                    parts.push(point.length);
                });
                overlayPoints.push(parts.join(','));
                overlayPoints.push(points.join(';'));
                overlayPoints = overlayPoints.join('^');

                var that = $(this);
                that.addClass("active").siblings().removeClass("active");
                setCursor();

                map.plugin(["AMap.MouseTool"],function(){
                    var lineTool = new AMap.MouseTool(map);
                    lineTool.polyline(); //使用鼠标工具，在地图上画标记点
                    lineTool.on('draw',function(e){
                        var splitPoints = [];
                        $.each(e.obj.getPath(),function(index,item){
                            splitPoints.push(item['lng']+','+item['lat']);
                        });
                        splitPoints = splitPoints.join(";");
                        map.setDefaultCursor();

                        splitRegion(overlayPoints,splitPoints,splitGon);
                        lineTool.close(true);
                        that.removeClass("active");
                    });
                });
            }
        }else{
            bootoast({
                message: '请选择待拆分的面！',
                type: 'danger',
                icon: 'remove-circle',
                position: 'top',
                timeout: 3,
                dismissable:'false'
            });
        }
    });
    //面拆分
    $("#gonSplit").click(function(){
        if($("#hm-edit span.active").length == 0 && mergeGon.length != 2){
            if(splitGon != null) {
                //面拆分
                /* *
                 * *parts:复合面的组成部分
                 * *points:每个复合面的组成路径
                 * *overlayPoints:传入后台的待拆分的面参数
                 * */
                var parts = [];
                var points = [];
                var overlayPoints = [];
                splitGon.eachOverlay(function (overlay) {
                    var point = [];
                    $.each(overlay.getPath(),function(i,item){
                        point.push(item['lng']+','+item['lat']);
                    });
                    if(point[0] != point[point.length-1]){
                        point.push(point[0]);
                    }
                    for(var i = 0;i<point.length;i++){
                        points.push(point[i]);
                    }
                    parts.push(point.length);
                });
                overlayPoints.push(parts.join(','));
                overlayPoints.push(points.join(';'));
                overlayPoints = overlayPoints.join('^');

                var that = $(this);
                that.addClass("active").siblings().removeClass("active");
                setCursor();

                map.plugin(['AMap.MouseTool'], function () {
                    var gonTool = new AMap.MouseTool(map);
                    gonTool.polygon();
                    gonTool.on('draw', function (e) {
                        var splitPoints = [];
                        $.each(e.obj.getPath(), function (index, item) {
                            splitPoints.push(item['lng'] + ',' + item['lat']);
                        });
                        splitPoints.push(splitPoints[0]);
                        splitPoints = splitPoints.join(";");
                        map.setDefaultCursor();

                        splitRegion(overlayPoints, splitPoints, splitGon);
                        gonTool.close(true);
                        that.removeClass("active");
                    });
                });
            }
        }
    });
    //面合并
    $("#merge").click(function(){
        if($("#hm-edit span.active").length == 0){
            if(mergeGon.length == 2){
                var that = $(this);
                that.addClass("active").siblings().removeClass("active");

                //需要合并的第一个面
                var points = [];
                var parts1 = [];
                var overlay = [];
                //需要合并的第二个面
                var unionPoints = [];
                var parts2 = [];
                var unionOverlay = [];

                //第一个面
                mergeGon[0].eachOverlay(function(overlay){
                    var point = [];
                    $.each(overlay.getPath(),function(i,item){
                        point.push(item['lng']+','+item['lat']);
                    });
                    if(point[0] != point[point.length-1]){
                        point.push(point[0]);
                    }
                    parts1.push(point.length);
                    for(var i = 0;i<point.length;i++){
                        points.push(point[i]);
                    }
                });

                overlay.push(parts1.join(','));
                overlay.push(points.join(';'));
                overlay = overlay.join('^');

                //第二个面
                mergeGon[1].eachOverlay(function(overlay){
                    var point = [];
                    $.each(overlay.getPath(),function(i,item){
                        point.push(item['lng']+','+item['lat']);
                    });
                    if(point[0] != point[point.length-1]){
                        point.push(point[0]);
                    }
                    parts2.push(point.length);
                    for(var i = 0;i<point.length;i++){
                        unionPoints.push(point[i]);
                    }
                });

                unionOverlay.push(parts2.join(','));
                unionOverlay.push(unionPoints.join(';'));
                unionOverlay = unionOverlay.join('^');

                unionGon(overlay,unionOverlay,mergeGon[0],mergeGon[1]);
                mergeGon = [];
                that.removeClass("active");
            }
        }
    });
    window.document.onkeydown = function(){
        if (window.event.keyCode == 17) {
            ctrlStatus = 1;
        }
    };
    window.document.onkeyup = function(){
        if (window.event.keyCode == 17) {
            ctrlStatus = 0;
        }
    };
});