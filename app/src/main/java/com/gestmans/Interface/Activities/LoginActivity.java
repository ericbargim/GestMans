package com.gestmans.Interface.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gestmans.Business.App;
import com.gestmans.Business.DataClass;
import com.gestmans.Business.HelperClass;
import com.gestmans.Business.Permissions;
import com.gestmans.Business.FetchDataPHP;
import com.gestmans.Interface.Dialogs.LoadingDialog;
import com.gestmans.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private static Context mContext;
    private TextInputLayout ilLogin, ilPassowrd;
    private TextInputEditText etLogin, etPassword;
    private Button btnLogin, btnLoginQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Get the context
        mContext = this;

        // Reference the elements from the XML layout
        references();

        // Call the constructor of the loading screen
        LoadingDialog ld = new LoadingDialog((Activity) mContext);

        // This is needed to apply the font of the app to the password field
        Typeface cache = etPassword.getTypeface();
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.setTypeface(cache);

        // When user clicks Login button
        btnLogin.setOnClickListener(v -> {
            // Clear error messages from the TextInputLayouts
            ilLogin.setError("");
            ilPassowrd.setError("");

            // Check if Login EditText is empty
            if (etLogin.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.LOGIN_ERROR_USERNAME), Toast.LENGTH_SHORT).show();
            }

            // Check if Password EditText is empty
            else if (etPassword.getText().toString().equals("")) {

                Toast.makeText(getApplicationContext(), getString(R.string.LOGIN_ERROR_PASSWORD), Toast.LENGTH_SHORT).show();
            }

            //All EditText fields are not empty
            else {
                // Hide keyboard
                HelperClass.hideKeyboard((Activity) mContext);

                // Start loading screen
                ld.startLoadingDialog();

                new Handler().post(() -> {
                    // Get text from TextInputEditTexts
                    String username = etLogin.getText().toString();
                    String password = etPassword.getText().toString();
                    try {
                        // Check if username exist in DB
                        if (Integer.parseInt(new FetchDataPHP().execute("username_exist", username).get()) > 0) {
                            // Username exist in DB
                            Log.d(getString(R.string.LOGIN_ACTIVITY), "Username exist. Checking password.");

                            // Check if password matches username in DB
                            if (Integer.parseInt(new FetchDataPHP().execute("username_password_matches", username, password).get()) > 0) {
                                // Password matches
                                Log.d(getString(R.string.LOGIN_ACTIVITY), "Password matches.");

                                // Save name and lastname
                                String name = new FetchDataPHP().execute("get_name_lastname", username).get();
                                if (!name.equals("error")) {
                                    DataClass.username = name;
                                } else {
                                    DataClass.username = getString(R.string.HOME_WELCOME_HOLDER_USER);
                                }

                                // Clear both TextInputEditTexts
                                etLogin.setText("");
                                etPassword.setText("");

                                // Close loading screen
                                ld.dismissDialog();
                                startActivity(new Intent(getApplicationContext(), AppMainActivity.class));
                            } else {
                                // Password does not match
                                Log.d(getString(R.string.LOGIN_ACTIVITY), "Password does not match.");

                                // Close loading screen
                                ld.dismissDialog();

                                // Select password TextInputEditText text
                                etPassword.requestFocus();
                                etPassword.selectAll();

                                // Show error in login TextInputLayout
                                ilPassowrd.setError("Wrong password.");

                                // Show keyboard
                                HelperClass.showKeyboard((Activity) mContext);
                            }
                        } else {
                            // Username does not exist in DB
                            Log.d(getString(R.string.LOGIN_ACTIVITY), "Username does not exist.");

                            // Close loading screen
                            ld.dismissDialog();

                            // Select login TextInputEditText text
                            etLogin.requestFocus();
                            etLogin.selectAll();

                            // Show error in password TextInputLayout
                            ilLogin.setError("Username not found.");

                            // Show keyboard
                            HelperClass.showKeyboard((Activity) mContext);
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();

                        // Close loading screen
                        ld.dismissDialog();
                    }
                });
            }
        });
        // When user clicks Login with QR button
        btnLoginQR.setOnClickListener(v -> {
            // If camera permission is already granted, open QR Scanner
            if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), QRScannerActivity.class));
            }

            // If not, request permission
            else {
                requestCameraPermission();
            }
        });
    }

    public static void postQRLogin(String qr) {
        // Method called from QRScannerActivity after detecting QR
        try {
            Log.d(App.getContext().getString(R.string.LOGIN_ACTIVITY), "QR received: " + qr + ".");

            // Check if QR exist in DB
            if (Integer.parseInt(new FetchDataPHP().execute("qr_code_exist", qr).get()) > 0) {
                Log.d(App.getContext().getString(R.string.LOGIN_ACTIVITY), "QR exist.");
                DataClass.username = new FetchDataPHP().execute("get_name_lastname", qr).get();
                mContext.startActivity(new Intent(mContext, AppMainActivity.class));

                // QR does not exist in DB
            } else {
                Log.d(App.getContext().getString(R.string.LOGIN_ACTIVITY), "QR does not exist.");
                HelperClass.createDialogMessageSingle("Error", "QR does not exist", "OK", mContext);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void requestCameraPermission() {
        // Show dialog to request camera permission
        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.CAMERA},
                Permissions.CAMERA_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if camera permission is granted or not
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
        // Reference all the XML elements
        ilLogin = findViewById(R.id.ilLogin);
        etLogin = findViewById(R.id.etLogin);
        ilPassowrd = findViewById(R.id.ilPassword);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginQR = findViewById(R.id.btnLoginQR);
    }
}
