package pl.coherentsolutions.client.service.impl;

import pl.coherentsolutions.client.service.ClientService;
import pl.coherentsolutions.core.exceptions.ClientServerException;
import pl.coherentsolutions.core.catalog.Catalog;
import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.core.clientserver.Request;
import pl.coherentsolutions.core.clientserver.RequestType;
import pl.coherentsolutions.core.clientserver.Response;
import pl.coherentsolutions.core.order.Order;
import pl.coherentsolutions.core.product.Product;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientServiceImpl implements ClientService {
    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    public ClientServiceImpl(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.socket = socket;
        this.output = output;
        this.input = input;
    }

    @Override
    public Catalog receiveCatalog() throws ClientServerException {
        Request requestCatalog = new Request(RequestType.RECEIVE_CATALOG, null);
        Response response = sendRequest(requestCatalog);

        return response != null ? (Catalog) response.getResponseBody() : null;
    }

    @Override
    public StringBuilder placeOrder(Order order) throws ClientServerException {
        Request placeOrderRequest = new Request(RequestType.PLACE_ORDER, order);
        Response response = sendRequest(placeOrderRequest);
        return (StringBuilder) response.getResponseBody();
    }

    @Override
    public List<Order> receiveOrders() throws ClientServerException {
        Request ordersRequest = new Request(RequestType.RECEIVE_ORDERS, null);
        Response response = sendRequest(ordersRequest);

        return response != null ? (List<Order>) response.getResponseBody() : null;
    }

    @Override
    public Product findProductByName(String productName) throws ClientServerException {
        Catalog catalog = receiveCatalog();
        for (Category category : catalog.getCategories()) {
            for (Product product : category.getProducts()) {
                if (product.getName().equalsIgnoreCase(productName)) {
                    return product;
                }
            }
        }
        return null;
    }

    @Override
    public void closeConnection() throws IOException {
        Request closeRequest = new Request(RequestType.CLOSE_CONNECTION, null);
        output.writeObject(closeRequest);
        output.flush();
    }

    private Response sendRequest(Request request) throws ClientServerException {
        try {
            output.writeObject(request);
            output.flush();

            return (Response) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new ClientServerException("Error sending Request: " + e.getMessage(), e);
        }
    }
}
