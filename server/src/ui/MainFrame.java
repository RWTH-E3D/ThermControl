/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import config.Config;
import ui.client.ClientsPanel;
import ui.server.ServerPanel;

public class MainFrame extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private ClientsPanel clientPanel;
    private ServerPanel serverPanel;
    private JToolBar toolBar;
    
    public MainFrame() {
        setPreferredSize(new Dimension(800, 250));
        setTitle(Config.attribute("Application", "name"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setLayout(new BorderLayout());
        
        initToolBar();
        initServerPanel();
        initClientPanel();
        
        // Change the MainFrame icon:
        Image im = null;
        try {
            im = ImageIO.read(MainFrame.class.getResource("server.png"));
        } catch (IOException ex) {
            
        }
        
        this.setIconImage(im);
        this.pack();
        this.setVisible(true);
        setLocationRelativeTo(null);
    }
    
    private void initToolBar() {
        toolBar = new JToolBar();
        this.add(toolBar, BorderLayout.PAGE_START);
    }
    
    private void initServerPanel() {
        serverPanel = new ServerPanel();
        this.add(serverPanel, BorderLayout.CENTER);
    }
    
    public void initClientPanel() {
        clientPanel = new ClientsPanel(serverPanel.getServer());
        clientPanel.setBorder(BorderFactory.createTitledBorder("Connected Clients"));
        this.add(clientPanel, BorderLayout.LINE_END);
    }
}