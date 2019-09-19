package com.test.mylibrary;

import com.test.httputils.CommonResult;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Joe
 * @date 2019/9/19.
 * description：
 */
public interface UpService {


    @POST("device/collect")
    Observable<CommonResult<Object>> deviceCollect(
            @Field("collect") String collect);

}
