import Dijkstra.AdjacencyList.AdjacencyList;
import Dijkstra.Dijkstra;
import Models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

// menu class: provides the interactive text menu for navigation, appointments, patients, doctors and feedback
// this class handles user input and delegates database operations to DatabaseConnect
public class Menu
{
    //  as the menu is just a list of options, we can map each menu/submenu to a corresponding list of values
    private final ArrayList<String> mainMenu = new ArrayList<>(Arrays.asList("Navigation", "Appointment Maintenance", "Patient Maintenance", "Doctor Maintenance", "Anonymous Feedback", "View System Statistics"));
    private final ArrayList<String> navigation = new ArrayList<>(Arrays.asList("Find Route", "View Route Stats"));
    private final ArrayList<String> appointmentMaintenance = new ArrayList<>(Arrays.asList("Create Appointment", "View Appointments", "Update Appointment"));
    private final ArrayList<String> patientMaintenance = new ArrayList<>(Arrays.asList("Create Patient", "Remove Patient", "Modify Patient", "View Patient"));
    private final ArrayList<String> doctorMaintenance = new ArrayList<>(Arrays.asList("Create Doctor", "Remove Doctor", "Modify Doctor", "View Doctor"));

    // our entities, which become populated after retrieving information from the database
    private ArrayList<Patient> patients;
    private ArrayList<Doctor> doctors;
    private ArrayList<Appointment> appointments;
    private ArrayList<Location> locations;
    private ArrayList<RouteStats> routes;

    // build the adjacency list for pathfinding using the database connection
    private final AdjacencyList adj = conn.buildPathsAdjacencyList();
    private static DatabaseConnect conn = new DatabaseConnect(); // single database connector used by menu methods

    private boolean isAdminLoggedIn = false;
    private final Validation validation = new Validation();

    // returns the main menu so it can be called in the main() function
    public ArrayList<String> mainMenu()
    {
        return mainMenu; // returns the list of top-level menu options
    }

    private enum Operation
    {
        CREATE, UPDATE, DELETE
    }

    Scanner in = new Scanner(System.in);

    //  displays the options for a specific menu
    public void display(ArrayList<String> array)
    {

//      the counter increments with each option
        int counter = 1;
        int choice;

        System.out.println();
        System.out.println("Please select one of the following: ");
        System.out.println();

        for (String option : array)
        {
            System.out.println("(" + counter + ") " + option); // print numbered options for the provided array
            counter++;
        }

        System.out.println();
        System.out.print("Option: ");
        choice = validation.validateIntegerInRange(this.in, 1, array.size()); // read the numeric choice from the user

//      depending on the choice and the array being displayed, the action will be executed
//      e.g. if the customer has pressed 1 and the array being displayed is the customerMaintenance, a new customer will be created
        int INVALID_ID = -1;
        switch (choice)
        {
            case 1:
                if (array.equals(mainMenu))
                {
                    display(navigation); // if main menu and user selects 1, show navigation submenu
                }
                else if (array.equals(navigation))
                {
                    findRoute(); // execute find route flow
                }
                else if (array.equals(appointmentMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    createAppointment(); // create a new appointment
                }
                else if (array.equals(patientMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    managePatient(Operation.CREATE, INVALID_ID); // create a patient (use -1 as placeholder id)
                }
                else if (array.equals(doctorMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    manageDoctor(Operation.CREATE, INVALID_ID); // create a doctor (use -1 as placeholder id)
                }
                else
                {
                    System.out.println("We don't have the facilities for that. Try again later.");
                }
                break;
            case 2:
                if (array.equals(mainMenu))
                {
                    display(appointmentMaintenance); // show appointment submenu
                }
                else if (array.equals(navigation))
                {
                    viewRouteStats();
                }
                else if (array.equals(appointmentMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    viewAppointment();
                }
                else if (array.equals(patientMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    deletePatient();
                }
                else if (array.equals(doctorMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    removeDoctor();
                }
                else
                {
                    System.out.println("We don't have the facilities for that. Try again later.");
                }
                break;
            case 3:
                if (array.equals(mainMenu))
                {
                    display(patientMaintenance);
                }
                else if (array.equals(appointmentMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    updateAppointment();
                }
                else if (array.equals(patientMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    updatePatient();
                }
                else if (array.equals(doctorMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    updateDoctor();
                }
                else
                {
                    System.out.println("We don't have the facilities for that. Try again later.");
                }
                break;
            case 4:
                if (array.equals(mainMenu))
                {
                    display(doctorMaintenance); // show doctor submenu
                }
                else if (array.equals(patientMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    viewPatient();
                }
                else if (array.equals(doctorMaintenance))
                {
                    if (!isAdminLoggedIn)
                    {
                        adminLogin();
                    }
                    viewDoctor();
                }
                else
                {
                    System.out.println("We don't have the facilities for that. Try again later.");
                }
                break;
            case 5:
                if (array.equals(mainMenu))
                {
                    createFeedbackEntry(); // accept anonymous feedback from the user
                }
                else
                {
                    System.out.println("We don't have the facilities for that. Try again later.");
                }
                break;
            case 6:
                if (array.equals(mainMenu))
                {
                    displaySystemStatistics();
                }
                else
                {
                    System.out.println("We don't have the facilities for that. Try again later.");
                }
                break;
            default:
                System.out.println("We don't have the facilities for that. Try again later.");

        }

    }

    //  depending on the operation, we create, update or delete a patient
//  if we are updating or deleting a patient, we need to take in the relevant information
//  otherwise we can delete the patient straight away
    public void managePatient(Operation operation, int patientID)
    {
        Scanner in = new Scanner(System.in);

        String firstName;
        String lastName;
        String updateNotice = "";
        int age;
        char sex;

        if (operation == Operation.DELETE)
        {
            boolean result = conn.deletePatient(patientID); // call database to delete patient

            if (result)
            {
                System.out.println();
                System.out.println("Patient successfully deleted.");
            }
            return;
        }

        if (operation == Operation.UPDATE)
        {
            updateNotice = ", or type '/' to leave unchanged"; // inform user how to skip fields when updating
        }

        System.out.println();
        System.out.print("Enter the patient's first name" + updateNotice + ": ");
        firstName = in.next();

        System.out.print("Enter the patient's last name" + updateNotice + ": ");
        lastName = in.next();

        System.out.print("Enter the patient's age" + updateNotice + ": ");
        int MIN_PATIENT_AGE = 1;
        age = validation.validateAge(this.in, MIN_PATIENT_AGE);

        System.out.print("Enter the patient's sex (M for male, F for female)" + updateNotice + ": ");
        sex = validation.validateCharacterSet(this.in, new ArrayList<>(Arrays.asList('M', 'F'))); // read single character for sex

        if (operation == Operation.CREATE)
        {
            Patient newPatient = new Patient(firstName, lastName, age, sex); // construct a new patient instance
            boolean result = conn.createPatient(newPatient); // insert patient into database

            if (result)
            {
                System.out.println();
                System.out.println("Patient successfully created.");
            }
        }
//      in the case of updating a patient:
        else
        {
            Patient updatedPatient = new Patient(patientID, firstName, lastName, age, sex); // build updated patient with id
            boolean result = conn.updatePatient(updatedPatient); // update patient record in database

            if (result)
            {
                System.out.println();
                System.out.println("Patient successfully updated.");
            }
        }
    }

    //  requests the user for the patient's ID so it can be updated using managePatient()
    public void updatePatient()
    {
        int updateID;

        displayPatients(); // show patients list so user can pick an id

        System.out.println();
        System.out.print("Select an ID number to update a patient: ");
        updateID = validation.validateInteger(in); // read chosen patient id

        managePatient(Operation.UPDATE, updateID); // delegate to managePatient with update operation
    }

    //  after fetching the patients from the database, we can get the user to enter a patientID
//  to view a patient in more detail
    public void viewPatient()
    {
        int patientID;

        displayPatients(); // list patients before asking for selection

        System.out.println();
        System.out.print("Select an ID to view a patient's details: ");

        patientID = validation.validateInteger(in);
        System.out.println();

        System.out.println("First Name: " + patients.get(patientID - 1).FirstName());
        System.out.println("Last Name: " + patients.get(patientID - 1).LastName());
        System.out.println("Age: " + patients.get(patientID - 1).Age());
        System.out.println("Sex: " + patients.get(patientID - 1).Sex());

    }

    //  removes a patient and any entries where they are mentioned from the database
    public void deletePatient()
    {
        int deleteID;

        displayPatients(); // list patients so user can choose which to delete

        System.out.println();
        System.out.print("Select an ID number to delete a patient: ");
        deleteID = validation.validateInteger(in);

        managePatient(Operation.DELETE, deleteID); // delegate to managePatient with delete operation
    }

    //  depending on the operation, we create, update or delete a doctor
//  if we are updating or deleting a doctor, we need to take in the relevant information
//  otherwise we can delete the doctor straight away
    public void manageDoctor(Operation operation, int doctorID)
    {
        Scanner in = new Scanner(System.in);

        String firstName;
        String lastName;
        String updateNotice = "";
        String specialisation;
        char sex;

        if (operation == Operation.DELETE)
        {
            boolean result = conn.deleteDoctor(doctorID); // call database to delete the doctor

            if (result)
            {
                System.out.println();
                System.out.println("Doctor successfully deleted.");
            }
            return;
        }

        if (operation == Operation.UPDATE)
        {
            updateNotice = ", or type '/' to leave unchanged"; // hint shown to user when updating fields
        }

        System.out.print("Enter the doctor's first name" + updateNotice + ": ");
        firstName = in.next(); // read first name

        System.out.print("Enter the doctor's last name" + updateNotice + ": ");
        lastName = in.next(); // read last name

        System.out.print("Enter the doctor's specialisation" + updateNotice + ": ");
        specialisation = in.next(); // read specialisation

        System.out.print("Enter the doctor's sex" + updateNotice + ": ");
        sex = validation.validateCharacterSet(this.in, new ArrayList<>(Arrays.asList('M', 'F'))); // read sex character

        if (operation == Operation.CREATE)
        {
            Doctor newDoctor = new Doctor(firstName, lastName, specialisation, sex); // create a new doctor object
            boolean result = conn.createDoctor(newDoctor); // insert doctor into database

            if (result)
            {
                System.out.println();
                System.out.println("Doctor successfully created.");
            }
        }
        else
        {
            Doctor updatedDoctor = new Doctor(doctorID, firstName, lastName, specialisation, sex); // prepare updated doctor with id
            boolean result = conn.updateDoctor(updatedDoctor); // update doctor record in db

            if (result)
            {
                System.out.println();
                System.out.println("Doctor successfully updated.");
            }
        }
    }

    //  displays a list of doctors and asks the user which doctor they would like to remove from the system
//  the doctor is then removed from the system
    public void removeDoctor()
    {
        int deleteID;

        displayDoctors();
        System.out.println();
        System.out.print("Select an ID number to delete a doctor: ");
        deleteID = validation.validateInteger(in); // selected doctor id to delete

        manageDoctor(Operation.DELETE, deleteID); // perform deletion via manageDoctor
    }

    //  gathers user information to update the doctor
    public void updateDoctor()
    {
        int updateID;

        displayDoctors(); // display doctors so user can pick one

        System.out.println();
        System.out.print("Select an ID number to update a doctor: ");
        updateID = validation.validateInteger(in); // chosen doctor id

        manageDoctor(Operation.UPDATE, updateID); // delegate update to manageDoctor
    }

    //  displays detailed information about a doctor on request
    public void viewDoctor()
    {
        int doctorID;

        displayDoctors(); // list doctors before selection

        System.out.println();
        System.out.print("Select an ID to view a doctor's details: ");

        doctorID = validation.validateInteger(in);
        System.out.println();

        System.out.println("First Name: " + doctors.get(doctorID - 1).FirstName());
        System.out.println("Last Name: " + doctors.get(doctorID - 1).LastName());
        System.out.println("Specialisation: " + doctors.get(doctorID - 1).Specialisation());
        System.out.println("Sex: " + doctors.get(doctorID - 1).Sex());
    }

    //  creates an appointment onto the system
    public void createAppointment()
    {
        Scanner in = new Scanner(System.in);

        int doctorID;
        int patientID;
        LocalDate date;
        String reason;
        Appointment appointment;

        displayDoctors(); // show doctors for selection
        System.out.println();
        System.out.print("Enter the doctor's ID: ");
        doctorID = validation.validateInteger(this.in); // read doctor id

        displayPatients(); // show patients for selection
        System.out.println();
        System.out.print("Enter the patient's ID: ");
        patientID = validation.validateInteger(this.in);
        in.nextLine();

        System.out.println();
        System.out.print("Enter the date of the appointment: ");
        date = validation.validateDate(this.in); // parse user input to a LocalDate

        System.out.print("Enter the reason for the appointment: ");
        reason = in.nextLine(); // read the reason text

        appointment = new Appointment(patientID, doctorID, date, reason); // construct appointment object

        boolean result = conn.createAppointment(appointment);

        if (result)
        {
            System.out.println();
            System.out.print("Appointment successfully created.");
        }
    }

    //  displays the detailed information for an appointment record on request
    public void viewAppointment()
    {
        int appointmentID;

        displayAppointments(); // show list of appointments

        System.out.println();
        System.out.print("Select an ID to view an appointment's details: ");

        appointmentID = validation.validateInteger(this.in);
        System.out.println();

        System.out.println("Patient: " + conn.getPatientByID(appointments.get(appointmentID - 1).PatientID()).FirstName() + " " + conn.getPatientByID(appointments.get(appointmentID - 1).PatientID()).LastName()); // show patient name for appointment
        System.out.println("Doctor: " + conn.getDoctorByID(appointments.get(appointmentID - 1).DoctorID()).FirstName() + " " + conn.getDoctorByID(appointments.get(appointmentID - 1).DoctorID()).LastName()); // show doctor name for appointment
        System.out.println("Date: " + appointments.get(appointmentID - 1).Date()); // show appointment date
        System.out.println("Reason: " + appointments.get(appointmentID - 1).Reason()); // show appointment reason
    }

    //  updates an appointment's time
    public void updateAppointment()
    {
        Scanner in = new Scanner(System.in);
        int appointmentNumber;
        LocalDate newDate;

        displayAppointments(); // list appointments before update

        System.out.println();
        System.out.print("Select an appointment number to change an appointment's time: ");

        appointmentNumber = validation.validateInteger(this.in);
        in.nextLine();

        System.out.print("Enter the new date for the appointment: ");
        newDate = validation.validateDate(this.in); // parse new date

        boolean result = conn.updateAppointment(appointments.get(appointmentNumber-1), newDate); // update appointment in database, zero-based indexing

        if (result)
        {
            System.out.println();
            System.out.println("Appointment successfully updated.");
        }
    }

    //  finds the shortest path from the main Entrance to the requested node and updates the RouteStats table accordingly
    public void findRoute()
    {
        int destination;
        int mainEntranceNodeID = 1;

        displayLocations(); // show available locations to navigate to
        System.out.println();
        System.out.print("Enter an ID to select a location to navigate to: ");

        destination = validation.validateIntegerInRange(this.in, mainEntranceNodeID, locations.size()); // chosen destination id

        System.out.println();
        System.out.print("Path: ");

        displayShortestRoute(destination, adj); // compute and display the shortest route using dijkstra

        conn.updateRouteStats(1, destination); // increment usage stats for this route
    }

    //  shows the top 5 most used locations along with their usage frequency
    public void viewRouteStats()
    {
        routes = conn.selectRoutes(); // fetch route usage statistics from database

        for (int i = 0; i < Math.min(5, routes.size()); i++)
        {
            System.out.println();
            System.out.println("Destination: " + conn.getLocationByID(routes.get(i).ToLocationID()).LocationName()); // print destination name
            displayShortestRoute(routes.get(i).ToLocationID(), adj); // display the shortest route to that destination
            System.out.println("Uses: " + routes.get(i).RequestCount()); // show how many times route was requested
        }
    }

    //  gets the user's opinion on how helpful the system was and adds this entry to the database
    public void createFeedbackEntry()
    {
        Scanner in = new Scanner(System.in);
        String comment;
        char response;
        boolean wasHelpful = false;

        System.out.print("Was the system helpful (Y/N)?: ");
        response = validation.validateCharacterSet(this.in, new ArrayList<>(Arrays.asList('Y', 'N', 'y', 'n')));
        in.nextLine();

        if (response == 'Y' || response == 'y')
        {
            wasHelpful = true;
        }

        System.out.print("Leave a comment: ");
        comment = in.nextLine(); // collect free text comment

        Feedback feedback = new Feedback(wasHelpful, comment); // build feedback object

        boolean result = conn.createFeedbackEntry(feedback);

        if (result)
        {
            System.out.println();
            System.out.println("Feedback accepted.");
        }
    }

    //  fetches and displays all patients in the Patient table
    public void displayPatients()
    {
        patients = conn.selectPatients(); // load patients from database

        System.out.println();
        System.out.println("Below is a list of patients: ");
        System.out.println();

        for (int i = 0; i < patients.size(); i++)
        {
            System.out.print("ID:" + patients.get(i).ID());
            System.out.print(" First Name:" + patients.get(i).FirstName());
            System.out.print(" Last Name:" + patients.get(i).LastName());
            System.out.println();
        }
    }

    //  fetches and displays all doctors in the Doctor table
    public void displayDoctors()
    {
        doctors = conn.selectDoctors(); // load doctors from database

        System.out.println();
        System.out.println("Below is a list of doctors: ");
        System.out.println();

        for (int i = 0; i < doctors.size(); i++)
        {
            System.out.print("ID:" + doctors.get(i).ID());
            System.out.print(" First Name:" + doctors.get(i).FirstName());
            System.out.print(" Last Name:" + doctors.get(i).LastName());
            System.out.println();
        }
    }

    //  fetches and displays all appointments in the Appointment table
    public void displayAppointments()
    {
        appointments = conn.selectAppointments(); // load appointments from database

        System.out.println();
        System.out.println("Below is the list of appointments: ");
        System.out.println();

        for (int i = 0; i < appointments.size(); i++)
        {
            System.out.print("Appointment " + (i + 1)); // print appointment id
            System.out.print(" Patient: " + conn.getPatientByID(appointments.get(i).PatientID()).FirstName() + " " + conn.getPatientByID(appointments.get(i).PatientID()).LastName()); // print patient name for appointment
            System.out.print(" Doctor: " + conn.getDoctorByID(appointments.get(i).DoctorID()).FirstName() + " " + conn.getDoctorByID(appointments.get(i).DoctorID()).LastName()); // print doctor name for appointment

            System.out.println();
        }
    }

    //  fetches and displays all locations in the Location table
    public void displayLocations()
    {
        locations = conn.selectLocations(); // load locations from database

        System.out.println();
        System.out.println("Where do you want to navigate to?: ");
        System.out.println();

        for (int i = 0; i < locations.size(); i++)
        {
            System.out.print("ID:" + locations.get(i).ID());
            System.out.print(" Name: " + locations.get(i).LocationName());
            System.out.print(" Type: " + locations.get(i).Type());

            System.out.println();
        }
    }

    //  uses Dijkstra's algorithm to calculate the shortest path from the Main Entrance to any other node
    public void displayShortestRoute(int destination, AdjacencyList adj)
    {
        Dijkstra dijkstra = new Dijkstra(); // dijkstra instance used to compute the shortest path
        int minDistance;
        String[] path;

        path = dijkstra.shortestPath(adj, "1", Integer.toString(destination)); // compute path from source "1" (root node) to destination
        minDistance = Integer.parseInt(dijkstra.distance(adj, "1", Integer.toString(destination))); // get distance as integer

        for (int i = 0; i < path.length; i++)
        {
            System.out.print(conn.getLocationByID(Integer.parseInt(path[i])).LocationName()); // print location names along the path
            if (i != path.length - 1) System.out.print(" -> ");
        }

        System.out.println();
        System.out.println("Distance: " + minDistance + " metres"); // show distance in metres
        System.out.println(estimatedWalkingTime(minDistance)); // print estimated walking time for the distance
    }

    //  collects username and password to verify an admin account
//  if this is successful, the user is granted access to the requested admin function
    public void adminLogin()
    {
        Scanner in = new Scanner(System.in);
        String username;
        String password;

        System.out.println();

        System.out.print("Enter username: ");
        username = in.next();

        System.out.print("Enter password: ");
        password = in.next();

        Admin admin = new Admin(username, password);

        if (conn.verifyLogin(admin))
        {
            isAdminLoggedIn = true;
            System.out.println();
            System.out.println("Access granted. Welcome, " + username + ".");
        }
        else
        {
            System.out.println("Access denied.");
            System.exit(0);
        }

        System.out.println();
    }

    //  estimates the time to walk a certain distance, assuming the average walking speed is 1.1 metres per second
    public String estimatedWalkingTime(int distance)
    {
        double walkingSpeed = 1.1;
        int secondsPerMinute = 60;

        double timeInSeconds = distance / walkingSpeed; // estimate walking seconds using average speed of 1.1 m/s
        return "Estimated walking time: " + (int) (timeInSeconds / secondsPerMinute) + " minutes and " + Math.round(timeInSeconds % 60) + " seconds";
    }

    //  displays system metrics to the console, such as doctor count, patient count and the number of helpful feedback entries
    public void displaySystemStatistics()
    {
        System.out.println();
        System.out.println("System Statistics:");

        int patientCount = conn.getTotalPatientCount();
        int doctorCount = conn.getTotalDoctorCount();
        int appointmentCount = conn.getTotalAppointmentCount();
        int feedbackCount = conn.getTotalFeedbackCount();
        int helpfulFeedbackCount = conn.getHelpfulFeedbackCount();

        System.out.println("Total Patients Registered: " + patientCount);
        System.out.println("Total Doctors Registered: " + doctorCount);
        System.out.println("Total Appointments Scheduled: " + appointmentCount);

        System.out.println("Total Feedback Entries: " + feedbackCount);
        if (feedbackCount > 0)
        {
            System.out.printf("Helpful Feedback Entries: %d (%.1f%%)%n",
                    helpfulFeedbackCount, (double) helpfulFeedbackCount * 100 / feedbackCount);
        }
    }
}
