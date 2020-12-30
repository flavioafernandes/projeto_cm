package com.example.projectcm.fragments;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.Event;
import com.example.projectcm.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarPerfil extends Fragment {

    DatabaseHelper db;
    int userid;
    ArrayList<Event> events = new ArrayList<>();

    public EditarPerfil() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EditarPerfil newInstance() {
        EditarPerfil fragment = new EditarPerfil();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();
            userid = b.getInt("userid");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        View alertView = getLayoutInflater().inflate(R.layout.add_event_layout, null);

        Button addEvent = v.findViewById(R.id.addToAgenda);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertView.getParent() != null) {
                    ((ViewGroup)alertView.getParent()).removeView(alertView); // <- fix
                }
                // criar popup com nome, calendário e descrição
                AlertDialog.Builder newEvent = new AlertDialog.Builder(v.getContext());
                newEvent.setTitle("Adicionar Evento");
                newEvent.setView(alertView);
                AlertDialog dialog = newEvent.show();


                Button confirmEvent = alertView.findViewById(R.id.AddEvent);
                CalendarView cv = alertView.findViewById(R.id.calendarView2);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                final String[] selectedDate = {""};
                selectedDate[0] = sdf.format(new Date());
                cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        selectedDate[0] = sdf.format(new Date(year,month,dayOfMonth));
                    }
                });
                EditText nameEvent = alertView.findViewById(R.id.NameOfEvent);
                EditText descriptionEvent = alertView.findViewById(R.id.DescriptionOfEvent);


                confirmEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = nameEvent.getText().toString();
                        String description = descriptionEvent.getText().toString();
                        Event temp = new Event(name,description,selectedDate[0]);
                        events.add(temp);
                        System.out.println("Evento adiconado");
                        dialog.dismiss();
                    }
                });
                Button cancelEvent = alertView.findViewById(R.id.CancelBunttonEvent);
                cancelEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });



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

            System.out.printf("GetAllNotfifs");
            Cursor result = db.getNotifList(userid[0]);

            return result;
        }
    }


    /**
     * Passar o notifID
     *
     * Dá return do número de linhas que apagou.
     * */
    private class DeleteNotifTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... notifID) {

            System.out.printf("DeleteNotifTask");
            Integer result = db.deleteNotif(notifID[0]);

            return result;
        }
    }


    /**
     * Passar o userID
     *
     * Dá return de todos os carros do utilizador.
     * */
    private class GetCarsFromUserTask extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... ids) {

            Cursor results = db.getCarsFromUser(ids[0]);

            return results;
        }
    }

}