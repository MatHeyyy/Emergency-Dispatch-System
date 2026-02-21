import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * This class holds data about incidents in emergency calls
 * @author Matei Costinescu
 * @version 1.0
 */

public class Incident{
    private String type;
    private String district;
    private int priority;
    private UUID id;
    private Instant timeRecorded;

    /**
     * Constructor for the Incident class
     * @param type
     * @param district
     * @param priority
     */
    public Incident(String type, String district, int priority){
        this.id = UUID.randomUUID();
        this.timeRecorded = Instant.now();
        this.type = type;
        this.district = district;
        this.priority = priority;
    }

    // --- Getters ---

    /**
     * Gets the type of the incident
     * @return type of the incident
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the district of the incident
     * @return district of the incident
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Gets the priority of the incident
     * @return priority of the incident
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Gets the unique identifier of the incident
     * @return unique identifier of the incident
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the time when the incident was recorded
     * @return time when the incident was recorded
     */
    public Instant getTimeRecorded() {
        return timeRecorded;
    }

    /**
     * Gets the wait time in seconds since the incident was recorded
     * @return wait time in seconds since the incident was recorded
     */
    public long getWaitTimeInSeconds(){
        return Duration.between(timeRecorded, Instant.now()).getSeconds();
    }

    //---- Methods ----

    /**
     * Returns a string representation of the incident, including its ID, time recorded, wait time, type, district, and priority level.
     * The priority level is displayed as "High" for priority 1 and "Normal" for priority 0.
     * @return string representation of the incident
     */
    @Override
    public String toString(){
        String priorityLevel = (priority == 1) ? "High" : "Normal";
        return String.format("[ID: %s | Time: %s | Wait: %ds | Type: %s, District: %s, Priority: %s]",
                id.toString().substring(0, 8), timeRecorded.toString(), getWaitTimeInSeconds(),
                type, district, priorityLevel);
    }
}
