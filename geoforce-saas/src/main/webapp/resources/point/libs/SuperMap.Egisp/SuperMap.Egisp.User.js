
/** 
 * 企业版在线接口
 * 命名空间:SuperMap.Egisp.User
 */
SuperMap.Egisp.User = SuperMap.Egisp.User || {};
/** 
 * 当前的产品ID
 */
SuperMap.Egisp.User.currentModuleId = '';
/** 
 * 获取设置的默认城市
 */
SuperMap.Egisp.User.getDefaultCity = function(callback) {
    SuperMap.Egisp.request({
        url: urls.server + "/user/getUserDefaultCity?",
        data: {},
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e.isSuccess && e.result) {
                callback(e.result);
            }
            else {
                callback(false);
            }
        },
        error: function(){
            callback(false);      
        }
    });
}

/**
 * 设置默认城市
 */
SuperMap.Egisp.User.setDefaultCity = function() {
    var smcity = $('.smcity');
    var admincode = smcity.attr('admincode');
    var level = smcity.attr('level');
    if(!admincode || admincode == '' || !level || level == '' || level == '0') {
        return;
    }
    SuperMap.Egisp.showMask();
    SuperMap.Egisp.request({
        url: urls.server + "/user/setUserDefaultCity?",
        data: {
            admincode: admincode,
            level: level
        },
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e.isSuccess) {
                $('.show-default-city').html( '(默认: ' + smcity.attr('adminname') + ')' );
                SuperMap.Egisp.showPopover('保存默认城市成功');
            }
            else {
                SuperMap.Egisp.showHint(e.info && e.info.length > 0 ? e.info : '保存默认城市失败');
            }
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            SuperMap.Egisp.showHint('保存默认城市失败');          
        }
    });
}

/**
 * 获取子部门
 */
SuperMap.Egisp.User.getDepts = function(success, failed){
    SuperMap.Egisp.request({        
        url : urls.server + "/dept/getChildDepts?random=" + Math.random(),
        success : function(e) {
            if(e && e.length > 0) {
                success(e)
            }
            else {
                failed();
            }
        },
        error: function() {
            failed();
        }
    });
}
/**
 * 获取购买的产品
 */
SuperMap.Egisp.User.getMenu = function(success, failed){
    SuperMap.Egisp.request({        
        url : urls.server + "/user/menu?random=" + Math.random(),
        success : function(e) {
            if(e && e.length > 0) {
                success(e)
            }
            else {
                failed();
            }
        },
        error: function() {
            failed();
        }
    });
}
/**
 * 查询日志
 */
SuperMap.Egisp.User.getOperationLogs = function(param, success, failed){
    SuperMap.Egisp.request({        
        url : urls.server + "/syslog/getAllLogs?random=" + Math.random(),
        data: param,
        success : function(e) {
            if(e) {
                success(e)
            }
            else {
                failed();
            }
        },
        error: function() {
            failed();
        }
    });
}