var pageSize=5;
$(document).ready(function() {
    bootbox.setDefaults("locale","zh_CN");
    regEvent();
    searchData(0);
});
function test1(i){
    var param={
                id:"ff8080815b127958015b129bb411000e",
                avatarId:"asd",
                nickname:"aaa",
                email:"aaa",
                phone:"18708149522",
                password:"123456"
            };
    $.ajax({
        type: "post",
        async: true,
        url: "pUser/update",
        data: param,
        dataType:'json',
        success: function(e){
            if(e && e.isSuccess){
                //refreshData();
            }
        },
        error: function(e){
            console.log("请求出错");
        }
    });
}
//绑定事件
function regEvent(){
    //打开弹框
    $('#add_application').click(function(){
        $(".mask").fadeIn(150);
        $("#alertAdd-Key").fadeIn(200);
        httpRequest({
            url: "api/findAll",
            type:"get",
            success: function(e){
                if(e && e.isSuccess){
                    initCreateBoxApiIds(e.result.data);
                }
            },
            error: function(e){
                console.log("请求出错");
            }
        });
    });
    //关闭弹框
    $("#close,.cancel").click(function(){
        $(".alertAdd-Key").fadeOut(100);
        $(".mask").fadeOut(200);
        clearForm();
    });
    //创建应用框的ip白名单
    $("#create_app_ip,#edit_app_ip").keypress(function(e){
        if(e.keyCode==13){
            //校验ip
            if(checkIp($(this))==false){
                return false;
            }
            var ip=$(this).val();
            var _ips=$(this).next();
            var oldIps=$(_ips).val();
            var newIps=(oldIps==""?ip:(oldIps+","+ip));
            $(_ips).val(newIps);
            $(this).val("");
        }
    })
    //双击ips框，清除所有
    $(".ips").dblclick(function(){
        $(this).val("");
    })
    //保存创建应用
    $("#create_app_save").click(function(){
        var appName=$("#create_app_name").val();
        var appType=$("#create_app_type").find("option:selected").text();
        var apiIds=[];
        var arr=$("#create_app_apiIds .api-title").find("input[type=checkbox]");
        $.each(arr,function(i,o){
            if($(o).prop("checked")==true){
                apiIds.push($(o).attr("data-id"));
            }
        });
        var whiteList=$("#create_app_ips").val();
        //表单验证
        //验证app_name
        if(checkAppName($("#create_app_name"))==false){
            return false;
        }
        //apis必选
        if(apiIds.length<1){
            $("#create_app_apiIds").addClass("red-border");
            return false;
        }else{
            $("#create_app_apiIds").removeClass("red-border");
        }
        //白名单必填
        if(whiteList==""){
            $("#create_app_ips").addClass("red-border");
            return false;
        }else{
            $("#create_app_ips").removeClass("red-border");
        }
        var param={
            pUserId:"ff8080815b127958015b129bb411000e",
            appName:appName,
            appType:appType,
            whiteList:whiteList,
            apiIds:apiIds
        };
        httpRequest({
            url: "application/save",
            type:"post",
            data: param,
            success: function(e){
                if(e && e.isSuccess){
                    refreshData();
                }
            },
            error: function(e){
                console.log("请求出错");
            }
        });
        $(".close").click();
    });
    //保存修改应用
    $("#edit_app_save").click(function(){
        var id=$("#edit_app_name").attr("data-id");
        var apiIds=[];
        var arr=$("#edit_app_apiIds .api-title").find("input[type=checkbox]");
        for(var i=0;i<arr.length;i++){
            if($(arr[i]).prop("checked")==true){
                apiIds.push($(arr[i]).attr("data-id"));
            }
        }
        var whiteList=$("#edit_app_ips").val();
        //表单验证
        //验证app_name
        if(checkAppName($("#edit_app_name"))==false){
            return false;
        }
        //apis必选
        if(apiIds.length<1){
            $("#edit_app_apiIds").addClass("red-border");
            return false;
        }else{
            $("#edit_app_apiIds").removeClass("red-border");
        }
        //白名单必填
        if(whiteList==""){
            $("#edit_app_ips").addClass("red-border");
            return false;
        }else{
            $("#edit_app_ips").removeClass("red-border");
        }
        var param={
            id:id,
            pUserId:"ff8080815b127958015b129bb411000e",
            whiteList:whiteList,
            apiIds:apiIds
        };
        httpRequest({
            url: "application/update",
            type:"post",
            data: param,
            success: function(e){
                if(e && e.isSuccess){
                    refreshData();
                }
            },
            error: function(e){
                console.log("请求出错");
            }
        });
        $(".close").click();
    });
}
//分页查询
function searchData(index){
    //var pageSize=parseInt($("#pageSize").val());
    httpRequest({
        url: "application/query",
        type:"get",
        data: {
            first:index,
            max:pageSize,
            t:new Date().getTime()
        },
        success: function(e){
            if(e && e.isSuccess){
                var res=e.result;
                if(res.total==0){
                    $("#myAppInfo").html("您还没有任何应用，先去新建一个应用吧！");
                }else{
                    $("#myAppInfo").html("我的应用("+res.total+")");
                    //渲染表格
                    renderTable(res.data);
                    //分页
                    genPaginator(res.total);
                }
            }
        },
        error: function(e){
            console.log("请求出错");
        }
    });
}
//按ak查询分页查询
function searchDataByAk(index,ak){
    //var pageSize=parseInt($("#pageSize").val());
    httpRequest({
        url: "application/query",
        type:"get",
        data: {
            first:index,
            max:pageSize,
            searcher:ak,
            t:new Date().getTime()
        },
        success: function(e){
            if(e && e.isSuccess){
                var res=e.result;
                $("#myAppInfo").html("我的应用("+res.total+")");
                //渲染表格
                renderTable(res.data);
                //分页
                genPaginator(res.total);
            }
        },
        error: function(e){
            console.log("请求出错");
        }
    });
}


//编辑应用(一行)，初始化编辑框
function editRow(t){
    var id=$(t).parent().attr("data-id");
    httpRequest({
        url: "application/findById/"+id,
        type:"get",
        success: function(e){
            if(e && e.isSuccess){
                var record=e.result.data;
                $("#edit_app_name").val(record.appName).attr("data-id",id);
                $("#edit_app_type").val(record.appType);
                $("#edit_app_ips").val(record.whiteList);
                //初始化apis
                httpRequest({
                    url: "api/findAll",
                    type:"get",
                    success: function(e){
                        if(e && e.isSuccess){
                            initEditBoxApiIds(record.apis,e.result.data);
                        }
                    },
                    error: function(e){
                        console.log("请求出错");
                    }
                });
            }
        },
        error: function(e){
            console.log("请求出错");
        }
    });
    //显示编辑框
    $(".mask").fadeIn(150);
    $("#alertEdit-Key").fadeIn(100);
}
//删除应用
function deleteConfirm(t){
    bootbox.confirm("是否确定删除？", function (result) {
        if(result) {
                var id=$(t).parent().attr("data-id");
                httpRequest({
                    url: "application/delete",
                    type:"post",
                    data: {
                        id:id
                    },
                    success: function(e){
                        if(e && e.isSuccess){
                            refreshData();
                        }
                    },
                    error: function(e){
                        console.log("请求出错");
                    }
                });                        
            } else {
                //alert('取消');
            }
    });
}
//渲染表格
function renderTable(data){
    var h="";
    $.each(data,function(i,o){
        h+='<tr>';
        h+='<td>'+o.id+'</td>';
        h+='<td>'+o.appName+'</td>';
        h+='<td>'+o.appKey+'</td>';
        h+='<td>'+o.appType+'</td>';
        h+='<td data-id="'+o.id+'""><a href="javascript:void(0);" onclick="editRow(this);">设置</a><a class="delete" href="javascript:void(0);" onclick="deleteConfirm(this);">删除</a></td>';
        h+='</tr>';
    })
    $("#my_apps tbody").html(h);
}
//刷新表格
function refreshData(){
    /*var pageSize=parseInt($("#pageSize").val());
    var page=$("#pagination li.active").text();
    var index=(page-1)*pageSize;*/
    searchData(0);
}
//应用appName校验
function checkAppName(inputObj){
    var reg=/^[a-zA-Z\u4e00-\u9fa5_0-9\-]{1,15}$/;
    if(reg.test($(inputObj).val())==false){
        $(inputObj).addClass("red-border");
        return false;
    }else{
        $(inputObj).removeClass("red-border");
        return true;
    }
}
//ip校验
function checkIp(inputObj){
    var reg=/^[0-9.\/]+$/;
    if(reg.test($(inputObj).val())==false){
        $(inputObj).addClass("red-border");
        return false;
    }else{
        $(inputObj).removeClass("red-border");
        return true;
    }
}
//初始化创建框中的apiIds
function initCreateBoxApiIds(allApiIds){
    var h='<ul>';
    $.each(allApiIds,function(i,o){
        h+='<li class="api-li">'+
                '<div class="api-title">'+
                    '<label><input type="checkbox" data-id="'+o.id+'">'+o.apiName+'</label>'+
                '</div>'+
                /*'<div class="api-content" style="display:none;">'+
                    '<label class="check-api">接口1</label>'+
                    '<label class="check-api">接口2</label>'+
                    '<label class="check-api">接口3</label>'+
                '</div>'+*/
            '</li>';
    })
    h+="</ul>";
    $("#create_app_apiIds").html(h);
    //绑定事件
    $(".api-title label").click(function(e){
        event.stopPropagation();
        var input=$(this).find("input");
        if($(input).prop("checked")==true){
            $(this).parent().next().slideDown();
        }else{
            $(this).parent().next().slideUp();
        }
    });
}
//初始化编辑框中的apiIds
function initEditBoxApiIds(selectedApis,allApis){
    var h='<ul>';
    var apiIds=[];
    $.each(selectedApis,function(i,o){
        apiIds.push(o.id);
    });
    $.each(allApis,function(i,o){
        h+='<li class="api-li">'+
                '<div class="api-title">'+
                    '<label class="check-api"><input type="checkbox" data-id="'+o.id+'" '+(apiIds.indexOf(o.id)==-1?"":"checked='checked'")+'">'+o.apiName+'</label>'+
                '</div>'+
                /*'<div class="api-content" '+(apiIds.indexOf(o.id)==-1?'style="display:none;"':"")+'>'+
                    '<label class="check-api">接口1</label>'+
                    '<label class="check-api">接口2</label>'+
                    '<label class="check-api">接口3</label>'+
                '</div>'+*/
            '</li>';
    });
    h+="</ul>";
    $("#edit_app_apiIds").html(h);
    //绑定事件
    $(".api-title label").click(function(e){
        event.stopPropagation();
        var input=$(this).find("input");
        if($(input).prop("checked")==true){
            $(this).parent().next().slideDown();
        }else{
            $(this).parent().next().slideUp();
        }
    });
}
//清空表单
function clearForm(){
    $(".alertAdd-Key").find("input").val("");
    $(".alertAdd-Key").find("textarea").val("");
    //$("#create_app_name").attr("false");
}
//分页
var genPaginator = function(total) {
    //var pageSize=parseInt($("#pageSize").val());
    if(total<=pageSize){
        $("#DataTables_Table_0_info").hide();
        $("#DataTables_Table_0_paginate").hide();
        return false;
    }
    $("#pageCount").text(Math.ceil(total/pageSize));
    $("#totalCount").text(total);
    $("#DataTables_Table_0_info").show();
    $("#DataTables_Table_0_paginate").show();
    $("#pagination").bootstrapPaginator({
        bootstrapMajorVersion: 3,
        size: "small",
        totalPages: Math.ceil(total/pageSize),
        numberOfPages: 4,
        tooltipTitles: function(type, page, current) {
            switch (type) {
                case "first":
                return "首页";
                case "prev":
                return "上一页";
                case "next":
                return "下一页";
                case "last":
                return "末页";
                case "page":
                return "第" + page + "页";
            }
        },
        onPageClicked: function(event, originalEvent, type, page) {
            var index=(page-1)*pageSize;
            searchData(index);
        }
    });
}