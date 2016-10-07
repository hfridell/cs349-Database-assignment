package edu.umkc;

public class Transaction {
  private final int to;
  private final int from;
  private final int amount;

  Transaction(String to, String from, String amount) {
    this.to = Integer.valueOf(to);
    this.from = Integer.valueOf(from);
    this.amount = Integer.valueOf(amount);

    transact();
  }

  Transaction(int to, int from, int amount) {
    this.to = to;
    this.from = from;
    this.amount = amount;

    transact();
  }
  private void transact() {
    if (fromBalance() >= amount) {
      // Start transaction
      // move money From
      // move money TO
      // End transaction
    } else {
      // error
    }
  }

  private int fromBalance(){
    // query for From balance
    return 0;
  }
}
