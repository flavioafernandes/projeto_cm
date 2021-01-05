package com.example.projectcm.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPage extends Fragment {

    private static DatabaseHelper db;
    private OnMainPageListener mListener;
    private static String UserName;
    private  static  String email;
    static Integer currentUserID;
    String userpic;
    private String rootsubs= "CM2021-Autohub-";


    public MainPage() {
        // Required empty public constructor
    }


    public static MainPage newInstance(int userID) {
        MainPage fragment = new MainPage();
        currentUserID = userID;
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

        String token = mListener.gettoken();
        // Inflate the layout for this fragment
        final View MainPageView = inflater.inflate(R.layout.fragment_main_page, container, false);
        db = new DatabaseHelper(getContext());
        GetUserName getUserName = new GetUserName();
        Cursor resultado = getUserName.doInBackground(currentUserID.toString());
        while (resultado.moveToNext()){
            UserName=resultado.getString(0);
            userpic=resultado.getString(3);
        }

        LinearLayout gallery = MainPageView.findViewById(R.id.galery);
        LayoutInflater inflater1 = LayoutInflater.from(getContext());
        /*
        db.addACarToAUser("Opel","Corsa","2000","null",Integer.parseInt(UserID));
        db.addACarToAUser("Opel1","Corsa","2001","null",Integer.parseInt(UserID));
        db.addACarToAUser("Opel2","Corsa","2002","null",Integer.parseInt(UserID));

        Integer numbercars= 5;
        */
        GetCarsFromUserTask getCarsFromUserTask = new GetCarsFromUserTask();
        Cursor resultado1 = getCarsFromUserTask.doInBackground(currentUserID);


        //galery
        while (resultado1.moveToNext()){

            int carID = resultado1.getInt(0);
            String carmake = resultado1.getString(1);
            String carmodel= resultado1.getString(2);
            String carImage=resultado1.getString(6);
            View view = inflater1.inflate(R.layout.caritem, gallery,false);
            Button details = view.findViewById(R.id.button7);


            TextView textView1 = view.findViewById(R.id.textView4);
            textView1.setText(carmake);

            TextView textView2 = view.findViewById(R.id.textView5);
            textView2.setText(carmodel);

            ImageView ImageView1 = view.findViewById(R.id.imageView2);
            Uri imageuri = Uri.parse(carImage);

            System.out.println("IMAGE: -" + carImage + "-");
            if(!carImage.equals("none")){
                try {
                    Bitmap bitmapcar = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), imageuri);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, carImage.getMaxWidth(), carImage.getMaxHeight(), false);

                    ImageView1.setImageBitmap(bitmapcar);
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                ImageView1.setImageResource(R.drawable.car_default);
            }

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Cliquei no botao detalhes de 1 carro !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    //TODO: Passar aqui o carID
                    mListener.onMPDetailsButtonInteraction(currentUserID, carID);
                }
            });

            gallery.addView(view);

        }

        //for test is default avatar + random name
        TextView TVUserName = (TextView)  MainPageView.findViewById(R.id.textView);
        TVUserName.setText(UserName);

        TextView TVUserCode = (TextView)  MainPageView.findViewById(R.id.textView3);
        TVUserCode.setText("O meu código:  "+token);

        //Edit profile on avatar click
        ImageView IVAvatar = (ImageView) MainPageView.findViewById(R.id.imageView);
        if(!userpic.equals("")){
            Uri imageuri = Uri.parse(userpic);
            try {

                Bitmap bitmapavatar = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(),imageuri);
                //bitmap = Bitmap.createScaledBitmap(bitmap, carImage.getMaxWidth(), carImage.getMaxHeight(), false);

                    IVAvatar.setImageBitmap(bitmapavatar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            IVAvatar.setImageResource(R.drawable.avatar);
        }
        IVAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Cliquei na imagem !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                mListener.onMPImageInteraction(currentUserID);
            }
        });


        //Add button click
        Button BAdd = (Button) MainPageView.findViewById(R.id.addCarButton);
        BAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMPAddButtonInteraction(currentUserID);
            }
        });

        //Add button click
        View alertView = getLayoutInflater().inflate(R.layout.share_event_layout, null);
        Button BShare = (Button) MainPageView.findViewById(R.id.button2);
        if(gallery.getChildCount()<1){
            BShare.setEnabled(false);
        }
        else {
            BShare.setEnabled(true);
        }
        BShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertView.getParent() != null) {
                    ((ViewGroup) alertView.getParent()).removeView(alertView); // <- fix
                }
                JSONObject payload = new JSONObject();
                JSONObject carpayload = new JSONObject();
                JSONArray allnotifs = new JSONArray();
                GetCarsFromUserTask getCarsFromUserTask = new GetCarsFromUserTask();
                Cursor resultado3 = getCarsFromUserTask.doInBackground(currentUserID);
                String car2 = null;
                ArrayList<String> carslist = new ArrayList<String>();
                ArrayList<String> caridlist = new ArrayList<String>();
                while (resultado3.moveToNext()) {
                    Integer carid = resultado3.getInt(0);
                    String carmake2 = resultado3.getString(1);
                    String carmodel2 = resultado3.getString(2);
                    car2 = carmake2 + " " + carmodel2;
                    carslist.add(car2);
                    caridlist.add(carid.toString());
                }
                caridlist.toArray();
                String[] CarID = {""};

                android.app.AlertDialog.Builder newEvent = new android.app.AlertDialog.Builder(v.getContext());
                newEvent.setView(alertView);
                AlertDialog dialog = newEvent.show();

                Button confirmEvent = alertView.findViewById(R.id.ShareEvent);
                final Spinner[] spinner = {(Spinner) alertView.findViewById(R.id.spinner)};
                ArrayAdapter<String> adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,  carslist.toArray());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner[0].setAdapter(adapter);
                spinner[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Integer temp = position;

                        CarID[0] = caridlist.get(temp);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                EditText tokentext = alertView.findViewById(R.id.textView16);
                confirmEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Cursor resultado5 = getCarsFromUserTask.doInBackground(currentUserID);
                        while (resultado5.moveToNext()) {
                            if (resultado5.getString(0).equals(CarID[0])){

                                String payloadcarmake = resultado5.getString(1);
                                String payloadcarmodel = resultado5.getString(2);
                                String payloadusercaryear = resultado5.getString(3);
                                String payloadinfo = resultado5.getString(4);
                                try {
                                    carpayload.put("Marca", payloadcarmake);
                                    carpayload.put("Modelo", payloadcarmodel);
                                    carpayload.put("Ano", payloadusercaryear);
                                    carpayload.put("Infor", payloadinfo);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                        GetAllNotfifs getnotifs = new GetAllNotfifs();
                        Cursor resultado4 = getnotifs.doInBackground(currentUserID);
                        while (resultado4.moveToNext()) {
                            if (resultado4.getString(4).equals(CarID[0])){
                                JSONObject notifpayload = new JSONObject();
                                String payloadnotiftitle = resultado4.getString(1);
                                String payloadnotifbody = resultado4.getString(2);
                                String payloadnotifcar = resultado4.getString(4);
                                String payloadnotifdate = resultado4.getString(5);
                                try {
                                    notifpayload.put("Titulo", payloadnotiftitle);
                                    notifpayload.put("Boddy", payloadnotifbody);
                                    notifpayload.put("Carro", payloadnotifcar);
                                    notifpayload.put("Data", payloadnotifdate);
                                    allnotifs.put(notifpayload);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                        try {
                            payload.put("Sender",UserName);
                            payload.put("Carro", carpayload);
                            payload.put("Notifs", allnotifs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getContext(),rootsubs+tokentext.getText().toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(payload.toString());
                        mListener.onMPShareButtonInteraction(rootsubs+tokentext.getText().toString(),payload.toString());
                        dialog.dismiss();
                    }
                });
                Button voltarEvent = alertView.findViewById(R.id.VoltarBunttonEvent);
                voltarEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });



        return MainPageView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnMainPageListener){
            mListener = (OnMainPageListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private class GetUserName extends AsyncTask<String, Cursor, Cursor> {

        @Override
        protected Cursor doInBackground(String... strings) {
            Cursor results=db.getUserInfo(strings[0]);
            return results;
        }
    }

    private class GetCarsFromUserTask extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... ids) {

            Cursor results = db.getCarsFromUser(ids[0]);

            return results;
        }
    }

    private class GetAllNotfifs extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... userid) {

            System.out.printf("GetAllNotfifs");
            Cursor result = db.getNotifList(userid[0]);

            return result;
        }
    }

    public interface OnMainPageListener{

        void onMPImageInteraction(int userid);//open fragment for profile edit
        void onMPAddButtonInteraction(int userid);//open fragment add car
        void onMPShareButtonInteraction(String topic, String payload);//open fragment for share
        void onMPDetailsButtonInteraction(int userID, int carID);// open detalhes do carro

        String gettoken();
    }


}