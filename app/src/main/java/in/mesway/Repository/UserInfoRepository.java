package in.mesway.Repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import in.mesway.Response.DetailResponse;
import in.mesway.Response.UpcomingMeal.UserInfo;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserInfoRepository {

    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private static  UserInfoRepository userInfoRepository;
    public MutableLiveData<UserInfo> userInfoMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static UserInfoRepository getInstance(Context context){

        return userInfoRepository = new UserInfoRepository(context);

    }

    public UserInfoRepository(Context context){
        userInfoMutableLiveData=new MutableLiveData<>();
        isLoading= new MutableLiveData<>();
        status_code = new MutableLiveData<>();
        detail = new MutableLiveData<>();
        getUserDetailInfoInBackgroundThread(context);



    }

    public void getUserDetailInfoInBackgroundThread(Context context){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getUserDetailInfo(context);
                }catch (Exception e){


                }
            }
        });

    }
    private void getUserDetailInfo(Context context){
        isLoading.postValue(true);
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,context);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);

        Call<UserInfo> userInfoCall = apiInterface.get_user_info(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null),sharedPreferences.getString(Constant.USER_ID,null));
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code()==200){
                    status_code.postValue(response.code());
                    userInfoMutableLiveData.postValue(response.body());
                    isLoading.postValue(false);


                }else {

                    try {

                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        detail.postValue(detailResponse.getDetail());
                        isLoading.postValue(false);
                        status_code.postValue(response.code());




                    }catch (Exception exception){
                        detail.postValue("Something went error");
                        isLoading.postValue(false);
                        status_code.postValue(response.code());





                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    detail.postValue("Slow internet connection");
                    status_code.postValue(421);
                    isLoading.postValue(false);


                } else if (t instanceof IOException) {
                    detail.postValue("Slow internet connection(time out)");
                    status_code.postValue(421);
                    isLoading.postValue(false);


                } else{
                    detail.postValue(t.getMessage());
                    isLoading.postValue(false);
                    status_code.postValue(500);
                }


            }
        });


    }


}
