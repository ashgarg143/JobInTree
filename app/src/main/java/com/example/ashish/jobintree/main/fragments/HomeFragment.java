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
import android.widget.RelativeLayout;

import com.example.ashish.jobintree.R;
import com.example.ashish.jobintree.main.activities.VacanciesActivity;
import com.example.ashish.jobintree.main.rest.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private Button btAllJobs;
    private RelativeLayout rlGurugram,rlDelhi,rlMumbai,rlBangalore,rlHyderabad,rlAppDevelopment,rlWebDevelopment
    ,rlMachineLearning,rlBackendDevelopment,rlDesigning,rlOtherJobs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btAllJobs = view.findViewById(R.id.bt_view_all_jobs);

        rlGurugram = view.findViewById(R.id.rl_gurugram);
        rlDelhi = view.findViewById(R.id.rl_delhi);
        rlMumbai = view.findViewById(R.id.rl_mumbai);
        rlBangalore = view.findViewById(R.id.rl_bangalore);
        rlHyderabad = view.findViewById(R.id.rl_hyderabad);

        rlAppDevelopment = view.findViewById(R.id.rl_app_development);
        rlWebDevelopment = view.findViewById(R.id.rl_web_development);
        rlMachineLearning = view.findViewById(R.id.rl_machine_learning);
        rlBackendDevelopment = view.findViewById(R.id.rl_backend_development);
        rlDesigning = view.findViewById(R.id.rl_designing);
        rlOtherJobs = view.findViewById(R.id.other_jobs);

        rlGurugram.setOnClickListener(this);
        rlDelhi.setOnClickListener(this);
        rlMumbai.setOnClickListener(this);
        rlHyderabad.setOnClickListener(this);
        rlBangalore.setOnClickListener(this);
        rlAppDevelopment.setOnClickListener(this);
        rlWebDevelopment.setOnClickListener(this);
        rlMachineLearning.setOnClickListener(this);
        rlBackendDevelopment.setOnClickListener(this);
        rlDesigning.setOnClickListener(this);
        rlOtherJobs.setOnClickListener(this);
        btAllJobs.setOnClickListener(this);

        /*btAllJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VacanciesActivity.class);
                intent.putExtra("url","vacancy");
                startActivity(intent);
            }
        });*/
    }


    @Override
    public void onClick(View view) {

        Intent intent = new Intent(getActivity(), VacanciesActivity.class);
        String url="";
        String location = "vacancy/location/";
        String category = "vacancy/category/";
        switch (view.getId()){

            case R.id.rl_gurugram:
                //loadVacancy(location+"Gurugram");
                intent.putExtra("name","Gurugram");
                url = location+"Gurugram";
                break;
            case R.id.rl_delhi:
                //loadVacancy(location+"Delhi");
                intent.putExtra("name","Delhi");
                url = location+"Delhi";
                break;
            case R.id.rl_mumbai:
               // loadVacancy(location+"Mumbai");
                intent.putExtra("name","Mumbai");
                url = location+"Mumbai";
                break;
            case R.id.rl_hyderabad:
                //loadVacancy(location+"Hyderabad");
                intent.putExtra("name","Hyderabad");
                url = location+"Hyderabad";
                break;
            case R.id.rl_bangalore:
               // loadVacancy(location+"Bangalore");
                intent.putExtra("name","Bangalore");
                url = location+"Bangalore";
                break;
            case R.id.rl_app_development:
               // loadVacancy(category+"App Development");
                intent.putExtra("name","App Development");
                url = category+"App Development";
                break;
            case R.id.rl_web_development:
                //loadVacancy(category+"Web Development");
                intent.putExtra("name","Web Development");
                url = category+"Web Development";
                break;
            case R.id.rl_machine_learning:
               // loadVacancy(category+"Machine Learning");
                intent.putExtra("name","Machine Learning");
                url = category+"Machine Learning";
                break;
            case R.id.rl_backend_development:
               // loadVacancy(category+"Backend Development");
                intent.putExtra("name","Backend Development");
                url = category+"Backend Development";
                break;
            case R.id.rl_designing:
                //loadVacancy(category+"Design");
                intent.putExtra("name","Design");
                url = category+"Design";
                break;
            case R.id.other_jobs:
                //loadVacancy(category+"Others");
                intent.putExtra("name","Other");
                url = category+"Others";
                break;
            case R.id.bt_view_all_jobs:
                intent.putExtra("name","Showing All");
                url = "vacancy";

        }
        intent.putExtra("url",url);
        startActivity(intent);
    }

    /*private void loadVacancy(String url) {

        Call<ResponseBody> responseBodyCall = RetrofitClient.getRetrofitClient()
                .connectUser()
                .vacancy(url);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }*/
}
