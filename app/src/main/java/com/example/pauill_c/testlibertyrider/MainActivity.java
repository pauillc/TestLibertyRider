package com.example.pauill_c.testlibertyrider;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static TextView  mTextMessage;
    public static TextView  titleText;
    private Database        db = new Database();
    private HttpRaceHandler raceHandler;
    private RequestQueue    queue;

    // Function for get the informations of the last race
    private void requestErgastAPI(String url, final Callback handler){

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    handler.function(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    titleText.setText("Error connecting with http://ergast.com");
                }
            }
        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        mTextMessage = (TextView) findViewById(R.id.Age);
        titleText = (TextView) findViewById(R.id.title);
        mTextMessage.setKeyListener(null);
        titleText.setKeyListener(null);

        raceHandler = new HttpRaceHandler();
        requestErgastAPI("http://ergast.com/api/f1/current/last", raceHandler);

        ImageView img = (ImageView)findViewById(R.id.imgDriver);

        final HttpPilotHandler pilotHandler = new HttpPilotHandler(db, getString(R.string.db_key_age), raceHandler);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestErgastAPI("http://ergast.com/api/f1/current/last/drivers", pilotHandler);
            }
        });

        requestErgastAPI("http://ergast.com/api/f1/current/last/drivers", pilotHandler);
    }

}
