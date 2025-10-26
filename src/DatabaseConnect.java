import Dijkstra.AdjacencyList.AdjacencyList;
import Models.*;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

// DatabaseConnect: handles all database interactions including connection management and basic crud operations.
// this class is responsible for translating model objects to sql statements and for building the adjacency list used by routing
public class DatabaseConnect
{
    private Connection conn = null;
    private ArrayList<Patient> patients;
    private ArrayList<Doctor> doctors;
    private ArrayList<Appointment> appointments;
    private ArrayList<Location> locations;
    private ArrayList<RouteStats> routes;

    private final int LOCKOUT_DURATION = 3600;
    private final int MAX_LOGIN_ATTEMPTS = 3;


    public DatabaseConnect()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");//specify the sqlite java driver
            conn = DriverManager.getConnection("jdbc:sqlite:HospitalDatabase.db");//specify the database, since relative in the main project folder
            conn.setAutoCommit(false); // important: disable auto-commit so commits are performed only after successful statements
            System.out.println("Opened database successfully");
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // closes the active database connection and logs any sql exceptions that occur during close
    public void close()
    {
        try
        {
            conn.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // creates a patient record in the database by constructing an insert statement from the patient model.
    public boolean createPatient(Patient patient)
    {
        boolean isTaskFinished = false;
        Statement stmt;

        try
        {
            stmt = conn.createStatement();
            String sql = "INSERT INTO Patient (FirstName, LastName, Age, Sex) " +
                    "VALUES ('" + patient.FirstName() + "', '" +
                    patient.LastName() + "', '" +
                    patient.Age() + "', '" +
                    patient.Sex() + "');";

            stmt.executeUpdate(sql);

            stmt.close();
            conn.commit(); // commit the transaction only after successful insert
            isTaskFinished = true;

        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return isTaskFinished;
    }

    // updates an existing patient record using fields from the model and skips any fields the user indicated as unchanged.
    public boolean updatePatient(Patient patient)
    {
        boolean bUpdate = false;
        Statement stmt;

        try
        {
            stmt = conn.createStatement();
            String sql = "UPDATE Patient SET ";

//          conditional statements are added to the sql statement upon user request
            if (!patient.FirstName().equals("/"))
            {
                sql += "FirstName = " + "'" + patient.FirstName() + "'" + ", ";
            }
            if (!patient.LastName().equals("/"))
            {
                sql += "LastName = " + "'" + patient.LastName() + "'" + ", ";
            }
            if (patient.Age() != "/".charAt(0))
            {
                sql += "Age = " + "'" + patient.Age() + "'" + ", ";
            }
            if (patient.Sex() != '/')
            {
                sql += "Sex = " + "'" + patient.Sex() + "'" + ", ";
            }

//          at the end of the sql string, there will be this string - ", ", so that needs to be removed
            sql = sql.substring(0, sql.length() - 2); // remove trailing comma and space added in the loop above
            sql += " WHERE PatientID = " + patient.ID() + ";";

            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return bUpdate;
    }

    // deletes a patient and commits the change to the database, returning true on success.
    public boolean deletePatient(int patientID)
    {
        boolean isTaskFinished = false;
        Statement stmt;

        try
        {
            stmt = conn.createStatement();
            String stmt1 = "DELETE FROM Patient WHERE PatientID = " + patientID;
            String stmt2 = "DELETE FROM Appointment WHERE PatientID = " + patientID;

            stmt.executeUpdate(stmt1);
            stmt.executeUpdate(stmt2);

            stmt.close();
            conn.commit();
            isTaskFinished = true;
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return isTaskFinished;
    }

    // creates a doctor record in the database by converting the doctor model into an insert statement.
    // note: similar to createPatient this uses string concatenation for convenience in this exercise.
    public boolean createDoctor(Doctor doctor)
    {
        boolean isTaskFinished = false;
        Statement stmt;

        try
        {
            stmt = conn.createStatement();
            String sql = "INSERT INTO Doctor (FirstName, LastName, Specialisation, Sex) " +
                    "VALUES ('" + doctor.FirstName() + "', '" +
                    doctor.LastName() + "', '" +
                    doctor.Specialisation() + "', '" +
                    doctor.Sex() + "');";

            stmt.executeUpdate(sql);

            stmt.close();
            conn.commit();
            isTaskFinished = true;

        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return isTaskFinished;
    }

    // updates an existing doctor record and similarly removes the trailing comma before executing the sql update.
    public boolean updateDoctor(Doctor doctor)
    {
        boolean bUpdate = false;
        Statement stmt;

        try
        {
            stmt = conn.createStatement();
            String sql = "UPDATE Doctor SET ";

//          conditional statements are added to the sql statement upon user request
            if (!doctor.FirstName().equals("/"))
            {
                sql += "FirstName = " + "'" + doctor.FirstName() + "'" + ", ";
            }
            if (!doctor.LastName().equals("/"))
            {
                sql += "LastName = " + "'" + doctor.LastName() + "'" + ", ";
            }
            if (!doctor.Specialisation().equals("/"))
            {
                sql += "Specialisation = " + "'" + doctor.Specialisation() + "'" + ", ";
            }
            if (doctor.Sex() != '/')
            {
                sql += "Sex = " + "'" + doctor.Sex() + "'" + ", ";
            }

//          at the end of the sql string, there will be this string - ", ", so that needs to be removed
            sql = sql.substring(0, sql.length() - 2);
            sql += " WHERE DoctorID = " + doctor.ID() + ";";

            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return bUpdate;
    }

    // deletes a doctor and their corresponding appointments
    public boolean deleteDoctor(int doctorID)
    {
        boolean isTaskFinished = false;
        Statement stmt;

        try
        {
            stmt = conn.createStatement();
            String stmt1 = "DELETE FROM Appointment WHERE DoctorID = " + doctorID;
            String stmt2 = "DELETE FROM Doctor WHERE DoctorID = " + doctorID;

            stmt.executeUpdate(stmt1);
            stmt.executeUpdate(stmt2);

            stmt.close();
            conn.commit();
            isTaskFinished = true;
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return isTaskFinished;
    }

    // saves a feedback entry to the database with a boolean and a text comment.
    public boolean createFeedbackEntry(Feedback feedback)
    {
        boolean isTaskFinished = false;
        Statement stmt;

        try
        {
            stmt = conn.createStatement();
            String sql = "INSERT INTO Feedback (WasHelpful, Comment) " +
                    "VALUES (" + feedback.WasHelpful() + ", '" +
                    feedback.Comment() + "');";

            stmt.executeUpdate(sql);

            stmt.close();
            conn.commit();
            isTaskFinished = true;

        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return isTaskFinished;
    }

    // checks whether a route record exists for the pair of start and end location ids.
    public boolean hasRouteBeenUsed(int startLocation, int endLocation)
    {
        Statement stmt;
        ResultSet rs;
        boolean doesRouteExist = false;

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT 1 FROM RouteStats WHERE FromLocationID = " + startLocation
                            + " AND ToLocationID = " + endLocation + " LIMIT 1");

            if (rs.next())
            {
                doesRouteExist = true;
            }


            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return doesRouteExist;
    }

    // increments the request count for an existing route or inserts a new row if the route was never used before
    // this method relies on hasRouteBeenUsed to decide whether to update or insert the route stats record
    public void updateRouteStats(int startLocation, int endLocation)
    {
        if (hasRouteBeenUsed(startLocation, endLocation))
        {
            Statement stmt;

            try
            {
                stmt = conn.createStatement();

                String sql = "UPDATE RouteStats SET ";
                sql += "RequestCount = RequestCount + 1";
                sql += " WHERE RouteStats.FromLocationID = " + startLocation;
                sql += " AND RouteStats.ToLocationID = " + endLocation;

                stmt.executeUpdate(sql);
                stmt.close();
                conn.commit();
            } catch (Exception e)
            {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }

        }
        else
        {
            Statement stmt;

            try
            {
                stmt = conn.createStatement();
                String sql = "INSERT INTO RouteStats (FromLocationID, ToLocationID, RequestCount) " +
                        "VALUES (" + startLocation + ", " + endLocation + ", 1);";


                stmt.executeUpdate(sql);

                stmt.close();
                conn.commit();
            } catch (Exception e)
            {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }

        }
    }

    // inserts a new appointment record into the appointment table using the appointment model
    public boolean createAppointment(Appointment appointment)
    {
        boolean isTaskFinished = false;
        Statement stmt;

        try
        {
            stmt = conn.createStatement();
            String sql = "INSERT INTO Appointment (PatientID, " +
                    "DoctorID, Date, Reason) " +
                    "VALUES ('" + appointment.PatientID() + "', '" +
                    appointment.DoctorID() + "', '" +
                    appointment.Date() + "', '" +
                    appointment.Reason() + "');";

            stmt.executeUpdate(sql);

            stmt.close();
            conn.commit();
            isTaskFinished = true;

        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return isTaskFinished;
    }

    // updates the date for an existing appointment identified by appointmentid and commits the change
    public boolean updateAppointment(Appointment appointment, LocalDate newDate)
    {
        boolean bUpdate = false;
        Statement stmt;

        try
        {
            stmt = conn.createStatement();
            String sql = "UPDATE Appointment SET ";

            sql += "Date = " + "'" + newDate + "'";

            sql += " WHERE PatientID = " + appointment.PatientID();
            sql += " AND DOCTORID = " + appointment.DoctorID();

            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return bUpdate;
    }


    // fetches all patient rows from the database and converts each row into a patient model object
    public ArrayList<Patient> selectPatients()
    {
        Statement stmt;
        ResultSet rs;

        try
        {
            patients = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Patient;");

            while (rs.next())
            {
                int id = rs.getInt("PatientID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                int age = rs.getInt("Age");
                char sex = rs.getString("Sex").charAt(0);

                //instantiate the patient object from the resultset row
                Patient patient = new Patient(id, firstName, lastName, age, sex);
                //append to the list returned by this method
                patients.add(patient);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return patients;
    }

    // fetches all doctor rows and converts them into doctor model objects for use elsewhere in the application
    public ArrayList<Doctor> selectDoctors()
    {
        Statement stmt;
        ResultSet rs;

        try
        {
            doctors = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Doctor;");

            while (rs.next())
            {
                int id = rs.getInt("DoctorID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String specialisation = rs.getString("Specialisation");
                char sex = rs.getString("Sex").charAt(0);

                //instantiate the doctor object from the current row
                Doctor doctor = new Doctor(id, firstName, lastName, specialisation, sex);
                //add the object to the result list
                doctors.add(doctor);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return doctors;
    }

    // fetches all appointment rows and maps them to appointment model objects, parsing the date string to a LocalDate
    public ArrayList<Appointment> selectAppointments()
    {
        Statement stmt;
        ResultSet rs;

        try
        {
            appointments = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Appointment ORDER BY Date;");

            while (rs.next())
            {
                int patientID = rs.getInt("PatientID");
                int doctorID = rs.getInt("DoctorID");
                LocalDate date = LocalDate.parse(rs.getString("Date")); // parse date string to localdate for the appointment object
                String reason = rs.getString("Reason");

                //instantiate the appointment object
                Appointment appointment = new Appointment(patientID, doctorID, date, reason);
                //collect appointment objects into a list
                appointments.add(appointment);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return appointments;
    }

    // fetches all locations which are later used by the routing algorithm and user menus
    public ArrayList<Location> selectLocations()
    {
        Statement stmt;
        ResultSet rs;

        try
        {
            locations = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Location;");

            while (rs.next())
            {
                int id = rs.getInt("LocationID");
                String locationName = rs.getString("LocationName");
                String locationType = rs.getString("Type");

                //instantiate the location object for each row
                Location location = new Location(id, locationName, locationType);
                //collect into the list to return
                locations.add(location);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return locations;
    }

    // selects route statistics ordered by request count and constructs route stats objects for display in the menu
    public ArrayList<RouteStats> selectRoutes()
    {
        Statement stmt;
        ResultSet rs;

        try
        {
            routes = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM RouteStats ORDER BY RequestCount Desc;");

            while (rs.next())
            {
                int source = rs.getInt("FromLocationID");
                int destination = rs.getInt("ToLocationID");
                int requestCount = rs.getInt("RequestCount");

                RouteStats route = new RouteStats(source, destination, requestCount);

                routes.add(route);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return routes;
    }

    // returns a patient model populated from the row with the given patient id, or null if not found
    public Patient getPatientByID(int patientID)
    {
        Statement stmt;
        ResultSet rs;
        Patient patient = null;

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Patient WHERE PatientID = " + patientID);

            while (rs.next())
            {
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                int age = rs.getInt("Age");
                char sex = rs.getString("Sex").charAt(0);


                // instantiate the patient object from the selected row
                patient = new Patient(patientID, firstName, lastName, age, sex);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        // return the patient object
        return patient;
    }

    // returns a doctor model populated from the row with the given doctor id, or null if the doctor does not exist
    public Doctor getDoctorByID(int doctorID)
    {
        Statement stmt;
        ResultSet rs;
        Doctor doctor = null;

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Doctor WHERE DoctorID = " + doctorID);

            while (rs.next())
            {
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String specialisation = rs.getString("Specialisation");
                char sex = rs.getString("Sex").charAt(0);


                // instantiate the doctor object from the selected row
                doctor = new Doctor(firstName, lastName, specialisation, sex);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        // return the doctor object
        return doctor;
    }

    // returns a location model for the provided location id or null if not found
    public Location getLocationByID(int locationID)
    {
        Statement stmt;
        ResultSet rs;
        Location location = null;

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Location WHERE LocationID = " + locationID);

            while (rs.next())
            {
                String locationName = rs.getString("LocationName");
                String type = rs.getString("Type");

                // instantiate the location object from the result row
                location = new Location(locationID, locationName, type);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        // return the location object
        return location;
    }

    // builds an adjacency list representing paths between locations by reading the path table and adding two-way edges
    public AdjacencyList buildPathsAdjacencyList()
    {
        AdjacencyList adj = new AdjacencyList();
        Statement stmt;
        ResultSet rs;

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Path;");

            while (rs.next())
            {
//              int id = rs.getInt("RouteID");
                int sourceLocation = rs.getInt("FromLocationID");
                int destinationLocation = rs.getInt("ToLocationID");
                int distance = rs.getInt("Distance");

                // add a bidirectional edge between the two location nodes in the adjacency list
                adj.addTwoWayEdge(Integer.toString(sourceLocation), Integer.toString(destinationLocation), distance);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return adj;
    }

    //  for an incorrect login, the number of failed attempts at accessing the same acount must be incremented by 1
    private void updateAttempts(Admin admin, int attempts)
    {
        try
        {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Admin SET FailedAttempts = " + attempts + " WHERE Username = '" + admin.Username() + "';");
            conn.commit();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    //  if a login is successful, the number of failed attempts must be reset to 0
    private void resetAttempts(Admin admin)
    {
        try
        {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Admin SET FailedAttempts = 0, LockoutUntil = 0 WHERE Username = '" + admin.Username() + "'");
            conn.commit();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    //  the user is given three attempts to log in with the same username before lockout
//  if login is unsuccessful, the system locks the user out for one hour
    private void lockAccount(Admin admin, long lockoutUntil)
    {
        try
        {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Admin SET FailedAttempts = 0, LockoutUntil = " + lockoutUntil + " WHERE Username = '" + admin.Username() + "'");
            conn.commit();
            stmt.close();
        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    //  takes admin data and attempts to match it to a profile on the database
//  if the login is successful, this method returns true
    public boolean verifyLogin(Admin admin)
    {
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Password, FailedAttempts, LockoutUntil FROM Admin WHERE Username = '" + admin.Username() + "'");

            if (!rs.next())
            {
                System.out.println();
                System.out.println("User not found.");
                rs.close();
                stmt.close();
                return false;
            }

            String storedPassword = rs.getString("Password");
            int attempts = rs.getInt("FailedAttempts");
            long lockoutUntil = rs.getLong("LockoutUntil");
            rs.close();
            stmt.close();

            long now = Instant.now().getEpochSecond();

            if (lockoutUntil > now)
            {
                long minutes = (lockoutUntil - now) / 60;
                System.out.println();
                System.out.println("Account locked. Try again in " + minutes + " minutes.");
                return false;
            }

            // Password check
            if (storedPassword.equals(admin.Password()))
            {
                resetAttempts(admin);
                return true;
            }
            else
            {
                attempts++;
                if (attempts >= MAX_LOGIN_ATTEMPTS)
                {
                    long oneHourLater = now + LOCKOUT_DURATION; // lock for 1 hour
                    lockAccount(admin, oneHourLater);

                    System.out.println();
                    System.out.println("Too many failed attempts. Account locked for 1 hour.");
                }
                else
                {
                    updateAttempts(admin, attempts);

                    System.out.println();
                    System.out.println("Incorrect password. Attempts left: " + (MAX_LOGIN_ATTEMPTS - attempts));
                }
                return false;
            }

        } catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    // getting count from a table
    private int getTableCount(String tableName, String columnName)
    {
        int count = 0;
        String sql = String.format("SELECT COUNT(*) AS %s FROM %s;", columnName, tableName);
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                count = rs.getInt(columnName);
            }
        } catch (SQLException e)
        {
            System.err.println("Error getting count for table " + tableName + ": " + e.getMessage());
        }
        return count;
    }

    // gets total patient count
    public int getTotalPatientCount()
    {
        return getTableCount("Patient", "totalPatients");
    }

    // gets total doctor count
    public int getTotalDoctorCount()
    {
        return getTableCount("Doctor", "totalDoctors");
    }

    // gets total appointment count
    public int getTotalAppointmentCount()
    {
        return getTableCount("Appointment", "totalAppointments");
    }

    // gets total feedback count
    public int getTotalFeedbackCount()
    {
        return getTableCount("Feedback", "totalFeedback");
    }

    // gets count of helpful feedback
    public int getHelpfulFeedbackCount()
    {
        int count = 0;
        String sql = "SELECT COUNT(*) AS helpfulFeedback FROM Feedback WHERE WasHelpful = 1;";
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                count = rs.getInt("helpfulFeedback");
            }
        } catch (SQLException e)
        {
            System.err.println("Error getting helpful feedback count: " + e.getMessage());
        }
        return count;
    }

}
