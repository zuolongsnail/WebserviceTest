package com.valuestudio.demo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.valuestudio.web.webservice.BaseReqParams;
import com.valuestudio.web.webservice.BaseResp;
import com.valuestudio.web.webservice.BaseWebserviceRequest;
import com.valuestudio.web.webservice.JSONParseUtil;
import com.valuestudio.web.webservice.MessageType;
import com.valuestudio.webservicetest.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequest extends BaseWebserviceRequest {

    public LoginRequest(Context context, String methodName, Handler handler, BaseReqParams params, BaseResp baseResp,
                        Class entityCls, int parseType) {
        super(context, methodName, handler, params, baseResp, entityCls, parseType);
    }

    @Override
    public void parseRespData(String response) throws JSONException {
        Message msg = new Message();
        LoginResp resp = new LoginResp();
        if (response != null && response.length() > 0) {
            JSONObject jsonObject = new JSONObject(response);
            resp.result = JSONParseUtil.getRequestResult(jsonObject);
            if (resp.result) {
                JSONObject msginfo = JSONParseUtil.getMessage(jsonObject);
                if (msginfo.has("uid")) {
                    resp.uid = msginfo.getString("uid");
                }
                if (msginfo.has("userName")) {
                    resp.userName = msginfo.getString("userName");
                }
                if (msginfo.has("phoneNo")) {
                    resp.userName = msginfo.getString("phoneNo");
                }
                if (msginfo.has("gender")) {
                    resp.userName = msginfo.getString("gender");
                }
                if (msginfo.has("headUrl")) {
                    resp.userName = msginfo.getString("headUrl");
                }
                msg.what = MessageType.REQ_SUCCESS;
                msg.obj = resp;
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

}
