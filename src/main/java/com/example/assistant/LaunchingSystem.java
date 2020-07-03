package com.example.assistant;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import ai.api.model.Result;

public class LaunchingSystem {
    Context mContext;
    PackageManager pkmg;
    List<ApplicationInfo> packages;
    TextToSpeech tts;

    LaunchingSystem(Context context,TextToSpeech tts)
    {
        mContext = context;
        this.tts = tts;
        getPackageNames();
    }

    //TODO app launch code

    public void initialteLaunchingProcess(Result result1)
    {
        String appName = "";

        if (result1.getParameters() != null && !result1.getParameters().isEmpty()) {
            appName = result1.getStringParameter("app_name","none");
        }

        String apppackageName = findAppExists(appName);
        if(!apppackageName.equals("-1"))
        {
            if( !openApp(mContext,apppackageName))
            {
                tts.speak("not found should I search it on playStore", TextToSpeech.QUEUE_FLUSH,null,null);
            }else
            {
                tts.speak("launching",TextToSpeech.QUEUE_FLUSH,null,null);
            }
        }else {
            tts.speak("not found should I do something else for you",TextToSpeech.QUEUE_FLUSH,null,null);
        }
    }
    public String findAppExists(String name)
    {
        for(int i=0;i<packages.size();i++) {

            Log.d("tryApps",packages.get(i).name +"  " +packages.get(i).packageName);
            String tempAppPkgName = packages.get(i).packageName;
            String appName = pkmg.getApplicationLabel(packages.get(i)).toString();
            if(tempAppPkgName.substring(tempAppPkgName.lastIndexOf('.')+1,tempAppPkgName.length()).toLowerCase().equals(name.toLowerCase())) {
                return packages.get(i).packageName;
            }
            if(appName.toLowerCase().contains(name.toLowerCase()))
            {
                return packages.get(i).packageName;
            }
        }
        return "-1";
    }


    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new ActivityNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }


    public void getPackageNames()
    {
        pkmg = mContext.getPackageManager();
        packages = pkmg.getInstalledApplications(PackageManager.GET_META_DATA);
    }
}


