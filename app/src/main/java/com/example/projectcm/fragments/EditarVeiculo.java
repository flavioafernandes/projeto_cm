package com.example.projectcm.fragments;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarVeiculo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarVeiculo extends Fragment {

    DatabaseHelper db;

    ImageView goBackBtn;
    TextView carMake;
    TextView carModel;
    ImageView carLogo;
    ImageView carImage;
    ImageView changeImageBtn;
    ScrollView carInfo;
    Button cancelBtn;
    Button saveBtn;

    private static final String USER_ID = "userID";
    private static final String CAR_ID = "carID";

    private String userID;
    private String carID;

    public EditarVeiculo() {
        // Required empty public constructor
    }

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
        db = new DatabaseHelper(getContext());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(USER_ID);
            carID = getArguments().getString(CAR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editar_veiculo, container, false);
        // Inflate the layout for this fragment

        goBackBtn = v.findViewById(R.id.voltar);
        carMake = v.findViewById(R.id.Title_car);
        carModel = v.findViewById(R.id.SubTitle_car);
        carLogo = v.findViewById(R.id.Car_logo);
        carImage = v.findViewById(R.id.Car_image);
        return v;
    }

    /**
     * 0 - carID
     * 1 - carMake
     * 2 - carModel
     * 3 - carYear
     * 4 - carInfo (JSON)
     * 5 - imageURI
     * */
    private class UpdateCarTask extends AsyncTask<Bundle, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Bundle... bundles) {

            System.out.printf("UpdateCarTask");
            boolean result = db.updateCarInfo(bundles[0].getInt("carID"), bundles[0].getString("carMake"), bundles[0].getString("carModel"), bundles[0].getString("carYear"), bundles[0].getString("carInfo"), bundles[0].getString("imageURI"));

            return result;
        }
    }
}