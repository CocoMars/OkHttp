# OkHttp
网络框架
        OkHttp优势：
        
          支持SPDY，共享同一个Socket来处理同一个服务器的所有请求
          如果SPDY不可用，则通过连接池来减少请求延迟
          无缝的支持GZIP来减少数据流量
          缓存响应数据来减少重复的网络请求

    在build.gradle中添加okhttp的依赖：
    compile 'com.squareup.okhttp3:okhttp:3.3.1'
    
    okhttp异步发起get请求
             /**
               *  异步GET方式
               *  注意：无需自己开启子线程，异步方式会自己开一个线程去执行
               */
              @OnClick(R.id.btn_get_asyc)
              public void getAsyc() {
                  // 1. 获取okHttpClient实例
                  mOkHttpClient = new OkHttpClient();
                  // 2. 构建Request
                  mGetRequest = new Request.Builder().url(Constants.GET_URL).build();
                  // 3. 获取网络请求Call对象
                  Call call = mOkHttpClient.newCall(mGetRequest);
                  // 4. 执行网络请求（异步方式）
                 call.enqueue(new Callback() {
                     @Override
                     public void onFailure(Request request, IOException e) {
                         Log.e(TAG, "异步GET请求失败 ===========, code = " + e.getMessage());
                     }

                     @Override
                     public void onResponse(Response response) throws IOException {
                         // 5.处理响应的结果
                         if (response.isSuccessful()) {
                             String reponseStr = response.body().string();
                             Log.e(TAG, "异步GET请求成功 ===========" + reponseStr);
                         } else {
                             Log.e(TAG, "异步GET请求失败 ===========, code = " + response.code());
                         }
                     }
                 });
                 
                 
            okhttp异步发起post请求
                  @OnClick(R.id.btn_post_asyc)
                  public void postAsyc() {
                      // 2. 构建要传输给服务器的参数
                      RequestBody body = new FormEncodingBuilder()
                              .add("platform", "android")
                              .add("version", "23")
                              .add("SDK", "24")
                              .build();
                      // 3. 构建Request
                      Request request = new Request.Builder().url(Constants.POST_URL_PARAMS).post(body).build();
                      // 4. 获取Call对象
                      Call call = mOkHttpClient.newCall(request);
                      // 5. 同步post请求服务器
                      call.enqueue(new Callback() {
                          @Override
                          public void onFailure(Request request, IOException e) {
                              Log.e(TAG, "异步Post请求失败 =========== " + e.getMessage());
                          }

                          @Override
                          public void onResponse(Response response) throws IOException {
                              // 6. 处理响应的结果
                              if (response.isSuccessful()) {
                                  String reponseStr = response.body().string();
                                  Log.e(TAG, "异步Post请求成功 ===========" + reponseStr);
                              } else {
                                  Log.e(TAG, "异步Post请求失败 ===========, code = " + response.code());
                              }
                          }
                      });
                  }

