package com.lauri.oliovko10;

import java.util.ArrayList;

public class CarDataStorage {
    private String city;
    private int year;
    private ArrayList<CarData> carData = new ArrayList<>();
    private static CarDataStorage carDataStorage = null;

    private CarDataStorage(){
    }

    static public CarDataStorage getInstance(){
        if(carDataStorage == null){
            carDataStorage = new CarDataStorage();
        }
        return carDataStorage;
    }
    public ArrayList<CarData> getCarData() {
        return carData;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void clearData() {
        carDataStorage = null;
    }
    public String getCity() {
        return city;
    }
    public int getYear() {
        return year;
    }
    public void addCarData(CarData carData) {
        this.carData.add(carData);
    }

}
