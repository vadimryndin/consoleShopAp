package pl.coherentsolutions.service.order.controller;

import pl.coherentsolutions.core.order.Order;
import pl.coherentsolutions.service.order.repository.OrderRepository;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class OrderController {
    private final OrderRepository orderRepository;
    private final Lock lock;

    public OrderController(OrderRepository orderRepository, Lock lock) {
        this.orderRepository = orderRepository;
        this.lock = lock;
    }

    public void printAllOrders() {
        System.out.println("printing all orders...");

        lock.lock();
        try {
            List<Order> orders = orderRepository.getAllOrders();
            if (orders.isEmpty()) {
                System.out.println("No orders found");
            } else {
                orders.forEach(System.out::println);
            }
        } finally {
            lock.unlock();
        }
    }
}
