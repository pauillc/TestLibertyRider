package com.example.pauill_c.testlibertyrider;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pauill_c on 26/03/2017.
 */

public class ParserRace implements Parser {

    private static final String ns = null;

    // Parse XML File
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readMRData(parser);
        } finally {
            in.close();
        }
    }

    // Get first tag name MRData and search for DriverTable tag
    private List readMRData(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "MRData");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the RaceTable tag
            if (name.equals("RaceTable")) {
                entries.addAll(readRaceTable(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Get first tag name DriverTable and search for Race tag
    private List readRaceTable(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "RaceTable");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the Race tag
            if (name.equals("Race")) {
                entries.add(readRace(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Get first tag name Driver and search for DateOfBirth tag
    // Here if we wanted all information of drivers we could have done a class Driver and return it
    private String readRace(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Race");
        String date = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals("Date")) {
                date = readDate(parser);
            } else {
                skip(parser);
            }
        }

        return (date);
    }

    // Processes date tag
    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Date");
        String res = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Date");
        return res;
    }

    // Extract text of tags
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skip the next tag
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
