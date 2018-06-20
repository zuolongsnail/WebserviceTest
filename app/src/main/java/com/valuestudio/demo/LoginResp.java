package com.valuestudio.demo;

import com.valuestudio.web.webservice.BaseResp;

import java.io.Serializable;

/**
 * 登录响应类
 */
public class LoginResp extends BaseResp implements Serializable {
    /**
     * uid
     */
    public String uid;
    /**
     * 用户姓名
     */
    public String userName;
    /**
     * 手机
     */
    public String phoneNo;
    /**
     * 性别:男or女
     */
    public String gender;
    /**
     * 头像url
     */
    public String headUrl;

    public LoginResp() {
    }

    public LoginResp(String respId) {
        this.respId = respId;
    }
}
