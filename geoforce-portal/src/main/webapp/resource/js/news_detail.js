/**
 * Created by zr on 2018/1/29.
 */
$(function(){

    var id = UrlParm.parm("id");
    $.ajax({
        url:BASE_PATH + "announce/findById/" + id,
        // url:BASE_PATH + "announce/findById/",
        success:function(json){
            var data = json.result.data;
            // console.log(data);
            var html = '';
            $("#title").html(data.title);

            html = '<h3>'+data.title+'</h3>'+
                    '<h6>发布时间：'+data.createTime+'</h6>'+
                    '<p>'+data.content+'</p>';
            $(".news-content").html(html);
        },
        complete:function(){
            $("body").show();
        },
        error:function(){
            alert("请求出错啦~~请刷新~~");
        }
    });

    $(".has_child span").click(function(){
        if($(this).parent().hasClass("show")){
            $(this).parent().removeClass("show");
        }else{
            $(this).parent().addClass("show");
        }
    });
});