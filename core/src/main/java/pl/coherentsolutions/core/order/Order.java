package pl.coherentsolutions.core.order;

import pl.coherentsolutions.core.product.Product;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Order implements Serializable {
    private static int counterId = 0;
    private final int id;
    private final Map<Product, Integer> products;
    private OrderStatus orderStatus;
    private LocalDateTime processingStartTime;
    private int processingDuration;

    public Order() {
        this.id = ++counterId;
        this.products = new HashMap<>();
        this.orderStatus = OrderStatus.OPEN;
    }

    public int getId() {
        return id;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addProduct(Product product, int quantity) {
        this.products.put(product, quantity);
    }

    public LocalDateTime getProcessingStartTime() {
        return processingStartTime;
    }

    public int getProcessingDuration() {
        return processingDuration;
    }

    public void setProcessingStartTime(LocalDateTime processingStartTime) {
        this.processingStartTime = processingStartTime;
    }

    public void setProcessingDuration(int processingDuration) {
        this.processingDuration = processingDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(products, order.products) && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, products, orderStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", products=" + products +
                ", orderStatus=" + orderStatus +
                ", processingStartTime=" + processingStartTime +
                ", processingDuration=" + processingDuration +
                '}';
    }
}
