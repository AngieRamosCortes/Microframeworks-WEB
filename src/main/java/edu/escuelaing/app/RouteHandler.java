package edu.escuelaing.app;

/**
 * Functional interface that defines the contract for handling HTTP requests.
 * This interface allows developers to use lambda expressions to define route
 * handlers.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
@FunctionalInterface
public interface RouteHandler {

    /**
     * Handles an HTTP request and returns a response.
     *
     * @param request  The HTTP request containing all request data
     * @param response The HTTP response object for setting response properties
     * @return The response body as a String
     */
    String handle(Request request, Response response);
}
