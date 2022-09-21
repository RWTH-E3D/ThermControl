/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import events.DataEvent;

public class ClientPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LocalDateTime lastUpdated;
    private DateTimeFormatter dateTimeFormatter;
    private JPanel mainPanel;
    private JLabel lastUpdateLabel;
    private JLabel numSignalsLabel;
    private JPanel headerPanel;
    private JPanel ledPanel;

    public ClientPanel(Client client) {
        setPreferredSize(new Dimension(175, 60));
        setMaximumSize(new Dimension(175, 60));
        
        setBorder(BorderFactory.createTitledBorder("Origin: " + client.getOrigin()));
        
        setLayout(new BorderLayout());
        
        lastUpdateLabel     = new JLabel();
        dateTimeFormatter   = DateTimeFormatter.ofPattern("hh:mm:ss");
        numSignalsLabel     = new JLabel();
        
        ledPanel = new JPanel();
        ledPanel.setPreferredSize(new Dimension(4, 4));
        headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(20, 10));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(ledPanel);
        headerPanel.setBackground(Color.GRAY);
        add(headerPanel, BorderLayout.BEFORE_LINE_BEGINS);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));
        mainPanel.add(new JLabel("  Last update:"));
        mainPanel.add(lastUpdateLabel);
        mainPanel.add(new JLabel("  Signals:"));
        mainPanel.add(numSignalsLabel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    public void update(DataEvent dataEvent) {
        lastUpdated = LocalDateTime.now();
        lastUpdateLabel.setText(lastUpdated.format(dateTimeFormatter));
        numSignalsLabel.setText("" + dataEvent.getNumberOfSignals());
        ledPanel.setBackground(Color.GREEN);
    }
    
    public void timedUpdate() {
        ledPanel.setBackground(Color.GRAY);
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}