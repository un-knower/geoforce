/**
 * Created by zr on 2017/12/14.
 */
//渲染页面
$(function(){
    $.ajax({
        url:BASE_PATH + "announce/query?first=0&size=",
        // async:false,
        success:function(json){
            var data = json.result.data;
            var list = "";
            $.each(data,function(index,item){
                if(index<=2){
                    list += '<li>';
                    if(item.property == 1){
                        list += '<a href="news_detail.html?id='+item.id+'" class="isNew">'+item.title+'</a>';
                    }else if(item.property == 2){
                        list += '<a href="news_detail.html?id='+item.id+'" class="isNew">'+item.title+'<img src="resource/img/new.png"></a>';
                    }else if(item.property == 3){
                        list += '<a href="news_detail.html?id='+item.id+'" class="isNew">'+item.title+'<img src="resource/img/news_hot.gif"></a>';
                    }
                    list += '</li>';
                }
            });
            $("#announce").prepend(list);
        }
    });

    $.ajax({
        url:BASE_PATH + "groupSet/findAll",
        // async:false,
        success:function(json){
            var data = json.result.data;
            var html = "";
            $.each(data,function(index,item){
                // console.log(item.apis[0].id);
               if(item.groupName == '沿路寻路' ||
                   item.groupName == '坐标转换' ||
                   item.groupName == '逆地理编码' ||
                   item.groupName == '地理编码' ||
                   item.groupName == '路线导航与测距'){
                    html += '<li class="pro-father">'+
                                '<div class="pro-son">'+
                                    '<div class="img-box">'+
                                    '<img src="'+ BASE_PATH + item.imageUrl +'"/>'+
                                    '<p>'+item.groupName+'</p>'+
                                    '<span>'+item.description+'</span>'+
                                    '</div>'+
                                '</div>'+
                                '<div class="pro-son-h">'+
                                    '<p>'+item.groupName+'</p>'+
                                    '<img src="'+ BASE_PATH + item.imageUrl +'"/>'+
                                    '<a href="product.html?id='+item.apis[0].id+'">查看详情</a>'+
                                '</div>'+
                            '</li>';
               }
            });
            html += '<li class="pro-father">'+
                        '<div class="pro-son">'+
                        '<div class="img-box">'+
                        '<img src="resource/img/come_soon.png"/>'+
                        '<p>敬请期待</p>'+
                        '<span>行政区域、多边形、地址分单、线路规划、POI查询、位置服务、空间运算等API产品 </span>'+
                        '</div>'+
                        '</div>'+
                        '<div class="pro-son-h">'+
                        '<p>敬请期待</p>'+
                        '<img src="resource/img/come_soon.png"/>'+
                        '<a href="product.html">查看详情</a>'+
                        '</div>'+
                    '</li>';
            $("#pro-father").html(html);
        },
        complete:function(){
            $("body").show();
        }
    });

   //产品添加滚动条
   $(".li-child2").scrollBar();
});
$(function(){
    var $header = $(".header");
    $header.css({
       position:"fixed",
       top:"0",
       background:"rgba(0,0,0,0)"
   });
   $header.hover(function(){
       $(this).css({
           background:"rgba(0,0,0,1)"
       })
   },function(){
       $(this).css({
           background:"rgba(0,0,0,0)"
       })
   });
   $(window).scroll(function(){
       var scrollTop = $(document).scrollTop();
       if (scrollTop>60){
           $header.css({
               background:"rgba(0,0,0,1)"
           });

           $header.hover(function(){
               $(this).css({
                   background:"rgba(0,0,0,1)"
               })
           },function(){
               $(this).css({
                   background:"rgba(0,0,0,1)"
               })
           });
       }else if(scrollTop<=60){
           $header.css({
               background:"rgba(0,0,0,0)"
           });
           $header.hover(function(){
               $(this).css({
                   background:"rgba(0,0,0,1)"
               })
           },function(){
               $(this).css({
                   background:"rgba(0,0,0,0)"
               })
           });
       }
   });
});
//banner滚动
$(function(){

    var timer = null;
    var slideDot = $(".slide_link li");
    var slideBox = $("#banner_slides .slide");
    var loop = 0;//当前索引值

    $(".slide").css({
        width:window.innerWidth || document.documentElement.clientWidth
    });
    $(window).resize(function(){
        $(".slide").css({
            width:$(window).width()
        });
    });
   $("#banner_slides").slides({
       preload: true
   });

   slideDot.on('click',function(){
      $(this).addClass("active").siblings().removeClass("active");
      //当前的value值
      var indexValue = $(this).index();
      $(".banner_slides .pagination a").eq(indexValue).click();
   });

    //自动播放
    function autoPlay(loop){
        timer = setInterval(function(){
            loop++;
            if(loop > slideDot.length-1){
                loop = 0;
            }
            slideDot.eq(loop).trigger('click');
        },5000);
    }
    autoPlay(loop);
    //移入点时清除定时器，移出开启定时器
    slideDot.hover(function () {
        clearInterval(timer);
    },function(){
        autoPlay($(this).index());
    });
    //移入盒子时清除定时器，移出开启定时器
    slideBox.hover(function () {
        clearInterval(timer);
        $(".banner_change").show();
        $(".banner_change").hover(function(){
            $(".banner_change").show();
        },function(){
            $(".banner_change").show();
        });
    },function(){
        $(".banner_change").hide();
        autoPlay($(this).index());
    });
    $(".banner_prev").hover(function(){
        clearInterval(timer);
    },function(){
        autoPlay($(this).index());
    }).click(function(){
        if(loop == 0){
            loop = slideDot.length;
        }
        loop--;
        slideDot.eq(loop).click();
    });
    $(".banner_next").hover(function(){
        clearInterval(timer);
    },function(){
        autoPlay($(this).index());
    }).click(function(){
        loop++;
        if(loop == slideDot.length){
            loop = 0;
        }
        slideDot.eq(loop).click();
    });
});
//解决方案
$(function() {
    $('#slides').slides({
        pause: 2500,
        slideSpeed: 800,
        hoverPause: true
    });

    $(".industry>li>img").click(function () {
        var index = $(this).parent().index();
        var eq= $(".slide-1").eq(index);
        $(".slide-1").eq(index).addClass("active1");
        $(".slide-1").not(eq).removeClass("active1");
        $(".solution .pagination a").eq(index).click();

        $(this).parent().addClass("active")
            .siblings().removeClass("active").find("img").attr({
            src: "resource/img/sol-dot.png"
        });
        changeSrc();
    });
    $(".industry>li>a").click(function () {
        var index = $(this).parent().index();
        var eq= $(".slide-1").eq(index);
        $(".slide-1").eq(index).addClass("active1");
        $(".slide-1").not(eq).removeClass("active1");
        $(".solution .pagination a").eq(index).click();

        $(this).parent().addClass("active")
            .siblings().removeClass("active").find("img").attr({
            src: "resource/img/sol-dot.png"
        });
        changeSrc();
    });

    function changeSrc() {
        $(".industry .active").find("img").attr({
            src: "resource/img/" + $(".industry .active").find("img").attr("name") + ".png"
        });
    }
});

//解决方案的上一页和下一页
$(function(){
    //前一张
    $(".prev1").on('click', function () {
        if($(".slide-1.active1").children().length!=2){
            $(".slide-1.active1").animate({left:'-1000px' }, 800,function(){
                var left = $(".slide-1.active1").children(":first");
                $(".slide-1.active1").children(":first").remove();
                var right = $(".slide-1.active1").children(":first");
                $(".slide-1.active1").children(":first").remove();

                $(".slide-1.active1").children(":last").after(left);
                $(".slide-1.active1").children(":last").after(right);

                $(".slide-1.active1").animate({left:'0px' }, 0);
            });
        }
    });
    //后一张
    $(".next1").on('click', function () {
        if($(".slide-1.active1").children().length!=2){
            var left = $(".slide-1.active1").children(":last");
            $(".slide-1.active1").children(":last").remove();
            $(".slide-1.active1").children(":first").before(left);

            var right = $(".slide-1.active1").children(":last");
            $(".slide-1.active1").children(":last").remove();
            $(".slide-1.active1").children(":first").before(right);

            $(".slide-1.active1").animate({left:'-1000px' }, 0);
            $(".slide-1.active1").animate({left:'0px' }, 800);
        }
    });
});
//开发文档
// $(function(){
//     var dots = $("#dots li");//获取按钮
//     var ul = $("#dev-doc ul");
//     var speed = $(ul).find(".dev-doc-child").eq(0).width();
//     var timer = null;//定义定时器
//     var loop = 0;//当前图片的索引值
//     dots.on('click',function(){
//         //改变选中状态
//         $(this).addClass("current").siblings().removeClass("current");
//         var index = $(this).index();
//
//         ul.animate({
//            "left":-speed * index
//         });
//     });
//     //自动轮播
//     function autoPlay(){
//         timer = setInterval(function(){
//             loop++;
//             if(loop > dots.length-1){
//                 loop = 0;
//             }
//             dots.eq(loop).trigger('click');
//         },5000);
//     }
//     autoPlay();
//     dots.hover(function () {
//         clearInterval(timer);
//     },function(){
//         autoPlay();
//     });
//     //移入盒子清除定时器
//     $("#dev-doc").hover(function(){
//         clearInterval(timer);
//     },function(){
//         autoPlay();
//     });
// });

//活跃度
$(function(){
    var allNum = parseFloat($("#allNum").html());
    var apiNum = parseFloat($("#apiNum").html());
    var timer = null;//定义定时器
    timer = setInterval(function(){
        //定义一个随机数
        var num = Math.floor(Math.random()*998+999);
        allNum += num;
        apiNum += num;
        $("#allNum").html(allNum);
        $("#apiNum").html(apiNum);
    },2000);
});

//console
$(function(){
   $("#console").click(function(){
       $(location).attr('href','/portal/u/login');
       // window.location.href = "u/login";
   });
});

