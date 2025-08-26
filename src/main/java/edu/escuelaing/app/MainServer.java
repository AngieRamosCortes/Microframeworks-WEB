package edu.escuelaing.app;

/**
 * Example application demonstrating the Web Framework usage.
 * This class shows how developers can use the framework to create
 * web applications with REST services and static file serving.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class MainServer {

    /**
     * Main method that demonstrates framework usage.
     * Sets up routes and static file serving, then starts the server.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {

        WebFramework.staticfiles("/webroot");

        WebFramework.get("/hello", (req, resp) -> {
            String name = req.getValues("name");
            if (name.isEmpty()) {
                name = "World";
            }
            return "Hello " + name + "!";
        });

        WebFramework.get("/pi", (req, resp) -> {
            resp.json();
            return "{\"value\": " + Math.PI + ", \"description\": \"Mathematical constant PI\"}";
        });

        WebFramework.get("/random", (req, resp) -> {
            resp.json();
            int randomNumber = (int) (Math.random() * 1000) + 1;
            return "{\"randomNumber\": " + randomNumber + "}";
        });

        WebFramework.get("/calc", (req, resp) -> {
            resp.json();
            try {
                String aStr = req.getValues("a");
                String bStr = req.getValues("b");
                String operation = req.getValues("op");

                if (aStr.isEmpty() || bStr.isEmpty() || operation.isEmpty()) {
                    resp.status(400);
                    return "{\"error\": \"Missing parameters. Use: /calc?a=10&b=5&op=add\"}";
                }

                double a = Double.parseDouble(aStr);
                double b = Double.parseDouble(bStr);
                double result;

                switch (operation.toLowerCase()) {
                    case "add":
                        result = a + b;
                        break;
                    case "subtract":
                        result = a - b;
                        break;
                    case "multiply":
                        result = a * b;
                        break;
                    case "divide":
                        if (b == 0) {
                            resp.status(400);
                            return "{\"error\": \"Division by zero not allowed\"}";
                        }
                        result = a / b;
                        break;
                    default:
                        resp.status(400);
                        return "{\"error\": \"Unsupported operation. Use: add, subtract, multiply, divide\"}";
                }

                return "{\"a\": " + a + ", \"b\": " + b + ", \"operation\": \"" + operation + "\", \"result\": "
                        + result + "}";

            } catch (NumberFormatException e) {
                resp.status(400);
                return "{\"error\": \"Invalid number format\"}";
            }
        });

        WebFramework.get("/health", (req, resp) -> {
            resp.json();
            return "{\"status\": \"healthy\", \"timestamp\": " + System.currentTimeMillis() + "}";
        });

        WebFramework.get("/about", (req, resp) -> {
            resp.html();
            return "<html><body><h1>Web Framework Demo</h1>" +
                    "<p>This is a demonstration of the custom web framework.</p>" +
                    "<p>Available endpoints:</p>" +
                    "<ul>" +
                    "<li><a href='/hello?name=Pedro'>/hello?name=Pedro</a></li>" +
                    "<li><a href='/pi'>/pi</a></li>" +
                    "<li><a href='/random'>/random</a></li>" +
                    "<li><a href='/calc?a=10&b=5&op=add'>/calc?a=10&b=5&op=add</a></li>" +
                    "<li><a href='/health'>/health</a></li>" +
                    "</ul>" +
                    "</body></html>";
        });

        WebFramework.start(8080);

        System.out.println("\n--- Framework Demo Usage ---");
        System.out.println("Try these endpoints:");
        System.out.println("  http://localhost:8080/hello?name=Pedro");
        System.out.println("  http://localhost:8080/pi");
        System.out.println("  http://localhost:8080/random");
        System.out.println("  http://localhost:8080/calc?a=10&b=5&op=add");
        System.out.println("  http://localhost:8080/health");
        System.out.println("  http://localhost:8080/about");
        System.out.println("  http://localhost:8080/index.html (static files)");
        System.out.println("---------------------------\n");
    }
}