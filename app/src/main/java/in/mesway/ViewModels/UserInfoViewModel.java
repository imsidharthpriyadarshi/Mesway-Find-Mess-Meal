package in.mesway.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import in.mesway.Repository.UserInfoRepository;
import in.mesway.Response.UpcomingMeal.UserInfo;

public class UserInfoViewModel extends AndroidViewModel {
    private UserInfoRepository userInfoRepository;
    public MutableLiveData<Boolean> isLoading;
    public MutableLiveData<String> detail;
    public MutableLiveData<Integer> status_code;
    public MutableLiveData<UserInfo> userInfoMutableLiveData;


    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        userInfoRepository = UserInfoRepository.getInstance(application.getApplicationContext());
        userInfoMutableLiveData=userInfoRepository.userInfoMutableLiveData;
        isLoading= userInfoRepository.isLoading;
        detail=userInfoRepository.detail;
        status_code = userInfoRepository.status_code;

    }

    public void getUserDetailInfo(Context context){
        userInfoRepository.getUserDetailInfoInBackgroundThread(context);

    }
}
