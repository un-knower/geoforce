/**
 * Created by zr on 2018/1/8.
 */
// $("body").hide();
$(function(){

    var getMenu = function(hrefID){
        $.ajax({
            url:"groupSet/findAll",
            async:false,
            success:function(json){
                var data = json.result.data;
                // console.log(data);
                var html = "";
                html += '<li class="">'+
                    '<a href="javascript:void(0);">概述</a>'+
                    '</li>'+
                    '<li class="">'+
                    '<a href="javascript:void(0);">获取KEY</a>'+
                    '</li>'+
                    '<li class="has_child">'+
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
                        html += '<a id="'+itemSon.id+'" href="product.html?id='+itemSon.id+'" class="">'+itemSon.apiName+'</a>';
                        // html += '<a href="product.html?id='+itemSon.id+'" class="">'+itemSon.apiName+'</a>';
                        html += '</li>';
                    });
                    html += '</ul>'+
                        '</li>';
                });
                html += '</ul>'+
                    '</li>'+
                    '<li class="">'+
                    '<a href="javascript:void(0);">使用限制</a>'+
                    '</li>';
                $("#menu-grandpa").append(html);

                //如果没有传参默认显示第一个
                if(hrefID == undefined){
                    $(".menu-grandpa .menu-father .menu-son li").eq(0).addClass("active");
                }

                //左侧菜单的选中状态
                var _this = $(".menu-son li.active");
                var apiID = _this.find("a").attr("id");
                $(_this).parents(".menu-son").parent().addClass("active").addClass("show");
                $(_this).parents(".menu-father").parent().addClass("active").addClass("show");
                getContent(apiID);
                changeLocation();
            }
        });
    };
    var getContent = function(apiID){
        $.ajax({
            url:"api/findOne/" + apiID,
            async:false,
            success:function(json){
                // console.log(json.result);
                if(json.result != null){
                    var data = json.result.data;
                    // console.log(data);
                    var html = "";
                    //当前位置
                    html += '<p class="location">当前位置：'+
                        '<a>产品</a> / '+
                        '<a href="javascript:void(0);" class="level-1"></a> / '+
                        '<a href="javascript:void(0);" class="level-2"></a> / '+
                        '<a class="active level-3"></a>'+
                        '</p>';

                    //产品名称和更新时间
                    html += '<h1 id="title" class="title">'+data.apiName+'</h1>';
                    var updateDate = data.updateDate.split(" ")[0].split("-");
                    var dateTip = "最后更新时间为" + updateDate[0] + "年" + parseInt(updateDate[1]) + "月" + parseInt(updateDate[2]) + "日";
                    html += '<h6 class="refresh-time">'+dateTip+'</h6>';

                    //产品介绍
                    html += '<h3 id="proIntroduce">产品介绍</h3>'+
                        '<p class="pro-intro">'+data.description+'</p>';

                    //产品特色
                    if(data.lightspot){
                        html += '<h3 id="lightspot">产品特色</h3>';
                        var lightspot = data.lightspot.toString().split(";");
                        html += '<ul class="lightspot">';
                        $.each(lightspot,function(ind,itm){
                            html += '<li><span>'+itm+'</span></li>';
                        });
                        html += '</ul>';
                        $(".nav").html('<li class="active"><a href="#proIntroduce">产品介绍</a></li>' +
                            '<li><a href="#lightspot">产品特色</a></li>' +
                            '<li><a href="#featureDemo">功能示例</a></li>' +
                            '<li><a href="#useMethod">使用方法</a></li>' +
                            '<li><a href="#updateLog">更新日志</a></li>');
                    }else if(!data.lightspot){
                        $(".nav").html('<li class="active"><a href="#proIntroduce">产品介绍</a></li>' +
                            '<li><a href="#featureDemo">功能示例</a></li>' +
                            '<li><a href="#useMethod">使用方法</a></li>' +
                            '<li><a href="#updateLog">更新日志</a></li>');
                    }
                    //功能示例
                    html += '<h3 id="featureDemo">功能示例</h3>'+
                        '<p>以下是示例的效果和代码：</p>'+
                        '<iframe id="demoResult" class="demoResult" scrolling="no"></iframe>'+
                        '<div class="demoCode">'+
                        '<textarea id="demoCode">'+data.requestDemo+'</textarea>'+
                        '</div>';

                    //使用方法
                    html += '<h3 id="useMethod">使用方法</h3>'+
                        '<p class="pro-intro">成为Geoforce开发者的步骤：</p>'+
                        '<p class="ak-step">1. 成为Geoforce用户</p>'+
                        '<p class="ak-step">2. 注册Geoforce开发者</p>'+
                        '<p class="ak-step">3. 去控制台创建应用</p>'+
                        '<p class="ak-step">4. 获取KEY</p>'+
                        '<ul class="AK-step">'+
                        '<li><i></i><span>1</span><br>成为Geoforce用户</li>'+
                        '<li><i></i><span>2</span><br>注册Geoforce开发者</li>'+
                        '<li><i></i><span>3</span><br>去控制台创建应用</li>'+
                        '<li><span>4</span><br>获取KEY</li>'+
                        '<div style="clear: both"></div>'+
                        '</ul>';
                    //更新日志
                    if(data.years){
                        html += '<h3 id="updateLog">更新日志</h3>'+
                            '<div class="update-log">';
                        $.each(data.years,function(index,item){
                            if(index == 0){
                                html += '<div class="year active">';
                            }else{
                                html += '<div class="year">';
                            }
                            html += '<h2>'+
                                    '<a href="javascript:void(0);">'+item.year+'<img src="resource/img/time-triangle.png" alt=""> </a>'+
                                    '</h2>'+
                                    '<div class="list">'+
                                    '<ul>';
                            $.each(item.logs,function(logsIndex,logsItem){
                                var logDate = logsItem.createTime.split(" ")[0].split("-");
                                logDate[1] = parseInt(logDate[1]);
                                logDate[2] = parseInt(logDate[2]);
                                logDate = logDate.splice(1,2).join(".");
                                html += '<li class="cls">'+
                                        '<p class="date">'+logDate+'</p>'+
                                        '<div class="more">'+logsItem.newContent+'</div>'+
                                        '<p class="version">'+logsItem.version+'</p>'+
                                        '</li>';
                            });
                            html += '</ul>'+
                                    '</div>'+
                                    '</div>';
                        });
                        html += '</div>';
                    }
                    $(".centerBar").html(html);
                    $("body").show();
                    demoShow();
                }else{
                    console.log("请求出错。");
                }
            }
        });
    };

    //位置变化
    var changeLocation = function(){
        $(".location").find(".level-1").html($(".menu-grandpa li.active").find("span").html().replace(/<[^>]*>/g,""));
        $(".location").find(".level-2").html($(".menu-father li.active").find("span").html().replace(/<[^>]*>/g,""));
        $(".location").find(".level-3").html($(".menu-son li.active").find("a").html());

        $(".location .level-2").attr("href",'product.html?id='+$(".menu-father>li.active li").eq(0).find("a").attr("id"));

        $(".location .level-1").attr("href",'product.html?id='+$(".menu-grandpa>li.active").find(".menu-father .menu-son li").eq(0).find("a").attr("id"));
    };

    var demoShow = function(){
        //demo展示
        var mixedMode = {
            name: "htmlmixed",
            scriptTypes: [{matches: /\/x-handlebars-template|\/x-mustache/i,
                mode: null},
                {matches: /(text|application)\/(x-)?vb(a|script)/i,
                    mode: "vbscript"}]
        };
        var editor = CodeMirror.fromTextArea(document.getElementById("demoCode"), {
            mode: mixedMode,
            selectionPointer: true,
            readOnly:true
        });
        var o = document.getElementById("demoResult");
        var ed = document.all ? o.contentWindow.document : o.contentDocument;
        ed.open();
        ed.write(editor.getValue());
        ed.close();
        ed.contentEditable = true;
        ed.designMode = 'on';
    };

    //从静态页面跳转的显示
    var hrefID = UrlParm.parm("id");
    getMenu(hrefID);//显示菜单

    //左侧菜单折叠面板
    menuStatus();

    /**三级菜单**/
    $(".menu-son>li>a").click(function(){
        var apiID = UrlParm.parm("id");
        // var apiID = $(this).attr("id");

        getContent(apiID);
        // getMenu(apiID);
        //先移除所有三级选中
        $(this).parents(".menu-grandpa")
            .find(".menu-father").find(".menu-son")
            .find("li").removeClass("active");
        //给当前添加
        $(this).parent().addClass("active");
        $(this).parents(".menu-son").parent().addClass("active").siblings().removeClass("active");
        $(this).parents(".menu-father").parent().addClass("active").siblings().removeClass("active");

        //改变location的显示
        // console.log($(this).html());
        //".html().replace(/<[^>]*>/g,"")":去掉文本中的HTML代码
        changeLocation();
    });


    //更新日志
    $(".year>h2>a").click(function() {
        $(this).parent().parent().addClass("active")
            .siblings().removeClass("active");
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

});

