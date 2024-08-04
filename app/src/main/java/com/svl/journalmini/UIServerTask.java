package com.svl.journalmini;

import android.os.AsyncTask;

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

public class UIServerTask extends AsyncTask<Void, Void, String> {

    private final String url;
    private final String FetchType;
    private final JSONObject data;
    private final OnTaskCompleted listener;
    private final String access_Token;
    boolean onlyGet;



    public UIServerTask(String url, String FETCH_TYPE, JSONObject data, String UIAcessToken, OnTaskCompleted listener) {
        this.url = url;//URL To Fetch
        this.data = data;// JSON Data include if it not empty
        this.FetchType = FETCH_TYPE;// POST or Get
        this.listener = listener;//Just for another activity
        this.access_Token = UIAcessToken;//Access token that i have
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
            urlConnection.setRequestProperty("Serveruniqid", access_Token);



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
            return "-123|Error|null";
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
