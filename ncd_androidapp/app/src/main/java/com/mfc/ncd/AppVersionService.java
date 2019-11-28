package com.mfc.ncd;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


import java.util.List;

public class AppVersionService
{
public static  String URL="https://cpt.stageibb.com/cpt_stage/cpt_mis/Api/checkappversion/";//"https://ncd.ibbtrade.com/Api/checkappversion/";
                        // "https://cpt.stageibb.com/cpt_stage/cpt_mis/Api/checkappversion/";

    protected static final String BASE_URL = URL;
    protected static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    protected static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build();
    protected static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();


    protected static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();





    public interface VersionInterface
    {
        @Headers("Content-Type: application/json; charset=utf-8")
        @POST("https://ncd.ibbtrade.com/Api/checkappversion/")

        Call<AppVersionResponseModel> pushData(@Body AppVersionModel appVersionModel);

    }
    public void pushData(AppVersionModel appVersionModel , final HttpCallResponse mCallResponse){
        VersionInterface versionInterface = retrofit.create(VersionInterface.class);
        final Call<AppVersionResponseModel> call = versionInterface.pushData(appVersionModel);
        call.enqueue(new Callback<AppVersionResponseModel>() {
            @Override
            public void onResponse(Call<AppVersionResponseModel> call, Response<AppVersionResponseModel> response) {
                if(response.isSuccessful()){
                    mCallResponse.OnSuccess(response);
                   // Log.d("Success Message"," "+response);
                }
            }

            @Override
            public void onFailure(Call<AppVersionResponseModel> call, Throwable throwable) {
                mCallResponse.OnFailure(throwable);
                //Log.d("Failure Message"," "+throwable);
            }
        });
    }


   }
