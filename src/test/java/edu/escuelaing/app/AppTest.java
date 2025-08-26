package edu.escuelaing.app;

import java.util.HashMap;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Web Framework components.
 * These tests verify the functionality of core framework classes.
 * 
 * @author WebFramework Team
 * @version 1.0
 */
public class AppTest {
    
    private Router router;
    private StaticFileHandler staticFileHandler;
    
    @Before
    public void setUp() {
        router = new Router();
        staticFileHandler = new StaticFileHandler();
    }
    
    @After
    public void tearDown() {
        WebFramework.reset();
    }
    
    @Test
    public void testRequestCreationAndQueryParams() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        
        Request request = new Request("GET", "/hello", "name=Pedro&age=25", headers);
        
        assertEquals("GET", request.getMethod());
        assertEquals("/hello", request.getPath());
        assertEquals("name=Pedro&age=25", request.getQueryString());
        assertEquals("Pedro", request.getValues("name"));
        assertEquals("25", request.getValues("age"));
        assertEquals("", request.getValues("nonexistent"));
        assertEquals("application/json", request.getHeader("content-type"));
    }
    
    @Test
    public void testResponseConfiguration() {
        Response response = new Response();
        
        assertEquals(200, response.getStatusCode());
        assertEquals("text/plain; charset=utf-8", response.getContentType());
        
        response.status(404).type("application/json").header("Custom-Header", "Value");
        
        assertEquals(404, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
        assertEquals("Value", response.getHeaders().get("Custom-Header"));
    }
    
    @Test
    public void testRouteMatching() {
        RouteHandler handler = (req, res) -> "Hello World";
        Route route = new Route("GET", "/hello", handler);
        
        assertTrue(route.matches("GET", "/hello"));
        assertFalse(route.matches("POST", "/hello"));
        assertFalse(route.matches("GET", "/goodbye"));
        
        assertEquals("GET", route.getMethod());
        assertEquals("/hello", route.getPath());
    }
    
    @Test
    public void testRouterFunctionality() {
        RouteHandler handler1 = (req, res) -> "Response 1";
        RouteHandler handler2 = (req, res) -> "Response 2";
        
        router.addRoute("GET", "/path1", handler1);
        router.addRoute("POST", "/path2", handler2);
        
        assertEquals(2, router.getRouteCount());
        assertTrue(router.hasRoute("GET", "/path1"));
        assertTrue(router.hasRoute("POST", "/path2"));
        assertFalse(router.hasRoute("GET", "/nonexistent"));
        
        assertTrue(router.findRoute("GET", "/path1").isPresent());
        assertFalse(router.findRoute("GET", "/nonexistent").isPresent());
        
        router.clearRoutes();
        assertEquals(0, router.getRouteCount());
    }
    
    @Test
    public void testStaticFileHandlerConfiguration() {
        assertEquals("/webroot", staticFileHandler.getStaticFilesDirectory());
        
        staticFileHandler.setStaticFilesDirectory("public");
        assertEquals("/public", staticFileHandler.getStaticFilesDirectory());
        
        staticFileHandler.setStaticFilesDirectory("/assets");
        assertEquals("/assets", staticFileHandler.getStaticFilesDirectory());
    }
    
    @Test
    public void testWebFrameworkRouteRegistration() {
        WebFramework.get("/test", (req, res) -> "Test Response");
        WebFramework.post("/submit", (req, res) -> "Submitted");
        
        Router frameworkRouter = WebFramework.getRouter();
        assertEquals(2, frameworkRouter.getRouteCount());
        assertTrue(frameworkRouter.hasRoute("GET", "/test"));
        assertTrue(frameworkRouter.hasRoute("POST", "/submit"));
    }
    
    @Test
    public void testWebFrameworkStaticFilesConfiguration() {
        WebFramework.staticfiles("public");
        StaticFileHandler handler = WebFramework.getStaticFileHandler();
        assertEquals("/public", handler.getStaticFilesDirectory());
    }
    
    @Test
    public void testRouteExecution() {
        HashMap<String, String> headers = new HashMap<>();
        Request testRequest = new Request("GET", "/hello", "name=TestUser", headers);
        Response testResponse = new Response();
        
        RouteHandler handler = (req, res) -> {
            String name = req.getValues("name");
            res.json();
            return "{\"greeting\": \"Hello " + name + "\"}";
        };
        
        Route route = new Route("GET", "/hello", handler);
        String result = route.execute(testRequest, testResponse);
        
        assertEquals("{\"greeting\": \"Hello TestUser\"}", result);
        assertEquals("application/json; charset=utf-8", testResponse.getContentType());
    }
    
    @Test
    public void testResponseChaining() {
        Response response = new Response()
            .status(201)
            .json()
            .header("Location", "/resource/123");
        
        assertEquals(201, response.getStatusCode());
        assertEquals("application/json; charset=utf-8", response.getContentType());
        assertEquals("/resource/123", response.getHeaders().get("Location"));
    }
    
    @Test
    public void testContentTypeUtility() {
        assertEquals("text/html; charset=utf-8", ContentType.get("index.html"));
        assertEquals("text/css; charset=utf-8", ContentType.get("style.css"));
        assertEquals("application/javascript; charset=utf-8", ContentType.get("app.js"));
        assertEquals("image/jpeg", ContentType.get("logo.jpg"));
        assertEquals("image/png", ContentType.get("icon.png"));
        assertEquals("application/octet-stream", ContentType.get("unknown.xyz"));
    }
}
