/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui.server;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import config.Config;

public class HostPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JTextField hostTextField;
    private JTextField portTextField;
    
    public HostPanel() {
        this.hostTextField = new JTextField(Config.attribute("Server", "host"));
        this.portTextField = new JTextField(Config.attribute("Server", "port"));
        
        this.setLayout(new GridLayout(2, 2));
        this.add(new JLabel("Host: "));
        this.add(this.hostTextField);
        this.add(new JLabel("Port: "));
        this.add(this.portTextField);
        this.setPreferredSize(new Dimension(200, 0));
    }
    
    public String getHost() {
        return this.hostTextField.getText();
    }
    
    public String getPort() {
        return this.portTextField.getText();
    }
}
