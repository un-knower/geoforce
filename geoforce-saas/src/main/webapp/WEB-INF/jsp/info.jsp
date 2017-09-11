<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
	request.setAttribute("ctx", basePath);
	request.setAttribute("user2", request.getSession().getAttribute("user"));
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
	var currentModuleId = '';
	function getUrlArgs(){
	    var url = location.search; 
	    var theRequest = {};
	    if (url.indexOf("?") != -1) {
	        var str = url.substr(1);
	        strs = str.split("&");
	        for ( var i = 0; i < strs.length; i++) {
	            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
	        }
	    } 
	    else {
	        theRequest = null;
	    }
	    return theRequest;    
	}
	var PROJECT_URL='${ctx}';
	function update(){
		var validater=$("#infoForm").validate({
			onsubmit:true,
			onfocusout:true,
			rules: {
				sex: {  
			        required: true
			    }
			},
	        submitHandler:function(form){
	        	$.ajax({
	    			type : "POST",
	    			url : PROJECT_URL+"/user/update/info?ram=" + Math.random() + "&moduleId=" + currentModuleId,
	    			data : $("#infoForm").serializeArray(),
	    			success : function(obj) {
	    				if (obj.flag == "ok") {
	    					bootbox.alert("操作成功!",function(){
	    						window.location.reload();
	    					});
	    				} else {
	    					bootbox.alert("操作失败!");
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
		var param_location = getUrlArgs();
		if(param_location && param_location.moduleid) {
			currentModuleId = param_location.moduleid;
		}
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
				<td style="width: 40%;text-align: right;">公司名称：</td>
				<td style="text-align:left;">${user2.eid.name }
				<input id="id" name="id" type="hidden" value="${user2.id }" >
				<input id="dept" name="dept" type="hidden" value="${user2.deptId.id }" >
				<input id="username3" name="username3" type="hidden" value="${user2.username }" >
				</td>
			</tr>
			<tr>
				<td style="width: 40%;text-align: right;">(<label class="star">*</label>)联系人姓名：</td>
				<td style="text-align:left;">
				<input value="${user2.realname }" type="text" id="realname" name="realname" placeholder="联系人姓名" class="required" minlength="2" maxlength="8"/>
				</td>
			</tr>
			<tr style="display: none;">
				<td style="width: 40%;text-align: right;">部门：</td>
				<td style="text-align:left;">${user2.deptId.name }</td>
			</tr>
			<tr style="display: none;">
				<td style="width: 40%;text-align: right;">(<label class="star">*</label>)性别：</td>
				<td style="text-align:left;">
				<label >
					<c:set var="sexString"><c:out value="${user2.sex}" /></c:set> 
					<input id="sexm" name="sex" type="radio" value="1" class="ace"  <c:if test="${sexString=='1' }">checked="checked"</c:if>  />
					<span class="lbl" > 男</span>
				</label>
				<label>
					<input id="sexf" name="sex" type="radio" value="0" class="ace"  <c:if test="${sexString=='0' }">checked="checked"</c:if>  />
					<span class="lbl"> 女</span>
				</label>
				</td>
			</tr>
			<tr>
				<td style="width: 40%;text-align: right;">(<label class="star">*</label>)手机号码：</td>
				<td style="text-align:left;">
				<input value="${user2.mobilephone }" type="text" id="mobilephone2" name="mobilephone2" placeholder="手机号码" class="required" minlength="11" maxlength="11"/>
				</td>
			</tr>
			<tr style="display: none;">
				<td style="width: 40%;text-align: right;">(<label class="star">*</label>)电子邮箱：</td>
				<td style="text-align:left;">
				<input value="${user2.email }" type="text" id="email" name="email" placeholder="电子邮箱" class="required" minlength="5" maxlength="32"/>
				</td>
			</tr>
			<tr>
				<td style="width: 40%;text-align: right;">(<label class="star">*</label>)QQ：</td>
				<td style="text-align:left;">
				<input value="${user2.qq }" type="text" id="qq" name="qq" placeholder="QQ" class="required" minlength="5" maxlength="16"/>
				</td>
			</tr>
			<tr style="display: none;">
				<td style="width: 40%;text-align: right;">联系地址：</td>
				<td style="text-align:left;">
				<input value="${user2.address }" type="text" id="address" name="address" placeholder="联系地址"  minlength="5" maxlength="20"/>
				</td>
			</tr>
			<tr style="display: none;">
				<td style="width: 40%;text-align: right;">邮政编码：</td>
				<td style="text-align:left;">
				<input value="${user2.zipCode }" type="text" id="zipcode" name="zipcode" placeholder="邮政编码"  minlength="5" maxlength="10"/>
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
