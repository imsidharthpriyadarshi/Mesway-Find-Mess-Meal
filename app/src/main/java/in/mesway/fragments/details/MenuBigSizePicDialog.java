package in.mesway.fragments.details;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import in.mesway.R;
import in.mesway.ViewModels.ImageViewModel;
import in.mesway.databinding.FragmentMenuBigSizePicDialogBinding;


public class MenuBigSizePicDialog extends DialogFragment {
    private FragmentMenuBigSizePicDialogBinding menuBigSizePicDialogBinding;
    private ImageViewModel imageViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        menuBigSizePicDialogBinding = FragmentMenuBigSizePicDialogBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).requestWindowFeature(STYLE_NO_TITLE);
        //setCancelable(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return menuBigSizePicDialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        getDataFromMainDetailFragment();
    }

    private void initView() {
        imageViewModel= new ViewModelProvider(requireActivity()).get(ImageViewModel.class);
    }

    private void getDataFromMainDetailFragment() {
//and also load image here
        assert getArguments() != null;
        if (getArguments().getString("type").equals("Menu")) {
            menuBigSizePicDialogBinding.tMenuType.setText(getArguments().getString("type"));
            menuBigSizePicDialogBinding.consBigMenu.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_breakfast_time));
            imageViewModel.getBreakfastImg().observe(requireActivity(), new Observer<Bitmap>() {
                @Override
                public void onChanged(Bitmap bitmap) {
                    if (bitmap!=null){
                        menuBigSizePicDialogBinding.imgBigMessMenu.setImageBitmap(bitmap);
                        menuBigSizePicDialogBinding.progressBar.setVisibility(View.GONE);

                    }else {
                        menuBigSizePicDialogBinding.imgBigMessMenu.setVisibility(View.GONE);
                        menuBigSizePicDialogBinding.progressBar.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else if (getArguments().getString("type").equals("Lunch")) {
            menuBigSizePicDialogBinding.tMenuType.setText(getArguments().getString("type"));
            menuBigSizePicDialogBinding.consBigMenu.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_lunch_time));
            imageViewModel.getLunchImg().observe(requireActivity(), bitmap -> {
                if (bitmap != null) {
                    menuBigSizePicDialogBinding.imgBigMessMenu.setImageBitmap(bitmap);
                    menuBigSizePicDialogBinding.progressBar.setVisibility(View.GONE);

                }else {
                    menuBigSizePicDialogBinding.imgBigMessMenu.setVisibility(View.GONE);

                    menuBigSizePicDialogBinding.progressBar.setVisibility(View.VISIBLE);
                }
            });
        } else {
            menuBigSizePicDialogBinding.tMenuType.setText(getArguments().getString("type"));
            menuBigSizePicDialogBinding.consBigMenu.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_dinner_time));
            imageViewModel.getDinnerImg().observe(requireActivity(), bitmap -> {
                if (bitmap!=null){
                    menuBigSizePicDialogBinding.imgBigMessMenu.setImageBitmap(bitmap);
                    menuBigSizePicDialogBinding.progressBar.setVisibility(View.GONE);

                }else {
                    menuBigSizePicDialogBinding.imgBigMessMenu.setVisibility(View.GONE);

                    menuBigSizePicDialogBinding.progressBar.setVisibility(View.VISIBLE);
                }
            });
        }



    }
}