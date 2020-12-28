package com.example.projectcm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.projectcm.R;
import com.example.projectcm.fragments.MainPage;

public class LoggedIn_Activity extends AppCompatActivity implements MainPage.OnMainPageListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_);
        MainPage mainPage = MainPage.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.loggedIn,mainPage,"mainpage");
        fragmentTransaction.commit();   
    }












    @Override
    public void onMPImageInteraction() {

    }

    @Override
    public void onMPAddButtonInteraction() {

    }

    @Override
    public void onMPShareButtonInteraction() {

    }

    @Override
    public void onMPGDetailsButtonInteraction() {

    }
}