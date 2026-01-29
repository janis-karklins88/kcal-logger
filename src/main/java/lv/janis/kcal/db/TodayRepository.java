package lv.janis.kcal.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class TodayRepository {

  public int getBaselineKcal() throws Exception {
    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "SELECT value FROM settings WHERE key = 'baseline_kcal'")) {

      ResultSet rs = ps.executeQuery();
      if (!rs.next()) {
        throw new IllegalStateException("baseline_kcal not found");
      }
      return Integer.parseInt(rs.getString("value"));
    }
  }

  public int getActiveKcal(LocalDate date) throws Exception {
    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "SELECT active_kcal FROM daily_burn WHERE date = ?")) {

      ps.setString(1, date.toString());
      ResultSet rs = ps.executeQuery();
      return rs.next() ? rs.getInt("active_kcal") : 0;
    }
  }

  public int getIntakeKcal(LocalDate date) throws Exception {
    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "SELECT COALESCE(SUM(kcal), 0) AS total FROM food_entry WHERE date = ?")) {

      ps.setString(1, date.toString());
      ResultSet rs = ps.executeQuery();
      rs.next();
      return rs.getInt("total");
    }
  }
}
