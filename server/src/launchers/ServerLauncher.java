/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package launchers;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import config.Config;
import system.data.DataAggregator;
import system.data.Recording;
import system.time.Time;
import system.time.TimeThread;
import ui.Console;
import ui.MainFrame;

public class ServerLauncher {
	public static void main(String args[]) {
        // Set native look and feel:
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        	e.printStackTrace(Console.err);
            e.printStackTrace();
        } catch (InstantiationException e) {
        	e.printStackTrace(Console.err);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
        	e.printStackTrace(Console.err);
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
        	e.printStackTrace(Console.err);
            e.printStackTrace();
        }
        
        // Initialize singletons:
        Console.init();
        Config.init();
        Time.init();
        TimeThread.init();
        DataAggregator.init();
        
        //Start user interface:
        new MainFrame();
        
        // Create default recording:
        Recording recording = new Recording();
        recording.setRecording(true);
        DataAggregator.addRecording(recording);
	}
}