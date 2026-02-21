import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for logging all incidents and dispatches in the system.
 * It maintains a list of log entries and provides methods to add new entries and retrieve the log history.
 *
 * @param <T> The type of log entries to be stored in the system log.
 * @author Matei Costinescu
 * @version 1.0
 */
public class SystemLog<T> {
    private List<T> logItems;

    /**
     * Constructor for the SystemLog class. Initializes the logItems list.
     */
    public SystemLog() {
        this.logItems = new ArrayList<>();
    }

    /**
     * Adds a new entry to the system log.
     *
     * @param item The item to be added to the log.
     */
    public void add(T item) {
        logItems.add(item);
    }


    public void display(){
        if(logItems.isEmpty()){
            System.out.println("No log entries found.");
        } else {
            System.out.println("\n--- System Log ---");
            for (T item : logItems) {
                System.out.println("- " + item.toString());
            }
        }
    }
}
