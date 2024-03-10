package in.mesway.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.mesway.Adapters.NotificationAdapter;
import in.mesway.Models.NotificationsModel;
import in.mesway.R;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.activity.MainActivity;
import in.mesway.databinding.FragmentNotificationBinding;


public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding notificationBinding;
    private SharedPreferences sharedPreferences;
    private NavController navController;
    private Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationBinding= FragmentNotificationBinding.inflate(inflater,container,false);
        recNotification();
        return notificationBinding.getRoot();
    }


    private void recNotification() {
        RecyclerView recyclerView = notificationBinding.recNotification;
        NotificationAdapter notificationAdapter = new NotificationAdapter(getNotificationList());
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private List<NotificationsModel> getNotificationList() {
        List<NotificationsModel> notificationsModelList= new ArrayList<>();
        notificationsModelList.add(new NotificationsModel("Welcome to Mealko","12-01-2023"));
        notificationsModelList.add(new NotificationsModel("Welcome to Mealko","12-01-2023"));
        notificationsModelList.add(new NotificationsModel("Welcome to Mealko","12-01-2023"));

        return notificationsModelList;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity= (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.mainBinding.menuBottom.setVisibility(View.GONE);
        navController= Navigation.findNavController(view);
        initView();
        if (sharedPreferences.getString(Constant.ACCESS_TOKEN,null)==null){
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.notificationFragment,true)
                    .build();
            try {

                navController.navigate(NotificationFragmentDirections.actionGlobalDoLoginFragment(),navOptions);
            }catch (Exception ignored){}

        }  else {
            //

        }


    }

    private void initView() {
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,activity);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=context instanceof Activity ?(Activity) context:null;


    }
}