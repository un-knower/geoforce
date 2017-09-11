
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);

    var href = location.href;
    if(href.match("#")) {
        var position = href.slice("#")[1];
        
        scrollto(position);
    }

    $('a[option="watch_video"]').click(watchVideo);
    $('#modal_video').on('shown.bs.modal', play);
    $('#modal_video').on('hidden.bs.modal', pause);

});

function initcss() {
    $(".introduce-area").css({"width": 960});
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );

    var bodywidth = getWindowWidth();
    // $(".pendant").css( { "left":  ( bodywidth*0.5 + 150) } );
    $(".pendant").css( { "left": "100px"} );
}

/**
 * 自动播放
 */
function play() {    
    var player = videojs('video');
    player.load();
    setTimeout(function(){
        player.play();
    }, 700)
}
/**
 * 退出后暂停
 */
function pause() {    
    var player = videojs('video');
    if(!player.paused()) {        
        player.pause();
    }
}

/**
 * 关闭窗口后停止播放
 */
function stopPlay() {    
    var player = videojs('video');
    // player.dispose();
}

/**
 * 观看演示
 */
function watchVideo(){
    var me = $(this);
    var player = videojs('video');
    player.autoplay( false );
    var type = me.attr("data-movie");
    var openmodal = me.attr("open-modal");
    if(type === "all") {
        player.src(data_movies.movies);
        var list = $('a[data-list="true"]');
        list.each(function(){
            var me = $(this);
            var li = me.parent('li');
            li.removeClass('selected');
            var type = me.attr("data-movie");
            if(type === "wangdian") {
                li.addClass('selected');
                me.unbind('click');
            }
            else {
                li.addClass('item');
                me.unbind('click').bind("click", watchVideo);
            }
        });
    }
    else {
        var source = data_movies.getMovie(type);
        if(source) {
            $('a[data-list="true"]').each(function(){
                var me = $(this);
                var li = me.parent('li');
                li.removeClass('selected');
                var type = me.attr("data-movie");

                // var text = me.text();
                // text = text.replace("当前播放: ", "");
                
                if(type === source[0].data_movie) {
                    li.addClass('selected');
                    me.unbind('click');
                    // me.html("当前播放: " + text);
                }
                else {
                    li.addClass('item');
                    me.unbind('click').bind("click", watchVideo);
                    // me.html(text);
                }
            });

            player.src(source);
            if(openmodal === "false") {
                setTimeout(function(){
                    player.play();
                }, 500);         
            }
        }
    }
    if(openmodal === "true") {
        $('#modal_video').modal('show');            
    }
}

/**
 * 视频数据
 */
var data_movies = {
    movies: [
        { type: "video/mp4", src: "data/wangdian.mp4", data_movie: "wangdian" },
        { type: "video/mp4", src: "data/quhua.mp4", data_movie: "quhua" },
        { type: "video/mp4", src: "data/default.mp4", data_movie: "fendanbao" },
        { type: "video/mp4", src: "data/default.mp4", data_movie: "zhixingtong" },
        { type: "video/mp4", src: "data/default.mp4", data_movie: "dianshang" },
        { type: "video/mp4", src: "data/default.mp4", data_movie: "tongji" },
        { type: "video/mp4", src: "data/pathplan.mp4", data_movie: "pathplan" }
    ]
};

data_movies.getMovie = function(data) {
    var movies = data_movies.movies;
    var len = movies.length;
    for(var i=len; i--; ) {
        var movie = movies[i];
        if(movie.data_movie === data) {
            var arr = [movie];
            return arr;
        }
    }
    return null;
}

function free(type){
    //验证是否登陆
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.userdetail,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success) {
                location.href = "../apps/cart-free?q=" + type;
            }
            else {
                location.href = "../login";
            }

        }
    });    
}