package com.example.wxdemo.controller.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wxdemo.constant.Constant;
import com.example.wxdemo.service.JdbcService;
import com.example.wxdemo.utils.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/wechatApi")
public class WXPayController {


	@Autowired
	private JdbcService jdbcService;

	/**
	 * 下单
	 * @param code
	 * @return
	 */
	@PostMapping("wxPay")
	public ResultData order(String code,HttpServletRequest request){
		String openid=getOpenId(code);
		if(StringUtils.isBlank(openid)) {
			return ResultData.build(202,"获取到openId为空");
		} else {
			String clientIP = CommonUtil.getClientIp(request);//获取终端ip
			String randomNonceStr = RandomUtil.generateStr(32);//生成32位随机字符串
			Map<String,String> prepayIdAndPaySign = unifiedOrder(openid, clientIP, randomNonceStr);//调用统一下单接口获取预支付id
			if(prepayIdAndPaySign==null) {
				return ResultData.build(202,"出错了，未获取到prepayId");
			} else {
                prepayIdAndPaySign.put("nonceStr", randomNonceStr);
				return ResultData.build(200,"获取成功",prepayIdAndPaySign);
			}
		}
	}


    private String getOpenId(String code){
		String apiUrl="https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.APP_ID+"&secret="+Constant.APP_SECRET+"&js_code="+code+"&grant_type=authorization_code";
		System.out.println(apiUrl);
		String responseBody = HttpClientUtil.doGet(apiUrl);
		System.out.println(responseBody);
		JSONObject jsonObject = JSON.parseObject(responseBody);
		if(StringUtils.isNotBlank(jsonObject.getString("openid"))&&StringUtils.isNotBlank(jsonObject.getString("session_key"))){
			return jsonObject.getString("openid");
		}else{
			return "";
		}
	}

	/**
	 * 调用统一下单接口获取预支付id
	 * @param openId
	 * @param clientIP
	 * @param randomNonceStr
	 * @return
	 */
	private Map<String,String> unifiedOrder(String openId, String clientIP, String randomNonceStr) {
		String url = Constant.URL_UNIFIED_ORDER;//同一订单接口地址
		PayInfo payInfo = createPayInfo(openId, clientIP, randomNonceStr);
		try {
			String md5 = getSign(payInfo);
			String xml = CommonUtil.payInfoToXML(payInfo);
			System.out.println(xml);
			xml = xml.replace("__", "_").replace("<![CDATA[1]]>", "1");
			System.out.println(xml);

			StringBuffer buffer = HttpClientUtil.httpsRequest(url, "POST", xml);
			System.out.println("unifiedOrder request return body:"+buffer.toString());
			Map<String, String> result = CommonUtil.parseXml(buffer.toString());
			String return_code = result.get("return_code");
			if(StringUtils.isNotBlank(return_code) && return_code.equals("SUCCESS")) {

				String return_msg = result.get("return_msg");
				if(StringUtils.isNotBlank(return_msg) && !return_msg.equals("OK")) {
					//log.error("统一下单错误！");
					return null;
				}

				String prepay_Id = result.get("prepay_id");
                Map<String,String> resultMap=new HashMap<>();
                resultMap.put("prepayId",prepay_Id);
                resultMap.put("paySign",md5);
				return resultMap;

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		return null;
	}


	private PayInfo createPayInfo(String openId, String clientIP, String randomNonceStr) {

		Date date = new Date();
		String timeStart = TimeUtils.getFormatTime(date, Constant.TIME_FORMAT);
		String timeExpire = TimeUtils.getFormatTime(TimeUtils.addDay(date, Constant.TIME_EXPIRE), Constant.TIME_FORMAT);

		String randomOrderId = CommonUtil.getRandomOrderId();

		PayInfo payInfo = new PayInfo();
		payInfo.setAppid(Constant.APP_ID);
		payInfo.setMch_id(Constant.MCH_ID);
		payInfo.setDevice_info("WEB");
		payInfo.setNonce_str(randomNonceStr);
		payInfo.setSign_type("MD5");  //默认即为MD5
		payInfo.setBody("JSAPI支付测试");
		payInfo.setAttach("支付测试4luluteam");
		payInfo.setOut_trade_no(randomOrderId);
		payInfo.setTotal_fee(1);
		payInfo.setSpbill_create_ip(clientIP);
		payInfo.setTime_start(timeStart);
		payInfo.setTime_expire(timeExpire);
		payInfo.setNotify_url(Constant.URL_NOTIFY);
		payInfo.setTrade_type("JSAPI");
		payInfo.setLimit_pay("no_credit");
		payInfo.setOpenid(openId);

		return payInfo;
	}

	private String getSign(PayInfo payInfo) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("appid=" + payInfo.getAppid())
				.append("&attach=" + payInfo.getAttach())
				.append("&body=" + payInfo.getBody())
				.append("&device_info=" + payInfo.getDevice_info())
				.append("&limit_pay=" + payInfo.getLimit_pay())
				.append("&mch_id=" + payInfo.getMch_id())
				.append("&nonce_str=" + payInfo.getNonce_str())
				.append("&notify_url=" + payInfo.getNotify_url())
				.append("&openid=" + payInfo.getOpenid())
				.append("&out_trade_no=" + payInfo.getOut_trade_no())
				.append("&sign_type=" + payInfo.getSign_type())
				.append("&spbill_create_ip=" + payInfo.getSpbill_create_ip())
				.append("&time_expire=" + payInfo.getTime_expire())
				.append("&time_start=" + payInfo.getTime_start())
				.append("&total_fee=" + payInfo.getTotal_fee())
				.append("&trade_type=" + payInfo.getTrade_type())
				.append("&key=" + Constant.APP_KEY);

		System.out.println("排序后的拼接参数：" + sb.toString());

		return CommonUtil.getMD5(sb.toString().trim()).toUpperCase();
	}
}
