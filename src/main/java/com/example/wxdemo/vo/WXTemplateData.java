package com.example.wxdemo.vo;

/**
 * @author lqx
 * @create 2018-08-09 15:17
 */

public class WXTemplateData {

    private String value; // 小标题

    private String color; // 模板内容字体的颜色，不填默认黑色

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
