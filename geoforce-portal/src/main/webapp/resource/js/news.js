/**
 * Created by zr on 2018/1/25.
 */
$(function(){
    var size;
    var first = 0;
    var page = 1;
    var newsList = function(first,size){
        $.ajax({
            url:BASE_PATH + "announce/query?first="+first+"&size=",
            async:false,
            success:function(json){
                var data = json.result.data;
                var html = "";
                $.each(data,function(index,item){
                    // .replace(/<[^>]*>/g,"").substring(2,0)
                    console.log(item.content);
                    // var content = item.content.replace(/<[^]*>/g,"");
                    var content = item.content.replace(/<\/?.+?>/g, "");
                    // item.content = item.content.replace(/<\/?(img|a)[^>]*>/gi, "");
                    console.log(content);
                    html += '<li class="news-child">'+
                            '<h4><a href="news_detail.html?id='+item.id+'">'+item.title+'</a></h4>'+
                            '<h6>发布时间：'+item.createTime+'</h6>';
                    if(content.length <= 200){
                        html += '<p>'+content+'</p>';
                    }else{
                        html += '<p>'+content.substring(200,0)+'...</p>';
                    }
                    html += '<a href="news_detail.html?id='+item.id+'" class="news_detail">查看详情</a>'+
                            '</li>';
                });
                $(".news").html(html);

                var totalPages = Math.ceil(json.result.total / 10);
                pages(page,totalPages);
            },
            complete:function(){
                $("body").show();
            }
        });
    };
    var pages = function(page,totalPages){
        var options = {
            bootstrapMajorVersion: 3,//bootstrap版本
            currentPage: page, //当前页数
            totalPages: totalPages, //总页数
            itemTexts: function (type, page, current) {
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
                        return page;
                }
            },
            //点击事件，用于通过Ajax来刷新整个list列表
            onPageClicked: function (event, originalEvent, type, page) {
                first = (page-1)*10;
                newsList(first,size);
            }
        };
        $('#page').bootstrapPaginator(options);
    };

    $(".has_child span").click(function(){
        if($(this).parent().hasClass("show")){
            $(this).parent().removeClass("show");
        }else{
            $(this).parent().addClass("show");
        }
    });
    newsList(0,size);
});

