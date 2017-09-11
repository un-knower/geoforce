
function isBoughtMe(id, items) {
    var len = items.length;
    if(len === 0) {
        return false;
    }
    for(var i=len; i--; ) {
        var item = items[i];
        if( id === items[i].id ) {
            return true;
        }
    }
    return false;
}

//iframe自适应高度
$(function(){
	$(window.parent.document).find("#content_frame").load(function(){
        var thisheight = $(document).height() + 10;
		$(this).height(thisheight);
	});

	var nav_offets = $('.product-tab').offset();
	if( nav_offets ) {
    	Scroll.nav_top = nav_offets.top;
    	Scroll.nav_left = nav_offets.left;
	}

	//免费试用
	$('a[data-option="put-free"]').click(function(){
		var me = $(this);
		var id = me.attr("data-id");
		window.parent.trial.freeTrialClickHandler( id );
		duringFreeTrial(me);
	});

	//物流行业 -- 免费试用
	$('a[data-option="put-all-wuliu-free"]').bind('click', function(){
		var me = $(this);
		window.parent.trial.trialFreeWuliu( me.attr('data-id') );
		duringFreeTrial(me);
	});

	rebindClick();


	//联系客服
	$("a[data-option='kefu']").click(function(){
		window.parent.openServiceWindow();
	});
	$('a[data-option="changeFrame"]').click(function(){		
		var me = $(this);
		var id = me.attr("data-id");
		window.parent.changeFrame(id);
	})

    $('a[option="watch_video"]').click(function(){
        var me = $(this);
        // window.parent.watchVideo(me);
        window.parent.openServiceWindow();
    });

    setTimeout(function(){    	
		$('a[data-option="put"]').each(function(){
			var me = $(this);
			var id = me.attr("data-id");
	    	var visible = window.parent.checkIsBought(id);
	    	if(visible) {
				me.click();
	    	}
		})
    }, 300);
});


function setBtns(id) {
	var me = $('a[data-id="'+ id +'"][data-option="put"]');

	me.html("已加入").unbind('click').bind('click', removeAppForSingle);
}

function resetbtn(id) {
	var me = $('a[data-id="'+ id +'"][data-option="put"], a[data-id="'+ id +'"][data-option="put-wiliu"]');
	var text = me.attr("oldtext") ? me.attr("oldtext") : "加入我的方案";
	me.html(text);
	rebindClick();
}

function resetBtns() {
	var me = $('a[data-option="put"], a[data-option="put-wuliu"]');
	var text = me.attr("oldtext") ? me.attr("oldtext") : "加入我的方案";
	me.html(text);

	rebindClick();
}

function duringFreeTrial(btn) {
	btn.html("正在处理...").unbind('click');
}

function resetFreeBtn() {
	//免费试用
	$('a[data-option="put-free"]').unbind('click').click(function(){
		var me = $(this);
		var id = me.attr("data-id");
		window.parent.trial.freeTrialClickHandler( id );
		duringFreeTrial(me);
		me.html("免费试用");
	}).html("免费试用");

	//物流行业 -- 免费试用
	$('a[data-option="put-all-wuliu-free"]').unbind('click').bind('click', function(){
		var me = $(this);
		window.parent.trial.trialFreeWuliu( me.attr('data-id') );
		duringFreeTrial(me);
	}).html("免费试用");
}

function rebindClick(){
	$('a[data-option="put"]').unbind('click').bind('click', addAppForSingle);

	$('a[data-option="put-wuliu"]').unbind('click').bind('click', addAppForWuliu);

	$('a[data-option="put-all-wuliu"]').unbind('click').bind('click', addAppsForWuliu);
}

function addAppForSingle(){
	var me = $(this);
	window.parent.addAppForCookie(me.attr("data-id"));
	me.html("已加入").unbind('click').bind('click', removeAppForSingle);
}
function removeAppForSingle(){
	var me = $(this);
	window.parent.removeAppFromCookie( me.attr("data-id") );
	var text = me.attr("oldtext") ? me.attr("oldtext") : "加入我的方案";
	me.html(text);
	me.unbind('click').bind('click', addAppForSingle);
}

function addAppForWuliu(){
	var me = $(this);
	window.parent.addWuliuAppForCookie( me.attr("data-id") );
	me.html("已加入").unbind('click').bind('click', removeAppForWuliu);
}

function removeAppForWuliu() {	
	var myself = $(this);
	window.parent.removeAppFromCookie( myself.attr("data-id") );
	var text = myself.attr("oldtext") ? myself.attr("oldtext") : "加入我的方案";
	myself.html(text);
	myself.unbind('click').bind('click', addAppForWuliu);
}

function addAppsForWuliu() {
	var me = $(this);
	window.parent.addWuliuAppsForCookie();
	me.html("已加入").unbind('click').bind('click', removeAppsForWuliu);
}
function removeAppsForWuliu() {	
	var me = $(this);
	window.parent.removeWuliuAppsForCookie();
	var text = me.attr("oldtext") ? me.attr("oldtext") : "加入我的方案";
	me.html(text).unbind('click').bind('click', addAppsForWuliu);
}

/**
 * 页面滚动，保持导航条在最前面
 */
var Scroll = {
	nav_top: 0,
	nav_left: 0
};

Scroll.window_scrollHandler = function(topVal) {	 
	var nav = $('#J_tab');
    if(topVal <= Scroll.nav_top) {    
      	nav.removeClass('tab-fix-top').css({"top": "0"});
    }    
    else {  
    	if( !nav.hasClass('tab-fix-top') ) {
      		nav.addClass('tab-fix-top');
    	}
    	nav.css({"top": (topVal - 100) + "px"});
    } 
}