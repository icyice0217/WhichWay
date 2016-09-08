package org.bangeek.whichway.utils;

import org.bangeek.shjt.models.Car;
import org.bangeek.shjt.models.Cars;
import org.bangeek.shjt.models.LineStop;
import org.bangeek.whichway.models.LineCard;

/**
 * Created by BinGan on 2016/9/7.
 */
public class TransformUtil {
    public static LineCard toLineCard(LineStop lineStop) {
        LineCard lineCard = new LineCard();
        lineCard.setLine(lineStop.getName());
        lineCard.setStop(formatTime(lineStop.getWalkTime()));
        lineCard.setTime1(lineStop.getStopName());
        lineCard.setTime2("--:--");
        return lineCard;
    }

    public static LineCard toLineCard(LineStop lineStop, Cars cars) {
        int time1 = 0;
        int time2 = 0;
        if (cars == null || cars.getCars() == null)
            return null;

        for (Car car : cars.getCars()) {
            int time = CommonUtil.tryParseInt(car.getTime());
            if (time < lineStop.getWalkTime()) continue;
            if (time1 == 0) {
                time1 = time;
                continue;
            }
            time2 = time;
            break;
        }

        if (time1 == 0)
            return null;

        LineCard lineCard = new LineCard();
        lineCard.setLine(lineStop.getName());
        lineCard.setStop(formatTime(time1));
        lineCard.setTime1Value(time1);
        lineCard.setTime1(lineStop.getStopName());
        if (time2 != 0) {
            lineCard.setTime2(formatTime(time2));
            lineCard.setTime2Value(time2);
        } else {
            lineCard.setTime2("--:--");
        }

        return lineCard;
    }

    public static String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return (sec == 0) ?
                String.format("%d分", min) :
                String.format("%d分%d秒", min, sec);
    }
}
