package edu.umkc;


import java.sql.SQLException;
import java.util.List;

public class AccountTransactionController {
  AccountTransactionLayout layout;
  DbController dbController;

  Object[] columnNames = {"Account_ID", "Account_Name", "Account_Balance"};

  Object[][] data = {{}};

  AccountTransactionController(AccountTransactionLayout layout) {
    this.layout = layout;
    dbController = DbController.getInstance();
    updateModel();
  }

  public void transfer() {
    Transaction transaction = new Transaction(
        layout.fromField.getText(),
        layout.toField.getText(),
        layout.amountField.getText());

    updateModel();
    clear();
  }

  private void updateModel() {
    String[] col = {"Account_ID", "Account_Name", "Account_Balance"};
    List<List<Object>> queryResults;

    try {
      queryResults = dbController.query(col, "account");
      int size = queryResults.size();
      data = new Object[size][];
      for (int i = 0; i < size; i++) {
        data[i] = queryResults.get(i).toArray();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    layout.updateUi();
  }

  public void clear() {
    layout.fromField.setText("");
    layout.toField.setText("");
    layout.amountField.setText("");
  }
}
