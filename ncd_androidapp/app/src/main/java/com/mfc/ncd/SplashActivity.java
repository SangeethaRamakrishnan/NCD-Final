package com.mfc.ncd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       /* progressBar=findViewById(R.id.main_progress);
        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);*/

        PackageInfo packageInfo = new PackageInfo();
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        AppVersionModel appVersionModel = new AppVersionModel();
        appVersionModel.setApp_version(String.valueOf(versionCode));
        AppVersionService appVersionService = new AppVersionService();
        appVersionService.pushData(appVersionModel, new HttpCallResponse() {
            @Override
            public void OnSuccess(Object obj) {
                //   Log.d("SuccessMsg", "successfully sent");
                Response<AppVersionResponseModel> mData = (Response<AppVersionResponseModel>)obj;

                AppVersionResponseModel res = mData.body();
                if(res.getStatus().equalsIgnoreCase("FALSE")){
                  setAlertDialog(SplashActivity.this,"Warning!",res.getMessage());
                } else {
                    timer();
                }
            }

            @Override
            public void OnFailure(Throwable mThrowable) {

            }
        });


    }

    public void timer() {
        new CountDownTimer(3300, 1000) {

            public void onTick(long millisUntilFinished) {
                // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void setAlertDialog(Context mContext, final String strTitle, final String message) {
        final String url = "https://play.google.com/store/apps";
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext)
                .setTitle(strTitle)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
        //  alert.create();
        alert.setCancelable(false);
        alert.show();

    }


}


