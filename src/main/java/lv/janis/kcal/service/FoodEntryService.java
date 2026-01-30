package lv.janis.kcal.service;

import java.time.LocalDate;

import lv.janis.kcal.db.FoodEntryRepository;

public class FoodEntryService {

  private final FoodEntryRepository repo;

  public FoodEntryService(FoodEntryRepository repo) {
    this.repo = repo;
  }

  public int addFoodEntry(LocalDate date, int kcal, String label) {
    if (date == null) {
      throw new IllegalArgumentException("date is required");
    }
    if (kcal <= 0) {
      throw new IllegalArgumentException("kcal must be > 0");
    }
    try {
      return repo.addFoodEntry(date, kcal, label);
    } catch (Exception e) {
      throw new RuntimeException("Failed to add food entry", e);
    }
  }

  public int updateFoodEntry(int id, int kcal, String label) {
    if (id <= 0) {
      throw new IllegalArgumentException("id must be > 0");
    }
    if (kcal <= 0) {
      throw new IllegalArgumentException("kcal must be > 0");
    }
    try {
      return repo.updateFoodEntry(id, kcal, label);
    } catch (Exception e) {
      throw new RuntimeException("Failed to update food entry", e);
    }
  }

  public int deleteFoodEntry(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("id must be > 0");
    }
    try {
      return repo.deleteFoodEntry(id);
    } catch (Exception e) {
      throw new RuntimeException("Failed to delete food entry", e);
    }
  }
}
