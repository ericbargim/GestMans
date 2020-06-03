package com.gestmans.Interface.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.gestmans.Interface.Dialogs.LoadingDialog;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create scanner object and set it as content
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result result) {
        //When scanner detects a QR code, send the data and press back button
        LoginActivity.postQRLogin(result.getText());
        onBackPressed();
    }

    @Override
    protected void onPause() {
        //If the app is being paused, stop the camera
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        //If the app is being resumed, set the ResultHandler and start the camera
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
