package com.example.projectcm.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectcm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarVeiculo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarVeiculo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "userID";
    private static final String CAR_ID = "carID";

    // TODO: Rename and change types of parameters
    private String userID;
    private String carID;

    public EditarVeiculo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userID Parameter 1.
     * @param carID Parameter 2.
     * @return A new instance of fragment EditarVeiculo.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarVeiculo newInstance(int userID, int carID) {
        EditarVeiculo fragment = new EditarVeiculo();
        Bundle args = new Bundle();
        args.putInt(USER_ID, userID);
        args.putInt(CAR_ID, carID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(USER_ID);
            carID = getArguments().getString(CAR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_veiculo, container, false);
    }
}