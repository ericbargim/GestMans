package com.gestmans.Interface.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.gestmans.Interface.Fragments.HomeFragment;
import com.gestmans.R;

public class AppMainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentViewLayout, new HomeFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, getString(R.string.APP_MAIN_BACK_AGAIN), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
    }
}
