package com.example.ashish.jobintree.main.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashish.jobintree.R;
import com.example.ashish.jobintree.main.adapters.VacancyAdapter;
import com.example.ashish.jobintree.main.helpers.VacancyItem;
import com.example.ashish.jobintree.main.rest.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacanciesActivity extends AppCompatActivity {

    private static final String TAG = VacanciesActivity.class.getSimpleName();
    RecyclerView rvVacancies;
    ProgressBar pbVacancy;
    TextView tvNovacancy,tvAllVacancies;
    private String url;
    private String name;

    private List<VacancyItem> vacancyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancies);

        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");


        rvVacancies = findViewById(R.id.rv_activity_vacancy_all_vacancies);
        pbVacancy = findViewById(R.id.pb_activity_vacancy);
        tvNovacancy = findViewById(R.id.tv_activity_vacancy_no_vacancy);
        tvAllVacancies = findViewById(R.id.tv_showing_all_vacancies);

        vacancyList = new ArrayList<>();

        rvVacancies.setLayoutManager(new LinearLayoutManager(this));
        rvVacancies.setHasFixedSize(true);

        tvAllVacancies.setText(name+" Vacancies");

       // pbVacancy.setVisibility(View.GONE);
        tvNovacancy.setVisibility(View.GONE);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadVacancies();
    }

    private void loadVacancies() {
        if(isNetworkAvailable()){
            Call<ResponseBody> responseBodyCall = RetrofitClient.getRetrofitClient()
                    .connectUser()
                    .vacancy(url);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.isSuccessful()){
                        pbVacancy.setVisibility(View.GONE);

                        try {
                            String s = response.body().string();
                            if(s!=null){
                                JSONObject jsonObject = new JSONObject(s);
                                int status = jsonObject.getInt("status");

                                if (status == 200){
                                    JSONArray data = jsonObject.getJSONArray("data");

                                    for(int i=0;i<data.length();i++){
                                        JSONObject object = data.getJSONObject(i);
                                        VacancyItem vacancyItem = new VacancyItem(object.getString("id"),object.getString("name"),object.getString("location")
                                                ,object.getString("salary"),object.getString("applyby"),object.getString("description")
                                                ,object.getString("category"),object.getJSONArray("listOfSkills"),object.getJSONObject("company"));
                                        vacancyList.add(vacancyItem);

                                    }
                                }
                                VacancyAdapter adapter = new VacancyAdapter(VacanciesActivity.this,vacancyList);
                                rvVacancies.setAdapter(adapter);


                                if(vacancyList.size()==0){
                                    tvAllVacancies.setVisibility(View.GONE);
                                    tvNovacancy.setVisibility(View.VISIBLE);
                                }

                                //Toast.makeText(VacanciesActivity.this, "list size: "+vacancyList.size(), Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pbVacancy.setVisibility(View.GONE);
                    Log.i(TAG, "onFailure: "+ t.getMessage());
                    Toast.makeText(VacanciesActivity.this, "internet failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
