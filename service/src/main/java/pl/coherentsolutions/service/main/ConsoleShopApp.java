package pl.coherentsolutions.service.main;

import pl.coherentsolutions.core.catalog.Catalog;
import pl.coherentsolutions.core.order.Order;
import pl.coherentsolutions.core.product.Product;
import pl.coherentsolutions.core.sorting.SortingCriterion;
import pl.coherentsolutions.service.catalog.controller.CatalogController;
import pl.coherentsolutions.service.catalog.service.CatalogService;
import pl.coherentsolutions.service.catalog.service.impl.CatalogServiceImpl;
import pl.coherentsolutions.service.category.dao.impl.CategoryDaoImpl;
import pl.coherentsolutions.service.category.service.impl.CategoryServiceImpl;
import pl.coherentsolutions.service.category.repository.impl.CategoryRepositoryImpl;
import pl.coherentsolutions.service.exception.ConfigReaderException;
import pl.coherentsolutions.service.exception.LoadCategoryException;
import pl.coherentsolutions.service.order.controller.OrderController;
import pl.coherentsolutions.service.order.repository.OrderRepository;
import pl.coherentsolutions.service.order.repository.impl.OrderRepositoryImpl;
import pl.coherentsolutions.service.order.service.OrderService;
import pl.coherentsolutions.service.order.service.impl.OrderServiceImpl;
import pl.coherentsolutions.service.order.thread.OrderCompletion;
import pl.coherentsolutions.service.order.thread.OrderProcessing;
import pl.coherentsolutions.service.product.ProductService;
import pl.coherentsolutions.service.product.impl.ProductServiceImpl;
import pl.coherentsolutions.service.util.configreader.ConfigReader;
import pl.coherentsolutions.service.util.configreader.DatabaseConfigReader;
import pl.coherentsolutions.service.util.configreader.impl.SortingConfigReaderImpl;
import pl.coherentsolutions.service.util.connectionprovider.ConnectionProvider;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConsoleShopApp {
    private final CatalogService consoleShopCatalogService;
    private ScheduledExecutorService executorService;
    private OrderRepository orderRepository;
    private OrderService orderService;
    private OrderController orderController;
    private ProductService productService;
    private static ConnectionProvider connectionProvider;
    private final Lock lock = new ReentrantLock();
    private static final String PLACE_ORDER = "ORDER";
    private static final String PRINT_ALL_ORDERS = "PRINT";
    private static final String EXIT = "EXIT";

    public ConsoleShopApp() {
        this.consoleShopCatalogService = new CatalogServiceImpl(
                new CategoryServiceImpl(new CategoryRepositoryImpl(new CategoryDaoImpl())),
                new ProductServiceImpl()
        );
    }

    public static void main(String[] args) {
        try {
            List<String> databaseConfig = DatabaseConfigReader.readDatabaseConfig("production");
            connectionProvider = ConnectionProvider.getInstance();
            connectionProvider.initializeConnection(databaseConfig);

            ConsoleShopApp consoleShopApp = new ConsoleShopApp();
            consoleShopApp.run();
        } catch (SQLException | ConfigReaderException e) {
            throw new RuntimeException("Error initializing database connection. Error: " + e.getMessage(), e);
        } finally {
            connectionProvider.closeConnection();
        }
    }

    public void run() {
        Catalog unsortedCatalog = prepareUnsortedCatalog();

        Catalog sortedCatalog = prepareSortedCatalog();

        Map<String, Double> mostExpensivePricesPerCategory = prepareMostExpensivePricesPerCategory();

        // custom method is used to print a user-friendly info
        CatalogController.printCatalog(unsortedCatalog);

        CatalogController.printCatalog(sortedCatalog);

        CatalogController.printProductPriceSumPerCategory(mostExpensivePricesPerCategory);

        runOrderService();
    }

    private Catalog prepareUnsortedCatalog() {
        try {
            return this.consoleShopCatalogService.createCatalog();
        } catch (LoadCategoryException e) {
            throw new RuntimeException("Error creating Catalog. Error: " + e.getMessage());
        }
    }

    private Catalog prepareSortedCatalog() {
        String fileName = "sortingConfig.xml";
        ConfigReader sortingConfigReader = new SortingConfigReaderImpl();
        List<SortingCriterion> sortingCriteria = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            sortingCriteria = (List<SortingCriterion>) sortingConfigReader.readConfig(classLoader.getResource(fileName).getFile());
        } catch (ConfigReaderException e) {
            throw new RuntimeException("Error preparing sorted Catalog. Error: " + e.getMessage());
        }

        return this.consoleShopCatalogService.sortCatalog(prepareUnsortedCatalog(), sortingCriteria);
    }

    private Map<String, Double> prepareMostExpensivePricesPerCategory() {
        int selectedRange = 5;
        return this.consoleShopCatalogService.findMostExpensiveProducts(prepareUnsortedCatalog(), selectedRange);
    }

    private void runOrderService() {
        orderRepository = new OrderRepositoryImpl();
        orderService = new OrderServiceImpl(orderRepository);
        orderController = new OrderController(orderRepository, lock);
        productService = new ProductServiceImpl();

        executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(new OrderProcessing(orderRepository, lock), 0, 10, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new OrderCompletion(orderRepository, lock), 0, 20, TimeUnit.SECONDS);

        interactOrderService();
    }

    private void interactOrderService() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean isRunningOrderProcessing = true;

            while (isRunningOrderProcessing) {
                System.out.println("Enter command ORDER (to create new order), PRINT (to print all orders), or EXIT");
                String option = scanner.nextLine().toUpperCase();

                switch (option) {
                    case PLACE_ORDER:
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
                                System.out.println("[Err] Invalid input value. Please try again.");
                                scanner.nextLine();
                                continue;
                            }
                            scanner.nextLine();

                            Product product = this.productService.findProductByName(productName);
                            if (product != null) {
                                order.addProduct(product, quantity);
                            } else {
                                System.out.println("[Err] Invalid product name.");
                            }

                            System.out.print("Add another product to this order? (yes/no): ");
                            String more = scanner.nextLine();
                            if (!more.equalsIgnoreCase("yes")) {
                                isOrdering = false;
                            }
                        }
                        if (!order.getProducts().isEmpty()) {
                            this.orderService.placeOrder(order);
                        }
                        break;
                    case PRINT_ALL_ORDERS:
                        this.orderController.printAllOrders();
                        break;
                    case EXIT:
                        isRunningOrderProcessing = false;
                        executorService.shutdown();

                        try {
                            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                                executorService.shutdownNow();
                            }
                        } catch (InterruptedException e) {
                            System.out.println("[LOG] [Thread] " + Thread.currentThread().getName() + " interrupted.");
                            executorService.shutdownNow();
                            Thread.currentThread().interrupt();
                        }

                        System.out.println("Exiting...");
                        scanner.close();
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
}