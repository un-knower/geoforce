/**
 * 标准GPS坐标转化
 *
 **/

var tranGPS = {
	tranCall:null,
	infoLen:0,
	infoDatas:null,//转换完成后的data数据
	historyList:null,//历史轨迹数据格式不同单独将位置数据转换坐标
	type:null,
	maxCount:20,//百度批量转换一次最多20个
	tranXy:function(infoObjs,active,callFun){//标准坐标转图商坐标
		this.tranCall = callFun;
		this.infoDatas = infoObjs;
		this.type = active;
		if(active == "ls"){//历史轨迹
			this.historyList = infoObjs[0].list;
			this.infoLen = this.historyList.length;
		}else{
			this.infoLen = infoObjs.length;
		}
		
		//ps_mapType in taglibs.jsp
		if(ps_mapType == "supermap"){//超图
			this.tranToSuper();
		}else if(ps_mapType == "baidu"){//百度
			this.tranToBaidu(0);
		}else{
			this.tranToSuper();
		}
		
	},
	tranToBaidu:function(index){
		var infos = null,points = [],tmp = null,obj = null,startIndex = index;
		if(this.type == "ls"){
			infos = this.historyList;
		}else{
			infos = this.infoDatas;
		}
		
		for(var i=startIndex;i<this.infoLen;i++){
			index = i;
			if(parseInt(i % this.maxCount) == 0 && i != 0){
				break;
			}
			obj = infos[index];
			
			tmp = new BMap.Point(obj.originalLon,obj.originalLat);
			points.push(tmp);
			if(i == this.infoLen-1){
				break;
			}
		}
		BMap.Convertor.transMore(points,0,this.callback);
		callback = function(results){
			var xyResult = null,tmpObj = null;
			for(var i=0;i<results.length; i++){
				xyResult = results[i];
				tmpObj = infos[parseInt(startIndex+i)];
				if(xyResult.error != 0){//出错赋值原始坐标
					tmpObj.x = tmpObj.originalLon;
					tmpObj.y = tmpObj.originalLat;
				}else{
					tmpObj.x = xyResult.x;
					tmpObj.y = xyResult.y;
				}
				infos[parseInt(startIndex+i)] = tmpObj;	  
			}
			if(this.type == "ls"){
				this.historyList = infos;
				this.infoDatas[0].list = infos;
			}else{
				this.infoDatas = infos;
			}
			if(index < tranGPS.infoLen-1){
				tranGPS.tranToBaidu(index+1);
			}else{
				tranGPS.tranCall && tranGPS.tranCall();
			}
		}
	},
	tranToSuper:function(){//标准坐标转超图坐标
		var obj = null;
		var infos = null;
		if(this.type == "ls"){//历史轨迹数据格式和定位等不同
			infos = this.infoDatas[0].list;
		}else{
			infos = this.infoDatas;
		}
		for(var index in infos){
			obj = infos[index];
			obj.x = obj.longitude;
			obj.y = obj.latitude;
			infos[index] = obj;
		}
		if(this.type == "ls"){//历史轨迹数据格式和定位等不同
			this.infoDatas[0].list = infos;
		}else{
			this.infoDatas = infos;
		}
		tranGPS.tranCall && tranGPS.tranCall();
	}
};
