/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package server;

import config.Config;
import system.data.DataAggregator;
import ui.Console;

public class Server {
    private HttpServerAdapter httpServerAdapter;
    private DataAggregator dataAggregator;
    
    public Server() {}
    
    public void initServerComponents() {
        Console.out.println(Config.attribute("Application", "name") + " v" + Config.attribute("Application", "version"));
        
        // Initialize HTTP server interface:
        httpServerAdapter = new HttpServerAdapter();
    }

    public HttpServerAdapter getHttpServerAdapter() {
        return httpServerAdapter;
    }

    public void setHttpServerAdapter(HttpServerAdapter httpServerInterface) {
        this.httpServerAdapter = httpServerInterface;
    }

    public DataAggregator getDataAggregator() {
        return dataAggregator;
    }

    public void setDataAggregator(DataAggregator dataAggregator) {
        this.dataAggregator = dataAggregator;
    }
}