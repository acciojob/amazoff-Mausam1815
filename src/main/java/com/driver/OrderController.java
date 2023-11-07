package com.driver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.PushBuilder;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final AmazoffService amazoffService;

    @Autowired
    public OrderController(AmazoffService amazoffService) {
        this.amazoffService = amazoffService;
    }

    @PostMapping("/add-order")
    public ResponseEntity<String> addOrder(@RequestBody Order order){
        String orderId = order.getId();
        if(amazoffService.getOrderById(orderId) != null) {
            return new ResponseEntity<>("Order with ID " + orderId + " already exist.", HttpStatus.CONFLICT);
        }
        amazoffService.addOrder(order);
        return new ResponseEntity<>("New order added successfully", HttpStatus.CREATED);
    }

    @PostMapping("/add-partner/{partnerId}")
    public ResponseEntity<String> addPartner(@PathVariable String partnerId){
        if(amazoffService.getPartnerById(partnerId) != null) {
            return new ResponseEntity<>("Partner with ID " + partnerId + " already exist", HttpStatus.CONFLICT);
        }
        amazoffService.addPartner(partnerId);
        return new ResponseEntity<>("New delivery partner added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/add-order-partner-pair")
    public ResponseEntity<String> addOrderPartnerPair(@RequestParam String orderId, @RequestParam String partnerId){
        amazoffService.addOrderPartnerPair(orderId, partnerId);
        //This is basically assigning that order to that partnerId
        return new ResponseEntity<>("New order-partner pair added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get-order-by-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId){

        Order order= amazoffService.getOrderById(orderId);
        //order should be returned with an orderId.
        if(order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


    @GetMapping("/get-partner-by-id/{partnerId}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable String partnerId){

        DeliveryPartner deliveryPartner = amazoffService.getPartnerById(partnerId);

        //deliveryPartner should contain the value given by partnerId
        if(deliveryPartner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deliveryPartner, HttpStatus.CREATED);
    }

    @GetMapping("/get-order-count-by-partner-id/{partnerId}")
    public ResponseEntity<Integer> getOrderCountByPartnerId(@PathVariable String partnerId){

        Integer orderCount = amazoffService.getOrderCountByPartnerId(partnerId);

        //orderCount should denote the orders given by a partner-id
        if(orderCount == 0) {
            return new ResponseEntity<>(orderCount, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(orderCount, HttpStatus.CREATED);
    }

    @GetMapping("/get-orders-by-partner-id/{partnerId}")
    public ResponseEntity<List<String>> getOrdersByPartnerId(@PathVariable String partnerId){
        List<String> orders = amazoffService.getOrdersByPartnerId(partnerId);

        //orders should contain a list of orders by PartnerId
        if(orders == null || orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<String>> getAllOrders(){
        List<String> orders = amazoffService.getAllOrder();
        if(orders == null || orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //Get all orders
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/get-count-of-unassigned-orders")
    public ResponseEntity<Integer> getCountOfUnassignedOrders(){
        Integer countOfOrders = amazoffService.getCountOfUnassignedOrder();

        //Count of orders that have not been assigned to any DeliveryPartner

        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    @GetMapping("/get-count-of-orders-left-after-given-time/{partnerId}")
    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(@PathVariable String time, @PathVariable String partnerId){

        Integer countOfOrders = amazoffService.getOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);

        //countOfOrders that are left after a particular time of a DeliveryPartner

        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    @GetMapping("/get-last-delivery-time/{partnerId}")
    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(@PathVariable String partnerId){
        String time = amazoffService.getLastDeliveryTimeByPartnerId(partnerId);

        //Return the time when that partnerId will deliver his last delivery order.

        return new ResponseEntity<>(time, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-partner-by-id/{partnerId}")
    public ResponseEntity<String> deletePartnerById(@PathVariable String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        if(amazoffService.getPartnerById(partnerId) == null) {
            return new ResponseEntity<>(partnerId + " does not exist", HttpStatus.NOT_FOUND);
        }
        amazoffService.deletePartnerById(partnerId);
        return new ResponseEntity<>(partnerId + " removed successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-order-by-id/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId
        if(amazoffService.getOrderById(orderId) == null) {
            return new ResponseEntity<>(orderId + " does not exist", HttpStatus.NOT_FOUND);
        }
        amazoffService.deleteOrderById(orderId);
        return new ResponseEntity<>(orderId + " removed successfully", HttpStatus.CREATED);
    }
}
