package edu.umkc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class DbController {

  private static DbConnection db  = new DbConnection();

  @Deprecated
  public Connection getConnection() {
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


  }

}
