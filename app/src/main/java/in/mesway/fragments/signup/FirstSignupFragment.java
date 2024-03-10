package in.mesway.fragments.signup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.FirstTimeUserResponse;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.activity.MainActivity;
import in.mesway.activity.SignupActivity;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentFirstSignupBinding;
import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FirstSignupFragment extends Fragment {
    private FragmentFirstSignupBinding firstSignupBinding;
    private NavController navController;
    private TextInputEditText et_full_name,et_email;
    private MaterialButton btn_done;
    private ProgressBar progressBar;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;

    private SignupActivity signupActivity;
    private Activity activity;
    private  Snackbar snackbar;




    public FirstSignupFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firstSignupBinding = FragmentFirstSignupBinding.inflate(inflater,container,false);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface =retrofit.create(ApiInterface.class);
        return firstSignupBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        signupActivity= (SignupActivity) getActivity();

        initView();
        inputOperations();
        clickHandle();





    }

    private void clickHandle() {
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                progressBar.setVisibility(View.VISIBLE);
                btn_done.setVisibility(View.INVISIBLE);
                et_email.setEnabled(false);
                et_full_name.setEnabled(false);
                Call<FirstTimeUserResponse> firstTimeUserResponseCall = apiInterface.first_time_user(Constant.TOKEN_TYPE_VALUE+ sharedPreferences.getString(Constant.ACCESS_TOKEN,null),sharedPreferences.getString(Constant.USER_ID,null),et_full_name.getText().toString().trim(),et_email.getText().toString(),sharedPreferences.getString(Constant.NOTIFICATION_TOKEN,null) );
                firstTimeUserResponseCall.enqueue(new Callback<FirstTimeUserResponse>() {
                    @SuppressLint("SuspiciousIndentation")
                    @Override
                    public void onResponse(@NonNull Call<FirstTimeUserResponse> call, @NonNull Response<FirstTimeUserResponse> response) {
                        Gson gson =new GsonBuilder().create();
                        if (response.code()==200){
                            FirstTimeUserResponse firstTimeUserResponse = response.body();
                            assert firstTimeUserResponse != null;
                            try {

                                sharedPreferences.edit()
                                        .putString(Constant.REGISTRATION_STEP,String.valueOf(firstTimeUserResponse.getReg_steps()))
                                        .putString(Constant.USER_EMAIL,firstTimeUserResponse.getEmail())
                                        .putString(Constant.FULL_NAME,firstTimeUserResponse.getFull_name())
                                        .apply();

                                String isAlive;
                                isAlive=signupActivity.getIsLive();

                                if (Objects.equals(isAlive, "active")) {

                                    activity.finish();
                                } else {
                                    startActivity(new Intent(activity, MainActivity.class));
                                    activity.finish();
                                }

                            }catch (Exception e){
                                Toast.makeText(activity, "SharedPreferences Exception"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }



                        }else {

                            try {
                                progressBar.setVisibility(View.GONE);
                                btn_done.setVisibility(View.VISIBLE);
                                et_email.setEnabled(true);
                                et_full_name.setEnabled(true);
                                assert response.errorBody() != null;
                                DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                             snackbar=   Snackbar.make(view,response.code()+": "+detailResponse.getDetail(),Snackbar.LENGTH_SHORT);
                                        snackbar.setBackgroundTint(ContextCompat.getColor(activity,R.color.red));
                                        snackbar.show();


                            }catch (Exception e){

                                progressBar.setVisibility(View.GONE);
                                btn_done.setVisibility(View.VISIBLE);
                                et_email.setEnabled(true);
                                et_full_name.setEnabled(true);
                                try {
                                    JSONObject jsonObject = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));

                                    Toast.makeText(requireActivity(), ""+jsonObject.toString(), Toast.LENGTH_SHORT).show();
                                } catch (JSONException ec) {
                                    ec.printStackTrace();
                                }



                            }


                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<FirstTimeUserResponse> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        btn_done.setVisibility(View.VISIBLE);
                        et_email.setEnabled(true);
                        et_full_name.setEnabled(true);
                      snackbar=  Snackbar.make(view,"Something went error "+t.getMessage(),Snackbar.LENGTH_SHORT);
                                snackbar.show();


                    }
                });


            }
        });
    }


    private void initView() {
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,activity);

        et_email= firstSignupBinding.etEmail;
        et_full_name= firstSignupBinding.etFullName;
        btn_done= firstSignupBinding.btnDone;
        progressBar = firstSignupBinding.progressBar;


    }


    private void inputOperations() {
        et_full_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn_done.setEnabled(fullNameChecker() && emailChecker());
            }
        });

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn_done.setEnabled(fullNameChecker() && emailChecker());

            }
        });



    }

    private boolean emailChecker() {
        if (!TextUtils.isEmpty(et_email.getText())){

            if(Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(et_email.getText())).matches()){
                firstSignupBinding.etlEmail.setErrorEnabled(false);
                return true;

            }
            firstSignupBinding.etlEmail.setErrorEnabled(true);
            firstSignupBinding.etlEmail.setError("Not a valid email");
            return false;


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

    private boolean fullNameChecker() {

        return !TextUtils.isEmpty(et_full_name.getText());
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