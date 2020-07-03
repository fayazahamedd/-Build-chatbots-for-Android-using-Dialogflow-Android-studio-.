package com.example.assistant;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import ai.api.model.Result;

//https://developer.android.com/training/location/retrieve-current
//implementation "com.google.android.gms:play-services-location:15.0.1"
public class Maps {
    Context mContext;
    TextToSpeech tts;
    String location;
    String to = "";
    String from = "";
    public Maps(Context mContext, TextToSpeech tts)
    {
        this.mContext = mContext;
        this.tts = tts;
        location = "";
    }



    public void initiateShowMapProcess(Result result1) {
        String uri = "geo:0,0?";
        if (result1.getParameters() != null && !result1.getParameters().isEmpty()) {
            JsonObject locationJson = result1.getComplexParameter("location");
            Log.d("tryQ",locationJson.toString());
            if(locationJson != null){

                //TODO get street address
                if(!locationJson.get("street-address").getAsString().equals(null)){
                    location +=" "+ locationJson.get("street-address").getAsString();
                }

                //TODO get business name
                if(!locationJson.get("business-name").getAsString().equals(null)){
                    location +=" "+ locationJson.get("business-name").getAsString();
                }

                //TODO get city
                if(!locationJson.get("city").getAsString().equals(null)){
                    location +=" "+ locationJson.get("city").getAsString();
                }

                //TODO get street address
                if(!locationJson.get("zip-code").getAsString().equals(null)){
                    location +=" "+ locationJson.get("zip-code").getAsString();
                }

                if(!locationJson.get("country").getAsString().equals(null)){
                    location +=" "+ locationJson.get("country").getAsString();
                }

                //TODO get admin area
                if(!locationJson.get("admin-area").getAsString().equals(null)){
                    location +=" "+ locationJson.get("admin-area").getAsString();
                }
                uri += "q=" + location;
                Log.d("tryQ",uri);
            }

        }
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.apps.maps");
        mContext.startActivity(intent);
    }

    public void initiateNavigationStartProcess(Result result1) {
        String newsSententce = "";
        if (result1.getParameters() != null && !result1.getParameters().isEmpty()) {
            JsonObject locationJson = result1.getComplexParameter("from");
            Log.d("tryQ",locationJson.toString());
            if(locationJson != null) {

                //TODO get street address
                if (!locationJson.get("street-address").getAsString().equals(null)) {
                    from += " " + locationJson.get("street-address").getAsString();
                }

                //TODO get business name
                if (!locationJson.get("business-name").getAsString().equals(null)) {
                    from += " " + locationJson.get("business-name").getAsString();
                }

                //TODO get city
                if (!locationJson.get("city").getAsString().equals(null)) {
                    from += " " + locationJson.get("city").getAsString();
                }

                //TODO get street address
                if (!locationJson.get("zip-code").getAsString().equals(null)) {
                    from += " " + locationJson.get("zip-code").getAsString();
                }

                if (!locationJson.get("country").getAsString().equals(null)) {
                    from += " " + locationJson.get("country").getAsString();
                }

                //TODO get admin area
                if (!locationJson.get("admin-area").getAsString().equals(null)) {
                    from += " " + locationJson.get("admin-area").getAsString();
                }
            }
            Log.d("tryLog",from+"   "+to);

            JsonObject locationJsonTo = result1.getComplexParameter("to");
            Log.d("tryQ",locationJsonTo.toString());
            if(locationJsonTo != null) {

                //TODO get street address
                if (!locationJsonTo.get("street-address").getAsString().equals(null)) {
                    to += " " + locationJsonTo.get("street-address").getAsString();
                }

                //TODO get business name
                if (!locationJsonTo.get("business-name").getAsString().equals(null)) {
                    to += " " + locationJsonTo.get("business-name").getAsString();
                }

                //TODO get city
                if (!locationJsonTo.get("city").getAsString().equals(null)) {
                    to += " " + locationJsonTo.get("city").getAsString();
                }

                //TODO get street address
                if (!locationJsonTo.get("zip-code").getAsString().equals(null)) {
                    to += " " + locationJsonTo.get("zip-code").getAsString();
                }

                if (!locationJsonTo.get("country").getAsString().equals(null)) {
                    to += " " + locationJsonTo.get("country").getAsString();
                }

                //TODO get admin area
                if (!locationJsonTo.get("admin-area").getAsString().equals(null)) {
                    to += " " + locationJsonTo.get("admin-area").getAsString();
                }
            }

            Log.d("tryLog",from+"   "+to);

            String uri = "";
            if(!to.equals("") && from.equals(""))
            {
                tts.speak("Navigation from your location to "+to,TextToSpeech.QUEUE_FLUSH,null);
                uri = "google.navigation:q="+to;

            }else if(!to.equals("-1") && !from.equals("-1")) {
                uri = "http://maps.google.com/maps?saddr="+from+"&daddr="+to;//,  "Malakwal", "Lahore";
            }
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(uri));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.apps.maps");
            mContext.startActivity(intent);

        }
    }


}

