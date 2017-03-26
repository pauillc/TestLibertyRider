package com.example.pauill_c.testlibertyrider;

import android.util.Xml;

import org.joda.time.*;
import org.xmlpull.v1.*;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pauill_c on 26/03/2017.
 */

public class ParserXML implements Parser {
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
            // Starts by looking for the DriverTable tag
            if (name.equals("DriverTable")) {
                entries.addAll(readDriverTable(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Get first tag name DriverTable and search for Driver tag
    private List readDriverTable(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "DriverTable");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the Driver tag
            if (name.equals("Driver")) {
                entries.add(readDriver(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Get first tag name Driver and search for DateOfBirth tag
    // Here if we wanted all information of drivers we could have done a class Driver and return it
    private int readDriver(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Driver");
        LocalDate     date_of_birth = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals("DateOfBirth")) {
                date_of_birth = readDate(parser);
            } else {
                skip(parser);
            }
        }

        if (date_of_birth != null) {
            LocalDate now = new LocalDate();
            Days days = Days.daysBetween(date_of_birth, now);

            return days.getDays() / 365;
        }
        return (0);
    }

    // Processes date tag
    private LocalDate readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        try {
            parser.require(XmlPullParser.START_TAG, ns, "DateOfBirth");

            String d = readText(parser);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
            Date f = df.parse(d);
            LocalDate date = new LocalDate(f);

            parser.require(XmlPullParser.END_TAG, ns, "DateOfBirth");
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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
