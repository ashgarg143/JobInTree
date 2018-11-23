package com.example.ashish.jobintree.main.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class VacancyItem {
    private String id,name,location,salary,applyby,description,category;
    private JSONArray listOfSkills;
    private JSONObject company;

    public VacancyItem(String id, String name, String location, String salary, String applyby, String description
            , String category, JSONArray listOfSkills,JSONObject company) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.salary = salary;
        this.applyby = applyby;
        this.description = description;
        this.category = category;
        this.listOfSkills = listOfSkills;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getSalary() {
        return salary;
    }

    public String getApplyby() {
        return applyby;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public JSONArray getListOfSkills() {
        return listOfSkills;
    }

    public JSONObject getCompany() {
        return company;
    }
}
