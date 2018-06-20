package com.valuestudio.web.webservice;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.valuestudio.web.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 请求基类
 *
 * @author zuolong
 * @description 请求基类
 */
public abstract class BasicRequest {
    /**
     * 请求类型
     */
    public enum RequestType {
        /**
         * GET请求
         */
        GET,
        /**
         * POST请求
         */
        POST
    }

    /**
     * 列表解析类型，对应的返回数据必须严格按照以下格式：
     * {
     *     "success":"true",
     *     "message":{
     *         "root":[
     *             {
     *                 "data1":"数据字段1",
     *                 "data2":"数据字段2"
     *             }
     *         ]
     *     }
     * }
     */
    public static final int LIST_PARSE_TYPE = 1;
    /**
     * 对象解析类型，对应的返回数据必须严格按照以下格式：
     * {
     *     "success":"true",
     *     "message":{
     *         "data1":"数据字段1",
     *         "data2":"数据字段2"
     *     }
     * }
     */
    public static final int OBJECT_PARSE_TYPE = 2;
    /**
     * JSON解析类型，对应的返回数据必须严格按照以下格式：
     * {
     *     "success":"true",
     *     "message":{
     *         包含任何数据格式
     *     }
     * }
     */
    public static final int JSON_PARSE_TYPE = 3;
    /**
     * 结果解析类型，对应的返回数据必须严格按照以下格式：
     * {
     *     "success":"true/false",
     *     "message":"请求响应信息"
     * }
     */
    public static final int RESULT_PARSE_TYPE = 4;

    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 回调句柄
     */
    public Handler mHandler;
    /**
     * 响应数据对象
     */
    protected BaseResp mResp;
    /**
     * 列表实体
     */
    protected Class mEntityCls;
    /**
     * 解析类型
     */
    protected int mParseType;
    /**
     * 是否被取消
     */
    protected boolean hasCancel;
    /**
     * 使用统一接口时用于辨别返回的数据对应哪个接口
     */
    protected String method;

    /**
     * 解析响应数据
     *
     * @param response
     * @throws Exception
     */
    public void parseRespData(String response) throws JSONException,
            IllegalAccessException, InstantiationException {
        Message msg = new Message();
        if (!TextUtils.isEmpty(response)) {
            JSONObject jsonObject = new JSONObject(response);
            boolean result = JSONParseUtil.getRequestResult(jsonObject);
            if (result) {
                if (mParseType == LIST_PARSE_TYPE) {
                    mResp.pageSum = JSONParseUtil.getPageTotal(jsonObject);
                    mResp.itemTotal = JSONParseUtil.getItemTotal(jsonObject);
                    JSONArray array = JSONParseUtil.getDatas(jsonObject);
                    mResp.setList(mEntityCls, array);
                    // 如果返回数据中无字段，则返回“请求成功无数据”结果
                    if (mResp.list.size() <= 0) {
                        msg.what = MessageType.REQ_NODATA;
                        msg.obj = mContext
                                .getString(R.string.request_nodata_msg);
                        // 请求无数据
                        mHandler.sendMessage(msg);
                        return;
                    }
                } else if (mParseType == OBJECT_PARSE_TYPE) {
                    // 数据标识
                    String respId = mResp.respId;
                    // 数据类型
                    DataType dataType = mResp.dataType;
                    JSONObject objectMessage = JSONParseUtil
                            .getMessage(jsonObject);
                    // 如果返回数据中无字段，则返回“请求成功无数据”结果
                    if (objectMessage.length() <= 0) {
                        msg.what = MessageType.REQ_NODATA;
                        msg.obj = mContext
                                .getString(R.string.request_nodata_msg);
                        // 请求无数据
                        mHandler.sendMessage(msg);
                        return;
                    }
                    mResp = mResp.getClass().cast(
                            JSONParseUtil.reflectObject(mResp.getClass(),
                                    objectMessage));
                    mResp.respId = respId;
                    mResp.dataType = dataType;
                } else if (mParseType == JSON_PARSE_TYPE) {
                    msg.what = MessageType.REQ_SUCCESS;
                    msg.obj = jsonObject;
                    mHandler.sendMessage(msg);
                    return;
                } else if (mParseType == RESULT_PARSE_TYPE) {
                    mResp.resultInfo = JSONParseUtil.getResultMsg(jsonObject)
                            .toString();
                }
                msg.what = MessageType.REQ_SUCCESS;
                msg.obj = mResp;
            } else {
                msg.what = MessageType.REQ_FAILED;
                msg.obj = JSONParseUtil.getResultMsg(jsonObject);
            }
        } else {
            msg.what = MessageType.REQ_FAILED;
            msg.obj = mContext.getString(R.string.request_server_error_msg);
        }
        mHandler.sendMessage(msg);
    }

    public abstract void sendRequest();
}
