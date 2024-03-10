package in.mesway.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivityIsLiveViewModel extends ViewModel {
    private MutableLiveData<Boolean> isLiveMainActivity = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLiveDetailActivity = new MutableLiveData<>();


    public LiveData<Boolean> getIsLiveMainActivity(){
        return isLiveMainActivity;

    }

    public void setIsLiveMainActivity(Boolean isLive){
        isLiveMainActivity.postValue(isLive);

    }
    public LiveData<Boolean> getIsLiveDetailActivity(){
        return isLiveDetailActivity;

    }

    public void setIsLiveDetailActivity(Boolean isLive){
        isLiveDetailActivity.postValue(isLive);

    }


}
