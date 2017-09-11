<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
	request.setAttribute("ctx", basePath);
%>
<html>
<head>
<title>个人资料</title>
<meta name="description" content="地图慧-企业可视化管理平台" />
<meta name="keywords" content="地图慧-企业可视化管理平台" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- page specific plugin styles -->

<!-- page specific plugin scripts -->
<script src="${ctx}/resources/assets/js/bootbox.min.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.min.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.message.cn.js"></script>
<script src="${ctx}/resources/js/sys/util.js"></script>
<script type="text/javascript" >
	var PROJECT_URL='${ctx}';
	function update(){
		var validater=$("#infoForm").validate({
			onsubmit:true,
			onfocusout:true,
	        submitHandler:function(form){
	        	$.ajax({
	    			type : "POST",
	    			url : PROJECT_URL+"/user/update/pass?ram=" + Math.random() ,
	    			data : $("#infoForm").serializeArray(),
	    			success : function(obj) {
	    				if (obj.flag == "ok") {
	    					bootbox.alert("操作成功!",function(){
	    						window.location.reload();
	    					});
	    				} else {
	    					bootbox.alert(obj.info);
	    				}
	    			}
	    		});
	        },
	        errorPlacement: function(error, element) {
        	   if (element.attr('type') === 'radio') {
        	       error.insertAfter($("input[name='sexHidden']"));
        	   }else {
        	       error.insertAfter(element);
        	   }
        	}
	    }); 
		$("#infoForm").trigger("submit");
		if(!validater.valid()){
			return false;
		}
		
	}
	$(function(){
		$("#updateInfo").on("click",update);
	});
</script>
<style type="text/css">
table tr td {
padding: 8px 8px 8px 8px;
}
.star {
color: red;
}
</style>

</head>

<body>
 
<!-- PAGE CONTENT BEGINS -->
<div class="row"  style="text-align: center;"> 
	<div class="col-xs-12  " style="font-size: 20px;" >
		<form id="infoForm">
		<table style="width: 100%;height: 100%;margin-top: 20px;">
			<tr>
				<td style="width: 40%;text-align: right;">(<label class="star">*</label>)旧密码：</td>
				<td style="text-align:left;">
				<input type="password" id="oldPass" name="oldPass" placeholder="旧密码" class="required" minlength="5" maxlength="15"/>
				</td>
			</tr>
			<tr>
				<td style="width: 40%;text-align: right;">(<label class="star">*</label>)新密码：</td>
				<td style="text-align:left;">
				<input type="password" id="newPass" name="newPass" placeholder="新密码" class="required" minlength="5" maxlength="15"/>
				</td>
			</tr>
			<tr>
				<td style="width: 40%;text-align: right;">(<label class="star">*</label>)确认密码：</td>
				<td style="text-align:left;">
				<input type="password" id="check_password" name="check_password" placeholder="确认密码" class="required" minlength="5" maxlength="15" equalTo="#newPass"/>
				</td>
			</tr>
			<tr>
				<td style="width: 40%;text-align: right;"></td>
				<td style="text-align:left;">
					<button id="updateInfo" type="button" class="btn btn-sm btn-success">
					   <i class="ace-icon fa fa-check"></i>
					   提交
					</button>
				</td>
			</tr>
		</table>
		</form>
	</div>
</div>

<!-- PAGE CONTENT ENDS -->
</body>
</html>
