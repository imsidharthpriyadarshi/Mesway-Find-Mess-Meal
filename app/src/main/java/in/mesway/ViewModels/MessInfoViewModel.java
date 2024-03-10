package in.mesway.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.mesway.Repository.MessInfoRepository;
import in.mesway.Response.Location.MessInfo;

public class MessInfoViewModel extends AndroidViewModel {
    public MessInfoRepository messInfoRepository;
    public MutableLiveData<MessInfo> messInfoMutableLiveData;
    public MutableLiveData<Boolean> isLoading;
    public MutableLiveData<String> detail;
    public MutableLiveData<Integer> status_code;

    public MessInfoViewModel(@NonNull Application application) {
        super(application);
        messInfoRepository= MessInfoRepository.getInstance(application.getApplicationContext());
        messInfoMutableLiveData=messInfoRepository.messInfoMutableLiveData;
        isLoading= messInfoRepository.isLoading;
        detail= messInfoRepository.detail;
        status_code=messInfoRepository.status_code;
    }

    public void getMessInfo(Context context){
        messInfoRepository.getMessInfoInBackGroundThread(context);
    }
}
