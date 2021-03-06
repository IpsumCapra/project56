package main.java.sensordata.sadd.pages;

import main.java.sensordata.sadd.database.QuerySystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Overview extends JPanel implements Runnable {
    // Variables.
    private String query;
    private int id;
    private int refreshRate;
    private QuerySystem querySystem;

    private JLabel nameLabel;
    private JScrollPane scrollPane;
    private JTable table;

    // Getter.
    public int getID() {
        return id;
    }

    // Overview constructor.
    public Overview(int id, String name, String query, int refreshRate, QuerySystem querySystem) {
        this.setLayout(new BoxLayout(this, 1));

        this.query = query;
        this.id = id;
        this.refreshRate = refreshRate;
        this.querySystem = querySystem;

        nameLabel = new JLabel(name);
        table = new JTable();
        scrollPane = new JScrollPane(table);

        add(nameLabel);
        add(scrollPane);
    }

    // Thread run method. Runs the registered query every at the set refresh rate.
    @Override
    public void run() {
        while (true) {
            try {
                DefaultTableModel res = querySystem.query(query);
                if (res != null) {
                    table.setModel(res);
                }
                Thread.sleep(refreshRate * 60000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
