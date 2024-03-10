package in.mesway.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mesway.R;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.activity.SignupActivity;
import in.mesway.databinding.FragmentDoLoginBinding;


public class DoLoginFragment extends Fragment {
    private FragmentDoLoginBinding doLoginBinding;
    private NavController navController;
    private SharedPreferences sharedPreferences;
    private Activity activity;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        doLoginBinding=FragmentDoLoginBinding.inflate(inflater,container,false);
        return doLoginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,activity);
        clickHandle();

    }

    private void clickHandle() {
        doLoginBinding.btnDoLogin.setOnClickListener(view -> {
            if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)!=null){
                try {

                    navController.popBackStack();
                }catch (Exception ignored){}
            }else {
                Intent intent= new Intent(activity, SignupActivity.class);
                intent.putExtra("isActivityLive","active");

                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences!=null) {
            if (sharedPreferences.getString(Constant.ACCESS_TOKEN, null)!=null) {
                try {
                    navController.popBackStack();
                }catch (Exception ignored){

            }
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=context instanceof Activity ?(Activity) context:null;


    }
}