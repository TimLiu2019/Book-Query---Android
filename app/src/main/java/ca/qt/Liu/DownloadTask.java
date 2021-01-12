package ca.qt.Liu;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * this class made for downloading
 */
public class DownloadTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        String results = ""; // Hold our results


        try {
            URL url = new URL(strings[0]); // Create URL from string passed in

            // Create our HTTP connection
            HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();

            int statusCode = myConnection.getResponseCode(); // Get the status code of the request

            if (statusCode == 200) {
                // Read in the data

                // First we create an input stream
                InputStream myInputS = new BufferedInputStream(myConnection.getInputStream());

                // Create a buffered reader
                BufferedReader br = new BufferedReader(new InputStreamReader(myInputS, "UTF-8"));

                // Read each line of the input
                String line = null;
                while ((line = br.readLine()) != null) {
                    results += line;
                }

                Log.d("DownloadTask", results);
            } else {
                // Was an error, do something
                Log.d("error", "Error response code: " + myConnection.getResponseCode());
            }


        } catch (MalformedURLException e) {
            // Handle error
            e.printStackTrace();
        } catch (IOException e) {
            // Handle error
            e.printStackTrace();
        }

        return results;
    }
}