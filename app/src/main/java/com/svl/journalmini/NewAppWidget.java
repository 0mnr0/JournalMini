package com.svl.journalmini;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    static String Access_Token = "";
    static String UserName;
    static String UserPassword;
    static String LastSheduleTime;
    static String Shedule="";
    int MonetColorText=0;
    int MonetColorTextLite=0;



    @SuppressLint("ResourceType")
    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);


        if (UserName.isEmpty() || UserPassword.isEmpty()){
            views.setTextViewText(R.id.LessonFirst, "Войдите в аккаунт");
        } else {
            try {
                JSONArray jsonArray = new JSONArray(Shedule);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject;
                    jsonObject = jsonArray.getJSONObject(i);
                    String subjectName = jsonObject.getString("subject_name");
                    String startTime = jsonObject.getString("started_at");
                    switch (i) {
                        case 0:
                            views.setTextViewText(R.id.LessonFirst, subjectName);
                            views.setTextColor(R.id.LessonFirst, MonetColorText);
                            views.setTextViewText(R.id.FirstLessonTimeID, startTime);
                            views.setTextColor(R.id.FirstLessonTimeID, MonetColorTextLite);
                            break;
                        case 1:
                            views.setTextViewText(R.id.LessonSecond, subjectName);
                            views.setTextColor(R.id.LessonSecond, MonetColorText);
                            views.setTextViewText(R.id.SecondLessonTimeID, startTime);
                            views.setTextColor(R.id.SecondLessonTimeID, MonetColorTextLite);
                            break;
                        case 2:
                            views.setTextViewText(R.id.LessonThird, subjectName);
                            views.setTextColor(R.id.LessonThird, MonetColorText);
                            views.setTextViewText(R.id.ThirdLessonTimeID, startTime);
                            views.setTextColor(R.id.ThirdLessonTimeID, MonetColorTextLite);
                            break;
                        case 3:
                            views.setTextViewText(R.id.LessonFourth, subjectName);
                            views.setTextColor(R.id.LessonFourth, MonetColorText);
                            views.setTextViewText(R.id.FourthLessonTimeID, startTime);
                            views.setTextColor(R.id.FourthLessonTimeID, MonetColorTextLite);
                            break;
                    }
                }
                String Time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" +Calendar.getInstance().get(Calendar.SECOND);
                views.setTextViewText(R.id.LastTimeUpdated, Time);
            } catch (JSONException e) {
                String Time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" +Calendar.getInstance().get(Calendar.SECOND);
                views.setTextViewText(R.id.LastTimeUpdated, Time);
                views.setTextViewText(R.id.LessonFirst, UserName+", "+UserPassword);
                views.setTextViewText(R.id.LessonSecond, String.valueOf(e));
                views.setTextViewText(R.id.LessonThird, Shedule);
                views.setTextViewText(R.id.LessonFourth, Access_Token);
            }
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    public static void sendData(String URL, String FETCH_TYPE, JSONObject JSON, String Access_Token, boolean UseCache) {
        SendDataTask sendData = new SendDataTask(URL, FETCH_TYPE, JSON, Access_Token, NewAppWidget::onTaskCompleted, UseCache);
        sendData.execute();
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences prefs = context.getSharedPreferences("com.svl.journalmini", MODE_PRIVATE);
        UserName= prefs.getString("LoginData", "null");
        UserPassword= prefs.getString("PassData", "null");
        Access_Token = prefs.getString("LastSucsessfulAccessToken", "");


        MonetColorText = context.getResources().getColor(android.R.color.system_accent1_600);
        MonetColorTextLite = context.getResources().getColor(android.R.color.system_accent1_500);


        int Year = Calendar.getInstance().get(Calendar.YEAR);
        int Month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int Day = Calendar.getInstance().get(Calendar.DATE);
        LastSheduleTime="https://msapi.top-academy.ru/api/v2/schedule/operations/get-by-date?date_filter="+Year+"-"+Month+"-"+Day;
        JSONObject JSON = new JSONObject();
        try {
            JSON.put("application_key", "6a56a5df2667e65aab73ce76d1dd737f7d1faef9c52e8b8c55ac75f565d8e8a6");
            JSON.put("id_city", JSONObject.NULL);
            JSON.put("password", UserPassword);
            JSON.put("username", UserName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendData("https://msapi.top-academy.ru/api/v2/auth/login", "POST", JSON, Access_Token, false);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);



        Handler handler  = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            if (Access_Token.isEmpty()) {
                Access_Token=prefs.getString("LastSucsessfulAccessToken", "");
            }
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }, 6000);

    }

    @Override
    public void onEnabled(Context context) {
        Log.d("Widget", "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d("Widget", "onDisabled");
        // Enter relevant functionality for when the last widget is disabled
    }



    public static void onTaskCompleted(String result) {
        int ReturnCode = Integer.parseInt(result.split("\\|")[0]);
        String ReturnValue = result.split("\\|")[1];
        String UrlQuestion = result.split("\\|")[2];


        if (ReturnCode == 200) {
            switch (UrlQuestion) {
                case "https://msapi.top-academy.ru/api/v2/auth/login":
                    try {
                        JSONObject jsonObject = new JSONObject(ReturnValue);
                        Access_Token=(jsonObject.getString("access_token"));
                        sendData(LastSheduleTime, "GET", new JSONObject(), Access_Token, false);
                    } catch (JSONException e) {
                    }
                    break;
            }
            if (UrlQuestion.contains("https://msapi.top-academy.ru/api/v2/schedule/operations/get-by-date")) {
                Shedule = ReturnValue;
            }

        }
    }


}