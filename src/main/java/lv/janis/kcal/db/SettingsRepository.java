package lv.janis.kcal.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SettingsRepository {

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

  public int getBaselineKcalOrDefault(int defaultValue) throws Exception {
    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "SELECT value FROM settings WHERE key = 'baseline_kcal'")) {

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return Integer.parseInt(rs.getString("value"));
      }
    }
    setBaselineKcal(defaultValue);
    return defaultValue;
  }

  public void setBaselineKcal(int kcal) throws Exception {
    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "INSERT INTO settings(key, value) VALUES ('baseline_kcal', ?) " +
                "ON CONFLICT(key) DO UPDATE SET value = excluded.value")) {

      ps.setString(1, Integer.toString(kcal));
      ps.executeUpdate();
    }
  }
}
