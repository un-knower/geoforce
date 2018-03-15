/**
 * Created by zr on 2018/3/5.
 */
var map;
var currentMarker = null;//当前的标注
var currentPolygon = null;//当前的面
var prevPolygon;//前一个面对象
var polygonMouseTool = null;//画面鼠标工具
var polygonObjs = [];//面对象集合
var polygonEditor = null;//多边形编辑器
var polygonTexts = [];//面对象的名称集合
var polygonInfoWin = null;//面对象的信息窗体
var hmStatus = 0;//画面选中的状态（0：未选中；1：表示选中）

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
var showPolygonInfoWindow = function(polygonObj,position,mouseTool){
    var _thisPolygon = polygonObj;
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
        function savePolygon(name,attr_name,status){
            if(name != "" && attr_name != ""){
                hmInfoWindow.close();
                _thisPolygon.setExtData({
                    content:name,
                    attr_name:attr_name
                });
                _thisPolygon.setOptions({
                    fillOpacity:"0.35"
                });

                if(status == 0){
                    showPolygonText(_thisPolygon,name,mouseTool);
                    removePolygonTool(mouseTool);
                    // _thisPolygon.on('click',function(){
                    //     polygonClick(_thisPolygon,mouseTool);
                    // });
                    _thisPolygon.on('click',function(){
                       polygonClick(_thisPolygon);
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
            savePolygon(name,attr_name,status);
        });

        //label内容不为空时的保存操作（即查看标注时的确定操作）
        hmInfoWindow.get$InfoBody().on('click', '#show_save', function(e) {
            //阻止冒泡
            e.stopPropagation();
            var name = $("#name").html();
            var attr_name = $("#attr_name").html();
            //内容不为空，为1
            var status = 1;
            savePolygon(name,attr_name,status);
        });

        //取消
        function resetPolygon(){
            _thisPolygon.setMap(null);
            //删除最后一个对象
            polygonObjs.pop();
            currentPolygon = prevPolygon;
            map.clearInfoWindow();
            removePolygonTool(mouseTool);
        }
        //取消操作
        hmInfoWindow.get$InfoBody().on('click',"#reset",function(e){
            e.stopPropagation();
            resetPolygon();
        });

        //删除
        function deletePolygon(){
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
                        _thisPolygon.setMap(null);
                        hmInfoWindow.close();
                        if(polygonEditor != null){
                            polygonEditor.close();
                        }
                        //删除面集合的当前对象
                        for(var i = 0; i< polygonObjs.length;i++){
                            if(polygonObjs[i] == _thisPolygon){
                                //删除当前对象
                                polygonObjs.splice(i,1);
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
            deletePolygon();
        });

        //编辑操作
        hmInfoWindow.get$InfoTitle().on('click',"#edit",function(e){
            e.stopPropagation();
            //构造折线编辑对象，并开启折线的编辑状态
            map.plugin(["AMap.PolyEditor"],function(){
                polygonEditor = new AMap.PolyEditor(map,_thisPolygon);
                polygonEditor.open();
            });

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
            _thisPolygon.on('click',function(){
                polygonEditor.close();
            });
            $("#save").click(function(){
                var name = $("#name").val();
                var attr_name = $("#attr_name").val();
                for(var i = 0;i<polygonObjs.length;i++){
                    if(polygonObjs[i] == _thisPolygon){
                        polygonTexts[i].setText(name);
                    }
                }
                savePolygon(name,attr_name);
                polygonEditor.close();
            });
            $("#delete").click(function(){
                deletePolygon();
            });
        });
    });
};

//添加text
var showPolygonText = function(polygon,name,mouseTool){
    var _thisPolygon = polygon;
    var textInfo;
    textInfo = new AMap.Text({
        map:map,
        position:_thisPolygon.getBounds().getCenter(),
    });
    textInfo.setText(name);
    textInfo.on('click',function(){
        textClick(_thisPolygon,mouseTool);
    });
    polygonTexts.push(textInfo);
};

//取消画面操作（参数：鼠标画面工具对象）
var removePolygonTool = function(mouseTool){
    mouseTool.close();
    var src = $("#hm").find("img").attr("src").split("_");
    $("#hm").removeClass("active").find("img").attr("src",src[0]+".png");
    polygonMouseTool = null;
    $("#hm-act").hide();
    //未选中"画面"
    hmStatus = 0;
};

//面的点击事件（参数是面对象）
var textClick = function(polygon,mouseTool){
    $("#hm-edit").show();
    var polygonObj = polygon;
    if(hmStatus == 0){
        polygonObj.setOptions({
            fillOpacity:"0.6"
        });
        showPolygonInfoWindow(polygonObj,[polygonObj.getPath()[0]['lng'],polygonObj.getPath()[0]['lat']],mouseTool);

        for(var i = 0;i<polygonObjs.length;i++){
            if(polygonObjs[i] != polygonObj){
                polygonObjs[i].setOptions({
                    fillOpacity:"0.35"
                });
            }
        }
    }
};

//点击面（参数：当前待拆分的面）
var polygonClick = function(polygon){
    var _thisPolygon = polygon;
    if(hmStatus == 0){
        $("#hm-edit").show();
        //线拆分
        var points = [];
        $.each(_thisPolygon.getPath(),function(index,item){
            points.push(item['lng']+','+item['lat']);
        });
        points.push(points[0]);
        points = points.join(";");
        $("#lineSplit").click(function(){
            setCursor();

            map.plugin(["AMap.MouseTool"],function(){
                var lineTool = new AMap.MouseTool(map);
                lineTool.polyline(); //使用鼠标工具，在地图上画标记点
                lineTool.on('draw',function(e){
                    var splitPoints = [];
                    var splitLineObj = e.obj;
                    $.each(e.obj.getPath(),function(index,item){
                        splitPoints.push(item['lng']+','+item['lat']);
                    });
                    splitPoints = splitPoints.join(";");
                    map.setDefaultCursor();
                    lineTool.close();

                    splitRegion(points,splitPoints,_thisPolygon,splitLineObj);
                });
            });
        });
        //面拆分
        $("#gonSplit").click(function(){
            setCursor();

            map.plugin(['AMap.MouseTool'],function(){
               var gonTool = new AMap.MouseTool(map);
               gonTool.polygon();
               gonTool.on('draw',function(e){
                   var splitPoints = [];
                   var splitGonObj = e.obj;
                   $.each(e.obj.getPath(),function(index,item){
                       splitPoints.push(item['lng']+','+item['lat']);
                   });
                   splitPoints.push(splitPoints[0]);
                   splitPoints = splitPoints.join(";");
                   map.setDefaultCursor();
                   gonTool.close();

                   splitRegion(points,splitPoints,_thisPolygon,splitGonObj);
               });
            });
        });
    }
};

var splitRegion = function(points,splitPoints,polygon,polyline){

    var _thisPolygon = polygon;
    var _thisPolyline = polyline;
    console.log(points);
    console.log(splitPoints);
    $.ajax({
        url:"http://172.16.14.110/gateway/geocalculator/splitRegion?ak=11",
        dataType:"jsonp",
        jsonp:"callback",
        data:{
            "points":points,
            "splitPoints":splitPoints,
            "coordType":"gcj02ll"
        },
        success:function(e){
            console.log(e.result);
            console.log(e);
            //返回成功
            if(e && e.info == "OK"){
                for(var i = 0;i<polygonObjs.length;i++){
                    if(polygonObjs[i] == _thisPolygon){
                        polygonObjs[i].setMap(null);
                        polygonObjs.splice(i,1);
                        polygonTexts[i].setMap(null);
                        polygonTexts.splice(i,1);
                    }
                }
                _thisPolyline.setMap(null);
                $.each(e.result,function(indexGrandPa,itemGrandPa){
                    if(itemGrandPa != true){
                        var pathArr = [];
                        $.each(itemGrandPa.points,function(indexFather,itemFather){
                            pathArr.push([itemFather.x,itemFather.y]);
                        });
                        drawPolygon(pathArr);
                    }
                });
            }
        }
    });
};

//重新渲染面
/****
 *param:pathArr(路径参数)
 ****/
var drawPolygon = function(pathArr){
    var  polygon = new AMap.Polygon({
        path: pathArr,//设置多边形边界路径
        strokeColor: "#1791fc", //线颜色
        strokeOpacity: 0.9, //线透明度
        strokeWeight: 3,    //线宽
        fillColor: "#1791fc", //填充色
        fillOpacity: 0.35//填充透明度
    });
    polygon.setMap(map);
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
            polygonObjs = [];
            polygonTexts = [];
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
        if(polygonMouseTool == null){
            if(polygonInfoWin == null || polygonInfoWin.getIsOpen() == false){
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

                        //将画完的数组对象存入面集合中
                        polygonObjs.push(polygonObj);

                        //如果当前的面对象不为空，保存下来，在清空当前所画的面之后，重新赋值
                        if(currentPolygon != null){
                            prevPolygon = currentPolygon;
                        }

                        currentPolygon = polygonObj;
                        var position = [polygonObj.getPath()[0]['lng'],polygonObj.getPath()[0]['lat']];

                        showPolygonInfoWindow(polygonObj,position,mousetool);

                        mousetool.close();
                        map.setDefaultCursor();
                    });
                });
            }
        }
    });
    /*画面end*/
});