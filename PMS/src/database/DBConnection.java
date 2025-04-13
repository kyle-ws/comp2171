package database;

import patient_management.MedicalHistory;
import patient_management.Patient;
import user_management.Doctor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DBConnection {
    private static Connection conn;
    private static String url;
    private static String username = "blairwcee";
    private static String password = "MakingherSQL@5";

    public DBConnection () {
    }

    public static void setUrl(String schema) {
        url = "jdbc:mysql://127.0.0.1:5150/" + schema;
    }

    public static void createConnection() {
        try {
            conn = DriverManager.getConnection(url, username, password);
        }
        catch(SQLException se) {
            se.fillInStackTrace();
        }
    }

    public static boolean savePatients(ArrayList<Patient> patients) {
        setUrl("pms");
        String insertSQL = "INSERT INTO patients (pid, first_name, middle_name, last_name, gender, dob, email_address, phone_number, address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        createConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(insertSQL);

            for (Patient p : patients) {
                stmt.setString(1, p.getPatientID());
                stmt.setString(2, p.getFName());
                stmt.setString(3, p.getMName());
                stmt.setString(4, p.getLName());
                stmt.setString(5, p.getGender());
                stmt.setDate(6, java.sql.Date.valueOf(p.getDob()));
                stmt.setString(7, p.getEmailAddress());
                stmt.setString(8, p.getContactNo());
                stmt.setString(9, p.getAddress());

                stmt.addBatch();
                return true;
            }
        }
        catch (SQLException se) {
            se.fillInStackTrace();
        }
        return false;
    }

    // Fix save method to save conditions in a string that can be deconstructed methodically
    public static ArrayList<Patient> loadPatients() {
        String url = "jdbc:mysql://127.0.0.1:5150/pms";
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, username, password);
            String query = "SELECT * FROM PATIENTS";
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                String pid = resultSet.getString("pid");
                String fName = resultSet.getString("first_name");
                String mName = resultSet.getString("middle_name");
                String lName = resultSet.getString("last_name");
                String gender = resultSet.getString("gender");
                LocalDate dob = resultSet.getDate("dob").toLocalDate();
                String emailAddress = resultSet.getString("email_address");
                String contactNo = resultSet.getString("phone_number");
                String address = resultSet.getString("address");

                Patient currPatient = new Patient(pid, fName, mName, lName, gender, dob, emailAddress, contactNo, address);
                patients.add(currPatient);
            }
        }
        catch (SQLException se) {
            se.fillInStackTrace();
        }
        return patients;
    }

    public static MedicalHistory loadMedicalHistory(String pid) {
        MedicalHistory retmh = null;
        setUrl("pms");
        createConnection();

        try {
            String queryMR = "SELECT * FROM MEDICAL_RECORDS WHERE patient_id like " + pid;
            Statement stmtMR = conn.createStatement();
            ResultSet resultSetMR = stmtMR.executeQuery(queryMR);

            if (resultSetMR.wasNull()) {
                retmh = new MedicalHistory(pid);
            }
            else {
                MedicalHistory currMedHistory = new MedicalHistory(pid);

                String lastVisitDate = resultSetMR.getString("last_visit_date");
                int numVisits = resultSetMR.getInt("num_visits");
                String conditionsTxt = resultSetMR.getString("conditions");
                String visitsInfoTxt = resultSetMR.getString("visits_info");

                currMedHistory.setLastVisitDate(lastVisitDate);
                currMedHistory.setNumVisits(numVisits);
                retmh = currMedHistory;
            }
        }
        catch (SQLException se) {
            se.fillInStackTrace();
        }
        return retmh;
    }

    public static boolean saveDoctor(Doctor doctor) {
        setUrl("pms");
        createConnection();

        if (conn == null) {
            System.out.println("Failed to connect to database.");
            return false;
        }

        String insertSQL = "INSERT INTO PMS.DOCTORS (doctor_id, first_name, middle_initial, last_name, specialization) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setString(1, doctor.getID());
            stmt.setString(2, doctor.getFirstName());
            stmt.setString(3, doctor.getMiddleInitial());
            stmt.setString(4, doctor.getLastName());
            stmt.setString(5, doctor.getSpecialization());

            stmt.execute();
            stmt.close();
            System.out.println("true");
            return true;

        } catch (SQLException se) {
            se.fillInStackTrace();
            return false;
        }
    }

    public static boolean saveDoctors(ArrayList<Doctor> doctors) {
        setUrl("pms");
        createConnection();
        String insertSQL = "INSERT INTO doctors (doctor_id, first_name, middle_initial, last_name, specialization) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(insertSQL);

            for (Doctor d : doctors) {
                System.out.println("Inserting doctor: " + d.getID());
                stmt.setString(1, d.getID());
                stmt.setString(2, d.getFirstName());
                stmt.setString(3, d.getMiddleInitial());
                stmt.setString(4, d.getLastName());
                stmt.setString(5, d.getSpecialization());
                stmt.addBatch();
            }

            conn.setAutoCommit(false);
            stmt.executeBatch();
            conn.commit();
            return true;
        }
        catch (SQLException se) {
            se.fillInStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {

        ArrayList<Doctor> doctorList = new ArrayList<>();

        // Sample doctors
        doctorList.add(new Doctor("Calvin", "R", "Klein", "Ophthalmology"));
        doctorList.add(new Doctor("Alice", "M", "Lee", "Pediatrics"));
        doctorList.add(new Doctor("John", "S", "Doe", "Cardiology"));
        doctorList.add(new Doctor("Emily", "T", "Clark", "Neurology"));
        doctorList.add(new Doctor("Samuel", "L", "King", "Orthopedics"));
        doctorList.add(new Doctor("Grace", "N", "Adams", "Dermatology"));
        doctorList.add(new Doctor("Brian", "P", "Stone", "ENT"));
        doctorList.add(new Doctor("Rachel", "B", "Green", "Gynaecology"));
        doctorList.add(new Doctor("Tom", "W", "Hardy", "Psychiatry"));
        doctorList.add(new Doctor("Julia", "Q", "Roberts", "Pulmonology"));
        doctorList.add(new Doctor("Nathan", "J", "Scott", "Oncology"));
        doctorList.add(new Doctor("Sophia", "L", "White", "Nephrology"));
        doctorList.add(new Doctor("Ethan", "A", "Hunt", "Urology"));
        doctorList.add(new Doctor("Olivia", "C", "Evans", "Gastroenterology"));
        doctorList.add(new Doctor("Victor", "D", "Young", "Hematology"));

        saveDoctors(doctorList);
    }

} // End of Class
