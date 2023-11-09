package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private HashMap<String, Order> ordersMap;

    private HashMap<String, DeliveryPartner> partnersMap;

    private HashMap<String, List<String>> partnerVsOrders;

    private HashMap<String, String> orderVsPartner;

    public OrderRepository(){
        this.ordersMap = new HashMap<>();
        this.partnersMap = new HashMap<>();
        this.partnerVsOrders = new HashMap<>();
        this.orderVsPartner = new HashMap<>();
    }

    public HashMap<String, Order> getOrdersMap() {
        return ordersMap;
    }

    public HashMap<String, DeliveryPartner> getPartnersMap() {
        return partnersMap;
    }

    public HashMap<String, List<String>> partnerVsOrders() {
        return partnerVsOrders;
    }

    public HashMap<String, String> getOrderVsPartner() {
        return orderVsPartner;
    }

    public void addOrder(Order order){
        String oId = order.getId();
        int dTime = order.getDeliveryTime();
        if(oId.length() != 0 && dTime != 0){
            ordersMap.put(oId, order);
        }
    }

    public void addPartner(DeliveryPartner partner){
        String pId = partner.getId();
        if(pId.length() != 0){
            partnersMap.put(pId, partner);
        }
    }

    public void assignOrder(String pId, String oId){
        if(!partnersMap.containsKey(pId) || !ordersMap.containsKey(oId)){
            return;
        }
        if(!partnerVsOrders.containsKey(pId)){
            partnerVsOrders.put(pId, new ArrayList<String>());
        }
        partnerVsOrders.get(pId).add(oId);

        partnersMap.get(pId).setNumberOfOrders(partnerVsOrders.get(pId).size());

        // updating order vs partner map also
        orderVsPartner.put(oId, pId);
    }

    public Order getOrderById(String oId){
        if(ordersMap.containsKey(oId)){
            return ordersMap.get(oId);
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String pId){
        if(partnersMap.containsKey(pId)){
            return partnersMap.get(pId);
        }
        return null;
    }

    public int numOfOrdersAssignedToPartner(String pId){
        if(partnersMap.containsKey(pId)){
            return partnersMap.get(pId).getNumberOfOrders();
        }
        return 0;
    }

    public List<String> getListOfOrdersByPartner(String pId){
        if(partnerVsOrders.containsKey(pId)){
            return partnerVsOrders.get(pId);
        }
        return null;
    }

    public List<String> getAllOrders(){
        if(ordersMap.size() > 0){
            List<String> orders = new ArrayList(ordersMap.keySet());
            return orders;
        }
        return null;
    }

    public int getCountOfUnassignedOrders(){
        return ordersMap.size() - orderVsPartner.size();
    }

    public void deletePartner(String pId){
        if(partnersMap.containsKey(pId)){
            // remove from partners map
            partnersMap.remove(pId);
        }

        // need to un assign this partner orders
        if(partnerVsOrders.containsKey(pId)){
            List<String> orders = partnerVsOrders.get(pId);
            partnerVsOrders.remove(pId);

            for(String oId: orders){
                // this order should be removed from order vs partner map
                orderVsPartner.remove(oId);
            }
        }
    }

    public void deleteOrder(String oId){
        if(ordersMap.containsKey(oId)){
            // remove from orders map
            ordersMap.remove(oId);
        }

        // need to remove from partner's orders list also
        // get assigned partner
        if(orderVsPartner.containsKey(oId)){
            String pId = orderVsPartner.get(oId);
            orderVsPartner.remove(oId);

            List<String> orders = partnerVsOrders.get(pId);

            for(int i=0; i<orders.size(); i++){
                if(orders.get(i).equals(oId)){
                    orders.remove(i);
                    break;
                }
            }
            partnersMap.get(pId).setNumberOfOrders(partnerVsOrders.get(pId).size());
        }
    }
}