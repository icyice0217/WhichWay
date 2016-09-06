package org.bangeek.shjt.models;

import java.util.List;

/**
 * Created by BinGan on 2016/9/6.
 */
public class LineCollection {
    private List<Line> list;
    private String version;

    public List<Line> getList() {
        return list;
    }

    public void setList(List<Line> list) {
        this.list = list;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
