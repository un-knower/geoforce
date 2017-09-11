/**
 * js 监控类 定位、跟踪、图像监控、历史轨迹
 * */
/**及时定位 config*/
var stopLocateFlag = true;//及时定位停止
var personLocateTimer = null;//及时定位定时器
/**及时定位 config*/

var personZtree = null;//员工树
/**
 * 初始化进入位置监控某一功能时调用的状态清除方法
 * 如进入定位功能初始化时调用
 * @param active
 * @return
 */
function initMonitor(active){
	setCurActive(active);//in mapSupport.js
	
	clearAllInterval();
	
	var mapApiObj = loadMapApi();//in mapSupport.js
	if(mapApiObj == null){
		return;
	}
	var map = mapApiObj.map;
	if(map == null){
		return;
	}
	deactiveMapDraw();//让画点线面方式处于非激活状态 in mapSupport.js
	clearLayerFeatures();//in mapSupport.js 清除所有点线面
}
/**
 * 清除位置监控所有定时器 setTimeout setInterval等
 * @return
 */
function clearAllInterval(){//清空所有的setTimeout setInterval等
	personLocateClosed();
}
/**
 * 定时定位停止
 * @return
 */
function personLocateClosed(){
	stopLocateFlag =true;
	clearTimeout(personLocateTimer);
	personLocateTimer = null;
}
/**
 * 生成树
 * @param checkType
 * @param param请求参数
 * @return
 */
function initPersonTree(param){
	//全部车辆树
	var url = ctx+"/com/supermap/personTree";
	//请求参数
	var search = "";
	if(param){
		//检索出的车辆生成树
		url = ctx+"/com/supermap/personTreeSearch";
		search = param.searchValue;
	}
	var setting ={
		view: {
			dblClickExpand: false,
			showTitle: true
		},
		check:{enable: true,chkStyle:"checkbox"},
		data: {
			simpleData: {
				enable: true
			},
			key: {
				name:'ename',
				title:'name'
			}
		},
		async: {
			enable: true,
			url: url,
			autoParam:["id=treeId"],
			otherParam:{"personSearch":search}
		},
		callback: {
//			onClick: carTreeOnClick
		}
	};
	$('#personDeptTree').css("background-color", "transparent");
	personZtree = $.fn.zTree.init($('#personDeptTree'), setting);
}
/**
 * 获得车辆树中已选车辆id
 * @return
 */
function getSelectedPersonId(){
	var selectPersons = new Array();
	if(personZtree){
		var nodes = personZtree.getCheckedNodes(true);
		for(var i=0;i<nodes.length;i++){
			if(!nodes[i].isParent)
				selectPersons.push(nodes[i].id);
		}
	}
	return selectPersons;
}


/**
 * 主页面车辆搜索
 * @return
 */
function monitorPersonSearch(){
	var searchValue = $("#searchValue").val();
	//in common.js 特殊字符校验
	if(searchValue && !textCheck(searchValue)){
		alert("不能输入特殊字符");//搜索内容不能输入特殊字符
		return;
	}
	var param = null;
	if(searchValue){
		param = new Object();
		param.searchValue = searchValue;
	}
	//加载树
	initPersonTree(param);
}

/**
 * 及时定位
 * @return
 */
function personLocate(personIds){
	
	if(personIds == null || personIds == ""){
		var array = getSelectedPersonId();
		if(array.length == 0){
			alert("请选择人员");
			return;
		}
		personIds = array.join(",");
	}
	initMapList("locate");
	initMonitor("locate");
	//底部列表栏弹出in mapList.js
	mapListShow();
	var timer = 8;//定位频率 第一次默认为10秒
	stopLocateFlag = false;
	//in mapSupport.js 点击及时定位 表示首次定位 满屏显示所有车辆
	setInitLocateFlag(true);
	ajaxLocate(personIds,timer*1000);
}

/**
 * 及时定位操作方法
 * @return
 */
function ajaxLocate(personIds,timer){
	if(stopLocateFlag){
		return;
	}
	$.ajax({
		type : "POST",
		async : true,
		url : ctx+"/personMonitor/locate",
		data : "personIds="+personIds,
		dataType : "json",
		success : function(msg) {
			if(msg){
				if(msg.status == 1){//定位成功
					if(stopLocateFlag){//如果定位停止，不在继续打点
						return;
					}
					person_Locate(msg.info);//地图上定位打点初始化方法 in mapSupport.js
					//保证只存在一个personLocateTimer
					if(personLocateTimer){
						clearTimeout(personLocateTimer);
						personLocateTimer = null;
					}
					personLocateTimer = setTimeout(function(){
						ajaxLocate(personIds,30000);//从第二次开始每30秒查询一次位置
					},timer);
				}else{
					alert(msg.info+"，请稍后重试！");
				}
			}else{
				alert("无位置信息，请重新查询！");
			}
		},
		error : function() {
			alert("定位失败，请稍后重试！");
        }
	});
}
/**
 * 勾选人员发送通知打开弹出框
 * @return
 */
var sendMsgLayer;//
function sendMsgOpen(personIds){
	if(personIds == null || personIds == ""){
		var array = getSelectedPersonId();
		if(array.length == 0){
			alert("请先选择人员");
			return;
		}
		personIds = array.join(",");
	}
	$("#sendPersonIds").val(personIds);
	sendMsgLayer = $.layer({
		type:1,
		title: false,
		area:['auto','auto'],
		shade : [0.1 , '#000' , true],
		shade:[0],
		page:{dom:'#personSendMsgDiv'}
	});
}
/**
 * 关闭发送通知弹出层
 * @return
 */
function sendMsgClose(){
	$("#msgTitle").val('');
	$("#msgContent").val('');
	layer.close(sendMsgLayer);
}
/**
 * 发送通知
 * @return
 */
function sendMsg(){
	$("#msgTitle_error").html('&nbsp;');
	$("#msgContent_error").html('&nbsp;');
	var personIds  = $("#sendPersonIds").val();
	var title = $("#msgTitle").val();
	var content = $("#msgContent").val();
	if(!personIds){
		$("#msgTitle_error").html("未选中人员");
		return;
	}
	if(!title){
		$("#msgTitle_error").html("请输入标题");
		return;
	}
	if(!textCheck(title)){
		$("#msgTitle_error").html("标题不能输入特殊字符");
		return;
	}
	if(!content){
		$("#msgContent_error").html("请输入内容");
		return;
	}
	if(!textCheck(content)){
		$("#msgContent_error").html("内容不能输入特殊字符");
		return;
	}
	$.ajax({
		type : "POST",
		async : true,
		url : ctx+"/personMonitor/sendNote",
		data : "personIds="+personIds+"&title="+title+"&content="+content,
		dataType : "json",
		success : function(msg) {
			if(msg){
				if(msg.status == 1){//发送成功
					alert("通知已成功发送到对应员工的手机");
					sendMsgClose();
				}else{
					alert(msg.info+"，然后重试！");
				}
			}else{
				alert("发送失败，请稍后重试！");
			}
		},
		error : function() {
			alert("发送失败，请稍后重试！");
        }
	});
	
}
/**
 * 初始化地图时加载当前用户权限内的门店poi
 * @param params{curPage}当前页
 * @return
 */
var store_ajax = null;//ajax请求的对象
function getStores(params){
	if(!params){
		params = {curPage:1};
	}
	var curPage = params.curpage;
	if(curPage == null || curPage == "" || curPage == 0){
		params.curpage = 1;
	}
	
	store_ajax = $.ajax({
		type : "POST",
		async : true,
		url : ctx+"/personMonitor/storeList",
		data: "currentPageNum="+params.curpage,
		dataType : "json",
		success : function(msg) {
			//权限及sesion校验
			if(msg == null || msg == ""){
				return;
			}
			if(msg.status == 0){
				return;
			}
			var page = msg.info;
			if(!page){
				return;
			}
			var data = page.result;
			curPage = page.currentPageNum;
			var totalNum = page.totalNum;
			var pageSize = page.pageSize;//每页记录数
			if(!data){
				return;
			}
			if(curPage == 1){//第一页初始化时清空门店点						
				clearPois();//in mapSupport.js
			}
			map_addPois(data,0);//地图上绘制兴趣点
			if(curPage * pageSize < totalNum){
				if(ps_mapType == "baidu"){
					if($.browser.msie && (curPage * pageSize > 900) ){//ie版本最多显示900条记录
						return false;
					}
				}
				params.curpage = curPage+1;
				getStores(params);
			}
		},
		error : function() {
        }
	});
}
/**
 * 中断poi ajax加载
 * @return
 */
function abortStoreLoad(){
	if(store_ajax)
		store_ajax.abort();
}