package in.mesway.fragments.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.OtpResponse;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.ImportantViewmodel;
import in.mesway.activity.App;
import in.mesway.activity.MainActivity;
import in.mesway.activity.SignupActivity;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentReceiveOtpBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ReceiveOtpFragment extends Fragment {
    private FragmentReceiveOtpBinding otpBinding;
    private TextInputEditText et_otp;
    private TextInputLayout etl_otp;
    private MaterialButton btn_verify,btn_resend;
    private NavController navController;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private ApiInterface apiInterface;
    private NavOptions navOptions;
    private SignupActivity signupActivity;
    private Activity activity;
    private Snackbar snackbar;

    private ImportantViewmodel importantViewmodel;



    

    

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        otpBinding = FragmentReceiveOtpBinding.inflate(inflater,container,false);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface =retrofit.create(ApiInterface.class);
        return otpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        signupActivity= (SignupActivity) getActivity();

        initView();
        otpOperation();
        verifyOtpClick();
    }

    private void verifyOtpClick() {
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                progressBar.setVisibility(View.VISIBLE);
                btn_verify.setVisibility(View.INVISIBLE);
                btn_resend.setEnabled(false);
                Call<OtpResponse> otpResponseCall = apiInterface.verify_number(App.getAPIKey(),sharedPreferences.getString(Constant.USER_NUMBER,null),et_otp.getText().toString().trim());

                otpResponseCall.enqueue(new Callback<OtpResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<OtpResponse> call, @NonNull Response<OtpResponse> response) {
                        Gson gson = new GsonBuilder().create();

                        if (response.isSuccessful()){
                            if (response.code()==200){
                                OtpResponse otpResponse = response.body();
                                assert otpResponse != null;
                                Reusable.updateNotificationToken(activity,sharedPreferences.getString(Constant.NOTIFICATION_TOKEN,null));

                                sharedPreferences.edit()
                                        .putString(Constant.ACCESS_TOKEN,otpResponse.getAccess_token())
                                        .putString(Constant.REGISTRATION_STEP,String.valueOf(otpResponse.getReg_steps()))
                                        .putString(Constant.USER_EMAIL,otpResponse.getEmail())
                                        .apply();

                                importantViewmodel.setAccess_token_update(true);
                                if (otpResponse.getReg_steps()>1){
                                    String isAlive;

                                    isAlive=signupActivity.getIsLive();

                                    if (Objects.equals(isAlive, "active")) {
                                        activity.finish();

                                    } else {
                                    startActivity(new Intent(activity, MainActivity.class));
                                    activity.finish();
                                    }
                                }else {
                                    try {

                                        navController.navigate(ReceiveOtpFragmentDirections.actionReceiveOtpToFirstSignup(),navOptions);
                                    }catch (Exception ignored){}

                                    progressBar.setVisibility(View.GONE);
                                    btn_verify.setVisibility(View.VISIBLE);
                                    btn_resend.setEnabled(true);
                                }


                            }




                        }else {
                            try {

                                assert response.errorBody() != null;
                                DetailResponse errorResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                             snackbar=  Snackbar.make(view,response.code()+": "+errorResponse.getDetail(),Snackbar.LENGTH_LONG);
                                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));
                                        snackbar.show();
                                progressBar.setVisibility(View.GONE);
                                btn_verify.setVisibility(View.VISIBLE);
                                btn_resend.setEnabled(true);

                            }catch (Exception e){
                               snackbar= Snackbar.make(view,response.code()+": "+"Something went wrong",Snackbar.LENGTH_LONG);
                                       snackbar.show();
                                progressBar.setVisibility(View.GONE);
                                btn_verify.setVisibility(View.VISIBLE);
                                btn_resend.setEnabled(true);


                            }


                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<OtpResponse> call, @NonNull Throwable t) {
                   snackbar=     Snackbar.make(view,"Failure: Check internet connection/Something went wrong ",Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();
                        progressBar.setVisibility(View.GONE);
                        btn_verify.setVisibility(View.VISIBLE);
                        btn_resend.setEnabled(true);


                    }
                });



            }
        });

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Resend Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void otpOperation() {
        et_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkOtpInput();
            }
        });
    }

    private void initView() {
        et_otp=otpBinding.etOtp;
        importantViewmodel= new ViewModelProvider((ViewModelStoreOwner) activity).get(ImportantViewmodel.class);
        etl_otp=otpBinding.etlOtp;
        btn_verify=otpBinding.btnVerify;
        btn_resend=otpBinding.btnResend;
        progressBar=otpBinding.progressBar;
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,activity);
        ReceiveOtpFragmentArgs args = ReceiveOtpFragmentArgs.fromBundle(getArguments());
        otpBinding.tVerificationNumber.setText("+91"+args.getNumber());
        navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.login, true)
                .build();



    }


    private void hideKeyboard() {
        View views = activity.getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }
    private void checkOtpInput(){
        if (!TextUtils.isEmpty(et_otp.getText())){
            if (et_otp.length()==6){
                etl_otp.setErrorEnabled(false);

                btn_verify.setEnabled(true);
            }else {
                etl_otp.setErrorEnabled(true);
                btn_verify.setEnabled(false);
            }

        }
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