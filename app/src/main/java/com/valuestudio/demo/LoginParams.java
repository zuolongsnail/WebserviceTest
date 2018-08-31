package com.valuestudio.demo;


import com.valuestudio.web.webservice.BaseReqParams;
import com.valuestudio.web.webservice.InjectReqParam;

/**
 * 登录参数类
 */

public class LoginParams extends BaseReqParams {

    @InjectReqParam(index = 1)
    public String userAccount;
    @InjectReqParam(index = 2)
    public String password;
}
