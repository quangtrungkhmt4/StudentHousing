package com.example.quang.studenthousing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.example.quang.studenthousing.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//
//                try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.example.quang.studenthousing",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        if (!Utils.checkPermission(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivity(new Intent(this,PermissionActivity.class));
            finish();
            return;
        }

//        SharedPreferences pre = getSharedPreferences("studenthousing", MODE_PRIVATE);
//        SharedPreferences.Editor edit=pre.edit();
//        edit.putString("user","");
//        edit.commit();

         new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                SharedPreferences pre = getSharedPreferences("studenthousing", MODE_PRIVATE);
                String user = pre.getString("user","");
                if (!user.equalsIgnoreCase("")){
                    String[] arr = user.split("-");

                    int permission = Integer.parseInt(arr[5]);
                    if (permission == 0 || permission == 2){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }else if (permission == 1){
                        startActivity(new Intent(SplashActivity.this, ManagerActivity.class));
                        finish();
                    }

                }else {
                    startActivity(new Intent(SplashActivity.this, AccountActivity.class));
                    finish();
                }

            }
        }.start();
    }
}
