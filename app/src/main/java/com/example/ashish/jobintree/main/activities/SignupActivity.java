package com.example.ashish.jobintree.main.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashish.jobintree.R;
import com.example.ashish.jobintree.main.rest.RetrofitClient;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements VerificationListener {

    private EditText etSignupName, etSignupEmail, etSignupNumber, etSignUPOTP;
    private Button btSignup,btSignUpRequestOtp;
    TextView tvEmailExists;

    List<String> listOfUsers;

    Verification verification;

    ProgressDialog otpProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        String number = getIntent().getStringExtra("number");

        etSignupName = findViewById(R.id.et_signup_name);
        etSignupEmail = findViewById(R.id.et_signup_email);
        etSignupNumber = findViewById(R.id.et_signup_number);
        etSignUPOTP = findViewById(R.id.et_signup_otp);
        tvEmailExists = findViewById(R.id.tv_signup_email_exists);


        btSignup = findViewById(R.id.bt_sign_up);

        btSignUpRequestOtp = findViewById(R.id.bt_signup_request_otp);

        if(number!=null) {
            etSignupNumber.setText(number);
            etSignupNumber.setFocusable(false);
        }



        listOfUsers = new ArrayList<>();

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpProgress = new ProgressDialog(SignupActivity.this);
                otpProgress.setMessage("Please wait...");
                otpProgress.setCanceledOnTouchOutside(false);
                otpProgress.setCancelable(false);
                otpProgress.show();
                verification.verify(etSignUPOTP.getText().toString());
            }
        });

        btSignUpRequestOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyInputs()){
                    if(isNetworkAvailable()){
                        showView(etSignUPOTP);
                        hideView(btSignUpRequestOtp);
                        btSignup.setVisibility(View.VISIBLE);
                        //btSignup.setVisibility(View.VISIBLE);
                        verification = SendOtpVerification.createSmsVerification(
                                SendOtpVerification.config("+91" + etSignupNumber.getText().toString())
                                        .context(SignupActivity.this)
                                        .senderId("JNTREE")
                                        .autoVerification(false)
                                        .build(), SignupActivity.this
                        );
                        verification.initiate();
                    }
                }
            }
        });
    }

    public void hideView(final View view){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.push_down_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }

    public void showView(final View view){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }

    private void signup() {
        if (verifyOTPInput()) {
            if (isNetworkAvailable()) {
                final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                progressDialog.setMessage("Signing you up...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                Call<ResponseBody> responseBodyCall = RetrofitClient.getRetrofitClient()
                        .connectUser()
                        .signup(etSignupName.getText().toString(),etSignupEmail.getText().toString(),etSignupNumber.getText().toString());
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressDialog.dismiss();

                        if(response.isSuccessful()){
                            try {
                                String s= response.body().string();

                                if(s!=null){
                                    JSONObject jsonObject = new JSONObject(s);
                                    int status = jsonObject.getInt("status");
                                    if(status == 200){
                                        Toast.makeText(SignupActivity.this,jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                                    } else if(status == 204){
                                        Toast.makeText(SignupActivity.this,jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignupActivity.this,jsonObject.getString("message") , Toast.LENGTH_SHORT).show();

                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }
    }

    private String generateHash(String s) {
        int hash = 21;
        for (int i = 0; i < s.length(); i++) {
            hash = hash*31 + s.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private boolean verifyOTPInput(){
        String otp = etSignUPOTP.getText().toString();

        if (otp.isEmpty()){
            etSignUPOTP.setError("Required");
            etSignUPOTP.requestFocus();
            return false;
        }

        if (otp.length() < 4){
            etSignUPOTP.setError("Minimum 4 characters required");
            etSignUPOTP.requestFocus();
            return false;
        }

        return true;

    }

    private boolean verifyInputs() {
        String name = etSignupName.getText().toString();
        String email = etSignupEmail.getText().toString();


        if (name.isEmpty()){
            etSignupName.setError("Reuiqred");
            etSignupName.requestFocus();
            return false;
        }

        if (email.isEmpty()){
            etSignupEmail.setError("Required");
            etSignupEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etSignupEmail.setError("Enter a valid email");
            etSignupEmail.requestFocus();
            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    @Override
    public void onInitiated(String response) {

    }

    @Override
    public void onInitiationFailed(Exception paramException) {

    }

    @Override
    public void onVerified(String response) {
        otpProgress.dismiss();
        signup();
    }

    @Override
    public void onVerificationFailed(Exception paramException) {
        otpProgress.dismiss();
        Toast.makeText(this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
    }
}
