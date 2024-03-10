package in.mesway.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import in.mesway.Response.DetailResponse;
import in.mesway.Response.UpcomingMeal.UpcomingMeals;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MealsRepository {

    private ApiInterface apiInterface;
    private static MealsRepository mealsRepository;
    public MutableLiveData<UpcomingMeals> upcomingMealsMutableLiveData;
    public MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public MutableLiveData<String> detail;
    private SharedPreferences sharedPreferences;
    private Executor executor = Executors.newSingleThreadExecutor();


    public static MealsRepository getInstance(Context context){
        return mealsRepository = new MealsRepository(context);


    }

    private MealsRepository(Context context){
        upcomingMealsMutableLiveData= new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        status_code = new MutableLiveData<>();
        detail = new MutableLiveData<>();
        getUpcomingMealInBackGroundThread(context);


    }

    public void getUpcomingMealInBackGroundThread(Context context){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getUpcomingMeals(context);
                }catch (Exception e){}
            }
        });


    }

    private void getUpcomingMeals(Context context){
        isLoading.postValue(true);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,context);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        Call<UpcomingMeals> upcomingMealsCall= apiInterface.get_upcoming_meal(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null), sharedPreferences.getString(Constant.USER_ID,null));
        upcomingMealsCall.enqueue(new Callback<UpcomingMeals>() {
            @Override
            public void onResponse(@NonNull Call<UpcomingMeals> call, @NonNull Response<UpcomingMeals> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code()==200){
                    status_code.postValue(response.code());
                    upcomingMealsMutableLiveData.postValue(response.body());
                    isLoading.postValue(false);


                }else {

                    try {

                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        detail.postValue(detailResponse.getDetail());
                        isLoading.postValue(false);
                        status_code.postValue(response.code());




                    }catch (Exception exception){
                        try {
                            JSONObject jsonObject = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            detail.postValue("Something went error");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isLoading.postValue(false);
                        status_code.postValue(response.code());





                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpcomingMeals> call, @NonNull Throwable t) {
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
