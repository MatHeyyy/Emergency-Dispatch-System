import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

/**
 * Core management system for the Emergency Dispatch System.
 *
 * @author: Matei Costinescu
 * @version: 1.0
 */
public class DispatchSystem {
    private Deque<Incident> incidentQueue;
    private Set<String> todayIncidentTypes;
    private Set<String> yesterdayIncidentTypes;
    private Scanner scanner;

    /**
     * Constructor for the DispatchSystem class. Initializes the incident queue, sets for tracking incident types, and the scanner for user input.
     */
    public DispatchSystem(){
        this.incidentQueue = new ArrayDeque<>();
        this.todayIncidentTypes = new HashSet<>();
        this.yesterdayIncidentTypes = new HashSet<>();
        this.scanner = new Scanner(System.in);

        yesterdayIncidentTypes.add("fire");
        yesterdayIncidentTypes.add("medical");
        yesterdayIncidentTypes.add("security");
    }

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

        Incident newIncident = new Incident(type, district, priority);
        if(priority == 0){
            incidentQueue.addLast(newIncident);
            System.out.println("Normal priority incident added to the end of the queue.");
        } else {
            incidentQueue.addFirst(newIncident);
            System.out.println("High priority incident added to the start of the queue.");
        }
        todayIncidentTypes.add(type);
    }

    /**
     * Displays all incidents currently in the queue.
     * If the queue is empty, a message is shown indicating that there are no incidents.
     */
    private void viewIncidents(){
        System.out.println("\n--- Current Incidents in Queue ---");
        if(incidentQueue.isEmpty()){
            System.out.println("No incidents in the queue.");
            return;
        } else {
            for(Incident incident : incidentQueue){
                System.out.println(incident.toString());
            }
        }
    }

    /**
     * Displays the unique incident types that have been reported today.
     * If no incidents have been reported, a message is shown indicating that there are no incident types to display.
     */
    private void dispatchIncident(){
        System.out.println("\n--- Dispatching Next Incident ---");
        if(incidentQueue.isEmpty()){
            System.out.println("No incidents to dispatch.");
            return;
        } else {
            Incident dispatechedIncident = incidentQueue.pollFirst();
            System.out.println("Dispatched: " + dispatechedIncident.toString() + "to emergency services.");
        }
    }

    /**
     * Starts the application by creating an instance of the Dispatch System
     */
    public static void main(String[] args) {
        DispatchSystem system = new DispatchSystem();
        system.start();
    }
}
