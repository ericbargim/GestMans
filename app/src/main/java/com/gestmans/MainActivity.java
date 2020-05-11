package com.gestmans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText etLogin, etPassword;
    Button btnLogin, btnLoginQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    private void references() {
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginQR = findViewById(R.id.btnLoginQR);
    }
}
