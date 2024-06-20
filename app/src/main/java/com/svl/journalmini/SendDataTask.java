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
import java.nio.charset.StandardCharsets;

public class SendDataTask extends AsyncTask<Void, Void, String> {

    private final String url;
    private final String FetchType;
    private final JSONObject data;
    private final OnTaskCompleted listener;
    private final String access_Token;
    private boolean onlyGet;
    private boolean caching;

    public SendDataTask(String url, String FETCH_TYPE, JSONObject data, String access_Token, OnTaskCompleted listener, boolean UseCache) {
        this.url = url;//URL To Fetch
        this.data = data;// JSON Data include if it not empty
        this.FetchType = FETCH_TYPE;// POST or Get
        this.listener = listener;//Just for another activity
        this.access_Token = access_Token;//Access token that i have
        this.caching = UseCache;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            onlyGet = FetchType.equals("GET");

            URL urlObject = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setRequestMethod(FetchType);


            if (!onlyGet) {
                urlConnection.setDoInput(!data.toString().equals("{}"));
                if (urlConnection.getDoInput()) {
                    urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                }
                if (urlConnection.getDoInput()) {
                    urlConnection.setRequestProperty("Accept", "application/json");
                }
            }
            urlConnection.setRequestProperty("Origin", "https://journal.top-academy.ru");
            urlConnection.setRequestProperty("Referer", "https://journal.top-academy.ru/");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_Token);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");



            if (urlConnection.getDoInput() && !onlyGet){
                urlConnection.setDoOutput(true);
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                bufferedWriter.write(data.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
            }


            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                return responseCode+"|"+response+"|"+url;
            } else {
                return responseCode+"|Error|"+url;
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
