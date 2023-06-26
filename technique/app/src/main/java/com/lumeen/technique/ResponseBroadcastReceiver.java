package com.lumeen.technique;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class ResponseBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "ResponseBroadcastReceiver";
    public static final String ACTION_RESPONSE = "com.lumeen.technique.ACTION_RESPONSE";
    private Context context; // instance Context

    public ResponseBroadcastReceiver(Context mContext) {
        this.context = mContext;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(ACTION_RESPONSE)) {
            String quote = intent.getStringExtra("quote");
            String author = intent.getStringExtra("author");
            int quoteId = intent.getIntExtra("id", -1);

            if (quoteId != -1) {
                //Traiter données reçus
                System.out.println("affichage des données reçus ： " + quoteId + quote + author);
                showDialog(quoteId,quote,author);
            } else {
                Log.e(TAG, "Invalid quote ID received");
            }
        }
    }

    private void showDialog(int id , String quote , String author){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quotes");
        builder.setMessage("ID: " + id + "\nQuote: " + quote  + "\nAuthor: " + author);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}













