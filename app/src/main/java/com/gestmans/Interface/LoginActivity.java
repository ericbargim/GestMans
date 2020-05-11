package com.gestmans.Interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gestmans.Business.DataClass;
import com.gestmans.Business.Permissions;
import com.gestmans.R;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etLogin, etPassword;
    Button btnLogin, btnLoginQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        references();

        //When user clicks Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if Login EditText is empty
                if (etLogin.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please, enter a valid username!", Toast.LENGTH_SHORT).show();
                }
                //Check if Password EditText is empty
                else if (etPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please, enter a valid password!", Toast.LENGTH_SHORT).show();
                }
                //All EditText fields are not empty
                else {
                    String username = etLogin.getText().toString();
                    String password = etPassword.getText().toString();
                }
            }
        });
        //When user clicks Login with QR button
        btnLoginQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If camera permission is already granted, open QR Scanner
                if (ContextCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getApplicationContext(), QRScannerActivity.class));
                }
                //If not, request permission
                else {
                    requestCameraPermission();
                }
            }
        });
    }

    private void requestCameraPermission() {
        //Show dialog to request camera permission
        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.CAMERA},
                Permissions.CAMERA_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Check if camera permission is granted or not
        if (requestCode == Permissions.CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), QRScannerActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void references() {
        //Reference all the XML elements
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginQR = findViewById(R.id.btnLoginQR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LOGIN-ACTIVITY", "Resuming Login");
        try {
            if (DataClass.password != null) {
                Log.d("LOGIN-ACTIVITY", "Checking password...");
                //Attempt login

                DataClass.password = null;
            }
        } catch (NullPointerException ex) {

        }
    }
}
