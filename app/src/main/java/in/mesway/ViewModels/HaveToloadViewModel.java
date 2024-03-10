package in.mesway.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HaveToloadViewModel extends ViewModel {
    private MutableLiveData<Boolean> have_to_load =new MutableLiveData<>();

    private MutableLiveData<Boolean> subs_have_to_load=new MutableLiveData<>();
    private MutableLiveData <Boolean> order_have_to_load=new MutableLiveData<>();
    private MutableLiveData<Boolean> address_have_to_load= new MutableLiveData<>();
    private MutableLiveData<Boolean> detail_address_have_to_load=new MutableLiveData<>();



    public LiveData<Boolean> getSubs_have_to_load() {
        return subs_have_to_load;
    }

    public void setSubs_have_to_load(boolean r_subs_have_to_load) {
       subs_have_to_load.postValue(r_subs_have_to_load);
    }

    public LiveData<Boolean> getOrder_have_to_load() {
        return order_have_to_load;
    }

    public void setOrder_have_to_load(boolean r_order_have_to_load) {
        order_have_to_load.postValue(r_order_have_to_load);
    }

    public LiveData<Boolean> getAddress_have_to_load() {
        return address_have_to_load;
    }

    public void setAddress_have_to_load(boolean r_address_have_to_load) {
        address_have_to_load.postValue(r_address_have_to_load);
    }

    public LiveData<Boolean> getDetail_address_have_to_load() {
        return detail_address_have_to_load;
    }

    public void setDetail_address_have_to_load(boolean r_detail_address_have_to_load) {
        detail_address_have_to_load.postValue(r_detail_address_have_to_load);
    }

    public LiveData<Boolean> isHave_to_load() {
        return have_to_load;
    }

    public void setHave_to_load(boolean have_load) {
        have_to_load.postValue(have_load);

    }
}
