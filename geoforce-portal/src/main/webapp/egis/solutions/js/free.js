/**
 * 免费试用
 */
var trial = {
    /**
     * 选择的产品(非物流行业)，内容为ID
     */
    product_ids: [],

    /**
     * 保存用户的邮箱
     */
    email: '',

    /**
     * 试用的类型，普通产品：0，整个物流行业：1
     */
    type: 0,

    /**
     * 已经在试用的产品
     */
    trial_products: []
};


/**
 * 判断用户是否登录
 * @param: success_handler - 回调函数
 */
trial.checkUser = function( success_handler ) {
    $.ajax({
        type: 'GET',
        url: urls.userdetail,
        dataType: 'jsonp',
        success: function(e) {
            if(e && e.success && e.result) {
                var me = e.result;
                trial.email = me.email;

                //判断用户是否已经完善企业信息
                if(!me.id || me.id === "" || me.id.length === 0) {
                    $('#txt_email').val(me.email);
                    trial.showFillUserBox();
                }
                else {
                    success_handler(e);
                }
            }
            else {
                location.href = urls.login;
            }
        }
    });
}

/**
 * 判断用户是否已经试用了产品
 * 历史方案查询-funcNames不为空的才是正在试用的
 * @param: success_handler - 回调函数
 * @param: moreProducts - 点击功能包中的免费试用
 */
trial.checkTrying = function(success_handler, moreProducts) {   
    var param = {
        pageNo: -1
    }; 
    trial.trial_products = [];

    $.ajax({
        type: 'GET',
        url: urls.tradesearch,
        dataType: 'jsonp',
        data: param,
        success: function(e) {
            if(e && e.success && e.result ) {
                //没有试用过该产品
                if( !e.result.infos || e.result.currentCount === 0 || e.result.infos.length === 0) {
                    success_handler();
                }
                else {
                    var infos = e.result.infos;

                    var isTrying = false, isOverdued = false, isWaitAudit = false;
                    for( var i=infos.length; i--; ) {
                        var order = infos[i];
                        if(order.funcNames === '') {
                            continue;
                        }
                        var funcNames = order.funcNames.split('_');
                        for(var k=funcNames.length; k--; ) {
                            var id = funcNames[k];
                            trial.trial_products = trial.addTryingProduct(id, trial.trial_products);
                            for(var j=trial.product_ids.length; j--; ) {
                                if(trial.product_ids[j] === id) {
                                    if(order.status == '0') {
                                        isWaitAudit = true;
                                    }
                                    else if( order.status == '2' ){                                        
                                        var deadline = Number( order.orderItems[0].deadline.replace(/-/g,"") );
                                        var today = Number( new Date().format('yyyy-MM-dd').replace(/-/g,"") );
                                        if(deadline >= today) {
                                            //正在试用  
                                            isTrying = true;                            
                                        }
                                        else {
                                            //过期了
                                            isOverdued = true;
                                        }
                                    }
                                }
                            }
                        }       
                    }   
                    if( isOverdued ) {           
                        trial.showOverdueWindow();
                        return;        
                    } 

                    if( isTrying && (typeof(moreProducts) === 'undefined' || !moreProducts ) ) {
                        trial.showTrialWindow();
                        return;                                  
                    }

                    if( typeof(moreProducts) !== 'undefined' || moreProducts ) {                        
                        var len = trial.trial_products.length + trial.product_ids.length;
                        //只能最多选择2个模块进行试用
                        if(len > 2) {
                            trial.showNoPermissionsWindow();
                            return;
                        }
                    }

                    if(trial.trial_products.length >= 2) {
                        //最多只能试用2个产品
                        trial.showNoPermissionsWindow();
                        return;
                    }

                    if( isWaitAudit ) {
                        trial.showTrialToAuditWindow();
                        return;
                    }

                    success_handler();
                }
            }
        }
    });
}



/**
 * 获取随机赠送的模块
 */
trial.getPresentsPros = function() {
    var param = [];
    var now = new Date();
    now.setDate(new Date().getDate()+14);
    var useTime = new Date().format('yyyy-MM-dd');
    var deadline = now.format('yyyy-MM-dd');
    var len_system = urls.systemmges.length;
    for(var k=len_system; k--; ) {
        var myid = urls.systemmges[k];
        var o = {
            moduleId: myid,
            name: "free",
            useTime: useTime,
            deadline: deadline,
            consultPrice: 0,
            useLimit: 0,
            type: 1
        };
        param.push(o);
    }
    return param;
}

/**
 * 显示补充用户信息的弹窗
 */
trial.showFillUserBox = function(){
    $('#modal_filluser').modal('show');
    $('.modal-backdrop').css('background-color', '#ccc');
    $('#btn_filluser1').show();
    $('#btn_filluser').hide();
}

/**
 * 补充信息成功后的弹窗
 */
trial.fillUserSuccess = function() {
    $('.modal').modal('hide');

    $('#modal_filluser_success').modal('show');
    $('.modal-backdrop').css('background-color', '#ccc');
}

/**
 * 暂无权限弹窗
 */
trial.showNoPermissionsWindow = function() {
    $('.modal').modal('hide');

    $('#modal_no_permissions').modal('show');
    $('.modal-backdrop').css('background-color', '#ccc');
}

/**
 * 用户本身无权限的弹窗
 */
trial.showNoPermissionsWindowUser = function() {
    $('.modal').modal('hide');

    $('#modal_no_permissions_user').modal('show');
    $('.modal-backdrop').css('background-color', '#ccc');
}

/**
 * 试用已过期
 */
trial.showOverdueWindow = function() {
    $('.modal').modal('hide');

    $('#modal_overdue').modal('show');
    $('.modal-backdrop').css('background-color', '#ccc');
}

/**
 * 弹窗 欢迎进入试用
 */
trial.showTrialWindow = function() {
    $('.modal').modal('hide');

    $('#modal_trial').modal('show');
    $('.modal-backdrop').css('background-color', '#ccc');
}


/**
 * 弹窗 免费试用生成订单后，需要审核
 */
trial.showTrialToAuditWindow = function() {
    $('.modal').modal('hide');

    $('#modal_order_audit').modal('show');
    $('.modal-backdrop').css('background-color', '#ccc');
}

/**
 * 物流行业-免费试用
 */
trial.trialFreeWuliu = function(id) {
    trial.product_ids = [ id ];

    //验证用户是否登录
    trial.checkUser(function(e){
        var me = e.result;
        
        //判断用户是否已经在试用该产品
        trial.checkTrying(function(){
            //生成订单
            trial.createTrialOrderFromWuliu(function(e){
                e = jQuery.parseJSON(e);
                if(e.success) {
                    trial.showTrialToAuditWindow();
                }
            });
        });
    });
}


/**
 * 从整个物流产品生成订单
 * @param: success_handler - 回调函数
 */
trial.createTrialOrderFromWuliu = function(success_handler) {    

    var data_wuliu = data_apps_wuliu_v1.concat();

    var now = new Date();
    now.setDate(new Date().getDate()+14);

    var func_name = 'wuliu';

    var useTime = new Date().format('yyyy-MM-dd');
    var deadline = now.format('yyyy-MM-dd');

    var param = [];
    for(var i=data_wuliu.length; i--; ) {
        var item = data_wuliu[i];
        param.push(
            {
                moduleId: item.id,
                useTime: useTime,
                deadline: deadline,
                type: item.type
            }
        );
        if( !item.kids ) {
            continue;
        }
        for( var k=item.kids.length; k--; ) {
            var kid = item.kids[k];
            param.push(
                {
                    moduleId: kid.id,
                    useTime: useTime,
                    deadline: deadline,
                    type: kid.type
                }
            );            
        }
    }

    param = param.concat( trial.getPresentsPros() );

    var parameter = {
        remarks: '',
        orderItems: param,
        funcNames: func_name,
        orderType: 0
    };

    $.post(
        urls.trade_buy,
        {parameter:  JSON.stringify( parameter ) },
        success_handler
    );
}

/**
 * 累积已经在试用或者已经使用过的产品(去重)
 * @param: id
 * @param: datas 
 */
trial.addTryingProduct = function(id, datas) {
    for( var i=datas.length; i--; ) {
        if( datas[i] === id ) {
            return datas;
        }
    }
    datas.push(id);
    return datas;
}


trial.fillUser = function(callbacks) {
    var flag = User.verifyUserInfo();
    if(!flag) {
        return false;
    }
    $('#btn_filluser').button('正在处理，请稍候...');

    var param = {
        realName: $("#txt_realname").val(),
        qq: $("#txt_qq").val(),
        telephone: $("#txt_mobile").val(),
        email: $("#txt_email").val(),
        companyName: $("#txt_companyName").val(),
        username: user.username,
        admincode: $('.user-location').val(),
        adminname: $('.user-location').find('option:selected').text(),
        combusiness: $('.user-solutions').val(),
        businessremark: $('.text-user-solutions').val()
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.register + "&callbacks=?",
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success) {     
                User.refreshHeader();                  
                if(typeof(callbacks) === "function") {
                    callbacks();
                }
                else {
                    trial.fillUserSuccess();               
                }   
            }
            else {         
                var info = e.info;
                if( info.match("已使用") && info.match("email") ) {                    
                    scrollto("#txt_email");
                    $("#hint_email").html("该邮箱已被使用");
                }
                else {
                    showPopover(e.info);
                }
            }
        }
    });
}