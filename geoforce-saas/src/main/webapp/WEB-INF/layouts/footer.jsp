<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>





								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content-area -->
				</div><!-- /.page-content -->
			</div><!-- /.main-content -->

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
		</div><!-- /.main-container -->

<script type="text/javascript">
  	//8a04a77b54effacc0154ff4ea9cb0121  湖南省金融扶贫服务站电子地图
	if(${userid == '8a04a77b54effacc0154ff4ea9cb0121'}) {
		document.title = "湖南省金融扶贫服务站电子地图";
		var h =  '<img class="boc" src="${ctx}/resources/images/logo/boc.png">';
			h += '<span class="red">湖南省金融扶贫服务站电子地图</span>';
		$('#header-logo')
		.attr('href', "javascript:;")
		.html(h);
		$('.bottom-logo').removeClass('hide');
	}
</script>
		