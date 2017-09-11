package skins.zoomslider
{
	import flash.geom.Matrix;
	
	import mx.core.EdgeMetrics;
	import mx.skins.halo.SliderTrackSkin;
	public class Trackskin extends  SliderTrackSkin
	{
		public function Trackskin()
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
			
			super.graphics.beginFill(0xCFE6FF)
			super.graphics.drawRect(x,y-1,width,height);
			super.graphics.lineStyle(0,0xCFE6FF);
			super.graphics.endFill();
			
			//super.drawRoundRect(x, y , width, height,null, 0xE2E2E2, alpha, null, null, null, null);
		}
	}
}