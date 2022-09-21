/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import config.Config;
import server.HttpClientAdapter;
import server.HttpServerAdapter;
import server.Server;
import tools.TextFileReader;
import ui.Console;

public class ControlPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Server server;
	private ManualRequestFrame manualRequestFrame;
	private HostPanel hostPanel;
	private JPanel buttonPanel;
	private JButton startStopButton;
	private JButton manualRequestButton;
	private JButton startRecordingButton;
	private JButton stopRecordingButton;
	private HttpClientAdapter httpClientAdapter;
	
	private String startRecordingXml;
	private String stopRecordingXml;
	
	public ControlPanel(Server server) {
	    this.server = server;
	    httpClientAdapter = new HttpClientAdapter();
	    setBorder(BorderFactory.createTitledBorder("Server Settings"));
	    setLayout(new BorderLayout());
	    initSettingsPanel();
	    initButtonPanel();
	}
	
	private void initSettingsPanel() {
	    hostPanel = new HostPanel();
        add(this.hostPanel, BorderLayout.BEFORE_LINE_BEGINS);
	}
	
	private void initButtonPanel() {
        startStopButton = new JButton("Start Server");
        startStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HttpServerAdapter serverAdapter = ControlPanel.this.server.getHttpServerAdapter();
                if (!serverAdapter.isStarted()) {
                    try {
                        String host = ControlPanel.this.hostPanel.getHost().trim();
                        int port = Integer.parseInt(ControlPanel.this.hostPanel.getPort());
                        
                        if (serverAdapter.start(host, port)) {
                            ControlPanel.this.startStopButton.setText("Stop Server");
                        } else {
                            ControlPanel.this.startStopButton.setText("Start Server");
                        }
                    } catch (NumberFormatException ex) {
                        Console.err.println(ex.toString());
                    }
                } else {
                    serverAdapter.stop();
                    ControlPanel.this.startStopButton.setText("Start Server");
                }
            }
        });
        
        manualRequestFrame = new ManualRequestFrame();
        manualRequestButton = new JButton("Manual Request");
        manualRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manualRequestFrame.setVisible(true);
            }
        });
        
        String dir = System.getProperty("user.dir");
        startRecordingXml = TextFileReader.readTextFile(dir + "/start-recording.xml");
        stopRecordingXml = TextFileReader.readTextFile(dir + "/stop-recording.xml");
        
        startRecordingButton = new JButton("Start Recording");
        startRecordingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientRequest(startRecordingXml);
            }
        });
        
        stopRecordingButton = new JButton("Stop Recording");
        stopRecordingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientRequest(stopRecordingXml);
            }
        });
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));
        add(buttonPanel, BorderLayout.AFTER_LINE_ENDS);
        
        buttonPanel.add(startRecordingButton);
        buttonPanel.add(manualRequestButton);
        buttonPanel.add(stopRecordingButton);
        buttonPanel.add(startStopButton);
	}
	
	private void clientRequest(String xml) {
        try {
            String host = ControlPanel.this.hostPanel.getHost().trim();
            int port = Integer.parseInt(ControlPanel.this.hostPanel.getPort());
            httpClientAdapter.executePostRequest(xml, host, port);
        } catch (NumberFormatException ex) {
            Console.err.println(ex.toString());
        }
	}
}