package com.example.wxdemo.constant;

/**
 * Created by Hyman on 2017/2/27.
 */
public class Constant {

    public static final String DOMAIN = "https://localhost:8080";

    //public static final String APP_ID = "wx74374b7595b9a02a";
    public static final String APP_ID = "wxaebe3ef7a9d6ee72";

    //public static final String APP_SECRET = "5f958e827386540544e4b9a812074f65";
    public static final String APP_SECRET = "44da4d3c8a7116134c68af14c7d2af4a";

    public static final String APP_KEY = "填写自己的";

    public static final String MCH_ID = "填写自己的";  //商户号

    public static final String URL_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String URL_NOTIFY = Constant.DOMAIN + "/ceshi.html";

    public static final String TIME_FORMAT = "yyyyMMddHHmmss";

    public static final int TIME_EXPIRE = 2;  //单位是day

    public static final int TOKEN_EXPIRE=86400;//token过期时间
}
