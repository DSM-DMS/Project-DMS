package com.dms.beinone.application;

import com.dms.beinone.application.models.Account;
import com.dms.beinone.application.models.ApplyStatus;
import com.dms.beinone.application.models.Class;
import com.dms.beinone.application.models.Meal;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by BeINone on 2017-07-10.
 */

public interface DMSService {

    int HTTP_OK = 200;
    int HTTP_CREATED = 201;
    int HTTP_NO_CONTENT = 204;
    int HTTP_BAD_REQUEST = 400;
    int HTTP_CONFLICT = 409;
    int HTTP_INTERNAL_SERVER_ERROR = 500;

    String SERVER_URL = "http://dsm2015.cafe24.com:3000";
    String SERVER_MEAL_URL =  "http://dsm2015.cafe24.com:81";

    @FormUrlEncoded
    @POST("/account/login/student")
    Call<Void> login(@Field("id") String id, @Field("password") String password, @Field("remember") boolean remember);

    @FormUrlEncoded
    @POST("/account/register/student")
    Call<Void> register(@Field("uid") String uid, @Field("id") String id, @Field("password") String password);

    @FormUrlEncoded
    @PATCH("/account/password/student")
    Call<Void> change(@Field("exist") String exist, @Field("change") String change);

    @FormUrlEncoded
    @PUT("/apply/goingout")
    Call<Void> applyGoingout(@Field("sat") boolean sat, @Field("sun") boolean sun);

    @GET("/apply/extension/class")
    Call<Class> loadExtensionClass(@Query("option") String option, @Query("class") int clazz);


    @FormUrlEncoded
    @PUT("/apply/extension")
    Call<Void> applyExtension(@Field("class") int clazz, @Field("seat") int seat);

    @GET("apply/all")
    Call<ApplyStatus> loadApplyStatus();

    @GET("meal")
    Call<JsonObject> loadMeal(@Query("year") String year , @Query("month")String  month, @Query("day") String day);

    @GET("account/student")
    Call<Account> loadMyPage();

    @POST("account/logout/student")
    Call<Void> logout();

    @GET("post/list/faq")
    Call<JsonObject> loadFag();

    @GET("post/list/notice")
    Call<JsonObject> loadNotice();

    @GET("post/list/rule")
    Call<JsonObject> loadRule();

}
