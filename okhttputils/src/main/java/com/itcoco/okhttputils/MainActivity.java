package com.itcoco.okhttputils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    public static final String URL_GET = "http://apis.juhe.cn/mobile/get?phone=13812345678&key=daf8fa858c330b22e342c882bcbac622";
    public static final String URL_POST = "http://apis.juhe.cn/mobile/get ";

    @InjectView(R.id.btn_get)
    Button mBtnGet;
    @InjectView(R.id.btn_post)
    Button mBtnPost;
    @InjectView(R.id.tv_OK)
    TextView mTvOK;
    @InjectView(R.id.tv_error)
    TextView mTvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_get, R.id.btn_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                OkHttpUtils
                        .get()
                        .url(URL_GET)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                mTvError.setText(e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                mTvOK.setText(response);
                            }
                        });
                break;
            case R.id.btn_post:
                OkHttpUtils
                        .post()
                        .url(URL_POST)
                        .addParams("phone", "13812345678")
                        .addParams("key", "daf8fa858c330b22e342c882bcbac622")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                mTvError.setText(e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                mTvOK.setText(response);
                            }
                        });
                break;
        }
    }
}
