package com.driver;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    //    @Autowired
    OrderRepository orderRepository = new OrderRepository();

    public void addOrder(Order order){
        orderRepository.addOrder(order);
    }

    public void addPartner(String pId){
        DeliveryPartner partner = new DeliveryPartner(pId);
        orderRepository.addPartner(partner);
    }

    public void assignOrder(String pId, String oId){
        orderRepository.assignOrder(pId, oId);
    }

    public Order getOrderById(String oId){
        return orderRepository.getOrderById(oId);
    }

    public DeliveryPartner getPartnerById(String pId){
        return orderRepository.getPartnerById(pId);
    }

    public int numOfOrdersAssignedToPartner(String pId){
        return orderRepository.numOfOrdersAssignedToPartner(pId);
    }

    public List<String> getListOfOrdersByPartner(String pId){
        return orderRepository.getListOfOrdersByPartner(pId);
    }

    public List<String> getAllOrders(){
        return orderRepository.getAllOrders();
    }

    public int getCountOfUnassignedOrders(){
        return orderRepository.getCountOfUnassignedOrders();
    }

    public int getCountOfUndeliveredOrders(String pId, String time){
        if(orderRepository.getListOfOrdersByPartner(pId) == null || time.length() == 0){
            return 0;
        }
        // convert time from string to integer
        String hrs = time.substring(0, 2);
        String mnts = time.substring(3);
        int maxTime = Integer.parseInt(hrs)*60 + Integer.parseInt(mnts);

        // get list of all orders from partner id
        List<String> partnerOrders = orderRepository.getListOfOrdersByPartner(pId);
        int unDeliveredOrders = 0;

        for(String oId: partnerOrders){
            // get order by id then get delivery time of each order
            Order order = orderRepository.getOrderById(oId);
            if(order.getDeliveryTime() > maxTime){
                unDeliveredOrders++;
            }
        }

        return unDeliveredOrders;
    }

    public String getLastDeliveryTime(String pId){
        if(orderRepository.getListOfOrdersByPartner(pId) == null){
            return null;
        }
        // get order list of that partner
        List<String> partnerOrders = orderRepository.getListOfOrdersByPartner(pId);

        int lastDeliveryTime = Integer.MIN_VALUE;

        for(String oId: partnerOrders){
            // get order and then get delivery time of that order
            Order order = orderRepository.getOrderById(oId);
            int deliveryTime = order.getDeliveryTime();
            if(deliveryTime > lastDeliveryTime){
                lastDeliveryTime = deliveryTime;
            }
        }

        // now convert this time from int to string
        int h = lastDeliveryTime/60;
        int m = lastDeliveryTime - (h*60);

        String hrs = String.format("%02d", h);
        String mnts = String.format("%02d", m);

        return (hrs + ":" + mnts);
    }

    public void deletePartner(String pId){
        orderRepository.deletePartner(pId);
    }

    public void deleteOrder(String oId){
        orderRepository.deleteOrder(oId);
    }
}