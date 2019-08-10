package com.example.wxdemo.utils;

import java.io.Serializable;

/**
 * Created by Hyman on 2017/2/28.
 */
public class PayInfo implements Serializable {

    private String appid;                   //微信分配的小程序ID 必填
    private String mch_id;                  // 	微信支付分配的商户号 必填
    private String device_info;             //设备号，小程序传"WEB" 可不填
    private String nonce_str;               //随机字符串，长度要求在32位以内。 必填
    private String sign;                    //通过签名算法计算得出的签名值， 必填
    private String sign_type;               //签名类型 默认为MD5，支持HMAC-SHA256和MD5。 必填
    private String body;                    //商品简单描述，该字段请按照规范传递 必填
    //private String detail;                //商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传 可不填
    private String attach;                  //附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。 可不填
    private String out_trade_no;            //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一 必填
    private int total_fee;                  //订单总金额，单位为分 必填
    private String spbill_create_ip;        //APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP 必填
    private String time_start;              //订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010 可不填
    private String time_expire;             //订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。
    private String notify_url;              //异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url 必填
    private String trade_type;              //交易类型,JSAPI
    private String limit_pay;               //指定支付方式，no_credit 上传此参数no_credit--可限制用户不能使用信用卡支付
    private String openid;                  //用户标识

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getLimit_pay() {
        return limit_pay;
    }

    public void setLimit_pay(String limit_pay) {
        this.limit_pay = limit_pay;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
