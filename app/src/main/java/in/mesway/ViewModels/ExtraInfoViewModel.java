package in.mesway.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.mesway.Repository.OtherInformationRepository;
import in.mesway.Response.ExtraInfoResponse;

public class ExtraInfoViewModel extends AndroidViewModel {
    private static OtherInformationRepository otherInformationRepository;
    public MutableLiveData<ExtraInfoResponse> extraInfoResponseMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;

    public ExtraInfoViewModel(@NonNull Application application) {
        super(application);
        otherInformationRepository= OtherInformationRepository.getInstance(application.getApplicationContext());
        extraInfoResponseMutableLiveData=otherInformationRepository.extraInfoResponseMutableLiveData;
        isLoading= otherInformationRepository.isLoading;
        detail=otherInformationRepository.detail;
        status_code = otherInformationRepository.status_code;
    }
}
