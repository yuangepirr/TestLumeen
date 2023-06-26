package com.lumeen.technique;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class QuoteBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "QuoteBroadcastReceiver";
    public static final String ACTION_QUOTE = "com.lumeen.technique.ACTION_QUOTE";
    public static final String ACTION_RESPONSE = "com.lumeen.technique.ACTION_RESPONSE";
    public static final String EXTRA_QUOTE_ID = "com.lumeen.technique.EXTRA_QUOTE_ID";

    private SingletonData singletonData;

    @Override
    public void onReceive(Context context, Intent intent) {
        singletonData = SingletonData.getInstance();

        //recevoir Broadcast et traiter
        if (intent != null && intent.getAction() != null && intent.getAction().equals(ACTION_QUOTE)){
            int quoteId = intent.getIntExtra(EXTRA_QUOTE_ID , -1);
            //vérifier quote ID est correcte
            if (quoteId != -1){
                Quote quotes = singletonData.getQuotes(quoteId);
                sendResponse(context,quotes,quoteId);
                System.out.println("quoteId"+ quoteId);
                Log.e(TAG, "Broadcast bien reçu");
            }else {
                Log.e(TAG, "quoteId inutile");
            }
        }else
            Log.e(TAG, "erreur");
    }

    //envoyer le réponse de Broadcast
    private void sendResponse(Context mContext , Quote quotes , int quoteId){
        Intent responseIntent = new Intent(ACTION_RESPONSE);
        responseIntent.putExtra("quote" , quotes.getQuote());
        responseIntent.putExtra("author" , quotes.getAuthor());
        responseIntent.putExtra("id" , quoteId);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(responseIntent);
    }
}



















