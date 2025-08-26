package edu.escuelaing.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages all routes in the web framework using the Observer pattern.
 * The Router is responsible for storing routes and finding the appropriate
 * route handler for incoming HTTP requests.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class Router {

    private final List<Route> routes;

    /**
     * Constructor that initializes an empty list of routes.
     */
    public Router() {
        this.routes = new ArrayList<>();
    }

    /**
     * Adds a new route to the router.
     *
     * @param method  The HTTP method for the route
     * @param path    The URL path for the route
     * @param handler The handler function for the route
     */
    public void addRoute(String method, String path, RouteHandler handler) {
        Route route = new Route(method, path, handler);
        routes.add(route);
        System.out.println("Route registered: " + route);
    }

    /**
     * Finds a matching route for the given method and path.
     * Uses the Strategy pattern to find the appropriate handler.
     *
     * @param method The HTTP method of the request
     * @param path   The URL path of the request
     * @return An Optional containing the matching Route, or empty if no match
     */
    public Optional<Route> findRoute(String method, String path) {
        for (Route route : routes) {
            if (route.matches(method, path)) {
                return Optional.of(route);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets all registered routes.
     * Useful for debugging and monitoring purposes.
     *
     * @return A copy of the list containing all routes
     */
    public List<Route> getAllRoutes() {
        return new ArrayList<>(routes);
    }

    /**
     * Gets the total number of registered routes.
     *
     * @return The number of routes currently registered
     */
    public int getRouteCount() {
        return routes.size();
    }

    /**
     * Removes all routes from the router.
     * Useful for testing or dynamic route management.
     */
    public void clearRoutes() {
        routes.clear();
        System.out.println("All routes cleared");
    }

    /**
     * Checks if a route exists for the given method and path.
     *
     * @param method The HTTP method to check
     * @param path   The URL path to check
     * @return true if a matching route exists, false otherwise
     */
    public boolean hasRoute(String method, String path) {
        return findRoute(method, path).isPresent();
    }
}
