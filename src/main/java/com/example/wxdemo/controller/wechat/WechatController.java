package com.example.wxdemo.controller.wechat;

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
@RequestMapping("/wechatApi")
public class WechatController {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private JdbcService jdbcService;

	/**
	 * d登录接口
	 * @param encryptedData
	 * @param iv
	 * @param code
	 * @return
	 */
	@PostMapping("/onLogin")
	public ResultData login(String encryptedData, String iv, String code) {
		if(!StringUtils.isNotBlank(code)){
			return ResultData.build(202,"未获取到用户凭证code");
		}
		String apiUrl="https://api.weixin.qq.com/sns/jscode2session?appid="+ Constant.APP_ID+"&secret="+Constant.APP_SECRET+"&js_code="+code+"&grant_type=authorization_code";
		System.out.println(apiUrl);
		String responseBody = HttpClientUtil.doGet(apiUrl);
		System.out.println(responseBody);
		JSONObject jsonObject = JSON.parseObject(responseBody);
		if(StringUtils.isNotBlank(jsonObject.getString("openid"))&&StringUtils.isNotBlank(jsonObject.getString("session_key"))){
			//解密获取用户信息
			JSONObject userInfoJSON=WechatGetUserInfoUtil.getUserInfo(encryptedData,jsonObject.getString("session_key"),iv);
			if(userInfoJSON!=null){
				//这步应该set进实体类
				Map userInfo = new HashMap();
				userInfo.put("openId", userInfoJSON.get("openId"));
				userInfo.put("nickName", userInfoJSON.get("nickName"));
				userInfo.put("gender", userInfoJSON.get("gender"));
				userInfo.put("city", userInfoJSON.get("city"));
				userInfo.put("province", userInfoJSON.get("province"));
				userInfo.put("country", userInfoJSON.get("country"));
				userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
				// 解密unionId & openId;
				if (userInfoJSON.get("unionId")!=null) {
					userInfo.put("unionId", userInfoJSON.get("unionId"));
				}
				//然后根据openid去数据库判断有没有该用户信息，若没有则存入数据库，有则返回用户数据
				Map<String,Object> dataMap = new HashMap<>();
				dataMap.put("userInfo", userInfo);
				String uuid=UUID.randomUUID().toString();
				dataMap.put("WXTOKEN", uuid);
				redisTemplate.opsForValue().set(uuid,userInfo);
				redisTemplate.expire(uuid,Constant.TOKEN_EXPIRE,TimeUnit.SECONDS);
				return ResultData.build(200,"登陆成功",dataMap);
			}else{
				return ResultData.build(202,"解密失败");
			}
		}else{
			return ResultData.build(202,"未获取到用户openid 或 session");
		}
	}




	@PostMapping("/doLogin")
	public ResultData doLogin(String code) {
		if(!StringUtils.isNotBlank(code)){
			return ResultData.build(202,"未获取到用户凭证code");
		}
		String apiUrl="https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.APP_ID+"&secret="+Constant.APP_SECRET+"&js_code="+code+"&grant_type=authorization_code";
		System.out.println(apiUrl);
		String responseBody = HttpClientUtil.doGet(apiUrl);
		System.out.println(responseBody);
		JSONObject jsonObject = JSON.parseObject(responseBody);
		if(StringUtils.isNotBlank(jsonObject.getString("openid"))&&StringUtils.isNotBlank(jsonObject.getString("session_key"))){
			//生成token并添加到缓存
			String uuid=UUID.randomUUID().toString();
			redisTemplate.opsForValue().set(uuid,jsonObject.getString("openid")+"&&"+jsonObject.getString("session_key"));
			redisTemplate.expire(uuid,Constant.TOKEN_EXPIRE,TimeUnit.SECONDS);
			//然后根据openid去数据库判断有没有该用户信息，若没有则存入数据库，有则返回用户数据(这里由于没有数据库所以一致没有)
			boolean hasUserInfo=false;
			Map<String,Object> dataMap = new HashMap<>();
			if(!hasUserInfo){
				dataMap.put("userInfo", null);
			}else{
				dataMap.put("userInfo", "userInfo");
			}
			dataMap.put("WXTOKEN", uuid);
			return ResultData.build(200,"登陆成功",dataMap);
		}else{
			return ResultData.build(202,"未获取到用户openid 或 session");
		}
	}

	@PostMapping("/updateUserInfoLogin")
	public ResultData updateUserInfoLogin(String encryptedData, String iv, String token) {
		String[] openid_sessionKey=redisTemplate.opsForValue().get(token).toString().split("&&");
		if(openid_sessionKey!=null&&openid_sessionKey.length>0){
			//解密获取用户信息
			JSONObject userInfoJSON=WechatGetUserInfoUtil.getUserInfo(encryptedData,openid_sessionKey[1],iv);
			if(userInfoJSON!=null){
				//这步应该set进实体类
				Map userInfo = new HashMap();
				userInfo.put("openId", userInfoJSON.get("openId"));
				userInfo.put("nickName", userInfoJSON.get("nickName"));
				userInfo.put("gender", userInfoJSON.get("gender"));
				userInfo.put("city", userInfoJSON.get("city"));
				userInfo.put("province", userInfoJSON.get("province"));
				userInfo.put("country", userInfoJSON.get("country"));
				userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
				// 解密unionId & openId;
				if (userInfoJSON.get("unionId")!=null) {
					userInfo.put("unionId", userInfoJSON.get("unionId"));
				}
				//然后根据openid去数据库判断有没有该用户信息，若没有则存入数据库，有则返回用户数据
				Map<String,Object> dataMap = new HashMap<>();
				dataMap.put("userInfo", userInfo);
				return ResultData.build(200,"登陆成功",dataMap);
			}else{
				return ResultData.build(202,"解密失败");
			}
		}else{
			return ResultData.build(202,"未获取到用户openid 或 session");
		}
	}



	/**
	 * 验证登录
	 * @param wxToken
	 * @return
	 */
	@GetMapping("/validateLogin")
	public ResultData validateToken(String wxToken){
		JSONObject obj=(JSONObject)redisTemplate.opsForValue().get(wxToken);
		if(obj!=null){
			redisTemplate.opsForValue().set(wxToken,obj);
			redisTemplate.expire(wxToken,Constant.TOKEN_EXPIRE,TimeUnit.SECONDS);
			return ResultData.ok();
		}
		return ResultData.build(202,"token过期");
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

	/**
	 * 发送麽办消息测试（失败）
	 * @param wxTemplate
	 * @return
	 */
	@PostMapping("pushMessageTest")
	public ResultData pushMessageTest(@RequestBody WXTemplate wxTemplate) {

		String jsonMsg = JSON.toJSONString(wxTemplate);
		Map<String, String> param = new HashMap<>();
		param = JsonUtils.jsonToPojo(jsonMsg, param.getClass());
		String accestoken = "12_w7A6iJhx25DvceUmrL0zFtXfA8hTkxGrfOhRm7XMmomlnPZ13PbWKxaBV8E5NX8O5eQVTwpsNIpswDsIICqr1ldvyDuFea26VCJ_FBKquFnOlN6qi3CYadYNpxLlub5jFIZBjK66V8-IDSTUZSPjAGACVS";
		String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + accestoken;
		try {
			String resp = HttpClientUtil.doPost(url, param);
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResultData.ok();
	}

}
