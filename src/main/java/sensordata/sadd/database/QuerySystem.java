package main.java.sensordata.sadd.database;

import main.java.sensordata.sadd.pages.Overview;
import main.java.sensordata.sadd.pages.Shortcut;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.LinkedList;

public class QuerySystem {
    String properties[] = {"temperatuur", "druk", "tag", "unit"};
    String transProperty[] = {"temp", "pressure", "tag", "unit"};

    String locationIdentifiers[] = {"uit", "van"};

    String specialIdentifiers[] = {"minimum", "maximum", "gemiddelde"};
    String transIdentifiers[] = {"MIN", "MAX", "AVG"};

    public static Connection getConnection() throws Exception {
        try {
            String url = "jdbc:mysql://localhost:3306/sadd";
            String username = "otacon";
            String password = "sherman";

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    private int isProperty(String test) {
        for (int i = 0; i < properties.length; i++) {
            if (test.equalsIgnoreCase(properties[i])) {
                return i;
            }
        }
        return -1;
    }

    private boolean isLocationIdentifier(String test) {
        for (String identifer : locationIdentifiers) {
            if (test.equalsIgnoreCase(identifer)) {
                return true;
            }
        }
        return false;
    }

    private int isSpecialIdentifier(String test) {
        for (int i = 0; i < specialIdentifiers.length; i++) {
            if (test.equalsIgnoreCase(specialIdentifiers[i])) {
                return i;
            }
        }
        return -1;
    }

    private String findProperties(String[] query) {
        String result = "*";
        for (int i = 0; i < query.length; i++) {
            int propID = isProperty(query[i]);
            if (propID != -1) {
                if (i > 0) {
                    if (!isLocationIdentifier(query[i - 1])) {
                        if (result.equals("*")) result = "";
                        int special = isSpecialIdentifier(query[i-1]);
                        if (special == -1) {
                            result += transProperty[propID] + ", ";
                        } else {
                            result += transIdentifiers[special] + "(" + transProperty[propID] + "), ";
                        }
                    }
                } else {
                    if (result.equals("*")) result = "";
                    result += transProperty[propID] + ", ";
                }
            }
        }
        if (result.endsWith(", ")) {
            return result.substring(0, result.length() - 2);
        } else {
            return result;
        }
    }

    private String findLocations(String[] query) {
        String result = "";

        for (int i = 0; i < query.length; i++) {
            if (isLocationIdentifier(query[i]) && (i + 1) < query.length) {
                if (query[i + 1].equalsIgnoreCase("unit") && (i + 2) < query.length) {
                    result = "unit = " + query[i + 2];
                } else {
                    result = "tag = \"" + query[i + 1] + "\"";
                }
            }
        }
        return result;
    }

    public DefaultTableModel query(String query) throws Exception {
        Connection con = getConnection();
        PreparedStatement statement = null;

        System.out.println("Processing: " + query);

        //split query into loose parts.
        String querySplit[] = query.split(" ");

        String properties = findProperties(querySplit);
        String location = findLocations(querySplit);


        String queryString = "SELECT " + properties + " FROM sensordata" + (!location.equals("") ? " WHERE " + location : "");

        statement = con.prepareStatement(queryString);
        System.out.println(queryString);

        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData rsmd = resultSet.getMetaData();

        int columns = rsmd.getColumnCount();

        String rowValues[] = new String[columns];

        DefaultTableModel model = new DefaultTableModel();

        if (columns > 0) {
            for (int i = 1; i <= columns; i++) {
                model.addColumn(rsmd.getColumnName(i));
            }

            while (resultSet.next()) {

                for (int i = 1; i <= columns; i++) {
                    rowValues[i - 1] = resultSet.getString(i);
                }
                model.addRow(rowValues);
            }
        }
        return model;
    }

    public Overview[] getOverviews(String email) throws Exception {
        PreparedStatement statement = getConnection().prepareStatement("SELECT overview_id, name, query, refresh_rate  FROM overviews WHERE user_mail = \"" + email + "\"");
        ResultSet resultSet = statement.executeQuery();

        LinkedList<Overview> overviews = new LinkedList<>();

        while (resultSet.next()) {
            overviews.add(new Overview(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), this));
        }
        return overviews.toArray(new Overview[0]);
    }

    public Shortcut[] getShortcuts(String email) throws Exception {
        PreparedStatement statement = getConnection().prepareStatement("SELECT name, shortcut_id, query  FROM shortcuts WHERE user_mail = \"" + email + "\"");
        ResultSet resultSet = statement.executeQuery();

        LinkedList<Shortcut> shortcuts = new LinkedList<>();

        while (resultSet.next()) {
            shortcuts.add(new Shortcut(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3)));
        }
        return shortcuts.toArray(new Shortcut[0]);
    }

    public boolean addOverview(String email, String query, String name, int refreshRate) {
        try {
            PreparedStatement update = getConnection().prepareStatement("INSERT INTO overviews (user_mail, query, name, refresh_rate) VALUES (\"" + email + "\", \"" + query + "\", \"" + name + "\", " + refreshRate + ")");
            System.out.println(update.executeUpdate());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOverview(String email, int id) {
        try {
            PreparedStatement delete = getConnection().prepareStatement("DELETE FROM overviews WHERE overview_id = " + id);
            delete.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addShortcut(String email, String query, String name) {
        try {
            PreparedStatement update = getConnection().prepareStatement("INSERT INTO shortcuts (user_mail, query, name) VALUES (\"" + email + "\", \"" + query + "\", \"" + name + "\")");
            System.out.println(update.executeUpdate());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteShortcut(String email, int id) {
        try {
            PreparedStatement delete = getConnection().prepareStatement("DELETE FROM shortcuts WHERE shortcut_id = " + id);
            delete.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}