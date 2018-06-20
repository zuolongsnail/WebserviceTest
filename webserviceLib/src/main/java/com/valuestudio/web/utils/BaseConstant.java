package com.valuestudio.web.utils;

public class BaseConstant {
    /****************************** SharedPreferences key **********************************/
    /**
     * http://
     */
    public static final String HTTP_HEAD = "http://";
    /**
     * 主机ip地址key
     */
    public static final String KEY_HOST_IP = "host_ip";
    /**
     * 端口号key
     */
    public static final String KEY_HOST_PORT = "host_port";
    /**
     * 主机名称key
     */
    public static final String KEY_HOST_NAME = "host_name";
    /**
     * 命名空间key
     */
    public static final String KEY_NAME_SPACE = "name_space";
    /****************************** web request key **********************************/
    /**
     * 每页记录数
     */
    public static final int PAGE_SIZE = 10;
    /**
     * 分页条件key
     */
    public static final String KEY_PAGE_STR = "pageStr";
    /**
     * 分页条件
     */
    public static final String KEY_PAGE_STR_FORMAT = "pageNo:%1s;pageSize:%2s;";
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
