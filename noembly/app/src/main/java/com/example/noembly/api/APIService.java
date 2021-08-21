package com.example.noembly.api;

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
                    "Authorization:key=AAAA6Gg9l3Q:APA91bEXURdQdbz-fuLTH6yfwMKpV2KVb9CRXlg51S_zLAmBQUgArIJ9IXetaLi7kryz6WrAEC6u6SSgGqyJwAwWMq_vkecWd9EE2wPwr50vp_2MBfJ651am4b027-LTqRL57qTOwVvW"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
