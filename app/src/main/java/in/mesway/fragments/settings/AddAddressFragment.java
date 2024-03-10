package in.mesway.fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import in.mesway.R;
import in.mesway.Response.DetailResponse;
import in.mesway.Response.UpcomingMeal.UserAddress;
import in.mesway.Response.ZipcodeResponse;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.client.PinCodeApiClient;
import in.mesway.databinding.FragmentAddAddressBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AddAddressFragment extends Fragment {
    private FragmentAddAddressBinding addAddressBinding;
    private NavController navController;

    private boolean is_otp_validate;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;
    private String location_type = "home";
    private AlertDialog alertDialog;
    private Activity activity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addAddressBinding = FragmentAddAddressBinding.inflate(inflater, container, false);
        return addAddressBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        getAllValidation();
        clickHandle(view);


    }


    private void getAllValidation() {
        addAddressBinding.etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nameValidator();
                addAddressBinding.btnAddAddress.setEnabled(nameValidator() && isValidNo() && addressValidator() && pinCodeValidator() && districtValidator() && stateValidator());
            }
        });

        addAddressBinding.etMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isValidNo();
                addAddressBinding.btnAddAddress.setEnabled(nameValidator() && isValidNo() && addressValidator() && pinCodeValidator() && districtValidator() && stateValidator());
            }
        });


        addAddressBinding.etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addressValidator();
                addAddressBinding.btnAddAddress.setEnabled(nameValidator() && isValidNo() && addressValidator() && pinCodeValidator() && districtValidator() && stateValidator());
            }
        });

        addAddressBinding.etZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pinCodeValidator();
                addAddressBinding.btnAddAddress.setEnabled(nameValidator() && isValidNo() && addressValidator() && pinCodeValidator() && districtValidator() && stateValidator());
            }
        });

        addAddressBinding.secondDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                districtValidator();
                addAddressBinding.btnAddAddress.setEnabled(nameValidator() && isValidNo() && addressValidator() && pinCodeValidator() && districtValidator() && stateValidator());
            }
        });

        addAddressBinding.secondState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stateValidator();
                addAddressBinding.btnAddAddress.setEnabled(nameValidator() && isValidNo() && addressValidator() && pinCodeValidator() && districtValidator() && stateValidator());
            }
        });

    }


    private void clickHandle(View views) {
        addAddressBinding.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCodeValidator(views);

            }
        });

    }

    private void addAddress(View view) {
        String full_name = Objects.requireNonNull(addAddressBinding.etFullName.getText()).toString();
        String fFull_name = full_name.substring(0, 1).toUpperCase() + full_name.substring(1);


        String mobile_no = Objects.requireNonNull(addAddressBinding.etMobileNo.getText()).toString();

        String full_address = Objects.requireNonNull(addAddressBinding.etAddress.getText()).toString();
        String fFull_address = full_address.substring(0, 1).toUpperCase() + full_address.substring(1);

        String landmark;
        if (Objects.requireNonNull(addAddressBinding.secondLandmark.getText()).toString().trim().isEmpty()) {
            landmark = "none";
        } else {
            landmark = Objects.requireNonNull(addAddressBinding.secondLandmark.getText()).toString();

        }
        String fLandmark = landmark.substring(0, 1).toUpperCase() + landmark.substring(1);


        String house_no;
        if (Objects.requireNonNull(addAddressBinding.secondBuildingNo.getText()).toString().trim().isEmpty()) {
            house_no = "none";
        } else {

            house_no = Objects.requireNonNull(addAddressBinding.secondBuildingNo.getText()).toString();
        }

        String zip_code = Objects.requireNonNull(addAddressBinding.etZipCode.getText()).toString();

        String district = Objects.requireNonNull(addAddressBinding.secondDistrict.getText()).toString();
        String fDistrict = district.substring(0, 1).toUpperCase() + district.substring(1);

        String state = Objects.requireNonNull(addAddressBinding.secondState.getText()).toString();
        String fState = state.substring(0, 1).toUpperCase() + state.substring(1);

        if (!addAddressBinding.homeRadioBtn.isChecked()) {
            location_type = "work";
        }
        Call<UserAddress> addressCall = apiInterface.add_user_address(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), sharedPreferences.getString(Constant.USER_ID, null), fFull_name, mobile_no, location_type, house_no, fFull_address, fLandmark, fState, "none", "none", fDistrict, zip_code);
        addressCall.enqueue(new Callback<UserAddress>() {
            @Override
            public void onResponse(@NonNull Call<UserAddress> call, @NonNull Response<UserAddress> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    Snackbar.make(view, "Address added", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green))
                            .show();

                    try {

                        navController.popBackStack();
                    }catch (Exception ignored){}
                    alertDialog.dismiss();

                } else {
                    try {
                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        Snackbar.make(view, response.code() + ": " + detailResponse.getDetail(), Snackbar.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    } catch (Exception e) {
                        Snackbar.make(view, response.code() + ": " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }


                }

            }

            @Override
            public void onFailure(@NonNull Call<UserAddress> call, @NonNull Throwable t) {
                Snackbar.make(view, "Something went error", Snackbar.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

    private void initView(View view) {
        navController = Navigation.findNavController(view);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        alertDialog = Reusable.alertDialog(activity);
    }


    private void pinCodeValidator(View views) {
        ApiInterface pinApiServices;
        if (!TextUtils.isEmpty(addAddressBinding.etZipCode.getText())) {
            if (addAddressBinding.etZipCode.length() == 6) {
                alertDialog.show();

                Retrofit pinRetrofit = PinCodeApiClient.getClient();
                pinApiServices = pinRetrofit.create(ApiInterface.class);
                Call<ZipcodeResponse[]> zipcodeResponseCall = pinApiServices.get_zip_code_data(addAddressBinding.etZipCode.getText().toString());
                zipcodeResponseCall.enqueue(new Callback<ZipcodeResponse[]>() {
                    @Override
                    public void onResponse(@NonNull Call<ZipcodeResponse[]> call, @NonNull Response<ZipcodeResponse[]> response) {
                        ZipcodeResponse[] zipcodeResponse = response.body();

                        if (response.code() == 200) {
                            assert zipcodeResponse != null;
                            if (zipcodeResponse[0].getStatus().equalsIgnoreCase("success")){


                                if (zipcodeResponse[0].getPostOfficeList() != null) {
                               /*     for (int i = 0; i < zipcodeResponse[0].getPostOfficeList().size(); i++) {
                                        if (zipcodeResponse[0].getPostOfficeList().get(i).getState().toLowerCase().trim().equals(Objects.requireNonNull(addAddressBinding.secondState.getText()).toString().toLowerCase().toLowerCase())) {
                                            if (zipcodeResponse[0].getPostOfficeList().get(i).getDistrict().toLowerCase().trim().equals(Objects.requireNonNull(addAddressBinding.secondDistrict.getText()).toString().toLowerCase().toLowerCase())) {
                                                is_otp_validate = true;
                                                break;


                                          }
                                        }
                                    }*/

                                    addAddress(views);
                                    /*if (!is_otp_validate) {
                                        Snackbar.make(views, "Plz enter valid address", Snackbar.LENGTH_SHORT)
                                                .setBackgroundTint(ContextCompat.getColor(activity, R.color.red))
                                                .show();
                                        alertDialog.dismiss();

                                    }else{

                                    }*/
                                } else {
                                    Snackbar.make(views, "Plz enter valid address", Snackbar.LENGTH_SHORT)
                                            .setBackgroundTint(ContextCompat.getColor(activity, R.color.red))
                                            .show();
                                    is_otp_validate = false;
                                    alertDialog.dismiss();

                                }
                            }else {
                                Snackbar.make(views, "Enter a valid address", Snackbar.LENGTH_SHORT)
                                        .setBackgroundTint(ContextCompat.getColor(activity, R.color.red))
                                        .show();
                                alertDialog.dismiss();
                                is_otp_validate = false;

                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ZipcodeResponse[]> call, @NonNull Throwable t) {
                        Snackbar.make(views, "Something went wrong/ Slow network connection", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(activity, R.color.red))
                                .show();
                        alertDialog.dismiss();
                        is_otp_validate = false;

                    }
                });


            }


        }

    }

    private boolean stateValidator() {
        if (!TextUtils.isEmpty(addAddressBinding.secondState.getText())) {

            addAddressBinding.layoutSecondState.setErrorEnabled(false);
            return true;


        }
        addAddressBinding.layoutSecondState.setErrorEnabled(true);
        addAddressBinding.layoutSecondState.setError("State Required");
        return false;

    }

    private boolean districtValidator() {
        if (!TextUtils.isEmpty(addAddressBinding.secondDistrict.getText())) {

            addAddressBinding.layoutSecondDistrict.setErrorEnabled(false);
            return true;


        }
        addAddressBinding.layoutSecondDistrict.setErrorEnabled(true);
        addAddressBinding.layoutSecondDistrict.setError("District required");
        return false;


    }

    private boolean nameValidator() {
        if (!TextUtils.isEmpty(addAddressBinding.etFullName.getText())) {

            addAddressBinding.etlFullName.setErrorEnabled(false);
            return true;


        }
        addAddressBinding.etlFullName.setErrorEnabled(true);
        addAddressBinding.etlFullName.setError("Name Required");
        return false;


    }


    private boolean addressValidator() {
        if (!TextUtils.isEmpty(addAddressBinding.etAddress.getText())) {

            addAddressBinding.etlAddress.setErrorEnabled(false);
            return true;


        }
        addAddressBinding.etlAddress.setErrorEnabled(true);
        addAddressBinding.etlAddress.setError("Address required *");
        return false;

    }

    private boolean isValidNo() {
        if (!TextUtils.isEmpty(addAddressBinding.etMobileNo.getText()) && addAddressBinding.etMobileNo.getText().length() == 10) {

            try {
                Long.parseLong(Objects.requireNonNull(addAddressBinding.etMobileNo.getText()).toString());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }


    }

    private boolean pinCodeValidator() {
        if (!TextUtils.isEmpty(addAddressBinding.etZipCode.getText()) && addAddressBinding.etZipCode.length() == 6) {
            try {
                addAddressBinding.etlZipCode.setErrorEnabled(false);
                Long.parseLong(Objects.requireNonNull(addAddressBinding.etZipCode.getText()).toString());
                return true;
            } catch (Exception e) {
                addAddressBinding.etlZipCode.setErrorEnabled(true);
                addAddressBinding.etlZipCode.setError("Not a valid pin");
                return false;


            }

        } else {

            addAddressBinding.etlZipCode.setErrorEnabled(true);
            addAddressBinding.etlZipCode.setError("Not a valid pin");
            return false;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=context instanceof Activity ?(Activity) context:null;


    }

}