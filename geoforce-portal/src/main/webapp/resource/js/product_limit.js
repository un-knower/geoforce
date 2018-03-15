/**
 * Created by zr on 2018/1/29.
 */
$(function(){
    var BASE_PATH = "http://192.168.10.252:8082/portal/";
    $.ajax({
        url:BASE_PATH + "groupSet/findAll",
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
                    html += '<li class="">';
                    html += '<a id="'+itemSon.id+'" href="product.html?id='+itemSon.id+'" class="">'+itemSon.apiName+'</a>';
                    html += '</li>';
                });
                html += '</ul>'+
                        '</li>';
            });
            html += '</ul>'+
                    '</li>'+
                    '<li class="active">'+
                    '<a href="product_limit.html">使用限制</a>'+
                    '</li>';
            $("#menu-grandpa").append(html);
        },
        complete:function(){
            $("body").show();
        }
    });

    menuStatus();
});