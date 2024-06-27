package pl.coherentsolutions.service.server;

import pl.coherentsolutions.core.catalog.Catalog;
import pl.coherentsolutions.core.clientserver.Request;
import pl.coherentsolutions.core.clientserver.Response;
import pl.coherentsolutions.core.exceptions.ClientServerException;
import pl.coherentsolutions.core.order.Order;
import pl.coherentsolutions.service.catalog.service.CatalogService;
import pl.coherentsolutions.service.catalog.service.impl.CatalogServiceImpl;
import pl.coherentsolutions.service.category.dao.impl.CategoryDaoImpl;
import pl.coherentsolutions.service.category.repository.impl.CategoryRepositoryImpl;
import pl.coherentsolutions.service.category.service.impl.CategoryServiceImpl;
import pl.coherentsolutions.service.exception.LoadCategoryException;
import pl.coherentsolutions.service.order.repository.OrderRepository;
import pl.coherentsolutions.service.order.repository.impl.OrderRepositoryImpl;
import pl.coherentsolutions.service.order.service.OrderService;
import pl.coherentsolutions.service.order.service.impl.OrderServiceImpl;
import pl.coherentsolutions.service.product.impl.ProductServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;

public class ClientHandler implements Callable {
    private final Socket socket;
    private final CatalogService catalogService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.orderRepository = new OrderRepositoryImpl();
        this.orderService = new OrderServiceImpl(this.orderRepository);
        this.catalogService = new CatalogServiceImpl(new CategoryServiceImpl(new CategoryRepositoryImpl(new CategoryDaoImpl())), new ProductServiceImpl());
    }

    @Override
    public Object call() throws ClientServerException {
        try(ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ) {
            boolean isRunning = true;
            while (isRunning) {
                Request request = (Request) input.readObject();
                Response response = handleRequest(request);
                output.writeObject(response);
                output.flush();
            }
        } catch (IOException | ClassNotFoundException | LoadCategoryException e) {
            throw new ClientServerException("Error running client handler: " + e.getMessage(), e);
        }
        return null;
    }

    private Response handleRequest(Request request) throws LoadCategoryException {
        switch (request.getRequestType()) {
            case PLACE_ORDER:
                Order order = (Order) request.getRequestBody();
                this.orderService.placeOrder(order);
                return new Response(new StringBuilder("Order " + order.getId() + " is created"));
            case RECEIVE_ORDERS:
                List<Order> orders = this.orderRepository.getAllOrders();
                return new Response(orders);
            case RECEIVE_CATALOG:
                Catalog catalog = this.catalogService.createCatalog();
                return new Response(catalog);
            case CLOSE_CONNECTION:
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("[Error] Error closing connection: " + e.getMessage());
                    }
                }
            default:
                return new Response("Invalid request type");
        }
    }
}
