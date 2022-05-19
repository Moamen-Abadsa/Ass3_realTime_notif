package com.example.realTime_fcm


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=AAAAXn4vUrs:APA91bFU6M8vSh0xSMZ6V4cNPswvl_pS-Y8Te60fD35pxxKXB04F_aWySiUcnLxvbPFWTH7dntzRDT1yVOiYQyUZtKwmA3bJODxpe5PfjEdL5tIDCIFtH_M0dMI9WVJoyvPffoLEwBUC","Content-type:application/json")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}