﻿/* COPYRIGHT 2012 SUPERMAP
 * 本程序只能在有效的授权许可下使用。
 * 未经许可，不得以任何手段擅自使用或传播。*/

/**
 * @requires SuperMap/Util.js
 * @requires SuperMap/BaseTypes/Bounds.js
 * @requires SuperMap/CanvasLayer.js
 */

/**
 * Class: SuperMap.Layer.CloudLayer
 * 云服务图层类。
 *     通过向SuperMap 云服务器发送请求得到 SuperMap 云服务发布的图层。
 *
 * Inherits from:
 *  - <SuperMap.CanvasLayer>
 */

SuperMap.Layer.CloudLayer = SuperMap.Class(SuperMap.CanvasLayer, {

    /**
     * APIProperty: name
     * {String} 图层标识名称，默认为：CloudLayer。
     */
    name: "CloudLayer",
    
    /**
     * APIProperty: url
     * {String} 地图资源地址。默认为：http://t0.supermapcloud.com/FileService/image
     */
    url: 'http://t1.supermapcloud.com/FileService/image',
    

    /**
     * Constructor: SuperMap.Layer.CloudLayer
     * 云服务图层类。
     *
     * Parameters:
     * options - {Object}  附加到图层属性上的可选项。
     */
    initialize: function (options) {
        
        var me = this;
        if( options && options.url ) {
            me.url =  options.url + '?x=${x}&y=${y}&z=${z}';
        }
        
        //超图云只有一个开放的出图地址，投影为墨卡托投影，所以maxExtent和resolutions可以直接设置好
        options = SuperMap.Util.extend({
            
            maxExtent: new SuperMap.Bounds(

                -20037508.3427892440,
                -20037508.3427891,
                 20037508.3427892440,
                 20037508.3427891
            ),

            //第19级分辨率为0.298817952474，但由于绝大部分城市和地区在此级别都无图，所以暂不增加
            // resolutions: [156605.46875, 78302.734375, 39151.3671875, 19575.68359375, 9787.841796875, 4893.9208984375, 2446.96044921875, 1223.48022460937, 611.740112304687, 305.870056152344, 152.935028076172, 76.4675140380859, 38.233757019043, 19.1168785095215, 9.55843925476074, 4.77921962738037, 2.38960981369019, 1.19480490684509, 0.597402453422546]
            /*
            resolutions: [
            19575.68359375, 9787.841796875, 4893.9208984375, 2446.96044921875, 
            1223.48022460937, 611.740112304687, 305.870056152344, 152.935028076172, 
            76.4675140380859, 38.233757019043, 19.1168785095215, 9.55843925476074, 4.77921962738037, 
            2.38960981369019, 1.19480490684509, 0.597402453422546]
            */


            resolutions: [156543.033928041, 78271.5169640203, 39135.7584820102, 
            19567.8792410051, 9783.93962050254, 4891.96981025127, 2445.98490512563, 
            1222.99245256282, 611.496226281409, 305.748113140704, 152.874056570352, 
            76.4370282851761, 38.218514142588, 19.109257071294, 9.55462853564701, 
            4.77731426782351, 2.38865713391175, 1.19432856695588, 0.597164283477938]

        }, options);


        SuperMap.CanvasLayer.prototype.initialize.apply(me, [me.name, me.url, null, options]);
        me.units = "meter";
    },

    
    /**
     * APIMethod: destroy
     * 解构CloudLayer类，释放资源。  
     */
    destroy: function () {
        var me = this;
        SuperMap.CanvasLayer.prototype.destroy.apply(me, arguments);
        me.name = null;
        me.url = null;
    },



    /**
     * APIMethod: clone
     * 创建当前图层的副本。
     *
     * Parameters:
     * obj - {Object} 
     *
     * Returns:
     * {<SuperMap.Layer.CloudLayer>}新的图层。
     */
    clone: function (obj) {
        var me = this;
        if (obj == null) {
            obj = new SuperMap.Layer.CloudLayer(
                me.name, me.url, me.layerName, me.getOptions());
        }
       
        obj = SuperMap.CanvasLayer.prototype.clone.apply(me, [obj]);

        return obj;
    },
    
    /** 
     * Method: getTileUrl
     * 获取瓦片的URL。
     *
     * Parameters:
     * xyz - {Object} 一组键值对，表示瓦片X, Y, Z方向上的索引。
     *
     * Returns
     * {String} 瓦片的 URL。
     */
    getTileUrl: function (xyz) {
        var me = this,
            url = me.url;
        return SuperMap.String.format(url, {
            x: xyz.x,
            y: xyz.y,
            z: xyz.z
        });
    },

    CLASS_NAME: "SuperMap.Layer.CloudLayer"
});
