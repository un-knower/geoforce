/*package com.supermap.egispportal.service.impl;


import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispportal.service.IMailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class MaiServiceImplTest {

	@Resource
	private IMailService mailService;
	
	@Test
	public void testSend() {
//		boolean isSendSuccess = this.mailService.send("huanghuasong@supermap.com", "hello", "hello");
		String modulename="网点管理";
		String type="问题反馈";
		String content="十分刻苦精神的发生口角发生的了空间访客撒酒疯" +
				"的购房打算购房的所得税返还的方式速度放缓对方的好过分的合法收入所提供的繁华" +
				"的购房的更多发挥第三方哈的风格的方式的绥芬河的方式" +
				"阿股份的规划的风格撒代理商都提哦二批投票率高达加快该款发动机后热天健康的立法计划" +
				"大功率电力供给口令卡死对头怕热经过考虑重返国家队客服了解放得开两个角度看结果投票二" +
				"电信管理局可多人珀特肉感江东父老可根据阿孙悟空孤苦伶仃肺结核大概看了绝对是快乐日哦价格翻了个空间" +
				"的个空间饿啊日哦特哦我gas大概看了附近的很快就会快乐发达国家额澳盘热裤个角度考虑国家阿大力开发";
		String contactinfo="13608182850";
		String mailcont=formatFeedbackContent(modulename,type,content,contactinfo);
		boolean isSendSuccess = this.mailService.sendHTMLMail("ouyangxiujuan@supermap.com", "意见反馈", mailcont);
		System.out.println(isSendSuccess);
	}
	
	
	public String formatFeedbackContent(String modulename,String type,String content,String contactinfo){
		StringBuilder sb=new StringBuilder();
		sb.append("<html>")
		.append("<body>")
		.append("<table width='60%' align='left' border=1 bordercolor=#000000 style='border-collapse:collapse'>")
		.append("<tr>")
		.append("<td width='15%' style='font-weight: bold;'>功能模块：</td>")
		.append("<td width='85%'>"+modulename+"</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td width='15%' style='font-weight: bold;'>反馈类别：</td>")
		.append("<td width='85%'>"+type+"</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td width='15%' style='font-weight: bold;'>反馈内容：</td>")
		.append("<td width='85%'>"+content+"</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td width='15%' style='font-weight: bold;'>联系方式：</td>")
		.append("<td width='85%'>"+contactinfo+"</td>")
		.append("</tr>")
		.append("</table>")
		.append("</body>")
		.append("</html>");
		return sb.toString();
	}

}
*/