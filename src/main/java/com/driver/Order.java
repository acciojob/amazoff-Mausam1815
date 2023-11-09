package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {
        this.id = id;

        // The deliveryTime has to converted from string to int and then stored in the attribute deliveryTime  = HH*60 + MM
//        this.deliveryTime = Integer.parseInt(deliveryTime.substring(0, 2)) * 60 + Integer.parseInt(deliveryTime.substring(3));
        if(deliveryTime.length() != 0){
            String hrs = deliveryTime.substring(0, 2);
            String mnts = deliveryTime.substring(3);
            this.deliveryTime = Integer.parseInt(hrs)*60 + Integer.parseInt(mnts);
        }
        else{
            this.deliveryTime = 0;
        }
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }
}