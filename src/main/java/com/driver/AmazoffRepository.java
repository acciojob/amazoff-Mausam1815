package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Repository
public class AmazoffRepository {
    private final HashMap<String, Order> ordersDB = new HashMap<>();
    private final HashMap<String, DeliveryPartner> deliveryPartnersDB = new HashMap<>();
    private final HashMap<String, String> orderToPartnerDB = new HashMap<>();
    private final HashMap<String, List<String>> partnerToOrdersDB = new HashMap<>();

    public void addOrder(Order order) {
        ordersDB.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        deliveryPartnersDB.put(partnerId, partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderToPartnerDB.put(orderId, partnerId);
        if(partnerToOrdersDB.containsKey(partnerId)) {
           partnerToOrdersDB.get(partnerId).add(orderId);
        }else {
            partnerToOrdersDB.put(partnerId, new ArrayList<>());
            partnerToOrdersDB.get(partnerId).add(orderId);
        }
    }

    public Order getOrderById(String orderId) {
        return ordersDB.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnersDB.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        return partnerToOrdersDB.getOrDefault(partnerId, new ArrayList<>()).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerToOrdersDB.getOrDefault(partnerId, new ArrayList<>());
    }

    public List<String> getAllOrders() {
        List<String> allOrders = new ArrayList<>(ordersDB.keySet());
        Collections.sort(allOrders);
        return allOrders;
    }

    public int getCountOfUnassignedOrders() {
        return ordersDB.size() - orderToPartnerDB.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        if(partnerToOrdersDB.containsKey(partnerId)) {
            int count = 0;
            int givenTime = convertTimeToMinutes(time);
            for(String orderId : partnerToOrdersDB.get(partnerId)) {
                if(ordersDB.containsKey(orderId) && ordersDB.get(orderId).getDeliveryTime() > givenTime) {
                    count++;
                }
            }
            return count;
        }
        return 0;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        if(partnerToOrdersDB.containsKey(partnerId)) {
            int lastDeliveryTime = 0;
            for(String orderId : partnerToOrdersDB.get(partnerId)) {
                if(ordersDB.containsKey(orderId) && ordersDB.get(orderId).getDeliveryTime() > lastDeliveryTime) {
                    lastDeliveryTime = ordersDB.get(orderId).getDeliveryTime();
                }
            }
            return convertMinutesToTime(lastDeliveryTime);
        }
        return "No orders for the partner";
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartnersDB.remove(partnerId);
        if(partnerToOrdersDB.containsKey(partnerId)) {
            List<String> ordersToUnassign = partnerToOrdersDB.get(partnerId);
            for(String orderId : ordersToUnassign) {
                orderToPartnerDB.remove(orderId);
            }
            partnerToOrdersDB.remove(partnerId);
        }
    }

    public void deleteOrderById(String orderId) {
        String partnerId = orderToPartnerDB.get(orderId);
        if(partnerId != null) {
            List<String> orderForPartner = partnerToOrdersDB.get(partnerId);
            if(orderForPartner != null) {
                orderForPartner.remove(orderId);
            }
        }
        orderToPartnerDB.remove(orderId);
        ordersDB.remove(orderId);
    }

    private String convertMinutesToTime(int minutes) {
        int HH = minutes / 60;
        int MM = minutes % 60;
        if(MM == 0) {
            return HH + ":" + MM + MM;
        }else {
            return HH + ":" + MM;
        }
    }

    public int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int HH = Integer.parseInt(parts[0]);
        int MM = Integer.parseInt(parts[1]);
        return HH * 60 + MM;
    }
}
