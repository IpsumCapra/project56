package main.java.sensordata.sadd.database;

import java.sql.*;

public class QueryDemo {
    String properties[] = {"temperatuur", "druk", "tag", "unit"};
    String transProperty[] = {"temp", "pressure", "tag", "unit"};

    String locationIdentifiers[] = {"uit", "van"};

    String conditionIdentifiers[] = {"of", "en"};

    public static Connection getConnection() throws Exception {
        try {
            String url = "jdbc:mysql://localhost:3306/sadd";
            String username = "login";
            String password = "pass";

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

    private String findProperties(String[] query) {
        String result = "*";
        for (int i = 0; i < query.length; i++) {
            int propID = isProperty(query[i]);
            if (propID != -1) {
                if (i > 0) {
                    if (!isLocationIdentifier(query[i-1])) {
                        if (result.equals("*")) result = "";
                        result += transProperty[propID] + ", ";
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

    public String query(String query) throws Exception {
        String result = "";

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

        if (columns > 0) {
            for (int i = 1; i <= columns; i++ ) {
                result += rsmd.getColumnName(i) + "\t";
            }
            result += "\n";
            while (resultSet.next()) {
                for (int i = 1; i <= columns; i++ ) {
                    result += resultSet.getString(i) + "\t";
                }
                result += "\n";
            }
        }
        return result;
    }
}