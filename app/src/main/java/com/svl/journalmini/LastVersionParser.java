package com.svl.journalmini;

import java.io.IOException;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LastVersionParser {


        public void fetchTextFromUrl(String urlString, TextFetchListener listener) {
            new FetchTextTask(listener).execute(urlString);
        }

        private static class FetchTextTask extends AsyncTask<String, Void, String> {
            private TextFetchListener listener;

            FetchTextTask(TextFetchListener listener) {
                this.listener = listener;
            }

            @Override
            protected String doInBackground(String... params) {
                String urlString = params[0];
                StringBuilder result = new StringBuilder();
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                    reader = new BufferedReader(inputStream);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

                return result.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                if (listener != null) {
                    listener.onTextFetched(result);
                }
            }
        }

        public interface TextFetchListener {
            void onTextFetched(String text);
        }


}

