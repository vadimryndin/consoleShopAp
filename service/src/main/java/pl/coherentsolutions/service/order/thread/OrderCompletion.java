package pl.coherentsolutions.service.order.thread;

import pl.coherentsolutions.core.order.Order;
import pl.coherentsolutions.core.order.OrderStatus;
import pl.coherentsolutions.service.order.repository.OrderRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class OrderCompletion implements Runnable {
    private final OrderRepository orderRepository;
    private final Lock lock;

    public OrderCompletion(OrderRepository orderRepository, Lock lock) {
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
                if (order.getOrderStatus() == OrderStatus.PROCESSING && isProcessingTimeFinished(order)) {
                    order.setOrderStatus(OrderStatus.COMPLETED);
                    System.out.println("[Thread] " + this.getClass().getSimpleName() + " change status of order " + order.getId() + " to COMPLETED");
                }
            }
        } finally {
            lock.unlock();
            System.out.println("[Thread] " + this.getClass().getSimpleName() + " finishing order check.");
        }
    }

    private boolean isProcessingTimeFinished(Order order) {
        LocalDateTime startTime = order.getProcessingStartTime();
        int duration = order.getProcessingDuration();

        return ChronoUnit.SECONDS.between(startTime, LocalDateTime.now()) > duration;
    }
}
