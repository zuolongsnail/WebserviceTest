package com.valuestudio.web.webservice;

/**
 * 消息类型
 *
 * @author zuolong
 * @version V1.0
 * @title MessageType
 * @description 消息类型
 */
public class MessageType {

    /**
     * 请求成功
     */
    public static final int REQ_SUCCESS = 1;
    /**
     * 请求成功无数据
     */
    public static final int REQ_NODATA = 2;
    /**
     * 请求失败
     */
    public static final int REQ_FAILED = 3;
    /**
     * 请求超时
     */
    public static final int REQ_TIMEOUT = 4;
    /**
     * 系统错误
     */
    public static final int REQ_SERVER_ERROR = 5;
    /**
     * 解析错误
     */
    public static final int REQ_PARSE_ERROR = 6;
    /**
     * 网络连接失败
     */
    public static final int REQ_CONNTECT_FAIL = 7;
}
