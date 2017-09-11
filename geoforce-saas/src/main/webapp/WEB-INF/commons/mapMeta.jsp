
<%@page import="com.Dituhuiweb.common.Constant"%>
<!--## map js loading -->

<!-- supermap -->
<link rel=stylesheet type=text/css href="${base}/resources/js/map/supermap/SMap/SMapCity.css">
<script src="${base }/resources/js/map/supermap/libs/SuperMap.Include.js" type="text/javascript"></script>
<script src="${base }/resources/js/map/weather.js" type="text/javascript"></script>
<script src="${base }/resources/js/map/supermap/SMap/SMapCity.js" type="text/javascript"></script>
<script src="${base }/resources/js/map/supermap/osp.js" type="text/javascript"></script>
<script src="${base }/resources/js/map/supermap/supermap.js" type="text/javascript"></script>
<script src="${base}/resources/js/Dituhui/iclient-8c/CloudLayer.js" type="text/javascript"></script>
<!-- supermap -->
<!-- ###############################baidu########################### -->
<script src="http://api.map.baidu.com/api?v=2.0&ak=<%=Constant.BAIDU_MAP_KEY %>" type="text/javascript"></script>
<!-- hui zhi gong ju -->
<%--<script src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js" type="text/javascript"></script>--%>
<script src="${base }/resources/js/map/baidu/DrawingManager_1.4.js" type="text/javascript"></script>
<!-- hui zhi gong ju -->
<!-- markers ju he -->
<script src="${base }/resources/js/map/baidu/TextIconOverlay.js" type="text/javascript"></script>
<script src="${base }/resources/js/map/baidu/MarkerClusterer.js" type="text/javascript"></script>
<!-- markers ju he -->
<!-- la kuang zoom -->
<script src="http://api.map.baidu.com/library/RectangleZoom/1.2/src/RectangleZoom_min.js" type="text/javascript"></script>
<!-- la kuang zoom -->
<%--<script type="text/javascript" src="http://developer.baidu.com/map/resources/jsdemo/demo/changeMore.js"></script>--%>
<script src="${base }/resources/js/map/baidu/baidu.js" type="text/javascript"></script>
<!-- #############################end baidu########################### -->
<!-- ######################  google  ####################################### -->
<script src="${base }/resources/js/map/google/googleApi.js" type="text/javascript"></script>
<script src="${base }/resources/js/map/google/google.js" type="text/javascript"></script>
<script src="${base }/resources/js/map/google/MarkerCluterer.js" type="text/javascript"></script>
<script src="${base }/resources/js/map/google/MarkerLabel.js" type="text/javascript"></script>
<!-- ##################### end google ###################################### -->
<script src="${base }/resources/js/map/mapSupport.js" type="text/javascript"></script>
<script src="${base }/resources/js/config.js" type="text/javascript"></script>

