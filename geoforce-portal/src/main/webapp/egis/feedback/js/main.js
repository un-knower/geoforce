$(document).ready(function() {
  function getUrlParam(name) {  
         var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");  
         var r = window.location.search.substr(1).match(reg);  
         if (r!=null)   
             return unescape(r[2]);   
         return null;  
  }
  var key=getUrlParam("key");
	$("#moduleType > ul").find("a").each(function(i,o){
        $(o).click(function(){
          $($("#moduleType > button > span")[0]).text($(o).text());
        })
      });
    $("#sugestionType > ul").find("a").each(function(i,o){
        $(o).click(function(){
          $($("#sugestionType > button > span")[0]).text($(o).text());
        })
      });
    $("#feedbackBtn").click(function(){
    	var content=$("#sugestionContent").val();
    	if(content==""){
    		$("#sugestionContent").focus();
    		return false;
    	}
    	$.ajax({
    		type:"get",
    		url:"/egispportal/feedbackmailService",
    		data:{
          "modulename":$($("#moduleType > button > span")[0]).text(),
          "type":$($("#sugestionType > button > span")[0]).text(),
          "content":content,
          "contactinfo":$("#contactMethod").val(),
          "key":key
          },
        success:function(res){
          var json_res=JSON.parse(res);
          if(json_res.success){
            $("#sugestionContent").val("");
            $("#contactMethod").val("");
            $("#modal_success").modal("show");
            setTimeout(function(){
              $("#modal_success").modal("hide");
            },2000);
          }else{
            $("#modal_fail").modal("show");
            setTimeout(function(){
              $("#modal_fail").modal("hide");
            },2000);
          }
        }
    	});
    });
})