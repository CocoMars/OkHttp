package com.example.okhttpdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String json = "{\n" +
            "    \"name\": \"hello.android6.0\"\n" +
            "}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1,开启一个子线程,做联网操作
        new Thread(){
            @Override
            public void run() {
                get();
//                postJson();
//                postParams();
            }
        }.start();
    }

    private void postParams() {
        //1,创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2,构建请求体
        RequestBody requestBody = new FormEncodingBuilder()
                .add("platform", "android")
                .add("version", "23")
                .add("SDK", "24")
                .build();
        //3,构建一个请求对象
        Request request = new Request.Builder()
                .url("http://192.168.0.102:8080/TestProject/ParamServlet")
                .post(requestBody)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            //4,判断此请求是否成功
            if(response.isSuccessful()){
                //5,在post请求发送成功后,服务端返回的内容,做一个打印
                Log.i(TAG,response.body().string()+"xxxxxxxxxxxxxxxxxx");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postJson() {
        //1,申明给服务端传递一个json串
        //2,创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //3,创建一个RequestBody(参数1:数据类型 参数2:出阿尼的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //4,创建一个请求对象
        Request request = new Request.Builder()
                .url("http://192.168.0.102:8080/TestProject/JsonServlet")
                .post(requestBody)
                .build();
        //5,发送请求获取响应对象
        try {
//            Response response = okHttpClient.newCall(request).execute();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }
                @Override
                public void onResponse(Response response) throws IOException {
                    //6,判断此请求是否成功
                    if(response.isSuccessful()){
                        //7,在post请求发送成功后,服务端返回的内容,做一个打印
                        Log.i(TAG,response.body().string()+"00000000000000");
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get() {
        OkHttpClient okHttpClient = new OkHttpClient();
        //2,构建一个请求对象
        Request request = new Request.Builder().url("http://wthrcdn.etouch.cn/weather_mini?citykey=101010100").build();
        //3,发送请求
        try {
            Response response = okHttpClient.newCall(request).execute();
            //4,打印服务端返回的json串
            Log.i(TAG,response.body().string()+"==========");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
