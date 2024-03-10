package in.mesway.Rusable;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.UUID;

import in.mesway.R;
import in.mesway.activity.App;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.services.NotificationService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Reusable {

    public static AlertDialog alertDialog(Context context) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyProgressDialog);
        alertDialog.setView(R.layout.progress_bar);
        alertDialog.setCancelable(false);
        return alertDialog.create();

    }

    public static boolean CheckInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities activeNetwork = connectivityManager.getNetworkCapabilities(network);
        return activeNetwork != null && (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));

    }

    public static void updateNotificationToken(Context context, String token) {
        ApiInterface apiInterface;
        SharedPreferences sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,context);
        sharedPreferences.edit().putBoolean(Constant.HAVE_TO_UPDATE_NOTIFICATION_TOKEN,true).apply();
        sharedPreferences.edit().putString(Constant.NOTIFICATION_TOKEN,token).apply();


        if (sharedPreferences.getString(Constant.USER_ID,null)!=null){
            Retrofit retrofit = ApiClient.getClient();

            apiInterface= retrofit.create(ApiInterface.class);
            Call<Boolean> booleanResponseCall=apiInterface.update_notification_token( App.getAPIKey(), UUID.fromString(sharedPreferences.getString(Constant.USER_ID,null)),token);
            booleanResponseCall.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (response.code()==200){
                        sharedPreferences.edit().putBoolean(Constant.HAVE_TO_UPDATE_NOTIFICATION_TOKEN,false).apply();
                      //  sharedPreferences.edit().putString(Constant.NOTIFICATION_TOKEN,token).apply();
                    }else{
                        sharedPreferences.edit().putBoolean(Constant.HAVE_TO_UPDATE_NOTIFICATION_TOKEN,true).apply();

                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    sharedPreferences.edit().putBoolean(Constant.HAVE_TO_UPDATE_NOTIFICATION_TOKEN,true).apply();


                }
            });

        }else{

            Log.e("not_tag","not seller id present");
        }
    }

    public static void update_aws_img_url(String file_type,String mess_id,UpdateImgResponseInterface updateImgResponse){
         UpdateImgResponseInterface updateImgResponseInterface=updateImgResponse;

        ApiInterface apiInterface;
        apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<String> stringCall=apiInterface.update_expired_img_url(App.getAPIKey(),UUID.fromString(mess_id),file_type);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code()==200) {
                    updateImgResponseInterface.getResponse(response.body(),200);

                }else if (response.code()==303){

                    updateImgResponseInterface.getResponse("available",303);

                }else {
                    updateImgResponseInterface.getResponse("error",222);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                updateImgResponseInterface.getResponse("error",500);

            }
        });


    }
}
