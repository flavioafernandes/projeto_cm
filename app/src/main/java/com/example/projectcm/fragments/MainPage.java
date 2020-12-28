package com.example.projectcm.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projectcm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPage extends Fragment {


    private OnMainPageListener mListener;
    String UserName;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPage.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPage newInstance() {
        MainPage fragment = new MainPage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Load from db user name
        //Load avatar /photo/image WIP
        UserName = "Ambrósio Ferrero";

        //function to get name from DB based on ?? login?



        //Load "Os meus veículos"

        //Creater slider with loaded vehicles

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

        Integer numbercars= 6;

        for (int i =0; i <numbercars; i++){
            View view = inflater1.inflate(R.layout.caritem,gallery,false);

            TextView textView1 = view.findViewById(R.id.textView4);
            textView1.setText("Marca ");

            TextView textView2 = view.findViewById(R.id.textView5);
            textView2.setText("Modelo ");

            ImageView ImageView1 = view.findViewById(R.id.imageView2);
            ImageView1.setImageResource(R.drawable.avatar);
            //Details button click
            Button Bdetails = (Button) MainPageView.findViewById(R.id.button);
            Bdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMPGDetailsButtonInteraction();
                }
            });
            gallery.addView(view);
        }

        //for test is default avatar + random name
        TextView TVUserName = (TextView)  MainPageView.findViewById(R.id.textView);
        TVUserName.setText(UserName);

        //Edit profile on avatar click
        ImageView IVAvatar = (ImageView) MainPageView.findViewById(R.id.imageView);
        IVAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMPImageInteraction();
            }
        });

        //Add button click
        Button BAdd = (Button) MainPageView.findViewById(R.id.button);
        BAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMPAddButtonInteraction();
            }
        });
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
    public interface OnMainPageListener{

        void onMPImageInteraction();//open fragment for profile edit
        void onMPAddButtonInteraction();//open fragment add car
        void onMPShareButtonInteraction();//open fragment for share
        void onMPGDetailsButtonInteraction();// open detalhes do carro
    }
}