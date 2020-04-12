package com.uk.ac.ucl.carefulai;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

//Runnable class (in order to run in background) to send a POST request to the map visualisation (used in ThePedometerService)
public class PostRequest implements Runnable {

    private volatile String url = ""; //in future implementations, multiple post requests could be sent at the same time, therefore volatile for concurrency issues

    private DatabaseHelper myDb; //get the most recently collected data from SQLite database

    private SharedPreferences careNetworkPreferences; //used to get postcode and carer support reference

    private final String myPreferences = "careNetwork";


    public PostRequest(String url, Context context) { //constructor providing the endpoint and the context for SharedPreferences and myDb

        this.url = url;
        this.myDb = new DatabaseHelper(context); //initialise database

        careNetworkPreferences = context.getSharedPreferences(myPreferences, 0); //initialise careNetworkPreferences
    }

    @Override
    public void run() {
        sendPost(url);
    } //when thread started, send POST request


    private void sendPost(String endpoint) { //takes endpoint to send

        String postcode = careNetworkPreferences.getString("postcode", "").toUpperCase(); //get postcode

        String supportCode = careNetworkPreferences.getString("carer_support_ref", ""); //get support ref

        String date = String.valueOf(LocalDate.now()).replace("-", ""); //get date and format


        Cursor res = myDb.getLastLine(); //get the last line of database holding most recent data

        String steps = String.valueOf(res.getInt(0)); //get steps

        String calls = String.valueOf(res.getInt(1)); //get calls

        int score = res.getInt(3); //get the score

        int userScore = res.getInt(4); //get the user's inputted score

        String errorRate = String.valueOf(Math.abs(userScore - score)); //calculate the error rate using the difference between the user and model scores

        try {
            //generate POST request
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //create JSON object for data
            JSONObject jsonParam = new JSONObject();

            //anonymise the score
            AnonymousData anonymousData = new AnonymousData(score);
            int wellBeingScore = anonymousData.getScore();

            //add the required parameters into schema
            jsonParam.put("postCode", postcode);
            jsonParam.put("wellBeingScore", String.valueOf(wellBeingScore));
            jsonParam.put("weeklySteps", steps);
            jsonParam.put("weeklyCalls", calls);
            jsonParam.put("errorRate", errorRate);
            jsonParam.put("supportCode", supportCode);
            jsonParam.put("date", date);

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            //log the response message
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            //stop the HTTP connection
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
