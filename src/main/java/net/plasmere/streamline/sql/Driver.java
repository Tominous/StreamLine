package net.plasmere.streamline.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Driver {

    public static Connection connect(String fileName) {
        String url = "jdbc:sqlite:" + fileName + ".db";
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement level = conn.createStatement();
            level.executeUpdate("CREATE TABLE IF NOT EXISTS Level(UUID,XP INT DEFAULT 0, LVL INT DEFAULT 0)");
            level.close();
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean updateRow(String fileName, String query, List<Object> sql2) {
        try {
            Connection conn = connect(fileName);
            assert conn != null;
            PreparedStatement sql = conn.prepareStatement(query);
            int i = 1;
            for (Object result: sql2) {
                sql.setObject(i, result);
                i++;
            }
            sql.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean execute(String fileName, String query, List < String > values) {
        try {
            Connection conn = connect(fileName);
            assert conn != null;
            PreparedStatement sql = conn.prepareStatement(query);
            for (int i = 0; i < values.size(); i++) {
                sql.setString(1, values.get(i));
            }
            sql.executeBatch();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet queryFetch(String fileName, String query, List < String > values) {
        try {
            Connection conn = connect(fileName);
            assert conn != null;
            PreparedStatement sql = conn.prepareStatement(query);
            int i = 1;
            for (String result: values) {
                sql.setString(i, result);
                i++;
            }
            return sql.executeQuery();
        } catch (SQLException e) {
            return null;
        }
    }

}