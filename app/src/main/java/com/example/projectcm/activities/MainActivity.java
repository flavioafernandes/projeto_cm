
package com.example.projectcm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.projectcm.R;
import com.example.projectcm.fragments.Login;
import com.example.projectcm.fragments.Register;

public class MainActivity extends AppCompatActivity implements Login.registerClickListener {

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
        fragmentTransaction.commit();
    }
}