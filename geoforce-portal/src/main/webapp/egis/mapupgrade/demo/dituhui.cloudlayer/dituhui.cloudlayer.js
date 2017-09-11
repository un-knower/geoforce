/* COPYRIGHT 2012 SUPERMAP
 * 本程序只能在有效的授权许可下使用。
 * 未经许可，不得以任何手段擅自使用或传播。
 * */
/**
 * Class: SuperMap.Layer.CloudLayer
 * 云服务图层类。
 *     通过向SuperMap 云服务器发送请求得到 SuperMap 云服务发布的图层。
 *
 * Inherits from:
 *  - <SuperMap.CanvasLayer>
 */
SuperMap.Layer.CloudLayer=SuperMap.Class(SuperMap.CanvasLayer,{name:"CloudLayer",url:'http://t2.supermapcloud.com/FileService/image',initialize:function(options){var me=this;if(options&&options.url){me.url=options.url}me.url+="?&x=${x}&y=${y}&z=${z}&s=0";options=SuperMap.Util.extend({maxExtent:new SuperMap.Bounds(-20037508.3427892440,-20037508.3427891,20037508.3427892440,20037508.3427891),resolutions:[156543.033928041,78271.5169640203,39135.7584820102,19567.8792410051,9783.93962050254,4891.96981025127,2445.98490512563,1222.99245256282,611.496226281409,305.748113140704,152.874056570352,76.4370282851761,38.218514142588,19.109257071294,9.55462853564701,4.77731426782351,2.38865713391175,1.19432856695588,0.597164283477938]},options);SuperMap.CanvasLayer.prototype.initialize.apply(me,[me.name,me.url,null,options]);me.units="meter"},destroy:function(){var me=this;SuperMap.CanvasLayer.prototype.destroy.apply(me,arguments);me.name=null;me.url=null},clone:function(obj){var me=this;if(obj==null){obj=new SuperMap.Layer.CloudLayer(me.name,me.url,me.layerName,me.getOptions())}obj=SuperMap.CanvasLayer.prototype.clone.apply(me,[obj]);return obj},getTileUrl:function(xyz){var me=this,url=me.url;return SuperMap.String.format(url,{x:xyz.x,y:xyz.y,z:xyz.z})},CLASS_NAME:"SuperMap.Layer.CloudLayer"});