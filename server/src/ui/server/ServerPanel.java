/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui.server;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import events.MessageEvent;
import listeners.MessageListener;
import server.Server;
import ui.Console;

public class ServerPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Server server;
    
    private ControlPanel controlPanel;
    private JPanel consolePanel;
    
    public ServerPanel() {
        this.setBorder(new TitledBorder("Output"));
        this.setLayout(new BorderLayout());
        this.initServer();
        this.consolePanel = new JPanel();
        this.add(consolePanel, BorderLayout.CENTER);
        this.consolePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        this.consolePanel.setLayout(new BorderLayout());
        this.consolePanel.add(Console.consoleScrollPane, BorderLayout.CENTER);
        this.initControlPanel();
    }
    
    private void initControlPanel() {
        this.controlPanel = new ControlPanel(this.server);
        this.add(this.controlPanel, BorderLayout.PAGE_END);
    }
    
    public void initServer() {
        server = new Server();
        server.initServerComponents();
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}