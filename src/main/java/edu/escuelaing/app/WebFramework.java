package edu.escuelaing.app;

/**
 * Main entry point for the Web Framework.
 * This class provides static methods for configuring routes and starting the
 * server.
 * It implements the Facade pattern to provide a simple interface for
 * developers.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class WebFramework {

    private static Router router = new Router();
    private static StaticFileHandler staticFileHandler = new StaticFileHandler();
    private static HttpServer server;
    private static boolean isRunning = false;

    /**
     * Registers a GET route with the specified path and handler.
     * This method allows developers to define REST services using lambda
     * expressions.
     *
     * @param path    The URL path for the route (e.g., "/hello")
     * @param handler The lambda function to handle requests to this route
     */
    public static void get(String path, RouteHandler handler) {
        router.addRoute("GET", path, handler);
    }

    /**
     * Registers a POST route with the specified path and handler.
     *
     * @param path    The URL path for the route
     * @param handler The lambda function to handle requests to this route
     */
    public static void post(String path, RouteHandler handler) {
        router.addRoute("POST", path, handler);
    }

    /**
     * Registers a PUT route with the specified path and handler.
     *
     * @param path    The URL path for the route
     * @param handler The lambda function to handle requests to this route
     */
    public static void put(String path, RouteHandler handler) {
        router.addRoute("PUT", path, handler);
    }

    /**
     * Registers a DELETE route with the specified path and handler.
     *
     * @param path    The URL path for the route
     * @param handler The lambda function to handle requests to this route
     */
    public static void delete(String path, RouteHandler handler) {
        router.addRoute("DELETE", path, handler);
    }

    /**
     * Sets the directory where static files are located.
     * The framework will look for static files in the specified directory
     * within the classpath resources (typically target/classes/directory).
     *
     * @param directory The directory path relative to classpath resources
     */
    public static void staticfiles(String directory) {
        staticFileHandler.setStaticFilesDirectory(directory);
    }

    /**
     * Starts the web server on the default port (8080).
     * This method automatically starts the server after route configuration.
     */
    public static void start() {
        start(8080);
    }

    /**
     * Starts the web server on the specified port.
     *
     * @param port The port number to listen on
     */
    public static void start(int port) {
        if (isRunning) {
            System.out.println("Server is already running!");
            return;
        }

        try {
            server = new HttpServer(port, router, staticFileHandler);
            server.start();
            isRunning = true;

            System.out.println("=================================");
            System.out.println("Web Framework Server Started!");
            System.out.println("Port: " + port);
            System.out.println("Static files: " + staticFileHandler.getStaticFilesDirectory());
            System.out.println("Registered routes: " + router.getRouteCount());
            System.out.println("Server URL: http://localhost:" + port);
            System.out.println("=================================");

        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Stops the web server if it's running.
     */
    public static void stop() {
        if (server != null && isRunning) {
            server.stop();
            isRunning = false;
            System.out.println("Server stopped.");
        }
    }

    /**
     * Gets the current router instance.
     * Useful for testing and debugging purposes.
     *
     * @return The Router instance
     */
    public static Router getRouter() {
        return router;
    }

    /**
     * Gets the static file handler instance.
     * Useful for testing and configuration purposes.
     *
     * @return The StaticFileHandler instance
     */
    public static StaticFileHandler getStaticFileHandler() {
        return staticFileHandler;
    }

    /**
     * Checks if the server is currently running.
     *
     * @return true if server is running, false otherwise
     */
    public static boolean isRunning() {
        return isRunning;
    }

    /**
     * Resets the framework state.
     * Clears all routes and resets configuration. Useful for testing.
     */
    public static void reset() {
        stop();
        router.clearRoutes();
        staticFileHandler = new StaticFileHandler();
        isRunning = false;
    }
}
