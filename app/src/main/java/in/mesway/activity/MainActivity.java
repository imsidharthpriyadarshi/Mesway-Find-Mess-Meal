package in.mesway.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import in.mesway.R;
import in.mesway.ViewModels.ActivityIsLiveViewModel;
import in.mesway.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mainBinding;
    private ActivityIsLiveViewModel activityIsLiveViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        activityIsLiveViewModel= new ViewModelProvider(this).get(ActivityIsLiveViewModel.class);
        activityIsLiveViewModel.setIsLiveMainActivity(true);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;

        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView= mainBinding.menuBottom;
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        mainBinding.updateApp.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/detail?id=" +getPackageName())));

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityIsLiveViewModel.setIsLiveMainActivity(false);


    }
}