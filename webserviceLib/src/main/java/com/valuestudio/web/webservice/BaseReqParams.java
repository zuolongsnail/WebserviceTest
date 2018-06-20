package com.valuestudio.web.webservice;

import java.io.Serializable;

/**
 * 请求参数基类
 *
 * @author zuolong
 * @version V1.0
 * @title BaseReqParams
 * @description 请求参数基类
 * @使用方法 请求参数需要加注解，index值为参数顺序
 */
public class BaseReqParams implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @InjectReqParam(index = 1)
    public String id;
}
