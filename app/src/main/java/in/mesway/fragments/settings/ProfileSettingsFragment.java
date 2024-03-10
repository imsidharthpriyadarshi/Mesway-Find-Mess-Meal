package in.mesway.fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import in.mesway.R;
import in.mesway.Response.UpcomingMeal.UserInfo;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.UserInfoViewModel;
import in.mesway.activity.MainActivity;
import in.mesway.databinding.FragmentProfileSettingsBinding;

public class ProfileSettingsFragment extends Fragment {
    private FragmentProfileSettingsBinding profileSettingsBinding;
    private NavController navController;
    private SharedPreferences sharedPreferences;
    private UserInfoViewModel userInfoViewModel;
    private UserInfo user_detail_info;
    private AlertDialog alertDialog;
    private Activity activity;
    private boolean isConnected;
    private Snackbar snackbar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileSettingsBinding = FragmentProfileSettingsBinding.inflate(inflater, container, false);

        return profileSettingsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.mainBinding.linearBottomMenu.setVisibility(View.GONE);

        initView();

        if (isConnected) {
            clickHandle();
            statusCodeObserver();
            isLoading();
        }else {
           snackbar= Snackbar.make(view,"No internet connection",Snackbar.LENGTH_SHORT);
                   snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                           snackbar.show();
        }
    }

    private void initView() {
        isConnected=Reusable.CheckInternet(activity);
        userInfoViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UserInfoViewModel.class);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        alertDialog = Reusable.alertDialog(activity);

    }

    private void isLoading() {
        Observer<Boolean> is_loading = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    alertDialog.show();

                } else {
                    alertDialog.dismiss();

                }
            }
        };

        userInfoViewModel.isLoading.observe((LifecycleOwner) activity, is_loading);


    }

    private void statusCodeObserver() {
        Observer<Integer> status_code_observer = integer -> {
            if (integer == 200) {
                getUserDetailInfo();


            } else {
                detailObserver();


            }


        };

        userInfoViewModel.status_code.observe((LifecycleOwner) activity, status_code_observer);

    }

    private void detailObserver() {
        Observer<String> detail_observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {

                Toast.makeText(activity, " " + s, Toast.LENGTH_SHORT).show();

            }
        };


        userInfoViewModel.detail.observe((LifecycleOwner) activity, detail_observer);


    }

    private void getUserDetailInfo() {
        Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                user_detail_info = userInfo;
                profileOperations();
            }
        };

        userInfoViewModel.userInfoMutableLiveData.observe((LifecycleOwner) activity, userInfoObserver);


    }

    private void profileOperations() {
        if (user_detail_info != null) {
            profileSettingsBinding.etFullName.setText(user_detail_info.getFullName());
            profileSettingsBinding.etEmail.setText(user_detail_info.getEmail());
            profileSettingsBinding.etPhoneNo.setText(user_detail_info.getMobileNumber());

        }
    }


    private void clickHandle() {
        profileSettingsBinding.imgEditPersonaInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileSettingsBinding.etlEmail.setEnabled(true);
                profileSettingsBinding.etlFullName.setEnabled(true);
                profileSettingsBinding.etlPhoneNo.setEnabled(true);
                profileSettingsBinding.btnUpdateInfo.setEnabled(true);

            }
        });

        profileSettingsBinding.imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    navController.popBackStack();
                }catch (Exception ignored){}
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            statusCodeObserver();
            isLoading();
        } catch (Exception ignored) {


        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=context instanceof Activity ?(Activity) context:null;


    }
    @Override
    public void onPause() {
        super.onPause();
        if (snackbar!=null){
            snackbar.dismiss();

        }
    }


}