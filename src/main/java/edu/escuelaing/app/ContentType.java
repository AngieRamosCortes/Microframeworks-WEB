package edu.escuelaing.app;

public class ContentType {
    public static String get(String path) {
        if (path.endsWith(".html"))
            return "text/html; charset=utf-8";
        if (path.endsWith(".css"))
            return "text/css; charset=utf-8";
        if (path.endsWith(".js"))
            return "application/javascript; charset=utf-8";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
            return "image/jpeg";
        if (path.endsWith(".png"))
            return "image/png";
        return "application/octet-stream";
    }
}
