package com.example.projectcm.fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddVeiculo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddVeiculo extends Fragment {

    DatabaseHelper db;
    Spinner spinnerMake;
    Spinner spinnerModel;
    Button addImageBtn;
    Button addInfo;
    Button saveNewCar;
    Button cancelBtn;

    String chosenMake;
    String chosenModel;
    JSONObject newCarInfo;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddVeiculo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddVeiculo.
     */
    // TODO: Rename and change types and number of parameters
    public static AddVeiculo newInstance(String param1, String param2) {
        AddVeiculo fragment = new AddVeiculo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_veiculo, container, false);

        addInfo = v.findViewById(R.id.Adicionar_caracteristica);
        saveNewCar = v.findViewById(R.id.Guardar);
        cancelBtn = v.findViewById(R.id.Cancelar);

        // Inflate the layout for this fragment
        ArrayList<String> carsArrayList = new ArrayList<String>();
        GetAllMakesTask getAllMakesTask = new GetAllMakesTask();
        Cursor cars = getAllMakesTask.doInBackground();

        cars.moveToFirst();
        while(!cars.isAfterLast()) {
            carsArrayList.add(cars.getString(0)); //add the item
            cars.moveToNext();
        }

        Collections.sort(carsArrayList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });


        spinnerMake = v.findViewById(R.id.spinnerMake);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, carsArrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMake.setAdapter(spinnerArrayAdapter);

        spinnerMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                chosenMake = carsArrayList.get(position);
                //System.out.println(carsArrayList.get(position));
                ArrayList<String> modelsArrayList = new ArrayList<String>();
                GetAllModelsFromMakeTask getAllModelsFromMakeTask = new GetAllModelsFromMakeTask();
                Cursor models = getAllModelsFromMakeTask.doInBackground(chosenMake);

                models.moveToFirst();
                while(!models.isAfterLast()) {
                    modelsArrayList.add(models.getString(0) + " (" + models.getString(1) + ")"); //add the item
                    models.moveToNext();
                }

                Collections.sort(modelsArrayList, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareToIgnoreCase(s2);
                    }
                });

                spinnerModel = v.findViewById(R.id.spinnerModel);
                ArrayAdapter<String> spinnerModelAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, modelsArrayList);
                spinnerModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerModel.setAdapter(spinnerModelAdapter);

                spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        chosenModel = (modelsArrayList.get(position));
                        System.out.println(chosenModel);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        saveNewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("SAVE NEW CAR CLICKED");

                String model = chosenModel.split("\\(")[0];
                String year = chosenModel.split("\\(")[1].replace(")", "");

                newCarInfo = new JSONObject();

                //TODO: Fazer um JSONArray por cada info que se queira guardar
                JSONArray arr = new JSONArray();
                arr.put("Motor");
                arr.put("1.5");

                JSONArray arr2 = new JSONArray();
                arr2.put("Portas");
                arr2.put("5");

                JSONArray finalArr = new JSONArray();
                finalArr.put(arr);
                finalArr.put(arr2);

                try {
                    newCarInfo.put("infos", finalArr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                bundle.putString("make", chosenMake);
                bundle.putString("model", model.substring(0, model.length()-1));
                bundle.putString("year", year);
                bundle.putString("info", newCarInfo.toString());
                bundle.putInt("ownerID", 1);

                System.out.println("OI");

                AddNewCarTask addNewCarTask = new AddNewCarTask();
                addNewCarTask.doInBackground(bundle);

            }
        });


        return v;
    }

    private class GetAllMakesTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {

            System.out.printf("GetAllMakesTask");
            Cursor result = db.getAllMakes();

            return result;
        }
    }

    private class GetAllModelsFromMakeTask extends AsyncTask<String, Void, Cursor> {

        @Override
        protected Cursor doInBackground(String... makes) {

            System.out.printf("GetAllMakesTask");
            Cursor result = db.getAllModelsFromAMake(makes[0]);

            return result;
        }
    }


    /**
     * 0 - make
     * 1 - model
     * 2 - year
     * 3 - info (JSON)
     * 4 - ownerID
     * */
    private class AddNewCarTask extends AsyncTask<Bundle, Void, Integer> {

        @Override
        protected Integer doInBackground(Bundle... bundles) {

            System.out.printf("AddNewCarTask");
            Integer result = (int) db.addACarToAUser(bundles[0].getString("make"), bundles[0].getString("model"), bundles[0].getString("year"), bundles[0].getString("info"), bundles[0].getInt("ownerID"));

            return result;
        }
    }


}