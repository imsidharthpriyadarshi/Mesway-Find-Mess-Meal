package in.mesway.fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import in.mesway.R;
import in.mesway.Response.ExtraInfoResponse;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.ExtraInfoViewModel;
import in.mesway.activity.MainActivity;
import in.mesway.databinding.FragmentTermConditionBinding;

public class TermConditionFragment extends Fragment {
    private FragmentTermConditionBinding termConditionBinding;
    private NavController navController;
    private AlertDialog alertDialog;

    private Activity activity;
    private ExtraInfoViewModel extraInfoViewModel;
    private ExtraInfoResponse extra_info_response;
    private boolean isConnected;
    private Snackbar snackbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        termConditionBinding = FragmentTermConditionBinding.inflate(inflater, container, false);
        return termConditionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.mainBinding.linearBottomMenu.setVisibility(View.GONE);

        initView();
        if (isConnected) {
            statusCodeObserver(view);
            isLoading();
        }else {
           snackbar=Snackbar.make(view,"No internet connection",Snackbar.LENGTH_SHORT);
                   snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                   snackbar.show();

        }
    }

    private void initView() {
        isConnected= Reusable.CheckInternet(activity);
        alertDialog = Reusable.alertDialog(activity);
        extraInfoViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ExtraInfoViewModel.class);

    }

    private void isLoading() {
        Observer<Boolean> is_loading = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    alertDialog.show();

                }
            }
        };

        extraInfoViewModel.isLoading.observe((LifecycleOwner) activity, is_loading);


    }

    private void statusCodeObserver(View view) {
        Observer<Integer> status_code_observer = integer -> {
            if (integer == 200) {
                getExtraInfoDetail();



            } else {
                detailObserver(view);


            }


        };

        extraInfoViewModel.status_code.observe((LifecycleOwner) activity, status_code_observer);

    }

    private void detailObserver(View view) {
        Observer<String> detail_observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {

              snackbar=  Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
              snackbar.show();
            }
        };


        extraInfoViewModel.detail.observe((LifecycleOwner) activity, detail_observer);


    }

    private void getExtraInfoDetail() {
        Observer<ExtraInfoResponse> extraInfoResponseObserver = new Observer<ExtraInfoResponse>() {
            @Override
            public void onChanged(ExtraInfoResponse extraInfoResponse) {
                extra_info_response = extraInfoResponse;
                termConditionOperation();
                clickHandel();
            }
        };

        extraInfoViewModel.extraInfoResponseMutableLiveData.observe((LifecycleOwner) activity, extraInfoResponseObserver);

    }

    private void termConditionOperation() {
        termConditionBinding.wvTermCondition.loadUrl(extra_info_response.getTerm_condition());
        termConditionBinding.wvTermCondition.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                alertDialog.dismiss();
            }
        });
    }


    private void clickHandel() {
        termConditionBinding.imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    navController.popBackStack();
                }catch (Exception ignored){}
            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;


    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar!=null){
            snackbar.dismiss();

        }
    }
}