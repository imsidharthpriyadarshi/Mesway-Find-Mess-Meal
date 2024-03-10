package in.mesway.Repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import in.mesway.Response.DetailResponse;
import in.mesway.Response.Location.ListLocation;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.activity.App;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationRepository {

    private ApiInterface apiInterface;
    private static  LocationRepository locationRepository;
    public MutableLiveData<ListLocation> listLocationMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;
    private SharedPreferences sharedPreferences;
    private Executor executor = Executors.newSingleThreadExecutor();


    public static LocationRepository getInstance(Context context){
        return locationRepository = new LocationRepository(context);

    }

    public LocationRepository(Context context){
        listLocationMutableLiveData= new MutableLiveData<>();
        isLoading= new MutableLiveData<>();
        status_code = new MutableLiveData<>();
        detail = new MutableLiveData<>();
        getNearestMessLocationInBackgroundThread(context);

    }

    public void getNearestMessLocationInBackgroundThread(Context context){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getNearestMessLocationList(context);
                }catch (Exception e){

                }
            }
        });

    }
    private void getNearestMessLocationList(Context context) {
        isLoading.postValue(true);
        sharedPreferences=EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,context);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface= retrofit.create(ApiInterface.class);
        Call<ListLocation> listLocationCall = apiInterface.get_mess_list(App.getAPIKey(),sharedPreferences.getString(Constant.LATITUDE,null) , sharedPreferences.getString(Constant.LONGITUDE,null));
        listLocationCall.enqueue(new Callback<ListLocation>() {
            @Override
            public void onResponse(@NonNull Call<ListLocation> call, @NonNull Response<ListLocation> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code()==200){
                    status_code.postValue(response.code());
                    listLocationMutableLiveData.postValue(response.body());
                    isLoading.postValue(false);


                }else {

                    try {

                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        detail.postValue(detailResponse.getDetail());
                        isLoading.postValue(false);
                        status_code.postValue(response.code());




                    }catch (Exception exception){
                        /*try {
                            JSONObject jsonObject = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            detail.postValue(jsonObject.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        detail.postValue(response.code()+": "+"Something went error");
                        isLoading.postValue(false);
                        status_code.postValue(response.code());





                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<ListLocation> call, @NonNull Throwable t) {

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
