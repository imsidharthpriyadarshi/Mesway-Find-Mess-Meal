package in.mesway.fragments.meals;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import in.mesway.Adapters.UpcomingMealAdapter;
import in.mesway.Models.UpcomingMealModel;
import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.ExtraInfoResponse;
import in.mesway.Response.TakeDeliveryResponse;
import in.mesway.Response.UpcomingMeal.UpcomingMeals;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.ExtraInfoViewModel;
import in.mesway.ViewModels.HaveToloadViewModel;
import in.mesway.ViewModels.ImportantViewmodel;
import in.mesway.ViewModels.UpcomingMealViewModel;
import in.mesway.activity.MainActivity;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentMealsBinding;
import in.mesway.fragments.home.UpcomingMealInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MealsFragment extends Fragment {
    private FragmentMealsBinding mealsBinding;
    private NavController navController;
    private UpcomingMealAdapter upcomingMealAdapter;
    private UpcomingMealInterface upcomingMealInterface;
    private SharedPreferences sharedPreferences;
    private UpcomingMeals upcoming_meals_detail;
    private Activity activity;
    private UpcomingMealViewModel upcomingMealViewModel;
    private ApiInterface apiInterface;
    private String date_with_day = "none";
    private MainActivity mainActivity;
    private AlertDialog alertDialog;
    private Snackbar snackbar;
    private String lunch_cancel_time = "11 am", dinner_cancel_time = "5 pm", breakfast_cancel_time = "6 am";
    private ExtraInfoResponse extra_info_response;
    private ExtraInfoViewModel extraInfoViewModel;
    private ImportantViewmodel importantViewmodel;

    private HaveToloadViewModel haveToloadViewModel;


    //  private MealRecViewLoadingViewModel mealRecViewLoadingViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mealsBinding = FragmentMealsBinding.inflate(inflater, container, false);


        return mealsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.mainBinding.menuBottom.setVisibility(View.VISIBLE);
        mainActivity.mainBinding.linearBottomMenu.setVisibility(View.VISIBLE);
        initView();

            haveToloadViewModel.isHave_to_load().observe((LifecycleOwner) activity, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (view!=null) {
                        if (aBoolean) {

                            upcomingMealViewModel.getUpcomingMeal(activity);
                        }
                    }
                }
            });

        // importantDataObserver(view);
        if (sharedPreferences.getString(Constant.ACCESS_TOKEN, null) == null) {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.mealsFragment, true)
                    .build();
            try {

                navController.navigate(MealsFragmentDirections.actionGlobalDoLoginFragment(), navOptions);
            } catch (Exception ignored) {
            }

        } else {


            dateLoading();
            extraInfoStatusCodeObserver();
            statusCodeObserver(view);
            isLoading();

            clickHandle();

        }

    }

    private void extraInfoStatusCodeObserver() {
        Observer<Integer> status_code_observer = integer -> {
            if (integer == 200) {

                geExtraInfoDetail();

            }


        };

        extraInfoViewModel.status_code.observe((LifecycleOwner) activity, status_code_observer);

    }

    private void geExtraInfoDetail() {
        Observer<ExtraInfoResponse> extraInfoResponseObserver = new Observer<ExtraInfoResponse>() {
            @Override
            public void onChanged(ExtraInfoResponse extraInfoResponse) {
                extra_info_response = extraInfoResponse;
                getUpdateOperation();
            }
        };


        extraInfoViewModel.extraInfoResponseMutableLiveData.observe((LifecycleOwner) activity, extraInfoResponseObserver);

    }

    private void getUpdateOperation() {
        if (extra_info_response != null) {
            breakfast_cancel_time = extra_info_response.getBreakfast_cancel_time().substring(0, 2) + " am";
            lunch_cancel_time = extra_info_response.getLunch_cancel_time().substring(0, 2) + " am";
            dinner_cancel_time = (Integer.parseInt(extra_info_response.getDinner_cancel_time().substring(0, 2)) - 12) + " pm";
        }
    }


    private void dateLoading() {

        Call<String> getDate = apiInterface.get_date_as_date();

        getDate.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    String dtString = response.body();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    try {
                        date = simpleDateFormat.parse(dtString);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date != null) {
                        String dayOfWeek = (String) DateFormat.format("EEE", date);
                        String monthString = (String) DateFormat.format("MMM", date);
                        String year = (String) DateFormat.format("yyyy", date);
                        String day = (String) DateFormat.format("dd", date);
                        date_with_day = dayOfWeek + ", " + day + " " + monthString + " " + year;
                        mealsBinding.tTodayDate.setText(date_with_day);

                    } else {

                        mealsBinding.tTodayDate.setText(response.body());
                        date_with_day = response.body();

                    }

                    mealsBinding.dateShimmer.stopShimmer();
                    mealsBinding.dateShimmer.setVisibility(View.GONE);
                    mealsBinding.tTodayDate.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });

    }

    private void initView() {

        importantViewmodel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ImportantViewmodel.class);
        Retrofit retrofit = ApiClient.getClient();
        alertDialog = Reusable.alertDialog(activity);
        apiInterface = retrofit.create(ApiInterface.class);
        upcomingMealViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UpcomingMealViewModel.class);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        extraInfoViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ExtraInfoViewModel.class);
        haveToloadViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(HaveToloadViewModel.class);

    }

    private void importantDataObserver(View view) {
        importantViewmodel.getAccess_token_update().observe((LifecycleOwner) activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (view != null) {
                    upcomingMealViewModel.getUpcomingMeal(activity);
                }
            }
        });

        importantViewmodel.getLocation_update().observe((LifecycleOwner) activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (view != null) {
                    upcomingMealViewModel.getUpcomingMeal(activity);
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
                }else{

                    alertDialog.dismiss();
                }
            }
        };

        upcomingMealViewModel.isLoading.observe((LifecycleOwner) activity, is_loading);


    }


    private void statusCodeObserver(View view) {
        Observer<Integer> status_code_observer = integer -> {
            if (integer == 200) {
                getUpcomingMeal(view);
                mealsBinding.recViewUpcomingMeal.setVisibility(View.VISIBLE);
                mealsBinding.lnNoUpcomingMeal.setVisibility(View.GONE);

            } else if (integer == 404) {
                mealsBinding.recViewUpcomingMeal.setVisibility(View.GONE);
                mealsBinding.lnNoUpcomingMeal.setVisibility(View.VISIBLE);


            } else {
                detailObserver(view);
                mealsBinding.recViewUpcomingMeal.setVisibility(View.GONE);
                mealsBinding.lnNoUpcomingMeal.setVisibility(View.VISIBLE);

            }


        };

        upcomingMealViewModel.status_code.observe((LifecycleOwner) activity, status_code_observer);

    }

    private void detailObserver(View views) {
        Observer<String> detail_observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                snackbar = Snackbar.make(views, s, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAnchorView(mainActivity.mainBinding.menuBottom);
                snackbar.setAction("Refresh", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upcomingMealViewModel.getUpcomingMeal(activity);
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                snackbar.show();


            }
        };


        upcomingMealViewModel.detail.observe((LifecycleOwner) activity, detail_observer);


    }

    private void getUpcomingMeal(View view) {
       /* Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {


            @Override
            public void run() {*/
                Observer<UpcomingMeals> upcomingMealsObserver = new Observer<UpcomingMeals>() {
                    @Override
                    public void onChanged(UpcomingMeals upcomingMeals) {
                        upcoming_meals_detail = upcomingMeals;
                        basicOperations();
                        upcomingMealRecViewHandel(view);
                    }
                };

                upcomingMealViewModel.upcomingMealsMutableLiveData.observe((LifecycleOwner) activity, upcomingMealsObserver);

     /*       }
        }, 200);*/


    }


    private void upcomingMealRecViewHandel(View views) {
        upcomingMealInterface = new UpcomingMealInterface() {
            @Override
            public void deliveryBoyCall(View btn_view, int position, String delivery_boy_phone_number, String delivery_boy_id) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                String number = "tel:" + delivery_boy_phone_number;
                call.setData(Uri.parse(number));
                startActivity(call);
            }

            @Override
            public void mealCancel(View btn_view, int position, String sub_id, String mess_id, String upcoming_meal_type) {
                Bundle bundle = new Bundle();
                bundle.putString("date_with_day", date_with_day);
                bundle.putString("meal_type", upcoming_meal_type);
                bundle.putString("mess_id", mess_id);
                bundle.putString("subs_id", sub_id);
                try {

                    navController.navigate(R.id.action_mealsFragment_to_cancelMealDialogFragment, bundle);
                } catch (Exception ignored) {
                }


            }

            @Override
            public void messConsClick(View cons_view, String mess_id) {

            }

            @Override
            public void getCodeBtnClick(ProgressBar progressBar, TextView delivery_code, String subs_id, String meal_type) {
                getDeliveryCodeOperations(views, progressBar, delivery_code, subs_id, meal_type);


            }
        };

        upcomingMealAdapter = new UpcomingMealAdapter(getUpcomingMealList(), activity, upcomingMealInterface);
        mealsBinding.recViewUpcomingMeal.setLayoutManager(new LinearLayoutManager(activity));
        mealsBinding.recViewUpcomingMeal.setAdapter(upcomingMealAdapter);
        mealsBinding.recViewUpcomingMeal.setNestedScrollingEnabled(false);


    }

    private void getDeliveryCodeOperations(View views, ProgressBar progressBar, TextView delivery_code, String subs_id, String meal_type) {
        progressBar.setVisibility(View.VISIBLE);
        Call<TakeDeliveryResponse> takeDeliveryResponseCall = apiInterface.get_delivery(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), sharedPreferences.getString(Constant.USER_ID, null), subs_id, meal_type.toLowerCase().trim());
        takeDeliveryResponseCall.enqueue(new Callback<TakeDeliveryResponse>() {
            @Override
            public void onResponse(@NonNull Call<TakeDeliveryResponse> call, @NonNull Response<TakeDeliveryResponse> response) {
                Gson gson = new GsonBuilder().create();

                if (response.code() == 200) {
                    TakeDeliveryResponse takeDeliveryResponse = response.body();
                    assert takeDeliveryResponse != null;
                    delivery_code.setText(takeDeliveryResponse.getCode());
                    progressBar.setVisibility(View.GONE);



                } else {
                    try {
                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        snackbar = Snackbar.make(views, response.code() + ": " + detailResponse.getDetail(), Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAnchorView(mainActivity.mainBinding.menuBottom);
                        progressBar.setVisibility(View.GONE);

                        snackbar.setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();

                    } catch (Exception e) {
                        snackbar = Snackbar.make(views, response.code() + ":Something went error " + e.getMessage(), Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(mainActivity.mainBinding.menuBottom);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                        snackbar.show();
                        progressBar.setVisibility(View.GONE);


                    }
                }

            }

            @Override
            public void onFailure(Call<TakeDeliveryResponse> call, Throwable t) {
                snackbar = Snackbar.make(views, "Something went error " + t.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.setAnchorView(mainActivity.mainBinding.menuBottom);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                snackbar.show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void basicOperations() {

         int total_meal = 0, left_meal = 0, cancel_credit = 0;

        if (upcoming_meals_detail != null) {
            for (int i = 0; i < upcoming_meals_detail.getUpcomingMeal().size(); i++) {
                if (Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getStatus(), "active")) {
                    total_meal = total_meal + upcoming_meals_detail.getUpcomingMeal().get(i).getTotalMeals();
                    left_meal = left_meal + upcoming_meals_detail.getUpcomingMeal().get(i).getLeftMeals();
                    cancel_credit = cancel_credit + upcoming_meals_detail.getUpcomingMeal().get(i).getAllowedCancelMeal();
                }
            }

            mealsBinding.tTotalMealsValue.setText(String.valueOf(total_meal));
            mealsBinding.tLeftMealsValue.setText(String.valueOf(left_meal));
            mealsBinding.tCancelCredit.setText(String.valueOf(cancel_credit));


        } else {

            mealsBinding.tTotalMealsValue.setText("0");
            mealsBinding.tLeftMealsValue.setText("0");
            mealsBinding.tCancelCredit.setText("0");

        }
    }

    private List<UpcomingMealModel> getUpcomingMealList() {

        List<UpcomingMealModel> mealModelList = new ArrayList<>();
        if (upcoming_meals_detail != null) {
            for (int i = 0; i < upcoming_meals_detail.getUpcomingMeal().size(); i++) {
                if (Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getStatus(), "active")) {
                    if (!Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getBreakfast(), "not serving")) {

                        if (Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getBreakfast(), "cancel")) {
                            mealModelList.add(new UpcomingMealModel(upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_images().get(0).getBigImage().get(0), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_name(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_location().get(0).getCompanyAddress(), "BreakFast", "Cancelled", upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getName(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), breakfast_cancel_time, upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getNumber(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessId(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getDeliveryBoyId(), upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), "\u20B9 " + upcoming_meals_detail.getUpcomingMeal().get(i).getPaymentValue(), upcoming_meals_detail.getUpcomingMeal().get(i).getDeliveredMeal()));

                        } else if (Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getBreakfast(), "upcoming")) {


                            mealModelList.add(new UpcomingMealModel(upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_images().get(0).getBigImage().get(0), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_name(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_location().get(0).getCompanyAddress(), "BreakFast", "Upcoming", upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getName(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), breakfast_cancel_time, upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getNumber(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessId(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getDeliveryBoyId(), upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), "\u20B9 " + upcoming_meals_detail.getUpcomingMeal().get(i).getPaymentValue(), upcoming_meals_detail.getUpcomingMeal().get(i).getDeliveredMeal()));

                        } else {

                            mealModelList.add(new UpcomingMealModel(upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_images().get(0).getBigImage().get(0), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_name(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_location().get(0).getCompanyAddress(), "BreakFast", "Delivered", upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getName(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), breakfast_cancel_time, upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getNumber(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessId(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getDeliveryBoyId(), upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), "\u20B9 " + upcoming_meals_detail.getUpcomingMeal().get(i).getPaymentValue(), upcoming_meals_detail.getUpcomingMeal().get(i).getDeliveredMeal()));

                        }
                    }


                    if (!Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getLunch(), "not serving")) {

                        if (Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getLunch(), "cancel")) {
                            mealModelList.add(new UpcomingMealModel(upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_images().get(0).getBigImage().get(0), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_name(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_location().get(0).getCompanyAddress(), "Lunch", "Cancelled", upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getName(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), lunch_cancel_time, upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getNumber(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessId(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getDeliveryBoyId(), upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), "\u20B9 " + upcoming_meals_detail.getUpcomingMeal().get(i).getPaymentValue(), upcoming_meals_detail.getUpcomingMeal().get(i).getDeliveredMeal()));

                        } else if (Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getLunch(), "upcoming")) {


                            mealModelList.add(new UpcomingMealModel(upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_images().get(0).getBigImage().get(0), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_name(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_location().get(0).getCompanyAddress(), "Lunch", "Upcoming", upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getName(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), lunch_cancel_time, upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getNumber(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessId(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getDeliveryBoyId(), upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), "\u20B9 " + upcoming_meals_detail.getUpcomingMeal().get(i).getPaymentValue(), upcoming_meals_detail.getUpcomingMeal().get(i).getDeliveredMeal()));

                        } else {

                            mealModelList.add(new UpcomingMealModel(upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_images().get(0).getBigImage().get(0), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_name(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_location().get(0).getCompanyAddress(), "Lunch", "Delivered", upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getName(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), lunch_cancel_time, upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getNumber(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessId(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getDeliveryBoyId(), upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), "\u20B9 " + upcoming_meals_detail.getUpcomingMeal().get(i).getPaymentValue(), upcoming_meals_detail.getUpcomingMeal().get(i).getDeliveredMeal()));

                        }
                    }


                    if (!Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getDinner(), "not serving")) {

                        if (Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getDinner(), "cancel")) {
                            mealModelList.add(new UpcomingMealModel(upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_images().get(0).getBigImage().get(0), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_name(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_location().get(0).getCompanyAddress(), "Dinner", "Cancelled", upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getName(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), dinner_cancel_time, upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getNumber(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessId(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getDeliveryBoyId(), upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), "\u20B9 " + upcoming_meals_detail.getUpcomingMeal().get(i).getPaymentValue(), upcoming_meals_detail.getUpcomingMeal().get(i).getDeliveredMeal()));

                        } else if (Objects.equals(upcoming_meals_detail.getUpcomingMeal().get(i).getDinner(), "upcoming")) {


                            mealModelList.add(new UpcomingMealModel(upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_images().get(0).getBigImage().get(0), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_name(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_location().get(0).getCompanyAddress(), "Dinner", "Upcoming", upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getName(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), dinner_cancel_time, upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getNumber(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessId(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getDeliveryBoyId(), upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), "\u20B9 " + upcoming_meals_detail.getUpcomingMeal().get(i).getPaymentValue(), upcoming_meals_detail.getUpcomingMeal().get(i).getDeliveredMeal()));

                        } else {

                            mealModelList.add(new UpcomingMealModel(upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_images().get(0).getBigImage().get(0), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_name(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMess_location().get(0).getCompanyAddress(), "Dinner", "Delivered", upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getName(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), dinner_cancel_time, upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getNumber(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessId(), upcoming_meals_detail.getUpcomingMeal().get(i).getMessInfo().getMessDeliveryBoy().get(0).getDeliveryBoyId(), upcoming_meals_detail.getUpcomingMeal().get(i).getSubscriptionId(), "\u20B9 " + upcoming_meals_detail.getUpcomingMeal().get(i).getPaymentValue(), upcoming_meals_detail.getUpcomingMeal().get(i).getDeliveredMeal()));

                        }
                    }



                }

            }
        }
        alertDialog.dismiss();
        return mealModelList;
    }

    private void clickHandle() {
        /*mealsBinding.consSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mealsFragment_to_subscriptionsFragment);


            }
        });*/
        Executor executor = Executors.newSingleThreadExecutor();
        mealsBinding.imgRefreshUpcomingMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        upcomingMealViewModel.getUpcomingMeal(activity);

                    }
                });
            }
        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("scroll_position", mealsBinding.scrollOrders.getScrollY());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            int scrolledPosition = savedInstanceState.getInt("scroll_position");
            mealsBinding.scrollOrders.scrollTo(0, scrolledPosition);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null) {
            snackbar.dismiss();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;
    }
}