package com.example.projectcm.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarVeiculo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class    EditarVeiculo extends Fragment {

    DatabaseHelper db;

    ImageView goBackBtn;
    TextView carMake;
    TextView carModel;
    ImageView carLogo;
    ImageView carImage;
    ImageView changeImageBtn;
    ScrollView carInfo;
    LinearLayout carDetails;
    Button cancelBtn;
    Button saveBtn;
    Button addInfo;

    JSONObject newCarInfo;
    ArrayList<String> newInfos = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;



    private static int RESULT_LOAD_IMAGE = 1;
    private static int SELECT_PICTURE = 1;

    private static final String USER_ID = "userID";
    private static final String CAR_ID = "carID";

    Uri selectedImageURI;

    private int userID;
    private int carID;

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
            userID = getArguments().getInt(USER_ID);
            carID = getArguments().getInt(CAR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater singleItemList = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.fragment_editar_veiculo, container, false);
        // Inflate the layout for this fragment

        goBackBtn = v.findViewById(R.id.voltar);
        carMake = v.findViewById(R.id.Title_car);
        carModel = v.findViewById(R.id.SubTitle_car);
        carLogo = v.findViewById(R.id.Car_logo);
        carImage = v.findViewById(R.id.Car_image);
        changeImageBtn = v.findViewById(R.id.Change_Car_Image);
        carDetails = v.findViewById(R.id.scroll_car_info2);

        newCarInfo = new JSONObject();
        JSONArray finalArr = new JSONArray();

        addInfo = v.findViewById(R.id.Adicionar_caracteristica_on_edit);
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

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });


        GetCarInfoTask getCarInfoTask = new GetCarInfoTask();
        Cursor carInfo = getCarInfoTask.doInBackground(carID);

        while (carInfo.moveToNext()){
            int carID = Integer.parseInt(carInfo.getString(0));
            String make = carInfo.getString(1);
            String model =  carInfo.getString(2);
            String year =  carInfo.getString(3);
            String info = carInfo.getString(4);
            String imageURI = carInfo.getString(6);
            System.out.println(carInfo.getColumnNames());
            System.out.println(carInfo.toString());
            JSONArray infoArray = null;

            try {
                JSONObject jsonInfo = new JSONObject(info);
                infoArray = jsonInfo.getJSONArray("infos");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //carTitle.setText(make);
            carModel.setText(model + " (" + year + ")");
            int resourceID =  getResources().getIdentifier(make.toLowerCase().replace(" ","_").replace("-","_"), "drawable", getContext().getPackageName());
            System.out.println("\n\n\n\n\n\nResource ID: " + resourceID);
            carLogo.setImageResource(resourceID);
            System.out.println("Passo aqui");
            System.out.println(imageURI);
            Uri imageuri = Uri.parse(imageURI);
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(),imageuri);
                //bitmap = Bitmap.createScaledBitmap(bitmap, carImage.getMaxWidth(), carImage.getMaxHeight(), false);

                carImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //final String[] split = imageURI.split(":");//split the path.
            //String imgFilepath = split[1];
            //System.out.println(imgFilepath);


            for (int i=0 ; i < infoArray.length(); i++){

                View infoListView = singleItemList.inflate(R.layout.list_item_card, carDetails, false);

                try {
                    JSONArray singleInfo = (JSONArray) infoArray.get(i);
                    System.out.println(singleInfo.get(0));

                    TextView name = infoListView.findViewById(R.id.line1);
                    TextView value = infoListView.findViewById(R.id.line2);
                    name.setText(singleInfo.get(0).toString());
                    value.setText(singleInfo.get(1).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                carDetails.addView(infoListView);
            }

        }

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageURI = data.getData();
                final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;

                ContentResolver resolver = getActivity().getContentResolver();
                resolver.takePersistableUriPermission(selectedImageURI, takeFlags);

                System.out.println(selectedImageURI.toString());
            }
        }
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

    private class GetCarInfoTask extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... ids) {

            System.out.printf("GetCarInfoTask");
            Cursor results = db.getCarInfo(ids[0]);

            return results;
        }
    }

}