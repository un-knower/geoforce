var Point = {
    data_points: [],
    data_count:0,
    data_param_url:""
};
var Region = {
    data_regions: [],
    data_count:0,
    data_param_url:""
};
var Fendan = {
    data_orders: [],
    data_count:0,
    data_param_url:"",
    data_batchs:[],
    data_batchs_count:0
};
$(window).on('load', function () {
    $(".multiple-init").multiselect({
        includeSelectAllOption: true,
        allSelectedText: '全选',
        selectAllText:'全选',
        maxHeight:254,
        numberDisplayed: 1,
        buttonContainer: '<div class="btn-group" id="example-selectedClass-container"></div>',
        selectedClass: 'multiselect-selected',
        disableIfEmpty: true,
        nonSelectedText:"请选择"
    });
    $('#point_main_account').tooltip();
    $('#fendan_batchs_input').tooltip();
});
//查询主账号
function AutoCompleteMainAccount(selector,keyword,moudle){
    var param={
        info:keyword
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.user.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                if(e.result && e.result.userInfos.length < 1) {
                    showPopover("查询到0条用户数据");
                    return;
                }
                selector.html("");
                var users = e.result.userInfos;
                var len = users.length;
                var html = '';

                for(var i=0; i<len; i++) {
                    var item = users[i];
                    html += '   <li eid="'+item.companyid+'" userid="'+item.id+'">'+ item.username +'</td>';
                }
                selector.html(html).show();  
                selector.find("li").click(function(){
                    var f=$(this).parent().prev();
                    f.val($(this).html());
                    var mainid=$(this).attr("userid");
                    f.attr("main-userid",mainid);
                    f.attr("eid",$(this).attr("eid"));
                    $(this).parent().hide();
                    switch(moudle){
                        case "point":
                            InitSubAccount($("#point_sub_account"),mainid);
                            InitPointGroup($("#point_group"),mainid);
                            break;
                        case "region":
                            InitSubAccount($("#region_sub_account"),mainid);
                            break;
                        case "fendan":
                            InitSubAccount($("#fendan_sub_account"),mainid);
                            break;
                    }                    
                });           
            }
            else {  
                selector.html("").hide();           
                showPopover(e.info);
            }
        }
    });
}
//初始化子账号
function InitSubAccount(selector,userid){
    var param = {
        parentuserid:userid
    };
    $.ajax({
        type: 'GET',
        url: urls.user.querychilduser,
        data: param,
        dataType: 'json',
        success: function(data){
            if(data.success && data.result.length>0){
                var h="";
                $.each(data.result, function(i,o) {
                    h+='<option value="'+o.id+'">'+o.username+'</option>';
                });
                selector.html(h);
                selector.multiselect('rebuild');
            }
        }
    });
}
//初始化网点分组
function InitPointGroup(selector,userid){
    var param = {
        parentuserid:userid
    };
    $.ajax({
        type: 'GET',
        url: urls.point.querypointgroup,
        data: param,
        dataType: 'json',
        success: function(data){
            if(data.success && data.result.length>0){
                var h='<option value="0">无</option>';
                $.each(data.result, function(i,o) {
                    h+='<option value="'+o.id+'">'+o.groupname+'</option>';
                });
                selector.html(h);
                selector.multiselect('rebuild');
            }
        }
    });

}
function getSelectedText(selector){
    var li=selector.find("option:selected");
    var text=''
    $.each(li,function(i,o){
        text+=$(o).text();
        text+=',&nbsp&nbsp';
    });
    return text;
}
/////////////////*网点数据*/////////////////
/*根据总账号更新子账号和网点分组*/
$("#point_main_account").keyup(function(e){
    if(e.keyCode==13){
        var keyword=$(this).val().trim();
        if(keyword==""){
            return false;
        }
        AutoCompleteMainAccount($("#point_main_account").parent().find("ul"),keyword,"point");
    }
}).change(function(){
    //清除子账号和网点分组
    $("#point_main_account").attr("main-userid","");
    $("#point_sub_account").html("");
    $("#point_sub_account").multiselect('rebuild');
    $("#point_group").html("");
    $("#point_group").multiselect('rebuild');
});
Point.init = function(){
    Point.data_count=0;
    Point.data_param_url="";
    $("#point_main_account").val("");
    $("#point_main_account").attr("main-userid","");
    $("#point_sub_account").html("");
    $("#point_sub_account").multiselect('rebuild');
    $("#point_group").html("");
    $("#point_group").multiselect('rebuild');
    $("#table_points").html("");
}
/*查询网点*/
Point.search = function() {
	Point.data_points = [];
    var parentuserid = $("#point_main_account").attr("main-userid");
    if(parentuserid==""){
        alert("填写完总账号之后，请按enter键进行选择！");
        $("#table_points").html("");
        return false;
    }
    var eid=$("#point_main_account").attr("eid");
    var childuserids=$("#point_sub_account").val()==null?[]:$("#point_sub_account").val();
    var groupids=$("#point_group").val()==null?[]:$("#point_group").val();
    var pointstatus=$("#point_status").val();
    var admincode=$(".smcity").attr("admincode");
    var smcity= $('.current-city span.text').html();
    var level=$(".smcity").attr("level");
    var param = {
        parentuserid: parentuserid,
        eid:eid,
        groupids:groupids.join(','),
        childuserids: childuserids.join(','),
        pointstatus:pointstatus,
        admincode:admincode,
        levels:level
    };
    var startTime = $('.txt-searchpoint-starttime').val();
    var endTime = $('.txt-searchpoint-endtime').val();
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
        param.bdate = startTime;
        param.edate = endTime;
    }
    var table = $("#table_points");
    table.html('');
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.point.querypointscount,
        data: param,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                Point.data_count=e.result;
                Point.data_param_url=generateParamURL(param);
                var export_url="http://"+window.location.hostname+":"+window.location.port+urls.point.exportpoints+Point.data_param_url;
                var sub_text=getSelectedText($("#point_sub_account"));
                var group_text=getSelectedText($("#point_group"));

                var per = user.privilegeCodes;
                var per_export = jQuery.inArray("pointmge_export", per);
                var per_delete = jQuery.inArray("pointmge_delete", per);

                var html="";
                html += '<tr>';
                html += '   <td>'+ "1" +'</td>';
                html += '   <td>'+ $("#point_main_account").val() +'</td>';
                html += '   <td>'+ sub_text+'</td>';
                if(startTime!="" && endTime!=""){
                    html += '   <td>'+ startTime+"至"+ endTime +'</td>';
                }else{
                    html += '<td></td>';
                }
                html += '   <td>'+group_text+'</td>';
                html += '   <td>'+ $("#point_status").find("option:selected").text() +'</td>';
                html += '   <td>'+ smcity +'</td>';
                html += '   <td style="color:red;">'+ e.result +'</td>';
                if(Point.data_count==0){
                    html += '<td>无</td>';
                }else{
                    html +='<td>';
                    if( per_export  !== -1) {
                        html += '<a href="'+export_url+'" target="_blank" down-a>下载</a><i></i>';
                    }
                    if( per_delete  !== -1){
                        html += '<a href="javascript:Point.modalpointdelete();">删除</a>';
                    }
                    html +='</td>';
                }
                html += '</tr>';
                table.html(html);
            }
            else {
                showPopover(e.info);
            }
        }
    });
    //alert("搜索网点");
}
/*删除网点确认框*/
Point.modalpointdelete=function() {
	var count = Point.data_count;
    $("#txt_point_delete_id").val( count );
    $("#span_delete_point_vresionname").html("确定删除包含的所有网点(共" + count + "条)吗？删除后不可恢复");
    $('#modal_delete_point').modal("show");
}
/*删除网点*/
Point.remove = function() {
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.point.remove+Point.data_param_url,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                showPopover("删除成功");
                $('#modal_delete_point').modal("hide");  
                Point.search();  
            }
            else {
                showPopover(e.info);
            }
        }
    });
    /*$('#modal_delete_point').modal("hide");
    Point.search();
    alert(urls.point.remove+Point.data_param_url);*/
}
/*end 网点数据*/

/////////////////*区划数据*/////////////////
/*根据总账号更新子账号和网点分组*/
$("#region_main_account").keyup(function(e){
    if(e.keyCode==13){
        var keyword=$(this).val().trim();
        if(keyword==""){
            return false;
        }
        AutoCompleteMainAccount($("#region_main_account").next(),keyword,"region");
    }
}).change(function(){
    $("#region_main_account").attr("main-userid","");
    $("#region_sub_account").html("");
    $("#region_sub_account").multiselect('rebuild');
});
Region.init = function(){
    Region.data_count=0;
    Region.data_param_url="";
    $("#region_main_account").val("");
    $("#region_main_account").attr("main-userid","");
    $("#region_sub_account").html("");
    $("#region_sub_account").multiselect('rebuild');
    $("#table_regions").html("");
}
/*查询区划*/
Region.search = function() {
    Region.data_regions = [];
    var parentuserid = $("#region_main_account").attr("main-userid");
    if(parentuserid==""){
        alert("填写完总账号之后，请按enter键进行选择！");
        $("#table_regions").html("");
        return false;
    }
    var eid=$("#region_main_account").attr("eid");
    var childuserids=$("#region_sub_account").val()==null?[]:$("#region_sub_account").val();
    //var groupids=$("#region_group").val()==null?"":$("#region_group").val();
    var admincode=$(".smcity").attr("admincode");
    var smcity= $('.current-city span.text').html();
    var level=$(".smcity").attr("level");
    var param = {
        parentuserid: parentuserid,
        eid:eid,
        //groupids:groupids,
        childuserids: childuserids.join(','),
        admincode:admincode,
        levels:level
    };
    var startTime = $('.txt-searchregion-starttime').val();
    var endTime = $('.txt-searchregion-endtime').val();
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
        param.bdate = startTime;
        param.edate = endTime;
    }
    var table = $("#table_regions");
    table.html('');
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.region.queryareascount,
        data: param,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                Region.data_count=e.result==null?0:e.result;
                Region.data_param_url=generateParamURL(param);
                var export_url="http://"+window.location.hostname+":"+window.location.port+urls.region.exportareas+Region.data_param_url;
                var sub_text=getSelectedText($("#region_sub_account"));

                var per = user.privilegeCodes;
                var per_export = jQuery.inArray("areamge_export", per);
                var per_delete = jQuery.inArray("areamge_delete", per);

                var html="";
                html += '<tr>';
                html += '   <td>'+ "1" +'</td>';
                html += '   <td>'+ $("#region_main_account").val() +'</td>';
                html += '   <td>'+ sub_text +'</td>';
                if(startTime!="" && endTime!=""){
                    html += '   <td>'+ startTime+"至"+ endTime +'</td>';
                }else{
                    html += '<td></td>';
                }
                //html += '   <td>'+ $("#region_group").text() +'</td>';
                html += '   <td>'+ smcity +'</td>';
                html += '   <td style="color:red;">'+ Region.data_count +'</td>';
                if(Region.data_count==0){
                    html += '<td>无</td>';
                }else{
                    html +='<td>';
                    if( per_export  !== -1) {
                        html += '<a href="'+export_url+'" target="_blank" down-a>下载</a><i></i>';
                    }
                    if( per_delete  !== -1){
                        html += '<a href="javascript:Region.modalpointdelete();">删除</a>';
                    }
                    html +='</td>';
                }
                html += '</tr>';
                table.html(html);
            }
            else {
                showPopover(e.info);
            }
        }
    });
    //alert("搜索网点");
}
/*删除区划确认框*/
Region.modalpointdelete=function() {
    var count = Region.data_count;
    $("#txt_region_delete_id").val( count );
    $("#span_delete_region_vresionname").html("确定删除包含的所有区划(共" + count + "条)吗？删除后不可恢复");
    $('#modal_delete_region').modal("show");
}
/*删除区划*/
Region.remove = function() {
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.region.remove+Region.data_param_url,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                showPopover("删除成功");
                $('#modal_delete_region').modal("hide");  
                Region.search();  
            }
            else {
                showPopover(e.info);
            }
        }
    });
    /*$('#modal_delete_region').modal("hide");
    Region.search();
    alert(urls.region.remove+Region.data_param_url);*/
}
/*end 区划数据*/

/////////////////*分单数据*/////////////////
$("#fendan_batchs_input").keyup(function(e){
    if(e.keyCode==13){
        var keyword=$(this).val().trim();
        /*if(keyword==""){
            return false;
        }*/
        if(keyword.split(',').length>1){
            $(".batchlist").html("").hide();
            alert("不支持对多个批次号进行模糊查询!");
            return false;
        }
        Fendan.queryBatchs(keyword);
    }
});
/*根据总账号更新子账号和网点分组*/
$("#fendan_main_account").keyup(function(e){
    if(e.keyCode==13){
        var keyword=$(this).val().trim();
        if(keyword==""){
            return false;
        }
        AutoCompleteMainAccount($("#fendan_main_account").next(),keyword,"fendan");
    }
}).change(function(){
    $("#fendan_main_account").attr("main-userid","");
    $("#fendan_sub_account").html("");
    $("#fendan_sub_account").multiselect('rebuild');
    $("#fendan_batchs_input").val("");
    $(".batchlist").html("");
    Fendan.data_batchs=[];
    data_batchs_count=0;
});
$(window).click(function(){
    $(".batchlist").hide();
})
Fendan.init = function(){
    Fendan.data_count=0;
    Fendan.data_param_url="";
    Fendan.data_batchs=[];
    data_batchs_count=0;
    $("#fendan_main_account").val("");
    $("#fendan_main_account").attr("main-userid","");
    $("#fendan_sub_account").html("");
    $("#fendan_sub_account").multiselect('rebuild');
    $("#fendan_batchs_input").val("");
    $(".batchlist").html("");
    $("#table_fendans").html("");
    Fendan.queryStatus();
}
$("#drop_caret").click(function(e){
    e.stopPropagation();
    var nextUl=$(this).parent().find("ul");
    if(nextUl.find("li").length>1){
        nextUl.show();
    }
});
$("#fendan_batchs_input").click(function(e){
    e.stopPropagation();
    $(".open").removeClass("open");
    var nextUl=$(this).parent().find("ul");
    if(nextUl.find("li").length>1){
        nextUl.show();
    }
});
Fendan.queryStatus = function(){
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.fendan.querystatus,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                //初始化订单批次
                if(e.result.length>0){
                    var h='<option value="">全部</option>';
                    $.each(e.result, function(i,o) {
                        h+='<option value="'+o.id+'">'+o.value+'</option>';
                    });
                    $("#fendan_status").html(h);
                }
            }
            else {
                showPopover(e.info);
            }
        },
        error:function(e){
            console.log(e);
        }
    });
}
Fendan.queryBatchs = function(keyword){
    var parentuserid = $("#fendan_main_account").attr("main-userid");
    if(parentuserid==""){
        alert("填写完总账号之后，请按enter键进行选择！");
        return false;
    }
    var eid=$("#fendan_main_account").attr("eid");
    var childuserids=$("#fendan_sub_account").val()==null?[]:$("#fendan_sub_account").val();
    //var batchs=$("#fendan_batchs_input").val();
    var param = {
        parentuserid: parentuserid,
        eid:eid,
        batchs:keyword
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.fendan.querybatchs,
        data: param,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                //初始化订单批次
                if(e.result.length>0){
                    Fendan.data_batchs_count=e.result.length;
                    var h='<li><input type="checkbox" id="checkAll" style="margin: 0px 4px !important;">全选</li>';
                    var inputKeywords=keyword.split(',');
                    $.each(e.result, function(i,o) {
                        if(inputKeywords.indexOf(o) !=-1){
                            h+='<li><input type="checkbox" class="check" checked="true">'+ o+'</li>';
                        }else{
                            h+='<li><input type="checkbox" class="check">'+ o+'</li>';    
                        }
                    });
                    /*$("#fendan_batchs_select").html(h);
                    $("#fendan_batchs_select").multiselect('rebuild');*/
                    var ul=$("#fendan_batchs_input").parent().find("ul");
                    ul.html(h).show();
                    ul.click(function(e){
                        e.stopPropagation();
                    });
                    $("#checkAll").click(function(){
                        var others=$("#checkAll").parent().parent().find(".check");
                        Fendan.data_batchs=[];
                        if($(this).prop("checked")){
                            $.each(others,function(i,o){
                                $(o).prop("checked",true);
                                var t=$(this).parent().text();
                                Fendan.data_batchs.push(t);
                            });
                        }else{
                            $.each(others,function(i,o){
                                $(o).prop("checked",false);
                            });
                        }
                        $("#fendan_batchs_input").val(Fendan.data_batchs.join(','));
                    });
                    ul.find(".check").click(function(e){
                        //阻止冒泡
                        e.stopPropagation();
                        var t=$(this).parent().text();
                        if($(this).prop("checked")){
                            //添加
                            Fendan.data_batchs.push(t);
                            if(Fendan.data_batchs.length==Fendan.data_batchs_count){
                                $("#checkAll").prop("checked",true);
                            }
                        }else{
                            //删除
                            Fendan.data_batchs.splice(Fendan.data_batchs.indexOf(t),1);
                            //取消全选
                            $("#checkAll").prop("checked",false);
                        }
                        $("#fendan_batchs_input").val(Fendan.data_batchs.join(','));
                    });

                }else{
                    Fendan.data_batchs_count=0;
                    alert("查询到0条数据！");
                }
            }
            else {
                showPopover(e.info);
            }
        },
        error:function(e){
            console.log(e);
        }
    });
}
//分单查询
Fendan.search = function() {
    Fendan.data_orders = [];
    var parentuserid = $("#fendan_main_account").attr("main-userid");
    if(parentuserid==""){
        alert("填写完总账号之后，请按enter键进行选择！");
        $("#table_fendans").html("");
        return false;
    }
    var eid=$("#fendan_main_account").attr("eid");
    var childuserids=$("#fendan_sub_account").val()==null?[]:$("#fendan_sub_account").val();
    var batchs=$("#fendan_batchs_input").val();
    var fendanstatus=$("#fendan_status").val();
    var admincode=$(".smcity").attr("admincode");
    var smcity= $('.current-city span.text').html();
    var level=$(".smcity").attr("level");
    var param = {
        parentuserid: parentuserid,
        eid:eid,
        batchs:batchs,
        childuserids: childuserids.join(','),
        status:fendanstatus,
        admincode:admincode,
        levels:level
    };
    var table = $("#table_fendans");
    table.html('');
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.fendan.queryorderscount,
        data: param,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                Fendan.data_count=e.result;
                Fendan.data_param_url=generateParamURL(param);
                var export_url="http://"+window.location.hostname+":"+window.location.port+urls.fendan.exportOrders+Fendan.data_param_url;
                var sub_text=getSelectedText($("#fendan_sub_account"));

                var per = user.privilegeCodes;
                var per_export = jQuery.inArray("fendanmge_export", per);
                var per_delete = jQuery.inArray("fendanmge_delete", per);

                var html="";
                html += '<tr>';
                html += '   <td>'+ "1" +'</td>';
                html += '   <td>'+ $("#fendan_main_account").val() +'</td>';
                html += '   <td>'+ sub_text +'</td>';
                html += '   <td>'+ param.batchs +'</td>';
                html += '   <td>'+ $("#fendan_status").find("option:selected").text() +'</td>';
                html += '   <td>'+ smcity +'</td>';
                html += '   <td style="color:red;">'+ e.result +'</td>';
                if(Fendan.data_count==0){
                    html += '<td>无</td>';
                }else{
                    html +='<td>';
                    if( per_export  !== -1) {
                        html += '<a href="'+export_url+'" target="_blank" down-a>下载</a><i></i>';
                    }
                    if( per_delete  !== -1){
                        html += '<a href="javascript:Fendan.modalfendandelete();">删除</a>';
                    }
                    html +='</td>';
                }
                html += '</tr>';
                table.html(html);
            }
            else {
                showPopover(e.info);
            }
        },
        error:function(e){
            console.log(e);
        }
    });
    //alert("搜索网点");
}
/*删除分单确认框*/
Fendan.modalfendandelete=function() {
    var count = Fendan.data_count;
    $("#txt_Fendan_delete_id").val( count );
    $("#span_delete_fendan_vresionname").html("确定删除包含的所有订单(共" + count + "条)吗？删除后不可恢复");
    $('#modal_delete_fendan').modal("show");
}
/*删除分单*/
Fendan.remove = function() {
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.fendan.remove+Fendan.data_param_url,
        dataType: 'json',
        success: function(e){
            if(e && e.success ) {
                showPopover("删除成功");
                $('#modal_delete_fendan').modal("hide");  
                Fendan.search();  
            }
            else {
                showPopover(e.info);
            }
        }
    });
    /*$('#modal_delete_fendan').modal("hide");
    Fendan.search();
    alert(urls.fendan.remove+Fendan.data_param_url);*/
}
/*end 分单数据*/