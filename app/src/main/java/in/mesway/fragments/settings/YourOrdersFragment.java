package in.mesway.fragments.settings;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.mesway.Adapters.YourOrderHistoryAdapter;
import in.mesway.Models.YourOrderHistoryModel;
import in.mesway.R;
import in.mesway.Response.UpcomingMeal.UserInfo;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.HaveToloadViewModel;
import in.mesway.ViewModels.UserInfoViewModel;
import in.mesway.activity.MainActivity;
import in.mesway.databinding.FragmentYourOrdersBinding;
import in.mesway.fragments.meals.YourOrderHistoryInterface;


public class YourOrdersFragment extends Fragment {
    private FragmentYourOrdersBinding yourOrdersBinding;
    private NavController navController;
    private YourOrderHistoryAdapter yourOrderHistoryAdapter;
    private YourOrderHistoryInterface yourOrderHistoryInterface;
    private SharedPreferences sharedPreferences;
    private UserInfoViewModel userInfoViewModel;
    private UserInfo user_detail_info;
    private Activity activity;
    private AlertDialog alertDialog;
    private Snackbar snackbar;

    private HaveToloadViewModel haveToloadViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        yourOrdersBinding = FragmentYourOrdersBinding.inflate(inflater, container, false);

        return yourOrdersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.mainBinding.linearBottomMenu.setVisibility(View.GONE);


        initView();
        //baad me dekhege
        /*haveToloadViewModel.getOrder_have_to_load().observe(activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (view!=null){
                    if (aBoolean){
                        userInfoViewModel.getUserDetailInfo(activity);
                    }
                }
            }
        });*/
        if (sharedPreferences.getString(Constant.ACCESS_TOKEN, null) == null) {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.yourOrderFragment, true)
                    .build();
            try {

                navController.navigate(YourOrdersFragmentDirections.actionGlobalDoLoginFragment(), navOptions);
            }catch (Exception ignored){}

        } else {
            clickHandle();
            statusCodeObserver(view);
            isLoading();
        }

    }

    private void initView() {
        alertDialog= Reusable.alertDialog(activity);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        userInfoViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UserInfoViewModel.class);

        haveToloadViewModel= new ViewModelProvider((ViewModelStoreOwner) activity).get(HaveToloadViewModel.class);
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
                getUserDetailInfo();




            }else {
                detailObserver(view);

            }


        };

        userInfoViewModel.status_code.observe((LifecycleOwner) activity, status_code_observer);

    }

    private void detailObserver(View view) {
        Observer<String> detail_observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {
              snackbar=  Snackbar.make(view,s,Snackbar.LENGTH_SHORT);
              snackbar.show();


            }
        };


        userInfoViewModel.detail.observe((LifecycleOwner) activity, detail_observer);


    }

    private void getUserDetailInfo() {
        Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                user_detail_info = userInfo;
                basicOperations();
                orderHistoryRecViewHandel();


            }
        };

        userInfoViewModel.userInfoMutableLiveData.observe((LifecycleOwner) activity, userInfoObserver);


    }

    @SuppressLint("SetTextI18n")
    private void basicOperations() {
        int delivered_meal = 0, stopped_meal = 0, subscription = 0;
        if (user_detail_info != null) {

            for (int i = 0; i < user_detail_info.getSubscriptions().size(); i++) {
                if (Objects.equals(user_detail_info.getSubscriptions().get(i).getStatus(), "active") || Objects.equals(user_detail_info.getSubscriptions().get(i).getStatus(), "expired")) {
                    delivered_meal = delivered_meal + user_detail_info.getSubscriptions().get(i).getDeliveredMeal();
                    stopped_meal = stopped_meal + user_detail_info.getSubscriptions().get(i).getHowManyMealCancel();
                }
            }

            yourOrdersBinding.tDeliveredMealsValue.setText(String.valueOf(delivered_meal));
            yourOrdersBinding.tStoppedMealValue.setText(String.valueOf(stopped_meal));
            if (user_detail_info.getSubscriptionCount()<10) {
                yourOrdersBinding.tTotalSubs.setText("0"+user_detail_info.getSubscriptionCount());

            }else {
                yourOrdersBinding.tTotalSubs.setText(String.valueOf(user_detail_info.getSubscriptionCount()));
            }
        } else {

            yourOrdersBinding.tDeliveredMealsValue.setText(String.valueOf(delivered_meal));
            yourOrdersBinding.tStoppedMealValue.setText(String.valueOf(stopped_meal));

            if (user_detail_info.getSubscriptionCount() < 10) {
                yourOrdersBinding.tTotalSubs.setText("0"+subscription);

            }else {
                yourOrdersBinding.tTotalSubs.setText(String.valueOf(subscription));

            }
        }


        }


        private void orderHistoryRecViewHandel () {
            yourOrderHistoryInterface = new YourOrderHistoryInterface() {
                @Override
                public void messConsClick(View cons_view) {

                }
            };
            yourOrderHistoryAdapter = new YourOrderHistoryAdapter(getOrderHistoryList(), activity, yourOrderHistoryInterface);
            yourOrdersBinding.recOrderHistory.setLayoutManager(new LinearLayoutManager(activity));
            yourOrdersBinding.recOrderHistory.setAdapter(yourOrderHistoryAdapter);
          //  yourOrdersBinding.recOrderHistory.setNestedScrollingEnabled(false);


        }

        private List<YourOrderHistoryModel> getOrderHistoryList () {
            List<YourOrderHistoryModel> orderHistoryModelList = new ArrayList<>();
            if (user_detail_info != null) {
                for (int i = 0; i < user_detail_info.getOrderHistory().size(); i++) {
                    orderHistoryModelList.add(new YourOrderHistoryModel(user_detail_info.getOrderHistory().get(i).getMess_basic_info().getMess_images().get(0).getBigImage().get(0), user_detail_info.getOrderHistory().get(i).getMess_basic_info().getMess_name(), user_detail_info.getOrderHistory().get(i).getMess_basic_info().getMess_location().get(0).getCompanyAddress(), user_detail_info.getOrderHistory().get(i).getStatus(), user_detail_info.getOrderHistory().get(i).getDelivery_date(), user_detail_info.getOrderHistory().get(i).getDelivery_time(), user_detail_info.getOrderHistory().get(i).getMeal_type(),user_detail_info.getOrderHistory().get(i).getMess_id()));
                }
            } else {
                //show what ever you want
            }

            if (orderHistoryModelList.size()==0){
                yourOrdersBinding.recOrderHistory.setVisibility(View.GONE);
                yourOrdersBinding.lnNoOrder.setVisibility(View.VISIBLE);

            }else {

                yourOrdersBinding.recOrderHistory.setVisibility(View.VISIBLE);
                yourOrdersBinding.lnNoOrder.setVisibility(View.GONE);

            }

            return orderHistoryModelList;

        }

        private void clickHandle () {
            yourOrdersBinding.consSubscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        navController.navigate(R.id.action_yourOrderFragment_to_subscriptionsFragment);
                    }catch (Exception ignored){}
                }
            });

            yourOrdersBinding.imgBackArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        navController.popBackStack();
                    }catch (Exception ignored){}
                }
            });
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