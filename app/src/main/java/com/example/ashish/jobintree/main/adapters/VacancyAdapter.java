package com.example.ashish.jobintree.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashish.jobintree.R;
import com.example.ashish.jobintree.main.activities.SingleVacancyActivity;
import com.example.ashish.jobintree.main.helpers.VacancyItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VacancyAdapter extends RecyclerView.Adapter<VacancyAdapter.ViewHolder> {

    private Context context;
    private List<VacancyItem> vacancyList;

    public VacancyAdapter(Context context, List<VacancyItem> vacancyList) {
        this.context = context;
        this.vacancyList = vacancyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.vacancy_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final VacancyItem vacancy = vacancyList.get(i);


        viewHolder.cvVacancyitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "vacancy id: "+vacancy.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, SingleVacancyActivity.class);
                intent.putExtra("vacancyid",vacancy.getId());
                context.startActivity(intent);
                Log.i("VacancyAdapter", "onClick: "+ vacancy.getId());

            }
        });

        JSONObject object = vacancy.getCompany();
        String companyName="";
        try {
            companyName = object.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewHolder.tvVacancyName.setText(vacancy.getName());
        viewHolder.tvCompanyName.setText(companyName);
        viewHolder.tvLocation.setText(": "+vacancy.getLocation());
        viewHolder.tvSalary.setText(": "+vacancy.getSalary());
        viewHolder.tvApplyBy.setText(": "+vacancy.getApplyby());
    }

    @Override
    public int getItemCount() {
        return vacancyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cvVacancyitem;
        private TextView tvVacancyName,tvCompanyName,tvLocation,tvSalary,tvApplyBy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvVacancyitem = itemView.findViewById(R.id.cv_vacancy_item);
            tvVacancyName = itemView.findViewById(R.id.tv_vacancy_name_vacancy_item);
            tvCompanyName = itemView.findViewById(R.id.tv_company_name_vacancy_item);
            tvLocation = itemView.findViewById(R.id.tv_location_vacancy_item);
            tvSalary = itemView.findViewById(R.id.tv_salary_vacancy_item);
            tvApplyBy = itemView.findViewById(R.id.tv_apply_by_vacancy_item);
        }
    }
}
