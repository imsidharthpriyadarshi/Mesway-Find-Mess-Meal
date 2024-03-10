package in.mesway.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import in.mesway.ViewModels.ActivityIsLiveViewModel;
import in.mesway.databinding.ActivityMessDetailBinding;
public class MessDetailActivity extends AppCompatActivity {
    protected ActivityMessDetailBinding messDetailBinding;
    private ActivityIsLiveViewModel activityIsLiveViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messDetailBinding = ActivityMessDetailBinding.inflate(getLayoutInflater());
        setContentView(messDetailBinding.getRoot());
        activityIsLiveViewModel = new ViewModelProvider(this).get(ActivityIsLiveViewModel.class);
        activityIsLiveViewModel.setIsLiveDetailActivity(true);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityIsLiveViewModel.setIsLiveDetailActivity(false);

    }
}