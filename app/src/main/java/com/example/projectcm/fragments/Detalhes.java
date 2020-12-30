package com.example.projectcm.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Detalhes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Detalhes extends Fragment {

    DatabaseHelper db;

    TextView carTitle;
    TextView carModel;
    ImageView carLogo;
    ImageView carImage;
    ScrollView carDetails;
    Button removeCarBtn;
    Button editCarBtn;
    ImageView goBackBtn;
    DetailsClickListener editPageListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "userID";
    private static final String CAR_ID = "carID";

    // TODO: Rename and change types of parameters
    private int userID;
    private int carID;

    public Detalhes() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Detalhes newInstance(int userID, int carID) {
        Detalhes fragment = new Detalhes();
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
            userID = getArguments().getInt(USER_ID);
            carID = getArguments().getInt(CAR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  = inflater.inflate(R.layout.fragment_detalhes, container, false);

        GetCarInfoTask getCarInfoTask = new GetCarInfoTask();
        Cursor carInfo = getCarInfoTask.doInBackground(carID);

        carTitle = v.findViewById(R.id.Title_car);
        carModel = v.findViewById(R.id.SubTitle_car);
        carLogo = v.findViewById(R.id.Car_logo);
        carImage = v.findViewById(R.id.Car_image);
        carDetails = v.findViewById(R.id.scroll_car_info);
        removeCarBtn = v.findViewById(R.id.remover);
        editCarBtn = v.findViewById(R.id.editar);
        goBackBtn = v.findViewById(R.id.voltar);

        while (carInfo.moveToNext()){
            int carID = Integer.parseInt(carInfo.getString(0));
            String make = carInfo.getString(1);
            String model =  carInfo.getString(2);
            String info =  carInfo.getString(3);
            String year = carInfo.getString(4);

            carTitle.setText(make);
            carModel.setText(model);
            int resourceID =  getResources().getIdentifier(make.toLowerCase(), "drawable", getContext().getPackageName());
            carLogo.setImageResource(resourceID);
        }


        removeCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RemoveCarTask removeCarTask = new RemoveCarTask();
                removeCarTask.doInBackground(carID);
                Toast.makeText(getActivity(), "Carro removido", Toast.LENGTH_LONG).show();
                //TODO: Chamar o fragmento da lista dos carros
                editPageListener.goToMainPage(userID);
            }
        });


        editCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Chamar o fragmento de editar carros
                editPageListener.goToEditCarPage(userID, carID);
            }
        });

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Chamar o fragmento da lista dos carros
                editPageListener.goToMainPage(userID);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DetailsClickListener){
            editPageListener = (DetailsClickListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface DetailsClickListener {
        void goToEditCarPage(int userID, int carID);
        void goToMainPage(int userID);
    }

    private class GetCarsFromUserTask extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... ids) {

            Cursor results = db.getCarsFromUser(ids[0]);

            return results;
        }
    }

    private class GetCarInfoTask extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... ids) {

            System.out.printf("GetCarInfoTask");
            Cursor results = db.getCarInfo(ids[0]);

            return results;
        }
    }


    private class AddNewCarTask extends AsyncTask<Bundle, Void, Integer> {

        @Override
        protected Integer doInBackground(Bundle... bundles) {

            System.out.printf("AddNewCarTask");
            int id = (int) db.addACarToAUser(bundles[0].getString("make"), bundles[0].getString("model"), bundles[0].getString("year"), bundles[0].getString("info"), bundles[0].getInt("ownerID"));
            System.out.println("ID: " + id);
            return id;
        }
    }

    private class RemoveCarTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... ids) {

            System.out.printf("RemoveCarTask");
            Integer result = db.deleteCarFromUser(ids[0]);

            return result;
        }
    }

}