package com.supermap.egispboss.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;

/**
 * 
 * @description 解决Date类型返回json格式为自定义格式
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-6-26
 * @version 1.0
 */
public class SMObjectMapper extends ObjectMapper {

	public SMObjectMapper() {
		CustomSerializerFactory factory = new CustomSerializerFactory();
		factory.addGenericMapping(Date.class, new JsonSerializer<Date>() {
			@Override
			public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				jsonGenerator.writeString(sdf.format(value));
//				String str=sdf.format(value);
//				if (str.endsWith(" 00:00:00")) {
//					jsonGenerator.writeString(str.replace(" 00:00:00", ""));
//				}else {
//					jsonGenerator.writeString(str);
//				}
			}
		});
		this.setSerializerFactory(factory);
	}
}