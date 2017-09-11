
/** 
 * 企业版在线接口
 * 命名空间:Dituhui.User
 */
Dituhui.User = Dituhui.User || {};
/*
 * 是否是总账号
 */
Dituhui.User.isTop = false;
Dituhui.User.dcode = "";
/** 
 * 当前的产品ID
 */
Dituhui.User.currentModuleId = '';
Dituhui.User.special = false;

/*
 * 是否有乡镇权限
 */
Dituhui.User.hasTownAuthority = false;
Dituhui.User.allowTownOption = false;
Dituhui.User.regionTableExtend = false;

/**
 * 开通区划管理的乡镇权限的userid
//8a04a77b4d7a6206014ddb66af2f01a2  龙邦物流userid 
//8a283f9358e3b3a30158f7aba1010123  西单民生银行userid 
//8a04a77b58e36449015900d6316c024a 方太userid 
//8a04a77b4cbc865c014cc0b8dfc4001a 中铁物流userid 
//8a04a77b4c54e9dc014c72cdd10100b5 李玮顾userid 
//8a04a77b54effacc0155a0ed71870f54  国通userid 
//8a04a77b56a21dcc0156f7fca9f007b3 速尔快递userid
// 海尔售后账号 8a04a77b5962181a015b234dbbd85634
// 海尔直配账号（日日顺） 8a04a77b5962181a015b232c2c255623 
// 颜菁菁账号 8a283f93596228fd015a3b61337e252c
// 徐冬冬账号 8a04a77b4cbc865c014cc15cd1160051
 */

//if(e.result.eid == "8a04a77b4d7a6206014ddb66aefc01a1" //龙邦物流eid
//if(e.result.eid == "40288e9f48625c010148625c07160000" //地图慧测试eid
//if(e.result.eid == "8a04a77b58e36449015900d6316c0249" //方太eid
//if(e.result.eid == "8a283f9358e3b3a30158f7aba1000122" //西单民生银行eid
//if(e.result.eid == "8a04a77b4c54e9dc014c72cdd0cc00b4" //李玮顾eid
//if(e.result.eid == "8a04a77b54effacc0155a0ed713e0f53" //国通物流eid
//eid=8a04a77b56a21dcc0156f7fca9b707b2  速尔快递eid 
// 8a04a77b5962181a015b234dbbd75633 海尔售后账号eid
// 8a04a77b5962181a015b232c2c255622 海尔直配账号（日日顺）
// 8a283f93596228fd015a3b61337e252b //颜菁菁eid
// 8a04a77b4cbc865c014cc15cd0d30050 //徐东东eid

Dituhui.User.countyKeys = [
	"8a04a77b4d7a6206014ddb66aefc01a1", //龙邦物流eid
	"40288e9f48625c010148625c07160000", //地图慧测试eid
	"8a04a77b58e36449015900d6316c0249", //方太eid
	"8a283f9358e3b3a30158f7aba1000122", //西单民生银行eid
	"8a04a77b4c54e9dc014c72cdd0cc00b4", //李玮顾eid
	"8a04a77b54effacc0155a0ed713e0f53", //国通物流eid
	"8a04a77b56a21dcc0156f7fca9b707b2",  //速尔快递eid
	"8a04a77b5962181a015b234dbbd75633", //海尔售后账号eid
	"8a04a77b5962181a015b232c2c255622",  //海尔直配账号（日日顺）
	"8a283f93596228fd015a3b61337e252b",  //颜菁菁
	"8a04a77b4cbc865c014cc15cd0d30050"  //徐东东
];

/**
 * 获取设置的默认城市
 */
Dituhui.User.getDefaultCity = function(callback) {
    Dituhui.request({
        url: urls.server + "/user/getUserDefaultCity?",
        data: {},
        success: function(e){
            Dituhui.hideMask();
            if(e.result) {
            	Dituhui.User.isTop = e.result.isTop;
            	Dituhui.User.dcode = e.result.dcode;
            	
                if( Dituhui.User.countyKeys.indexOf(e.result.eid) != -1
                	&& Dituhui.User.allowTownOption === true                 	
                ) { //地图慧测试eid
					Dituhui.User.hasTownAuthority = true;
				}
                
				//是否是海尔售后和直配企业账号，区划表格需要定制
				if(e.result.eid === "8a04a77b5962181a015b234dbbd75633"
					|| e.result.eid === "8a04a77b5962181a015b232c2c255622"
				) {
//				if(e.result.eid === "40288e9f48625c010148625c07160000") { //地图慧
					Dituhui.User.regionTableExtend = true;
				}
            }
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
Dituhui.User.setDefaultCity = function() {
    var smcity = $('.smcity');
    var admincode = smcity.attr('admincode');
    var level = smcity.attr('level');
    if(!admincode || admincode == '' || !level || level == '' || level == '0') {
        return;
    }
    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/user/setUserDefaultCity?",
        data: {
            admincode: admincode,
            level: level
        },
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
                $('.show-default-city').html( '(默认: ' + smcity.attr('adminname') + ')' );
                Dituhui.showPopover('保存默认城市成功');
            }
            else {
                Dituhui.showHint(e.info && e.info.length > 0 ? e.info : '保存默认城市失败');
            }
        },
        error: function(){
            Dituhui.hideMask();
            Dituhui.showHint('保存默认城市失败');          
        }
    });
}

/**
 * 获取子部门
 */
Dituhui.User.getDepts = function(success, failed){
    Dituhui.request({
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
Dituhui.User.getMenu = function(success, failed){
    Dituhui.request({        
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
Dituhui.User.getOperationLogs = function(param, success, failed){
    Dituhui.request({        
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
