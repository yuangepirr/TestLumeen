package com.lumeen.technique;

import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;


public class SingletonData {

    private static SingletonData instance;
    private List<Quote> list_quotes;
    private final static String TAG = "SingletonData";

    //construire fonction, initialiser la list
    public SingletonData(){
        list_quotes = new ArrayList<>();
    }

    //Réaliser l'instance en modèle Singleton
    public static SingletonData getInstance(){
        if (instance == null){
            instance = new SingletonData();
        }
        return instance;
    }

    //
    public Quote getQuotes(int index){
        if (index >= 0 && index < list_quotes.size()) {
            return list_quotes.get(index);
        }
        return null;
    }

    public void setQuotes(String jsonData) throws JSONException, IOException {
        System.out.println("Bien reçu jsonData, commencer à traiter .");
        processData(jsonData);
    }

    private void processData(String response) throws JSONException, IOException {
        //commencer à traiter jsonData retourné
        try{
            JSONObject jsonData = new JSONObject(response);
            JSONArray quotes = jsonData.getJSONArray("quotes");
            int getId = -1;
            String quote = null;
            String author = null;
            Log.e(TAG , String.valueOf(quotes));

            //préparer la structure de donnée pour les enregistrer
            for (int i = 0 ; i < quotes.length() ; i++){
                JSONObject quotesObject = quotes.getJSONObject(i);

                getId = quotesObject.getInt("id");
                quote = quotesObject.getString("quote");
                author = quotesObject.getString("author");

                Quote quoteItem = new Quote(quote, author);
                list_quotes.add(quoteItem);
            }

            //showData();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public void showData(){
        for (int i = 0 ; i < list_quotes.size() ; i++){
            System.out.println(list_quotes.get(i).getQuote() + " " + list_quotes.get(i).getAuthor());
        }
    }
}

















