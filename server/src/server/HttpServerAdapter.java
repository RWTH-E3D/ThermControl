/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import config.Config;
import system.data.DataAggregator;
import ui.Console;

public class HttpServerAdapter {
    private HttpServer httpServer;
    private HttpHandlerAdapter httpHandlerWrapper;
    private boolean started;
    
    public HttpServerAdapter() {
        this.started = false;
    }
    
    public boolean start(String host, int port) {
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);
            setHttpHandlerWrapper(new HttpHandlerAdapter());
            this.httpServer.createContext("/", getHttpHandlerAdapter());
            this.httpServer.setExecutor(null); // creates a default executor
            this.httpServer.start();
            
            this.started = true; // Everything is OK
            
        } catch (NumberFormatException e) {
            Console.err.println(e.getMessage());
        } catch (BindException e) {
            Console.err.println(e.getMessage());
        } catch (IOException e) {
            Console.err.println(e.getMessage());
        }
        
        String info = "Server started: "+ host + ":" + port;
        if (this.started) {
            Console.out.println(info);
        }
        
        return this.started;
    }
    
    public void stop() {
        DataAggregator.dataContainer.getCurrentSignals().clear();
        this.httpServer.stop(0);
        this.started = false;
        Console.out.println("Server stopped");
    }

    public HttpHandlerAdapter getHttpHandlerAdapter() {
        return httpHandlerWrapper;
    }

    public void setHttpHandlerWrapper(HttpHandlerAdapter httpHandlerWrapper) {
        this.httpHandlerWrapper = httpHandlerWrapper;
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public void setHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public boolean isStarted() {
        return started;
    }
}