package com.example.projectcm.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    DatabaseHelper db;

    registerClickListener registerListener;


    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance() {
        Login fragment = new Login();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button b = v.findViewById(R.id.login);
        EditText et1 = v.findViewById(R.id.username);
        EditText et2 = v.findViewById(R.id.password);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et1.getText().toString();
                String password = et2.getText().toString();
                Bundle b = new Bundle();
                b.putString("email",email);
                b.putString("password",password);
                //verificar
                loginTask lt = new loginTask();
                try {
                    int userID = lt.execute(b).get();
                    if(userID != -1){
                        registerListener.changeToMainPage(userID);
                    }
                    else{
                        //mensagem de erro
                        Toast.makeText(getContext(), "Por favor insira um email e uma password válidas", Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });


        TextView tv = v.findViewById(R.id.toRegister);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enviar para o registo
                registerListener.changeToRegisterPage();
                System.out.println("Erro?");
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof registerClickListener){
            registerListener = (registerClickListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface registerClickListener {
        void changeToRegisterPage();
        void changeToMainPage(int userID);
    }

    /**
     *
     * Esta função recebe um Bundle com as seguintes keys:
     * 0 - email
     * 1 - password
     */
    private class loginTask extends AsyncTask<Bundle, Void, Integer> {

        @Override
        protected Integer doInBackground(Bundle... bundles) {

            int result = db.loginUser(bundles[0].getString("email"), bundles[0].getString("password"));

            return result;
        }
    }
}