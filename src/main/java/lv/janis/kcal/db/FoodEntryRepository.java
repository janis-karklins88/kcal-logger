package lv.janis.kcal.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class FoodEntryRepository {

  public int addFoodEntry(LocalDate date, int kcal, String label) throws Exception {
    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "INSERT INTO food_entry(date, kcal, label) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, date.toString());
      ps.setInt(2, kcal);
      ps.setString(3, label);
      ps.executeUpdate();

      ResultSet keys = ps.getGeneratedKeys();
      if (keys.next()) {
        return keys.getInt(1);
      }
      throw new IllegalStateException("Failed to read generated id for food_entry");
    }
  }

  public int updateFoodEntry(int id, int kcal, String label) throws Exception {
    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "UPDATE food_entry SET kcal = ?, label = ? WHERE id = ?")) {

      ps.setInt(1, kcal);
      ps.setString(2, label);
      ps.setInt(3, id);
      return ps.executeUpdate();
    }
  }

  public int deleteFoodEntry(int id) throws Exception {
    try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(
            "DELETE FROM food_entry WHERE id = ?")) {

      ps.setInt(1, id);
      return ps.executeUpdate();
    }
  }
}
