package com.test.mylibrary;

import com.test.httputils.CommonResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Joe
 * @date 2019/9/19.
 * description：
 */
public interface UpService {

    @FormUrlEncoded
    @POST("/device/collect")
    Observable<CommonResult<Object>> deviceCollect(
            @Field("collect") Object collect);

}
