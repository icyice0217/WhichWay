package org.bangeek.shjt.models;

/**
 * Created by BinGan on 2016/9/6.
 */
public class ApiModel {
    private String name;
    private String version;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
