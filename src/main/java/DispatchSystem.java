import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * Core management system for the Emergency Dispatch System.
 *
 * @author: Matei Costinescu
 * @version: 1.0
 */
public class DispatchSystem {
    private Map<String, Deque<Incident>> districtQueues;
    private Set<String> todayIncidentTypes;
    private Set<String> yesterdayIncidentTypes;
    private final Scanner scanner;
    private SystemLog<Incident> incidentLog;

    /**
     * Constructor for the DispatchSystem class. Initializes the district queues, sets for tracking incident types, and the scanner for user input.
     */
    public DispatchSystem(){
        this.districtQueues = new HashMap<>();
        this.districtQueues.put("central", new ArrayDeque<>());
        this.districtQueues.put("south", new ArrayDeque<>());
        this.districtQueues.put("east", new ArrayDeque<>());
        this.todayIncidentTypes = new HashSet<>();
        this.yesterdayIncidentTypes = new HashSet<>();
        this.scanner = new Scanner(System.in);
        this.incidentLog = new SystemLog<>();

        yesterdayIncidentTypes.add("fire");
        yesterdayIncidentTypes.add("medical");
        yesterdayIncidentTypes.add("security");
    }

    // --- Main Loop and Menu System ---

    /**
     * Starts the main loop of the dispatch system, allowing users to interact with the system through a menu-driven interface.
     */
    public void start(){
        boolean running = true;
        while(running){
            System.out.println("\n--- Ramstropolis Emergency Dispatch ---" +
                    "\n1. Enter new incident" +
                    "\n2. View all incidents on queue" +
                    "\n3. Dispatch next incident" +
                    "\n4. View unique incident types today" +
                    "\n5. Search incidents" +
                    "\n6. Run trend analysis" +
                    "\n7. View system log" +
                    "\n8. Save system state" +
                    "\n9. Load system state" +
                    "\n0. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch(choice){
                case "1":
                    addIncident();
                    break;
                case "2":
                    viewIncidents();
                    break;
                case "3":
                    dispatchIncident();
                    break;
                case "4":
                    viewUniqueIncidentTypes();
                    break;
                case "5":
                    searchIncidents();
                    break;
                case "6":
                    runTrendAnalysis();
                    break;
                case "7":
                    incidentLog.display();
                    break;
                case "8":
                    saveState();
                    break;
                case "9":
                    loadState();
                    break;
                case "0":
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    /**
     * Adds a new incident to the queue based on user input.
     * The user is prompted to enter the incident type, district, and priority.
     * High priority incidents are added to the front of the queue, while normal priority incidents are added to the end.
     * The incident type is also tracked in a set for today's incidents.
     */
    private void addIncident(){
        System.out.println("\n--- Enter Incident Details ---");

        System.out.print("Enter incident type (e.g., fire, medical, security): ");
        String type = scanner.nextLine().toLowerCase().trim();

        System.out.print("Enter district (e.g. central, south, east): ");
        String district = scanner.nextLine().toLowerCase().trim();

        int priority = -1;
        while(priority != 0 && priority != 1){
            System.out.print("Enter priority (0 for normal, 1 for high): ");
            try {
                priority = Integer.parseInt(scanner.nextLine().trim());
                if(priority != 0 && priority != 1){
                    System.out.println("Invalid priority. Please enter 0 or 1.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (0 or 1).");
            }
        }

        districtQueues.putIfAbsent(district, new ArrayDeque<>());
        Incident newIncident = new Incident(type, district, priority);
        if(priority == 0){
            districtQueues.get(district).addLast(newIncident);
            System.out.println("Normal priority incident added to the end of the queue.");
        } else {
            districtQueues.get(district).addFirst(newIncident);
            System.out.println("High priority incident added to the start of the queue.");
        }
        todayIncidentTypes.add(type);
        incidentLog.add(newIncident);
    }

    /**
     * Displays all incidents currently in the queue.
     * If the queue is empty, a message is shown indicating that there are no incidents.
     */
    private void viewIncidents(){
        System.out.println("\n--- Current Incidents in Queue ---");
        boolean allEmpty = districtQueues.values().stream().allMatch(Deque::isEmpty);
        if(allEmpty){
            System.out.println("No incidents in any queue.");
            return;
        }
        for(Map.Entry<String, Deque<Incident>> entry : districtQueues.entrySet()){
            String district = entry.getKey();
            Deque<Incident> queue = entry.getValue();
            if(!queue.isEmpty()){
                System.out.println("Incidents in " + district + " queue:");
                for(Incident incident : queue){
                    System.out.println(incident);
                }
            }
        }
    }

    /**
     * Dispatches the next incident in the queue to emergency services.
     * If the queue is empty, a message is shown indicating that there are no incidents to dispatch.
     */
    private void dispatchIncident(){
        System.out.println("\n--- Dispatching Next Incident ---");
        System.out.print("Enter district to dispatch from (e.g. central, south, east): ");
        String district = scanner.nextLine().toLowerCase().trim();
        Deque<Incident> queue = districtQueues.get(district);
        if(queue == null || queue.isEmpty()){
            System.out.println("No incidents to dispatch in the " + district + " district.");
        } else {
            Incident incidentToDispatch = queue.pollFirst();
            System.out.println("Dispatching incident: " + incidentToDispatch.toString());
            incidentLog.add(incidentToDispatch);
        }
    }

    /**
     * Displays the unique incident types that have been reported today.
     * If no incidents have been reported, a message is shown indicating that there are no incident types to display.
     */
    private void viewUniqueIncidentTypes(){
        System.out.println("\n--- Unique Incident Types Reported Today ---");
        if(todayIncidentTypes.isEmpty()){
            System.out.println("No incident types reported today.");
        } else {
            for(String type : todayIncidentTypes){
                System.out.println("- " + type);
            }
        }
    }

    /**
     * Allows the user to search for incidents in the queue based on a search term.
     * The search term can match either the incident type or the district.
     * If no incidents match the search term, a message is shown indicating that no incidents were found.
     */
    private void searchIncidents(){
        System.out.println("\n--- Search Incidents ---");
        System.out.print("Enter search term (type or district): ");
        String searchTerm = scanner.nextLine().toLowerCase().trim();
        boolean found = false;
        for(Map.Entry<String, Deque<Incident>> entry : districtQueues.entrySet()){
            Deque<Incident> queue = entry.getValue();
            for(Incident incident : queue){
                if(incident.getType().contains(searchTerm) || incident.getDistrict().contains(searchTerm)){
                    System.out.println(incident);
                    found = true;
                }
            }
        }
        if(!found){
            System.out.println("No incidents found matching the search term: " + searchTerm);
        }
    }

    /**
     * Runs a trend analysis comparing today's incident types with yesterday's incident types.
     * It calculates and displays the union, intersection, and difference of the two sets of incident types.
     */
    private void runTrendAnalysis(){
        System.out.println("\n--- Trend Analysis ---" +
                "\nToday's Types: " + todayIncidentTypes +
                "\nYesterday's Types: " + yesterdayIncidentTypes +
                "\n------------------------------------");

        //Union
        Set<String> unionSet = new HashSet<>(todayIncidentTypes);
        unionSet.addAll(yesterdayIncidentTypes);
        System.out.println("Union of today's and yesterday's incident types: " + unionSet);

        //Intersection
        Set<String> intersectionSet = new HashSet<>(todayIncidentTypes);
        intersectionSet.retainAll(yesterdayIncidentTypes);
        System.out.println("Intersection of today's and yesterday's incident types: " + intersectionSet);

        //Difference
        Set<String> differenceSet = new HashSet<>(todayIncidentTypes);
        differenceSet.removeAll(yesterdayIncidentTypes);
        System.out.println("Incident types reported today but not yesterday: " + differenceSet);
    }

    /**
     * Saves the current state of the system to a file using serialization.
     * The incident queue, today's incident types, yesterday's incident types, and the incident log are all saved to a file named "dispatch_data.dat".
     * If an error occurs during the saving process, an error message is displayed.
     */
    private void saveState(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("dispatch_data.dat"))){
            oos.writeObject(districtQueues);
            oos.writeObject(todayIncidentTypes);
            oos.writeObject(yesterdayIncidentTypes);
            oos.writeObject(incidentLog);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    /**
     * Loads the state of the system from a file using deserialization.
     * The incident queue, today's incident types, yesterday's incident types, and the incident log are all loaded from a file named "dispatch_data.dat".
     * If an error occurs during the loading process, an error message is displayed.
     */
    @SuppressWarnings("unchecked")
    private void loadState(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("dispatch_data.dat"))){
            districtQueues = (Map<String, Deque<Incident>>) ois.readObject();
            todayIncidentTypes = (Set<String>) ois.readObject();
            yesterdayIncidentTypes = (Set<String>) ois.readObject();
            incidentLog = (SystemLog<Incident>) ois.readObject();
            System.out.println("System state loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
        }
    }

    /**
     * Starts the application by creating an instance of the Dispatch System
     */
    static void main(String[] args) {
        DispatchSystem system = new DispatchSystem();
        system.start();
    }
}
