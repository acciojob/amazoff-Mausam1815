package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmazoffService {
    private final AmazoffRepository amazoffRepository;

    @Autowired
    public AmazoffService(AmazoffRepository amazoffRepository) {
        this.amazoffRepository = amazoffRepository;
    }

    public void addOrder(Order order) {
         amazoffRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        amazoffRepository.addPartner(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        amazoffRepository.addOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        return amazoffRepository.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return amazoffRepository.getPartnerById(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        return amazoffRepository.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return amazoffRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrder() {
        return amazoffRepository.getAllOrders();
    }

    public int getCountOfUnassignedOrder() {
        return amazoffRepository.getCountOfUnassignedOrders();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        return amazoffRepository.getOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        return amazoffRepository.getLastDeliveryTimeByPartnerId(partnerId);
    }

    public void deletePartnerById(String partnerId) {
        amazoffRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        amazoffRepository.deleteOrderById(orderId);
    }
}
