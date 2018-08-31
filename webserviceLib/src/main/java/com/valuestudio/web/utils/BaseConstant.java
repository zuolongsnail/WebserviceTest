package com.valuestudio.web.utils;

public class BaseConstant {
    /****************************** SharedPreferences key **********************************/
    /**
     * http://
     */
    public static final String HTTP_HEAD = "http://";
    /**
     * 请求地址key
     */
    public static final String KEY_REQUEST_URL = "request_url";
    /**
     * 命名空间key
     */
    public static final String KEY_NAME_SPACE = "name_space";
    /****************************** web request key **********************************/
    /**
     * 请求服务端时报的流关闭的异常信息内容
     */
    public static final String SERVER_BUSY = "BufferedInputStream is closed";
    /**
     * 请求服务端404错误
     */
    public static final String SERVER_404 = "404";
    /**
     * 请求服务端500错误
     */
    public static final String SERVER_500 = "500";
    /**
     * 存在非法字符
     */
    public static final String ILLEGAL_CHAR = "Illegal character";

    /**
     * 系统存储路径
     */
    public static String APP_SD_PATH = "/webservice";
    /**
     * 系统日志保存目录
     */
    public static final String LOG_FILE_DIR = "/log";
}
