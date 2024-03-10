package in.mesway.fragments.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Objects;

import in.mesway.R;
import in.mesway.Response.Location.MessInfo;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.Rusable.UpdateImgResponseInterface;
import in.mesway.ViewModels.ImageViewModel;
import in.mesway.ViewModels.MessInfoViewModel;
import in.mesway.databinding.FragmentMainDetailBinding;


public class MainDetailFragment extends Fragment {
    private FragmentMainDetailBinding mainDetailBinding;
    private TextView mess_name, mess_rating, how_many_rated, mess_for, mess_address;
    private NavController navController;
    private MessInfo mess_info_detail;
    private MessInfoViewModel messInfoViewModel;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;
    private ImageViewModel imageViewModel;


    public MainDetailFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainDetailBinding = FragmentMainDetailBinding.inflate(inflater, container, false);

        return mainDetailBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        initView();
        setDataFromHomeActivity(view);
        clickHandle();


    }

    private void isLoading() {
        Observer<Boolean> is_loading = aBoolean -> {
            if (aBoolean) {
                alertDialog.show();
            } else {

                alertDialog.dismiss();

            }
        };

        messInfoViewModel.isLoading.observe(requireActivity(), is_loading);


    }

    private void statusCodeObserver(View view) {
        Observer<Integer> status_code_observer = integer -> {
            if (integer == 200) {
                getMessDetailInfo(view);

            } else {
                detailObserver(view);


            }


        };

        messInfoViewModel.status_code.observe(requireActivity(), status_code_observer);

    }

    private void detailObserver(View view) {
        Observer<String> detail_observer = s -> Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();


        messInfoViewModel.detail.observe(requireActivity(), detail_observer);


    }

    private void getMessDetailInfo(View view) {
        Observer<MessInfo> messInfoObserver = messInfo -> {
            mess_info_detail = messInfo;
            messTimeOperation();
            servingDaysOperation();
            whichMealServing();

            menuPhotoOperation();
            subscriptionOfferedOperation();


        };

        messInfoViewModel.messInfoMutableLiveData.observe(requireActivity(), messInfoObserver);


    }

    private void whichMealServing() {
        if (!mess_info_detail.getMessServingMeal().getBreakfast())
        {
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingBreakfast7.setVisibility(View.GONE);
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingBreakfast15.setVisibility(View.GONE);
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingBreakfastDay.setVisibility(View.GONE);
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingBreakfastMonth.setVisibility(View.GONE);





        }

        if (!mess_info_detail.getMessServingMeal().getLunch())
        {
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingLunch7.setVisibility(View.GONE);
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingLunch15.setVisibility(View.GONE);
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingLunchDay.setVisibility(View.GONE);
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingLunchMonth.setVisibility(View.GONE);


        }

        if (!mess_info_detail.getMessServingMeal().getDinner())
        {
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingDinner7.setVisibility(View.GONE);
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingDinner15.setVisibility(View.GONE);
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingDinnerDay.setVisibility(View.GONE);
            mainDetailBinding.includeSubsLayout.constraintLayoutIsServingDinnerMonth.setVisibility(View.GONE);



        }

    }

    @SuppressLint("SetTextI18n")
    private void subscriptionOfferedOperation() {
        for (int i = 0; i < mess_info_detail.getMessSubscriptionType().size(); i++) {
            if (mess_info_detail.getMessSubscriptionType().get(i).getMessSubsTypes().getSubsDay() == 1) {

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay(), "not_serving")) {

                    mainDetailBinding.includeSubsLayout.tOneMealPriceDay.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay());
                } else {

                    mainDetailBinding.includeSubsLayout.tOneMealPriceDay.setText("Not Serving");

                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay(), "not_serving")) {

                    mainDetailBinding.includeSubsLayout.tTwoMealPriceDay.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay());

                } else {

                    mainDetailBinding.includeSubsLayout.tTwoMealPriceDay.setText("Not Serving");
                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay(), "not_serving")) {
                    mainDetailBinding.includeSubsLayout.tThreeMealPriceDay.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay());


                } else {
                    mainDetailBinding.includeSubsLayout.tThreeMealPriceDay.setText("Not Serving");

                }

                if (mess_info_detail.getMessSubscriptionType().get(i).getSecurity_money()!=0){
                    mainDetailBinding.includeSubsLayout.condSecurityDeposit1.setVisibility(View.VISIBLE);
                    mainDetailBinding.includeSubsLayout.tSecurityPriceAmount1.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getSecurity_money());

                }  else{

                    mainDetailBinding.includeSubsLayout.condSecurityDeposit1.setVisibility(View.GONE);
                }

                mainDetailBinding.includeSubsLayout.consSubsOneDay.setVisibility(View.VISIBLE);


            } else if (mess_info_detail.getMessSubscriptionType().get(i).getMessSubsTypes().getSubsDay() == 7) {

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay(), "not_serving")) {

                    mainDetailBinding.includeSubsLayout.tOneMealPrice7.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay());
                } else {

                    mainDetailBinding.includeSubsLayout.tOneMealPrice7.setText("Not Serving");

                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay(), "not_serving")) {

                    mainDetailBinding.includeSubsLayout.tTwoMealPrice7.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay());

                } else {

                    mainDetailBinding.includeSubsLayout.tTwoMealPrice7.setText("Not Serving");
                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay(), "not_serving")) {
                    mainDetailBinding.includeSubsLayout.tThreeMealPrice7.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay());


                } else {
                    mainDetailBinding.includeSubsLayout.tThreeMealPrice7.setText("Not Serving");

                }

                if (mess_info_detail.getMessSubscriptionType().get(i).getSecurity_money()!=0){
                    mainDetailBinding.includeSubsLayout.condSecurityDeposit7.setVisibility(View.VISIBLE);
                    mainDetailBinding.includeSubsLayout.tSecurityPriceAmount7.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getSecurity_money());

                }  else{

                    mainDetailBinding.includeSubsLayout.condSecurityDeposit7.setVisibility(View.GONE);
                }
                mainDetailBinding.includeSubsLayout.consSubsSevenDay.setVisibility(View.VISIBLE);


            } else if (mess_info_detail.getMessSubscriptionType().get(i).getMessSubsTypes().getSubsDay() == 15) {
                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay(), "not_serving")) {

                    mainDetailBinding.includeSubsLayout.tOneMealPrice15.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay());
                } else {

                    mainDetailBinding.includeSubsLayout.tOneMealPrice15.setText("Not Serving");

                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay(), "not_serving")) {

                    mainDetailBinding.includeSubsLayout.tTwoMealPrice15.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay());

                } else {

                    mainDetailBinding.includeSubsLayout.tTwoMealPrice15.setText("Not Serving");
                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay(), "not_serving")) {
                    mainDetailBinding.includeSubsLayout.tThreeMealPrice15.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay());


                } else {
                    mainDetailBinding.includeSubsLayout.tThreeMealPrice15.setText("Not Serving");

                }

                if (mess_info_detail.getMessSubscriptionType().get(i).getSecurity_money()!=0){
                    mainDetailBinding.includeSubsLayout.condSecurityDeposit15.setVisibility(View.VISIBLE);
                    mainDetailBinding.includeSubsLayout.tSecurityPriceAmount15.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getSecurity_money());

                }  else{

                    mainDetailBinding.includeSubsLayout.condSecurityDeposit15.setVisibility(View.GONE);
                }

                mainDetailBinding.includeSubsLayout.consSubsFifteenDay.setVisibility(View.VISIBLE);

            } else if (mess_info_detail.getMessSubscriptionType().get(i).getMessSubsTypes().getSubsDay() == 30) {

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay(), "not_serving")) {

                    mainDetailBinding.includeSubsLayout.tOneMealPrice30.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getOneMealPricePerDay());
                } else {

                    mainDetailBinding.includeSubsLayout.tOneMealPrice30.setText("Not Serving");

                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay(), "not_serving")) {

                    mainDetailBinding.includeSubsLayout.tTwoMealPrice30.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getTwoMealPricePerDay());

                } else {

                    mainDetailBinding.includeSubsLayout.tTwoMealPrice30.setText("Not Serving");
                }

                if (!Objects.equals(mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay(), "not_serving")) {
                    mainDetailBinding.includeSubsLayout.tThreeMealPrice30.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getThreeMealPricePerDay());


                } else {
                    mainDetailBinding.includeSubsLayout.tThreeMealPrice30.setText("Not Serving");

                }

                if (mess_info_detail.getMessSubscriptionType().get(i).getSecurity_money()!=0){
                    mainDetailBinding.includeSubsLayout.condSecurityDeposit30.setVisibility(View.VISIBLE);
                    mainDetailBinding.includeSubsLayout.tSecurityPriceAmount30.setText("\u20B9 "+mess_info_detail.getMessSubscriptionType().get(i).getSecurity_money());

                }  else{

                    mainDetailBinding.includeSubsLayout.condSecurityDeposit30.setVisibility(View.GONE);
                }

                mainDetailBinding.includeSubsLayout.consSubsThirtyDay.setVisibility(View.VISIBLE);

            }
            mainDetailBinding.includeSubsLayout.txtSubscription.setVisibility(View.VISIBLE);
        }

        mainDetailBinding.lottieAnimation.setVisibility(View.GONE);
    }

    private void menuPhotoOperation() {

        mainDetailBinding.tMessRoutine.setVisibility(View.VISIBLE);
        if (mess_info_detail.getMessRoutinePhotos().get(0).getBreakfastRoutinePhoto() == null) {
            mainDetailBinding.consBreakfastMenu.setVisibility(View.GONE);

        }else{

         //   loadImage(mess_info_detail.getMessRoutinePhotos().get(0).getBreakfastRoutinePhoto(),mainDetailhBinding.imgBreakfastMenu,"breakfast");
            loadImage(mess_info_detail.getMessRoutinePhotos().get(0).getBreakfastRoutinePhoto(),mainDetailBinding.imgBreakfastMenu,"menu");
            ///for blur image when i click in small image then big pic blur becoz glide give bitmap of that dimension pic so we create a imageview which is big as big image but always visibility gone
            loadImage(mess_info_detail.getMessRoutinePhotos().get(0).getBreakfastRoutinePhoto(),mainDetailBinding.imgBreakfastMenuBigsizeForGlide,"menu");

            mainDetailBinding.consBreakfastMenu.setVisibility(View.VISIBLE);


        }

        if (mess_info_detail.getMessRoutinePhotos().get(0).getLunchRoutinePhoto() == null) {
            mainDetailBinding.consLunchMenu.setVisibility(View.GONE);

        }else {
            loadImage(mess_info_detail.getMessRoutinePhotos().get(0).getLunchRoutinePhoto(),mainDetailBinding.imgLunchMenu,"lunch");
            mainDetailBinding.consLunchMenu.setVisibility(View.VISIBLE);


        }

        if (mess_info_detail.getMessRoutinePhotos().get(0).getDinnerRoutinePhoto() == null) {
            mainDetailBinding.consDinnerMenu.setVisibility(View.GONE);

        }else {
            loadImage(mess_info_detail.getMessRoutinePhotos().get(0).getDinnerRoutinePhoto(),mainDetailBinding.imgDinnerMenu,"dinner");
            mainDetailBinding.consDinnerMenu.setVisibility(View.VISIBLE);


        }




    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    private void servingDaysOperation() {
        if (mess_info_detail.getMessServingDay().get(0).getMondayValue().getServingMealsId().equalsIgnoreCase("b35f6f14-e92c-4581-9c90-a593ff6b3a96")) {
            mainDetailBinding.monServing.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(), R.drawable.outline_cancel_24), null, null, null);
            mainDetailBinding.monServing.setCompoundDrawableTintList(ContextCompat.getColorStateList(requireActivity(), R.color.red));
            mainDetailBinding.consMonServing.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_not_serving_days));


        }

        if (mess_info_detail.getMessServingDay().get(0).getTuesdayValue().getServingMealsId().equalsIgnoreCase("b35f6f14-e92c-4581-9c90-a593ff6b3a96")) {
            mainDetailBinding.tueServing.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(), R.drawable.outline_cancel_24), null, null, null);
            mainDetailBinding.tueServing.setCompoundDrawableTintList(ContextCompat.getColorStateList(requireActivity(), R.color.red));
            mainDetailBinding.consTueServing.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_not_serving_days));


        }

        if (mess_info_detail.getMessServingDay().get(0).getWednesdayValue().getServingMealsId().equalsIgnoreCase("b35f6f14-e92c-4581-9c90-a593ff6b3a96")) {
            mainDetailBinding.wedServing.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(), R.drawable.outline_cancel_24), null, null, null);
            mainDetailBinding.wedServing.setCompoundDrawableTintList(ContextCompat.getColorStateList(requireActivity(), R.color.red));
            mainDetailBinding.consWedServing.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_not_serving_days));


        }
        if (mess_info_detail.getMessServingDay().get(0).getThrusdayValue().getServingMealsId().equalsIgnoreCase("b35f6f14-e92c-4581-9c90-a593ff6b3a96")) {
            mainDetailBinding.thuServing.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(), R.drawable.outline_cancel_24), null, null, null);
            mainDetailBinding.thuServing.setCompoundDrawableTintList(ContextCompat.getColorStateList(requireActivity(), R.color.red));
            mainDetailBinding.consThuServing.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_not_serving_days));


        }
        if (mess_info_detail.getMessServingDay().get(0).getFridayValue().getServingMealsId().equalsIgnoreCase("b35f6f14-e92c-4581-9c90-a593ff6b3a96")) {
            mainDetailBinding.friServing.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(), R.drawable.outline_cancel_24), null, null, null);
            mainDetailBinding.friServing.setCompoundDrawableTintList(ContextCompat.getColorStateList(requireActivity(), R.color.red));
            mainDetailBinding.consFriServing.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_not_serving_days));


        }

        if (mess_info_detail.getMessServingDay().get(0).getSaturdayValue().getServingMealsId().equalsIgnoreCase("b35f6f14-e92c-4581-9c90-a593ff6b3a96")) {
            mainDetailBinding.satServing.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(), R.drawable.outline_cancel_24), null, null, null);
            mainDetailBinding.satServing.setCompoundDrawableTintList(ContextCompat.getColorStateList(requireActivity(), R.color.red));
            mainDetailBinding.consSatServing.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_not_serving_days));


        }

        if (mess_info_detail.getMessServingDay().get(0).getSundayValue().getServingMealsId().equalsIgnoreCase("b35f6f14-e92c-4581-9c90-a593ff6b3a96")) {
            mainDetailBinding.sunServing.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(), R.drawable.outline_cancel_24), null, null, null);
            mainDetailBinding.sunServing.setCompoundDrawableTintList(ContextCompat.getColorStateList(requireActivity(), R.color.red));
            mainDetailBinding.consSunServing.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_not_serving_days));


        }
        mainDetailBinding.tServingDays.setVisibility(View.VISIBLE);
        mainDetailBinding.horiScrollServingDays.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void messTimeOperation() {
        if (!(Objects.equals(mess_info_detail.getMessTime().get(0).getBreakfastTime(), "none"))) {

            mainDetailBinding.tBreakfastValue.setText(mess_info_detail.getMessTime().get(0).getBreakfastTime() + " am");
        } else {
            mainDetailBinding.tBreakfastValue.setText("__");
        }
        if (!(Objects.equals(mess_info_detail.getMessTime().get(0).getLunchTime(), "none"))) {
            mainDetailBinding.tLunchValue.setText(mess_info_detail.getMessTime().get(0).getLunchTime() + " pm");
        } else {
            mainDetailBinding.tLunchValue.setText("__");
        }
        if (!(Objects.equals(mess_info_detail.getMessTime().get(0).getDinnerTime(), "none"))) {
            mainDetailBinding.tDinnerValue.setText(mess_info_detail.getMessTime().get(0).getDinnerTime() + " pm");
        } else {
            mainDetailBinding.tDinnerValue.setText("__");

        }

        mainDetailBinding.tMessTime.setVisibility(View.VISIBLE);
        mainDetailBinding.cardMessTime.setVisibility(View.VISIBLE);

    }

    private void clickHandle() {

        mainDetailBinding.consBreakfastMenu.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putString("type", mainDetailBinding.tBreakfastMenu.getText().toString());
            bundle.putString("type", "Menu");
            try {

                navController.navigate(R.id.action_mainDetailFragment_to_menuBigSizePicDialog, bundle);
            }catch (Exception ignored){}



        });

        mainDetailBinding.consLunchMenu.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", mainDetailBinding.tLunchMenu.getText().toString());
            try {

                navController.navigate(R.id.action_mainDetailFragment_to_menuBigSizePicDialog, bundle);
            }catch (Exception ignored){}

        });

        mainDetailBinding.consDinnerMenu.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", mainDetailBinding.tDinnerMenu.getText().toString());
            try {

                navController.navigate(R.id.action_mainDetailFragment_to_menuBigSizePicDialog, bundle);
            }catch (Exception ignored){}


        });


        mainDetailBinding.includeSubsLayout.consSubsOneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("subs_type", 1);
                    try {
                        navController.navigate(R.id.action_mainDetailFragment_to_subscribeForBottomSheet, bundle);

                    } catch (Exception ignored) {

                    }
                }else {
                    try {
                        navController.navigate(R.id.action_mainDetailFragment_to_doLoginFragment2);

                    }catch (Exception ignored){
                        Toast.makeText(requireActivity(), "Do login", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });


        mainDetailBinding.includeSubsLayout.btnSubsOneDay.setOnClickListener(view -> {
            if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {
                Bundle bundle = new Bundle();
                bundle.putInt("subs_type", 1);
                try {
                    navController.navigate(R.id.action_mainDetailFragment_to_subscribeForBottomSheet, bundle);

                } catch (Exception ignored) {

                }
            }else {
                try {
                    navController.navigate(R.id.action_mainDetailFragment_to_doLoginFragment2);

                }catch (Exception ignored){
                    Toast.makeText(requireActivity(), "Do login", Toast.LENGTH_SHORT).show();

                }

            }
        });


        mainDetailBinding.includeSubsLayout.consSubsSevenDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("subs_type", 7);

                    try {

                        navController.navigate(R.id.action_mainDetailFragment_to_subscribeForBottomSheet, bundle);
                    } catch (Exception ignored) {

                    }
                }else {
                    try {
                        navController.navigate(R.id.action_mainDetailFragment_to_doLoginFragment2);

                    }catch (Exception ignored){
                        Toast.makeText(requireActivity(), "Do login", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
        mainDetailBinding.includeSubsLayout.btnSubsSevenDay.setOnClickListener(view -> {
            if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {
                Bundle bundle = new Bundle();
                bundle.putInt("subs_type", 7);

                try {

                    navController.navigate(R.id.action_mainDetailFragment_to_subscribeForBottomSheet, bundle);
                } catch (Exception ignored) {

                }
            }else {
                try {
                    navController.navigate(R.id.action_mainDetailFragment_to_doLoginFragment2);

                }catch (Exception ignored){
                    Toast.makeText(requireActivity(), "Do login", Toast.LENGTH_SHORT).show();

                }

            }
        });

        mainDetailBinding.includeSubsLayout.consSubsFifteenDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("subs_type", 15);

                    try {

                        navController.navigate(R.id.action_mainDetailFragment_to_subscribeForBottomSheet, bundle);
                    } catch (Exception ignored) {

                    }

                }else {
                    try {
                        navController.navigate(R.id.action_mainDetailFragment_to_doLoginFragment2);

                    }catch (Exception ignored){
                        Toast.makeText(requireActivity(), "Do login", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

        mainDetailBinding.includeSubsLayout.btnSubs15Day.setOnClickListener(view -> {
            if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {
                Bundle bundle = new Bundle();
                bundle.putInt("subs_type", 15);

                try {

                    navController.navigate(R.id.action_mainDetailFragment_to_subscribeForBottomSheet, bundle);
                } catch (Exception ignored) {

                }

            }else {
                try {
                    navController.navigate(R.id.action_mainDetailFragment_to_doLoginFragment2);

                }catch (Exception ignored){
                    Toast.makeText(requireActivity(), "Do login", Toast.LENGTH_SHORT).show();

                }

            }
        });

        mainDetailBinding.includeSubsLayout.consSubsThirtyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("subs_type", 30);

                    try {

                        navController.navigate(R.id.action_mainDetailFragment_to_subscribeForBottomSheet, bundle);
                    } catch (Exception ignored) {

                    }

                }else {
                    try {
                        navController.navigate(R.id.action_mainDetailFragment_to_doLoginFragment2);

                    }catch (Exception ignored){
                        Toast.makeText(requireActivity(), "Do login", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
        mainDetailBinding.includeSubsLayout.btnSubsOneMonth.setOnClickListener(view -> {
            if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {
                Bundle bundle = new Bundle();
                bundle.putInt("subs_type", 30);

                try {

                    navController.navigate(R.id.action_mainDetailFragment_to_subscribeForBottomSheet, bundle);
                } catch (Exception ignored) {

                }

            }else {
                try {
                    navController.navigate(R.id.action_mainDetailFragment_to_doLoginFragment2);

                }catch (Exception ignored){
                    Toast.makeText(requireActivity(), "Do login", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void initView() {
        imageViewModel= new ViewModelProvider(requireActivity()).get(ImageViewModel.class);
        alertDialog = Reusable.alertDialog(requireActivity());
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());
        messInfoViewModel = new ViewModelProvider(requireActivity()).get(MessInfoViewModel.class);
        mess_name = mainDetailBinding.tMessName;
        mess_rating = mainDetailBinding.tMessRating;
        how_many_rated = mainDetailBinding.tHowManyRated;
        mess_for = mainDetailBinding.tMessFor;
        mess_address = mainDetailBinding.tMessAddress;
    }

    private void setDataFromHomeActivity(View views) {
        Bundle bundle = requireActivity().getIntent().getBundleExtra("bundle");
        File folder = requireActivity().getDir("mesway_img", Context.MODE_PRIVATE);
        if (bundle != null) {
            sharedPreferences.edit().putString(Constant.MESS_ID, bundle.getString("mess_id")).apply();
            File path = new File(folder, bundle.getString("mess_id"));
            if (path.exists()) {
                Bitmap mess_bitmap = BitmapFactory.decodeFile(path.getPath());
                mainDetailBinding.imgMessDetail.setImageBitmap(mess_bitmap);
            }else {
                Glide.with(requireActivity())
                        .asBitmap()
                        .load(bundle.getString("img_meal_address"))
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
                        .into(mainDetailBinding.imgMessDetail);
            }
            mess_name.setText(bundle.getString("mess_name"));
            mess_rating.setText(bundle.getString("mess_rating"));
            how_many_rated.setText(bundle.getString("how_many_rated"));
            mess_for.setText(bundle.getString("mess_for"));
            mess_address.setText(bundle.getString("mess_address"));


            if (mess_info_detail == null) {
                messInfoViewModel.getMessInfo(requireActivity());
            }
            statusCodeObserver(views);
            isLoading();


        } else {
            Toast.makeText(requireActivity(), "bundle null", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mainDetailBinding != null) {
            outState.putInt("scroll_position", mainDetailBinding.scrollViewMainDetail.getScrollY());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            int scroll = savedInstanceState.getInt("scroll_position");
            mainDetailBinding.scrollViewMainDetail.scrollTo(0, scroll);
        }
    }

    private void loadImage(String img_uri, ImageView targets,String name) {
        Glide.with(requireActivity())
                .asBitmap()
                .load(img_uri)
                .timeout(600000).listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        UpdateImgResponseInterface updateImgResponseInterface= new UpdateImgResponseInterface() {
                            @Override
                            public void getResponse(String url, Integer response_code) {
                                if (response_code==200){
                                    loadImage(url,targets,name);
                                }else {
                                    Toast.makeText(requireActivity(), "Check your internet connection" , Toast.LENGTH_SHORT).show();


                                }

                            }
                        };


                        Reusable.update_aws_img_url("routine",sharedPreferences.getString(Constant.MESS_ID,null),updateImgResponseInterface);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if(targets==mainDetailBinding.imgBreakfastMenuBigsizeForGlide) {
                            if (name.equals("menu")) {
                                imageViewModel.setBreakfast_img(resource);
                            } else if (name.equals("lunch")) {
                                imageViewModel.setLunch_img(resource);
                            } else {
                                imageViewModel.setDinner_img(resource);

                            }
                        }

                        return false;
                    }
                }).into(targets);


    }



}