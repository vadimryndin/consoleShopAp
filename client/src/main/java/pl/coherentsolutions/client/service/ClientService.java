package pl.coherentsolutions.client.service;

import pl.coherentsolutions.core.exceptions.ClientServerException;
import pl.coherentsolutions.core.catalog.Catalog;
import pl.coherentsolutions.core.order.Order;
import pl.coherentsolutions.core.product.Product;

import java.io.IOException;
import java.util.List;

public interface ClientService {
    Catalog receiveCatalog() throws ClientServerException;
    StringBuilder placeOrder(Order order) throws ClientServerException;
    List<Order> receiveOrders() throws ClientServerException;
    Product findProductByName(String productName) throws ClientServerException;
    void closeConnection() throws IOException;
}
