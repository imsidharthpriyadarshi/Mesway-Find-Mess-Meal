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
import in.mesway.Response.Location.MessInfo;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.activity.App;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessInfoRepository {
    private static MessInfoRepository messInfoRepository;
    private SharedPreferences sharedPreferences;
    public MutableLiveData<MessInfo> messInfoMutableLiveData;
    public MutableLiveData<Boolean> isLoading;
    public MutableLiveData<String> detail;
    private ApiInterface apiInterface;
    public MutableLiveData<Integer> status_code;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static MessInfoRepository getInstance(Context context){
        return messInfoRepository=  new MessInfoRepository(context);

    }



    private MessInfoRepository(Context context) {
        messInfoMutableLiveData= new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        status_code = new MutableLiveData<>();
        detail = new MutableLiveData<>();
        getMessInfoInBackGroundThread(context);

    }

    public void getMessInfoInBackGroundThread(Context context){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getMessInfo(context);
                }catch (Exception e){


                }
            }
        });
    }

    private void getMessInfo(Context context) {
        isLoading.postValue(true);
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,context);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface= retrofit.create(ApiInterface.class);

        Call<MessInfo> messInfoCall= apiInterface.get_mess_info(App.getAPIKey(),sharedPreferences.getString(Constant.MESS_ID,null));
        messInfoCall.enqueue(new Callback<MessInfo>() {
            @Override
            public void onResponse(@NonNull Call<MessInfo> call, @NonNull Response<MessInfo> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code()==200){
                    status_code.postValue(response.code());
                    messInfoMutableLiveData.postValue(response.body());
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
            public void onFailure(@NonNull Call<MessInfo> call, @NonNull Throwable t) {
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
