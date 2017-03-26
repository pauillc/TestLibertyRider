package com.example.pauill_c.testlibertyrider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by pauill_c on 26/03/2017.
 */

public interface Parser {

    public List parse(InputStream in) throws XmlPullParserException, IOException;
}
