package in.mesway.fragments.meals;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.NormalSubscription;
import in.mesway.Response.UpcomingMeal.UpcomingMeals;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.HaveToloadViewModel;
import in.mesway.ViewModels.UpcomingMealViewModel;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentCancelMealDialogBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CancelMealDialogFragment extends BottomSheetDialogFragment {
    private FragmentCancelMealDialogBinding cancelMealDialogBinding;
    private ApiInterface apiInterface;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private Bundle bundle;
    private UpcomingMealViewModel upcomingMealViewModel;
    private UpcomingMeals upcoming_meals_detail;

    private HaveToloadViewModel haveToloadViewModel;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.MyBottomSheetTheme);
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cancelMealDialogBinding = FragmentCancelMealDialogBinding.inflate(inflater, container, false);

        Objects.requireNonNull(getDialog()).requestWindowFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return cancelMealDialogBinding.getRoot();

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

    @SuppressLint("SetTextI18n")
    private void basicOperation() {
        cancelMealDialogBinding.tDate.setText(bundle.getString("date_with_day"));

        if (bundle.getString("meal_type").equalsIgnoreCase("lunch")) {
            cancelMealDialogBinding.radioStartingLunch.setEnabled(true);
            cancelMealDialogBinding.radioStartingLunch.setChecked(true);
            cancelMealDialogBinding.radioStartingLunch.setTextColor(ContextCompat.getColor(activity,R.color.black));
        } else if (bundle.getString("meal_type").equalsIgnoreCase("dinner")) {

            cancelMealDialogBinding.radioStartingDinner.setEnabled(true);
            cancelMealDialogBinding.radioStartingDinner.setChecked(true);
            cancelMealDialogBinding.radioStartingDinner.setTextColor(ContextCompat.getColor(activity,R.color.black));

        } else if (bundle.getString("meal_type").equalsIgnoreCase("breakfast")) {

            cancelMealDialogBinding.radioStartingBreakfast.setEnabled(true);
            cancelMealDialogBinding.radioStartingBreakfast.setChecked(true);
            cancelMealDialogBinding.radioStartingBreakfast.setTextColor(ContextCompat.getColor(activity,R.color.black));


        }

        cancelMealDialogBinding.plzConfirmTxt.setText("Please Confirm, do you want to cancel " + bundle.getString("meal_type") + " ?");


    }

    private void clickHandel(View views) {
        cancelMealDialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
                Call<NormalSubscription> normalSubscriptionCall = null;
                if (bundle.getString("meal_type").equalsIgnoreCase("lunch")) {
                    normalSubscriptionCall = apiInterface.cancel_upcoming_meal(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), bundle.getString("subs_id"), bundle.getString("mess_id"), sharedPreferences.getString(Constant.USER_ID, null), null, "cancel", null);

                } else if (bundle.getString("meal_type").equalsIgnoreCase("dinner")) {

                    normalSubscriptionCall = apiInterface.cancel_upcoming_meal(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), bundle.getString("subs_id"), bundle.getString("mess_id"), sharedPreferences.getString(Constant.USER_ID, null), null, null, "cancel");


                } else if (bundle.getString("meal_type").equalsIgnoreCase("breakfast")) {
                    normalSubscriptionCall = apiInterface.cancel_upcoming_meal(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), bundle.getString("subs_id"), bundle.getString("mess_id"), sharedPreferences.getString(Constant.USER_ID, null), "cancel", null, null);

                }

                if (normalSubscriptionCall != null) {
                    normalSubscriptionCall.enqueue(new Callback<NormalSubscription>() {
                        @Override
                        public void onResponse(@NonNull Call<NormalSubscription> call, @NonNull Response<NormalSubscription> response) {
                            Gson gson = new GsonBuilder().create();
                            if (response.code() == 200) {
                                alertDialog.dismiss();
                                Toast.makeText(activity, "Meal Cancelled successfully", Toast.LENGTH_SHORT).show();
                                haveToloadViewModel.setHave_to_load(true);
                                haveToloadViewModel.setOrder_have_to_load(true);

                                dismiss();

                            } else {
                                try {
                                    assert response.errorBody() != null;
                                    DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                                    Snackbar.make(views, response.code() + ": " + detailResponse.getDetail(), Snackbar.LENGTH_SHORT)
                                            .setAnchorView(cancelMealDialogBinding.btnCancel)
                                            .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                                            .show();

                                    alertDialog.dismiss();
                                } catch (Exception e) {

                                    Snackbar.make(views, response.code() + ": Something went wrong" , Snackbar.LENGTH_SHORT)
                                            .setAnchorView(cancelMealDialogBinding.btnCancel)
                                            .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                                            .show();

                                    alertDialog.dismiss();


                                }

                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<NormalSubscription> call, @NonNull Throwable t) {
                            Snackbar.make(views, "Failure: Check Internet connection/Something went wrong" + t.getMessage(), Snackbar.LENGTH_SHORT)
                                    .setAnchorView(cancelMealDialogBinding.btnCancel)
                                    .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                                    .show();

                            alertDialog.dismiss();
                        }
                    });

                } else {

                    Snackbar.make(views, "Something went error", Snackbar.LENGTH_SHORT)
                            .setAnchorView(cancelMealDialogBinding.btnCancel)
                            .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                            .show();

                    alertDialog.dismiss();


                }
            }
        });

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
                        .setAnchorView(cancelMealDialogBinding.btnCancel)
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
                setCancelCredit();


            }
        };

        upcomingMealViewModel.upcomingMealsMutableLiveData.observe((LifecycleOwner) activity, upcomingMealsObserver);
    }

    private void setCancelCredit() {
        for (int i=0;i<upcoming_meals_detail.getUpcomingMeal().size();i++){
            if (Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), bundle.getString("subs_id"))){
                cancelMealDialogBinding.tCancelCreditValue.setText(String.valueOf(upcoming_meals_detail.getUpcomingMeal().get(i).getAllowedCancelMeal()));
                return;
            }
        }
    }


    private void initView(View view) {
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        alertDialog = Reusable.alertDialog(activity);
        bundle = getArguments();
        upcomingMealViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UpcomingMealViewModel.class);
        haveToloadViewModel= new ViewModelProvider((ViewModelStoreOwner) activity).get(HaveToloadViewModel.class);

        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;


    }
}