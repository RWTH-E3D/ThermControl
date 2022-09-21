/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package server;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import config.Config;

public class Broadcaster {
    public void broadcast(LinkedHashMap<String, LinkedHashMap<String, String>> signals) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<data>";
        xml += "<send>";
        
        for (Map.Entry<String, LinkedHashMap<String, String>> entry : signals.entrySet()) {
            LinkedHashMap<String, String> signal = entry.getValue();
            xml += "<signal>";
            xml += "<name>" + signal.get("name") + "</name>";
            xml += "<value>" + signal.get("value") + "</value>";
            xml += "</signal>";
        }
        
        xml += "</send>";
        xml += "<request></request>";
        xml += "</data>";
        
        this.sendXmlData(xml);
    }
    
    private void sendXmlData(String xml) {
        try {
            // Create Apache HTTP POST object:
            String url = "http://" + Config.attribute("Server", "host") + ":" + Config.attribute("Server", "port");
            
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            
            // Create POST data:
            List <NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("xml", xml));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            // Execute POST request:
            try {
                httpClient.execute(httpPost);
                httpClient.close();
                return;
            } catch (BindException e) {
                // e.printStackTrace();
            }
            
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }
}
