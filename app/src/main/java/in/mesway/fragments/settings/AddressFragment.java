package in.mesway.fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.mesway.Adapters.AddressAdapter;
import in.mesway.Models.AddressesModel;
import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.UpcomingMeal.UserInfo;
import in.mesway.Response.UpcomingMeal.UserSubscription;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.UserInfoViewModel;
import in.mesway.ViewModels.UserSubscriptionViewModel;
import in.mesway.activity.MainActivity;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentAddressBinding;
import in.mesway.fragments.details.AddressInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AddressFragment extends Fragment {
    private FragmentAddressBinding addressBinding;
    private AddressAdapter addressAdapter;
    private NavController navController;
    private AddressInterface addressInterface;
    private UserInfoViewModel userInfoViewModel;
    private UserInfo user_detail_info;
    private Activity activity;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;
    private Snackbar snackbar;
    private boolean isConnected;


    public AddressFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addressBinding = FragmentAddressBinding.inflate(inflater, container, false);


        return addressBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initView();

            MainActivity mainActivity = (MainActivity) getActivity();
            assert mainActivity != null;
            mainActivity.mainBinding.linearBottomMenu.setVisibility(View.GONE);

            if (isConnected) {
                statusCodeObserver(view);
                isLoading();
                clickHandle();
            }else{

             snackbar=   Snackbar.make(view,"No internet connection",Snackbar.LENGTH_SHORT).setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
             snackbar.show();

            }




    }





    private void initView() {
        isConnected=Reusable.CheckInternet(activity);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        alertDialog = Reusable.alertDialog(activity);
        userInfoViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UserInfoViewModel.class);

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

    private void statusCodeObserver(View view) {
        Observer<Integer> status_code_observer = integer -> {
            if (integer == 200) {
                getUserDetailInfo(view);



            } else {


                detailObserver(view);
            }


        };

        userInfoViewModel.status_code.observe((LifecycleOwner) activity, status_code_observer);

    }

    private void detailObserver(View view) {
        Observer<String> detail_observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {

              snackbar=  Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
              snackbar.setBackgroundTint(ContextCompat.getColor(activity,R.color.red));
              snackbar.show();
            }
        };


        userInfoViewModel.detail.observe((LifecycleOwner) activity, detail_observer);


    }

    private void getUserDetailInfo(View view) {
        Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                user_detail_info = userInfo;

                    setupRecycleView(view);


            }
        };

        userInfoViewModel.userInfoMutableLiveData.observe((LifecycleOwner) activity, userInfoObserver);


    }


    private void setupRecycleView(View views) {
        addressInterface= new AddressInterface() {
            @Override
            public void getAllBtnView(MaterialButton main_btn, MaterialButton edit_btn, String location_id, String user_id) {

                main_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog alert = new AlertDialog.Builder(activity)
                                .setTitle("Are You sure ?")
                                .setMessage("Plz Confirm, Do you want to delete this address")
                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Call<Boolean> delete_address = apiInterface.delete_address(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), location_id, user_id);
                                        delete_address.enqueue(new Callback<Boolean>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                                                Gson gson = new GsonBuilder().create();
                                                if (response.code() == 200) {
                                                    alertDialog.dismiss();
                                                  snackbar=  Snackbar.make(views, "Successfully deleted", Snackbar.LENGTH_SHORT);
                                                  snackbar.show();
                                                    userInfoViewModel.getUserDetailInfo(activity);

                                                } else {
                                                    try {
                                                        assert response.errorBody() != null;
                                                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                                                       snackbar= Snackbar.make(views, response.code() + ": " + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                                                       snackbar.show();
                                                        alertDialog.dismiss();


                                                    } catch (Exception e) {

                                                      snackbar=  Snackbar.make(views, response.code() + ": " + e.getMessage(), Snackbar.LENGTH_SHORT);
                                                      snackbar.show();
                                                        alertDialog.dismiss();

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

                                              snackbar=  Snackbar.make(views, "Something went error" + t.getMessage(), Snackbar.LENGTH_SHORT);
                                              snackbar.show();
                                                alertDialog.dismiss();

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();


                    }
                });
            }
        };



        addressAdapter = new AddressAdapter(getAddressList(), activity, addressInterface);
        addressBinding.recAddress.setAdapter(addressAdapter);
        addressBinding.recAddress.setLayoutManager(new LinearLayoutManager(activity));


    }

    private void clickHandle() {
        addressBinding.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    navController.navigate(R.id.action_addressFragment_to_addAddressFragment);
                } catch (Exception ignored) {

                }
            }
        });


        addressBinding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfoViewModel.getUserDetailInfo(activity);
            }
        });


    }

    private List<AddressesModel> getAddressList() {
        List<AddressesModel> addressesModelList = new ArrayList<>();
        if (user_detail_info != null) {
            for (int i = 0; i < user_detail_info.getAddresses().size(); i++) {
                if (Objects.equals(user_detail_info.getAddresses().get(i).getLocationType(), "home") && !(user_detail_info.getAddresses().get(i).getIs_deleted())) {
                    addressesModelList.add(new AddressesModel(R.drawable.outline_home_24, "Home", user_detail_info.getAddresses().get(i).getFullName(), user_detail_info.getAddresses().get(i).getFullAddress(), user_detail_info.getAddresses().get(i).getLandmark(), user_detail_info.getAddresses().get(i).getState() + "(" + user_detail_info.getAddresses().get(i).getPinCode() + ")", user_detail_info.getAddresses().get(i).getMobileNumber(), user_detail_info.getAddresses().get(i).getLocationId(), user_detail_info.getAddresses().get(i).getUserId()));
                } else if (Objects.equals(user_detail_info.getAddresses().get(i).getLocationType(), "work") && !(user_detail_info.getAddresses().get(i).getIs_deleted())){
                    addressesModelList.add(new AddressesModel(R.drawable.baseline_apartment_24, "Work", user_detail_info.getAddresses().get(i).getFullName(), user_detail_info.getAddresses().get(i).getFullAddress(), user_detail_info.getAddresses().get(i).getLandmark(), user_detail_info.getAddresses().get(i).getState() + "(" + user_detail_info.getAddresses().get(i).getPinCode() + ")", user_detail_info.getAddresses().get(i).getMobileNumber(), user_detail_info.getAddresses().get(i).getLocationId(), user_detail_info.getAddresses().get(i).getUserId()));
                }
            }
        }

        if (addressesModelList.size()==0){

            addressBinding.recAddress.setVisibility(View.GONE);
            addressBinding.lnNoUpcomingMeal.setVisibility(View.VISIBLE);

        }else {

            addressBinding.recAddress.setVisibility(View.VISIBLE);
            addressBinding.lnNoUpcomingMeal.setVisibility(View.GONE);
        }
        return addressesModelList;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager layoutManager = (LinearLayoutManager) addressBinding.recAddress.getLayoutManager();
        assert layoutManager != null;
        int scrolled_position = layoutManager.findLastVisibleItemPosition();
        outState.putInt("scroll_position", scrolled_position);


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.getInt("scroll_position");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;


    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar!=null){
            snackbar.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}