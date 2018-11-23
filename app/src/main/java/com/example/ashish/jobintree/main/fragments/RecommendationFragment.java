package com.example.ashish.jobintree.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashish.jobintree.R;
import com.example.ashish.jobintree.main.activities.VacanciesActivity;
import com.example.ashish.jobintree.main.adapters.VacancyAdapter;
import com.example.ashish.jobintree.main.helpers.Skills;
import com.example.ashish.jobintree.main.helpers.VacancyItem;
import com.example.ashish.jobintree.main.rest.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationFragment extends Fragment {

    private List<VacancyItem> vacancyList;
    private String vacancyListString="";
    private List<String> skillsList;

    private RecyclerView rvVacancies;
    ProgressBar pbVacancy;
    TextView tvNovacancy,tvAllVacancies;

    private static final String vacancyurl = "http://2d5048ae.ngrok.io/get_vacancy";
    private static final String vacancyjson = "{\"skill\" : [\"1\" , \"2\"]}";
    private static final String TAG = RecommendationFragment.class.getSimpleName() ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.activity_vacancies,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        skillsList = new ArrayList<>();

        rvVacancies = view.findViewById(R.id.rv_activity_vacancy_all_vacancies);
        pbVacancy = view.findViewById(R.id.pb_activity_vacancy);
        tvNovacancy = view.findViewById(R.id.tv_activity_vacancy_no_vacancy);
        tvAllVacancies = view.findViewById(R.id.tv_showing_all_vacancies);


        vacancyList = new ArrayList<>();

        rvVacancies.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVacancies.setHasFixedSize(true);

        tvAllVacancies.setText("Recommended Vacancies");
        tvNovacancy.setVisibility(View.GONE);
        loadRecommeded();

    }

    private void loadRecommeded() {

        skillsList.add("13");
        skillsList.add("15");
        skillsList.add("0");
        /*skillsList.add("1");
        skillsList.add("2");*/
        Skills skills = new Skills(skillsList);
        try {
            Call<ResponseBody> responseBodyCall = RetrofitClient.getRetrofitClient()
                    .connectUser()
                    .getVacancy(vacancyurl, skills);

            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        String s = "";
                        try {
                            s = response.body().string();

                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("vacancy");

                            for(int i=0;i<array.length();i++){
                                String vacancyId = array.getString(i);
                                vacancyListString= vacancyListString+ vacancyId + ",";

                            }
                            Log.i(TAG, "onResponse: vacancy "+ vacancyListString);
                            loadVacancies();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                      //  Toast.makeText(getContext(), "response: "+s, Toast.LENGTH_SHORT).show();
                       // Log.i(TAG, "onResponse: "+s);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(TAG, "onFailure: "+ t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void  loadVacancies() {

        Call<ResponseBody> call = RetrofitClient.getRetrofitClient()
                .connectUser()
                .recommendedVacancies( vacancyListString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    pbVacancy.setVisibility(View.GONE);
                    try {
                        String s = response.body().string();
                        Log.i(TAG, "onResponse: " + s);


                        if (s != null) {
                            JSONObject jsonObject = new JSONObject(s);
                            int status = jsonObject.getInt("status");

                            if (status == 200) {
                                JSONArray data = jsonObject.getJSONArray("data");

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    VacancyItem vacancyItem = new VacancyItem(object.getString("id"), object.getString("name"), object.getString("location")
                                            , object.getString("salary"), object.getString("applyby"), object.getString("description")
                                            , object.getString("category"), object.getJSONArray("listOfSkills"), object.getJSONObject("company"));
                                    vacancyList.add(vacancyItem);

                                }
                            }
                            VacancyAdapter adapter = new VacancyAdapter(getContext(), vacancyList);
                            rvVacancies.setAdapter(adapter);


                            if (vacancyList.size() == 0) {
                                tvAllVacancies.setVisibility(View.GONE);
                                tvNovacancy.setVisibility(View.VISIBLE);
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pbVacancy.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Internet Failure", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: " + t.getMessage());
            }


            });
        }
    }
