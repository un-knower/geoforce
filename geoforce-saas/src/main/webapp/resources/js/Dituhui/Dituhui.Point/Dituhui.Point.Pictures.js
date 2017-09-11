/**
 * 网点图片类
 */
Dituhui.Point.Pictures = Dituhui.Point.Pictures || {};
/**
 * 根据ID查询网点图片
 */
Dituhui.Point.Pictures.search = function(id, success, failed) {    
    var param = {
        pointid: id
    };
    Dituhui.request({
        url: urls.server + "/pointService/getPointPicturesByPointid?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess && e.result && e.result.length > 0) {
                success(e.result);
            }
            else {
                failed(e.info ? e.info : "网点图片查询失败");
            }
        },
        error: function(){
            Dituhui.hideMask();
            failed();
        }
    })
}
/**
 * 根据图片ID删除网点图片
 */
Dituhui.Point.Pictures.remove = function(id, success, failed) {
    var param = {
        picid: id
    };
    Dituhui.request({
        url: urls.server + "/pointService/deletePointPicture?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
                success(e.result);
            }
            else {
                failed();
            }
        },
        error: function(){
            Dituhui.hideMask();
            failed();
        }
    })
}
