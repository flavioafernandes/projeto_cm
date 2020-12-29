package com.example.projectcm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.projectcm.R;
import com.example.projectcm.fragments.Detalhes;
import com.example.projectcm.fragments.EditarPerfil;
import com.example.projectcm.fragments.EditarVeiculo;
import com.example.projectcm.fragments.MainPage;

public class LoggedInActivity extends AppCompatActivity implements MainPage.OnMainPageListener,EditarPerfil.OnEditarPerfilListener{

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String usedmail = intent.getStringExtra("email");
        Bundle b = intent.getExtras();
        userID = b.getInt("userID");
        setContentView(R.layout.activity_logged_in_);
        MainPage mainPage = MainPage.newInstance(usedmail, userID);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.loggedIn,mainPage,"mainpage");
        fragmentTransaction.commit();   
    }



    @Override
    public void onMPImageInteraction(Integer userid) {
        System.out.println("Cliquei na imagem !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        EditarPerfil editarPerfil = EditarPerfil.newInstance(userid);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loggedIn, editarPerfil,"editarperfil");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }

    @Override
    public void onMPAddButtonInteraction(Integer userid) {

    }

    @Override
    public void onMPShareButtonInteraction() {

    }

    @Override
    public void onMPDetailsButtonInteraction() {
        System.out.println("Cliquei no botao detalhes de 1 carro !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        /*
        Integer arg1 = 0;
        Integer arg2= 0;
        Detalhes detalhes = Detalhes.newInstance(arg1,arg2);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loggedIn, detalhes,"detalhes");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();

         */
    }


}