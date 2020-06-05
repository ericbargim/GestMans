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
import android.widget.Button;
import android.widget.Toast;

import com.gestmans.Business.Exceptions.ErorRetrievingInfoException;
import com.gestmans.Business.Utilities.App;
import com.gestmans.Business.Utilities.DataClass;
import com.gestmans.Business.Utilities.HelperClass;
import com.gestmans.Business.Utilities.FetchDataPHP;
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

        // Get the context
        mContext = this;

        // Reference the elements from the XML layout
        references();

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
                HelperClass.hideKeyboard(this);
                // Get text from TextInputEditTexts
                String username = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                try {
                    // Check if username exist in DB
                    String loginRes = new FetchDataPHP().execute("username_exist", username).get();

                    // If returns error
                    if (loginRes.equals("error")) {
                        throw new ErorRetrievingInfoException("Error sending login");
                    }

                    // If username exist in DB
                    else if (Integer.parseInt(loginRes) > 0) {
                        // Username exist in DB
                        Log.d(getString(R.string.LOGIN_ACTIVITY) + "Check login", "Username exist. Checking password.");

                        // Check if password matches username in DB
                        loginRes = new FetchDataPHP().execute("username_password_matches", username, password).get();

                        // If returns error
                        if (loginRes.equals("error")) {
                            throw new ErorRetrievingInfoException("Error sending login");
                        }

                        // If password matches
                        else if (Integer.parseInt(loginRes) > 0) {
                            Log.d(getString(R.string.LOGIN_ACTIVITY) + "Check login", "Password matches.");

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
                            startActivity(new Intent(getApplicationContext(), AppMainActivity.class));
                        }

                        // If password does not match
                        else {
                            Log.d(getString(R.string.LOGIN_ACTIVITY) + "Check login", "Password does not match.");

                            // Select password TextInputEditText text
                            etPassword.requestFocus();
                            etPassword.selectAll();

                            // Show error in login TextInputLayout
                            ilPassowrd.setError("Wrong password.");

                            // Show keyboard
                            HelperClass.showKeyboard(this);
                        }
                    }

                    // If username does not exist in DB
                    else {
                        Log.d(getString(R.string.LOGIN_ACTIVITY) + "Check login", "Username does not exist.");

                        // Select login TextInputEditText text
                        etLogin.requestFocus();
                        etLogin.selectAll();

                        // Show error in password TextInputLayout
                        ilLogin.setError("Username not found.");

                        // Show keyboard
                        HelperClass.showKeyboard(this);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                } catch (ErorRetrievingInfoException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
            Log.d(App.getContext().getString(R.string.LOGIN_ACTIVITY) + "Post QR login", "QR received: " + qr + ".");

            // Check if QR exist in DB
            String qrRes = new FetchDataPHP().execute("qr_code_exist", qr).get();

            // If returns error
            if (qrRes.equals("error")) {
                throw new ErorRetrievingInfoException("Error sending QR");
            }

            // If QR exist in DB
            else if (Integer.parseInt(qrRes) > 0) {
                Log.d(App.getContext().getString(R.string.LOGIN_ACTIVITY) + "Post QR login", "QR exist.");
                DataClass.username = new FetchDataPHP().execute("get_name_lastname", qr).get();
                mContext.startActivity(new Intent(App.getContext(), AppMainActivity.class));
            }

            // If QR does not exist in DB
            else {
                Log.d(App.getContext().getString(R.string.LOGIN_ACTIVITY) + "Post QR login", "QR does not exist.");
                HelperClass.createDialogMessageNeutral("Error", "QR does not exist", "OK", mContext);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (ErorRetrievingInfoException e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void requestCameraPermission() {
        // Show dialog to request camera permission
        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.CAMERA},
                HelperClass.CAMERA_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if camera permission is granted or not
        if (requestCode == HelperClass.CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginActivity.this, getString(R.string.PERMISSION_GRANTED), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), QRScannerActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.PERMISSION_DENIED) + ".\n" + "Please, accept permission in order to log in via QR Code", Toast.LENGTH_LONG).show();
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
