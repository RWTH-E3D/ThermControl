/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import config.Config;
import ui.Console;

public class HttpClientAdapter {
    private CloseableHttpClient httpClient;
    
    public HttpClientAdapter() {
        this.httpClient = HttpClients.createDefault();
    }
    
    public String executePostRequest(String xml, String host, int port) {
        HttpPost httpPost = new HttpPost("http://" + host + ":" + port);
        
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("xml", xml));
        String responseText = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            Console.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            responseText = EntityUtils.toString(entity);
            
            response.close();
        } catch (IOException e1) {
            Console.err.println(e1.getMessage());
        }
        
        return responseText;
    }
}
