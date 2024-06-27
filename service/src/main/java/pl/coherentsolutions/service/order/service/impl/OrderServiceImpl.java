package pl.coherentsolutions.service.order.service.impl;

import pl.coherentsolutions.core.order.Order;
import pl.coherentsolutions.service.order.repository.OrderRepository;
import pl.coherentsolutions.service.order.service.OrderService;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void placeOrder(Order order) {
        orderRepository.addOrder(order);
        System.out.println("Order placed: " + order);
    }
}
