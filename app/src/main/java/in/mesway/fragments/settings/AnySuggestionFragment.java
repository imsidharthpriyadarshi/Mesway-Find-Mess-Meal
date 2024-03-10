package in.mesway.fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.UpcomingMeal.UserSuggestion;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.activity.MainActivity;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentAnySuggestionBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AnySuggestionFragment extends Fragment {
    private FragmentAnySuggestionBinding anySuggestionBinding;
    private NavController navController;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;
    private Activity activity;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        anySuggestionBinding = FragmentAnySuggestionBinding.inflate(inflater,container,false);
        return anySuggestionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.mainBinding.linearBottomMenu.setVisibility(View.GONE);

        initView();
        fieldValidator();
        clickHandle(view);

    }

    private void fieldValidator(){
        anySuggestionBinding.etAnySuggestions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                anySuggestionBinding.btnSendFeedback.setEnabled(suggestionValidator());
            }
        });

    }

    private void initView() {
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,activity);
        Retrofit retrofit= ApiClient.getClient();
        apiInterface=retrofit.create(ApiInterface.class);
        alertDialog = Reusable.alertDialog(activity);

    }

    private void clickHandle(View views) {
        anySuggestionBinding.imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    navController.popBackStack();
                }catch (Exception ignored){}
            }
        });

        anySuggestionBinding.btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                sendSuggestions(views);
            }
        });
    }

    private void sendSuggestions(View views){
        alertDialog.show();
        Call<UserSuggestion> suggestionCall= apiInterface.add_user_suggestion(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null),sharedPreferences.getString(Constant.USER_ID,null),anySuggestionBinding.etAnySuggestions.getText().toString());

        suggestionCall.enqueue(new Callback<UserSuggestion>() {
            @Override
            public void onResponse(@NonNull Call<UserSuggestion> call, @NonNull Response<UserSuggestion> response) {
                Gson gson =new GsonBuilder().create();
                if (response.code()==200){
                    anySuggestionBinding.etAnySuggestions.setText("");
                    Snackbar.make(views,"Suggestion sent successfully",Snackbar.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else {
                    try {

                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        Snackbar.make(views,response.code()+": "+detailResponse.getDetail(),Snackbar.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }catch (Exception e){
                        Snackbar.make(views,response.code()+": "+ e.getMessage(),Snackbar.LENGTH_SHORT).show();
                        alertDialog.dismiss();



                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserSuggestion> call, @NonNull Throwable t) {
                Snackbar.make(views,"Something went error"+t.getMessage(),Snackbar.LENGTH_SHORT).show();
                alertDialog.dismiss();


            }
        });


    }

    private boolean suggestionValidator(){
        if (!TextUtils.isEmpty(anySuggestionBinding.etAnySuggestions.getText()) && anySuggestionBinding.etAnySuggestions.getText().length()>20){
            anySuggestionBinding.etlSuggestions.setErrorEnabled(false);
            return true;


        }
        anySuggestionBinding.etlSuggestions.setErrorEnabled(true);
        anySuggestionBinding.etlSuggestions.setError("suggestion must be of at least 20 char");

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
}