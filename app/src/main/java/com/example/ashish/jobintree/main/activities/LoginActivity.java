package com.example.ashish.jobintree.main.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashish.jobintree.R;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

public class LoginActivity extends AppCompatActivity implements VerificationListener {

    private EditText etLoginNumber, etLoginOtp;
    private Button btLogin,btLoginRequestOtp;
    Verification verification;
    boolean boolverified = false;
    ProgressDialog otpprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLoginNumber = findViewById(R.id.et_login_number);
        etLoginOtp = findViewById(R.id.et_login_otp);

        btLogin = findViewById(R.id.bt_sign_in);

        etLoginOtp.setVisibility(View.INVISIBLE);

        btLoginRequestOtp = findViewById(R.id.bt_login_request_otp);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyOTPInput()){
                    if (isNetworkAvailable()){
                        otpprogressDialog = new ProgressDialog(LoginActivity.this);
                        otpprogressDialog.setMessage("Please wait...");
                        otpprogressDialog.show();
                        verification.verify(etLoginOtp.getText().toString());
                    }
                }
            }
        });

       btLoginRequestOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyNumberInput()) {
                    if (isNetworkAvailable()){
                        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        etLoginNumber.setFocusable(false);
                        hideView(btLoginRequestOtp);
                        TextView textView = findViewById(R.id.tv_a_otp_is_sent);
                        textView.setVisibility(View.VISIBLE);
                        showView(etLoginOtp);
                        btLogin.setVisibility(View.VISIBLE);
                        etLoginOtp.requestFocus();
                        verification = SendOtpVerification.createSmsVerification(
                                SendOtpVerification.config("+91" + etLoginNumber.getText().toString())
                                        .context(LoginActivity.this)
                                        .senderId("MMCHNE")
                                        .autoVerification(false)
                                        .build(), LoginActivity.this
                        );
                        verification.initiate();
                        progressDialog.dismiss();


                        /*DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(etLoginNumber.getText().toString());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignInActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                                    intent.putExtra("number", etLoginNumber.getText().toString());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else {
                                   // String pass = dataSnapshot.child("password").getValue().toString();
                                    if (pass.equals(etLoginOtp.getText().toString())){
                                        String name = dataSnapshot.child("name").getValue().toString();
                                        String email = dataSnapshot.child("email").getValue().toString();
                                        String number = dataSnapshot.child("number").getValue().toString();
                                        String emailverified = dataSnapshot.child("emailverified").getValue().toString();
                                        SharedPrefManager.getInstance(SignInActivity.this).LoginUser(
                                                name,
                                                email,
                                                number,
                                                emailverified
                                        );
                                     }

                                    etLoginNumber.setFocusable(false);
                                    hideView(btLoginRequestOtp);
                                    TextView textView = findViewById(R.id.tv_a_otp_is_sent);
                                    textView.setVisibility(View.VISIBLE);
                                    showView(etLoginOtp);
                                    btLogin.setVisibility(View.VISIBLE);
                                    etLoginOtp.requestFocus();
                                    verification = SendOtpVerification.createSmsVerification(
                                            SendOtpVerification.config("+91" + etLoginNumber.getText().toString())
                                                    .context(LoginActivity.this)
                                                    .senderId("MMCHNE")
                                                    .autoVerification(false)
                                                    .build(), LoginActivity.this
                                    );
                                    verification.initiate();
                                    progressDialog.dismiss();
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/
                    }
                }
            }
        });
    }

    private boolean verifyOTPInput(){
        String otp = etLoginOtp.getText().toString();

        if (otp.isEmpty()){
            etLoginOtp.setError("Required");
            etLoginOtp.requestFocus();
            return false;
        }

        if(otp.length() < 4){
            etLoginOtp.setError("Enter a valid otp");
            etLoginOtp.requestFocus();
            return false;
        }

        return true;
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

    private boolean verifyNumberInput() {
        String number = etLoginNumber.getText().toString();

        if (number.isEmpty()){
            etLoginNumber.setError("Required");
            etLoginNumber.requestFocus();
            return false;
        }

        if (number.length() < 10){
            etLoginNumber.setError("Enter a valid number");
            etLoginNumber.requestFocus();
            return false;
        }

        etLoginOtp.setFocusable(true);

        return true;
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

        otpprogressDialog.dismiss();
        Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show();
        /*final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
        otpprogressDialog.setMessage("Logging you in...");
        otpprogressDialog.setCancelable(false);
        otpprogressDialog.setCanceledOnTouchOutside(false);
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(etLoginNumber.getText().toString());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String number = dataSnapshot.child("number").getValue().toString();
                String emailverified = dataSnapshot.child("emailverified").getValue().toString();
                String alternatenumber = dataSnapshot.child("alternatenumber").getValue().toString();
                // String companyname = dataSnapshot.child("companyname").getValue().toString();
                // String address = dataSnapshot.child("address").getValue().toString();
                String pancardfront = dataSnapshot.child("documentimages").child("pancardfront").getValue().toString();
                String pancardback = dataSnapshot.child("documentimages").child("pancardback").getValue().toString();
                String aadharcardfront = dataSnapshot.child("documentimages").child("aadharcardfront").getValue().toString();
                String aadharcardback = dataSnapshot.child("documentimages").child("aadharcardback").getValue().toString();
                String visitingcardfront = dataSnapshot.child("documentimages").child("visitingcardfront").getValue().toString();
                String visitingcardback = dataSnapshot.child("documentimages").child("visitingcardback").getValue().toString();

                SharedPrefManager.getInstance(SignInActivity.this).LoginUser(
                        name,
                        email,
                        number,
                        alternatenumber,
                        emailverified,
                        "Yes"

                );

                SharedPrefManager.getInstance(getBaseContext()).pancardfront(pancardfront);
                SharedPrefManager.getInstance(getBaseContext()).pancardback(pancardback);
                SharedPrefManager.getInstance(getBaseContext()).aadharcardfront(aadharcardfront);
                SharedPrefManager.getInstance(getBaseContext()).aadharcardback(aadharcardback);
                SharedPrefManager.getInstance(getBaseContext()).visitingcardfront(visitingcardfront);
                SharedPrefManager.getInstance(getBaseContext()).visitingcardback(visitingcardback);
                otpprogressDialog.dismiss();
                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    @Override
    public void onVerificationFailed(Exception paramException) {
        otpprogressDialog.dismiss();
        Toast.makeText(this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
    }
}
