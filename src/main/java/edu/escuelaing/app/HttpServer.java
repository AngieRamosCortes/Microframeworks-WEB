package edu.escuelaing.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Core HTTP server implementation that handles incoming requests.
 * This class manages the server socket, request processing, and response
 * generation.
 * It uses a thread pool to handle concurrent requests efficiently.
 * 
 * @author WebFramework Team
 * @version 1.0
 */
public class HttpServer {

    private final int port;
    private final Router router;
    private final StaticFileHandler staticFileHandler;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private volatile boolean running = false;

    /**
     * Constructor to initialize the HTTP server.
     *
     * @param port              The port number to listen on
     * @param router            The router instance for handling routes
     * @param staticFileHandler The static file handler instance
     */
    public HttpServer(int port, Router router, StaticFileHandler staticFileHandler) {
        this.port = port;
        this.router = router;
        this.staticFileHandler = staticFileHandler;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    /**
     * Starts the HTTP server and begins listening for connections.
     *
     * @throws IOException If the server socket cannot be created
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;

        Thread serverThread = new Thread(this::acceptConnections);
        serverThread.setDaemon(false);
        serverThread.start();
    }

    /**
     * Stops the HTTP server and cleans up resources.
     */
    public void stop() {
        running = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }

        if (threadPool != null) {
            threadPool.shutdown();
        }
    }

    /**
     * Main server loop that accepts incoming connections.
     * Each connection is handled in a separate thread from the thread pool.
     */
    private void acceptConnections() {
        while (running && !serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleRequest(clientSocket));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Handles an individual HTTP request from a client socket.
     *
     * @param clientSocket The client socket connection
     */
    private void handleRequest(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputStream = clientSocket.getOutputStream()) {

            HttpRequestData requestData = parseHttpRequest(reader);
            if (requestData == null) {
                sendErrorResponse(outputStream, 400, "Bad Request");
                return;
            }

            Request request = new Request(
                    requestData.method,
                    requestData.path,
                    requestData.queryString,
                    requestData.headers);
            Response response = new Response();

            Optional<Route> route = router.findRoute(requestData.method, requestData.path);

            if (route.isPresent()) {
                try {
                    String responseBody = route.get().execute(request, response);
                    sendResponse(outputStream, response, responseBody);
                } catch (Exception e) {
                    System.err.println("Error executing route handler: " + e.getMessage());
                    sendErrorResponse(outputStream, 500, "Internal Server Error");
                }
            } else {
                StaticFileHandler.StaticFileResult fileResult = staticFileHandler.serveStaticFile(requestData.path);

                if (fileResult.isFound()) {
                    sendStaticFileResponse(outputStream, fileResult);
                } else {
                    sendErrorResponse(outputStream, 404, "Not Found");
                }
            }

        } catch (IOException e) {
            System.err.println("Error handling request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    /**
     * Parses an HTTP request from the input reader.
     *
     * @param reader The BufferedReader containing the request data
     * @return HttpRequestData object with parsed request information
     * @throws IOException If reading from the stream fails
     */
    private HttpRequestData parseHttpRequest(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        if (firstLine == null || firstLine.trim().isEmpty()) {
            return null;
        }

        String[] requestTokens = firstLine.split(" ");
        if (requestTokens.length < 3) {
            return null;
        }

        String method = requestTokens[0];
        String uri = requestTokens[1];

        String path = uri;
        String queryString = null;
        int queryIndex = uri.indexOf('?');
        if (queryIndex >= 0) {
            path = uri.substring(0, queryIndex);
            queryString = uri.substring(queryIndex + 1);
        }

        if ("/".equals(path)) {
            path = "/index.html";
        }

        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            String[] headerParts = line.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0].toLowerCase(), headerParts[1]);
            }
        }

        return new HttpRequestData(method, path, queryString, headers);
    }

    /**
     * Sends an HTTP response to the client.
     *
     * @param outputStream The output stream to write to
     * @param response     The Response object containing response data
     * @param body         The response body content
     * @throws IOException If writing to the stream fails
     */
    private void sendResponse(OutputStream outputStream, Response response, String body) throws IOException {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        String statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + response.getStatusMessage() + "\r\n";
        outputStream.write(statusLine.getBytes(StandardCharsets.UTF_8));

        outputStream.write(("Content-Type: " + response.getContentType() + "\r\n").getBytes());
        outputStream.write(("Content-Length: " + bodyBytes.length + "\r\n").getBytes());

        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            String headerLine = header.getKey() + ": " + header.getValue() + "\r\n";
            outputStream.write(headerLine.getBytes());
        }

        outputStream.write("\r\n".getBytes());
        outputStream.write(bodyBytes);
        outputStream.flush();
    }

    /**
     * Sends a static file response to the client.
     *
     * @param outputStream The output stream to write to
     * @param fileResult   The static file result containing file data
     * @throws IOException If writing to the stream fails
     */
    private void sendStaticFileResponse(OutputStream outputStream,
            StaticFileHandler.StaticFileResult fileResult) throws IOException {
        outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());

        outputStream.write(("Content-Type: " + fileResult.getContentType() + "\r\n").getBytes());
        outputStream.write(("Content-Length: " + fileResult.getData().length + "\r\n").getBytes());
        outputStream.write("\r\n".getBytes());
        outputStream.write(fileResult.getData());
        outputStream.flush();
    }

    /**
     * Sends an HTTP error response to the client.
     *
     * @param outputStream  The output stream to write to
     * @param statusCode    The HTTP status code
     * @param statusMessage The HTTP status message
     * @throws IOException If writing to the stream fails
     */
    private void sendErrorResponse(OutputStream outputStream, int statusCode, String statusMessage) throws IOException {
        String body = "<html><body><h1>" + statusCode + " " + statusMessage + "</h1></body></html>";
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        String statusLine = "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n";
        outputStream.write(statusLine.getBytes());
        outputStream.write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
        outputStream.write(("Content-Length: " + bodyBytes.length + "\r\n").getBytes());
        outputStream.write("\r\n".getBytes());
        outputStream.write(bodyBytes);
        outputStream.flush();
    }

    /**
     * Inner class to hold parsed HTTP request data.
     */
    private static class HttpRequestData {
        final String method;
        final String path;
        final String queryString;
        final Map<String, String> headers;

        /**
         * Constructor for HTTP request data.
         *
         * @param method      The HTTP method
         * @param path        The request path
         * @param queryString The query string
         * @param headers     The request headers
         */
        HttpRequestData(String method, String path, String queryString, Map<String, String> headers) {
            this.method = method;
            this.path = path;
            this.queryString = queryString;
            this.headers = headers;
        }
    }
}
