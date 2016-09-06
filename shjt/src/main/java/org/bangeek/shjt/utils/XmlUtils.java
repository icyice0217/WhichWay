package org.bangeek.shjt.utils;

import android.util.Xml;

import org.bangeek.shjt.models.ApiModel;
import org.bangeek.shjt.models.Car;
import org.bangeek.shjt.models.Cars;
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
        Cars,
        LineDetail,
        LineInfoDetail
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

                case Cars:
                    return readCars(parser);

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

    private static Cars readCars(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Car> entries = new ArrayList<>();
        Cars cars = new Cars();

        parser.require(XmlPullParser.START_TAG, ns, "result");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                break;
            }
        }

        parser.require(XmlPullParser.START_TAG, ns, "cars");
        cars.setLineId(parser.getAttributeValue(null, "lineid"));

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if ("car".equals(name)) {
                parser.require(XmlPullParser.START_TAG, ns, "car");

                Car car = new Car();

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }

                    switch (parser.getName()) {
                        case "terminal":
                            car.setTerminal(text(parser));
                            break;

                        case "stopdis":
                            car.setStopdis(text(parser));
                            break;

                        case "distance":
                            car.setDistance(text(parser));
                            break;

                        case "time":
                            car.setTime(text(parser));
                            break;
                    }
                }

                entries.add(car);
            } else {
                skip(parser);
            }
        }
        cars.setCars(entries);
        return cars;
    }

    private static String text(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        String text = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.TEXT) {
                continue;
            }

            text = parser.getText();
        }
        return text;
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
