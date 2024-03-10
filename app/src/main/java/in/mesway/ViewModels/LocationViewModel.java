package in.mesway.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.mesway.Repository.LocationRepository;
import in.mesway.Response.Location.ListLocation;

public class LocationViewModel extends AndroidViewModel {
    private LocationRepository locationRepository;
    public MutableLiveData<Boolean> isLoading;
    public MutableLiveData<String> detail;
    public MutableLiveData<Integer> status_code;
    public MutableLiveData<ListLocation> listLocationMutableLiveData;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository =LocationRepository.getInstance(application.getApplicationContext());
        listLocationMutableLiveData=locationRepository.listLocationMutableLiveData;
        isLoading= locationRepository.isLoading;
        detail=locationRepository.detail;
        status_code = locationRepository.status_code;

    }

    public void getNearestMess(Context context){
        locationRepository.getNearestMessLocationInBackgroundThread(context);

    }
}
