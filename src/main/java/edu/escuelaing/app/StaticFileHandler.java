package edu.escuelaing.app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Handles serving static files from the specified directory.
 * This class implements the Strategy pattern for different file serving
 * approaches
 * and manages the static files configuration for the web framework.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class StaticFileHandler {

    private String staticFilesDirectory;
    private static final String DEFAULT_DIRECTORY = "/webroot";

    /**
     * Constructor that initializes the static file handler with default directory.
     */
    public StaticFileHandler() {
        this.staticFilesDirectory = DEFAULT_DIRECTORY;
    }

    /**
     * Sets the directory where static files are located.
     *
     * @param directory The directory path relative to the classpath resources
     */
    public void setStaticFilesDirectory(String directory) {
        if (!directory.startsWith("/")) {
            directory = "/" + directory;
        }
        this.staticFilesDirectory = directory;
        System.out.println("Static files directory set to: " + directory);
    }

    /**
     * Gets the current static files directory.
     *
     * @return The static files directory path
     */
    public String getStaticFilesDirectory() {
        return staticFilesDirectory;
    }

    /**
     * Attempts to serve a static file from the configured directory.
     *
     * @param requestPath The requested file path
     * @return A StaticFileResult containing file data and content type, or null if
     *         not found
     */
    public StaticFileResult serveStaticFile(String requestPath) {
        try {
            String resourcePath = staticFilesDirectory + requestPath;
            try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
                if (inputStream != null) {
                    byte[] fileData = readAllBytes(inputStream);
                    String contentType = ContentType.get(requestPath);
                    return new StaticFileResult(fileData, contentType, true);
                }
            }
            return new StaticFileResult(null, null, false);

        } catch (IOException e) {
            System.err.println("Error serving static file: " + requestPath + " - " + e.getMessage());
            return new StaticFileResult(null, null, false);
        }
    }

    /**
     * Checks if a static file exists at the given path.
     *
     * @param requestPath The path to check
     * @return true if the file exists, false otherwise
     */
    public boolean staticFileExists(String requestPath) {
        String resourcePath = staticFilesDirectory + requestPath;
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            return inputStream != null;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reads all bytes from an InputStream.
     * Utility method for reading file contents.
     *
     * @param inputStream The InputStream to read from
     * @return Byte array containing all data from the stream
     * @throws IOException If an I/O error occurs
     */
    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int bytesRead;

        while ((bytesRead = inputStream.read(data)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        return buffer.toByteArray();
    }

    /**
     * Inner class to encapsulate static file serving results.
     * Uses the Builder pattern concept for result data.
     */
    public static class StaticFileResult {
        private final byte[] data;
        private final String contentType;
        private final boolean found;

        /**
         * Constructor for static file result.
         *
         * @param data        The file data as byte array
         * @param contentType The MIME type of the file
         * @param found       Whether the file was found
         */
        public StaticFileResult(byte[] data, String contentType, boolean found) {
            this.data = data;
            this.contentType = contentType;
            this.found = found;
        }

        /**
         * Gets the file data.
         *
         * @return Byte array of file contents
         */
        public byte[] getData() {
            return data;
        }

        /**
         * Gets the content type.
         *
         * @return MIME type string
         */
        public String getContentType() {
            return contentType;
        }

        /**
         * Checks if the file was found.
         *
         * @return true if file exists, false otherwise
         */
        public boolean isFound() {
            return found;
        }
    }
}
