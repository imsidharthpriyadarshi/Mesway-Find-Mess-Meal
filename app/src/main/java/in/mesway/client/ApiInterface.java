package in.mesway.client;

import java.util.UUID;

import in.mesway.Response.ExtraInfoResponse;
import in.mesway.Response.FirstTimeUserResponse;
import in.mesway.Response.Location.ListLocation;
import in.mesway.Response.Location.MessInfo;
import in.mesway.Response.LoginSignupResponse;
import in.mesway.Response.NormalSubscription;
import in.mesway.Response.OtpResponse;
import in.mesway.Response.TakeDeliveryResponse;
import in.mesway.Response.UpcomingMeal.UpcomingMeals;
import in.mesway.Response.UpcomingMeal.UserAddress;
import in.mesway.Response.UpcomingMeal.UserInfo;
import in.mesway.Response.UpcomingMeal.UserSubscription;
import in.mesway.Response.UpcomingMeal.UserSuggestion;
import in.mesway.Response.ZipcodeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("user/login")
    Call<LoginSignupResponse> do_sign_in(
      @Header("Authorization") String api_key,
      @Query("mobile_number") String mobile_number


    );

    @POST("utility/verify-login")
    Call<OtpResponse> verify_number(
            @Header("Authorization") String api_key,
            @Query("number") String number,
            @Query("otp") String otp

    );

    @POST("user/first_time_user")
    Call<FirstTimeUserResponse> first_time_user(
      @Header("Authorization") String access_token,
      @Query("user_id") String user_id,
      @Query("full_name") String full_name,
      @Query("email") String email,
      @Query("notification_token") String notification_token
    );


    @GET("user/get_mess_list")
    Call<ListLocation> get_mess_list(
      @Header("Authorization") String api_key,
      @Query("latitude") String latitude,
      @Query("langitude") String longitude



    );

    @GET("{zipcode}")
    Call<ZipcodeResponse[]> get_zip_code_data(
            @Path("zipcode") String zipcode
    );

    @GET("user/get_upcoming_meals")
    Call<UpcomingMeals> get_upcoming_meal(
            @Header("Authorization") String access_token,
            @Query("user_id") String user_id



    );




    @GET("user/get_user_by_user_id")
    Call<UserInfo> get_user_info(
            @Header("Authorization") String access_token,
            @Query("user_id") String user_id

    );

    @GET("utility/get_date_as_date")
    Call<String> get_date_as_date(
    );

    @GET("utility/get_extra_info")
    Call<ExtraInfoResponse> get_extra_info(
            @Header("Authorization") String api_key

    );

    @POST("user/add_address")
    Call<UserAddress> add_user_address(
            @Header("Authorization") String access_token,
            @Query("user_id") String user_id,
            @Query("full_name") String full_name,
            @Query("mobile_number") String mobile_number,
            @Query("location_type") String location_type,
            @Query("building_no") String building_no,
            @Query("full_address") String full_address,
            @Query("landmark") String landmark,
            @Query("state") String state,
            @Query("longitude") String longitude,
            @Query("latitude") String latitude,
            @Query("district") String district,
            @Query("pin_code") String pin_code

            );



    @POST("user/update_address")
    Call<UserAddress> update_address(
            @Header("Authorization") String access_token,
            @Query("location_id") String location_id,
            @Query("user_id") String user_id,
            @Query("full_name") String full_name,
            @Query("mobile_number") String mobile_number,
            @Query("location_type") String location_type,
            @Query("building_no") String building_no,
            @Query("full_address") String full_address,
            @Query("landmark") String landmark,
            @Query("state") String state,
            @Query("longitude") String longitude,
            @Query("latitude") String latitude,
            @Query("district") String district,
            @Query("pin_code") String pin_code

    );
    @POST("user/delete_address")
    Call<Boolean> delete_address(
            @Header("Authorization") String access_token,
            @Query("location_id") String location_id,
            @Query("user_id") String user_id
    );


    @POST("user/user_suggestion")
    Call<UserSuggestion> add_user_suggestion(
            @Header("Authorization") String access_token,
            @Query("user_id") String user_id,
            @Query("suggestion") String suggestion

    );


    @GET("utility/get_address_from_coordinate")
    Call<String> get_address_from_coordinate(
            @Header("Authorization") String api_key,
            @Query("lat") String lat,
            @Query(("long")) String lng

    );

    @POST("user/cancel_upcoming_meal")
    Call<NormalSubscription> cancel_upcoming_meal(
            @Header("Authorization") String access_token,
            @Query("subs_id") String subs_id,
            @Query("mess_id") String mess_id,
            @Query("user_id") String user_id,
            @Query("breakfast") String breakfast,
            @Query("lunch") String lunch,
            @Query("dinner") String dinner



    );

    @GET("user/get_mess_full_info")
    Call<MessInfo> get_mess_info(
            @Header("Authorization") String api_key,
            @Query("mess_id") String mess_id

    );


    @POST("user/add_subscriptions")
    Call<UserSubscription> add_subscription(
            @Header("Authorization") String access_token,
            @Query("mess_id") String mess_id,
            @Query("user_id") String user_id,
            @Query("start_from") String start_from,
            @Query("plan_type") String plan_type,
            @Query("starting_meal") String starting_meal,
            @Query("second_meal") String second_meal,
            @Query("third_meal") String third_meal,
            @Query("plan_price") String plan_price,
            @Query("how_many_days") int how_many_days,
            @Query("subs_type") String subs_type,
            @Query("payment_value") String payment_value
    );

    @POST("user/add_address_to_subs")
    Call<UserSubscription> add_address_to_subscription(
            @Header("Authorization") String access_token,
            @Query("subs_id") String subs_id,
            @Query("user_id") String user_id,
            @Query("address_id") String address_id


    );

    @POST("user/add_payment_mode")
    Call<UserSubscription> add_payment_to_subscription(
            @Header("Authorization") String access_token,
            @Query("subs_id") String subs_id,
            @Query("user_id") String user_id,
            @Query("payment_mode") String payment_mode,
            @Query("payment_by") String payment_by


    );


    @GET("user/take_delivery")
    Call<TakeDeliveryResponse> get_delivery(
            @Header("Authorization") String access_token,
            @Query("user_id") String user_id,
            @Query("subs_id") String subs_id,
            @Query("meal_type") String meal_type



    );

    @GET("utility/update_notification_token")
    Call<Boolean> update_notification_token(
            @Header("Authorization") String api_key,
            @Query("user_id") UUID user_id,
            @Query("notification_token") String notification_token
    );

    @POST("user/update_expired_img_url")
    Call<String> update_expired_img_url(
            @Header("Authorization") String api_key,
            @Query("mess_id") UUID mess_id,
            @Query("file_type") String file_type


    );











}
