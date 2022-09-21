/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import server.HttpClientAdapter;
import tools.TextFileReader;
import ui.Console;

public class ManualRequestFrame extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JTextArea requestTextArea;
    private JTextArea responseTextArea;
    private JButton executeRequestButton;
    
    private JButton protocolVersion1Button;
    private JButton protocolVersion2Button;
    
    private HostPanel hostPanel;
    private JPanel controlPanel;
    private JPanel buttonPanel;
    
    private ExecutorService executor;
    
    public ManualRequestFrame() {
        setSize(new Dimension(1024, 633));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setTitle("Manual Request");

        requestTextArea = new JTextArea();
        requestTextArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        
        responseTextArea = new JTextArea();
        responseTextArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(1, 2));
        add(textPanel, BorderLayout.CENTER);
        
        JScrollPane requestScrollPane = new JScrollPane(requestTextArea);
        requestScrollPane.setBorder(BorderFactory.createTitledBorder("XML Request"));
        textPanel.add(requestScrollPane);
        
        JScrollPane responseScrollPane = new JScrollPane(responseTextArea);
        responseScrollPane.setBorder(BorderFactory.createTitledBorder("XML Response"));
        textPanel.add(responseScrollPane);
        
        requestTextArea.setText(TextFileReader.readTextFile(System.getProperty("user.dir") + "/protocol-v2.xml"));
        initControlPanel();
    }
    
    private void initControlPanel() {
        controlPanel = new JPanel();
        add(this.controlPanel, BorderLayout.PAGE_END);
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Request Settings"));
        
        hostPanel = new HostPanel();
        controlPanel.add(this.hostPanel, BorderLayout.BEFORE_LINE_BEGINS);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        controlPanel.add(this.buttonPanel, BorderLayout.AFTER_LINE_ENDS);
        
        protocolVersion1Button = new JButton("Protocol v1.0");
        protocolVersion1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestTextArea.setText(TextFileReader.readTextFile(System.getProperty("user.dir") + "/protocol-v1.xml"));
			}
        });
        buttonPanel.add(protocolVersion1Button);
        
        protocolVersion2Button = new JButton("Protocol v2.0");
        protocolVersion2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestTextArea.setText(TextFileReader.readTextFile(System.getProperty("user.dir") + "/protocol-v2.xml"));
			}
        });
        buttonPanel.add(protocolVersion2Button);
        
        
        executor = Executors.newFixedThreadPool(5);
        executeRequestButton = new JButton("Execute");
        executeRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String request = ManualRequestFrame.this.requestTextArea.getText();
                
                ManualRequestFrame.this.executor.submit(new Runnable() {
                    public void run() {
                        String host = ManualRequestFrame.this.hostPanel.getHost();
                        int port;
                        try {
                            port = Integer.parseInt(ManualRequestFrame.this.hostPanel.getPort());
                            HttpClientAdapter httpClientAdapter = new HttpClientAdapter();
                            String response = httpClientAdapter.executePostRequest(request, host, port);
                            responseTextArea.setText(response);
                        } catch (NumberFormatException e) {
                            e.printStackTrace(Console.err);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(this.executeRequestButton);
    }
}
