var map;
var marker;

var singleDistributeArray = new Array();

var batchResult;
var pageSize = 5;
var pageBarOptions = {
	bootstrapMajorVersion: 3,
	size: "small",
	tooltipTitles: function(type, page, current) {},
	itemTexts: function (type, page, current) {
		switch (type) {
			case "first": return "首页";
			case "prev": return "上页";
			case "next": return "下页";
			case "last": return "末页";
			case "page": return page;
		}
	},
	onPageClicked: function(event, originalEvent, type, page) {
		refreshTable(page);
	}
};

var initMap = function() {
	$("#map").height($("#leftPanel").height());

	map = new AMap.Map('map', {
		position: [104.065735,30.659462],
		zoom: 15
	});
	AMap.plugin([ 'AMap.ToolBar', 'AMap.Scale', 'AMap.OverView' ],
		function() {
			map.addControl(new AMap.ToolBar());

			map.addControl(new AMap.Scale());

			map.addControl(new AMap.OverView({
				isOpen : true
			}));
		});

}

var showMarker = function(x,y) {
	if (marker) {
        marker.setMap(null);
        marker = null;
    }
	marker = new AMap.Marker({
      	position: [x,y]
 	});
  	marker.setMap(map);
  	
  	map.setZoomAndCenter(15, marker.getPosition());
}

var singleDistribute = function(address) {
	if(address == "") {
		$("#address").parent().addClass("has-error");
		$("#result").text("无");
  		$("#result").removeClass("result");
		return;
	} else {
		$("#address").parent().removeClass("has-error");
		$("#singleDistribute").attr("disabled", "disabled");
	}
	
	$.ajax({
		url: "geocoding/single",
		data: "address=" + encodeURIComponent(address),
		success: function(json) {
			$("#singleDistribute").removeAttr("disabled");
			
			showMarker(json.x,json.y);

			for (var i = 0;i<singleDistributeArray.length;i++){
				if (singleDistributeArray[i][0].contains(marker.getPosition())){
					var name = singleDistributeArray[i][1];
					$('#result').text(name);
                    	$("#result").addClass("result");
                    	break;//跳出循环
                }else{
                    $("#result").text("无");
                    	$("#result").removeClass("result");
                }
			}
		}
	});
}

var reset = function() {
	if (marker) {
        marker.setMap(null);
        marker = null;
    }
	
	map.setZoomAndCenter(15, [104.065735,30.659462]);
	
	$("#address").val("");;
	
	$("#result").text("无");
	$("#result").removeClass("result");
}

var refreshTable = function(currentPage) {
	$("#table tr").remove(".tableRow");
	
	var start = (currentPage - 1) * pageSize;
	var end = currentPage * pageSize - 1;
	for(var i=start; i<=end; i++) {
		if(batchResult[i]) {
			$("#table").append("<tr class='tableRow' index='" + i + "'><td>" + batchResult[i].id + 
					"</td><td>" + batchResult[i].address + 
					"</td><td class='result'>" + batchResult[i].area + "</td></tr>");
		}
	}
	
	$(".tableRow").click(function(e) {
		showMarker(batchResult[$(this).attr("index")].x, batchResult[$(this).attr("index")].y);
	})
	
	$("#map").height($("#leftPanel").height());
}

var bindOneClick4EditClass = function() {

	$("#area .edit").unbind();
	$("#area .edit").one("click",function() {
		var fullName = $(this).parent().prev().text();
		$(this).parent().prev().empty().append($("<input type='text' class='form-control input-sm' placeholder='"+fullName+"'>"));
		$(this).children("span").removeClass("glyphicon-edit").addClass("glyphicon-ok");
		$(this).removeClass("edit").addClass("ok");
		//编辑状态下不能导入
        $("#import").attr("disabled","disabled");

		$("#area .ok").one("click",function() {
			//点击最后一个'.ok'的时候   《导入》按钮可用
            var ok = $("#area .ok");
            if (ok.length == 1){
                $("#import").removeAttr("disabled");
            }

			var areaName = $(this).parent().prev().children("input").val().trim();
			if(areaName=="") {
				areaName = $(this).parent().prev().children("input").attr("placeholder");
			}
			$(this).parent().prev().empty().append(areaName);
			$(this).children("span").removeClass("glyphicon-ok").addClass("glyphicon-edit");
			$(this).removeClass("ok").addClass("edit");

			bindOneClick4EditClass();
		});

	});
}

var refreshSelect = function(json, level) {
	var time = null;
	var select = null;
	
	if(level == 1) {
		select = $("#province");
	} else if(level == 2) {
		select = $("#city");
		$("#city").empty();
		$("#county").empty();
	} else if(level == 3) {
		select = $("#county");
		$("#county").empty();
	}
	
	$.each(json, function(index, item) {
		select.append($("<option>").val(item.admincode).text(item.name)
				.attr("fullName", item.fullName).attr("level", item.level));
	});
	
	select.children().click(function() {
		clearTimeout(time);
		var option = $(this);
		time = setTimeout(function() {
			if(level == 3) {
				bootoast.toast({
				    "message": "<p>无第四级行政区数据权限！</p>",
				    "type": "info",
				    "position": "bottom-right",
				    "icon": "",
				    "timeout": "2",
				    "animationDuration": "300",
				    "dismissable": false
				});
			} else {
				getChildDistrict(option.val(), parseInt(option.attr("level")) + 1);					
			}
	    }, 300);
	});
	
	select.children().dblclick(function() {
		clearTimeout(time);
		
		if($("#area tr[admincode='"+$(this).val()+"']").length == 0) {
			var trStr = "<tr admincode='"+$(this).val()+"'>"+
						"<td>"+$(this).attr("fullName")+"</td>"+
						"<td>"+
							"<button type='button' class='btn btn-xs btn-link pull-right delete'>"+
						  		"<span class='glyphicon glyphicon-trash' aria-hidden='true'></span>"+
						  	"</button>"+
						  	"<button type='button' class='btn btn-xs btn-link pull-right edit'>"+
						  		"<span class='glyphicon glyphicon-edit' aria-hidden='true'></span>"+
						  	"</button>"+
						"</td>"+
					"</tr>";
			$("#area").append($(trStr));

			$("#area .delete").click(function() {
				$(this).parent().parent().remove();
			});
			
			bindOneClick4EditClass();
		}
	});
}

var getChildDistrict = function(admincode, level) {
	$.ajax({
		url: "district/getChildDistrict",
		data: "admincode=" + encodeURIComponent(admincode),
		success: function(json) {
			refreshSelect(json, level);
		}
	});
}

var getProvince = function() {
	$.ajax({
		url: "district/getProvince",
		success: function(json) {
			refreshSelect(json, 1);
		}
	});
}

var constructArea = function() {
	
}

var importDistrict = function(admincode,name){

	$.ajax({
		url: "district/getDistrict",
		data: "admincode=" + admincode,
		success: function(json) {

			var area = new Array();
			$.each(json.points, function(index, item) {
				area.push(new AMap.LngLat(item.x, item.y));
	        });
			//面填充色随机产生
			var colors = ['red','blue','green'];
			var color = colors[Math.floor(Math.random()*colors.length)];

		    var polygon = new AMap.Polygon({
		        path: area,
		        strokeColor: color,	//线颜色
		        strokeOpacity: 0.3,		//线透明度
		        strokeWeight: 1,		//线宽
		        fillColor: color,		//填充色
		        fillOpacity: 0.3		//填充透明度
		    });
		    polygon.setMap(map);

		    AMapUI.loadUI(['overlay/SimpleMarker'], function(SimpleMarker) {
		        new SimpleMarker({
		            iconLabel: {
		                innerHTML: '<h6>'+name+'</h6>',
		                style: {
		                    color: 'red',
		                    marginTop: '15px',
		                    minWidth: '80px'
		                }
		            },
		            iconStyle: "<>",
		            map: map,
		            position: polygon.getBounds().getCenter()
		        });
		    });
		    singleDistributeArray.push([polygon,name]);
		}
	});
}


$(function() {
	setTimeout("initMap()", 50);
	
	$("#pageBar ul").bootstrapPaginator(pageBarOptions);
	
	$("[data-toggle='popover']").popover();
	
	$("#leftPanel").on("shown.bs.collapse", function () {
		$("#map").height($("#leftPanel").height());
	});
	//绑定的键盘事件
	$("#address").bind("keypress",function(event) {
		if(event.keyCode == "13") {
			singleDistribute($("#address").val());
		}
		return false;
	});
	
	$("#singleDistribute").click(function() {
		singleDistribute($("#address").val());
	});
	
	$("#reset").click(function() {
		reset();
	});
	
	$("#download").click(function() {
		location.href = basePath + "resources/assets/template/template.xlsx";
	});
	
	$("#upload").click(function() {
		if($("#file").val() == "") {
			return;
		}
		
		$("#upload").attr("disabled", "disabled");
		$("#upload").text("上传中...");
		
		$("#form").ajaxSubmit({
			url: "geocoding/upload",
			type: "post",
			dataType: "json",
			success: function(json) {
				if(json.status == 10001) {
					$("#tipWin").modal("show");
					
					$("#upload").removeAttr("disabled");
					$("#upload").text("上传");
				} else {
					$.each(json.result, function(index, item) {

                        for (var i = 0;i<singleDistributeArray.length;i++){
                            if (singleDistributeArray[i][0].contains([item.x, item.y])){
                                var name = singleDistributeArray[i][1];
                                item.area = name;
                                break;//跳出循环
                            }else{
                                item.area = "";
							}
                        }
					});
					
					batchResult = json.result;
					
					$("#upload").removeAttr("disabled");
					$("#upload").text("上传");
					$("#file").val("");
					reset();
					
					refreshTable(1);
					
					pageBarOptions.totalPages = Math.ceil(batchResult.length / pageSize);
					$("#pageBar ul").bootstrapPaginator("setOptions", pageBarOptions);
				}
			}
		});
	});
	
	$("#tipWin button").click(function() {
		$("#tipWin").modal("hide");
	});
	
	///////////////////
	getProvince();
	
	// TODO
	$("#custom-area").click(function() {
		$("#districtWin").modal("show");
	});
	
	$("#close").click(function() {
		$("#districtWin").modal("hide");
	});

    $("#import").click(function() {

    	$("#districtWin").modal("hide");
        $.each($("#area tr"), function(index, item) {
        	var admincode = $(item).attr("admincode");
        	var name = $(item.children[0]).text();
            importDistrict(admincode,name);
        });
    });
})