package com.example.projectcm.fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarPerfil extends Fragment {

    DatabaseHelper db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarPerfil() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EditarPerfil newInstance(int userid) {
        EditarPerfil fragment = new EditarPerfil();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_perfil, container, false);
    }

    public interface OnEditarPerfilListener {
    }


    /**
     * Bundle:
     * 0 - userID
     * 1 - notifTitle
     * 2 - notifBody
     * 3 - notifDate
     * 4 - carID
     *
     * Dá return do ID da notificação
     * */
    private class AddNewNotifTask extends AsyncTask<Bundle, Void, Integer> {

        @Override
        protected Integer doInBackground(Bundle... bundles) {

            System.out.printf("AddNewNotifTask");
            Integer result = (int) db.addNotif(bundles[0].getInt("userID"), bundles[0].getString("notifTitle"),
                    bundles[0].getString("notifBody"), bundles[0].getString("notifDate"), bundles[0].getInt("carID"));

            return result;
        }
    }


    /**
     * Passar o userID
     *
     * Dá return de todas as notificações desse utilizador.
     * */
    private class GetAllNotfifs extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... userid) {

            System.out.printf("AddNewNotifTask");
            Cursor result = db.getNotifList(userid[0]);

            return result;
        }
    }
}