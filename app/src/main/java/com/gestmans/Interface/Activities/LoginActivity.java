package com.gestmans.Interface.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gestmans.Business.DataClass;
import com.gestmans.Business.Permissions;
import com.gestmans.Business.fetchDataPHP;
import com.gestmans.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout ilLogin, ilPassowrd;
    private TextInputEditText etLogin, etPassword;
    private Button btnLogin, btnLoginQR;

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        references();
        //This is needed to apply the font of the app to the password field
        Typeface cache = etPassword.getTypeface();
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.setTypeface(cache);

        //When user clicks Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if Login EditText is empty
                if (etLogin.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.LOGIN_ERROR_USERNAME), Toast.LENGTH_SHORT).show();
                }
                //Check if Password EditText is empty
                else if (etPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.LOGIN_ERROR_PASSWORD), Toast.LENGTH_SHORT).show();
                }
                //All EditText fields are not empty
                else {
                    String username = etLogin.getText().toString();
                    String password = etPassword.getText().toString();

                    try {
                        if (Integer.parseInt(new fetchDataPHP().execute("username_exist", username).get()) > 0) {
                            Log.d(getString(R.string.LOGIN_ACTIVITY), "Username exist. Checking password.");
                            if (Integer.parseInt(new fetchDataPHP().execute("username_password_matches", username, password).get()) > 0) {
                                Log.d(getString(R.string.LOGIN_ACTIVITY), "Password matches.");
                                DataClass.username = new fetchDataPHP().execute("get_name_lastname", username).get();
                                startActivity(new Intent(getApplicationContext(), AppMainActivity.class));
                            } else {
                                Log.d(getString(R.string.LOGIN_ACTIVITY), "Password does not match.");
                            }
                        } else {
                            Log.d(getString(R.string.LOGIN_ACTIVITY), "Username does not exist.");
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
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

    public static void postQRLogin(String qr) {
        try {
            Log.d(/*Resources.getSystem().getString(R.string.LOGIN_ACTIVITY)*/"LOGIN", "QR received: " + qr + ".");
            int i = Integer.parseInt(new fetchDataPHP().execute("qr_code_exist", qr).get());
            System.out.println(i);
            if (Integer.parseInt(new fetchDataPHP().execute("qr_code_exist", qr).get()) > 0) {
                Log.d(/*Resources.getSystem().getString(R.string.LOGIN_ACTIVITY)*/"LOGIN", "QR exist.");
                DataClass.username = new fetchDataPHP().execute("get_name_lastname", qr).get();
                mContext.startActivity(new Intent(mContext, AppMainActivity.class));
            } else {
                Log.d(/*Resources.getSystem().getString(R.string.LOGIN_ACTIVITY)*/"LOGIN", "QR does not exist.");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                Toast.makeText(LoginActivity.this, getString(R.string.PERMISSION_GRANTED), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), QRScannerActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.PERMISSION_DENIED), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void references() {
        //Reference all the XML elements
        ilLogin = findViewById(R.id.ilLogin);
        etLogin = findViewById(R.id.etLogin);
        ilPassowrd = findViewById(R.id.ilPassword);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginQR = findViewById(R.id.btnLoginQR);
    }
}
