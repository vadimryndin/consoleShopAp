package pl.coherentsolutions.service.order.repository;

import pl.coherentsolutions.core.order.Order;

import java.util.List;

public interface OrderRepository {
    void addOrder(Order order);

    List<Order> getAllOrders();

    Order getOrderById(int id);
}
