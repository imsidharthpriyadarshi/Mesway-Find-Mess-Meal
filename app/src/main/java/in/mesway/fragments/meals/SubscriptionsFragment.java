package in.mesway.fragments.meals;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.mesway.Adapters.SubscriptionAdapter;
import in.mesway.Models.SubscriptionModel;
import in.mesway.R;
import in.mesway.Response.UpcomingMeal.UserInfo;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.UserInfoViewModel;
import in.mesway.activity.MainActivity;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentSubscriptionsBinding;
import retrofit2.Retrofit;

public class SubscriptionsFragment extends Fragment {

    private FragmentSubscriptionsBinding subscriptionsBinding;
    private SubscriptionAdapter subscriptionAdapter;
    private SubscriptionInterface subscriptionInterface;



    private UserInfoViewModel userInfoViewModel;
    private UserInfo user_detail_info;
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;
    private Snackbar snackbar;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        subscriptionsBinding= FragmentSubscriptionsBinding.inflate(inflater,container,false);
        return subscriptionsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            MainActivity mainActivity = (MainActivity) getActivity();
            assert mainActivity != null;
            mainActivity.mainBinding.menuBottom.setVisibility(View.GONE);
        }catch (Exception ignored){

        }

        initView();
        statusCodeObserver(view);
        isLoading();
        clickHandel();



    }

    private void clickHandel() {
        subscriptionsBinding.imgSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    userInfoViewModel.getUserDetailInfo(activity);
                }catch (Exception ignored){

                }
            }
        });
    }

    private void initView() {
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
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



            } else{
                detailObserver(view);


            }


        };

        userInfoViewModel.status_code.observe((LifecycleOwner) activity, status_code_observer);

    }

    private void detailObserver(View view) {
        Observer<String> detail_observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {

                snackbar= Snackbar.make(view, s, Snackbar.LENGTH_SHORT);

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

                subscriptionsRecViewHandel();


            }
        };

        userInfoViewModel.userInfoMutableLiveData.observe((LifecycleOwner) activity, userInfoObserver);


    }


    private void subscriptionsRecViewHandel() {
        subscriptionInterface= new SubscriptionInterface() {
            @Override
            public void messConsClick(View cons_view) {

            }

            @Override
            public void otherViewOperations(View cons_security_deposit, TextView payment_status, TextView security_deposit_status) {


            }
        };
        subscriptionAdapter= new SubscriptionAdapter(getSubscriptionList(),activity,subscriptionInterface);

        subscriptionsBinding.recSubscriptions.setLayoutManager(new LinearLayoutManager(activity));
        subscriptionsBinding.recSubscriptions.setAdapter(subscriptionAdapter);
       // subscriptionsBinding.recSubscriptions.setNestedScrollingEnabled(false);

    }

    @SuppressLint("SetTextI18n")
    private List<SubscriptionModel> getSubscriptionList() {
        int active_subscription=0;
        String payment_status=" ";
        String security_deposit_status=" ";

        List<SubscriptionModel> subscriptionModelList=new ArrayList<>();

        for (int i=0;i<user_detail_info.getSubscriptions().size();i++){
         //   Log.e("status",user_detail_info.getSubscriptions().get(i).getStatus().toLowerCase());
            if (Objects.equals(user_detail_info.getSubscriptions().get(i).getStatus().toLowerCase(), "active") || Objects.equals(user_detail_info.getSubscriptions().get(i).getStatus().toLowerCase(), "expired")||Objects.equals(user_detail_info.getSubscriptions().get(i).getStatus().toLowerCase(), "rejected") ||(Objects.equals(user_detail_info.getSubscriptions().get(i).getStatus().toLowerCase(), "pending") && !user_detail_info.getSubscriptions().get(i).getPaymentMode().equalsIgnoreCase("none") && !user_detail_info.getSubscriptions().get(i).getPaymentBy().equalsIgnoreCase("none"))){
                if (Objects.equals(user_detail_info.getSubscriptions().get(i).getStatus().toLowerCase(), "active")){
                    active_subscription++;

                }

                if (user_detail_info.getSubscriptions().get(i).getPaymentStatus().equals("Not done")){
                    payment_status="Not Paid";
                }else {
                    payment_status="Paid";

                }
                if (user_detail_info.getSubscriptions().get(i).isIs_refunded()){
                    security_deposit_status="Refunded";

                }else {
                    security_deposit_status="Refundable";

                }

                subscriptionModelList.add(new SubscriptionModel(user_detail_info.getSubscriptions().get(i).getMess_basic_info().getMess_images().get(0).getBigImage().get(0),user_detail_info.getSubscriptions().get(i).getMess_basic_info().getMess_name(),user_detail_info.getSubscriptions().get(i).getMess_basic_info().getMess_location().get(0).getCompanyAddress(),user_detail_info.getSubscriptions().get(i).getSubsRejectedReason(),user_detail_info.getSubscriptions().get(i).getStatus(),"Left-meals: "+user_detail_info.getSubscriptions().get(i).getLeftMeals(),user_detail_info.getSubscriptions().get(i).getPlanType()+"/day",user_detail_info.getSubscriptions().get(i).getStartingMeal(),user_detail_info.getSubscriptions().get(i).getPlanPrice(),user_detail_info.getSubscriptions().get(i).getStartFrom(),"__",user_detail_info.getSubscriptions().get(i).getMessId(),payment_status,"\u20B9 "+user_detail_info.getSubscriptions().get(i).getPaymentValue(),"Tiffin","\u20B9 "+user_detail_info.getSubscriptions().get(i).getSecurity_money(),security_deposit_status));

            }
        }

        if (active_subscription<10) {

            subscriptionsBinding.txtActiveSubs.setText("0"+active_subscription);

        }else{
            subscriptionsBinding.txtActiveSubs.setText(String.valueOf(active_subscription));

        }

        if (subscriptionModelList.size()==0){
            subscriptionsBinding.recSubscriptions.setVisibility(View.GONE);
            subscriptionsBinding.lnNoSubs.setVisibility(View.VISIBLE);


        }else {
            subscriptionsBinding.recSubscriptions.setVisibility(View.VISIBLE);
            subscriptionsBinding.lnNoSubs.setVisibility(View.GONE);

        }
        return subscriptionModelList;

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