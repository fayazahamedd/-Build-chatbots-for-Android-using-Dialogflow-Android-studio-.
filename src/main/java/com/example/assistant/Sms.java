package com.example.assistant;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ai.api.model.Result;

public class Sms {
    ArrayList<ContactsDataModel> matchingNumbers = new ArrayList<ContactsDataModel>();
    Context mContext;
    TextToSpeech tts;

    public Sms(Context context, TextToSpeech tts) {
        this.mContext = context;
        this.tts = tts;
    }

    public void initiateSmsProcess(Result result1) {
        String name = result1.getStringParameter("contact","none");
        String text = result1.getStringParameter("text","none");
        if(name.endsWith(".")){
            name = name.substring(0,name.length()-1);
        }
        Toast.makeText(mContext, ""+name, Toast.LENGTH_SHORT).show();
        if(!name.equals("none") && !text.equals("none")) {
            sendSms(name,text);
        }
    }

    public void sendSms(String conName,String text) {
        findByName(mContext,conName);
        if (matchingNumbers.size() <= 0) {
            tts.speak("you have no contact with such name", TextToSpeech.QUEUE_FLUSH, null,null);
            openMessagingApp();
        } else if (matchingNumbers.size() >= 1) {
            send(matchingNumbers.get(0).getNo(),text);
        }
    }

    void openMessagingApp()
    {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(sendIntent);
    }

    public void send(String number,String text) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+number));
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.putExtra("sms_body", text);
        mContext.startActivity(sendIntent);
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


