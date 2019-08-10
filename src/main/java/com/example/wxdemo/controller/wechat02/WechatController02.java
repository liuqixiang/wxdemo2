package com.example.wxdemo.controller.wechat02;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wxdemo.constant.Constant;
import com.example.wxdemo.service.JdbcService;
import com.example.wxdemo.utils.*;
import com.example.wxdemo.vo.WXTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/wechatApi02")
public class WechatController02 {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private JdbcService jdbcService;

	@PostMapping("/getToken")
	public Map<String,Object> getToken(@RequestParam String code) {
		Map<String,Object> resultMap=new HashMap<>();
		String apiUrl="https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.APP_ID+"&secret="+Constant.APP_SECRET+"&js_code="+code+"&grant_type=authorization_code";
		String responseBody = HttpClientUtil.doGet(apiUrl);
		System.out.println(responseBody);
		JSONObject jsonObject = JSON.parseObject(responseBody);
		if(StringUtils.isNotBlank(jsonObject.getString("openid"))&&StringUtils.isNotBlank(jsonObject.getString("session_key"))){
			//生成token并添加到缓存
			String uuid=UUID.randomUUID().toString();
			redisTemplate.opsForValue().set(uuid,jsonObject.getString("openid")+"&&"+jsonObject.getString("session_key"));
			redisTemplate.expire(uuid,Constant.TOKEN_EXPIRE,TimeUnit.SECONDS);
			resultMap.put("token",uuid);
		}
		return resultMap;
	}

	/**
	 * 验证登录
	 * @param wxToken
	 * @return
	 */
	@GetMapping("/validateToken")
	public Map<String,Object> validateToken(String wxToken){
		Map<String,Object> resultMap=new HashMap<>();
		JSONObject obj=(JSONObject)redisTemplate.opsForValue().get(wxToken);
		if(obj!=null){
			redisTemplate.opsForValue().set(wxToken,obj);
			redisTemplate.expire(wxToken,Constant.TOKEN_EXPIRE,TimeUnit.SECONDS);
			resultMap.put("isValid",true);
		}else{
			resultMap.put("isValid",false);
		}
		return resultMap;
	}

	/**
	 * 获取列表数据
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@PostMapping("getContentList")
	public ResultData getContentExt(Integer pageNum,Integer pageSize,String keyword){
		Map<String,Object> resultMap=new HashMap<>();
		Integer start=(pageNum-1)*pageSize;
		List<Map<String, Object>> mapList = jdbcService.selectContentExt(start, pageSize,keyword);
		resultMap.put("pageData",mapList);
		Integer count = jdbcService.selectCount(keyword);
		PageUtil pageUtil=new PageUtil(pageNum,pageSize,count);
		resultMap.put("pageInfo",pageUtil);
		return ResultData.build(200,"获取成功",resultMap);
	}

	@PostMapping("getContentTxt/{contentId}")
	public ResultData getContentTxt(@PathVariable Integer contentId){
		Map<String, Object> contentByContentId = jdbcService.getContentByContentId(contentId);
		System.out.println(contentId);
		return ResultData.build(200,"获取数据成功",contentByContentId);
	}


}
