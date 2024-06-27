package pl.coherentsolutions.client.main;

import pl.coherentsolutions.core.exceptions.ClientServerException;
import pl.coherentsolutions.client.service.ClientService;
import pl.coherentsolutions.client.service.impl.ClientServiceImpl;
import pl.coherentsolutions.core.catalog.Catalog;
import pl.coherentsolutions.core.order.Order;
import pl.coherentsolutions.core.product.Product;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleShopClient {
    private static Socket socket;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) throws ClientServerException {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            ClientService clientService = new ClientServiceImpl(socket, output, input);
            Scanner scanner = new Scanner(System.in);

            boolean isClientRunning = true;
            while(isClientRunning) {
                System.out.println("Enter command (CATALOG, ORDER, ORDERS, EXIT):");
                String command = scanner.nextLine().toUpperCase();

                switch (command) {
                    case "CATALOG":
                        Catalog catalog = clientService.receiveCatalog();
                        if (catalog == null) {
                            System.out.println("[CLIENT RESPONSE] Catalog is empty");
                        } else {
                            System.out.println("[CLIENT RESPONSE] Catalog:: " + catalog);
                        }
                        break;
                    case "ORDER":
                        Order order = new Order();
                        boolean isOrdering = true;
                        while (isOrdering) {
                            System.out.print("Enter product name: ");
                            String productName = scanner.nextLine();
                            System.out.print("Enter quantity: ");
                            int quantity = 0;
                            try {
                                quantity = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("[Error] Invalid input value. Please try again.");
                                scanner.nextLine();
                                continue;
                            }
                            scanner.nextLine();

                            Product product = clientService.findProductByName(productName);
                            if (product != null) {
                                order.addProduct(product, quantity);
                            } else {
                                System.out.println("[Error] Invalid product name.");
                                isOrdering = false;
                            }

                            System.out.print("Add another product? (yes/no): ");
                            String more = scanner.nextLine();
                            if (!more.equalsIgnoreCase("yes")) {
                                isOrdering = false;
                            }
                        }
                        if (!order.getProducts().isEmpty()) {
                            System.out.println("[CLIENT RESPONSE] : " + clientService.placeOrder(order));
                        }
                        break;
                    case "ORDERS":
                        ArrayList<Order> orders = (ArrayList<Order>) clientService.receiveOrders();
                        if (orders.isEmpty()) {
                            System.out.println("[CLIENT RESPONSE] No orders found.");
                        } else {
                            System.out.println("[CLIENT RESPONSE] Orders: " + orders);
                        }
                        break;
                    case "EXIT":
                        System.out.println("Exiting...");
                        clientService.closeConnection();
                        isClientRunning = false;
                        break;
                    default:
                        System.out.println("Invalid command. Please try again.");
                }
            }
        } catch (IOException e) {
            throw new ClientServerException("Error running client: " + e.getMessage(), e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                System.out.println("[Error] Error closing client: " + e.getMessage());
            }
        }
    }
}
