package in.mesway.fragments.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import in.mesway.ViewModels.ImportantViewmodel;
import in.mesway.ViewModels.UserInfoViewModel;
import in.mesway.activity.MainActivity;
import in.mesway.activity.SignupActivity;
import in.mesway.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding settingsBinding;
    private NavController navController;
    private Activity activity;
    private UserInfoViewModel userInfoViewModel;
    private UserInfo user_detail_info;
    private SharedPreferences sharedPreferences;

    private ImportantViewmodel importantViewmodel;
    private boolean isConnected;
    private Snackbar snackbar;






    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);

        return settingsBinding.getRoot();
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.mainBinding.menuBottom.setVisibility(View.VISIBLE);
        activity.mainBinding.linearBottomMenu.setVisibility(View.VISIBLE);


        initView();

       // importantDataObserver(view);


        if (sharedPreferences.getString(Constant.FULL_NAME,null)!=null) {


           settingsBinding.txtProfile.setText(sharedPreferences.getString(Constant.FULL_NAME, null));
        }
        if (isConnected) {

            if (sharedPreferences.getString(Constant.ACCESS_TOKEN, null) == null) {
                settingsBinding.btnLogout.setText("Login");
                settingsBinding.btnLogout.setStrokeColor(ContextCompat.getColorStateList(activity, R.color.green));
                settingsBinding.btnLogout.setTextColor(ContextCompat.getColor(activity, R.color.green));
                settingsBinding.btnLogout.setCompoundDrawableTintList(ContextCompat.getColorStateList(activity, R.color.green));
                settingsBinding.txtProfile.setText("Guest");


            } else {
                statusCodeObserver();
                isLoading();

            }
            clickOperation();
        }else {
         snackbar=   Snackbar.make(view,"No internet connection",Snackbar.LENGTH_SHORT);
         snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                 snackbar.show();


        }
    }

    private void initView() {
        isConnected= Reusable.CheckInternet(activity);
        importantViewmodel= new ViewModelProvider((ViewModelStoreOwner) activity).get(ImportantViewmodel.class);
        userInfoViewModel=new ViewModelProvider((ViewModelStoreOwner) activity).get(UserInfoViewModel.class);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);

    }
    private void importantDataObserver(View view) {
        importantViewmodel.getAccess_token_update().observe((LifecycleOwner) activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (view!=null) {
                    userInfoViewModel.getUserDetailInfo(activity);
                }
            }
        });

        importantViewmodel.getLocation_update().observe((LifecycleOwner) activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (view!=null) {
                    userInfoViewModel.getUserDetailInfo(activity);
                }
            }
        });
    }
    private void isLoading(){
        Observer<Boolean> is_loading=new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){

                }else {


                }
            }
        };

        userInfoViewModel.isLoading.observe((LifecycleOwner) activity,is_loading);


    }

    private void statusCodeObserver(){
        Observer<Integer> status_code_observer= integer -> {
            if (integer==200){
                getUserDetailInfo();



            }else{
                detailObserver();



            }


        };

        userInfoViewModel.status_code.observe((LifecycleOwner) activity,status_code_observer);

    }

    private void detailObserver() {
        Observer<String> detail_observer=new Observer<String>() {
            @Override
            public void onChanged(String s) {


            }
        };


        userInfoViewModel.detail.observe((LifecycleOwner) activity,detail_observer);


    }
    
    private void getUserDetailInfo(){
        Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                user_detail_info = userInfo;
                profileOperations();
            }
        };
        
        userInfoViewModel.userInfoMutableLiveData.observe((LifecycleOwner) activity,userInfoObserver);
        
        
    }

    private void profileOperations() {
        if (sharedPreferences.getString(Constant.FULL_NAME,null)==null){


            settingsBinding.txtProfile.setText(user_detail_info.getFullName());
            sharedPreferences.edit()
                    .putString(Constant.FULL_NAME,user_detail_info.getFullName()).apply();
        }else {

            settingsBinding.txtProfile.setText(sharedPreferences.getString(Constant.FULL_NAME,null));
        }

    }



    private void clickOperation() {
        settingsBinding.profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null ) {
                        try {

                            navController.navigate(R.id.action_settingsFragment_to_profileSettingsFragment);
                        }catch (Exception ignored){

                        }

                    }else {

                        try {
                            navController.navigate(SettingsFragmentDirections.actionGlobalDoLoginFragment());

                        }catch (Exception ignored){

                        }

                    }

                } catch (Exception ignored) {
                }
            }
        });

        settingsBinding.consOrderHostory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    navController.navigate(R.id.action_settingsFragment_to_orderHistoryFragment);
                }catch (Exception ignored){

                }
            }
        });


        settingsBinding.consAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null ) {
                        try {

                            navController.navigate(R.id.action_settingsFragment_to_addressFragment);
                        }catch (Exception ignored){}

                    }else {
                        try {

                            navController.navigate(SettingsFragmentDirections.actionGlobalDoLoginFragment());
                        }catch (Exception ignored){}

                    }
                } catch (Exception ignored) {
                }
            }
        });

        settingsBinding.consContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    navController.navigate(R.id.action_settingsFragment_to_contactUsFragment);

                } catch (Exception ignored) {
                }
            }
        });
        settingsBinding.consTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    navController.navigate(R.id.action_settingsFragment_to_webviewFragment);


                } catch (Exception ignored) {
                }
            }
        });

        settingsBinding.consRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null ) {
                        try {

                            navController.navigate(R.id.action_settingsFragment_to_rateUsDialogFragment);
                        }catch (Exception ignored){}


                    }else {
                        try {

                            navController.navigate(SettingsFragmentDirections.actionGlobalDoLoginFragment());
                        }catch (Exception ignored){}

                    }

                } catch (Exception ignored) {
                }
            }
        });

        settingsBinding.consSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null ) {
                        try {

                            navController.navigate(R.id.action_settingsFragment_to_anySuggestionFragment);
                        }catch (Exception ignored){}

                    }else {
                        try {

                            navController.navigate(SettingsFragmentDirections.actionGlobalDoLoginFragment());
                        }catch (Exception ignored){}

                    }

                } catch (Exception ignored) {
                }
            }
        });

        settingsBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {

                    new AlertDialog.Builder(activity)
                            .setTitle("Are You sure? ")
                                    .setMessage("Do You want to logout ?")
                                            .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    sharedPreferences.edit()
                                                            .clear()
                                                            .apply();
                                                    startActivity(new Intent(activity, SignupActivity.class));
                                                    activity.finish();
                                                }
                                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();



                }else {

                   Intent intent= new Intent(activity, SignupActivity.class);
                   intent.putExtra("isActivityLive","active");

                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null){
                settingsBinding.btnLogout.setText("LOGOUT");
                settingsBinding.btnLogout.setStrokeColor(ContextCompat.getColorStateList(activity,R.color.red));
                settingsBinding.btnLogout.setTextColor(ContextCompat.getColor(activity,R.color.red));
                settingsBinding.btnLogout.setCompoundDrawableTintList(ContextCompat.getColorStateList(activity,R.color.red));

            }
        }catch (Exception ignored){

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