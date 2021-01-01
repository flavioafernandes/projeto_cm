package com.example.projectcm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.projectcm.R;
import com.example.projectcm.fragments.AddVeiculo;
import com.example.projectcm.fragments.Detalhes;
import com.example.projectcm.fragments.EditarPerfil;
import com.example.projectcm.fragments.EditarVeiculo;
import com.example.projectcm.fragments.Login;
import com.example.projectcm.fragments.MainPage;

public class LoggedInActivity extends AppCompatActivity implements MainPage.OnMainPageListener,EditarPerfil.OnEditarPerfilListener, Detalhes.DetailsClickListener,AddVeiculo.OnActionListener{

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        //String usedmail = intent.getStringExtra("email");
        Bundle b = intent.getExtras();
        userID = b.getInt("userID");
        setContentView(R.layout.activity_logged_in_);
        MainPage mainPage = MainPage.newInstance(userID);
        //AddVeiculo addVeiculo = AddVeiculo.newInstance("", "");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.loggedIn,mainPage,"mainpage");
        //fragmentTransaction.add(R.id.loggedIn, addVeiculo, "addveiculo");
        fragmentTransaction.commit();   
    }



    @Override
    public void onMPImageInteraction(int userid) {
        //System.out.println("Cliquei na imagem !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        EditarPerfil editarPerfil = EditarPerfil.newInstance();
        Bundle args = new Bundle();
        args.putInt("userid", userid);
        editarPerfil.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loggedIn, editarPerfil,"editarperfil");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }

    @Override
    public void onMPAddButtonInteraction(int userid) {
        System.out.println("A ir para a adicionar carro");
        AddVeiculo addVeiculo = AddVeiculo.newInstance();
        Bundle args = new Bundle();
        args.putInt("userid", userid);
        addVeiculo.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loggedIn, addVeiculo,"addCar");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();


    }

    @Override
    public void onMPShareButtonInteraction() {

    }

    @Override
    public void onMPDetailsButtonInteraction(int userID, int carID) {

        System.out.println("Cliquei no botao detalhes de 1 carro !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        Detalhes detalhes = Detalhes.newInstance(userID, carID);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loggedIn, detalhes,"detalhes");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();


    }

    @Override
    public void goToEditCarPage(int userID, int carID){
        EditarVeiculo editarVeiculo = EditarVeiculo.newInstance(userID, carID);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainActivityLayout, editarVeiculo,"editarcarro");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }
    @Override
    public void OnEPCancelClick(Integer UserID) {

    }

    @Override
    public void ONEPSaveClick(Integer UserID) {

    }

    @Override
    public void goToMainPage(int userID){
        MainPage mainPage = (MainPage) getSupportFragmentManager().findFragmentByTag("mainpage");
        getSupportFragmentManager().popBackStack();
    }



}