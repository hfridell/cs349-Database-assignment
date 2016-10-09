package edu.umkc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;


public class DbController {

  private static DbConnection db = new DbConnection();

  private Connection connection() {
    return db.connection;
  }

  public void close() {
    try {
      db.connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Statement createStatement() throws SQLException {
    return db.connection.createStatement();
  }

  public List<Object> query(String[] columnNames, String from) throws SQLException {
    return new Query().query(columnNames, from);
  }

  private class Query {

    private List<Object> query(String[] columnNames, String from) throws SQLException {
      Statement statement = createStatement();
      StringBuilder queryCommand = new StringBuilder();
      queryCommand.append("SELECT ").append(String.join(", ", columnNames))
          .append(" FROM ").append(from);

      ResultSet rs = statement.executeQuery(queryCommand.toString());
      List<Object> result = new Vector<>();
      while (rs.next()) {
        List<Object> row = new Vector<>();
        for (String col : columnNames) {
          row.add(rs.getObject(col));
        }
        result.add(row);
      }
      statement.close();
      return result;
    }
  }

  private void getAccountAmount(int accountId) throws SQLException {
    PreparedStatement fetchAmount = null;
    String amountString = "SELECT amount FROM account WHERE account_id = ?";

    try {
      db.setAutoCommit(false);
      fetchAmount = db.prepareStatement(amountString);
      fetchAmount.setInt(1, accountId);
      db.commit();
    } catch (SQLException e) {
      if (connection() != null) {
        try {
          System.err.print("Transaction is being rolled back");
          db.rollback();
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }

    } finally {
      if (fetchAmount != null) {
        fetchAmount.close();
      }
      db.setAutoCommit(true);
    }
  }


  private static class DbConnection {
    private String password;
    private String hostname;
    private String username;
    private String serverType;
    private String connectionInfo;
    private Connection connection;


    DbConnection() {
      FileReader config;

      try {
        config = new FileReader("db.json");
        if (config.ready()) {
          parseConfigFile(config);
          setConnectionInfo(hostname, serverType);
        }

      } catch (FileNotFoundException e) {
        manualConfiguration();
        setConnectionInfo(hostname, serverType);
      } catch (IOException e) {
        e.printStackTrace();
      }

      try {
        connection = DriverManager.getConnection(connectionInfo, username, password);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    private void manualConfiguration() {
      System.out.println("Configuration file not found.");
      Scanner in = new Scanner(System.in);
      System.out.println("Enter db hostname: ");
      hostname = in.next();
      System.out.println("Enter db type: ");
      serverType = in.next();
      System.out.println("Enter db username: ");
      username = in.next();
      System.out.println("Enter db password: ");
      password = in.next();
    }

    private void parseConfigFile(FileReader config) {
      JsonObject dbInfo = new JsonParser().parse(config).getAsJsonObject();
      username = dbInfo.get("db").getAsJsonObject().get("username").getAsString();
      password = dbInfo.get("db").getAsJsonObject().get("password").getAsString();
      hostname = dbInfo.get("db").getAsJsonObject().get("hostname").getAsString();
      serverType = dbInfo.get("db").getAsJsonObject().get("server_type").getAsString();
    }

    private void setConnectionInfo(String hostname, String serverType) {
      StringBuilder sb = new StringBuilder();
      sb.append("jdbc:");
      sb.append(serverType);
      sb.append("://");
      sb.append(hostname);
      connectionInfo = sb.toString();
    }

    private void setAutoCommit(boolean val) throws SQLException {
      connection.setAutoCommit(val);
    }

    private void commit() throws SQLException {
      connection.commit();
    }

    private void rollback() throws SQLException {
      connection.rollback();
    }

    private PreparedStatement prepareStatement(String statement) throws SQLException {
      return connection.prepareStatement(statement);
    }
  }

}
