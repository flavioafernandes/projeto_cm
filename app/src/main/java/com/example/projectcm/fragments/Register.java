package com.example.projectcm.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.projectcm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance() {
        Register fragment = new Register();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_register, container, false);

        Button b = v.findViewById(R.id.confirmRegister);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = v.findViewById(R.id.newName);
                EditText email = v.findViewById(R.id.newEmail);
                EditText password = v.findViewById(R.id.newpassword);
                EditText confirmPassword = v.findViewById(R.id.confirmNewPassword);
                CalendarView cv = v.findViewById(R.id.calendarView);

                cv.getDateTextAppearance();

                //guardar na base de dados
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}