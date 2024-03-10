package in.mesway.fragments.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.LoginSignupResponse;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.activity.App;
import in.mesway.activity.MainActivity;
import in.mesway.activity.SignupActivity;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentLoginBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding loginBinding;
    private TextInputEditText et_mobile_number;
    private TextInputLayout etl_mobile_number;
    private MaterialButton btn_send_otp, btn_skip;
    private NavController navController;
    private ApiInterface apiInterface;
    private SignupActivity signupActivity;
    private SharedPreferences sharedPreferences;
    private ProgressBar progress_bar;
    private NavOptions navOptions;
    private Activity activity;
    private Snackbar snackbar;

    public LoginFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);

        return loginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        signupActivity= (SignupActivity) getActivity();

        /*FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(loginBinding.btnSkip, "hero_image")
                .build();

        Navigation.findNavController(view).navigate(
                R.id.action_login_to_mainActivity,
                null, // Bundle of args
                null, // NavOptions
                extras);*/

        loginBinding.tTermConditionValue.setPaintFlags(loginBinding.tTermConditionValue.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        initAllView();
        navigate();
        numberOperation();
        sendOtpClick();
        clickHandel();


    }

    private void clickHandel() {
        loginBinding.tTermConditionValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.freeprivacypolicy.com/live/dcc163d2-8b6e-4d53-b885-e195c72bc43e"));
                startActivity(intent);
            }
        });
    }

    private void navigate() {

        if (sharedPreferences.getString(Constant.ACCESS_TOKEN, null) != null) {


            if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) <= 1) {
                try {

                    navController.navigate(LoginFragmentDirections.actionLoginToFirstSignup(), navOptions);
                }catch (Exception ignored){}

            } else {
                if(requireActivity().getIntent() !=null && requireActivity().getIntent().hasExtra("where")){
                    //   reusableViewModel.setNotification_where(requireActivity().getIntent().getExtras().getString("where"));
                    Bundle bundle = new Bundle();
                    bundle.putString("where",requireActivity().getIntent().getExtras().getString("where"));
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);


                }else {

                    startActivity(new Intent(requireActivity(), MainActivity.class));
                }

                activity.finish();

            }

        }


    }

    private void goToMainActivity() {
        if (sharedPreferences.getString(Constant.ACCESS_TOKEN, null) != null && Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) > 1) {
            startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        }
    }

    private void initAllView() {
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);

        et_mobile_number = loginBinding.etNumber;
        btn_send_otp = loginBinding.btnSendOtp;
        progress_bar = loginBinding.progressBar;
        btn_skip = loginBinding.btnSkip;
        etl_mobile_number = loginBinding.etNumberLayout;

        navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.login, true)
                .build();


    }

    private void sendOtpClick() {

        btn_send_otp.setOnClickListener(view -> {
            if (numberInputCheck()) {
                hideKeyboard();
                progress_bar.setVisibility(View.VISIBLE);
                btn_send_otp.setVisibility(View.INVISIBLE);
                btn_skip.setEnabled(false);


                Call<LoginSignupResponse> loginSignupResponseCall = apiInterface.do_sign_in(App.getAPIKey(), et_mobile_number.getText().toString().trim());
                loginSignupResponseCall.enqueue(new Callback<LoginSignupResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginSignupResponse> call, @NonNull Response<LoginSignupResponse> response) {
                        Gson gson = new GsonBuilder().create();
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                LoginSignupResponse loginSignupResponse = response.body();
                                assert loginSignupResponse != null;
                                if (sharedPreferences.getString(Constant.NOTIFICATION_TOKEN, null) == null || Objects.equals(sharedPreferences.getString(Constant.NOTIFICATION_TOKEN, null), "")) {
                                    buildNotificationToken();
                                    sharedPreferences.edit()
                                            .putString(Constant.USER_ID, loginSignupResponse.getUser_id().toString())
                                            .putString(Constant.USER_NUMBER, loginSignupResponse.getMobile_number())
                                            .apply();


                                    try {

                                        navController.navigate(LoginFragmentDirections.actionLoginToReceiveOtp(Objects.requireNonNull(et_mobile_number.getText()).toString()));
                                    }catch (Exception ignored){}

                                    progress_bar.setVisibility(View.GONE);
                                    btn_send_otp.setVisibility(View.VISIBLE);
                                    btn_skip.setEnabled(true);
                                   // Reusable.updateNotificationToken(activity,sharedPreferences.getString(Constant.NOTIFICATION_TOKEN,null));

                                } else {
                                    sharedPreferences.edit()
                                            .putString(Constant.USER_ID, loginSignupResponse.getUser_id().toString())
                                            .putString(Constant.USER_NUMBER, loginSignupResponse.getMobile_number())
                                            .apply();
                                    try {

                                        navController.navigate(LoginFragmentDirections.actionLoginToReceiveOtp(Objects.requireNonNull(et_mobile_number.getText()).toString()));
                                    }catch (Exception ignored){}
                                    progress_bar.setVisibility(View.GONE);
                                    btn_send_otp.setVisibility(View.VISIBLE);
                                    btn_skip.setEnabled(true);
                                }

                            }

                        } else {

                            try {

                                assert response.errorBody() != null;
                                DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                                snackbar=  Snackbar.make(view, response.code()+": " + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                                        snackbar.show();
                                progress_bar.setVisibility(View.GONE);
                                btn_send_otp.setVisibility(View.VISIBLE);
                                btn_skip.setEnabled(true);


                            } catch (Exception e) {

                               snackbar= Snackbar.make(view,  response.code()+": Something went wrong " , Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                                snackbar.show();
                                progress_bar.setVisibility(View.GONE);
                                btn_send_otp.setVisibility(View.VISIBLE);
                                btn_skip.setEnabled(true);
                            }


                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginSignupResponse> call, @NonNull Throwable t) {
                      snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went wrong" , Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();
                        progress_bar.setVisibility(View.GONE);
                        btn_send_otp.setVisibility(View.VISIBLE);
                        btn_skip.setEnabled(true);


                    }
                });


            }
        });

        btn_skip.setOnClickListener(view -> {
            String isAlive;
            isAlive=signupActivity.getIsLive();

            if (Objects.equals(isAlive, "active")) {

                activity.finish();
            } else {

                try {

                    navController.navigate(R.id.action_login_to_mainActivity);
                }catch (Exception ignored){}
                activity.finish();
            }


        });


    }

    private void numberOperation() {
        et_mobile_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                numberInputCheck();
            }
        });
    }


    private boolean numberInputCheck() {
        if (!TextUtils.isEmpty(et_mobile_number.getText())) {

            if (et_mobile_number.length() == 10) {
                etl_mobile_number.setErrorEnabled(false);
                return true;

            } else {
                etl_mobile_number.setErrorEnabled(true);
                etl_mobile_number.setError("Not Valid number");
                return false;
            }
        }
        return false;


    }


    private void hideKeyboard() {
        View views = activity.getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=context instanceof Activity ?(Activity) context:null;


    }
    private void buildNotificationToken() {
        SharedPreferences sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,activity);




        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String token="not_generated";
            try {
                token = task.getResult();
            }catch (Exception ignored){

            }
            if (task.isSuccessful()){
                sharedPreferences.edit()
                        .putString(Constant.NOTIFICATION_TOKEN,token)
                        .apply();
                Log.e("notification_token",token);

            }else {

                Log.e("notification_token","error");

            }

        });





    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar!=null){
            snackbar.dismiss();

        }
    }

}