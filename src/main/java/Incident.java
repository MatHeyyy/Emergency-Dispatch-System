/**
 * This class holds data about incidents in emergency calls
 * @author Matei Costinescu
 * @version 1.0
 */

public class Incident{
    private String type;
    private String district;
    private int priority;

    /**
     * Constructor for the Incident class
     * @param type
     * @param district
     * @param priority
     */
    public Incident(String type, String district, int priority){
        this.type = type;
        this.district = district;
        this.priority = priority;
    }

    // Getters for the Incident class

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
     * Overrides the toString method to provide a string representation of the incident
     * @return string representation of the incident
     */
    @Override
    public String toString(){
        String priorityLevel = (priority == 1) ? "High" : "Normal";
        return String.format("Incident Type: %s, District: %s, Priority: %s]", type, district, priorityLevel);
    }
}
