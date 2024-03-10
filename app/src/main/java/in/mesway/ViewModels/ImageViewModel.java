package in.mesway.ViewModels;

import android.graphics.Bitmap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImageViewModel extends ViewModel {
    private MutableLiveData<Bitmap> breakfast_img= new MutableLiveData<>();
    private MutableLiveData<Bitmap> dinner_img=new MutableLiveData<>();
    private MutableLiveData<Bitmap> lunch_img=new MutableLiveData<>();

    public LiveData<Bitmap> getBreakfastImg() {
        return breakfast_img;
    }

    public LiveData<Bitmap> getDinnerImg() {
        return dinner_img;
    }
    public LiveData<Bitmap> getLunchImg() {
        return lunch_img;
    }


    public void setBreakfast_img(Bitmap bitmap) {
       breakfast_img.postValue(bitmap);
    }

    public void setLunch_img(Bitmap bitmap) {
        lunch_img.postValue(bitmap);

    }
    public void setDinner_img(Bitmap bitmap) {
        dinner_img.postValue(bitmap);
    }
}
