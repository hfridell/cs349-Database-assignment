package edu.umkc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Transaction {
  private final int toAccountNumber;
  private final int fromAccountNumber;
  private final int amount;
  private DbController db = DbController.getInstance();

  Transaction(String from, String to, String amount) {
    this.toAccountNumber = Integer.valueOf(to);
    this.fromAccountNumber = Integer.valueOf(from);
    this.amount = Integer.valueOf(amount);

    transact();
  }

  Transaction(int from, int to, int amount) {
    this.toAccountNumber = to;
    this.fromAccountNumber = from;
    this.amount = amount;

    transact();
  }
  private void transact() {
    try {
      if (validTransfer()){
        transfer();
      } else{
        // error
      }
    } catch (SQLException e){
      e.printStackTrace();
    }
  }

  private void transfer() throws SQLException {
    String toTransfer =
        "UPDATE account SET account_balance = account_balance + "
            + amount + " WHERE account_id = " + toAccountNumber;
    String fromTransfer =
        "UPDATE account SET account_balance = account_balance - "
            + amount + " WHERE account_id = " + fromAccountNumber;

    Statement toStatement = null;
    Statement fromStatement = null;

    try {
      db.setAutoCommit(false);
      toStatement = db.createStatement();
      fromStatement = db.createStatement();

      toStatement.executeUpdate(toTransfer);
      fromStatement.executeUpdate(fromTransfer);
      db.commit();

    } catch (SQLException e) {
      if (db.connection() != null) {
        try {
          System.err.print("Transaction is being rolled back");
          db.rollback();
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }

    } finally {
      if (toStatement != null) {
        toStatement.close();
      }
      if (fromStatement != null) {
        fromStatement.close();
      }
      db.setAutoCommit(true);
    }
  }

  private boolean validTransfer() throws SQLException {
    PreparedStatement fetchAmount = null;
    String amountString = "SELECT Account_Balance FROM account WHERE account_id = ?";

    try {
      fetchAmount = db.prepareStatement(amountString);
      fetchAmount.setInt(1, fromAccountNumber);
      ResultSet rs = fetchAmount.executeQuery();
      rs.next();
      return rs.getInt(1) >= amount;
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (fetchAmount != null) {
        fetchAmount.close();
      }
    }
    return false;
  }
}
