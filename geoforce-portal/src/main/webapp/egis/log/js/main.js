var pageCount=1;
var pageSize=10;
var pageNo=0;
$(document).ready(function() {
	getLog();
	window.onscroll=function(){
		if($(window).scrollTop()+$(window).height() > $(".footer").offset().top-320){
			getLog();
		}
	}
})
function getLog(){
	pageNo++;
	if(pageNo>pageCount || pageCount>50){
		return false;
	}
    $.ajax({
        type:"GET",
        url:"/egispportal/sysUpdateLogService?method=queryLogList",
        data:{
          "pageSize":pageSize,
          "pageNo":pageNo,
          "btime":"",
          "etime":"",
          "versionname":""
          },
        dataType: 'json',
        success:function(e){
        	if(e && e.success){
            var logHtml="";
            if( !e.result || !e.result.records || e.result.records.length === 0 ) {
              logHtml='<div class="no-logs">暂无日志更新</div>';
              $("#list-log").html(logHtml);
              return;
            }
        	  var data=e.result.records;
			  pageCount=parseInt(e.result.totalPages);
            var len=data.length;
        	  for(var i=0;i<len;i++){
        	    logHtml+='<ul class="list-row '+(pageNo==1?"":"append-row")+'">'+
        	                '<li class="list-title">'+data[i].onlineTime+'/'+
        	                  '<span class="red">'+data[i].versionName+'</span>'+
        	                '</li>';
              if(data[i].newFunctions!=""){
                logHtml+='<li class="list-item"><div class="list-item-logo"><span class="log-new">NEW</span></div>'+data[i].newFunctions+
                          '</li>';
              }
              if(data[i].improveFunctions!=""){
                logHtml+='<li class="list-item"><div class="list-item-logo"><span class="log-improve">IMPROVE</span></div>'+data[i].improveFunctions+
                          '</li>';
              }
              if(data[i].repairBugs!=""){
                logHtml+='<li class="list-item"><div class="list-item-logo"><span class="log-fix">FIX</span></div>'+data[i].repairBugs+
                          '</li>';
              }
        	    logHtml+='</ul>';
        	  }
			  $("#list-log").append(logHtml);
			  $(".append-row").css("opacity","1");
        	}
        }
    });
  }