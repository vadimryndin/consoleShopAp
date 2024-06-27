package pl.coherentsolutions.service.order.thread;

import pl.coherentsolutions.core.order.Order;
import pl.coherentsolutions.core.order.OrderStatus;
import pl.coherentsolutions.service.order.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;

public class OrderProcessing implements Runnable {
    private final Random random = new Random();
    private final OrderRepository orderRepository;
    private final Lock lock;

    public OrderProcessing(OrderRepository orderRepository, Lock lock) {
        this.orderRepository = orderRepository;
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println("[Thread] " + this.getClass().getSimpleName() + " starting order check...");

        lock.lock();
        try {
            List<Order> orders = orderRepository.getAllOrders();
            for (Order order : orders) {
                if (order.getOrderStatus() == OrderStatus.OPEN) {
                    order.setProcessingStartTime(LocalDateTime.now());
                    order.setProcessingDuration(random.nextInt(1, 61));
                    order.setOrderStatus(OrderStatus.PROCESSING);
                    System.out.println("[Thread] " + this.getClass().getSimpleName() + " change status of order " + order.getId() + " to PROCESSING");
                }
            }
        } finally {
            lock.unlock();
            System.out.println("[Thread] " + this.getClass().getSimpleName() + " finishing order check.");
        }
    }
}
