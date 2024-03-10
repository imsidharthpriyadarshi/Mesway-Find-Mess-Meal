package in.mesway.fragments.details;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.UpcomingMeal.UserSubscription;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.UserSubscriptionViewModel;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentFinalPaymentBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FinalPaymentFragment extends Fragment {
    public FragmentFinalPaymentBinding finalPaymentBinding;
    private NavController navController;
    private AlertDialog alertDialog;
    private UserSubscriptionViewModel userSubscriptionViewModel;
    private SharedPreferences sharedPreferences;
    private UserSubscription user_subscription;
    private ApiInterface apiInterface;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        finalPaymentBinding=FragmentFinalPaymentBinding.inflate(inflater,container,false);
        return finalPaymentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
       // CheckoutActivity checkoutActivity = new CheckoutActivity();

        initView();
        userSubsObserver();
        clickHandle(view);






    }



    @SuppressLint("SetTextI18n")
    private void userSubsObserver() {
        alertDialog.show();
        userSubscriptionViewModel.getUserSubsLiveData().observe(requireActivity(), userSubscription -> {
            user_subscription=userSubscription;
            finalPaymentBinding.tvAmountDisplay.setText("\u20B9 "+userSubscription.getPaymentValue());
            finalPaymentBinding.btnPlaceOrder.setText("Pay "+"\u20B9 "+userSubscription.getPaymentValue());
            finalPaymentBinding.upiPrice.setText("\u20B9 "+userSubscription.getPaymentValue());
            finalPaymentBinding.cardPrice.setText("\u20B9 "+userSubscription.getPaymentValue()+" +2-3%");
            finalPaymentBinding.netBankingPrice.setText("\u20B9 "+userSubscription.getPaymentValue()+" +2-3%");

            finalPaymentBinding.codPrice.setText("\u20B9 "+userSubscription.getPaymentValue());




            alertDialog.dismiss();

        });
    }

    private void addPaymentToSubs(View view) {
        alertDialog.show();
        if (user_subscription!=null){


        Call<UserSubscription> userSubscriptionCall = apiInterface.add_payment_to_subscription(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null),user_subscription.getSubscriptionId(),sharedPreferences.getString(Constant.USER_ID,null),"offline","cod");
        userSubscriptionCall.enqueue(new Callback<UserSubscription>() {
            @Override
            public void onResponse(@NonNull Call<UserSubscription> call, @NonNull Response<UserSubscription> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code()==200){
                    alertDialog.dismiss();
                    userSubscriptionViewModel.setUserSubscriptionMutableLiveData(response.body());
                    NavOptions navOptions= new NavOptions.Builder()
                            .setPopUpTo(R.id.mainDetailFragment,true).build();
                    try {

                        navController.navigate(FinalPaymentFragmentDirections.actionFinalPaymentFragmentToOrderPlacedFragment(),navOptions);
                    }catch (Exception ignored){}

                }else {
                    try {
                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        Snackbar.make(view,response.code()+": "+detailResponse.getDetail(),Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(requireActivity(),R.color.red))
                                .show();
                        alertDialog.dismiss();

                    }catch (Exception e){
                        Snackbar.make(view,response.code()+": "+e.getMessage(),Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(requireActivity(),R.color.red))
                                .show();
                        alertDialog.dismiss();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserSubscription> call, @NonNull Throwable t) {
                Snackbar.make(view,"Something went error: "+t.getMessage(),Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(requireActivity(),R.color.red))
                        .show();
                alertDialog.dismiss();
            }
        });
        }else {
            Snackbar.make(view,"Something went error",Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireActivity(),R.color.red))
                    .show();
            alertDialog.dismiss();
        }
    }

    private void initView() {
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,requireActivity());
        alertDialog = Reusable.alertDialog(requireActivity());
        userSubscriptionViewModel = new ViewModelProvider(requireActivity()).get(UserSubscriptionViewModel.class);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface= retrofit.create(ApiInterface.class);



    }

    private void clickHandle(View views) {

       /* finalPaymentBinding.consUpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //upi_enabled();
            }
        });

        finalPaymentBinding.consCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //card_enabled();

            }
        });

        finalPaymentBinding.consNetBanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // net_banking_enabled();

                  }
        });*/
        finalPaymentBinding.consCashOnDelivery.setOnClickListener(view -> cod_enabled());

        finalPaymentBinding.checkBoxCashOnDelivery.setOnClickListener(view -> cod_enabled());

       /* finalPaymentBinding.checkBoxCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //card_enabled();

            }
        });

        finalPaymentBinding.checkBoxUpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upi_enabled();

            }
        });

        finalPaymentBinding.checkBoxNetbanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // net_banking_enabled();

            }
        });*/


        finalPaymentBinding.btnPlaceOrder.setOnClickListener(view -> {
          /*  if (is_upi){

                bundle.putString("which_selected","is_upi");
                Intent goToPayment= new Intent(requireActivity(),CheckoutActivity.class);
                goToPayment.putExtras(bundle);
                startActivity(goToPayment);


            }else if (is_card){
                bundle.putString("which_selected","is_card");
                Intent goToPayment= new Intent(requireActivity(),CheckoutActivity.class);
                goToPayment.putExtras(bundle);
                startActivity(goToPayment);

            }else if (is_net_banking){
                bundle.putString("which_selected","is_net_banking");
                Intent goToPayment= new Intent(requireActivity(),CheckoutActivity.class);
                goToPayment.putExtras(bundle);
                startActivity(goToPayment);

            } else*/

                addPaymentToSubs(views);








        });

        finalPaymentBinding.imgBackArrow.setOnClickListener(view -> navController.popBackStack());




    }

  /*  private void doPayment() {

        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "mesway.in@ybl")
                        .appendQueryParameter("pn", "Mesway")
                        .appendQueryParameter("mc", "BCR2DN4TYSSIRPCT")
                        .appendQueryParameter("tr", "239uiuijhb3")
                        .appendQueryParameter("tn", "You are paying to Mesway")
                        .appendQueryParameter("am", "1")
                        .appendQueryParameter("cu", "INR")
                        .appendQueryParameter("url",  "https://play.google.com/store/apps/details?id=in.thalionline")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
      //  intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);


        startActivityForResult(intent, TEZ_REQUEST_CODE);
    }*/

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEZ_REQUEST_CODE) {

            if (resultCode==200) {

                // Process based on the data in response.
                Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(requireActivity(), "Not Success", Toast.LENGTH_SHORT).show();

            }
        }

    }*/

/*
    private void upi_enabled(){

        finalPaymentBinding.checkBoxUpi.setChecked(true);
        finalPaymentBinding.checkBoxCard.setChecked(false);
        finalPaymentBinding.checkBoxNetbanking.setChecked(false);
        finalPaymentBinding.checkBoxCashOnDelivery.setChecked(false);

    }

    private void card_enabled(){
        finalPaymentBinding.checkBoxUpi.setChecked(false);
        finalPaymentBinding.checkBoxCard.setChecked(true);
        finalPaymentBinding.checkBoxNetbanking.setChecked(false);
        finalPaymentBinding.checkBoxCashOnDelivery.setChecked(false);



    }

    private void net_banking_enabled(){
        finalPaymentBinding.checkBoxUpi.setChecked(false);
        finalPaymentBinding.checkBoxCard.setChecked(false);
        finalPaymentBinding.checkBoxNetbanking.setChecked(true);
        finalPaymentBinding.checkBoxCashOnDelivery.setChecked(false);



    }
*/

    private void cod_enabled(){
        finalPaymentBinding.checkBoxUpi.setChecked(false);
        finalPaymentBinding.checkBoxCard.setChecked(false);
        finalPaymentBinding.checkBoxNetbanking.setChecked(false);
        finalPaymentBinding.checkBoxCashOnDelivery.setChecked(true);



    }
}
