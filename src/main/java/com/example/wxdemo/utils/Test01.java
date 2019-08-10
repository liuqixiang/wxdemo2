package com.example.wxdemo.utils;

/**
 * @author lqx
 * @create 2018-08-06 16:08
 */

public class Test01 {

    public static void main(String[] args) {
        String apiUrl2=String.format("aaa%sbb%scc%s","哈哈","深度","方法");
        System.out.println(apiUrl2);

        String t="sdfjskf";
        System.out.println(t.indexOf("s"));
        String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx74374b7595b9a02a&secret=5f958e827386540544e4b9a812074f65";
        String s = HttpClientUtil.doGet(url);
        System.out.println(s);

    }



}
