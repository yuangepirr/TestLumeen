package com.lumeen.technique;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private EditText inputID;
    private Button getData;
    private ProgressBar progressBar;
    private final static String TAG = "MainActivity";
    private static final String ACTION_QUOTE = "com.lumeen.technique.ACTION_QUOTE";

    public static final String ACTION_RESPONSE = "com.lumeen.technique.ACTION_RESPONSE";
    private static final String EXTRA_QUOTE_ID = "com.lumeen.technique.EXTRA_QUOTE_ID";
    private SingletonData singletonData;
    private static int index = -1;

    private QuoteBroadcastReceiver quoteBroadcastReceiver;
    private ResponseBroadcastReceiver responseBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        //lier Text, Button, Input
        inputID = (EditText) findViewById(R.id.et_inputID);
        progressBar = findViewById(R.id.processBar);
        getData = findViewById(R.id.btn_getID);

        //Initialiser Data
        singletonData = SingletonData.getInstance();


        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag = 0;
                String id = inputID.getText().toString().trim();
                if (!TextUtils.isEmpty(id) && TextUtils.isDigitsOnly(id)){
                    int inputId = Integer.parseInt(id);
                    initData(inputId);
                    flag = 1;
                }else {
                    showToast("Resaisir ID correcte");
                }
//                Intent intent = new Intent("com.lumeen.technique.ACTION_QUOTE");
//                sendBroadcast(intent);
                if (flag == 1){
//                    Intent intent = new Intent(ACTION_QUOTE);
//                    localBroadcastManager.sendBroadcast(intent);
                    int inputId = Integer.parseInt(id);

                }
            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_QUOTE);
        intentFilter.addAction(EXTRA_QUOTE_ID);

        quoteBroadcastReceiver = new QuoteBroadcastReceiver();
        responseBroadcastReceiver = new ResponseBroadcastReceiver(this);
        localBroadcastManager.registerReceiver(responseBroadcastReceiver,new IntentFilter(ACTION_RESPONSE));

        localBroadcastManager.registerReceiver(quoteBroadcastReceiver , intentFilter);
    }

    private void sendQuoteRequest(int quoteId){
        Intent quoteIntent = new Intent(ACTION_QUOTE);
        quoteIntent.putExtra(EXTRA_QUOTE_ID, quoteId);
        localBroadcastManager.sendBroadcast(quoteIntent);
    }

    public void getResponseData(){
        Intent quoteIntent = new Intent(ACTION_RESPONSE);
        localBroadcastManager.sendBroadcast(quoteIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(responseBroadcastReceiver);
        localBroadcastManager.unregisterReceiver(quoteBroadcastReceiver);
    }

    private void initData(int id){


        //Demande Data avec OkHttp
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url("http://192.168.154.53:8080/atguigu/json/quotes.json")
//                        .build();

                Request request = new Request.Builder()
                        .url("https://dummyjson.com/quotes")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("Data fetched error");
                                Log.e("MainActivity" , e.toString());
                            }
                        }).start();
                    }

                    @Override
                    public void onResponse( Call call,  Response response) throws IOException {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("Data fetched successfully");
                                try {
                                    singletonData.setQuotes(response.body().string());
                                    //processData(response.body().string() , id);
                                    sendQuoteRequest(id);
                                    getResponseData();
                                } catch (JSONException | IOException e) {
                                    throw new RuntimeException(e);
                                }finally {
                                    //envoyer quote ID nécessaire

                                }
                            }
                        }).start();
                    }
                });
            }
        }).start();

    }




    private boolean isToastShowing = false; // indication d'affichage de Toast
    private void showToast(String showText){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isToastShowing) { // sinon, il faut afficher Toast
                    Toast.makeText(MainActivity.this, showText, Toast.LENGTH_SHORT).show();
                    isToastShowing = true; // mise à jour l'état de l'indication, true = Toast affiché
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isToastShowing = false; //Après le délai d'affichage, mise à jour l'état, affichage de Toast est terminé
                        }
                    }, 2000); // délai = 2s
                }
            }
        });
    }
}
