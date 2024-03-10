package in.mesway.fragments.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import in.mesway.R;
import in.mesway.Response.UpcomingMeal.UserSubscription;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.UserSubscriptionViewModel;

import in.mesway.databinding.FragmentOrderPlacedBinding;
//created timestamp, is_deleted address, meal_type
//click ko lena h, geoDecoader, get_order, image_handle, progress bar, location_permission_disabled

//next time
//notification, when meal was delivered then show rating button, location api ,review and rating



public class OrderPlacedFragment extends Fragment {
    private FragmentOrderPlacedBinding orderPlacedBinding;
    private NavController navController;
    private AlertDialog alertDialog;
    private UserSubscriptionViewModel userSubscriptionViewModel;
    private Snackbar snackbar;


    public OrderPlacedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        orderPlacedBinding=FragmentOrderPlacedBinding.inflate(inflater,container,false);

        return orderPlacedBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        initView();
        userSubsObserver(view);
        clickHandle();

      snackbar=  Snackbar.make(view,"Order Placed Successfully",Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(),R.color.green));
                snackbar.show();


    }

    private void clickHandle() {
        orderPlacedBinding.btnDone.setOnClickListener(view -> requireActivity().finish());

        orderPlacedBinding.btnTrackOrder.setOnClickListener(view -> {
            NavOptions navOptions= new NavOptions.Builder().setPopUpTo(R.id.orderPlacedFragment,true).build();
            try {

                navController.navigate(OrderPlacedFragmentDirections.actionOrderPlacedFragmentToSubscriptionsFragment2(),navOptions);
            }catch (Exception ignored){}
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadSubscriptionDetail(UserSubscription user_subscription) {
        if (user_subscription!=null){

            orderPlacedBinding.linearLayoutOrderPlaced.setVisibility(View.VISIBLE);
            orderPlacedBinding.btnDone.setVisibility(View.VISIBLE);
           loadMessImage(user_subscription.getMessId(),user_subscription.getMess_basic_info().getMess_images().get(0).getBigImage().get(0));
            orderPlacedBinding.tMessName.setText(user_subscription.getMess_basic_info().getMess_name());
            orderPlacedBinding.tMessAddress.setText(user_subscription.getMess_basic_info().getMess_location().get(0).getCompanyAddress());

            orderPlacedBinding.tPlanTypeValue.setText(user_subscription.getPlanType()+"/day");
            orderPlacedBinding.tStartingMealValue.setText(user_subscription.getStartingMeal());
            orderPlacedBinding.tPlanPriceValue.setText("\u20B9 "+user_subscription.getPlanPrice());
            orderPlacedBinding.tPayableAmountValue.setText("\u20B9 "+user_subscription.getPaymentValue());
            orderPlacedBinding.subscriptionStatus.setText(user_subscription.getStatus());
            orderPlacedBinding.tStartFrom.setText(user_subscription.getStartFrom());
            orderPlacedBinding.tPaymentModeValue.setText(user_subscription.getPaymentBy());
            orderPlacedBinding.tRefundableAmountValue.setText("\u20B9 "+user_subscription.getSecurity_money());


            if (user_subscription.getPaymentStatus().equals("Not done")){
                orderPlacedBinding.txtPaymentDone.setText("Not Paid");
            }else {
                orderPlacedBinding.txtPaymentDone.setText(user_subscription.getPaymentStatus());
                orderPlacedBinding.txtPaymentDone.setTextColor(ContextCompat.getColor(requireActivity(),R.color.green));
            }


alertDialog.dismiss();

        }else {
            Toast.makeText(requireActivity(), "Something went error", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        }




    }

    private void loadMessImage(String mess_id,String img_value){

        File folder = requireActivity().getDir("mesway_img", Context.MODE_PRIVATE);

        File path = new File(folder, mess_id);
        if (path.exists()) {
            Bitmap mess_bitmap = BitmapFactory.decodeFile(path.getPath());
            orderPlacedBinding.imgMess.setImageBitmap(mess_bitmap);
        }else {
            Glide.with(requireActivity())
                    .asBitmap()
                    .load(img_value)
                    .timeout(600000).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                            File file = new File(folder, mess_id);

                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, fos);


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();

                            }


                            return false;
                        }
                    })
                    .into(orderPlacedBinding.imgMess);

        }


        }

    private void userSubsObserver(View view) {
        alertDialog.show();
        userSubscriptionViewModel.getUserSubsLiveData().observe(requireActivity(), new Observer<UserSubscription>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(UserSubscription userSubscription) {
                   loadSubscriptionDetail(userSubscription);


            }
        });
    }

    private void initView() {
        alertDialog= Reusable.alertDialog(requireActivity());
        userSubscriptionViewModel=new ViewModelProvider(requireActivity()).get(UserSubscriptionViewModel.class);
        SharedPreferences sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());

    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar!=null){
            snackbar.dismiss();

        }
    }


}