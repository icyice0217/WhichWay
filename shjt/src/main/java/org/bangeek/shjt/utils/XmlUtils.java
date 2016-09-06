package org.bangeek.shjt.utils;

import android.util.Xml;

import org.bangeek.shjt.models.ApiModel;
import org.bangeek.shjt.models.Line;
import org.bangeek.shjt.models.LineCollection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BinGan on 2016/9/6.
 */
public class XmlUtils {
    private static final String ns = null; //namespace

    public enum ParseType {
        ApiList,
        BusLine,
        Detail,
        DetailInfo
    }

    public static Object parse(InputStream in, ParseType parseType) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            switch (parseType) {
                case ApiList:
                    return readModifyApiList(parser);

                case BusLine:
                    return readLineList(parser);

                default:
                    return null;
            }
        } finally {
            in.close();
        }
    }

    private static List<ApiModel> readModifyApiList(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<ApiModel> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "modify");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            ApiModel apiModel = new ApiModel();
            apiModel.setName(parser.getName());
            apiModel.setUrl(parser.getAttributeValue(null, "url"));
            apiModel.setVersion(parser.getAttributeValue(null, "version"));
            entries.add(apiModel);
        }
        return entries;
    }

    private static LineCollection readLineList(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Line> entries = new ArrayList<>();
        LineCollection col = new LineCollection();

        parser.require(XmlPullParser.START_TAG, ns, "lines");
        col.setVersion(parser.getAttributeValue(null, "version"));

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            Line line = new Line();
            line.setName(parser.getAttributeValue(null, "name"));
            line.setValue(parser.getAttributeValue(null, "actual"));
            entries.add(line);
        }
        col.setList(entries);
        return col;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
