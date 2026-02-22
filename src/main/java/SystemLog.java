import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for logging all incidents and dispatches in the system.
 * It maintains a list of log entries with action information and provides methods to add new entries and retrieve the log history.
 *
 * @param <T> The type of items to be stored in the system log.
 * @author Matei Costinescu
 * @version 1.0
 */
public class SystemLog<T> implements Serializable {
    private List<LogEntry<T>> logItems;

    /**
     * Constructor for the SystemLog class. Initializes the logItems list.
     */
    public SystemLog() {
        this.logItems = new ArrayList<>();
    }

    /**
     * Adds a new entry to the system log with an action type.
     *
     * @param item The item to be logged.
     * @param action The action performed on the item (ADDED or DISPATCHED).
     */
    public void add(T item, LogEntry.Action action) {
        logItems.add(new LogEntry<>(item, action));
    }

    /**
     * Displays all log entries in the system log.
     */
    public void display(){
        if(logItems.isEmpty()){
            System.out.println("No log entries found.");
        } else {
            System.out.println("\n--- System Log ---");
            for (LogEntry<T> entry : logItems) {
                System.out.println("- " + entry.toString());
            }
        }
    }
}
