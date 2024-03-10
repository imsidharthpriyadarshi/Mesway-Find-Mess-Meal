package in.mesway.fragments.meals;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import in.mesway.R;
import in.mesway.Response.UpcomingMeal.UpcomingMeals;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.UpcomingMealViewModel;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentGetRefundDialogBinding;
import retrofit2.Retrofit;


public class GetRefundDialogFragment extends BottomSheetDialogFragment {

    private FragmentGetRefundDialogBinding getRefundDialogBinding;

    private ApiInterface apiInterface;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private Bundle bundle;
    private UpcomingMealViewModel upcomingMealViewModel;
    private UpcomingMeals upcoming_meals_detail;



    public GetRefundDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.MyBottomSheetTheme);

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getRefundDialogBinding=FragmentGetRefundDialogBinding.inflate(inflater,container,false);

        Objects.requireNonNull(getDialog()).requestWindowFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return getRefundDialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        basicOperation();
        if(getDialog()!=null) {
            statusCodeObserver(getDialog().getWindow().getDecorView());
            isLoading();
            clickHandel(Objects.requireNonNull(getDialog()).getWindow().getDecorView());
        }
    }

    private void clickHandel(View decorView) {
    }

    private void basicOperation() {
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

        upcomingMealViewModel.isLoading.observe((LifecycleOwner) activity, is_loading);


    }

    private void statusCodeObserver(View view) {
        Observer<Integer> status_code_observer = integer -> {
            if (integer == 200) {
                getUpcomingMeal();


            } else {
                detailObserver(view);


            }


        };

        upcomingMealViewModel.status_code.observe((LifecycleOwner) activity, status_code_observer);

    }


    private void detailObserver(View view) {
        Observer<String> detail_observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(view,s,Snackbar.LENGTH_SHORT)
                        .setAnchorView(getRefundDialogBinding.consYesIReceived)
                        .show();


            }
        };


        upcomingMealViewModel.detail.observe((LifecycleOwner) activity, detail_observer);


    }
    private void getUpcomingMeal() {

        Observer<UpcomingMeals> upcomingMealsObserver = new Observer<UpcomingMeals>() {
            @Override
            public void onChanged(UpcomingMeals upcomingMeals) {
                upcoming_meals_detail = upcomingMeals;



            }
        };

        upcomingMealViewModel.upcomingMealsMutableLiveData.observe((LifecycleOwner) activity, upcomingMealsObserver);
    }

    private void initView(View view) {
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        alertDialog = Reusable.alertDialog(activity);
        bundle = getArguments();
        upcomingMealViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UpcomingMealViewModel.class);

        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;


    }
}