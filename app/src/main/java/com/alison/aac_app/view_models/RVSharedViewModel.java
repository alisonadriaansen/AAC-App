package com.alison.aac_app.view_models;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class RVSharedViewModel extends ViewModel {
    private List<String> data = new ArrayList<>();

    public void setData(List<String> newData) {
        data = newData;
    }

    public List<String> getData() {
        return data;
    }
}

