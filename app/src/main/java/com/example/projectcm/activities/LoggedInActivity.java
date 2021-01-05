package com.example.projectcm.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.projectcm.DatabaseHelper;
import com.example.projectcm.R;
import com.example.projectcm.fragments.AddVeiculo;
import com.example.projectcm.fragments.Detalhes;
import com.example.projectcm.fragments.EditarPerfil;
import com.example.projectcm.fragments.EditarVeiculo;
import com.example.projectcm.fragments.Login;
import com.example.projectcm.fragments.MainPage;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

public class LoggedInActivity extends AppCompatActivity implements MainPage.OnMainPageListener,EditarPerfil.OnEditarPerfilListener, Detalhes.DetailsClickListener,AddVeiculo.OnActionListener, EditarPerfil.EditarPerfilListener,EditarVeiculo.OnEditarVeiculoListener{

    int userID;
    private static MqttAndroidClient client;
    private static Integer uniqueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        super.onCreate(savedInstanceState);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(LoggedInActivity.this, "tcp://test.mosquitto.org:1883",clientId);
        String randomuniqueID = UUID.randomUUID().toString();
        uniqueID= randomuniqueID.hashCode();
        uniqueID = Integer.parseInt(uniqueID.toString().replace("-", ""));
        String topic = "CM2021-Autohub-"+uniqueID.toString();



        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        //String usedmail = intent.getStringExtra("email");
        Bundle b = intent.getExtras();
        userID = b.getInt("userID");

        setContentView(R.layout.activity_logged_in_);
        MainPage mainPage = MainPage.newInstance(userID);
        //AddVeiculo addVeiculo = AddVeiculo.newInstance("", "");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.loggedIn,mainPage,"mainpage");
        //fragmentTransaction.add(R.id.loggedIn, addVeiculo, "addveiculo");
        fragmentTransaction.commit();



        try {
            IMqttToken token = client.connect();

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d( "TAGMQTT", "onSuccess");
                    client.setCallback(new MqttCallbackExtended() {
                        @Override
                        public void connectComplete(boolean reconnect, String serverURI) {
                            simple_subscribe(topic);
                        }

                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            System.out.println(message.toString());
                            JSONObject paylaod = new JSONObject( message.toString());
                            AlertDialog.Builder alert = new AlertDialog.Builder(LoggedInActivity.this);
                            alert.setTitle("Recebeste uma partilha de:\n"+paylaod.getString("Sender"));
                            alert.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    JSONObject carrecievedpayload = new JSONObject();
                                    JSONArray notifsrecievedpayload = new JSONArray();
                                    String reciverdMake = "";
                                    String reciverdModel = "";
                                    String reciverdyear = "";
                                    String reciverdinfo = "";
                                    try {
                                        carrecievedpayload= paylaod.getJSONObject("Carro");
                                        reciverdMake = carrecievedpayload.getString("Marca");
                                        reciverdModel = carrecievedpayload.getString("Modelo");
                                        reciverdyear = carrecievedpayload.getString("Ano");
                                        reciverdinfo = carrecievedpayload.getString("Infor");
                                        notifsrecievedpayload= paylaod.getJSONArray("Notifs");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Bundle bundlecar = new Bundle();
                                    bundlecar.putString("make", reciverdMake);
                                    bundlecar.putString("model", reciverdModel);
                                    bundlecar.putString("year", reciverdyear);
                                    bundlecar.putString("info", reciverdinfo);
                                    bundlecar.putInt("ownerID", userID);
                                    bundlecar.putString("imageURI", "none");
                                    AddNewCarTask addNewCarTask = new AddNewCarTask();
                                    Integer lastcarID = addNewCarTask.doInBackground(bundlecar);
                                    String recievedtitle = "";
                                    String recievedbdy = "";
                                    String reccieveddate = "";

                                    for( int i=0 ; i<notifsrecievedpayload.length();i++){
                                        JSONObject notif = null;
                                        try {
                                            notif = new JSONObject(notifsrecievedpayload.getString(i));
                                            recievedtitle = notif.getString("Titulo");
                                            recievedbdy = notif.getString("Boddy");
                                            reccieveddate = notif.getString("Data");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Bundle bundlenotif = new Bundle();
                                        bundlenotif.putInt("userID", userID);
                                        bundlenotif.putString("notifTitle", recievedtitle);
                                        bundlenotif.putString("notifBody", recievedbdy);
                                        bundlenotif.putString("notifDate", reccieveddate);
                                        bundlenotif.putInt("carID",lastcarID);
                                        System.out.println("OI");
                                        AddNewNotifTask addnewnotiftask = new AddNewNotifTask();
                                        addnewnotiftask.doInBackground(bundlenotif);
                                    }


                                }
                            });

                            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Canceled.
                                    Toast.makeText(LoggedInActivity.this,"A partilha foi recusada",Toast.LENGTH_LONG).show();
                                }
                            });

                            alert.show();


                            Toast.makeText(LoggedInActivity.this,topic,Toast.LENGTH_LONG).show();

                        }
                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("TAGMQTT", "onFailure");


                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onMPImageInteraction(int userid) {
        //System.out.println("Cliquei na imagem !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        EditarPerfil editarPerfil = EditarPerfil.newInstance();
        Bundle args = new Bundle();
        args.putInt("userid", userid);
        editarPerfil.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loggedIn, editarPerfil,"editarperfil");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }

    @Override
    public void onMPAddButtonInteraction(int userid) {
        System.out.println("A ir para a adicionar carro");
        AddVeiculo addVeiculo = AddVeiculo.newInstance();
        Bundle args = new Bundle();
        args.putInt("userid", userid);
        addVeiculo.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loggedIn, addVeiculo,"addCar");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();


    }

    @Override
    public void onMPShareButtonInteraction(String topic, String payload) {
        try {
            MqttMessage message = new MqttMessage( payload.getBytes());
            client.publish(topic,message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMPDetailsButtonInteraction(int userID, int carID) {

        System.out.println("Cliquei no botao detalhes de 1 carro !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        Detalhes detalhes = Detalhes.newInstance(userID, carID);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loggedIn, detalhes,"detalhes");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();


    }

    @Override
    public String gettoken() {
        return uniqueID.toString();
    }

    @Override
    public void goToEditCarPage(int userID, int carID){
        EditarVeiculo editarVeiculo = EditarVeiculo.newInstance(userID, carID);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loggedIn, editarVeiculo,"editarcarro");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }
    @Override
    public void OnEPCancelClick(Integer UserID) {

    }

    @Override
    public void ONEPSaveClick(Integer UserID) {

    }

    @Override
    public void goToMainPage(int userID){
        MainPage mainPage = (MainPage) getSupportFragmentManager().findFragmentByTag("mainpage");
        getSupportFragmentManager().popBackStack();

    }

    public void simple_subscribe(String topictosub) {
        try {
            IMqttToken subToken = client.subscribe(topictosub,1);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(LoggedInActivity.this,"Estás a recebecer tópicos enviados para o teu código",Toast.LENGTH_LONG).show();


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Toast.makeText(LoggedInActivity.this,"fail",Toast.LENGTH_SHORT).show();
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            });
        } catch (
                MqttException e) {
            e.printStackTrace();
        }

    }

    private class AddNewCarTask extends AsyncTask<Bundle, Void, Integer> {

        @Override
        protected Integer doInBackground(Bundle... bundles) {
            DatabaseHelper db = new DatabaseHelper(LoggedInActivity.this);
            System.out.printf("AddNewCarTask");
            Integer result = (int) db.addACarToAUser(bundles[0].getString("make"), bundles[0].getString("model"), bundles[0].getString("year"), bundles[0].getString("info"), bundles[0].getInt("ownerID"), bundles[0].getString("imageURI"));

            return result;
        }
    }

    private class AddNewNotifTask extends AsyncTask<Bundle, Void, Integer> {

        @Override
        protected Integer doInBackground(Bundle... bundles) {
            DatabaseHelper db = new DatabaseHelper(LoggedInActivity.this);
            Integer result = (int) db.addNotif(bundles[0].getInt("userID"), bundles[0].getString("notifTitle"),bundles[0].getString("notifBody"), bundles[0].getString("notifDate"), bundles[0].getInt("carID"));

            return result;
        }
    }

}