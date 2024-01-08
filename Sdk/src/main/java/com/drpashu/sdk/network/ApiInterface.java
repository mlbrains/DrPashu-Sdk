package com.drpashu.sdk.network;

import com.drpashu.sdk.network.model.request.DrPashuRequest;
import com.drpashu.sdk.network.model.response.AnimalListResponse;
import com.drpashu.sdk.network.model.response.BaseResponse;
import com.drpashu.sdk.network.model.response.CallDetailResponse;
import com.drpashu.sdk.network.model.response.CallHistoryListResponse;
import com.drpashu.sdk.network.model.response.DeviceTokenUpdateResponse;
import com.drpashu.sdk.network.model.response.DrPashuResponse;
import com.drpashu.sdk.network.model.response.FeedbackListResponse;
import com.drpashu.sdk.network.model.response.MessageListResponse;
import com.drpashu.sdk.network.model.response.StartCallResponse;
import com.drpashu.sdk.network.model.response.VetListResponse;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @GET("animals")
    Call<List<AnimalListResponse>> getAnimalList(@Header("user-id") String user_id);

    @POST("vet_list")
    @FormUrlEncoded
    Call<VetListResponse> getVetList(@Header("user-id") String user_id,
                                     @Field("lot_id") String lot_id,
                                     @Field("animal_type") String animalType);

    @POST("start_call")
    @FormUrlEncoded
    Call<StartCallResponse> startCall(@Header("user-id") String user_id,
                                      @Field("lot_id") String lot_id,
                                      @Field("animal_type") String animalType,
                                      @Field("vet_category") String vetCategory,
                                      @Field("health_val") String healthValue,
                                      @Field("company_name") String companyName,
                                      @Field("reconnecting_call") String reconnectingCall,
                                      @Field("group_id") String groupId,
                                      @Field("free_call") String freeCall,
                                      @Field("call_price") String callPrice,
                                      @Field("payment_id") String paymentId);

    @GET("call_history")
    Call<CallHistoryListResponse> getCallHistoryList(@Header("user-id") String user_id);

    @POST("call_details")
    @FormUrlEncoded
    Call<CallDetailResponse> getCallDetail(@Header("user-id") String user_id,
                                           @Field("call_id") String callId);

    @POST("call_status_update")
    @FormUrlEncoded
    Call<BaseResponse> updateCallStatus(@Header("user-id") String user_id,
                                        @Field("call_id") String callId,
                                        @Field("call_status") String callStatus);

    @POST("update_device_id")
    @FormUrlEncoded
    Call<DeviceTokenUpdateResponse> updateDeviceToken(@Header("user-id") String user_id,
                                                      @Field("device_id") String deviceId,
                                                      @Field("app_version") String appVersion);

    @POST("call_reject_api")
    @FormUrlEncoded
    Call<BaseResponse> rejectCall(@Header("user-id") String user_id,
                                  @Field("callid") String callId,
                                  @Field("notificationId") String notificationId,
                                  @Field("userType") String userType,
                                  @Field("call_initiated") String callInitiated);

    @POST("error_api")
    @FormUrlEncoded
    Call<JsonObject> postErrorDetails(@Header("user-id") String user_id,
                                      @Field("title") String title,
                                      @Field("message") String message,
                                      @Field("details") String details);

    @POST("record_user")
    @FormUrlEncoded
    Call<BaseResponse> recordUser(@Field("api_key") String api_key,
                                  @Field("user_id") String user_id);

    @POST("sdk/add_user")
    Call<DrPashuResponse> addUserFromSdk(@Body DrPashuRequest drPashuRequest);

    @POST("call_feedback")
    @FormUrlEncoded
    Call<BaseResponse> submitCallFeedback(@Header("user-id") String user_id,
                                          @Field("call_id") String call_id,
                                          @Field("rating") String rating,
                                          @Field("review_id") String rating_id,
                                          @Field("comment") String comment);

    @GET("get_feedback_list")
    Call<FeedbackListResponse> getFeedbackList(@Header("user-id") String user_id);

    @POST("vet_pick_up")
    @FormUrlEncoded
    Call<BaseResponse> checkCallPickAllowed(@Header("user-id") String user_id,
                                            @Field("call_id") String callId);
    @POST("view_messages")
    @FormUrlEncoded
    Call<MessageListResponse> getMessageList(@Header("user-id") String user_id,
                                             @Field("call_id") String callId);
    @Multipart
    @POST("send_message")
    Call<BaseResponse> sendMessage(@Header("user-id") String user_id,
                                   @Part("call_id") RequestBody callId,
                                   @Part("text") RequestBody text,
                                   @Part MultipartBody.Part image,
                                   @Part MultipartBody.Part video);
}