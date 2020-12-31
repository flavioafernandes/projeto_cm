package com.example.projectcm.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;
import com.example.projectcm.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.app.Activity.RESULT_OK;

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
    ListView addedInfos;

    String chosenMake;
    String chosenModel;
    JSONObject newCarInfo;
    int userID;

    Uri selectedImageURI;

    ArrayList<String> newInfos = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    OnActionListener onActionListener;

    private static int RESULT_LOAD_IMAGE = 1;
    private static int SELECT_PICTURE = 1;



    public AddVeiculo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddVeiculo.
     */
    // TODO: Rename and change types and number of parameters
    public static AddVeiculo newInstance() {
        AddVeiculo fragment = new AddVeiculo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getInt("userid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_veiculo, container, false);

        newCarInfo = new JSONObject();
        JSONArray finalArr = new JSONArray();

        addInfo = v.findViewById(R.id.Adicionar_caracteristica);
        addInfo.setOnClickListener(v1 -> {

            View blur = (View) v.findViewById(R.id.blur_grey_square);
            blur.setVisibility(View.VISIBLE);

            View add = (View) v.findViewById(R.id.add_caracteristica_grey_square);
            add.setVisibility(View.VISIBLE);

            EditText nome = (EditText) v.findViewById(R.id.nome_caracteristica_adicionar);
            nome.setVisibility(View.VISIBLE);

            EditText valor = (EditText) v.findViewById(R.id.valor_caracteristica_adicionar);
            valor.setVisibility(View.VISIBLE);

            Button guardar = (Button) v.findViewById(R.id.Guardar_adicionar_caracteristica);
            Button cancelar = (Button) v.findViewById(R.id.Cancelar_adicionar_caracteristica);

            guardar.setVisibility(View.VISIBLE);
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = nome.getText().toString();
                    String value = valor.getText().toString();

                    JSONArray arr = new JSONArray();
                    arr.put(name);
                    arr.put(value);

                    finalArr.put(arr);
                    newInfos.add(name + " - " + value);
                    arrayAdapter.notifyDataSetChanged();

                    blur.setVisibility(View.INVISIBLE);
                    add.setVisibility(View.INVISIBLE);
                    nome.setText("");
                    nome.setVisibility(View.INVISIBLE);
                    valor.setText("");
                    valor.setVisibility(View.INVISIBLE);
                    guardar.setVisibility(View.INVISIBLE);
                    cancelar.setVisibility(View.INVISIBLE);

                }
            });


            cancelar.setVisibility(View.VISIBLE);
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blur.setVisibility(View.INVISIBLE);
                    add.setVisibility(View.INVISIBLE);
                    nome.setVisibility(View.INVISIBLE);
                    valor.setVisibility(View.INVISIBLE);
                    guardar.setVisibility(View.INVISIBLE);
                    cancelar.setVisibility(View.INVISIBLE);
                }
            });
        });

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

        addedInfos = v.findViewById(R.id.nome_caracteristica);
        newInfos.add("teste");
        newInfos.add("teste");
        newInfos.add("teste");
        newInfos.add("teste");
        newInfos.add("teste");
        newInfos.add("teste");
        newInfos.add("teste");
        newInfos.add("teste");
        newInfos.add("teste");

        arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                newInfos );

        addedInfos.setAdapter(arrayAdapter);

        //BUSCAR imagem
        Button buttonaddImage = v.findViewById(R.id.buttonAddImage);
        buttonaddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionListener.goToMainPage(userID);
            }
        });

        saveNewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("SAVE NEW CAR CLICKED");

                String model = chosenModel.split("\\(")[0];
                String year = chosenModel.split("\\(")[1].replace(")", "");

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
                bundle.putInt("ownerID", userID);
                bundle.putString("imageURI", selectedImageURI.toString());

                System.out.println("OI");

                AddNewCarTask addNewCarTask = new AddNewCarTask();
                addNewCarTask.doInBackground(bundle);

                onActionListener.goToMainPage(userID);
            }
        });


        return v;
    }
    //TODO:guardar URI na base de dados junto à informação do carro do carro
    //TODO:
    // Picasso.with(MainActivity.this).load(selectedImageURI).noPlaceholder().centerCrop().fit()
    // .into((ImageView) findViewById(R.id.image_display));
    //TODO: esta linnha para chamar depois a imagem
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageURI = data.getData();
                System.out.println(selectedImageURI.toString());
            }
        }
    }



    public interface OnActionListener{
        void goToMainPage(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AddVeiculo.OnActionListener){
            onActionListener = (AddVeiculo.OnActionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
            Integer result = (int) db.addACarToAUser(bundles[0].getString("make"), bundles[0].getString("model"), bundles[0].getString("year"), bundles[0].getString("info"), bundles[0].getInt("ownerID"), bundles[0].getString("imageURI"));

            return result;
        }
    }


}