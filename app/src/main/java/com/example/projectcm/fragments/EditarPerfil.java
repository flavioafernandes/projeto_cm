package com.example.projectcm.fragments;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.Event;
import com.example.projectcm.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarPerfil extends Fragment {

    DatabaseHelper db;
    Integer userid;
    ArrayList<Event> events = new ArrayList<>();
    String CarID ;
    private OnEditarPerfilListener mListener;

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
        View alertView2 = getLayoutInflater().inflate(R.layout.add_event_layout2, null);
        View alertView3 = getLayoutInflater().inflate(R.layout.add_event_layout3, null);
        TextView textView4 = v.findViewById(R.id.InformationToEdit);
        LinearLayout gallery2 = v.findViewById(R.id.galery2);


        GetUserName getUserName = new GetUserName();
        Cursor resultado = getUserName.doInBackground(userid.toString());
        while (resultado.moveToNext()) {
            textView4.setText(resultado.getString(0));
        }
        //getUserCars
        GetCarsFromUserTask getCarsFromUserTask = new GetCarsFromUserTask();
        Cursor resultado3 = getCarsFromUserTask.doInBackground(userid);
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

        Button addEvent = v.findViewById(R.id.addToAgenda);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertView.getParent() != null) {
                    ((ViewGroup) alertView.getParent()).removeView(alertView); // <- fix
                }
                if(carslist.isEmpty()){
                    Toast.makeText(getActivity(),"Nao tens carros associados", Toast.LENGTH_LONG).show();
                    return;
                }

                // criar popup com nome, calendário e descrição
                AlertDialog.Builder newEvent = new AlertDialog.Builder(v.getContext());
                newEvent.setTitle("Adicionar Evento");
                newEvent.setView(alertView);
                AlertDialog dialog = newEvent.show();

                //Spinner selectCar = alertView.findViewById(R.id.spinnerSelectCar);

                //adicionar carros

                //selectCar.setAdapter();

                Button confirmEvent = alertView.findViewById(R.id.NextEvent);
                CalendarView cv = alertView.findViewById(R.id.calendarView2);
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
                EditText nameEvent = alertView.findViewById(R.id.NameOfEvent);



                confirmEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){

                        if (alertView2.getParent() != null) {
                            ((ViewGroup) alertView2.getParent()).removeView(alertView2); // <- fix
                        }
                        //passa ao resto do "alert"
                        AlertDialog.Builder newEvent2 = new AlertDialog.Builder(v.getContext());
                        newEvent2.setTitle("Adicionar Evento");
                        newEvent2.setView(alertView2);
                        AlertDialog dialog2 = newEvent2.show();
                        EditText descriptionEvent = alertView2.findViewById(R.id.DescriptionOfEvent);
                        Button confirmEvent2 = alertView2.findViewById(R.id.AddEvent);


                        Spinner spinner = (Spinner) alertView2.findViewById(R.id.spinner1);
                        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,  carslist.toArray());
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Integer temp = position;
                                CarID = caridlist.get(temp);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });



                        confirmEvent2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                String title = nameEvent.getText().toString();
                                String description = descriptionEvent.getText().toString();
                                AddNewNotifTask addnewnotiftask = new AddNewNotifTask();
                                Bundle b = new Bundle();
                                b.putInt("userID", userid);
                                b.putString("notifTitle", title);
                                b.putString("notifBody", description);
                                b.putString("notifDate", selectedDate[0]);

                                b.putInt("carID",Integer.parseInt(CarID));
                                System.out.println("Notificação com : "+userid.toString()+" +"+CarID+"+" + title + "+" + description + "+" + selectedDate[0] + "+" + CarID + "+!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                addnewnotiftask.doInBackground(b);
                                System.out.println("Evento adiconado");

                                //Adicionar à lista
                                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                                View view = inflater1.inflate(R.layout.notification_item, gallery2, false);

                                GetCarModel getCarModel = new GetCarModel();
                                Cursor resultado2 = getCarModel.doInBackground(Integer.parseInt(CarID));

                                String car = null;
                                while (resultado2.moveToNext()) {
                                    String carmake = resultado2.getString(1);
                                    String carmodel = resultado2.getString(2);
                                    car = carmake + " " + carmodel;
                                }

                                TextView textView1 = view.findViewById(R.id.textView8);
                                textView1.setText(title);

                                TextView textView2 = view.findViewById(R.id.textView12);
                                textView2.setText(car);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date today = java.util.Calendar.getInstance().getTime();
                                Date date = null;
                                try {
                                    date = sdf.parse(selectedDate[0]);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (!today.after(date)) {
                                    long diff = date.getTime() - today.getTime();
                                    Long days = diff / (24 * 60 * 60 * 1000);

                                    TextView textView3 = view.findViewById(R.id.textView10);
                                    textView3.setText(days.toString() + " Dias");
                                }else{
                                    TextView textView3 = view.findViewById(R.id.textView10);
                                    textView3.setText("Atrasado");
                                }

                                gallery2.addView(view);
                                dialog2.dismiss();
                                dialog.dismiss();


                            }

                        });
                        Button voltarEvent = alertView2.findViewById(R.id.VoltarBunttonEvent);
                        voltarEvent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
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


        LayoutInflater inflater1 = LayoutInflater.from(getContext());

        GetAllNotfifs getallnotifs = new GetAllNotfifs();
        Cursor resultado1 = getallnotifs.doInBackground(userid);

        while (resultado1.moveToNext()) {
            View view = inflater1.inflate(R.layout.notification_item, gallery2, false);

            String title = resultado1.getString(1);
            Integer carid = resultado1.getInt(4);
            String datenotif = resultado1.getString(5);
            //String descriptnotif = resultado1.getString(5);
            GetCarModel getCarModel = new GetCarModel();
            Cursor resultado2 = getCarModel.doInBackground(carid);

            String car = null;
            while (resultado2.moveToNext()) {
                String carmake = resultado2.getString(1);
                String carmodel = resultado2.getString(2);
                car = carmake + " " + carmodel;
            }

            System.out.println("Tem: " + title + "+" + carid.toString() + "+" + car + "+" + datenotif + "+!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


            TextView textView1 = view.findViewById(R.id.textView8);
            textView1.setText(title);

            TextView textView2 = view.findViewById(R.id.textView12);
            textView2.setText(car);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date today = java.util.Calendar.getInstance().getTime();
            Date date = null;
            try {
                date = sdf.parse(datenotif);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!today.after(date)) {
                long diff = date.getTime() - today.getTime();
                Long days = diff / (24 * 60 * 60 * 1000);

                TextView textView3 = view.findViewById(R.id.textView10);
                textView3.setText(days.toString() + " Dias");
            }else{
                TextView textView3 = view.findViewById(R.id.textView10);
                textView3.setText("Atrasado");
            }
            //remove if break till end
            /*
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertView.getParent() != null) {
                        ((ViewGroup) alertView.getParent()).removeView(alertView); // <- fix
                    }
                    // criar popup com nome, calendário e descrição
                    AlertDialog.Builder newEvent = new AlertDialog.Builder(v.getContext());
                    newEvent.setTitle("Adicionar Evento");
                    newEvent.setView(alertView);
                    AlertDialog dialog = newEvent.show();
                    EditText edittitulo = alertView3.findViewById(R.id.textView17);
                    EditText editdata = alertView3.findViewById(R.id.textView18);
                    EditText editdescrição = alertView3.findViewById(R.id.textView16);
                    edittitulo.setText(title);
                    editdata.setText(datenotif);
                    editdescrição.setText(descriptnotif);


                }
            });*/
            gallery2.addView(view);

        }
        return v;
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

            Integer result = (int) db.addNotif(bundles[0].getInt("userID"), bundles[0].getString("notifTitle"),bundles[0].getString("notifBody"), bundles[0].getString("notifDate"), bundles[0].getInt("carID"));

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


    private class GetUserName extends AsyncTask<String, Cursor, Cursor> {

        @Override
        protected Cursor doInBackground(String... strings) {
            Cursor results=db.getUserInfo(strings[0]);
            return results;
        }
    }

    private class GetCarModel extends AsyncTask<Integer, Cursor,Cursor> {

        @Override
        protected Cursor doInBackground(Integer... integers) {
            Cursor results=db.getCarInfo(integers[0]);
            return results;
        }
    }


    public interface OnEditarPerfilListener {

        void OnEPCancelClick(Integer UserID);

        void ONEPSaveClick(Integer UserID);
    }
}