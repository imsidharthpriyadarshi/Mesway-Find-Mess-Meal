package in.mesway.fragments.details;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.Location.MessInfo;
import in.mesway.Response.UpcomingMeal.UserSubscription;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.MessInfoViewModel;
import in.mesway.ViewModels.UserSubscriptionViewModel;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentSubscriptionDetailBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SubscriptionsDetail extends Fragment {
    private FragmentSubscriptionDetailBinding subscriptionDetailBinding;
    private final String[] months = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private boolean plan_selected;
    private NavController navController;
    private Calendar calendar;
    private int starting_year, starting_day, starting_month;
    private int max_year, max_day, max_month;
    private ArrayAdapter<String> first_meals_type;
    private ArrayAdapter<String> second_select;
    private UserSubscriptionViewModel userSubscriptionViewModel;
    private String start_from, plan_type;
    private ApiInterface apiInterface;
    private MessInfo mess_info_detail;
    private MessInfoViewModel messInfoViewModel;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;
    private Snackbar snackbar;


    private String one_meal_price, two_meal_price, three_meal_price, delivery_charge, mess_plan_id, three_meal_plan_starting_meal, three_meal_plan_second_meal, three_meal_plan_third_meal;
    private int security_deposit = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        subscriptionDetailBinding = FragmentSubscriptionDetailBinding.inflate(inflater, container, false);

        return subscriptionDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //when view is'nt available in oncreate fragmentcontainer have bug

        navController = Navigation.findNavController(view);


        initView();
        dateLoading(view);
        radioGroupHandle();

        statusCodeObserver(view);
        isLoading();


    }

    private void initView() {
        calendar = Calendar.getInstance();
        userSubscriptionViewModel = new ViewModelProvider(requireActivity()).get(UserSubscriptionViewModel.class);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        alertDialog = Reusable.alertDialog(requireActivity());
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());
        messInfoViewModel = new ViewModelProvider(requireActivity()).get(MessInfoViewModel.class);

    }

    private void dateLoading(View view) {
        alertDialog.show();

        Call<String> getDate = apiInterface.get_date_as_date();

        getDate.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    String dtString = response.body();
                    start_from = dtString;
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
                        String date_value = (String) DateFormat.format("MM", date);
                        String year = (String) DateFormat.format("yyyy", date);
                        String day = (String) DateFormat.format("dd", date);
                        whichDayWhichMeal(dayOfWeek,view);
                        subscriptionDetailBinding.tDate.setText(String.valueOf(day));
                        subscriptionDetailBinding.tMonthName.setText(monthString);
                        starting_month = Integer.parseInt(date_value) - 1;
                        starting_year = Integer.parseInt(year);
                        starting_day = Integer.parseInt(day);
                        max_year = starting_year;
                        max_month = starting_month;
                        max_day = starting_day + 5;
                        alertDialog.dismiss();
                        datePicker(view);

                    } else {
                        Toast.makeText(requireActivity(), "Error loading in date ", Toast.LENGTH_SHORT).show();

                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });

    }


    private void isLoading() {
        Observer<Boolean> is_loading = aBoolean -> {
            if (aBoolean) {
                alertDialog.show();
            } else {

                alertDialog.dismiss();

            }
        };

        messInfoViewModel.isLoading.observe((LifecycleOwner) requireActivity(), is_loading);


    }

    private void statusCodeObserver(View view) {
        Observer<Integer> status_code_observer = integer -> {
            if (integer == 200) {
                getMessDetailInfo(view);

            } else {
                detailObserver(view);


            }


        };

        messInfoViewModel.status_code.observe((LifecycleOwner) requireActivity(), status_code_observer);

    }

    private void detailObserver(View view) {
        Observer<String> detail_observer = s -> {
            snackbar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
            snackbar.show();
        };


        messInfoViewModel.detail.observe((LifecycleOwner) requireActivity(), detail_observer);


    }

    private void getMessDetailInfo(View view) {
        Observer<MessInfo> messInfoObserver = messInfo -> {
            mess_info_detail = messInfo;
            messDetailOperation(view);


        };

        messInfoViewModel.messInfoMutableLiveData.observe((LifecycleOwner) requireActivity(), messInfoObserver);


    }

    @SuppressLint("SetTextI18n")
    private void messDetailOperation(View view) {
        loadMessImage();
        subscriptionDetailBinding.tMessName.setText(mess_info_detail.getMessName());
        subscriptionDetailBinding.tMessAddress.setText(mess_info_detail.getMessLocation().get(0).getCompanyAddress());

        if (getArguments() != null) {
            int subs_type = getArguments().getInt("subs_type");

            for (int i = 0; i < mess_info_detail.getMessSubscriptionType().size(); i++) {
                if (mess_info_detail.getMessSubscriptionType().get(i).getMessSubsTypes().getSubsDay() == subs_type) {
                    mess_plan_id = mess_info_detail.getMessSubscriptionType().get(i).getMessSubsId();

                }


            }
            delivery_charge = String.valueOf(subs_type * mess_info_detail.getMessDeliveryBoy().get(0).getDelivery_charge());

            subscriptionDetailBinding.tMealDeliveryValue.setText(subs_type + " * " + mess_info_detail.getMessDeliveryBoy().get(0).getDelivery_charge());
            if (subs_type == 1) {
                subscriptionDetailBinding.tPlanName.setText("Plan for One Day");

            } else if (subs_type == 7) {
                subscriptionDetailBinding.tPlanName.setText("Plan for One Week");


            } else if (subs_type == 15) {
                subscriptionDetailBinding.tPlanName.setText("Plan for Half Month");


            } else if (subs_type == 30) {
                subscriptionDetailBinding.tPlanName.setText("Plan for One Month");

            }
            offeredSubsPrice(subs_type);

        }

        alertDialog.dismiss();
        clickHandle(view);
    }

    @SuppressLint("SetTextI18n")
    private void offeredSubsPrice(int subs_type) {
        for (int i = 0; i < mess_info_detail.getMessSubscriptionType().size(); i++) {

            if (mess_info_detail.getMessSubscriptionType().get(i).getMessSubsTypes().getSubsDay() == subs_type) {
                security_deposit = mess_info_detail.getMessSubscriptionType().get(i).getSecurity_money();
                subscriptionDetailBinding.tSecurityDepositValue.setText("\u20B9 " + security_deposit);

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay(), "not_serving")) {

                    subscriptionDetailBinding.tOneMealPriceDay.setText("\u20B9 " + mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay());
                    one_meal_price = mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay();

                } else {

                    subscriptionDetailBinding.constraintLayout1.setVisibility(View.GONE);
                    subscriptionDetailBinding.radio1Meal.setVisibility(View.GONE);
                    one_meal_price = "none";

                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay(), "not_serving")) {

                    subscriptionDetailBinding.tTwoMealPriceDay.setText("\u20B9 " + mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay());
                    two_meal_price = mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay();

                } else {

                    subscriptionDetailBinding.constraintLayout11.setVisibility(View.GONE);
                    subscriptionDetailBinding.radio2Meal.setVisibility(View.GONE);
                    two_meal_price = "none";

                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay(), "not_serving")) {
                    subscriptionDetailBinding.tThreeMealPriceDay.setText("\u20B9 " + mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay());
                    three_meal_price = mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay();


                } else {
                    subscriptionDetailBinding.constraintLayout111.setVisibility(View.GONE);
                    subscriptionDetailBinding.radio3Meal.setVisibility(View.GONE);
                    three_meal_price = "none";
                    subscriptionDetailBinding.consMealStartingType.setVisibility(View.GONE);


                }


            }

        }
    }

    private void loadMessImage() {

        File folder = requireActivity().getDir("mesway_img", Context.MODE_PRIVATE);

        File path = new File(folder, sharedPreferences.getString(Constant.MESS_ID, null));
        if (path.exists()) {
            Bitmap mess_bitmap = BitmapFactory.decodeFile(path.getPath());
            subscriptionDetailBinding.imgMess.setImageBitmap(mess_bitmap);
        } else {
            Glide.with(requireActivity())
                    .asBitmap()
                    .load(mess_info_detail.getMessImages().get(0).getBigImage().get(0))
                    .timeout(600000).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                            File file = new File(folder, mess_info_detail.getMessId());

                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, fos);


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();

                            }


                            return false;
                        }
                    })
                    .into(subscriptionDetailBinding.imgMess);

        }
    }


    @SuppressLint("SetTextI18n")
    private void radioGroupHandle() {
        subscriptionDetailBinding.radioGrpChoosePlan.setOnCheckedChangeListener((radioGroup, i) -> {
            if (subscriptionDetailBinding.radio2Meal.isChecked()) {
                subscriptionDetailBinding.tlSelectFirstMeal.setVisibility(View.VISIBLE);
                subscriptionDetailBinding.tlSelectSecondMeal.setVisibility(View.VISIBLE);
                subscriptionDetailBinding.autoCompleteTvFirstMeal.setText(null);
                subscriptionDetailBinding.tMealPriceValue.setText("\u20B9 " + two_meal_price);

                int total_price = Integer.parseInt(two_meal_price) + Integer.parseInt(delivery_charge) + security_deposit;
                subscriptionDetailBinding.tTotalPrice.setText("\u20B9 " + total_price);
                subscriptionDetailBinding.tTotalPriceFirst.setText("\u20B9 " + total_price);
                subscriptionDetailBinding.tlSelectSecondMeal.setEnabled(false);
                plan_type = "2 meals";
                subscriptionDetailBinding.radioGrpStartingMeal.clearCheck();
                subscriptionDetailBinding.consMealStartingType.setVisibility(View.GONE);
            } else if (subscriptionDetailBinding.radio1Meal.isChecked()) {
                subscriptionDetailBinding.tlSelectFirstMeal.setVisibility(View.VISIBLE);
                subscriptionDetailBinding.tlSelectSecondMeal.setVisibility(View.GONE);
                subscriptionDetailBinding.autoCompleteTvFirstMeal.setText(null);
                subscriptionDetailBinding.autoCompleteTvSecondMeal.setText(null);
                plan_type = "1 meal";
                subscriptionDetailBinding.tMealPriceValue.setText("\u20B9 " + one_meal_price);

                int total_price = Integer.parseInt(one_meal_price) + Integer.parseInt(delivery_charge) + security_deposit;

                subscriptionDetailBinding.tTotalPrice.setText("\u20B9 " + total_price);
                subscriptionDetailBinding.tTotalPriceFirst.setText("\u20B9 " + total_price);

                subscriptionDetailBinding.consMealStartingType.setVisibility(View.GONE);
                subscriptionDetailBinding.radioGrpStartingMeal.clearCheck();


            } else {
                subscriptionDetailBinding.tlSelectFirstMeal.setVisibility(View.GONE);
                subscriptionDetailBinding.tlSelectSecondMeal.setVisibility(View.GONE);
                subscriptionDetailBinding.consMealStartingType.setVisibility(View.VISIBLE);
                subscriptionDetailBinding.autoCompleteTvFirstMeal.setText(null);
                subscriptionDetailBinding.autoCompleteTvSecondMeal.setText(null);
                subscriptionDetailBinding.tMealPriceValue.setText("\u20B9 " + three_meal_price);
                plan_type = "3 meals";
                int total_price = Integer.parseInt(three_meal_price) + Integer.parseInt(delivery_charge) + security_deposit;
                subscriptionDetailBinding.tTotalPrice.setText("\u20B9 " + total_price);
                subscriptionDetailBinding.tTotalPriceFirst.setText("\u20B9 " + total_price);

            }
            plan_selected = true;

        });

        subscriptionDetailBinding.radioGrpStartingMeal.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton checked_button = requireActivity().findViewById(checkedId);
            try {
                if (checked_button.getText().equals("Lunch")) {
                    three_meal_plan_starting_meal = "lunch";
                    three_meal_plan_second_meal = "dinner";
                    three_meal_plan_third_meal = "breakfast";


                } else if (checked_button.getText().equals("BreakFast")) {
                    three_meal_plan_starting_meal = "breakfast";
                    three_meal_plan_second_meal = "lunch";
                    three_meal_plan_third_meal = "dinner";

                } else if (checked_button.getText().equals("Dinner")) {
                    three_meal_plan_starting_meal = "dinner";
                    three_meal_plan_second_meal = "breakfast";
                    three_meal_plan_third_meal = "lunch";

                }
            } catch (Exception ignored) {

            }


        });

    }

    private void datePicker(View views) {
        subscriptionDetailBinding.consStartingDatePicker.setOnClickListener(view -> {


            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), (datePicker, year, month, day) -> {
                subscriptionDetailBinding.tDate.setText(String.valueOf(day));
                subscriptionDetailBinding.tMonthName.setText(months[month]);
                if (month < 10) {
                    month = month + 1;
                    start_from = year + "-" + "0" + month + "-" + day;


                } else {
                    month = month + 1;

                    start_from = year + "-" + month + "-" + day;

                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                try {
                 date=   simpleDateFormat.parse(start_from);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date!=null){
                    String dayOfWeek= (String) DateFormat.format("EEE",date);

                    whichDayWhichMeal(dayOfWeek,views);
                }


            }, starting_year, starting_month, starting_day);

            calendar.set(starting_year, starting_month, starting_day);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());


            calendar.set(max_year, max_month, max_day);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            datePickerDialog.show();

        });

    }

    private void clickHandle(View views) {

        subscriptionDetailBinding.btnContinue.setOnClickListener(view -> addSubscription(views));
       /* List<String> meal_type = new ArrayList<>();
        //  String[] meal_type = new String[3];
        if (mess_info_detail.getMessServingMeal().getBreakfast()) {
            meal_type.add("BreakFast");
        }

        if (mess_info_detail.getMessServingMeal().getLunch()) {
            meal_type.add("Lunch");

        }

        if (mess_info_detail.getMessServingMeal().getDinner()) {
            meal_type.add("Dinner");
        }*/

        subscriptionDetailBinding.autoCompleteTvFirstMeal.setOnClickListener(view -> {
            subscriptionDetailBinding.autoCompleteTvFirstMeal.setAdapter(first_meals_type);
            subscriptionDetailBinding.autoCompleteTvFirstMeal.showDropDown();

        });


        subscriptionDetailBinding.autoCompleteTvSecondMeal.setOnClickListener(view -> {

            subscriptionDetailBinding.autoCompleteTvSecondMeal.setAdapter(second_select);
            subscriptionDetailBinding.autoCompleteTvSecondMeal.showDropDown();


        });


        ///some work
        subscriptionDetailBinding.autoCompleteTvFirstMeal.setOnItemClickListener((adapterView, view, i, l) -> {

            subscriptionDetailBinding.tlSelectSecondMeal.setEnabled(true);
            String selected = first_meals_type.getItem(i);
            subscriptionDetailBinding.autoCompleteTvSecondMeal.setText(null);
            second_select = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, removeFirstSelectedItem(selected));

        });
    }

    private void addSubscription(View view) {


        Call<UserSubscription> userSubscriptionCall;
        if (plan_selected) {
            if (Objects.equals(plan_type, "1 meal")) {
                if (!TextUtils.isEmpty(subscriptionDetailBinding.autoCompleteTvFirstMeal.getText())) {
                    assert getArguments() != null;
                    userSubscriptionCall = apiInterface.add_subscription(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), sharedPreferences.getString(Constant.MESS_ID, null), sharedPreferences.getString(Constant.USER_ID, null), start_from, plan_type, subscriptionDetailBinding.autoCompleteTvFirstMeal.getText().toString().toLowerCase(), null, null, one_meal_price, getArguments().getInt("subs_type"), mess_plan_id, subscriptionDetailBinding.tTotalPrice.getText().toString().substring(2));
                    alertDialog.show();
                    addSubscriptionOperations(userSubscriptionCall, view);
                } else {
                    snackbar = Snackbar.make(view, "Select starting meal", Snackbar.LENGTH_SHORT);
                    snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                    snackbar.show();

                }
            } else if (Objects.equals(plan_type, "2 meals")) {
                if (!TextUtils.isEmpty(subscriptionDetailBinding.autoCompleteTvFirstMeal.getText())) {

                    if (!TextUtils.isEmpty(subscriptionDetailBinding.autoCompleteTvSecondMeal.getText())) {
                        assert getArguments() != null;
                        userSubscriptionCall = apiInterface.add_subscription(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), sharedPreferences.getString(Constant.MESS_ID, null), sharedPreferences.getString(Constant.USER_ID, null), start_from, plan_type, subscriptionDetailBinding.autoCompleteTvFirstMeal.getText().toString().toLowerCase(), subscriptionDetailBinding.autoCompleteTvSecondMeal.getText().toString().toLowerCase(), null, two_meal_price, getArguments().getInt("subs_type"), mess_plan_id, subscriptionDetailBinding.tTotalPrice.getText().toString().substring(2));
                        alertDialog.show();
                        addSubscriptionOperations(userSubscriptionCall, view);

                    } else {
                        snackbar = Snackbar.make(view, "Select Second meal", Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                        snackbar.show();

                    }

                } else {
                    snackbar = Snackbar.make(view, "Select starting meal", Snackbar.LENGTH_SHORT);
                    snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                    snackbar.show();


                }
            } else if (Objects.equals(plan_type, "3 meals")) {
                if (subscriptionDetailBinding.radioStartingBreakfast.isChecked() || subscriptionDetailBinding.radioStartingLunch.isChecked() || subscriptionDetailBinding.radioStartingDinner.isChecked()) {

                    assert getArguments() != null;

                    userSubscriptionCall = apiInterface.add_subscription(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), sharedPreferences.getString(Constant.MESS_ID, null), sharedPreferences.getString(Constant.USER_ID, null), start_from, plan_type, three_meal_plan_starting_meal, three_meal_plan_second_meal, three_meal_plan_third_meal, three_meal_price, getArguments().getInt("subs_type"), mess_plan_id, subscriptionDetailBinding.tTotalPrice.getText().toString().substring(2));
                    alertDialog.show();
                    addSubscriptionOperations(userSubscriptionCall, view);
                } else {

                    snackbar = Snackbar.make(view, "Select starting meal", Snackbar.LENGTH_SHORT);
                    snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                    snackbar.show();
                }


            }

        } else {
            snackbar = Snackbar.make(view, "Select a Plan", Snackbar.LENGTH_SHORT);
            snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
            snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
            snackbar.show();

        }


    }

    private void addSubscriptionOperations(Call<UserSubscription> userSubscriptionCall, View view) {

        userSubscriptionCall.enqueue(new Callback<UserSubscription>() {
            @Override
            public void onResponse(@NonNull Call<UserSubscription> call, @NonNull Response<UserSubscription> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {

                    UserSubscription userSubscription = response.body();

                    userSubscriptionViewModel.setUserSubscriptionMutableLiveData(userSubscription);
                    try {

                        navController.navigate(R.id.action_subscribeForBottomSheet_to_addressDetailFragment);
                    } catch (Exception ignored) {
                    }

                    alertDialog.dismiss();

                } else {
                    try {
                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        if (response.code() == 303) {
                            snackbar = Snackbar.make(view, response.code() + ": " + detailResponse.getDetail(), Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                            snackbar.show();
                        } else {
                            snackbar = Snackbar.make(view, response.code() + ": " + detailResponse.getDetail(), Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                            snackbar.show();
                        }

                        alertDialog.dismiss();

                    } catch (Exception e) {
                        snackbar = Snackbar.make(view, response.code() + ": " + e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                        snackbar.show();

                        alertDialog.dismiss();

                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<UserSubscription> call, @NonNull Throwable t) {
                snackbar = Snackbar.make(view, "Something went error: " + t.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                snackbar.show();

                alertDialog.dismiss();
            }
        });
    }

    private List<String> removeFirstSelectedItem(String meal_type) {
        List<String> choose_meal = new ArrayList<>();
        if (mess_info_detail.getMessServingMeal().getBreakfast()) {
            choose_meal.add("Breakfast");
        }

        if (mess_info_detail.getMessServingMeal().getLunch()) {
            choose_meal.add("Lunch");
        }

        if (mess_info_detail.getMessServingMeal().getDinner()) {
            choose_meal.add("Dinner");
        }
        choose_meal.remove(meal_type);


        return choose_meal;
    }


    private void whichDayWhichMeal(String which_day,View view) {
        List<String> meal_type = new ArrayList<>();
        if (snackbar!=null){
            snackbar.dismiss();
        }

        subscriptionDetailBinding.autoCompleteTvFirstMeal.setVisibility(View.VISIBLE);
        subscriptionDetailBinding.autoCompleteTvSecondMeal.setVisibility(View.VISIBLE);

        if (which_day.equalsIgnoreCase("sun")) {
            if (mess_info_detail.getMessServingDay().get(0).getSundayValue().getBreakfast()) {
                meal_type.add("Breakfast");
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.VISIBLE);
            }else {
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.GONE);


            }
            if (mess_info_detail.getMessServingDay().get(0).getSundayValue().getLunch()) {
                meal_type.add("Lunch");
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.GONE);

            }

            if (mess_info_detail.getMessServingDay().get(0).getSundayValue().getDinner()) {
                meal_type.add("Dinner");
                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.VISIBLE);

            }else {

                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.GONE);

            }

            if (meal_type.size() == 0) {
                snackbar = Snackbar.make(view, "Not serving on sunday, Choose other day to get subscription", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                snackbar.show();
                subscriptionDetailBinding.autoCompleteTvFirstMeal.setVisibility(View.GONE);
                subscriptionDetailBinding.autoCompleteTvSecondMeal.setVisibility(View.GONE);
            }

        } else if (which_day.equalsIgnoreCase("mon")) {

            if (mess_info_detail.getMessServingDay().get(0).getMondayValue().getBreakfast()) {
                meal_type.add("Breakfast");
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.GONE);


            }
            if (mess_info_detail.getMessServingDay().get(0).getMondayValue().getLunch()) {
                meal_type.add("Lunch");
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.GONE);

            }

            if (mess_info_detail.getMessServingDay().get(0).getMondayValue().getDinner()) {
                meal_type.add("Dinner");
                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.VISIBLE);

            }else {

                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.GONE);

            }

            if (meal_type.size() == 0) {
                snackbar = Snackbar.make(view, "Not serving on Monday, Choose other day to get subscription", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                snackbar.show();
                subscriptionDetailBinding.autoCompleteTvFirstMeal.setVisibility(View.GONE);
                subscriptionDetailBinding.autoCompleteTvSecondMeal.setVisibility(View.GONE);
            }
        } else if (which_day.equalsIgnoreCase("tue")) {

            if (mess_info_detail.getMessServingDay().get(0).getTuesdayValue().getBreakfast()) {
                meal_type.add("Breakfast");
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.GONE);


            }
            if (mess_info_detail.getMessServingDay().get(0).getTuesdayValue().getLunch()) {
                meal_type.add("Lunch");
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.GONE);

            }

            if (mess_info_detail.getMessServingDay().get(0).getTuesdayValue().getDinner()) {
                meal_type.add("Dinner");
                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.VISIBLE);

            }else {

                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.GONE);

            }

            if (meal_type.size() == 0) {
                snackbar = Snackbar.make(view, "Not serving on tuesday, Choose other day to get subscription", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                snackbar.show();
            }
        } else if (which_day.equalsIgnoreCase("wed")) {

            if (mess_info_detail.getMessServingDay().get(0).getWednesdayValue().getBreakfast()) {
                meal_type.add("Breakfast");
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.GONE);


            }
            if (mess_info_detail.getMessServingDay().get(0).getWednesdayValue().getLunch()) {
                meal_type.add("Lunch");
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.GONE);

            }

            if (mess_info_detail.getMessServingDay().get(0).getWednesdayValue().getDinner()) {
                meal_type.add("Dinner");
                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.VISIBLE);

            }else {

                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.GONE);

            }

            if (meal_type.size() == 0) {
                snackbar = Snackbar.make(view, "Not serving on Wednesday, Choose other day to get subscription", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                snackbar.show();
                subscriptionDetailBinding.autoCompleteTvFirstMeal.setVisibility(View.GONE);
                subscriptionDetailBinding.autoCompleteTvSecondMeal.setVisibility(View.GONE);
            }
        } else if (which_day.equalsIgnoreCase("thu")) {

            if (mess_info_detail.getMessServingDay().get(0).getThrusdayValue().getBreakfast()) {
                meal_type.add("Breakfast");
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.GONE);


            }
            if (mess_info_detail.getMessServingDay().get(0).getThrusdayValue().getLunch()) {
                meal_type.add("Lunch");
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.GONE);

            }

            if (mess_info_detail.getMessServingDay().get(0).getThrusdayValue().getDinner()) {
                meal_type.add("Dinner");
                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.VISIBLE);

            }else {

                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.GONE);

            }

            if (meal_type.size() == 0) {
                snackbar = Snackbar.make(view, "Not serving on Thursday, Choose other day to get subscription", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                snackbar.show();
                subscriptionDetailBinding.autoCompleteTvFirstMeal.setVisibility(View.GONE);
                subscriptionDetailBinding.autoCompleteTvSecondMeal.setVisibility(View.GONE);
            }
        } else if (which_day.equalsIgnoreCase("fri")) {

            if (mess_info_detail.getMessServingDay().get(0).getFridayValue().getBreakfast()) {
                meal_type.add("Breakfast");
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.GONE);


            }
            if (mess_info_detail.getMessServingDay().get(0).getFridayValue().getLunch()) {
                meal_type.add("Lunch");
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.GONE);

            }

            if (mess_info_detail.getMessServingDay().get(0).getFridayValue().getDinner()) {
                meal_type.add("Dinner");
                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.VISIBLE);

            }else {

                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.GONE);

            }

            if (meal_type.size() == 0) {
                snackbar = Snackbar.make(view, "Not serving on Friday, Choose other day to get subscription", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                snackbar.show();
                subscriptionDetailBinding.autoCompleteTvFirstMeal.setVisibility(View.GONE);
                subscriptionDetailBinding.autoCompleteTvSecondMeal.setVisibility(View.GONE);
            }
        } else if (which_day.equalsIgnoreCase("sat")) {

            if (mess_info_detail.getMessServingDay().get(0).getSaturdayValue().getBreakfast()) {
                meal_type.add("Breakfast");
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingBreakfast.setVisibility(View.GONE);


            }
            if (mess_info_detail.getMessServingDay().get(0).getSaturdayValue().getLunch()) {
                meal_type.add("Lunch");
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.VISIBLE);

            }else {
                subscriptionDetailBinding.radioStartingLunch.setVisibility(View.GONE);


            }

            if (mess_info_detail.getMessServingDay().get(0).getSaturdayValue().getDinner()) {
                meal_type.add("Dinner");
                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.VISIBLE);

            }else {

                subscriptionDetailBinding.radioStartingDinner.setVisibility(View.GONE);

            }

            if (meal_type.size() == 0) {
                snackbar = Snackbar.make(view, "No  serving on Saturday, Choose other day to get subscription", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAnchorView(subscriptionDetailBinding.bottomMenuBtnPlanFor);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
                snackbar.show();
                subscriptionDetailBinding.autoCompleteTvFirstMeal.setVisibility(View.GONE);
                subscriptionDetailBinding.autoCompleteTvSecondMeal.setVisibility(View.GONE);
            }
        }


        first_meals_type = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, meal_type);
        second_select = first_meals_type;


    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null) {
            snackbar.dismiss();

        }
    }


}