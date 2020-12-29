
package com.example.projectcm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.projectcm.R;
import com.example.projectcm.fragments.Detalhes;
import com.example.projectcm.fragments.EditarVeiculo;
import com.example.projectcm.fragments.Login;
import com.example.projectcm.fragments.MainPage;
import com.example.projectcm.fragments.Register;

public class MainActivity extends AppCompatActivity implements Login.registerClickListener, Register.ListenerToLogin, Detalhes.EditClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    @Override
    public void changeToMainPage(int userID) {
        Bundle b = new Bundle();
        b.putInt("userID", userID);
        Intent myIntent = new Intent(this, LoggedInActivity.class);
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

    @Override
    public void goToEditCarPage(int userID, int carID){
        EditarVeiculo editarVeiculo = EditarVeiculo.newInstance(userID, carID);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainActivityLayout, editarVeiculo,"editarcarro");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }
}