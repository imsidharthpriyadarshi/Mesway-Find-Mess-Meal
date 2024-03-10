package in.mesway.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import in.mesway.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding signupBinding;
    public String isLive="none";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(signupBinding.getRoot());
        Intent intent = getIntent();

            if (intent!=null) {
                if (intent.getStringExtra("isActivityLive")!=null) {
                    if (intent.getStringExtra("isActivityLive").equals("active")) {
                        isLive="active";

                    }
                }
            }
    }
    public String getIsLive(){
        return isLive;
    }
}