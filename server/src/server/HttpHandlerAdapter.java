/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import config.Config;
import listeners.MessageListener;
import parsers.XmlMessageParser;
import system.data.DataAggregator;
import ui.Console;
/**
 * TODO: Implement a function that retrieves historic data as CSV output
 */
public class HttpHandlerAdapter implements HttpHandler {
    XmlMessageParser xmlMessageParser;
    private Pattern urlDataPattern;
    private Matcher urlDataMatcher;
    private String data;
    private String xmlData;
    private ArrayList<MessageListener> messageListeners;
    
    public HttpHandlerAdapter() {
        setMessageListeners(new ArrayList<MessageListener>());
        xmlMessageParser = new XmlMessageParser();
        urlDataPattern = Pattern.compile(".*(xml\\s*=\\s*)(.*)", Pattern.DOTALL);
    }
    
    public void handle(HttpExchange httpExchange) {
        data = httpExchange.getRequestURI().getQuery();
        
        if (data == null) { // No GET data, use POST data
            data = convertPostData(httpExchange); 
        }
        
        try {
            if (data == null || data.trim().equals("")) {
                String errorResponse = errorResponse();
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, errorResponse.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(errorResponse.getBytes());
                os.flush();
                os.close();
            } else {
                urlDataMatcher = urlDataPattern.matcher(data.trim());
                if (urlDataMatcher.matches()) {
                    xmlData = urlDataMatcher.group(2);
                    if (xmlData == null) {
                        throw new Exception("Invalid XML data");
                    }
                } else {
                    throw new Exception("Invalid XML data");
                }
                
                try {
                    // Process sent signals:
                    xmlMessageParser.parseXml(xmlData);
                    
                    DataAggregator.createDataEvent(
                        xmlMessageParser.getSendMessage().getSignals(),
                        httpExchange.getRemoteAddress().getHostString()
                    );
                } catch (Exception e) {
                    // Skip
                }
                
                // Generate XML response:
                Headers responseHeaders = httpExchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/xml");
                responseHeaders.set("Access-Control-Allow-Origin", "*");
                
                String xmlResponse = xmlMessageParser.xmlFromSignalList(xmlMessageParser.getRequestMessage().getSignals());
                
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, xmlResponse.length());
                OutputStream os = httpExchange.getResponseBody();
                
                os.write(xmlResponse.getBytes());
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String convertPostData(HttpExchange httpExchange) {
        InputStream is = httpExchange.getRequestBody();
        String result = "";
        
        int data;
        try {
            data = is.read();
            
            while (data != -1) {
                result += (char) data;
                data = is.read();
            }
            
            String urlData = URLDecoder.decode(result, "UTF-8");
            
            return urlData;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private void processData(HttpExchange httpExchange) {
        
    }
    
    private String errorResponse() {
        String errorResponse = "";
        errorResponse += "<html>\n";
        errorResponse += "    <head>\n";
        errorResponse += "    </head>\n";
        errorResponse += "    <h1>" + Config.attribute("Application", "name") + "</h1>\n";
        errorResponse += "    <p>\n";
        errorResponse += "        This is the default output of the software. Please send a formatted XML string.\n";
        errorResponse += "    </p>\n";
        errorResponse += "</html>";
        return errorResponse;
    }

    public ArrayList<MessageListener> getMessageListeners() {
        return messageListeners;
    }

    public void setMessageListeners(ArrayList<MessageListener> messageListeners) {
        this.messageListeners = messageListeners;
    }
    
    public void addMessageListener(MessageListener messageListener) {
        this.messageListeners.add(messageListener);
    }
}