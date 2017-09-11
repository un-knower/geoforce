package com.supermap.egispweb.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.entity.PointExtcolEntity;
import com.supermap.egispservice.base.entity.PointExtcolValEntity;


public class FieldMap {
	
	public Map<String,Object> ChangeObjectToMap(Object object){
		if(object==null){
			return null;
		}
		else{
			Map<String,Object> map=new HashMap<String,Object>();
			Class oclass=object.getClass();
			Field fields[]=oclass.getFields();
			try {
				for(Field field:fields){
					if(field.get(object)==null||field.get(object).equals("")){
						continue;
					}
					else {
						map.put(field.getName(), field.get(object));
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}
	}
	
	/**
	 * 将map的value值转换成数组
	 * @param map
	 * @return
	 * @Author Juannyoh
	 * 2015-8-25上午11:15:31
	 */
	public String[] ChangMapToStringArray(Collection collection){
		String result[]=new String[collection.size()];
		collection.toArray(result);
		return result;
	}
	
	
	
	/**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */ 
    @SuppressWarnings({ "rawtypes", "unchecked" }) 
    public static Map convertBean(Object bean) 
            { 
    	Class type = bean.getClass(); 
        Map returnMap = new HashMap(); 
    	try{
    		BeanInfo beanInfo = Introspector.getBeanInfo(type); 
            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
            for (int i = 0; i< propertyDescriptors.length; i++) { 
                PropertyDescriptor descriptor = propertyDescriptors[i]; 
                String propertyName = descriptor.getName(); 
                if (!propertyName.equals("class")) { 
                    Method readMethod = descriptor.getReadMethod(); 
                    Object result = readMethod.invoke(bean, new Object[0]); 
                    if (result != null) { 
                        returnMap.put(propertyName, result); 
                    } else { 
                        returnMap.put(propertyName, ""); 
                    } 
                } 
            } 
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
        return returnMap; 
    } 

    
/**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InstantiationException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */ 
    @SuppressWarnings("rawtypes") 
    public static Object convertMap(Class type, Map map) 
           { 
    	Object obj=null;
        try{
        	BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性 
            obj = type.newInstance(); // 创建 JavaBean 对象 
        	// 给 JavaBean 对象的属性赋值 
            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
            for (int i = 0; i< propertyDescriptors.length; i++) { 
                PropertyDescriptor descriptor = propertyDescriptors[i]; 
                String propertyName = descriptor.getName(); 
     
                if (map.containsKey(propertyName)) { 
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。 
                    Object value = map.get(propertyName); 
     
                    Object[] args = new Object[1]; 
                    args[0] = value; 
     
                    descriptor.getWriteMethod().invoke(obj, args); 
                } 
            } 
        }
        catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
        return obj; 
    } 
	
}
