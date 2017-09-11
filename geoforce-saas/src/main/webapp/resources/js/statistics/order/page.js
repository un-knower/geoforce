
$(function(){
	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();
	var param_location = urls.getUrlArgs();
	Dituhui.User.currentModuleId = param_location.moduleid ? param_location.moduleid : '';

	$('ul.during > li').click(function(){
		$(this).addClass('action').siblings('li').removeClass('action');
		var me = $(this);
		var div = me.parent().find(' + .custom-during')
		
		if(me.attr('data-show-date')) {
			div.show();
		}
		else {
			div.hide();
		}
	});
	
	$('.tab-map ul.during > li').click(function(){
		var me = $(this);
		if(me.attr('data-show-date')) {
			$(".tab-map .data-list-group").addClass("shorter");
		}
		else {
			$(".tab-map .data-list-group").removeClass("shorter");
		}		
	})
	
	$('.tab-map ul.during > li[data-time]').click(function(){
		var me = $(this);
		var time = me.attr('data-time');
		var end = new Date().format("yyyy-MM-dd hh:mm:ss");
		var start;
		switch(time) {
			case 'day':
				start = new Date(new Date().setDate( new Date().getDate() - 1));
				break;
			case 'week':
				start = new Date(new Date().setDate( new Date().getDate() - 7));
				break;
			case 'month':
				start = new Date(new Date().setDate( new Date().getDate() - 30));
				break;
		}
		start = start.format("yyyy-MM-dd hh:mm:ss");
		Map.start = start;
		Map.end = end;
		Map.search();
		
		var bodyHeight = getWindowHeight();
		var listHeight = $('.custom-during').is(":visible") ? 160 : 100;
		$('.data-list').css({'height': (bodyHeight - listHeight)+'px'});
	});
	
	$('.tab-map .custom-during .button-search').click(Map.checkCustomTime);
	
	$('.close, .btn-cancel, .close-modal').click(function(){
		var me = $(this);
		if(me.hasClass("close-fade-out")) {
			$(me.attr("data-target")).fadeOut("fast");
		}
		else {
			$(me.attr("data-target")).addClass("hide");
		}
		if('.data-update-style' == me.attr("data-target")) {
    		$('button[option="update-style"]').unbind('click').click(Map.Style.edit);
		}
	});
	$('input.input-during').click(function(){
		WdatePicker({
			dateFmt:'yyyy-MM-dd HH:mm:ss',
			maxDate:'%y-%M-#{%d} 23:59:59',
			minDate: '%y-%M-#{%d-30} 23:59:59'
		});
	});
	
    $(".close-table").click(function(){
    	$(".tab-tabel").addClass("hide");
    	$($(this).attr("show-target")).removeClass("hide");
    });
	
    $(".close-map").click(function(){
    	$(".tab-map").addClass("hide");
    	$($(this).attr("show-target")).removeClass("hide");
    });
    
    
    $(".data-list-group").on("click", ".list-group-item .top span.name", function(){
        var me = $(this);
        var code = me.attr('data-code');
        var level = me.attr('data-level');
        if( !code || !level) {
            return;
        }
        if(level == 3 ) {
        	$(".tab-map").addClass("hide");
        	$(".tab-tabel").removeClass("hide");
        	$(".close-table").attr("show-target", ".tab-map");
        	if(!tableDetail) {
        		tableDetail = TableDetail({
        			admincode: code,
        			level: level
        		});
        	}
        	else {
        		tableDetail.admincode = code;
        		tableDetail.level = level;
        		$("#grid-table").clearGridData();
        		tableDetail.reload();
        	}
        	return;
        }
        $('.smcity').attr({
            'admincode': code,
            'level': level
        });
        if(level == 1) {
            Dituhui.SMCity.showCurrentProvince(me.attr('data-name'));
        }
        else {
            Dituhui.SMCity.showCurrentCity(me.attr('data-name'));
        }        
        Map.search();
    }); 
    
    $(".console-foot .btn-export").on("click", function(){
    	var url = "http://" + location.host + urls.server + "/statistic/order/exportOrderDetail?";
    	var smcity = $('.smcity');
	    var code = smcity.attr('admincode');
	    var level = smcity.attr('level'); 
	    if( code && code != "" ) {
	        url += "&admincode=" + code + "&level=" + level;
	    }
	    url += "&start="+Map.start + "&end=" + Map.end + "&resulttype=" + Map.searchType;
	    
	    window.open(url, "_blank");
    });
}); 



