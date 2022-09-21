/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import events.DataEvent;
import listeners.DataListener;
import listeners.TimeThreadListener;
import server.Server;
import system.data.DataAggregator;
import system.time.TimeThread;

public class ClientsPanel extends JPanel implements DataListener, TimeThreadListener, Runnable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private LinkedHashMap<String, ClientPanel> clientPanels;
    JScrollPane scrollPane;
    private JPanel clientPanelsPanel;

    public ClientsPanel(Server server) {
        setPreferredSize(new Dimension(210, 0));
        DataAggregator.addDataListener(this);
        clientPanels = new LinkedHashMap<String, ClientPanel>();
        this.setLayout(new FlowLayout());
        TimeThread.addListener(this, 500);
        
        clientPanelsPanel = new JPanel();
        clientPanelsPanel.setLayout(new BoxLayout(clientPanelsPanel, BoxLayout.Y_AXIS));
        
        scrollPane = new JScrollPane(clientPanelsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
    public void currentSignalsUpdated(DataEvent dataEvent) {
        final DataEvent dataEventCpy = dataEvent;
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                synchronized(clientPanels) {
                    String origin = dataEventCpy.getOrigin();
                    
                    if (clientPanels.get(origin) == null) { // Client panel does not yet exist
                        Client client = new Client(origin);
                        ClientPanel clientPanel = new ClientPanel(client);
                        client.requestRegistered();
                        clientPanels.put(origin, clientPanel);
                        clientPanelsPanel.add(clientPanel);
                        ClientsPanel.this.revalidate();
                        ClientsPanel.this.repaint();
                    }
                    
                    clientPanels.get(origin).update(dataEventCpy); // Update client panel
                }
            }
        });

    }

    public void timeThreadUpdate() {}
   
    public void timeThreadTimedUpdate() {
        // Pass to Swing thread:
        SwingUtilities.invokeLater(this);
    }

    public void run() {
        Map.Entry<String, ClientPanel> entry;
        ClientPanel clientPanel;
        long between;
        
        synchronized(clientPanels) {
            Iterator<Entry<String, ClientPanel>> it = clientPanels.entrySet().iterator();
            while (it.hasNext()) {
                entry = (Map.Entry<String, ClientPanel>) it.next();
                clientPanel = entry.getValue();
                between     = ChronoUnit.MILLIS.between(clientPanel.getLastUpdated(), LocalDateTime.now());
                
                if (between > 20000) { // Client did not send data for 10 seconds
                    it.remove(); // Remove client panel from hash map
                    synchronized (clientPanel) {
                        clientPanelsPanel.remove(clientPanel); // Remove client panel from panel
                    }
                    
                    ClientsPanel.this.revalidate();
                    ClientsPanel.this.repaint();
                } else {
                    clientPanel.timedUpdate();
                }
            }
        }
    }
}