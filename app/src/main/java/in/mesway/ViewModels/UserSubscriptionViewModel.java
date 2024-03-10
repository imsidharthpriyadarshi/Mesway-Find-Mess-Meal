package in.mesway.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.mesway.Response.UpcomingMeal.UserSubscription;

public class UserSubscriptionViewModel extends ViewModel {
    private MutableLiveData<UserSubscription> userSubscriptionMutableLiveData = new MutableLiveData<>();

    public LiveData<UserSubscription> getUserSubsLiveData(){
        return  userSubscriptionMutableLiveData;

    }

    public void setUserSubscriptionMutableLiveData(UserSubscription userSubscription){
        userSubscriptionMutableLiveData.postValue(userSubscription);
    }


}
