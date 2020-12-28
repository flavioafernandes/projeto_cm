
package com.example.projectcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.projectcm.fragments.AddVeiculo;
import com.example.projectcm.fragments.Detalhes;
import com.example.projectcm.fragments.EditarPerfil;
import com.example.projectcm.fragments.MainPage;

public class MainActivity extends AppCompatActivity  implements MainPage.OnMainPageListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainPage Mainpage = new MainPage();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout,Mainpage,"MainPage");
        fragmentTransaction.commit();

        setContentView(R.layout.fragment_main_page);
    }

    @Override
    public void onMPImageInteraction() {
        EditarPerfil EditarPerfil = new EditarPerfil();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, EditarPerfil, "editperfil");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }

    @Override
    public void onMPAddButtonInteraction() {
        AddVeiculo AddVeiculo = new AddVeiculo();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, AddVeiculo, "addveiculo");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }

    @Override
    public void onMPShareButtonInteraction() {
        //to be done
    }

    @Override
    public void onMPGDetailsButtonInteraction() {
        Detalhes Details = new Detalhes();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, Details, "Detalhes");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }
}