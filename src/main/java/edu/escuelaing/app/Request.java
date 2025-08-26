package edu.escuelaing.app;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request with methods to access request data.
 * This class encapsulates all the information from an HTTP request
 * including headers, query parameters, and request body.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class Request {

    private final String method;
    private final String path;
    private final String queryString;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;

    /**
     * Constructor to create a Request object from HTTP request data.
     *
     * @param method      The HTTP method (GET, POST, etc.)
     * @param path        The request path without query string
     * @param queryString The query string part of the URL
     * @param headers     Map of HTTP headers
     */
    public Request(String method, String path, String queryString, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
        this.headers = headers != null ? headers : new HashMap<>();
        this.queryParams = parseQueryString(queryString);
    }

    /**
     * Gets the HTTP method of the request.
     *
     * @return The HTTP method as a String
     */
    public String getMethod() {
        return method;
    }

    /**
     * Gets the request path without query parameters.
     *
     * @return The request path as a String
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the raw query string.
     *
     * @return The query string or null if none exists
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * Gets the value of a query parameter by name.
     * This method provides access to URL query parameters.
     *
     * @param name The name of the query parameter
     * @return The parameter value or empty string if not found
     */
    public String getValues(String name) {
        return queryParams.getOrDefault(name, "");
    }

    /**
     * Gets a header value by name.
     *
     * @param name The header name (case-insensitive)
     * @return The header value or null if not found
     */
    public String getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    /**
     * Gets all query parameters as a map.
     *
     * @return Map of all query parameters
     */
    public Map<String, String> getAllQueryParams() {
        return new HashMap<>(queryParams);
    }

    /**
     * Parses the query string into a map of key-value pairs.
     *
     * @param queryString The raw query string to parse
     * @return Map containing parsed query parameters
     */
    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();

        if (queryString == null || queryString.trim().isEmpty()) {
            return params;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    params.put(key, value);
                } catch (UnsupportedEncodingException e) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }

        return params;
    }
}
