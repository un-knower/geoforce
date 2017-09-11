$(function(){
	
	
	/**
	 * 甘肃石油账号过期
	 */
	if(userid === "8a04a77b510e17c801532c04261225f1") {
		
	}

	//查找子菜单
	function getChildMenu(obj,pid){
		var childMenu=[];
		$.each(obj,function(k,v){
			if(pid==v.pid){
				childMenu.push(v);
			}
		});
		return childMenu;
	}
	
	$.ajax({
		type : "POST",
		url : location_url + "/user/menu?ram=" + Math.random() ,
		success : function(obj) {
			$.each(obj,function(key,value){
				if(!value.pid){
					var firstMenuHtml="";//父菜单的html变量
					var childMenus=getChildMenu(obj,value.id);
					var activeFirst="parentActiveCss";//如果点击了子菜单，则将这个替换为ACTIVE的css
					if(value.url && value.url!="#"){
						var valueUrl=value.url;
						if(valueUrl.indexOf("?")>0){
							valueUrl=valueUrl.substring(0,valueUrl.indexOf("?"));
						}
						activeFirst=('${uri}'.indexOf(valueUrl)>=0?"active":"");
					}
					
					firstMenuHtml="<li class='"+activeFirst+" "+(childMenus.length?"hsub":"")
						+" hover'><a href='javascript:void(0)' data-id='"+value.orderItemId
						+"' data-moduleid='"+ value.id +"' data-isLogined='"+value.isLogined
						+"' data-url='"+location_url+value.url
						+"' class='"+(childMenus.length?"dropdown-toggle":"")
						+" '><i class='menu-icon fa "+(value.iconUrl?value.iconUrl:"fa-list")+"'></i>"
						+"<span class='menu-text'> "+value.menuName+" </span>"
						+"<b class='arrow fa "+(value.iconUrl?value.iconUrl:"fa-list")
						+"'></b></a><b class='arrow'></b>";
					if(childMenus.length){
						var secondMenuHtml="<ul class='submenu'>";
						$.each(childMenus,function(k,v){
							var activeSecond="";
							var childMenus3=getChildMenu(obj,v.id);
							if(v.url && v.url!="#"){
								var vUrl=v.url;
								if(vUrl.indexOf("?")>=0){
									vUrl=vUrl.substring(0,vUrl.indexOf("?"));
								}
								activeSecond=('${uri}'.replace('${projectName}','')==(vUrl)?"active":"");
							}
							if(activeSecond!=""){
								firstMenuHtml=firstMenuHtml.replace("parentActiveCss", "active");
							}
							secondMenuHtml+="<li class='"+activeSecond+" "+(childMenus3.length?"hsub":"")
								+" hover'><a href='javascript:void(0)' data-id='"+v.orderItemId
								+"' data-moduleid='"+ value.id +"' data-isLogined='"+v.isLogined
								+"' data-url='" + location_url
								+v.url+"' class='"+(childMenus3.length?"dropdown-toggle":"")
								+" '><i class='menu-icon fa "+(v.iconUrl?v.iconUrl:"fa-list")+"'></i>"+
								v.menuName+"</a><b class='arrow'></b>";
							
								
							//三级菜单
							
							if(childMenus3.length){
								var thirdMenuHtml="<ul class='submenu'>";
								$.each(childMenus3,function(k3,v3){
									var activethird="";
									
									if(v3.url && v3.url!="#"){
										var vUrl3=v3.url;
										if(vUrl3.indexOf("?")>=0){
											vUrl3=vUrl3.substring(0,vUrl3.indexOf("?"));
										}
										activethird=('${uri}'.replace('${projectName}','')==(vUrl3)?"active":"");
									}
									if(activethird!=""){
										secondMenuHtml=secondMenuHtml.replace("parentActiveCss", "active");
									}
									thirdMenuHtml+="<li class='"+activethird
										+" hover'><a href='javascript:void(0)' data-id='"+v3.orderItemId
										+"' data-moduleid='"+ value.id +"' data-isLogined='"+v3.isLogined
										+"' data-url='"+location_url+v3.url+"'><i class='menu-icon fa "
										+(v3.iconUrl?v3.iconUrl:"fa-list")+"'></i>"+
										v3.menuName+"</a><b class='arrow'></b>";
										
										thirdMenuHtml+="</li>";
								});
								thirdMenuHtml+="</ul>";
								secondMenuHtml+=thirdMenuHtml;
							}
								
							secondMenuHtml+="</li>";
						});
						secondMenuHtml+="</ul>";
						firstMenuHtml+=secondMenuHtml;
					}
					firstMenuHtml+="</li>";
					
					$("#userMenus").append(firstMenuHtml);
					
					
				}
			});
			
			//绑定点击事件
			$.each($("#userMenus").children(),function(key,val){
				//查找子元素
				var that=$(this);
				if(that.find("ul").length){
					var a=that.find("ul li a");
					$(a).each(function(key){
						if($(this).attr("data-url").indexOf("#")==-1){
							$(this).bind("click",function(){
								var me = $(this);
								var dataUrl= me.attr("data-url");
								var dataId= me.attr("data-id");
								var isLoginedVal= me.attr("data-isLogined");
								var dataIsLogined=((isLoginedVal=="null" || isLoginedVal=="") ?false:true);
								var str = dataUrl.indexOf("?") != -1 ? 
									"&orderItemId="+dataId+"&isLogined="+dataIsLogined + "&moduleid=" + me.attr('data-moduleid')
									:
									"?"+"orderItemId="+dataId+"&isLogined="+dataIsLogined + "&moduleid=" + me.attr('data-moduleid');
								str=dataUrl+str;
								$.getJSON(location_url + "/user/updateLoginStatus?ram=" + Math.random()+"&id="+dataId, function(json){
									if(json.success){
										location.href=str;
									}
								});
							});
						}
						
					});
				}else{
					var a=that.find("a")[0];
					$(a).bind("click",function(){
						var me = $(this);
						var dataUrl= me.attr("data-url");
						var dataId= me.attr("data-id");
						var isLoginedVal= me.attr("data-isLogined");
						var dataIsLogined=((isLoginedVal=="null" || isLoginedVal=="") ?false:true);
						var str = dataUrl.indexOf("?")!=-1 ? 
						 	"&orderItemId="+dataId+"&isLogined="+dataIsLogined + "&moduleid=" + me.attr('data-moduleid') 
						 	:
						 	"?"+"orderItemId="+dataId+"&isLogined="+dataIsLogined + "&moduleid=" + me.attr('data-moduleid');
						str=dataUrl+str;
						$.getJSON(location_url + "/user/updateLoginStatus?ram=" + Math.random()+"&id="+dataId, function(json){
							if(json.success){
								location.href=str;
							}
						});
					});
				} 				
			});
			
			$('html').width(getWindowWidth() - 10);
			$('html').width(getWindowWidth() + 10);
		}
	});
});