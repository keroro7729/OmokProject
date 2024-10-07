package com.example.omokapp.ui.home;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.omokapp.Api.ApiManager;
import com.example.omokapp.OmokRules.RenjuRule;
import com.example.omokapp.R;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<Integer> number;

    public HomeViewModel() {
        number = new MutableLiveData<>();
        number.setValue(0);
    }

    public String getNumber(){ return number.getValue().toString(); }
    public void setNumber(int n){ number.setValue(n); }
}