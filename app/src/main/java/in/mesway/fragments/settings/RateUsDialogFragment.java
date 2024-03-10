package in.mesway.fragments.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import in.mesway.R;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.Rusable.SharedPreferencesInstance;
import in.mesway.databinding.FragmentRateusDailogBinding;
import retrofit2.Retrofit;


public class RateUsDialogFragment extends DialogFragment {

    private FragmentRateusDailogBinding rateusDailogBinding;
    private SharedPreferences sharedPreferences;
    private int rating;
    private boolean isConnected;
    private AlertDialog alertDialog;
    private int click_count;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rateusDailogBinding = FragmentRateusDailogBinding.inflate(inflater,container,false);

        Objects.requireNonNull(getDialog()).requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(true);
      /*  Retrofit retrofit = ApiClient.getClient();
        apiServices = retrofit.create(ApiServices.class);*/
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return rateusDailogBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        click_count = 0;
        alertDialog= Reusable.alertDialog(requireActivity());
        initView();

      //  sharedPreferences = SharedPreferencesInstance.getSharedPreferences(Constants.MY_GLOBAL_PREFERENCES, requireActivity());
        rateusDailogBinding.ratingBar.setRating(5);
        Snackbar no_internet = Snackbar.make(view,"You are not connected to internet",Snackbar.LENGTH_INDEFINITE);
        rating = (int) rateusDailogBinding.ratingBar.getRating();
        rateusDailogBinding.ratingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> {

            if (b) {
                if (v<1){
                    rateusDailogBinding.ratingBar.setRating(1);
                    v = (int) rateusDailogBinding.ratingBar.getRating();

                }
                click_count=0;
                rating = (int) v;

            }
        });


        rateusDailogBinding.yesBtnConfirmDialog.setOnClickListener(view1 -> {
            isConnected= Reusable.CheckInternet(requireActivity());
            if (isConnected){
                if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null) {

                    no_internet.dismiss();
                    hideKeyboard();
                    alertDialog.show();
                    if (click_count == 1) {
                        sendRatingData();

                    }
                    if (click_count == 0) {
                        if (rating < 5) {
                            click_count = 1;
                            alertDialog.dismiss();
                            rateusDailogBinding.reviewLayout.setVisibility(View.VISIBLE);


                        }
                        if (rating == 5) {
                            click_count = 1;
                            sendRatingData();
                        }
                    }
                }else {

                    Snackbar.make(view,"You have to login",Snackbar.LENGTH_INDEFINITE)
                            .show();
                }

            }else {
                no_internet.show();

            }
        });

        rateusDailogBinding.cancelBtnConfirmDialog.setOnClickListener(view12 -> {
            dismiss();
            no_internet.dismiss();
            alertDialog.dismiss();
        });

    }

    private void initView() {
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,requireActivity());

    }

    private void sendRatingData() {

    }

    private void hideKeyboard() {
        View views = requireActivity().getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rateusDailogBinding = null;
    }
}