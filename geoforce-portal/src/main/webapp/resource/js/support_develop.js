/**
 * Created by zr on 2018/1/5.
 */

$(function(){
    var demoDataRequest;
    var getMenu = function(hrefID){
        $.ajax({
            url:BASE_PATH + "groupSet/findAll",
            async:false,
            success:function(json){
                var data = json.result.data;
                var html = "";
                html += '<li class="has_child">'+
                    '<span>API'+
                    '<i class="glyphicon glyphicon-menu-down"></i>'+
                    '</span>'+
                    '<ul class="menu-father">';
                $.each(data,function(index,item){
                    html += '<li class="has_child">'+
                        '<span>'+item.groupName+
                        '<i class="glyphicon glyphicon-menu-down"></i>'+
                        '</span>'+
                        '<ul class="menu-son">';
                    $.each(item.apis,function(indexSon,itemSon){
                        if(hrefID == undefined){
                            html += '<li class="">';
                        }else{
                            if(itemSon.id == hrefID){
                                html += '<li class="active">'
                            }else{
                                html += '<li class="">';
                            }
                        }
                        // html += '<a id="'+itemSon.id+'" href="javascript:void(0);" class="">'+itemSon.apiName+'</a>';
                        html += '<a id="'+itemSon.id+'" href="support_develop.html?id='+itemSon.id+'" class="">'+itemSon.apiName+'</a>';
                        html += '</li>';
                    });
                    html += '</ul>'+
                        '</li>';
                });
                html += '</ul>'+
                    '</li>'+
                    '<li class="">'+
                    '<a href="javascript:void(0);">全局错误码</a>'+
                    '</li>';
                $("#menu-grandpa").html(html);

                //如果没有传参默认显示第一个
                if(hrefID == undefined){
                    $(".menu-grandpa .menu-father .menu-son li").eq(0).addClass("active");
                }

                //左侧菜单的选中状态
                var _this = $(".menu-son li.active");
                var apiID = _this.find("a").attr("id");
                $(_this).parents(".menu-son").parent().addClass("active").addClass("show");
                $(_this).parents(".menu-father").parent().addClass("active").addClass("show");
                changeLocation();
                getContent(apiID);
            }
        });
    };


    var getContent = function(apiID){
        $.ajax({
            url:BASE_PATH + "api/findOne/" + apiID,
            async:false,
            success:function(json){
                if(json){
                    var data = json.result.data;
                    // console.log(data);
                    //递归获得下一层级
                    //请求参数的子集
                    var getRequestParametersChild = function(col,paraHtml,requestParameters){
                        $.each(requestParameters,function(index,item){
                            paraHtml = item;
                            paraCols[paraC++] = col;
                            paraHtmls[paraH++] = paraHtml;
                            if (item.parameters.length != 0) {
                                getRequestParametersChild(col+1,paraHtml,item.parameters);
                            }else{
                                if(paraMax<col){
                                    paraMax = col;
                                }
                            }
                        });
                        return paraHtml;
                    };
                    //返回结果的子集
                    var getReturnResultsChild = function(col,html,returnResults){
                        $.each(returnResults,function(index,item){
                            html = item;
                            cols[returnC++] = col;
                            htmls[returnH++] = html;
                            if (item.returnResults.length != 0) {
                                getReturnResultsChild(col+1,html,item.returnResults);
                            }else{
                                if(max<col){
                                    max = col;
                                }
                            }
                        });
                        return html;
                    };
                    // console.log(data);
                    $(".title").html(data.apiName);

                    var updateDate = data.updateDate.split(" ")[0].split("-");
                    var dateTip = "最后更新时间为" + updateDate[0] + "年" + parseInt(updateDate[1]) + "月" + parseInt(updateDate[2]) + "日";
                    $(".refresh-time").html(dateTip);
                    demoDataRequest = data.requestPath;
                    $("#address").next().html("http://restapi.dituhui.com/v1"+data.requestPath);

                    //请求参数
                    var paraCols=[];
                    var paraHtmls=[];
                    var paraC=0;
                    var paraH=0;
                    var paraMax=0;
                    var requestHtml = "";
                    var paraHtml = [];

                    requestHtml = '<thead>'+
                        '<tr class="active">'+
                        '<td width="160" id="paraTh">参数</td>'+
                        '<td width="160">含义</td>'+
                        '<td width="80">是否必填</td>'+
                        '<td width="120">默认值</td>'+
                        '<td width="280">备注</td>'+
                        '</tr>'+
                        '</thead>';
                    $.each(data.requestParameters,function(index,item){
                        paraHtml = item;
                        paraCols[paraC++] = 0;
                        paraHtmls[paraH++] = paraHtml;
                        // console.log(item);
                        if (item.parameters.length != 0) {
                            getRequestParametersChild(1,paraHtml,item.parameters);
                        }
                    });
                    for(var a=0,b=0;a<paraCols.length;a++,b++){
                        var paraph="";
                        if(paraCols[a] != paraMax){
                            for(var c=0;c<paraCols[a];c++){
                                paraph+="<td width='10'></td>";
                            }
                            paraph+="<td colspan='"+(paraMax-paraCols[a]+1)+"'>"+paraHtmls[a].apiParameterName+"</td>";
                            paraph+="<td>"+paraHtmls[a].meaning+"</td>";
                            if(paraHtmls[a].required == "REQUIRED"){
                                paraph+="<td>必填</td>";
                                paraph+="<td>无</td>";
                            }else if(paraHtmls[a].required == "OPTIONAL"){
                                paraph+="<td>选填</td>";
                                paraph+="<td>"+paraHtmls[a].defaultValue+"</td>";
                            }
                            paraph+="<td>"+paraHtmls[a].description+"</td>";
                        }else{
                            for(var c=0;c<paraCols[a];c++){
                                paraph+="<td width='10'></td>";
                            }
                            paraph+="<td>"+paraHtmls[a].apiParameterName+"</td>";
                            paraph+="<td>"+paraHtmls[a].meaning+"</td>";
                            if(paraHtmls[a].required == "REQUIRED"){
                                paraph+="<td>必填</td>";
                                paraph+="<td>无</td>";
                            }else if(paraHtmls[a].required == "OPTIONAL"){
                                paraph+="<td>选填</td>";
                                paraph+="<td>"+paraHtmls[a].defaultValue+"</td>";
                            }
                            paraph+="<td>"+paraHtmls[a].description+"</td>";
                        }
                        requestHtml = requestHtml +"<tr>"+paraph+"</tr>";
                    }
                    $("#requestParam").next().html(requestHtml);
                    $("#paraTh").attr("colspan",paraMax+1);

                    //返回结果
                    var cols=[];
                    var htmls=[];
                    var returnC=0;
                    var returnH=0;
                    var max=0;
                    var returnHtml = "";
                    var html = [];

                    returnHtml = '<thead>'+
                        '<tr class="active">'+
                        '<td width="200" id="returnTh">名称</td>'+
                        '<td width="120">类型</td>'+
                        '<td width="200">含义</td>'+
                        '<td width="280">备注</td>'+
                        '</tr>'+
                        '</thead>';
                    $.each(data.returnResults,function(index,item){
                        html = item;
                        cols[returnC++] = 0;
                        htmls[returnH++] = html;
                        if (item.returnResults.length != 0) {
                            getReturnResultsChild(1,html,item.returnResults);
                        }
                    });
                    for(var i=0,j=0;i<cols.length;i++,j++){
                        var ph="";
                        if(cols[i]!=max){
                            for(var g=0;g<cols[i];g++){
                                ph+="<td width='10'></td>";
                            }
                            ph+="<td colspan='"+(max-cols[i]+1)+"'>"+htmls[i].name+"</td>";
                            ph+="<td>"+htmls[i].type+"</td>";
                            ph+="<td>"+htmls[i].meaning+"</td>";
                            ph+="<td>"+htmls[i].description+"</td>";
                        }else{
                            for(var g=0;g<cols[i];g++){
                                ph+="<td width='10'></td>";
                            }
                            ph+="<td>"+htmls[i].name+"</td>";
                            ph+="<td>"+htmls[i].type+"</td>";
                            ph+="<td>"+htmls[i].meaning+"</td>";
                            ph+="<td>"+htmls[i].description+"</td>";
                        }
                        returnHtml = returnHtml +"<tr>"+ph+"</tr>";
                    }
                    $("#returnResults").next().html(returnHtml);
                    $("#returnTh").attr("colspan",max+1);

                    //运行示例
                    $(".demoReqUrl").html("http://restapi.dituhui.com/v1"+data.requestPath+"?ak=您的ak");
                    var serviceDemoReqHtml = "";
                    // console.log(data.requestParameters);
                    serviceDemoReqHtml += '<thead>'+
                                            '<tr class="active">'+
                                            '<td width="25%">参数</td>'+
                                            '<td width="25%">值</td>'+
                                            '<td width="35%">备注</td>'+
                                            '<td width="15%">是否必填</td>'+
                                            '</tr>'+
                                            '</thead>'+
                                            '<tbody id="demoTr">';
                    $.each(data.requestParameters,function(index,item){
                        serviceDemoReqHtml += '<tr>'+
                                                '<td>'+item.apiParameterName+'</td>'+
                                                '<td>';
                                                if(item.apiParameterName == "addresses"){
                                                    // console.log(decodeURIComponent(item.defaultValue));
                                                    serviceDemoReqHtml += "<input class='form-control' value='"+decodeURIComponent(item.defaultValue)+"'/>";
                                                }else{
                                                    serviceDemoReqHtml += '<input class="form-control" value="'+item.defaultValue+'"/>';
                                                }

                        serviceDemoReqHtml +=  '</td>'+
                                               '<td>'+item.description+'</td>';
                        if(item.required == "REQUIRED"){
                            serviceDemoReqHtml += '<td>是</td>';
                        }else if(item.required == "OPTIONAL"){
                            serviceDemoReqHtml += '<td>否</td>';
                        }
                        serviceDemoReqHtml += '</tr>';
                    });
                    serviceDemoReqHtml += '</tbody>';
                    $(".demoReqUrl").next().html(serviceDemoReqHtml);
                    //运行结果
                    $("#return-result").css({
                       display:"none"
                    });
                    // 错误代码说明
                    var errorCodeHtml = "";
                    errorCodeHtml += '<table class="table table-bordered">'+
                        '<thead>'+
                        '<tr class="active">'+
                        '<td width="25%">status</td>'+
                        '<td width="25%">info</td>'+
                        '<td width="25%">描述</td>'+
                        '<td width="25%">排查</td>'+
                        '</tr>'+
                        '</thead>'+
                        '<tbody>';
                    $.each(data.errorCodeSamples,function(index,item){
                        errorCodeHtml += '<tr>'+
                            '<td>'+item.code+'</td>'+
                            '<td>'+item.description+'</td>'+
                            '<td>'+item.exclueStrategy+'</td>'+
                            '<td>'+item.exclueStrategy+'</td>'+
                            '</tr>';
                    });
                    errorCodeHtml += '</tbody>'+
                        '</table>';
                    $("#errorCode").next().html(errorCodeHtml);
                }else{
                    console.log("请求出错！");
                }
            },
            complete:function(){
                $("body").show();
            }
        });
    };


    //位置变化
    var changeLocation = function(){
        $(".location").find(".level-1").html($(".menu-grandpa li.active").find("span").html().replace(/<[^>]*>/g,""));
        $(".location").find(".level-2").html($(".menu-father li.active").find("span").html().replace(/<[^>]*>/g,""));
        $(".location").find(".level-3").html($(".menu-son li.active").find("a").html());
        $(".location .level-2").attr("href",'support_develop.html?id='+$(".menu-father>li.active li").eq(0).find("a").attr("id"));

        $(".location .level-1").attr("href",'support_develop.html?id='+$(".menu-grandpa>li.active").find(".menu-father .menu-son li").eq(0).find("a").attr("id"));

    };

    var hrefID = UrlParm.parm("id");
    getMenu(hrefID);

    //左侧菜单折叠面板
    menuStatus();
    /**三级菜单**/
    $(".menu-son>li>a").click(function(){
        var apiID = UrlParm.parm("id");
        // var apiID = $(this).attr("id");
        // getContent(apiID);
        getMenu(apiID);
        //先移除所有三级选中
        $(this).parents(".menu-grandpa")
            .find(".menu-father").find(".menu-son")
            .find("li").removeClass("active");
        //给当前添加
        $(this).parent().addClass("active");
        $(this).parents(".menu-son").parent().addClass("active").siblings().removeClass("active");
        $(this).parents(".menu-father").parent().addClass("active").siblings().removeClass("active");
        //给当前添加
        changeLocation();
    });


    //右侧侧栏导航滚动监听
    $('#myScroll').on('activate.bs.scrollspy', function () {
        $(window).scroll(function(){
            var scrollTop = $(document).scrollTop();
            if(scrollTop>60){
                $(".rightBar").css({
                    position:"fixed",
                    top:"20px",
                    right:"50%",
                    marginRight:"-600px",
                    float:""
                });
            }
            if(scrollTop<=60){
                $(".rightBar").css({
                    position:"",
                    top:"",
                    right:"",
                    marginRight:"",
                    float:"right"
                });
            }
            if(scrollTop<=130){
                $(".nav li").eq(0).addClass("active");
            }
        });
    });

    //显示运行结果
    $("#run").click(function(){
        // if($("#return-result").children("div")){
        //     $("#return-result").children("div").remove();
        // }
        var url = "http://restapi.dituhui.com/v1"+demoDataRequest+"?ak=4e41c1eefa7946f4bab0b66ebc664d31";
        var demoTr = $("#demoTr tr");
        for(var i = 0; i<demoTr.length;i++){
            var key = $(demoTr[i]).find("td:nth-child(1)").html();
            var value = $(demoTr[i]).find("td:nth-child(2)").find("input").val();
            if(key == "addresses"){
                value = encodeURIComponent(value);
            }
            url += "&"+key+"="+value;
        }
        $("#return-result").slideDown(300);
        $("#clear").css({
            display:"inline-block"
        });
        $.ajax({
            url:url,
            async:false,
            dataType:"jsonp",
            jsonp:"callback",
            success:function(json){
                if(json){
                    json = JSON.stringify(json, null, 4);

                    if($("#return-result").children("div")){
                        $("#return-result").children("div").remove();
                    }
                    //demo展示
                    var editor = CodeMirror.fromTextArea(document.getElementById("demoCode"), {
                        mode:"application/ld+json",
                        selectionPointer: true,
                        readOnly:true,
                        matchBrackets: true,
                        autoCloseBrackets: true
                    });
                    editor.setValue(json);
                    // $("#return-result").slideDown(300);
                    // $("#demoCode").html(json);
                }
            }
        });
        // $("#return-result").slideDown(300);
    });
    //清空结果
    $("#clear").click(function(){
        $("#return-result").slideUp(300);
        $("#demoCode").html("").next().remove();
        $("#clear").css({
            display:"none"
        });
    });
});
