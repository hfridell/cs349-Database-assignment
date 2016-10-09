package edu.umkc;

// This code gives the layout for the UI and
//   demonstrates two ways of updating the data
//   in a JTable.
// Another option to consider when using JTable is
//   creating your own data model by overriding
//   AbstractTableModel. You might use this option
//   if data for table was coming from say a DB.
//   One example: http://www.java2s.com/Code/Java/Swing-JFC/CreatingsimpleJTableusingAbstractTableModel.htm

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AccountTransactionLayout extends JFrame {
  private AccountTransactionController controller = new AccountTransactionController(this);
  JTable table;
  JTextField toField;
  JTextField fromField;
  JTextField amountField;


  public AccountTransactionLayout() {
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    DefaultTableModel dtm = new DefaultTableModel(controller.data, controller.columnNames);
    table = new JTable(dtm);
    // The default size of a JTable is something like 450 X 400.
    Dimension smallerSize = new Dimension(450, 50);
    table.setPreferredScrollableViewportSize(smallerSize);

    JScrollPane scrollPaneForTable = new JScrollPane(table);

    GridBagConstraints constraints = new GridBagConstraints();

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 2;
    constraints.gridheight = 1;
    constraints.weightx = 1;
    constraints.weighty = 1;
    constraints.insets = new Insets(4, 4, 4, 4);
    constraints.fill = GridBagConstraints.BOTH;

    contentPane.add(scrollPaneForTable, constraints);

    constraints.gridx = 0;
//	constraints.gridy = 1;
    constraints.weighty = 0;
    constraints.gridy = GridBagConstraints.RELATIVE;
    constraints.insets = new Insets(2, 4, 2, 4);
    constraints.fill = GridBagConstraints.NONE;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.NORTHEAST;
    JLabel toLabel = new JLabel("From:");
    contentPane.add(toLabel, constraints);

    constraints.gridx = 1;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    fromField = new JTextField("", 8);
    // Workaround, because of: http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4247013
    fromField.setMinimumSize(fromField.getPreferredSize());
    contentPane.add(fromField, constraints);

    constraints.gridx = 0;
//	constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.NORTHEAST;
    JLabel fromLabel = new JLabel("To:");
    contentPane.add(fromLabel, constraints);

    constraints.gridx = 1;
//	constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    toField = new JTextField("", 8);
    toField.setMinimumSize(toField.getPreferredSize());
    contentPane.add(toField, constraints);

    constraints.gridx = 0;
//	constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.NORTHEAST;
    JLabel amountLabel = new JLabel("Amount:");
    contentPane.add(amountLabel, constraints);

    constraints.gridx = 1;
//  constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    amountField = new JTextField("", 8);
    amountField.setMinimumSize(amountField.getPreferredSize());
    contentPane.add(amountField, constraints);

    constraints.gridx = 0;
//	constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.NORTHEAST;
    JButton clearButton = new JButton("Clear");
    contentPane.add(clearButton, constraints);
    // ATTENTION!!! The action here is just another
    //   example of how to update JTable. It is
    //   certainly not the logic for clearing the
    //   values in the GUI.
    clearButton.addActionListener(e -> controller.clear());

    constraints.gridx = 1;
//	constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    JButton transferButton = new JButton("Transfer");
    transferButton.addActionListener(e -> controller.transfer());
    contentPane.add(transferButton, constraints);
  }

  void updateUi(){
    if (table != null) {
      table.setModel(new DefaultTableModel(controller.data, controller.columnNames));
    }
  }

  public static void buildUi() {
    JFrame frame = new AccountTransactionLayout();
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }


  public static void main(String[] args) {
    JFrame frame = new AccountTransactionLayout();
    frame.pack();
    frame.setVisible(true);
  }
}