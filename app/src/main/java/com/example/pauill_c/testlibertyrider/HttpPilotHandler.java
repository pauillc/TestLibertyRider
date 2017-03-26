package com.example.pauill_c.testlibertyrider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by pauill_c on 26/03/2017.
 */

public class HttpPilotHandler implements Callback {

    private Database    db;
    private String      key;
    private Parser      parser = new ParserXML();
    private HttpRaceHandler race;

    public HttpPilotHandler(Database db, String key, HttpRaceHandler race){
        this.db = db;
        this.key = key;
        this.race = race;
    }

    private float getAverageAge(List<Integer> age_pilots) {
        int n = 0;
        int total = 0;

        for (int age : age_pilots) {
            total += age;
            n++;
        }
        return ((float) total / (float) n);
    }

    @Override
    public void function(String response) {
        try {
            InputStream stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
            List<Integer> l = (List<Integer>)parser.parse(stream);
            stream.close();
            db.pushData(this.key + "_" + race.getDate(), "" + getAverageAge(l));
            db.getReference(this.key + "_" + race.getDate());
        } catch (XmlPullParserException e) {
            System.err.println("HttpPilotHandler: Error XmlPullParserException: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("HttpPilotHandler: Error IOException: " + e.getMessage());
        }
    }
}
