package com.valuestudio.web.webservice;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.valuestudio.web.R;
import com.valuestudio.web.utils.BaseConstant;
import com.valuestudio.web.utils.LogUtil;
import com.valuestudio.web.utils.NetworkUtil;
import com.valuestudio.web.utils.PropertiesUtil;

import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * WebService请求基类
 *
 * @author zuolong
 * @description WebService请求基类
 */
public class BaseWebserviceRequest extends BasicRequest implements Runnable {
    private static int VERSION = SoapEnvelope.VER11;
    protected SoapSerializationEnvelope mEnvelope;
    /**
     * 方法名
     */
    protected String mMethodName;
    /**
     * SoapAction由命名空间和方法名拼接
     */
    protected String mSoapAction;
    /**
     * 请求参数对象
     */
    protected BaseReqParams mReqParams;

    /**
     * 非列表请求构造
     *
     * @param context
     * @param methodName 方法名
     * @param handler
     * @param params     请求参数对象
     * @param baseResp   响应数据封装对象
     * @param parseType  解析类型
     */
    public BaseWebserviceRequest(Context context, String methodName,
                                 Handler handler, BaseReqParams params, BaseResp baseResp,
                                 int parseType) {
        this(context, methodName, handler, params, baseResp, null, parseType);
    }

    /**
     * 列表请求构造
     *
     * @param context
     * @param methodName 方法名
     * @param handler
     * @param params     请求参数对象
     * @param baseResp   响应数据封装对象
     * @param entityCls  列表泛型类
     * @param parseType  解析类型
     */
    public BaseWebserviceRequest(Context context, String methodName,
                                 Handler handler, BaseReqParams params, BaseResp baseResp,
                                 Class entityCls, int parseType) {
        this.mContext = context;
        this.mMethodName = methodName;
        this.method = methodName;
        this.mSoapAction = getNameSpace() + mMethodName;
        this.mHandler = handler;
        this.mReqParams = params;
        this.mResp = baseResp;
        this.mEntityCls = entityCls;
        this.mParseType = parseType;
        LogUtil.d(LogUtil.TAG, "========request begin========");
        LogUtil.d(LogUtil.TAG, "request method=" + mMethodName);
    }

    /**
     * 设置请求参数
     *
     * @param soapObject
     * @return
     */
    public SoapObject setProperty(SoapObject soapObject) {
        try {
            Map<String, Map<String, String>> paramsIndexMap = injectParamsMap();
            // 封装参数5.参数排序后进行遍历
            TreeSet<String> indexTreeSet = new TreeSet<String>(
                    new IndexComparator());
            indexTreeSet.addAll(paramsIndexMap.keySet());
            Iterator<String> iterator = indexTreeSet.iterator();
            while (iterator.hasNext()) {
                String indexKey = iterator.next();
                // 封装参数6.获取<参数名称, 参数值>键值对，此时map中仅有一对
                Map<String, String> paramMap = paramsIndexMap.get(indexKey);
                Iterator<String> paramIterator = paramMap.keySet().iterator();
                String nameKey = paramIterator.next();
                String value = paramMap.get(nameKey);
                soapObject.addProperty(nameKey, value);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return soapObject;
    }

    /**
     * 通过注解反射参数顺序和名称
     *
     * @return
     * @throws IllegalAccessException
     * @description
     * @date 2014-3-5
     * @author zuolong
     */
    private Map<String, Map<String, String>> injectParamsMap()
            throws IllegalAccessException {
        // <参数顺序index, <参数名称name, 参数值value>>
        Map<String, Map<String, String>> paramsIndexMap = new HashMap<String, Map<String, String>>();
        Class<? extends BaseReqParams> cls = mReqParams.getClass();
        Field[] fields = cls.getDeclaredFields();
        // 循环遍历请求参数对象中的值
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                // 判断变量是否存在指定的注解
                if (field.isAnnotationPresent(InjectReqParam.class)) {
                    // 获得该成员的annotation
                    InjectReqParam reqParam = field
                            .getAnnotation(InjectReqParam.class);
                    // 封装参数1.通过注解获得该参数的顺序
                    int index = reqParam.index();
                    if (index <= 0) {
                        continue;
                    }
                    // 封装参数2.通过注解获得该参数的名称
                    String name = reqParam.name();
                    // 如果没有设置name则默认变量名为参数名称
                    if (TextUtils.isEmpty(name)) {
                        name = field.getName();
                    }
                    // 封装参数3.获取参数值
                    field.setAccessible(true);
                    Object object = field.get(mReqParams);
                    String value = null;
                    if (object != null) {
                        value = object.toString();
                    }
                    // 封装参数4.存储参数
                    Map<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put(name, value);
                    paramsIndexMap.put(String.valueOf(index), paramMap);
                }
            }
        }
        return paramsIndexMap;
    }

    /**
     * 封装请求
     */
    protected void getRequest() {
        // 请求1.制定命名空间和调用方法
        SoapObject soapObject = new SoapObject(getNameSpace(), mMethodName);
        // 请求2.设置方法参数（设置方法参数时确保与服务端WebService类中的方法参数顺序一致）
        soapObject = setProperty(soapObject);
        // 打印请求参数，发布时删除
        StringBuffer buff = new StringBuffer();
        for (int index = 0; index < soapObject.getPropertyCount(); index++) {
            Object property = soapObject.getProperty(index);
            if (property != null && !TextUtils.isEmpty(property.toString())) {
                buff.append(index + 1).append(":{").append(property)
                        .append("};");
            }
        }
        LogUtil.i(LogUtil.TAG, "property=" + buff.toString());
        // 请求3.SOAP请求信息，保证版本号与服务端的一致
        mEnvelope = new SoapSerializationEnvelope(VERSION);
        // mEnvelope.dotNet = true;// 默认为false，如果是访问.NET的webservice服务，这里需要设置为true
        mEnvelope.bodyOut = soapObject;
        // 请求3.设置请求url
        getHttpTransportSE().setUrl(getUrl());
    }

    /**
     * 封装url
     *
     * @return
     */
    protected String getUrl() {
        // 获取请求地址
        String requestUrl = PropertiesUtil.getInstance(mContext)
                .getProperty(BaseConstant.KEY_REQUEST_URL, "").trim();
        // 判断地址中是否有http头，没有则添加
        if (!requestUrl.startsWith(BaseConstant.HTTP_HEAD)) {
            requestUrl = BaseConstant.HTTP_HEAD + requestUrl;
        }
        LogUtil.d(LogUtil.TAG, "request url:" + requestUrl);
        return requestUrl;
    }

    /**
     * 获取命名空间
     *
     * @return
     */
    protected String getNameSpace() {
        String nameSpace = PropertiesUtil.getInstance(mContext)
                .getProperty(BaseConstant.KEY_NAME_SPACE, "").trim();
        if (!nameSpace.endsWith("/")) {// 判断命名空间后缀是不是斜杠，是则不用添加，否则添加
            nameSpace = nameSpace + "/";
        }
        return nameSpace;
    }

    /**
     * 发送请求
     */
    public void sendRequest() {
        // 封装请求
        getRequest();

        // 判断网络是否连接
        if (NetworkUtil.isNetworkConnected(mContext)) {
            // 请求5.添加请求到线程池
            ThreadPool.getInstance().addTask(this);
        } else {
            Message msg = new Message();
            msg.what = MessageType.REQ_CONNTECT_FAIL;
            msg.obj = mContext.getString(R.string.network_conntect_fail_msg);
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 取消请求
     *
     * @description 一般在onDestroy方法中调用，也可根据实际情况而定
     * @date 2013-12-25
     * @author zuolong
     */
    public void cancelRequests() {
        hasCancel = true;
        LogUtil.d(LogUtil.TAG, mMethodName + " canceled");
        LogUtil.d(LogUtil.TAG, "========request end========");
    }

    @Override
    public void run() {
        // 5.使用call方法调用WebService方法
        try {
            mHttpTransportSE.call(mSoapAction, mEnvelope);
            // 如果已经取消请求则不进行数据的解析和返回
            if (hasCancel) {
                return;
            }
            Object response = mEnvelope.getResponse();
            if (response != null) {
                LogUtil.i(LogUtil.TAG, method + " request resp=" + response.toString());
                LogUtil.d(LogUtil.TAG, "========request end========");
                parseRespData(response.toString());
            } else {
                parseRespData(null);
            }
        } catch (SocketTimeoutException e) {
            // 如果已经取消请求则不进行数据的解析和返回
            if (hasCancel) {
                return;
            }
            // 请求超时
            Message msg = new Message();
            msg.what = MessageType.REQ_TIMEOUT;
            msg.obj = mContext.getString(R.string.request_timeout_msg);
            mHandler.sendMessage(msg);
        } catch (ConnectException e) {
            // 如果已经取消请求则不进行数据的解析和返回
            if (hasCancel) {
                return;
            }
            // 服务器拒绝连接
            Message msg = new Message();
            msg.what = MessageType.REQ_SERVER_ERROR;
            msg.obj = mContext.getString(R.string.request_connect_error_msg);
            mHandler.sendMessage(msg);
            LogUtil.d(LogUtil.TAG, e.getMessage());
        } catch (IOException e) {
            // 如果已经取消请求则不进行数据的解析和返回
            if (hasCancel) {
                return;
            }
            // 请求IO错误
            Message msg = new Message();
            msg.what = MessageType.REQ_SERVER_ERROR;
            // 针对不同的返回消息，提供不同的提示信息
            String errorinfo = e.getMessage();
            if (errorinfo != null) {
                if (errorinfo.contains(BaseConstant.SERVER_BUSY)) {// 服务器忙
                    msg.obj = mContext
                            .getString(R.string.request_server_busy_msg);
                } else if (errorinfo.contains(BaseConstant.SERVER_404)) {// 404问题
                    msg.obj = mContext
                            .getString(R.string.request_connect_error_msg);
                } else if (errorinfo.contains(BaseConstant.SERVER_500)) {// 500错误
                    msg.obj = mContext
                            .getString(R.string.request_server_error_msg);
                } else {// 其他未知问题
                    msg.obj = mContext
                            .getString(R.string.request_connect_error_msg);
                }
                LogUtil.d(LogUtil.TAG, errorinfo);
            } else {// 其它系统错误
                msg.obj = mContext.getString(R.string.request_server_error_msg);
            }
            mHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            // 解析错误
            Message msg = new Message();
            msg.what = MessageType.REQ_PARSE_ERROR;
            msg.obj = mContext.getString(R.string.request_parse_error_msg);
            mHandler.sendMessage(msg);
            LogUtil.d(LogUtil.TAG, e.getMessage());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            // 解析错误
            Message msg = new Message();
            msg.what = MessageType.REQ_PARSE_ERROR;
            msg.obj = mContext.getString(R.string.request_parse_error_msg);
            mHandler.sendMessage(msg);
            LogUtil.d(LogUtil.TAG, e.getMessage());
        } catch (IllegalArgumentException e) {
            Message msg = new Message();
            msg.what = MessageType.REQ_SERVER_ERROR;
            // 非法字符
            if (e.getMessage().contains(BaseConstant.ILLEGAL_CHAR)) {
                msg.obj = mContext.getString(R.string.request_illegal_char_msg);
            } else {
                msg.obj = mContext.getString(R.string.request_server_error_msg);
            }
            mHandler.sendMessage(msg);
            LogUtil.d(LogUtil.TAG, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = MessageType.REQ_SERVER_ERROR;
            msg.obj = mContext.getString(R.string.request_server_error_msg);
            mHandler.sendMessage(msg);
            LogUtil.d(LogUtil.TAG, e.getMessage());
        }
    }

}
