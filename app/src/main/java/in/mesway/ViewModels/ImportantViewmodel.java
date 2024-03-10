package in.mesway.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImportantViewmodel extends ViewModel {
    private MutableLiveData<Boolean> access_token_update=new MutableLiveData<>();
    private MutableLiveData<Boolean> location_update= new MutableLiveData<>();

    public LiveData<Boolean> getAccess_token_update() {
        return access_token_update;
    }

    public LiveData<Boolean> getLocation_update() {
        return location_update;
    }

    public void setAccess_token_update(boolean is_updated){

        access_token_update.postValue(is_updated);

    }

    public void setLocation_update(boolean is_updated){
        location_update.postValue(is_updated);
    }

}
