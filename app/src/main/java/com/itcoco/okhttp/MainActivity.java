package com.itcoco.okhttp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String URL_GET = "http://apis.juhe.cn/mobile/get?phone=13812345678&key=daf8fa858c330b22e342c882bcbac622";
    public static final String URL_POST = "http://apis.juhe.cn/mobile/get ";
    private static final String TAG = "MainActivity";
    @InjectView(R.id.btn_get)
    Button mBtnGet;
    @InjectView(R.id.btn_post)
    Button mBtnPost;
    @InjectView(R.id.tv_OK)
    TextView mTvOK;
    @InjectView(R.id.tv_error)
    TextView mTvError;
    private OkHttpClient mClient;
    private Request mGetRequest;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String error = (String) msg.obj;
                    mTvError.setText(error);
                    break;

                case 1:
                    String result = (String) msg.obj;
                    mTvOK.setText(result);
                    break;
            }
        }
    };
    private Request mPostRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mClient = new OkHttpClient();
    }

    @OnClick({R.id.btn_get, R.id.btn_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                mGetRequest = new Request.Builder().url(URL_GET).build();
                Call call = mClient.newCall(mGetRequest);

                //同步请求
                // Response response = call.execute();

                //异步请求
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = e.toString();
                        mHandler.sendMessage(msg);

                        Log.e(TAG, "异步请求失败======.code = " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        ResponseBody body = response.body();
                        String string = body.string();
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = string;
                        mHandler.sendMessage(msg);

//                        body.bytes();   //把返回的结果转换成byte数组
//                        body.byteStream(); //把返回的结果转换成流
                    }
                });

                break;
            case R.id.btn_post:
                RequestBody body = new FormEncodingBuilder()
                        .add("phone", "13812345678")
                        .add("key", "daf8fa858c330b22e342c882bcbac622")
                        .build();
                mPostRequest = new Request.Builder()
                        .url(URL_POST)
                        .post(body)
                        .build();
                mClient.newCall(mPostRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = e.toString();
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        ResponseBody body = response.body();
                        String string = body.string();
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = string;
                        mHandler.sendMessage(msg);
                    }
                });

                break;
        }
    }
}
