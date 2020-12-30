
package com.example.projectcm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.projectcm.R;
import com.example.projectcm.fragments.Detalhes;
import com.example.projectcm.fragments.Login;
import com.example.projectcm.fragments.Register;

public class MainActivity extends AppCompatActivity implements Login.registerClickListener, Register.ListenerToLogin{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Login login = Login.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainActivityLayout,login,"login");
        fragmentTransaction.commit();
    }

    @Override
    public void changeToRegisterPage() {
        Register register = Register.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainActivityLayout,register,"registerPage");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }

    public void changeToMainPage() {
        Intent myIntent = new Intent(this, LoggedIn_Activity.class);
    }

    public void changeToMainPage(String email) {
        Bundle b = new Bundle();
        b.putString("mail", email);
        Intent myIntent = new Intent(this, LoggedIn_Activity.class);
        myIntent.putExtras(b);

        startActivity(myIntent);
    }

    public void backToLogin(){
        Login lon = (Login) getSupportFragmentManager().findFragmentByTag("login");
        getSupportFragmentManager().popBackStack();
    }


    public void backtoDetailsPage(){
        Detalhes detalhes = (Detalhes) getSupportFragmentManager().findFragmentByTag("detalhes");
        getSupportFragmentManager().popBackStack();
    }
}