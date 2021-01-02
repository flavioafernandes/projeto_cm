package com.example.projectcm.fragments;

import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarVeiculo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarVeiculo extends Fragment {

    DatabaseHelper db;
    OnEditarVeiculoListener listener;

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
    ListView addedInfos;

    JSONObject newCarInfo;
    ArrayList<String> newInfos = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    int carID;
    String make;
    String model;
    String year;
    String info;
    String imageURI;

    private static int RESULT_LOAD_IMAGE = 1;
    private static int SELECT_PICTURE = 1;

    private static final String USER_ID = "userID";
    private static final String CAR_ID = "carID";

    Uri selectedImageURI;

    private int userID;

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
        saveBtn = v.findViewById(R.id.Guardar_caracteristicas);
        cancelBtn = v.findViewById(R.id.cancelar);

        newCarInfo = new JSONObject();
        JSONArray finalArr = new JSONArray();
        //Adicionar informação
        addInfo = v.findViewById(R.id.Adicionar_caracteristica_on_edit);
        addInfo.setOnClickListener(v1 -> {

                View blur = (View) v.findViewById(R.id.blur_grey_square2);
            blur.setVisibility(View.VISIBLE);

            View add = (View) v.findViewById(R.id.add_caracteristica_grey_square2);
            add.setVisibility(View.VISIBLE);

            EditText nome = (EditText) v.findViewById(R.id.nome_caracteristica_adicionar2);
            nome.setVisibility(View.VISIBLE);

            EditText valor = (EditText) v.findViewById(R.id.valor_caracteristica_adicionar2);
            valor.setVisibility(View.VISIBLE);

            Button guardar = (Button) v.findViewById(R.id.Guardar_adicionar_caracteristica2);
            Button cancelar = (Button) v.findViewById(R.id.Cancelar_adicionar_caracteristica2);

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
                    LayoutInflater li = LayoutInflater.from(getContext());
                    View viewli = li.inflate(R.layout.list_item_edit_card, carDetails, false);
                    TextView nameCard = viewli.findViewById(R.id.line1);
                    EditText valueCard = viewli.findViewById(R.id.line2);
                    nameCard.setText(name);
                    valueCard.setText(value);

                    carDetails.addView(viewli);

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

        //mudar imagem
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

        //obter informações sobre o veiculo
        GetCarInfoTask getCarInfoTask = new GetCarInfoTask();
        Cursor carInfo = getCarInfoTask.doInBackground(carID);

        while (carInfo.moveToNext()){
            carID = Integer.parseInt(carInfo.getString(0));
            make = carInfo.getString(1);
            model =  carInfo.getString(2);
            year =  carInfo.getString(3);
            info = carInfo.getString(4);
            imageURI = carInfo.getString(6);
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
            selectedImageURI = imageuri;
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

                View infoListView = singleItemList.inflate(R.layout.list_item_edit_card, carDetails, false);

                try {
                    JSONArray singleInfo = (JSONArray) infoArray.get(i);
                    System.out.println(singleInfo.get(0));

                    TextView name = infoListView.findViewById(R.id.line1);
                    EditText value = infoListView.findViewById(R.id.line2);
                    name.setText(singleInfo.get(0).toString());
                    value.setText(singleInfo.get(1).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                carDetails.addView(infoListView);
            }

        }


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nDetails = carDetails.getChildCount();
                for(int i=0;i<nDetails;i++) {
                    System.out.println("\ni = ");
                    View card = carDetails.getChildAt(i);
                    TextView name = card.findViewById(R.id.line1);
                    EditText value = card.findViewById(R.id.line2);
                    try {
                        newCarInfo.put(name.getText().toString(),value.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(newCarInfo.toString());
                Bundle bundle = new Bundle();
                bundle.putString("make", make);
                bundle.putString("model", model);
                bundle.putString("year", year);
                bundle.putString("info", newCarInfo.toString());
                bundle.putInt("ownerID", userID);
                bundle.putString("imageURI", selectedImageURI.toString());

                System.out.println("OI");

                UpdateCarTask addNewCarTask = new UpdateCarTask();
                try {
                    addNewCarTask.execute(bundle).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Do i pass here? ");
                listener.goToMainPage(userID);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToMainPage(userID);
            }
        });

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

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), selectedImageURI);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, carImage.getMaxWidth(), carImage.getMaxHeight(), false);

                    carImage.setImageBitmap(bitmap);


                    System.out.println(selectedImageURI.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnEditarVeiculoListener){
            listener = (OnEditarVeiculoListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnEditarVeiculoListener{
        void goToMainPage(int userID);

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