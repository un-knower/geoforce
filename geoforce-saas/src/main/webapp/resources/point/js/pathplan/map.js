$(function(){	
	var param = urls.getUrlArgs();
	if(!param || !param.id || !param.index ){
		return;
	}
	param.index = Number(param.index);
	$('.path-title').html('路线'+(param.index+1));
	$.ajax({
		type : "POST",
		url : urls.server+"/path/result?ram=" + Math.random() + "&id=" + param.id,
		success : function(currentTaskObj) {
			var total = Number(eval(currentTaskObj.pathGuides)[param.index].length), data_times = eval(currentTaskObj.pathWeights);
			total = total > 10 ? total/1000 : total;
			total = total.toFixed(2);
			$('.path-length').html('总里程: ' + total + '公里' );
			$('.path-time').html('总时间: ' + data_times[param.index].toFixed(1) + '分钟');

			var data = eval(currentTaskObj.stopIndexes)[param.index];
			var data_times = eval(currentTaskObj.pathWeights);
			var data_stop_times = eval(currentTaskObj.stopTimes)[param.index];
			var len = data.length;			
			$('.path-title').html('路线' + (param.index +1 ) + ' (共'+ (len-2) +'单)' );


			$.ajax({
				type : "GET",
				url : urls.server + "/orderService/queryByIds?ram=" + Math.random(),
				data:{"orderIds": currentTaskObj.orderIds},
				success : function(e) {
					if(!e || !e.isSuccess || !e.result || e.result.length==0) {
						return;
					}
					var data_address = e.result, h = '';
					var leng = data.length - 1;
					for(var i=1; i<leng; i++) {
						var k = data[i], item = data_address[k] ;
						h += '<tr>';
						h += '	<td>'+ i +'</td>';
						h += '	<td>'+ item.number +'</td>';
						h += '	<td>'+ item.address +'</td>';
						h += '	<td>'+ item.startTime +'</td>';
						h += '	<td>'+ item.endTime +'</td>';
						h += '	<td>'+ data_stop_times[i] +'</td>';
						h += '</tr>';
					}
					$('tbody').html(h);
				}
			});
		}
	});
});