package com.example.assistant;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import ai.api.model.Result;


public class Call {
    ArrayList<ContactsDataModel> matchingNumbers = new ArrayList<>();
    Context mContext;
    TextToSpeech tts;

    public Call(Context context, TextToSpeech tts) {
        this.mContext = context;
        this.tts = tts;
    }

    public void initiateCallProcess(Result result1) {
        String name = result1.getStringParameter("contact","none");
        if(name.endsWith(".")){
            name = name.substring(0,name.length()-1);
        }
        Toast.makeText(mContext, ""+name, Toast.LENGTH_SHORT).show();
        if(!name.equals("none")) {
            makeCall(name);
        }else
        {
            tts.speak("To whom should I call", TextToSpeech.QUEUE_FLUSH, null,null);
        }
    }

    public void makeCall(String conName) {
        findByName(mContext,conName);
        if (matchingNumbers.size() <= 0) {
            tts.speak("you have no contact with such name", TextToSpeech.QUEUE_FLUSH, null,null);
            openContactsApp();
        } else if (matchingNumbers.size() >= 1) {
            dial(matchingNumbers.get(0).getNo());
        }
    }

    void openContactsApp()
    {
        Intent i = new Intent(Intent.ACTION_VIEW, CallLog.Calls.CONTENT_URI);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    public void dial(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    public void findByName(Context context , String name) {
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        Log.d("tryPara",selection);
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            matchingNumbers.add(new ContactsDataModel(c.getString(1),c.getString(0)));
            while(c.moveToNext()) {
                matchingNumbers.add(new ContactsDataModel(c.getString(1),c.getString(0)));
            }
        }
        c.close();
    }

    class ContactsDataModel{
        String name;
        String no;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public ContactsDataModel(String name, String no) {
            this.name = name;
            this.no = no;
        }
    }

}


