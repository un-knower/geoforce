package skins.zoomslider
{
	import flash.geom.Matrix;
	
	import mx.core.EdgeMetrics;
	import mx.skins.halo.SliderTrackSkin;
	public class TrackHighLightSkin extends  SliderTrackSkin
	{
		public function TrackHighLightSkin()
		{
			super();
		}
		override public function get measuredWidth():Number   
		{   
			return 18;
		} 
		
		override public function get measuredHeight():Number   
		{   
			return 9; 
		} 
		
		override protected function drawRoundRect(x:Number, y:Number, 
												  width:Number, height:Number,
												  cornerRadius:Object=null,
												  color:Object=null,
												  alpha:Object=null, 
												  gradientMatrix:Matrix=null, 
												  gradientType:String="linear", 
												  gradientRatios:Array=null,
												  hole:Object=null):void
		{
			
			super.graphics.beginFill(0x09A2E0,1);
//			super.drawRoundRect(x, y-1, width,height,null,0x09A2E0 , 1,  null, null, null, null);
			
			super.graphics.drawRect(x,y-2,width,height);
//			super.graphics.lineStyle(0,0x09A2E0);
			super.graphics.endFill();
		}       
	}
}















