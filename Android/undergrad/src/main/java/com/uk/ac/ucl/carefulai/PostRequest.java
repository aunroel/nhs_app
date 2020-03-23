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

public class PostRequest implements Runnable {

    private volatile String url = "";

    private DatabaseHelper myDb;

    private SharedPreferences careNetworkPreferences;

    private final String myPreferences = "careNetwork";

    public PostRequest(String url, Context context) {

        this.url = url;
        this.myDb = new DatabaseHelper(context);

        careNetworkPreferences = context.getSharedPreferences(myPreferences, 0);
    }

    @Override
    public void run() {
        sendPost(url);
    }


    private void sendPost(String address) {

        String postcode = careNetworkPreferences.getString("postcode", "");

        String supportCode = careNetworkPreferences.getString("carer_support_ref", "");

        String date = String.valueOf(LocalDate.now()).replace("-", "");


        Cursor res = myDb.getLastLine();

        String steps = String.valueOf(res.getInt(0));

        String calls = String.valueOf(res.getInt(1));

     //   String texts = String.valueOf(res.getInt(2));

        int score = res.getInt(3);

        int userScore = res.getInt(4);

        String errorRate = String.valueOf(Math.abs(userScore - score));

        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject();


            AnonymousData anonymousData = new AnonymousData(score);

            int wellBeingScore = anonymousData.getScore();

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

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
