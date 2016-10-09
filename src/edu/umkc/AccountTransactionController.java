package edu.umkc;


public class AccountTransactionController {
  AccountTransactionLayout layout;
  DbController dbController;

  String[] columnNames = {
      "Account ID",
      "Account Name",
      "Balance"};
  Object[][] data = {
      {}
  };

  AccountTransactionController(AccountTransactionLayout layout) {
    this.layout = layout;
    dbController = new DbController();
  }

  public void transfer() {
    Transaction transaction = new Transaction(
        layout.fromField.getText(),
        layout.toField.getText(),
        layout.amountField.getText());

    updateModel();
  }

  private void updateModel() {

  }

  public void clear() {
    layout.fromField.setText("");
    layout.toField.setText("");
    layout.amountField.setText("");
    // restart transaction
  }
}
