import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * This class represents a single entry in the system log, wrapping an incident with action information.
 * It tracks whether an incident was added or dispatched.
 *
 * @param <T> The type of item being logged.
 * @author Matei Costinescu
 * @version 1.0
 */
public class LogEntry<T> implements Serializable {
    public enum Action {
        ADDED("ADDED"),
        DISPATCHED("DISPATCHED");

        private final String label;

        Action(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private T item;
    private Action action;
    private Instant timestamp;

    /**
     * Constructor for the LogEntry class.
     *
     * @param item The item being logged.
     * @param action The action performed on the item (ADDED or DISPATCHED).
     */
    public LogEntry(T item, Action action) {
        this.item = item;
        this.action = action;
        this.timestamp = Instant.now();
    }

    // --- Getters ---
    /**
     * Gets the item being logged.
     *
     * @return The logged item.
     */
    public T getItem() {
        return item;
    }

    /**
     * Gets the action performed.
     *
     * @return The action (ADDED or DISPATCHED).
     */
    public Action getAction() {
        return action;
    }

    /**
     * Gets the timestamp of the log entry.
     *
     * @return The timestamp.
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a string representation of the log entry.
     *
     * @return A formatted string showing the action and item details.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("GMT"));
        String formattedTime = formatter.format(timestamp);
        return String.format("[%s] %s (at %s)", action.getLabel(), item.toString(), formattedTime);
    }
}
