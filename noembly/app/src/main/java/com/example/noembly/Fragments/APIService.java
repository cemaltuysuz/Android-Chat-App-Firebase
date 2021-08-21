package com.example.noembly.Fragments;

import com.example.noembly.Notifications.MyResponse;
import com.example.noembly.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAf3nyHLc:APA91bEA_g0WP3DugUBRX9LL8tmwfJv_rM7lk1mb7zbaxVJLBBc4kFnHgiv_lISd0bM2uyiZdJlreqO8dNtE78jFgGc8sr-Edssed0lILEh2ixbr35_uvGC365GTlZQlBRG_PskhIZ9k"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
