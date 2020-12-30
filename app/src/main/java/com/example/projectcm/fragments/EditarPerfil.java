package com.example.projectcm.fragments;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.Notif;
import com.example.projectcm.R;

import java.sql.Date;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarPerfil extends Fragment {

    private static DatabaseHelper db;
    private OnEditarPerfilListener mListener;
    private static Integer UserID;
    ListView listView;
    ArrayList<Notif> notifs = new ArrayList<>();

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarPerfil.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarPerfil newInstance(Integer userid) {
        EditarPerfil fragment = new EditarPerfil();
        UserID = userid;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View PerfilEditView =inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        //add_note_to_db("Arranjar", "Corrigir problema no tubo de escape", "10/10/2021", 2);
        load_notif_to_arrayList();

        Cursor result = db.getUserFullInfo(UserID.toString());
        while (result.moveToNext()){
            String name=result.getString(0);
            String birthdate = result.getString(1);
            EditText edit1 = PerfilEditView.findViewById(R.id.textView10);
            EditText edit2 = PerfilEditView.findViewById(R.id.textView11);
            edit1.setText(name);
            edit2.setText(birthdate);

        }


        listView = PerfilEditView.findViewById(R.id.listView);
        listView.setLongClickable(true);
        // para aparecer o contextMenu
        registerForContextMenu(listView);
        //adicionar os títulos
        ArrayAdapter<Notif> adapter = new ArrayAdapter<Notif>(this.getActivity(), android.R.layout.simple_list_item_1,notifs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mListener.donothing();
            }
        });
        return PerfilEditView;
    }

    private void load_notif_to_arrayList() {
        Cursor resultado=db.getUserNotifications(UserID);
        while (resultado.moveToNext()){
            Notif notif_temp =  new Notif(Integer.parseInt(resultado.getString(0)),Integer.parseInt(resultado.getString(3)),resultado.getString(1),resultado.getString(2), resultado.getString(5),Integer.parseInt(resultado.getString(4)));
            notifs.add(notif_temp);
        }
    }
    private void add_note_to_db( String notiftitle, String notifbody, String notifdate, Integer carID){
        db.addNotif( UserID, notiftitle, notifbody,  notifdate, carID);
    }

    public interface OnEditarPerfilListener{
        void donothing();
    }
}