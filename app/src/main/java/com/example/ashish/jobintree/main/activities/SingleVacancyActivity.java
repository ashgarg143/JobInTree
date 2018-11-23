package com.example.ashish.jobintree.main.activities;

import android.app.ProgressDialog;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ashish.jobintree.R;
import com.example.ashish.jobintree.main.rest.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleVacancyActivity extends AppCompatActivity {

    private static final String TAG = SingleVacancyActivity.class.getSimpleName();
    private ConstraintLayout cl;
    private TextView tvVacancyName,tvCompanyName,tvLocation,tvSalary,tvApplyBy,tvCompanyDetails
            ,tvJobDetails,tvSkillsRequired;

    private String vacancyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_vacancy);

        vacancyId = getIntent().getStringExtra("vacancyid");

        cl = findViewById(R.id.cl_single_vacancy_item_activity);
        tvVacancyName = findViewById(R.id.tv_vacancy_name_vacancy_item);
        tvCompanyName = findViewById(R.id.tv_company_name_vacancy_item);
        tvLocation = findViewById(R.id.tv_location_vacancy_item);
        tvSalary = findViewById(R.id.tv_salary_vacancy_item);
        tvApplyBy = findViewById(R.id.tv_apply_by_vacancy_item);
        tvCompanyDetails = findViewById(R.id.tv_company_detail_vacancy_item);
        tvJobDetails = findViewById(R.id.tv_job_detail_vacancy_item);
        tvSkillsRequired = findViewById(R.id.tv_skill_required_vacancy_item);

        setTitle("Job Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadVacancyDetails();
    }

    private void loadVacancyDetails() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Job Details...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Call<ResponseBody> call = RetrofitClient.getRetrofitClient()
                .connectUser()
                .vacancyDetails("vacancy/get/"+vacancyId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    dialog.dismiss();
                    cl.setVisibility(View.VISIBLE);
                    String s ="";
                    try {
                        s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray array = jsonObject.getJSONArray("data");

                        JSONObject object = array.getJSONObject(0);
                        JSONObject object1 = object.getJSONObject("company");
                        String vacancyName = object.getString("name");
                        String companyName = object1.getString("name");
                        String location = object.getString("location");
                        String salary = object.getString("salary");
                        String applyBy = object.getString("applyby");
                        String jobDescription = object.getString("description");
                        String companyDescription = object1.getString("description");

                        String skillsRequired = "";
                        JSONArray array1 = object.getJSONArray("listOfSkills");
                        int i;

                        for (i=0;i<array1.length()-1;i++){
                            JSONObject skill = array1.getJSONObject(i);

                            String name = skill.getString("name");
                            skillsRequired = skillsRequired + name + ", ";
                        }
                        JSONObject object2 = array1.getJSONObject(i);
                        String name = object2.getString("name");
                        skillsRequired = skillsRequired + name;

                        tvVacancyName.setText(vacancyName);
                        tvCompanyName.setText(companyName);
                        tvLocation.setText(location);
                        tvSalary.setText(salary);
                        tvApplyBy.setText(applyBy);
                        tvJobDetails.setText(jobDescription);
                        tvCompanyDetails.setText(companyDescription);
                        tvSkillsRequired.setText(skillsRequired);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Log.i(TAG, "onResponse: " + s);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
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
