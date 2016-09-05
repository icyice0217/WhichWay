package org.bangeek.whichway.models;

/**
 * Created by Bin on 2016/9/5.
 */

public class LineCard {
    private String mLine;
    private String mStop;
    private String mTime1;
    private String mTime2;

    public String getLine() {
        return mLine;
    }

    public void setLine(String line) {
        this.mLine = line;
    }

    public String getStop() {
        return mStop;
    }

    public void setStop(String stop) {
        this.mStop = stop;
    }

    public String getTime1() {
        return mTime1;
    }

    public void setTime1(String time1) {
        this.mTime1 = time1;
    }

    public String getTime2() {
        return mTime2;
    }

    public void setTime2(String time2) {
        this.mTime2 = time2;
    }
}
