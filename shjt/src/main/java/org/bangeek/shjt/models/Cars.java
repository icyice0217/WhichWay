package org.bangeek.shjt.models;

import java.util.List;

/**
 * Created by Bin on 2016/9/6.
 */

public class Cars {
    private String lineId;
    private List<Car> cars;

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
