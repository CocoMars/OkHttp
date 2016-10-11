package com.itheima.okhttpretrofit;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
//    public static final String SERVER_URL = "http://192.168.7.251:8080/zhbj";// 服务器主域名
//    public static final String PHOTOS_URL = SERVER_URL+ "/photos/photos_1.json";// 组图信息接口

//    public static final String baseUrl = "http://wthrcdn.etouch.cn/";
public static final String baseUrl = "http://192.168.78.251:8080/zhbj/";
    private Button btn_request;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_request = (Button) findViewById(R.id.btn_request);
        tv_content = (TextView) findViewById(R.id.tv_content);

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doAction();
                    }
                }).start();

            }
        });


        

    }

    private void doAction() {
        OkHttpClient.Builder  builder = new OkHttpClient().newBuilder();
        //添加日志拦截器
//        builder.addInterceptor(new LoggingInterceptor());
//        builder.addInterceptor(new CookiesInterceptor());
        //连接超时时间
        builder.connectTimeout(3000, TimeUnit.SECONDS);
        //设置缓存
        builder.cache(new Cache(Environment.getExternalStorageDirectory().getAbsoluteFile(), 20 * 1024));
        //读数据超时时间
        builder.readTimeout(3000, TimeUnit.SECONDS);
        //失败，刷新重试
        builder.retryOnConnectionFailure(true);
        //写数据超时时间
        builder.writeTimeout(3000, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//定义了对请求返回的数据，处理方式
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<Object> call = service.strRepos();
//        Call<Object> queryParamsCall = service.strReposQueryParams("101010100");

        //Rxjava处理请求任务
//        doRxjavaAction(service);

        //同步执行
        try {
            final Response<Object> response = call.execute();
            if(response.isSuccessful()){
                showUI(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



//        //异步执行
//        queryParamsCall.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                showUI(response);
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void doRxjavaAction(final ApiService service) {

//        //创建被观察者即Observable
//        Observable.create(new Observable.OnSubscribe<Object>() {
//            @Override
//            public void call(Subscriber<? super Object> subscriber) {
//                Call<Object> call = service.strRepos();
//                try {
//                    //处理耗时的任务
//                    Response<Object> response = call.execute();
//                    String result = response.body().toString();
//                    //任务完成以后，通过观察者处理事件
//                    subscriber.onNext(result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                    return;
//                }
//
//                subscriber.onCompleted();
//            }
//        })
//        .subscribeOn(Schedulers.io())//处理任务执行的线程
//        .observeOn(AndroidSchedulers.mainThread())//处理事件执行的线程
//                //由观察者或者订阅者，来处理事件
//        .subscribe(new Subscriber<Object>() {
//            @Override
//            public void onCompleted() {
//
//            }
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object o) {
//
//            }
//        });


        Observable<Object> observable = service.getReposRxjava();
        //注意请求处理以及结果的转化在RxJavaCallAdapterFactory中处理
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                @Override
                public void onCompleted() {

                    Toast.makeText(MainActivity.this, "completed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Throwable e) {

                    Toast.makeText(MainActivity.this, "error = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(Object o) {

                    Toast.makeText(MainActivity.this, "sucess = " + o.toString(), Toast.LENGTH_SHORT).show();
                    tv_content.setText(o.toString());
                }
        });
    }

    private void showUI(final Response<Object> response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            tv_content.setText(response.body().toString());
            }
        });
    }
}
