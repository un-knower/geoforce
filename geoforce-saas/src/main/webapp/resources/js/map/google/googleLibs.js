/**
 * GOOGLE地图开发使用工具
 * @date 2013-08-23
 * @notice 地图容器的z-index不能小于0，否则鼠标地图无法进行地图操作（可以看到地图，不过你会万分苦恼）
 */
(function(){
    window.map= gg.map;
    window.lineFeature=null;
    window.markers=[];
    window.infoWindow=null;
    window.GoogleUtil={};
    GoogleUtil={
        CONSTANT:{
            mapkey:'AIzaSyAY-HsXXPsBUqsbQLDFO8kpNWLANwH0E7k',
            container:"map",
            DEFAULT_ZOOM:12,
            zoomAddFeature:19,
            centerLat:30.65721817,
            centerLng:104.06594494,
            mapstatus:false,
            isnewMap:false,
            ZOOM_MAX:19,
            ZOOM_MIN:0,
            markerSize:32
        },
        /**
         * 控制地图显示范围为中国
         */
        mapShowBounds:function(){
            var strictBounds = new google.maps.LatLngBounds(
                new google.maps.LatLng(14.48003790418668, 66.28120434863283),
                new google.maps.LatLng(54.44617552862156, 143.71284497363283)
            );
            google.maps.event.addListener(map, 'dragend',function() {
                if (strictBounds.contains(map.getCenter())) {
                    return;
                }
                var c = map.getCenter(),
                x = c.lng(),
                y = c.lat(),
                maxX = strictBounds.getNorthEast().lng(),
                maxY = strictBounds.getNorthEast().lat(),
                minX = strictBounds.getSouthWest().lng(),
                minY = strictBounds.getSouthWest().lat();
                if (x < minX){x = minX;}
                if (x > maxX){x = maxX;}
                if (y < minY){y = minY;}
                if (y > maxY){y = maxY;}
                map.setCenter(new google.maps.LatLng(y, x));
            });
        },
        /**
         * 控制地图的缩放级别
         */
        limitShowMapZoom:function(zoom){
            this.CONSTANT.zoomMax=zoom;
            var limitedZoom=this.CONSTANT.zoomMax;
            google.maps.event.addListener(map, 'zoom_changed',function() {
                if (map.getZoom() < limitedZoom){map.setZoom(limitedZoom);}
            });
        },
        /**
         * 异步加载谷歌API
         */
        loadScript:function(){
            var script = document.createElement("script");
            script.type = "text/javascript";
            script.src = "http://maps.googleapis.com/maps/api/js?v=3&key="+this.CONSTANT.mapkey+"&sensor=false&libraries=drawing,places";
            document.body.appendChild(script);
        },
        /**
         * 谷歌街道
         */
        initStreetMap:function(key){
          this.CONSTANT.mapkey=key|| this.CONSTANT.mapkey;
          var mapOptions = {
                center: new google.maps.LatLng(GoogleUtil.CONSTANT.centerLat,GoogleUtil.CONSTANT.centerLng),
                zoom: this.CONSTANT.DEFAULT_ZOOM,
                panControl: true,
                zoomControl: true,
                mapTypeControl: false,
                scaleControl: true,
                scrollwheel:true,
                draggable:true,
                overviewMapControl: true,
                streetViewControl:true,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                navigationControlOptions: {
                        style: google.maps.NavigationControlStyle.ZOOM_PAN,
                        position: google.maps.ControlPosition.TOP_LEFT
                },
                zoomControlOptions:{
                     style: google.maps.ZoomControlStyle.SMALL,//DEFAULT,LARGE,SMALL
                     position: google.maps.ControlPosition.TOP_LEFT
                }
            };
            map = new google.maps.Map(document.getElementById(this.CONSTANT.container),mapOptions);
            //测距控件
            GoogleUtil.control.addRulerControl();
            //清除测距
            GoogleUtil.control.addClearRulerOverlayControl();
        },
        /**
         * 谷歌卫星
         */
        initSatelliteMap:function(key){
            this.CONSTANT.mapkey=key|| this.CONSTANT.mapkey;
            var mapOptions = {
                    center: new google.maps.LatLng(GoogleUtil.CONSTANT.centerLat,GoogleUtil.CONSTANT.centerLng),
                    zoom: this.CONSTANT.DEFAULT_ZOOM,
                    panControl: true,
                    zoomControl: true,
                    mapTypeControl: false,
                    scrollwheel:true,
                    draggable:true,
                    scaleControl: true,
                    overviewMapControl: true,
                    mapTypeId: google.maps.MapTypeId.SATELLITE,
                    navigationControlOptions: {
                        style: google.maps.NavigationControlStyle.ZOOM_PAN,
                        position: google.maps.ControlPosition.TOP_LEFT
                    },
                    zoomControlOptions:{
                         style: google.maps.ZoomControlStyle.SMALL,//DEFAULT,LARGE,SMALL
                         position: google.maps.ControlPosition.TOP_LEFT
                    }
                };
            map = new google.maps.Map(document.getElementById(this.CONSTANT.container),mapOptions);

            //测距控件
            GoogleUtil.control.addRulerControl();
            //清除测距
            GoogleUtil.control.addClearRulerOverlayControl();
        },
        /**
         * 谷歌手机
         */
        initMobileStreetMap:function(container,key){
             this.CONSTANT.mapkey=key|| this.CONSTANT.mapkey;
              var mapOptions = {
                    center: new google.maps.LatLng(GoogleUtil.CONSTANT.centerLat,GoogleUtil.CONSTANT.centerLng),
                    zoom: this.CONSTANT.DEFAULT_ZOOM,
                    panControl: false,
                    zoomControl: true,
                    mapTypeControl: false,
                    scaleControl: true,
                    scrollwheel:true,
                    draggable:true,
                    overviewMapControl: true,
                    streetViewControl:true,
                    mapTypeId: google.maps.MapTypeId.ROADMAP,
                    navigationControlOptions: {
                            style: google.maps.NavigationControlStyle.ZOOM_PAN,
                            position: google.maps.ControlPosition.TOP_LEFT
                    },
                    zoomControlOptions:{
                         style: google.maps.ZoomControlStyle.SMALL,//DEFAULT,LARGE,SMALL
                         position: google.maps.ControlPosition.TOP_LEFT
                    }
                };
                map = new google.maps.Map(document.getElementById(container||this.CONSTANT.container),mapOptions);
                //this.mapShowBounds();
        },
        /**
         * 居中或缩放
         */
        centerAndZoom:function(latlng,zoom){
            if(latlng){map.setCenter(latlng);}
            if(zoom){map.setZoom(zoom);}
        },
        /**
         * 获取图片对象
         */
        getIcon:function(imageUrl,size){
            var imgSize=size||32;
            var offSize=imgSize/2;
            var defaultSize=new google.maps.Size(imgSize, imgSize);
            var myIcon={
                    url: imageUrl,
                    size: defaultSize,
                    scaledSize:new google.maps.Size(imgSize,imgSize),
                    origin: new google.maps.Point(0,0),
                    anchor: new google.maps.Point(offSize,offSize)
            };
            return myIcon;
        },
        /**
         * 居中并缩放
         */
        centerAndZoom:function(point,zoom){
            map.setCenter(point);
            map.setZoom(zoom);
        },
        /**
         * 创建一个地图bounds对象
         * @param points
         */
        createBounds:function(points){
            if(points) {
                var bounds=new google.maps.LatLngBounds();
                for ( var i = 0; i < points.length; i++) {
                    var point=points[i];
                    if(point){
                        bounds.extend(point);
                    }
                }
                return bounds;
            }
            return null;
        },
        /**
         * 设置适合的地图边界范围Bounds
         * @param points
         */
        panToBounds:function(points){
            if(points){
                var bounds=this.createBounds(points);
                if(bounds){map.panToBounds(bounds);}
            }
        },
        /**
         * 设置合适的覆盖物显示范围(覆盖物聚合)
         */
        getViewport:function(points){
            if(points){
                var bounds=this.createBounds(points);
                if(bounds) {
                    map.fitBounds(bounds);
                }
            }
        },
        /**
         * 点越界处理
         * @param point
         */
        ifOutBoundsPantoCenter:function(point){
            var bounds=map.getBounds();
            var flag=bounds.contains(point);
            if(flag==false){
                map.panTo(point);
                this.centerAndZoom(point, map.getZoom());
            }
        }
    };

    var iterator=0,scount=0,playStatus=0;

    GoogleUtil.tools={
        /**
         * 创建信息窗体
         */
        createInfoWindow:function(latlng,htmlContent){
            var infowindow = new google.maps.InfoWindow({
                  content: htmlContent,
                  position:latlng,
                  disableAutoPan:false
              });
            return infowindow;
        },
        /**
         * 添加信息窗体
         */
        addInfoWindow:function(latlng,htmlContent,isCenter){
            if(!infoWindow){
                infoWindow=this.createInfoWindow(latlng, htmlContent);
            }else{
                infoWindow.close();
                infoWindow.setPosition(latlng);
                infoWindow.setContent(htmlContent);
            }
            infoWindow.open(map);
            if(isCenter){map.setCenter(latlng);}
        },
        /**
         * 创建普通标注
         */
        createMarker:function(id,title,point,icon){
            var marker = new google.maps.Marker({
                position: point,
                map: map,
                icon:icon,
                id:id
             });
             marker.id=id;
             marker.setTitle(title);
            return marker;
        },
        /**
         * 创建带有文本描述的标注
         */
        createMarkerWithLabel:function(markerOption){
        	var id = markerOption.id || null;
        	var position = markerOption.position || null;
        	if(position == null) return;
        	var text = markerOption.text || "";
        	var labelAnchor = markerOption.labelAnchor || new google.maps.Point(-1,15)
        	var labelClass = markerOption.labelClass || "labels";
        	var labelStyle = markerOption.labelStyle || {};
        	var icon = markerOption.icon || null;
        	
        	var color = labelStyle.color || 'navy';
        	var opacity = labelStyle.opacity || 1.0;
        	var borderColor = labelStyle.borderColor || 'white';
        	var fontSize = labelStyle.fontSize || '12px';
        	var fontWeight = labelStyle.fontWeight || 'normal';
            var marker = new MarkerWithLabel({
                position: position,
                draggable: false,
                map: map,
                labelContent: text,
                labelAnchor: labelAnchor,
                labelClass: labelClass,
                icon: icon,
                labelStyle: {
            		opacity: opacity,
            		borderColor:borderColor,
            		color:color,
            		fontSize:fontSize,
            		fontWeight:fontWeight,
            		fontFamily:'宋体',
            		whiteSpace:'nowrap'
            	}
             });
//            if(id) marker.id = id;
            return marker;
        },
        /**
         * 添加普通标注
         */
         addCommonMarker:function(id,title,point,icon){
            var marker =this.createMarker(id, title, point, icon);
            markers.push(marker);
            marker.setMap(map);
            return marker;
         },
         /**
          * 添加文本标注
          */
         addMarkerWithLabel:function(id,title,point,icon){
             var marker =this.createMarkerWithLabel({id:id, text:title, position:point, icon:icon});
             markers.push(marker);
             marker.setMap(map);
             return marker;
         },
        /**
         * 添加标注
         */
         addMarker:function(id,title,point,icon){
            var marker =this.addMarkerWithLabel(id, title, point, icon);
            return marker;
         },
         /**
          * 批量添加标注
          */
         addMarkers:function(points){
             if(points){
                 for ( var i = 0; i < points.length; i++) {
                    var point=points[i];
                    this.addMarker(point);
                 }
             }
         },
         /**
          * 添加跟踪轨迹线条
          */
         addLineFeature:function(id,points,style){
             lineFeature = new google.maps.Polyline({
                  path:points,
                  strokeWeight : style.strokeWeight,
                  strokeColor :style.strokeColor,
                  map: map
             });
             lineFeature.id=id;
             lineFeature.track=id;
             markers.push(lineFeature);
             return lineFeature;
         },
         /**
          * 添加折线(轨迹,包括起点、终点)
          */
         addLineFeatureAndStartAndEndPoint:function(spObj,points, startImageUrk,endImageUrk,lineStyle){
             var len=points.length;
             var index =len - 1;
             var startPoint = points[0];
             var endPoint =points[index];
             var startIcon = GoogleUtil.getIcon(startImageUrk,20);
             var endIcon = GoogleUtil.getIcon(endImageUrk,20);
             this.addMarker("start", spObj.start, startPoint, startIcon);
             this.addMarker("end", spObj.end, endPoint, endIcon);
             if(len>=2){
                var d=(len/2)+"";
                d=parseInt(d);
                GoogleUtil.centerAndZoom(points[d],12);
             }
             this.addLineFeature("track_line",points,lineStyle);
         },
         /**
          * 标注动画
          */
         markerAnimate:{
             dropSetTimeout:{
                 drop:function(points){
                     iterator=0;
                     for (var i = 0; i < points.length; i++) {
                            setTimeout(function() {
                                GoogleUtil.tools.markerAnimate.dropSetTimeout.addMarker(points);
                            }, i * 200);
                     }
                 },
                 addMarker:function(points){
                     markers.push(new google.maps.Marker({
                            position: points[iterator],
                            map: map,
                            draggable: false,
                            animation: google.maps.Animation.DROP
                    }));
                    iterator++;
                 }
             }
         },
         /**
          * 轨迹操作
          */
         track:{
             /**
              * 添加轨迹线条
              */
             addLineTrack:function(points){
                 if(points){
                     var lineCoordinates=[];
                     for ( var i = 0; i < points.length; i++) {
                          var point=points[i];
                          if(point){
                              lineCoordinates.push(point);
                          }
                     }
                     var lineSymbol = {
                               path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
                               //scale: 2,
                               strokeColor: 'green'
                         };
                     lineFeature = new google.maps.Polyline({
                           path: lineCoordinates,
                           strokeColor: 'red',
                           strokeWeight : 3,
                           icons: [{
                              icon: lineSymbol,
                              offset: '0%'
                                }],
                           map: map
                    });
                    lineFeature.id="track_line";
                 }
             },
             /**
              * 轨迹回放操作
              */
             operate:{
                 count:0,
                 object:null,
                 addListener:function(){
                       var animate=window.setInterval(function() {
                            scount = (scount + 1) % 200;
                            var icons = lineFeature.get('icons');
                            icons[0].offset = (scount / 2) + '%';
                            lineFeature.set('icons', icons);
                            //终点停车
                            if((scount / 2)>=99){
                                 clearInterval(this);
                            }
                      }, 1000);
                      this.object=animate;
                 },
                 play:function(){
                      playStatus=1;
                      scount=0;
                      lineFeature.playStatus=playStatus;
                      this.addListener();
                 },
                 continuePlay:function(){
                      playStatus=3;
                      lineFeature.playStatus=playStatus;
                     this.addListener();
                 },
                 pause:function(){
                      playStatus=2;
                      lineFeature.playStatus=playStatus;
                     if(this.object){clearInterval(this.object);}
                 },
                 stop:function(){
                      playStatus=4;
                      lineFeature.playStatus=playStatus;
                     if(this.object){clearInterval(this.object);}
                     scount=0;
                 }
             }
         },
         getOverlayByNodeId:function(id,value){
             for (var i = 0; i < markers.length; i++) {
                var marker=markers[i];
                if(marker[id]==value){
                    return marker;
                }
             }
             return null;
         },
         /**
          * 删除或显示覆盖物
          */
         deleteOrShowMarkerOverlayers:function(map){
             for (var i = 0; i < markers.length; i++) {
                    if(map==null){markers[i].setVisible(false);}
                    markers[i].setMap(map);
             }
             if(map==null){markers = [];}
         },
         /**
          * 删除轨迹
          */
         deleteTrack:function(){
             if(lineFeature){
                 lineFeature.setVisible(false);
                 lineFeature.setMap(null);
             }
         },
         /**
          * 移除所有覆盖物
          */
         removeAllOverlays:function(){
             for (var i = 0; i < markers.length; i++) {
                 markers[i].setVisible(false);
                 markers[i].setMap(map);
             }
             markers = [];
         },
         /**
          * 移除一个覆盖物
          */
         removeOverlay:function(propertyName,value){
             if(value){
                 for (var i = 0; i < markers.length; i++) {
                     var marker=markers[i];
                     if(marker[propertyName]==value){
                         markers[i].setVisible(false);
                         markers[i].setMap(map);
                     }
                 }
             }
             if(propertyName=="track"||propertyName=="track_line"){
                 if(lineFeature){
                     lineFeature.setVisible(false);
                     lineFeature.setMap(null);
                     lineFeature=null;
                 }
             }
         },
         /**
          * 显示或隐藏标注
          */
         isToShowMarkers:function(markers,bool){
             if(markers){
                 for (var i = 0; i < markers.length; i++) {
                     var marker=markers[i];
                     marker.setVisible(bool);
                 }
             }
         },
         /**
          * 删除轨迹覆盖物
          */
         removeTrackLineWithStartAndEndOverlay:function(){
             this.removeOverlay("id", "track_line");
             this.removeOverlay("id", "track");
             this.removeOverlay("id", "start");
             this.removeOverlay("id", "end");
             if(lineFeature){
                 lineFeature.setVisible(false);
                 lineFeature.setMap(null);
                 lineFeature=null;
             }
             this.removeAllOverlays();
         }
    };

    GoogleUtil.event={
            /**
             * 地图缩放事件
             */
            mapZoomChanged:function(markers,zoom){
                var listener=google.maps.event.addListener(map, 'zoom_changed', function(event) {
                     if(map.getZoom()<zoom){
                         var myMarkers=markers;
                         GoogleUtil.tools.isToShowMarkers(markers,false);//隐藏标注
                         markers=myMarkers;
                     }else{
                         GoogleUtil.tools.isToShowMarkers(markers,true);//显示标注
                     }
                 });
                return listener;
            },
            /**
             * 点击标注事件
             */
            markerClick:function(marker){
                var listener=google.maps.event.addListener(marker, 'click', function(event) {
                     marker.infoWindow.open(map,marker);
                 });
                return listener;
            },
            /**
             * 移除监听对象
             */
            removeListener:function(listener){
                google.maps.event.removeListener(listener);
            }
    };

    //测距变量
    var polyline;
    var polylinesArray = [];
    //距离标记数组
    var lenArray = [];
    var rulerActions=[];

    GoogleUtil.control={
            /**
             * 测距控件
             */
            addRulerControl:function(){
                var RulerControl=function(rulerControlDiv, map){

                    rulerControlDiv.style.padding = '5px';

                    //  CSS 边框
                    var controlUI = document.createElement('div');
                    controlUI.style.backgroundColor = 'white';
                    controlUI.style.color = '#888888';
                    controlUI.style.borderStyle = 'solid';
                    controlUI.style.borderWidth = '1px';
                    controlUI.style.cursor = 'pointer';
                    controlUI.style.textAlign = 'center';
                    controlUI.title = '点击测量距离';
                    rulerControlDiv.appendChild(controlUI);

                    // CSS 文本
                    var controlText = document.createElement('div');
                    controlText.style.fontFamily = 'Arial,sans-serif';
                    controlText.style.fontSize = '10px';
                    controlText.style.paddingLeft = '4px';
                    controlText.style.paddingRight = '4px';
                    controlText.innerHTML = '测距';
                    controlUI.appendChild(controlText);

                    google.maps.event.addDomListener(controlUI, 'click', function() {
                        //启动整个地图的click侦听
                        var rule = google.maps.event.addListener(map,"click",function(event){
                            var marker = new MarkerWithLabel({
                                   position: event.latLng,
                                   draggable: false,
                                   map: map,
                                   labelContent: "0.000公里",
                                   labelAnchor: new google.maps.Point(-1, -1),
                                   labelClass: "labels",
                                   labelStyle: {opacity: 1.0},
                                   icon: {}
                                });
                                //将标记压入数组
                                lenArray.push(marker);
                                //计算距离
                                var distance = GoogleUtil.control.drawOverlay();
                                marker.set("labelContent",distance);
                        });
                        rulerActions.push(rule);
                    });
                };

                var rulerControlDiv = document.createElement('div');
                RulerControl(rulerControlDiv, map);
                rulerControlDiv.index = 1;
                map.controls[google.maps.ControlPosition.LEFT_TOP].push(rulerControlDiv);
            },
            /**
             * 清除测距
             */
            addClearRulerOverlayControl:function(){
                var RulerControl=function(rulerControlDiv, map){

                    rulerControlDiv.style.padding = '5px';

                    //  CSS 边框
                    var controlUI = document.createElement('div');
                    controlUI.style.backgroundColor = 'white';
                    controlUI.style.color = '#888888';
                    controlUI.style.borderStyle = 'solid';
                    controlUI.style.borderWidth = '1px';
                    controlUI.style.cursor = 'pointer';
                    controlUI.style.textAlign = 'center';
                    controlUI.title = '点击清除测距';
                    rulerControlDiv.appendChild(controlUI);

                    // CSS 文本
                    var controlText = document.createElement('div');
                    controlText.style.fontFamily = 'Arial,sans-serif';
                    controlText.style.fontSize = '10px';
                    controlText.style.paddingLeft = '4px';
                    controlText.style.paddingRight = '4px';
                    controlText.innerHTML = '清除';
                    controlUI.appendChild(controlText);

                    google.maps.event.addDomListener(controlUI, 'click', function() {
                        while(rulerActions[0]){
                            google.maps.event.removeListener(rulerActions.pop());
                        }
                        while(lenArray[0]){
                            lenArray.pop().setMap(null);
                        }
                        if (polylinesArray) {
                            for (i in polylinesArray) {
                                polylinesArray[i].setMap(null);
                            }
                            polylinesArray = [];
                        }
                    });
                };

                var rulerControlDiv = document.createElement('div');
                RulerControl(rulerControlDiv, map);
                rulerControlDiv.index = 1;
                map.controls[google.maps.ControlPosition.LEFT_TOP].push(rulerControlDiv);
            },
            /**
             * 测距绘制
             */
            drawOverlay:function(){
                //路线数组
                var flightPlanCoordinates = [];
                //将坐标压入路线数组
                if (lenArray) {
                    for (i in lenArray) {
                        flightPlanCoordinates.push(lenArray[i].getPosition());
                    }
                }
                //路径选项
                var polylineOptions = {
                    path : flightPlanCoordinates,
                    map : map,
                    strokeColor : "#FC7F43",
                    strokeOpacity : 1.0,
                    strokeWeight : 2
                };
                polyline = new google.maps.Polyline(polylineOptions);
                //清除原有折线路径
                if (polylinesArray) {
                    for (i in polylinesArray) {
                        polylinesArray[i].setMap(null);
                    }
                    polylinesArray = [];
                }
                polyline.setMap(map);
                polylinesArray.push(polyline);
                return ((polyline.getLength()/1000).toFixed(3) + "公里");
            }


    };

    //google 测距
    google.maps.LatLng.prototype.distanceFrom = function(latlng) {
        var lat = [this.lat(), latlng.lat()];
        var lng = [this.lng(), latlng.lng()];
        var R = 6378137;
        var dLat = (lat[1] - lat[0]) * Math.PI / 180;
        var dLng = (lng[1] - lng[0]) * Math.PI / 180;
        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat[0] * Math.PI / 180) * Math.cos(lat[1] * Math.PI / 180) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var d = R * c;
        return Math.round(d);
    };

    google.maps.Marker.prototype.distanceFrom = function(marker) {
        return this.getPosition().distanceFrom(marker.getPosition());
    };

    google.maps.Polyline.prototype.getLength = function() {
        var d = 0;
        var path = this.getPath();
        var latlng;
        for (var i = 0; i < path.getLength() - 1; i++) {
            latlng = [path.getAt(i), path.getAt(i + 1)];
            d += latlng[0].distanceFrom(latlng[1]);
        }
        return d;
    };

})();
//window.onload= GoogleUtil.loadScript();