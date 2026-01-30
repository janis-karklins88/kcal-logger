package lv.janis.kcal.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeeklyRepository {

  public static record Intake(String date, int kcal) {
  };

  public static record Expendature(String date, int kcal) {
  };

  public List<Intake> getTotalIntakeForDates(LocalDate start, LocalDate end) throws Exception {
    List<Intake> entries = new ArrayList<>();

    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "SELECT date, SUM(kcal) AS intake " +
                "FROM food_entry " +
                "WHERE date >= ? AND date <= ? " +
                "GROUP BY date")) {

      ps.setString(1, start.toString());
      ps.setString(2, end.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        String label = rs.getString("date");
        int kcal = rs.getInt("intake");

        entries.add(new Intake(label, kcal));
      }
    }
    return entries;
  }

  public List<Expendature> getBurnedKcalForDates(LocalDate start, LocalDate end) throws Exception {
    List<Expendature> entries = new ArrayList<>();

    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "SELECT date, active_kcal " +
                "FROM daily_burn " +
                "WHERE date >= ? AND date <= ?")) {

      ps.setString(1, start.toString());
      ps.setString(2, end.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        String label = rs.getString("date");
        int kcal = rs.getInt("active_kcal");

        entries.add(new Expendature(label, kcal));
      }
    }
    return entries;
  }

}
