package com.valuestudio.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.valuestudio.web.webservice.BaseReqParams;
import com.valuestudio.web.webservice.BaseWebserviceRequest;
import com.valuestudio.web.webservice.BasicRequest;
import com.valuestudio.web.webservice.MessageType;
import com.valuestudio.webservicetest.R;


public class MainActivity extends AppCompatActivity {

    private Button btn_start_request;
    /**
     * webservice请求方法名
     */
    private String methodName = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start_request = (Button) findViewById(R.id.btn_start_request);
        btn_start_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求登录接口，接口定义参见login.interface文件
                LoginParams params = new LoginParams();
                params.userAccount = "admin";
                params.password = "123456";
                sendLoginReqest(params);
            }
        });
    }

    /**
     * 发起请求
     *
     * @param params
     */
    private void sendLoginReqest(BaseReqParams params) {
        // 使用自定义请求
//        LoginRequest loginRequest = new LoginRequest(this, methodName, loginHandler, params, new LoginResp(), null, BasicRequest.JSON_PARSE_TYPE);
//        loginRequest.sendRequest();
        // 使用基类请求
        BaseWebserviceRequest loginRequest = new BaseWebserviceRequest(this, methodName, loginHandler, params, new LoginResp(), null, BasicRequest.OBJECT_PARSE_TYPE);
        loginRequest.sendRequest();
    }

    /**
     * 数据返回以后在主线程进行业务处理
     */
    private Handler loginHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case MessageType.REQ_SUCCESS:
                    if (msg.obj instanceof LoginResp) {
                        LoginResp resp = (LoginResp) msg.obj;
                        String uid = resp.uid;
                        String userName = resp.userName;
                        String phoneNo = resp.phoneNo;
                        String gender = resp.gender;
                        String headUrl = resp.headUrl;
                    }
                    break;
                default:
                    Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
