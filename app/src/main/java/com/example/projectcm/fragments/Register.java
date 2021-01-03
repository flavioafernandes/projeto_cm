package com.example.projectcm.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {

    DatabaseHelper db;
    ListenerToLogin ltl;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance() {
        Register fragment = new Register();
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
        View v  = inflater.inflate(R.layout.fragment_register, container, false);
        EditText name = v.findViewById(R.id.newName);
        EditText email = v.findViewById(R.id.newEmail);
        EditText password = v.findViewById(R.id.newpassword);
        EditText confirmPassword = v.findViewById(R.id.confirmNewPassword);
        TextView tvError = v.findViewById(R.id.errorDisplayRegister);
        Button b = v.findViewById(R.id.confirmRegister);
        CalendarView cv = v.findViewById(R.id.calendarView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final String[] selectedDate = {""};
        selectedDate[0] = sdf.format(new Date());
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month = month + 1;

                selectedDate[0] = dayOfMonth + "/" + month + "/" + year;
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isEmailValid(email.getText().toString())){
                    tvError.setText("Formato de e-mail inválido.");
                    return;
                }

                if(password.getText().toString().compareTo(confirmPassword.getText().toString())!=0){
                    // se as palavras pass forem diferentes aparecer erro
                    tvError.setText("Passwords são diferentes.");
                }
                //verificar se o email existe

                else{
                    //guardar na base de dados
                    Bundle info = new Bundle();
                    info.putString("name",name.getText().toString());
                    info.putString("email",email.getText().toString());
                    info.putString("birth", selectedDate[0]);
                    info.putString("password",password.getText().toString());
                    registTask rt = new registTask();
                    try {
                        int result = rt.execute(info).get();
                        if(result == -1){
                            //email existe erro
                            tvError.setText("Email já existe no sistema");
                        }
                        else {
                            ltl.backToLogin();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //ir para o login page
                }

            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Register.ListenerToLogin){
            ltl = (Register.ListenerToLogin) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface ListenerToLogin {
        void backToLogin();
    }


    /**
     *
     * Esta função recebe um Bundle com as seguintes keys:
     * 0 - name
     * 1 - email
     * 2 - birth
     * 3 - password
     */
    private class registTask extends AsyncTask<Bundle, Void, Integer> {

        @Override
        protected Integer doInBackground(Bundle... bundles) {

            int id = (int) db.addNewUser(bundles[0].getString("name"), bundles[0].getString("email"), bundles[0].getString("birth"), bundles[0].getString("password"));

            return id;
        }
    }

}