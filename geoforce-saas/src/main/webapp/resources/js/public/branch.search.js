/**
 * 初始化时查询样式
 */
$(function(){
    Dituhui.Point.Style.search();
})

/**
 * 查询网点的公共使用函数
 * 需要已定义Map
 */
/**
 * 查询网点
 */
Map.searchBranches = function() {
    layer_branches.removeAllFeatures();

    var param = { pageNo: -1 };
    Dituhui.Point.Search(param, 
        function(data) {
            var level = $('.smcity').attr('level');
            if(level != '0') {
                Map.showBranchesToMap(data);
            }
            else {
                Dituhui.Point.National.display(data);
            }
        },
        function(error) {

        }
    );
}

/**
 * 地图上显示网点数据
 */
Map.showBranchesToMap = function(data) {
    // map.removeAllPopup();
    layer_branches.removeAllFeatures();
    var len = data ? data.length : 0;
    if( len === 0 ) {
        Dituhui.showPopover("当前查询到0条网点数据");
        return;
    }
    var pois = [], dcode = Dituhui.User.dcode;
    for( var i=0; i<len; i++ ) {
        var item = data[i];
        
        if(!item.smx || item.smx == 0) {
            continue;
        }

        if( !item.iconStyle ) {
            item.iconStyle = urls.server + "/resources/assets/map/red/s.png";
        }
        var style = Dituhui.Point.getBranchStyle( item.iconStyle );
        
        var size = new SuperMap.Size(style.width, style.height);
        
        if( typeof(Baidu) != 'undefined' && Baidu && Baidu.using) {
			var coord = Baidu.getCoord(item.smx, item.smy);
			item.smx = coord.x;
			item.smy = coord.y;
		}

        var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);            
        var poi = new SuperMap.Feature.Vector(geo_point);
        
        /**
		 * 不是自己的区划
		 */
		item.belongToOthers = false;
		item.dcode = !!item.dcode ? item.dcode : dcode.substr(0, 8);
		if(item.dcode.substr(0, dcode.length) !== dcode) {
			item.belongToOthers = true;
		}
        poi.style = Dituhui.Point.getBranchStyle( item );

        poi.attributes = item;
        poi.attributes.type = "point";
        pois.push(poi);
    }
    layer_branches.addFeatures(pois);
}

Map.selectBranch = function(poi) {
    var marker = poi.attributes;
    marker.lonlat = new SuperMap.LonLat(marker.smx, marker.smy);
    Map.openBranchAttrPopup(marker);
}

/**
 * 点击网点，弹出属性
 */
Map.openBranchAttrPopup = function(marker) {
    var netPicPath = marker.netPicPath ? 
        urls.server +'/pointService/getImg?path=' + marker.netPicPath 
        : "assets/map/branch.png";

    var h =  '<div class="map-popup">';
        h += Dituhui.Point.getAttrPopupHtml(marker, false);
        h += '</div>';

    Map.popup = new SuperMap.Popup.FramedCloud("popup-near",
        marker.lonlat,
        new SuperMap.Size(300, 70),
        h,
        null,
        false,
        null
    );
    Map.popup.autoSize = true;
    Map.popup.panMapIfOutOfView = true;
    Map.popup.relativePosition = "tr";
    map.removeAllPopup();
    map.addPopup(Map.popup);

    var span = $(".bind-cars");
    Dituhui.Point.getBindingCars( marker.id, 
        function(data){
            span.html(data);
        },
        function(){
            span.html("");
        }
    );
    Map.Pictures.search(marker.id);

    $('.popup-close').unbind("click").click(function(){
        Map.popup.hide();
    });
    $('.popup-edit').unbind("click").click(function(){
        Map.showEditOrder(marker);
    });
}

Map.Pictures = {};
Map.Pictures.search = function(id) {
    //查询网点照片
    Dituhui.Point.Pictures.search( id,
        function(data){
            var len = data.length, h = '', h_1 = '';
            Map.Pictures.view(data);

            var html  = '<img src="'+ urls.server + '/pointService/getImg?path=' + data[0].filepath.replace(/\\/g, '/') + '">';
                html += '<div class="count"><span>'+ len +'张</span></div>';
            $('.photo').removeClass('none').html(html).unbind('click').click(function(){
                $('.view-pictures').removeClass("hide");
            });

            $('.upload-pictures-title').html('图集('+ len +')');
        },
        function(){
            $('.photo').removeClass('none').unbind('click').addClass('none').html('');
        }
    );
}

Map.Pictures.view = function(e) {
    var data = [];

    for(var i=0, len=e.length; i<len; i++) {
        data.push({
            image: urls.server + '/pointService/getImg?path=' + e[i].filepath.replace(/\\/g, '/')
        })
    }
    
    Map.Pictures.viewPictures(data);
}
var rotateAngle = 0, galleria_zoom = 0;
Map.Pictures.viewPictures = function(data) {    
    rotateAngle = 0;
    galleria_zoom = 0;

    if(Galleria.destroy) {
        Galleria.destroy();
    }
    if( 0 == Galleria.getLoadedThemes().length ) {
        Galleria.loadTheme(urls.server + '/resources/js/public/galleria/themes/classic/galleria.classic.min.js');
    }
    
    Galleria.run("#images", {
        dataSource: data,
        image_crop: true,
        transition: 'fade',
        maxScaleRatio: 1,
        thumbCrop: false
    });

    $('.show-img-btn.zoom-in').unbind('click').click(function(){
        if (!(galleria_zoom >= 5)) {
            var e = $($(".images").data("galleria").getActiveImage())
              , t = e.width()
              , a = e.height();
            e.width(1.2 * t),
            e.height(1.2 * a);
            var r = e.parent(".galleria-image").width() / 2 - e.width() / 2;
            e.css("left", r);
            var i = e.parent(".galleria-image").height() / 2 - e.height() / 2;
            e.css("top", i),
            galleria_zoom++
        }
    });
    
    $(".show-img-btn.turn-left").unbind('click').click(function() {
        rotateAngle -= 90,
        $($(".images").data("galleria").getActiveImage()).rotate({
            animateTo: rotateAngle
        })
    });
    
    $(".show-img-btn.turn-right").unbind('click').click(function() {
        rotateAngle += 90;
        console.log($($(".images").data("galleria").getActiveImage()))
        $($(".images").data("galleria").getActiveImage()).rotate({
            animateTo: rotateAngle
        })
    });
    
    $(".show-img-btn.full-img").unbind('click').click(function() {
        var e = $(".images").data("galleria").getActiveImage();
        window.open(e.src.split("@")[0])
    });
    
    $(".show-img-btn.shut").click(function() {
        $('.view-pictures').addClass("hide");
    });
    
    $(".show-img-btn.zoom-out").unbind('click').click(function() {
        if (!(-5 >= galleria_zoom)) {
            var e = $($(".images").data("galleria").getActiveImage())
              , t = e.width()
              , a = e.height();
            e.width(t / 1.2),
            e.height(a / 1.2);
            var r = e.parent(".galleria-image").width() / 2 - e.width() / 2;
            e.css("left", r);
            var i = e.parent(".galleria-image").height() / 2 - e.height() / 2;
            e.css("top", i),
            galleria_zoom--
        }
    });
}