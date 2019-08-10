package com.example.wxdemo.vo;

import java.util.Map;

/**
 * @author lqx
 * @create 2018-08-09 15:16
 */

public class WXTemplate {
    private String touser;// 接受者（用户）的openid
    private String template_id;// 所需下发的模板消息的id
    private String page;// 点击模板卡片后的跳转页面
    private String form_id;// 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的prepay_id
    private Map<String, WXTemplateData> data; // 模板内容，不填则下发空模板
    private String emphasis_keyword;// 模板需要放大的关键词，不填则默认无放大

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public Map<String, WXTemplateData> getData() {
        return data;
    }

    public void setData(Map<String, WXTemplateData> data) {
        this.data = data;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }

    public void setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
    }
}
