package org.hse.lycsovet.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface EljurHTTPService {
    @FormUrlEncoded
    @POST("/api/auth")
    fun login(
            @Field("vendor") vendor: String,
            @Field("devkey") devkey: String,
            @Field("out_format") outFormat: String,
            @Field("login") login: String,
            @Field("password") password: String
    ) : Call<EljurHTTPResponse<EljurToken>>

    @GET("/api/getrules")
    fun getRules(
            @Query("vendor") vendor: String,
            @Query("devkey") devkey: String,
            @Query("out_format") out_format: String,
            @Query("auth_token") auth_token: String
    ) : Call<EljurHTTPResponse<EljurStudentDetails>>

    companion object Factory {

        private const val API_URL: String = "https://api.eljur.ru/"

        fun create() : EljurHTTPService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val builder = OkHttpClient.Builder()
            builder
                    .addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(API_URL)
                    .client(builder.build())
                    .build()

            return retrofit.create(EljurHTTPService::class.java)
        }
    }
}