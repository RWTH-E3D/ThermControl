/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system;

import java.util.HashMap;
import system.data.DataAggregator;
import ui.Console;

public class CommandExecutor {
    public static final String START_RECORDING = "START_RECORDING";
    public static final String STOP_RECORDING = "STOP_RECORDING";
    
    public void executeCommand(HashMap<String, String> command) {
        if (command.get("name").equals(START_RECORDING)) {
            DataAggregator.startRecording();
            Console.out.println("Start recording...");
        } else if (command.get("name").equals(STOP_RECORDING)) {
            DataAggregator.stop();
            Console.out.println("Stop recording...");
        }
    }
}