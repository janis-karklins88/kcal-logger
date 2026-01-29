package lv.janis.kcal.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {

    private static final String DB_URL =
        "jdbc:sqlite:C:/Users/janis/AppData/Local/KcalLogger/kcal.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
