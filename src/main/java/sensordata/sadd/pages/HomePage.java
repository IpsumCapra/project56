package main.java.sensordata.sadd.pages;

import main.java.sensordata.sadd.database.QueryDemo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends Page implements ActionListener {
    //<editor-fold desc="Swing elements">
    public JPanel homePage;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel username;
    private JButton logUitButton;
    private JPanel userIconPanel;
    private JLabel warningLabel;
    private JLabel errorLabel;
    private JPanel overview;
    private JPanel results;
    private JButton backToOverview;
    private JScrollPane shortcutPanel;
    private JScrollPane overviewPanel;
    private JTable resultTable;
    private JPanel contentPanel;
    private JLabel queryResult;
    //</editor-fold>

    private static final String SEARCH = "search";
    private static final String TO_OVERVIEW = "to overview";

    QueryDemo querySystem = new QueryDemo();


    public HomePage(CardLayout cards, Container parent) {
        super(cards, parent);

        searchButton.setActionCommand(SEARCH);
        searchButton.addActionListener(this);
        searchField.setActionCommand(SEARCH);
        searchField.addActionListener(this);

        backToOverview.setActionCommand(TO_OVERVIEW);
        backToOverview.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case SEARCH:
                DefaultTableModel res;
                long start = 0;
                long end = 0;
                try {
                    start = System.nanoTime();
                    res = querySystem.query(searchField.getText());
                    end = System.nanoTime();
                } catch (Exception ex) {
                    res = null;
                    queryResult.setText("Something went wrong. Try again later, or contact support.");
                }

                if (res != null) {
                    queryResult.setText("Query OK (Got " + res.getColumnCount() + " COLS, " + res.getRowCount() + " ROWS in " + (end - start) / 1000000 + "ms.)");
                    resultTable.setModel(res);
                }

                ((CardLayout) contentPanel.getLayout()).show(contentPanel, "results");
                break;
            case TO_OVERVIEW:
                ((CardLayout) contentPanel.getLayout()).show(contentPanel, "overview");
                break;
        }
    }
}
