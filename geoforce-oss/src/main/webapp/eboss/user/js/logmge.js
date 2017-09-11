var Log = {
    data_logs: []
}
/*添加日志 验证输入*/
Log.verifyAddLogInput=function(){
	if($("#txt_log_onlinetime").val()==""){
		$("#span_tip_log_onlinetime").html("请输入更新时间");
		return false;
	}else if($("#txt_log_versionname").val()==""){
		$("#span_tip_log_versionname").html("请输入版本名称");
		return false;
	}
	$("#span_tip_log_onlinetime").html("");
	$("#span_tip_log_versionname").html("");
	return true;
}
/*修改日志 验证输入*/
Log.verifyUpdateLogInput=function(){
    if($("#txt_log_update_onlinetime").val()==""){
        $("#span_tip_log_update_onlinetime").html("请输入更新时间");
        return false;
    }else if($("#txt_log_update_versionname").val()==""){
        $("#span_tip_log_update_versionname").html("请输入版本名称");
        return false;
    }
    $("#span_tip_log_update_onlinetime").html("");
    $("#span_tip_log_update_versionname").html("");
    return true;
}
/*添加日志*/
Log.add=function(){
	var flag = Log.verifyAddLogInput();	
	if(!flag) {
		return flag;
	}
	var param = {
		onlineTime: $('#txt_log_onlinetime').val(),
		versionName: $('#txt_log_versionname').val(),
		newFunctions: $('#txt_log_newfunctions').val().replace(/\n/g,"<br>"),
		improveFunctions: $('#txt_log_improvefunctions').val().replace(/\n/g,"<br>"),
		repairBugs: $('#txt_log_repairbugs').val().replace(/\n/g,"<br>"),
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.log.add,
        data: param,
        dataType: 'json',
        success: function(e){
        	if(e && e.success) {
        		showPopover("用户添加成功");
        		$('#modal_add_log').modal('hide');
        		//清空查询条件
				$(".txt-searchlog-starttime").val("");
				$(".txt-searchlog-endtime").val("");
				$("#txt_search_log").val("");
				Log.search();
        	}
        	else {        		
        		showPopover(e.info);
        	}
        },
        error: function(e){
            console.log(e);
            showPopover(e);
        }
    });
}
/*查询日志*/
Log.search = function() {
	Log.data_logs = [];
    var pageNo = Number($("#pager_logs").attr("page"));
    var param = {
        pageNo: pageNo+1,
        pageSize: pageSize
    };
    var startTime = $('.txt-searchlog-starttime').val();
    var endTime = $('.txt-searchlog-endtime').val();
    if(startTime != '' || endTime != '') {
        if(startTime == '') {
            showPopover("请选择开始时间");
            return;
        }
        if(endTime == '') {
            showPopover("请选择结束时间");
            return;
        }
        var st = new Date(startTime.replace(/-/g,"/"));
        var et = new Date(endTime.replace(/-/g,"/"));
        if(st > et) {
            showPopover("结束时间须晚于开始时间");
            return;
        }
        param.btime = startTime;
        param.etime = endTime;
    }
    var keyword = $("#txt_search_log").val();
    if(keyword && keyword.length > 0 ) {
        param.versionname = keyword;
    }
    var table = $("#table_logs");
    table.html('');
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.log.search,
        data: param,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                if( !e.result || !e.result.records || e.result.records.length === 0 ) {
                    showPopover("当前查询到0条数据");
                    $("#pager_logs > ul").html("");
                    return;
                }
                var items = e.result.records;
                Log.data_logs = items;
                var len = items.length, html = '';

                var per = user.privilegeCodes;
                var per_update = jQuery.inArray("logmge_update", per);
                var per_remove = jQuery.inArray("logmge_delete", per);

                for (var i=0; i<len; i++) {
                    var item = items[i];
                    html += '<tr>';
                    html += '   <td>'+ (i+1) +'</td>';
                    html += '   <td>'+ item.onlineTime +'</td>';
                    html += '   <td>'+ item.versionName +'</td>';
                    html += '   <td>'+ item.newFunctions +'</td>';
                    html += '   <td>'+ item.improveFunctions +'</td>';
                    html += '   <td>'+ item.repairBugs +'</td>';
                    if( per_update  !== -1) {
                        html += '   <td><a href="javascript:Log.modallogupdate('+ i +');">编辑</a></td>';
                    }
                    if( per_remove  !== -1) {
                        html += '   <td><a href="javascript:Log.modallogdelete('+ i +');">删除</a></td>';
                    }
                    html += '</tr>';
                }
                table.html(html);
                var html_page = setPage(e.result.totalCount, pageNo, '\'pager_logs\''); 
                $("#pager_logs > ul").html(html_page);   
            }
            else {
                showPopover(e.info);
            }
        }
    });
}
//删除后刷新前一页
Log.refresh = function() {
    Log.data_logs = [];
    var pageNo = Number($("#pager_logs").attr("page"));
    var param = {
        pageNo: pageNo==0?1:pageNo,
        pageSize: pageSize
    };
    var table = $("#table_logs");
    table.html('');
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.log.search,
        data: param,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                if( !e.result || !e.result.records || e.result.records.length === 0 ) {
                    showPopover("当前查询到0条数据");
                    $("#pager_logs > ul").html("");
                    return;
                }
                var items = e.result.records;
                Log.data_logs = items;
                var len = items.length, html = '';

                var per = user.privilegeCodes;
                var per_update = jQuery.inArray("logmge_update", per);
                var per_remove = jQuery.inArray("logmge_delete", per);

                for (var i=0; i<len; i++) {
                    var item = items[i];
                    html += '<tr>';
                    html += '   <td>'+ (i+1) +'</td>';
                    html += '   <td>'+ item.onlineTime +'</td>';
                    html += '   <td>'+ item.versionName +'</td>';
                    html += '   <td>'+ item.newFunctions +'</td>';
                    html += '   <td>'+ item.improveFunctions +'</td>';
                    html += '   <td>'+ item.repairBugs +'</td>';
                    if( per_update  !== -1) {
                        html += '   <td><a href="javascript:Log.modallogupdate('+ i +');">编辑</a></td>';
                    }
                    if( per_remove  !== -1) {
                        html += '   <td><a href="javascript:Log.modallogdelete('+ i +');">删除</a></td>';
                    }
                    html += '</tr>';
                }
                table.html(html);
                var pageNoNew = (pageNo-1)==0?1:(pageNo-1);
                var html_page = setPage(e.result.totalCount, pageNoNew, '\'pager_logs\''); 
                $("#pager_logs > ul").html(html_page);   
            }
            else {
                showPopover(e.info);
            }
        }
    });
}
/*显示修改日志模态框*/
Log.modallogupdate = function(index) {
	var id = Log.data_logs[index].id;
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.log.detail,
        data: { logid: id },
        dataType: 'json',
        success: function(e){
            if(e && e.success && e.result) {
            	var r = e.result;
                $("#txt_log_update_id").val(r.id);
            	$("#txt_log_update_onlinetime").val(r.onlineTime);
            	$("#txt_log_update_versionname").val(r.versionName);
            	$("#txt_log_update_newfunctions").val(r.newFunctions.replace(/<br>/g,"\n"));
            	$("#txt_log_update_improvefunctions").val(r.improveFunctions.replace(/<br>/g,"\n"));
            	$("#txt_log_update_repairbugs").val(r.repairBugs.replace(/<br>/g,"\n"));

            	$("#modal_update_log").modal("show");
            }
            else {
                showPopover(e.info);
            }
        }
    });
}
/*修改日志*/
Log.update=function(){
    var flag = Log.verifyUpdateLogInput();    
    if(!flag) {
        return flag;
    }
    var param = {
        logid:$("#txt_log_update_id").val(),
        onlineTime: $('#txt_log_update_onlinetime').val(),
        versionName: $('#txt_log_update_versionname').val(),
        newFunctions: $('#txt_log_update_newfunctions').val().replace(/\n/g,"<br>"),
        improveFunctions: $('#txt_log_update_improvefunctions').val().replace(/\n/g,"<br>"),
        repairBugs: $('#txt_log_update_repairbugs').val().replace(/\n/g,"<br>"),
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.log.update,
        data: param,
        dataType: 'json',
        success: function(e){
            if(e && e.success) {
                showPopover("日志修改成功");
                $('#modal_update_log').modal('hide');
                Log.search();
            }
            else {              
                showPopover(e.info);
            }
        },
        error: function(e){

        }
    });
}
/*删除日志确认框*/
Log.modallogdelete=function(index) {
	var item = Log.data_logs[index];
	$("#txt_log_delete_id").val( item.id );
	$("#span_delete_log_vresionname").html("确定删除日志\"" + item.versionName + "\"吗？删除后不可恢复");

	$('#modal_delete_log').modal("show");
}
/*删除日志*/
Log.remove = function() {
	var param = {
		logid: $("#txt_log_delete_id").val()
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.log.remove,
        data: param,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                showPopover("删除成功");
                $('#modal_delete_log').modal("hide");  
                Log.refresh();  
            }
            else {
                showPopover(e.info);
            }
        }
    });
}
Log.resetLogAddModal=function(){
    $('#txt_log_onlinetime').val("");
    $('#txt_log_versionname').val("");
    $('#txt_log_newfunctions').val("");
    $('#txt_log_improvefunctions').val("");
    $('#txt_log_repairbugs').val("");
}