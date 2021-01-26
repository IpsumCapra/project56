package main.java.sensordata.sadd.database;

import main.java.sensordata.sadd.pages.Overview;
import main.java.sensordata.sadd.pages.Shortcut;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.LinkedList;

public class QuerySystem {
    // Keywords for the language processor.
    String properties[] = {"Attribuut", "Waarde", "Tag", "Datum"};
    String transProperty[] = {"attribute", "value", "tag", "added"};

    String locationIdentifiers[] = {"uit", "van"};

    String specialIdentifiers[] = {"minimum", "maximum", "gemiddelde"};
    String transIdentifiers[] = {"MIN", "MAX", "AVG"};

    String attrIdentifiers[] = {"KVS", "EHM", "FC", "Sensorwaarde", "URV", "LRV"};

    // Build a connection with the SADD db.
    public static Connection getConnection() throws Exception {
        try {
            String url = "jdbc:mysql://localhost:3306/sadd";
            String username = "root";
            String password = "root";

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    // Check whether the word is a property, if yes, return it's translated index.
    private int isProperty(String test) {
        for (int i = 0; i < properties.length; i++) {
            if (test.equalsIgnoreCase(properties[i])) {
                return i;
            }
        }
        return -1;
    }

    // Check if a word is a location identifier.
    private boolean isLocationIdentifier(String test) {
        for (String identifer : locationIdentifiers) {
            if (test.equalsIgnoreCase(identifer)) {
                return true;
            }
        }
        return false;
    }

    // Check if a word is an attribute.
    private boolean isAttribute(String test) {
        for (String attribute : attrIdentifiers) {
            if (test.equalsIgnoreCase(attribute)) {
                return true;
            }
        }
        return false;
    }

    // Check if a word is a special identifier.
    private int isSpecialIdentifier(String test) {
        for (int i = 0; i < specialIdentifiers.length; i++) {
            if (test.equalsIgnoreCase(specialIdentifiers[i])) {
                return i;
            }
        }
        return -1;
    }

    // Find all properties in the query, and return the property part of the converted SQL query.
    private String findProperties(String[] query) {
        String result = "*";
        for (int i = 0; i < query.length; i++) {
            int propID = isProperty(query[i]);
            if (propID != -1) {
                if (i > 0) {
                    if (!isLocationIdentifier(query[i - 1])) {
                        if (result.equals("*")) result = "";
                        int special = isSpecialIdentifier(query[i - 1]);
                        if (special == -1) {
                            result += transProperty[propID] + ", ";
                        } else {
                            result += transIdentifiers[special] + "(CAST(" + transProperty[propID] + " AS float)), ";
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

    // Find the location from which to get info (if any)
    private String findLocations(String[] query) {
        String result = "";

        for (int i = 0; i < query.length; i++) {
            if (isLocationIdentifier(query[i]) && (i + 1) < query.length) {
                result = "tag = \"" + query[i + 1] + "\"";
            }
        }
        return result;
    }

    // Find all attributes, alter the query accordingly.
    private String[] findAttributes(String properties, String location, String[] query) {
        String tables = "sensordata";
        String tag = "";
        boolean attributeQuery = false;

        for (String word : query) {
            if (isAttribute(word)) {
                tables = "";
                properties = "";
                tag = location;
                location = "";
                attributeQuery = true;
                break;
            }
        }



        if (attributeQuery) {
            for (int i = 0; i < query.length; i++) {
                if (isAttribute(query[i])) {
                    if (properties.equals("*")) properties = "";
                    int special = i > 0 ? isSpecialIdentifier(query[i - 1]) : -1;
                    if (special == -1) {
                        properties += "s" + i + ".value AS \"" + query[i].toUpperCase() + "\", ";
                    } else {
                        properties += transIdentifiers[special] + "(CAST(" + "s" + i + ".value AS float)) AS \"" + query[i].toUpperCase() + "\", ";
                    }
                    if (!tables.equalsIgnoreCase("")) tables += ", ";
                    tables += "sensordata s" + i;

                    if (!location.equalsIgnoreCase("")) location += " AND ";

                    if (query[i].equalsIgnoreCase("Sensorwaarde")) {
                        properties += "a" + i + ".value AS \"EENHEID\", ";
                        tables += ", sensordata a" + i;
                        location += "s" + i + ".attribute = \"value\" AND s" + i + "." + tag + " AND a" + i + ".attribute = \"unit\" AND a" + i + "." + tag;
                    } else {
                        location += "s" + i + ".attribute = \"" + query[i] + "\" AND s" + i + "." + tag;
                    }
                }
            }
            if (properties.endsWith(", ")) {
                properties = properties.substring(0, properties.length() - 2);
            }
        }
        return new String[]{properties, location, tables};
    }

    // If there is a more user friendly definition for a column name, return it
    private String translateName(String name) {
        for (int i = 0; i < transProperty.length; i++) {
            if (name.equalsIgnoreCase(transProperty[i])) {
                return properties[i];
            }
        }
        return name;
    }

    // Build a table from a string query using the language processor.
    public DefaultTableModel query(String query) throws Exception {
        // Connect to the db
        Connection con = getConnection();
        PreparedStatement statement = null;

        System.out.println("Processing: " + query);

        // Split query into loose parts.
        String querySplit[] = query.split(" ");

        // Find relevant keywords, process them.
        String properties = findProperties(querySplit);
        String location = findLocations(querySplit);

        // Process query. if an attribute is found, the query is turned into an attribute query, other questions will be lost.
        String[] attributes = findAttributes(properties, location, querySplit);

        // Build the SQL string.
        String queryString = "SELECT " + attributes[0] + " FROM " + attributes[2] + (!attributes[1].equals("") ? " WHERE " + attributes[1] : "");

        // Prepare and execute the SQL statement.
        System.out.println(queryString);
        statement = con.prepareStatement(queryString);

        // Process the data into a DefaultTableModel for further processing in the UI.
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData rsmd = resultSet.getMetaData();

        int columns = rsmd.getColumnCount();

        String rowValues[] = new String[columns];

        DefaultTableModel model = new DefaultTableModel();

        // Place all values into the model accordingly.
        if (columns > 0) {
            for (int i = 1; i <= columns; i++) {
                // Translate column names.
                model.addColumn(attributes[2].equalsIgnoreCase("sensordata") ? translateName(rsmd.getColumnName(i)) : rsmd.getColumnLabel(i));
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

    // Fancy getters and setters, which use the database to function.
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

    // Functions to remove and add the UI shortcuts which are stored inside the database.
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