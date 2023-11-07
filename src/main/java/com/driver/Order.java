package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;
        this.deliveryTime = convertTimeToMinutes(deliveryTime);
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}

    public int convertTimeToMinutes(String deliveryTime) {
        String[] splittedTime = deliveryTime.split(":");
        int HH = Integer.parseInt(splittedTime[0]);
        int MM = Integer.parseInt(splittedTime[1]);
        return HH * 60 + MM;
    }
}
