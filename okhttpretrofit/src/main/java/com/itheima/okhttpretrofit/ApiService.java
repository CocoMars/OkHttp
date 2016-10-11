package com.itheima.okhttpretrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by ywf on 2016/8/4.
 */
public interface ApiService {
//    @Headers("Cache-Control: max-age=640000")
    @GET("photos/photos_1.json")
    Call<Object> strRepos();

    @GET("weather_mini")
    Call<Object> strReposQueryParams(@Query("citykey") String citykey);

    @GET("weather_mini?citykey=101010100")
    Observable<Object> getReposRxjava();


}
