package com.example.projectcm.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPage extends Fragment {

    private static DatabaseHelper db;
    private OnMainPageListener mListener;
    private static String UserName;
    private static String UserID;
    private  static  String email;
    static Integer currentUserID;

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
        db = new DatabaseHelper(getContext());
        GetUserName getUserName = new GetUserName();
        Cursor resultado = getUserName.doInBackground(currentUserID.toString());
        while (resultado.moveToNext()){
            UserName=resultado.getString(0);
        }
        System.out.println("Username "+UserName+" e  ID " + currentUserID +"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //UserName = "Ambrósio Ferrero";

        //Load "Os meus veículos"
        //get carid usercarmake usercarmodel


        //Transaction for Adicionar
        //Transaction for Partilhar


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View MainPageView = inflater.inflate(R.layout.fragment_main_page, container, false);

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

        while (resultado1.moveToNext()){

            int carID = resultado1.getInt(0);
            String carmake = resultado1.getString(1);
            String carmodel= resultado1.getString(2);

            View view = inflater1.inflate(R.layout.caritem, gallery,false);

            TextView textView1 = view.findViewById(R.id.textView4);
            textView1.setText(carmake);

            TextView textView2 = view.findViewById(R.id.textView5);
            textView2.setText(carmodel);

            ImageView ImageView1 = view.findViewById(R.id.imageView2);
            ImageView1.setImageResource(R.drawable.avatar);

            Button details = view.findViewById(R.id.button7);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Cliquei no botao detalhes de 1 carro !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    //TODO: Passar aqui o carID
                    mListener.onMPDetailsButtonInteraction();
                }
            });

            gallery.addView(view);

        }

        /*
        for (int i =0; i <numbercars; i++){
            View view = inflater1.inflate(R.layout.caritem,gallery,false);

            TextView textView1 = view.findViewById(R.id.textView4);
            textView1.setText("Marca ");

            TextView textView2 = view.findViewById(R.id.textView5);
            textView2.setText("Modelo ");

            ImageView ImageView1 = view.findViewById(R.id.imageView2);
            ImageView1.setImageResource(R.drawable.avatar);

            gallery.addView(view);
        }
        */
        //for test is default avatar + random name
        TextView TVUserName = (TextView)  MainPageView.findViewById(R.id.textView);
        TVUserName.setText(UserName);

        //Edit profile on avatar click
        ImageView IVAvatar = (ImageView) MainPageView.findViewById(R.id.imageView);
        IVAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Cliquei na imagem !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                mListener.onMPImageInteraction(currentUserID);
            }
        });

        /*
        //Add button click
        Button BAdd = (Button) MainPageView.findViewById(R.id.button);
        BAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMPAddButtonInteraction(Integer.parseInt(UserID));
            }
        });*/
        //Add button click
        Button BShare = (Button) MainPageView.findViewById(R.id.button2);
        BShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMPShareButtonInteraction();
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
    public interface OnMainPageListener{

        void onMPImageInteraction(int userid);//open fragment for profile edit
        void onMPAddButtonInteraction(int userid);//open fragment add car
        void onMPShareButtonInteraction();//open fragment for share
        void onMPDetailsButtonInteraction();// open detalhes do carro
    }


}