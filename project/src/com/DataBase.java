package com;

import java.sql.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class DataBase {
    private static String path;

    public DataBase(String fileName) {
        this.path = "jdbc:sqlite:" + fileName;
    }

    public static void createNewDatabase() {
        try {
            var connection = DriverManager.getConnection(path);
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS building (\n"
                + "	number text PRIMARY KEY,\n"
                + "	address text,\n"
                + "	snapshot text,\n"
                + "	appellation text,\n"
                + "	number_of_floor text,\n"
                + "	prefix_code text,\n"
                + " building_type text,\n"
                + " id_ text,\n"
                + " year_construction text\n"
                + ");";

        try (var connection = DriverManager.getConnection(path);Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addBuildings(ArrayList<Building> buildings){
        for(var building: buildings)
            addBuilding(building);
    }

    private static void addBuilding(Building building){
        try (var connection = DriverManager.getConnection(path);PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO building " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            var values = building.getAllValues();
            for(var i = 0; i < values.size(); i++)
                statement.setObject(i + 1, values.get(i));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TreeMap<String, String> getStagesData(){
        try (var connection = DriverManager.getConnection(path);) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT number_of_floor, count(*) from building " +
                    "WHERE number_of_floor not in (\"Малоэтажные\", \"Многоэтажные\") GROUP BY number_of_floor");
            var values = new TreeMap<String, String>();
            while(resultSet.next()){
                if(resultSet.getString(1).length() == 0)
                    continue;
                values.put(resultSet.getString(1), resultSet.getString(2));
            }
            return values;
        } catch (SQLException e) {
            e.printStackTrace();
            return new TreeMap<String, String>();
        }
    }

    public ArrayList<ArrayList<String>> getAllDataFromRow(String stat){
        try (var connection = DriverManager.getConnection(path);) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(stat);
            var values = new ArrayList<ArrayList<String>>();
            while(resultSet.next()){
                values.add(getArrayList(resultSet));
            }
            return values;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<ArrayList<String>>();
        }
    }

    private ArrayList<String> getArrayList(ResultSet result) throws SQLException {
        var list = new ArrayList<String>();
        for(var i = 1; i < result.getMetaData().getColumnCount() + 1; i++)
            list.add(result.getString(i));
        return list;
    }

}
