var User = {};
User.key = '8a04a77b4ee30e56014f00e022800216';
/*
 * 初始化时用户登录
 */
User.login = function() {
	SuperMap.Egisp.request({
		url: urls.server + "/pointService/initSessionUserByKey?",
        data: {
        	key: User.key
        },
        dataType: 'json',
        success: function(e){
        	if(e && e.isSuccess) {
        		User.getDefaultCity();
        		Point.Style.search();
        	}
        },
        error: function(){}
	});
}
User.defaultCity = null;
User.getDefaultCity = function() {
	SuperMap.Egisp.request({
        url: urls.server + "/user/getUserDefaultCity?",
        data: {},
        success: function(e){
        	var attr = {
        		admincode: '110000', 
        		level: 1, 
        		adminname: '北京'
        	}
        	
            if(e.isSuccess && e.result && e.result.admincode) {
                User.defaultCity = e.result;
                attr = {
            		admincode: e.result.admincode, 
            		level: e.result.clevel, 
            		adminname: e.result.defaultname
            	}
                var level = e.result.clevel, center = new SuperMap.LonLat(e.result.x, e.result.y);
	            if(level == 1) {
	            	var isZhixia = smcitys.isZhixia(attr.admincode);
	            	map.setCenter(center, isZhixia ? 8 : 6);
	            }
	            else if(level == 2) {
	            	map.setCenter(center, 8);
	            }
	            else {
	            	map.setCenter(center, 12);
	            }
            }
            jQuery('.smcity').attr(attr).html(attr.adminname);
            smcitys.GetBoundryByCode(attr.admincode, attr.level);
            
            Point.search();
        },
        error: function(){
                
        }
    });
}
User.setDefaultCity = function() {
	event.stopPropagation();
	
	var me = jQuery(this).parents('li');
	var admincode = me.attr('admincode');
	var level = me.attr('level');
	
	SuperMap.Egisp.request({
        url: urls.server + "/user/setUserDefaultCity?",
        data: {
            admincode: admincode,
            level: level
        },
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e.isSuccess) {
                jQuery('.header .title').html( '(默认: ' + e.result.defaultname + ')' );
                Map.showSuccess('设置成功');
            }
            else {
                SuperMap.Egisp.showHint(e.info && e.info.length > 0 ? e.info : '设置失败');
            }
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            SuperMap.Egisp.showHint('设置失败');     
        }
    });
    
}













