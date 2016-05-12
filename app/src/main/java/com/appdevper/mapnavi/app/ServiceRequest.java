package com.appdevper.mapnavi.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by worawit on 3/6/16.
 */
public class ServiceRequest extends AsyncTask<String, Void, String> {
    private Context context;
    private ServiceCallback callback;
    private ProgressDialog progressDialog;

    public ServiceRequest(Activity c) {
        context = c;
        progressDialog = new ProgressDialog(c);
        try {
            callback = (ServiceCallback) c;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ServiceRequest(Context c, ServiceCallback call) {
        progressDialog = new ProgressDialog(c);
        context = c;
        callback = call;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(strings[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 200) {
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("ServiceRequest", "Response: " + responseString);
                return responseString;
            } else {
                Log.v("ServiceRequest", "Response code:" + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        progressDialog.hide();
        if (s != null) {
            callback.onSuccess(s);
        } else {
            callback.onFail("Can't connect to service.");
        }

    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
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
        }
        return response.toString();
    }
}
