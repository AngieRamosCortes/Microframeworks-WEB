package edu.escuelaing.app;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP response with methods to set response properties.
 * This class encapsulates response data including status codes, headers,
 * and content types for HTTP responses.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class Response {

    private int statusCode;
    private String contentType;
    private final Map<String, String> headers;

    /**
     * Default constructor that initializes the response with default values.
     * Sets status code to 200 OK and content type to text/plain.
     */
    public Response() {
        this.statusCode = 200;
        this.contentType = "text/plain; charset=utf-8";
        this.headers = new HashMap<>();
    }

    /**
     * Sets the HTTP status code for the response.
     *
     * @param statusCode The HTTP status code (e.g., 200, 404, 500)
     * @return This Response object for method chaining
     */
    public Response status(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * Sets the content type of the response.
     *
     * @param contentType The MIME type for the response content
     * @return This Response object for method chaining
     */
    public Response type(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Sets a header value for the response.
     *
     * @param name  The header name
     * @param value The header value
     * @return This Response object for method chaining
     */
    public Response header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * Sets the response content type to JSON.
     *
     * @return This Response object for method chaining
     */
    public Response json() {
        return type("application/json; charset=utf-8");
    }

    /**
     * Sets the response content type to HTML.
     *
     * @return This Response object for method chaining
     */
    public Response html() {
        return type("text/html; charset=utf-8");
    }

    /**
     * Sets the response content type to plain text.
     *
     * @return This Response object for method chaining
     */
    public Response text() {
        return type("text/plain; charset=utf-8");
    }

    /**
     * Gets the current status code.
     *
     * @return The HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the current content type.
     *
     * @return The content type string
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Gets all response headers.
     *
     * @return Map of all response headers
     */
    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    /**
     * Gets the HTTP status message for the current status code.
     *
     * @return The status message corresponding to the status code
     */
    public String getStatusMessage() {
        switch (statusCode) {
            case 200:
                return "OK";
            case 201:
                return "Created";
            case 400:
                return "Bad Request";
            case 404:
                return "Not Found";
            case 500:
                return "Internal Server Error";
            default:
                return "Unknown";
        }
    }
}
