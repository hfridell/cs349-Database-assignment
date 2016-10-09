package edu.umkc;


import java.sql.SQLException;
import java.util.Vector;

public class AccountTransactionController {
  AccountTransactionLayout layout;
  DbController dbController;

  Vector<String> columnNames = new Vector<>();
  columnNames.add("Account ID");
  columnNames.add("Account Name");
  columnNames.add("Balance");

  Vector<Vector<Object>> data;

  AccountTransactionController(AccountTransactionLayout layout) {
    this.layout = layout;
    dbController = new DbController();
    updateModel();
  }

  public void transfer() {
    Transaction transaction = new Transaction(
        layout.fromField.getText(),
        layout.toField.getText(),
        layout.amountField.getText());

    updateModel();
  }

  private void updateModel() {
    String[] col = {"Account_ID", "Account_Name", "Account_Balance"};
    try {
      data = dbController.query(col, "account");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void clear() {
    layout.fromField.setText("");
    layout.toField.setText("");
    layout.amountField.setText("");
    // restart transaction
  }
}
