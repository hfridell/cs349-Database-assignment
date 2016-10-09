import java.sql.*;
import java.io.*;

public class JDBCExample {

    private Connection con;
    private Statement stmt;

    // Load class and create a connection to the database
    public JDBCExample() throws SQLException {
        // The ODBC Data Source Name (DSN) is example
        //String url = "jdbc:odbc:example";
        String url = "";
        String userID = "";
        String password = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(java.lang.ClassNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }

        con = DriverManager.getConnection(url,userID,password);
        stmt = con.createStatement();
    }

    public void cleanup() throws SQLException {
        // Close connection and statement
        // Connections, statements, and result sets are
        // closed automatically when garbage collected
        // but it is a good idea to free them as soon
        // as possible.
        // Closing a statement closes its current result set.
        // Operations that cause a new result set to be
        // created for a statement automatically close
        // the old result set.
        stmt.close();
        con.close();
    }

    public void createTables() throws SQLException { }
    public void updateTable() throws SQLException { }
    public void queryTable() throws SQLException { }

    public static void main(String args[]) {
        try {
            JDBCExample ex = new JDBCExample();

//          ex.createTables();
//          ex.updateTable();
//          ex.queryTable();

            ex.cleanup();
        }
        catch (SQLException e) {
            System.err.println(e);
        }
    }
}