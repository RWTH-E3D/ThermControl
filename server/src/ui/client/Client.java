/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui.client;

import java.time.LocalDateTime;

public class Client {
    private LocalDateTime lastRequest;
    private String origin;
    
    public Client(String origin) {
        this.setOrigin(origin);
    }
    
    public void requestRegistered() {
        lastRequest = LocalDateTime.now();
    }

    public LocalDateTime getLastRequest() {
        return lastRequest;
    }

    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
