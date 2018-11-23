package com.example.ashish.jobintree.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ashish.jobintree.R;
import com.example.ashish.jobintree.main.SharedPrefManager;
import com.example.ashish.jobintree.main.activities.LoginActivity;
import com.example.ashish.jobintree.main.activities.UploadResumeActivity;

public class AccountFragment extends Fragment {

    private Button btLogout,btChangeResume;
    private TextView tvUserName,tvName,tvEmail,tvNumber;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btLogout = view.findViewById(R.id.bt_logout);
        btChangeResume = view.findViewById(R.id.bt_save_changes);
        tvUserName = view.findViewById(R.id.tv_account_name);
        tvName = view.findViewById(R.id.tv_account_user_name);
        tvEmail = view.findViewById(R.id.tv_account_user_email);
        tvNumber = view.findViewById(R.id.tv_account_user_number);

        tvName.setText(SharedPrefManager.getInstance(getContext()).getName());
        tvUserName.setText(SharedPrefManager.getInstance(getContext()).getName());
        tvEmail.setText(SharedPrefManager.getInstance(getContext()).getEmail());
        tvNumber.setText(SharedPrefManager.getInstance(getContext()).getPhoneNumber());

        btChangeResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UploadResumeActivity.class));
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SharedPrefManager.getInstance(getContext()).Logout();
               startActivity(new Intent(getContext(), LoginActivity.class));
               getActivity().finish();
            }
        });
    }
}
