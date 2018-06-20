package com.valuestudio.web.webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求响应基类
 *
 * @author zuolong
 * @version V1.0
 * @title BaseResp
 * @description 请求参数基类
 */
public class BaseResp {
    /**
     * 请求是否成功
     */
    public boolean result;
    /**
     * 请求结果信息
     */
    public String resultInfo;
    /**
     * 总页数
     */
    public int pageSum;
    /**
     * 总记录数
     */
    public int itemTotal;
    /**
     * 列表数据
     */
    public List list;
    /**
     * 数据标识
     */
    public String respId;
    /**
     * 版本号
     */
    public String version;
    /**
     * 数据类型
     */
    public DataType dataType;

    public BaseResp() {
        list = new ArrayList();
    }

    public BaseResp(String respId) {
        this.respId = respId;
        list = new ArrayList();
    }

    public BaseResp(DataType dataType) {
        this.dataType = dataType;
        list = new ArrayList();
    }

    public BaseResp(String respId, DataType dataType) {
        this.respId = respId;
        this.dataType = dataType;
        list = new ArrayList();
    }

    public void setList(Class cls, JSONArray array) throws JSONException,
            IllegalAccessException, InstantiationException {
        if (array == null) {
            return;
        }
        JSONObject jsonObj = null;
        for (int i = 0; i < array.length(); i++) {
            jsonObj = array.getJSONObject(i);
            list.add(JSONParseUtil.reflectObject(cls, jsonObj));
        }
    }
}
