package com.example.wxdemo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义响应结构
 */
public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 将json结果集转化为对象
     * 
     * @param jsonData json数据
     * @param clazz 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    /**
	 * 将对象的null替换为"",日期格式化为年-月-日
	 * @param obj
	 * @return
	 */
	public static JSONObject getJsonResult(Object obj){
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String str2 = JSONObject.toJSONString(obj,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.WriteNullListAsEmpty);  
		//str2=str2.replace("null", "\"\"");
		JSONObject res=JSON.parseObject(str2);
		return res;
	}
	public static JSONArray getJsonResultList(Object obj){
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String str2 = JSONObject.toJSONString(obj,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.WriteNullListAsEmpty);  
		str2=str2.replace("null", "\"\"");
		JSONArray res=JSON.parseArray(str2);
		return res;
	}
    public static void main(String[] args) {
		/*String json="{\"usermesss\":[{\"price\":\"0对方水电费\",\"country\":\"中国\",\"p_port\":\"水电费水电费\",\"creator\":\"是的发送到发送到\",\"p_plant\":\"水电费是否\",\"uid\":\"c40141ff1e5f40d2a24220bc3dcddde5\"}]}";
		Map<String,List<Map>> map=JsonUtils.jsonToPojo(json,Map.class);
		map.forEach((k,v)->{
			System.out.println(k+":"+v.toString());
		});
		System.out.println(map.get("usermesss").get(0).get("uid"));*/
    	 Map < String , Object > jsonMap = new HashMap< String , Object>();  
    	 jsonMap.put("a",1);  
    	 jsonMap.put("b","");  
    	 jsonMap.put("c",null);  
    	 jsonMap.put("d","wuzhuti.cn");
    	List<Map < String , Object >> list=new ArrayList<>();
    	list.add(jsonMap);
    	
    	System.out.println(getJsonResultList(list));
    	
	}
}
