package main.java.sensordata.sadd.pages;

import javax.swing.*;
import java.awt.*;

public class Shortcut extends JButton {
    private String query;
    private int id;

    public Shortcut(String name, int id, String query) {
        super(name);
        this.query = query;
        this.id = id;
        setActionCommand("shortcut");
    }

    public String getQuery() {
        return query;
    }

    public int getID() {
        return id;
    }
}
