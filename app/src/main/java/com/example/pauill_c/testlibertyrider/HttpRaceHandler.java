package com.example.pauill_c.testlibertyrider;

import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 * Created by pauill_c on 26/03/2017.
 */

public class HttpRaceHandler implements Callback {

    private Parser  parser = new ParserRace();
    private String  date;

    public HttpRaceHandler() {
        date = "";
    }

    public String   getDate() {
        return date;
    }

    @Override
    public void function(String response) {
        try {
            InputStream stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
            List<String> l = (List<String>)parser.parse(stream);
            stream.close();
            date = l.get(0);
            parser.parse(stream);
        } catch (XmlPullParserException e) {
            System.err.println("HttpRaceHandler: Error XmlPullParserException: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("HttpRaceHandler: Error IOException: " + e.getMessage());
        }
    }
}
