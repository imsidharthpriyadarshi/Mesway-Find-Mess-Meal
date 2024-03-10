package in.mesway.fragments.details;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import in.mesway.Adapters.DetailAddressAdapter;
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
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentAddressDetailBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AddressDetailFragment extends Fragment {
    private FragmentAddressDetailBinding fragmentAddressDetailBinding;
    private NavController navController;
    private UserInfoViewModel userInfoViewModel;
    private UserInfo user_detail_info;
    private Activity activity;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;
    private UserSubscription user_subscription;
    private boolean btn_clicked;
    private UserSubscriptionViewModel userSubscriptionViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAddressDetailBinding = FragmentAddressDetailBinding.inflate(inflater,container,false);

        return fragmentAddressDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_clicked=false;
        initView(view);
        userSubscriptionObserver();
        statusCodeObserver(view);
        isLoading();
        clickHandle();


    }


    private void addAddressToSubscription(String location_id,View view) {
        if (btn_clicked){
        Call<UserSubscription> userSubscriptionCall= apiInterface.add_address_to_subscription(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null),user_subscription.getSubscriptionId(),sharedPreferences.getString(Constant.USER_ID,null),location_id);
        userSubscriptionCall.enqueue(new Callback<UserSubscription>() {
            @Override
            public void onResponse(@NonNull Call<UserSubscription> call, @NonNull Response<UserSubscription> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code()==200){
                    try {

                        navController.navigate(R.id.action_addressDetailFragment_to_finalPaymentFragment);
                    }catch (Exception ignored){}
                    alertDialog.dismiss();

                }else {
                    try {
                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        Snackbar.make(view,response.code()+": "+detailResponse.getDetail(),Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                                .show();
                        alertDialog.dismiss();

                    }catch (Exception e){
                        Snackbar.make(view,response.code()+": "+e.getMessage(),Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                                .show();
                        alertDialog.dismiss();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserSubscription> call, @NonNull Throwable t) {
                Snackbar.make(view,"Something went error: "+t.getMessage(),Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                        .show();
                alertDialog.dismiss();
            }
        });
}
    }

    private void userSubscriptionObserver() {
        userSubscriptionViewModel.getUserSubsLiveData().observe((LifecycleOwner) activity, userSubscription -> user_subscription= userSubscription);
    }

    private void initView(View view) {
        navController= Navigation.findNavController(view);
        userSubscriptionViewModel=new ViewModelProvider((ViewModelStoreOwner) activity).get(UserSubscriptionViewModel.class);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        alertDialog = Reusable.alertDialog(activity);
        userInfoViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UserInfoViewModel.class);

    }

    private void isLoading() {
        Observer<Boolean> is_loading = aBoolean -> {
            if (aBoolean) {
                alertDialog.show();
            } else {

                alertDialog.dismiss();

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
        Observer<String> detail_observer = s -> Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();


        userInfoViewModel.detail.observe((LifecycleOwner) activity, detail_observer);


    }

    private void getUserDetailInfo(View view) {
        Observer<UserInfo> userInfoObserver = userInfo -> {
            user_detail_info = userInfo;

            setupRecycleView(view);


        };

        userInfoViewModel.userInfoMutableLiveData.observe((LifecycleOwner) activity, userInfoObserver);


    }


    private void setupRecycleView(View views) {
        AddressInterface addressInterface = (main_btn, edit_btn, location_id, user_id) -> main_btn.setOnClickListener(view -> {
            btn_clicked = true;
            alertDialog.show();

            addAddressToSubscription(location_id, view);


        });


        DetailAddressAdapter detailAddressAdapter = new DetailAddressAdapter(getAddressList(), activity, addressInterface);
        fragmentAddressDetailBinding.recAddress.setAdapter(detailAddressAdapter);
        fragmentAddressDetailBinding.recAddress.setLayoutManager(new LinearLayoutManager(activity));


    }

    private void clickHandle() {
        fragmentAddressDetailBinding.btnAddAddress.setOnClickListener(view -> {
            try {
                navController.navigate(R.id.action_addressDetailFragment_to_addAddressFragment2);
            } catch (Exception ignored) {

            }
        });


        fragmentAddressDetailBinding.refreshBtn.setOnClickListener(view -> userInfoViewModel.getUserDetailInfo(activity));


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

            fragmentAddressDetailBinding.recAddress.setVisibility(View.GONE);
            fragmentAddressDetailBinding.lnNoUpcomingMeal.setVisibility(View.VISIBLE);

        }else {

            fragmentAddressDetailBinding.recAddress.setVisibility(View.VISIBLE);
            fragmentAddressDetailBinding.lnNoUpcomingMeal.setVisibility(View.GONE);
        }
        return addressesModelList;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager layoutManager = (LinearLayoutManager) fragmentAddressDetailBinding.recAddress.getLayoutManager();
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
    public void onDestroy() {
        super.onDestroy();
    }
}