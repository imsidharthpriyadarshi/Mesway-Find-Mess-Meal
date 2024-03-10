package in.mesway.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.mesway.Adapters.FoodSearchAdapter;
import in.mesway.Models.FoodSearchModel;
import in.mesway.R;
import in.mesway.Response.Location.ListLocation;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.LocationViewModel;
import in.mesway.activity.MainActivity;
import in.mesway.activity.MessDetailActivity;
import in.mesway.activity.SignupActivity;
import in.mesway.databinding.FragmentFoodSearchBinding;


public class MessSearchFragment extends Fragment {

    private FragmentFoodSearchBinding foodSearchBinding;
    private MessCardClickInterface mealCardClickInterface;
    private FoodSearchAdapter foodSearchAdapter;
    private NavController navController;
    private LocationViewModel locationViewModel;
    private boolean isConnected;
    private Activity activity;
    private ListLocation mess_list_location;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        foodSearchBinding = FragmentFoodSearchBinding.inflate(inflater, container, false);
        return foodSearchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        navController = Navigation.findNavController(view);
        assert mainActivity != null;
        mainActivity.mainBinding.menuBottom.setVisibility(View.GONE);
        initView();
        getFocusOnLocationEt();
        filterFood();
        clickHandle();
        if (isConnected) {
            statusCodeObserver(view);
            isLoading();
        }

    }

    private void isLoading() {
        Observer<Boolean> is_loading = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                 //   alertDialog.show();
                } else {
 /*                   homeBinding.containerShimmer.stopShimmer();
                    homeBinding.containerShimmer.setVisibility(View.GONE);*/
                  //  alertDialog.dismiss();
                }
            }
        };

        locationViewModel.isLoading.observe((LifecycleOwner) activity, is_loading);


    }

    private void statusCodeObserver(View view) {
        Observer<Integer> status_code_observer = integer -> {
            if (integer == 200) {
                nearestMessList();


            } /*else if (integer == 500) {
             snackbar=   Snackbar.make(view,"Slow internet connection/Something went error",Snackbar.LENGTH_SHORT);
             snackbar.show();

            } */else {
                detailObserver(view);


            }


        };

        locationViewModel.status_code.observe((LifecycleOwner) activity, status_code_observer);

    }

    private void detailObserver(View view) {
        Observer<String> detail_observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {


            }
        };


        locationViewModel.detail.observe((LifecycleOwner) activity, detail_observer);


    }

    private void nearestMessList() {
        Observer<ListLocation> listLocationObserver = new Observer<ListLocation>() {
            @Override
            public void onChanged(ListLocation listLocation) {
                mess_list_location = listLocation;
                loadFoodSearchItem();


            }
        };
        locationViewModel.listLocationMutableLiveData.observe((LifecycleOwner) activity, listLocationObserver);
    }


    private void initView() {
     //   foodModelList = new ArrayList<>();
        alertDialog = Reusable.alertDialog(activity);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        locationViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(LocationViewModel.class);
        isConnected = Reusable.CheckInternet(activity);

    }

    private void clickHandle() {
        foodSearchBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {

                navController.popBackStack();
            }catch (Exception ignored){}

            }
        });
    }

    private void getFocusOnLocationEt() {
        foodSearchBinding.etFoodQuery.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(foodSearchBinding.etFoodQuery, InputMethodManager.SHOW_IMPLICIT);

    }

    private void filterFood() {
        foodSearchBinding.etFoodQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(foodModelList().size()==0)) {
                    foodSearchAdapter.getFilter().filter(foodSearchBinding.etFoodQuery.getText());
                }else {
                    Toast.makeText(activity, "No mess Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadFoodSearchItem() {
        RecyclerView recyclerView = foodSearchBinding.recFoodSearch;
        mealCardClickInterface = new MessCardClickInterface() {
            @Override
            public void mealCardClick(String img_meal_address,int position, ImageView imageView, TextView mess_name, TextView mess_for, TextView mess_address, TextView mess_rating, LinearLayout linearLayout_rating, ImageView rating_star, TextView how_many_rating, String meal_items, String rating, String s_mess_address, String s_mess_for, String s_how_many_rated,String mess_id) {

            }

            @Override
            public void FoodCardClick(String img_meal_address,int position, String meal_items, String rating,String mess_id) {
                hideKeyboard();
                sharedPreferences.edit().putString(Constant.MESS_ID,mess_id).apply();
                Bundle bundle = new Bundle();
                bundle.putString("mess_id",mess_id);
                bundle.putInt("position", position);
                bundle.putString("mess_name", meal_items);
                bundle.putString("img_meal_address",img_meal_address);
                bundle.putString("mess_rating", rating);
                for (int i=0;i<mess_list_location.getLocation().size();i++){
                    if (Objects.equals(mess_list_location.getLocation().get(i).getMessId(), mess_id)){
                        bundle.putString("how_many_rated","("+mess_list_location.getLocation().get(i).getMessInfo().getHow_many_rated()+")" );
                        bundle.putString("mess_for", "Breakfast, Lunch, Dinner");
                        bundle.putString("mess_address", mess_list_location.getLocation().get(i).getCompanyAddress());

                    }

                }
                Intent intent = new Intent(activity, MessDetailActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);






            }
        };


        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        foodSearchAdapter = new FoodSearchAdapter(foodModelList(), activity, mealCardClickInterface);
        recyclerView.setAdapter(foodSearchAdapter);

        //this will fix some inappopriate behaviour
        recyclerView.setNestedScrollingEnabled(false);

    }

    private List<FoodSearchModel> foodModelList() {
        List<FoodSearchModel> foodModelList=new ArrayList<>();
        if (mess_list_location != null) {



            for (int i = 0; i < mess_list_location.getLocation().size(); i++) {
                float[] distance = new float[1];
                Location.distanceBetween(Double.parseDouble(sharedPreferences.getString(Constant.LATITUDE, null)), Double.parseDouble(sharedPreferences.getString(Constant.LONGITUDE, null)), Double.parseDouble(mess_list_location.getLocation().get(i).getLatitude()), Double.parseDouble(mess_list_location.getLocation().get(i).getLangitude()), distance);
                float distanceInKm = distance[0] / 1000;
               // Toast.makeText(activity, mess_list_location.getLocation().get(i).getMessInfo().getMessName()+""+distanceInKm, Toast.LENGTH_SHORT).show();


                if (distanceInKm <= 2) {

                    foodModelList.add(new FoodSearchModel(mess_list_location.getLocation().get(i).getMessInfo().getMessImages().get(0).getBigImage().get(0), mess_list_location.getLocation().get(i).getMessInfo().getMessName(), mess_list_location.getLocation().get(i).getCompanyAddress(), String.valueOf(mess_list_location.getLocation().get(i).getMessInfo().getMessRating()),mess_list_location.getLocation().get(i).getMessId()));
                }

            }

        }

       /* foodModelList.add(new FoodSearchModel("https://images.pexels.com/photos/1026679/pexels-photo-1026679.jpeg?cs=srgb&dl=curry-delicious-food-delicious-indian-food-indian-cuisine-1026679.jpg&fm=jpg","Parth food centre","4.3", "near to gopal nagar pachim champaran bihar"));
        foodModelList.add(new FoodSearchModel("https://th.bing.com/th/id/OIP.wATNbchyYhW_3tvqVHUDGAHaEd?pid=ImgDet&rs=1","Maa Food centre","4.3","near to gopal nagar pachim champaran bihar"));
        foodModelList.add(new FoodSearchModel("https://thali-owner-data.s3.ap-south-1.amazonaws.com/extra/20010bd7-05dc-4b4f-aac5-2e654d4f05dcpassbook.webp","Sidharth mess","4.3","near to gopal nagar pachim champaran bihar"));
        foodModelList.add(new FoodSearchModel("https://th.bing.com/th/id/OIP.wATNbchyYhW_3tvqVHUDGAHaEd?pid=ImgDet&rs=1","Pankaj food centre","4.3","near to gopal nagar pachim champaran bihar"));
        foodModelList.add(new FoodSearchModel("https://images.pexels.com/photos/958546/pexels-photo-958546.jpeg?cs=srgb&dl=food-healthy-vegetables-958546.jpg&fm=jpg","Rupankar messs","4.3","near to gopal nagar pachim champaran bihar"));
        foodModelList.add(new FoodSearchModel("https://thali-owner-data.s3.ap-south-1.amazonaws.com/extra/20010bd7-05dc-4b4f-aac5-2e654d4f05dcpassbook.webp","Uma mess centre ","4.3","near to gopal nagar pachim champaran bihar"));
        foodModelList.add(new FoodSearchModel("https://thali-owner-data.s3.ap-south-1.amazonaws.com/extra/20010bd7-05dc-4b4f-aac5-2e654d4f05dcpassbook.webp","Komal food","4.3","near to gopal nagar pachim champaran bihar"));
*/

        return foodModelList;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;


    }

    private void hideKeyboard() {
        View views = activity.getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }


}