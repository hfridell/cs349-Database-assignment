package edu.umkc;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class DbControllerTest {
  DbController controller;
  Statement stmt;

  @org.junit.Before
  public void setUp() throws Exception {
    controller = DbController.getInstance();
  }

  @org.junit.After
  public void tearDown() throws Exception {
    cleanup();
  }

  public void connectionTest() throws SQLException {
    String sqlCmd1 = "SELECT * FROM ACCOUNT";
    stmt = controller.createStatement();
    ResultSet rs = stmt.executeQuery(sqlCmd1);
    displayResultSet(rs, System.out);

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
    controller.close();
  }

  public void displayResultSet(ResultSet rs, PrintStream out) {
    int i;

    out.println();
    out.println();
    out.println("Result Set:");
    out.println("------------------------");
    out.println();

    try {
      ResultSetMetaData rsmd = rs.getMetaData();

      int numCols = rsmd.getColumnCount();

      for (i = 1; i <= numCols; i++) {
        out.print(rsmd.getColumnLabel(i));
        out.print(" ");
      }
      out.println();

      boolean more = rs.next();
      while (more) {
        for (i = 1; i <= numCols; i++) {
          // Every field value is fetched as a string
          // JDBC will convert the values from their
          // stored type to string type
          out.print(rs.getString(i));
          out.print(" ");
        }
        out.println();

        more = rs.next();
      }
      out.println();
    } catch (SQLException ex) {
      out.println("*** SQLException");

      while (ex != null) {
        out.println("SQLState: " + ex.getSQLState());
        out.println("Message: " + ex.getMessage());
        out.println("Vendor: " + ex.getErrorCode());
        ex = ex.getNextException();
        out.println("");
      }
    }
  }
}