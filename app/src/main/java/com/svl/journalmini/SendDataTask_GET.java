package com.svl.journalmini;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendDataTask_GET extends AsyncTask<Void, Void, String> {

    private final String url;
    private final String FetchType;
    private final JSONObject data;
    private final OnTaskCompleted listener;
    private final String access_Token;

    public SendDataTask_GET(String url, JSONObject data, String access_Token, OnTaskCompleted listener) {
        this.url = url;//URL To Fetch
        this.data = data;// JSON Data include if it not empty
        this.FetchType = "GET";// POST or Get
        this.listener = listener;//Just for another activity
        this.access_Token = access_Token;//Access token that i have
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL urlObject = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setRequestMethod("GET");


            //Setting the Headers for correct work
            //urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Origin", "https://journal.top-academy.ru");
            urlConnection.setRequestProperty("Referer", "https://journal.top-academy.ru/");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_Token);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");

            //urlConnection.setDoOutput(true);//Server will send data in any case


            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                return responseCode+"|"+response.toString()+"|"+url;
            } else {
                return responseCode+"|Error"+"|"+url;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "-123"+"|Error|null";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.onTaskCompleted(result);
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }
}
