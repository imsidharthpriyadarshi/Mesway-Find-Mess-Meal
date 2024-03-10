package in.mesway.ViewModels;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import in.mesway.Repository.MealsRepository;
import in.mesway.Response.UpcomingMeal.UpcomingMeals;

public class UpcomingMealViewModel extends AndroidViewModel {
    private static MealsRepository mealsRepository;
    public MutableLiveData<UpcomingMeals> upcomingMealsMutableLiveData;
    public MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public MutableLiveData<String> detail;

    public UpcomingMealViewModel(@NonNull Application application) {
        super(application);
        mealsRepository = MealsRepository.getInstance(application.getApplicationContext());
        upcomingMealsMutableLiveData=mealsRepository.upcomingMealsMutableLiveData;
        isLoading= mealsRepository.isLoading;
        detail=mealsRepository.detail;
        status_code = mealsRepository.status_code;

    }

    public void getUpcomingMeal(Context context){
        mealsRepository.getUpcomingMealInBackGroundThread(context);

    }
}
