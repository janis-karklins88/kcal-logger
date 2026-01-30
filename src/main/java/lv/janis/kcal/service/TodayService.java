package lv.janis.kcal.service;

import java.time.LocalDate;
import java.util.List;

import lv.janis.kcal.db.TodayRepository;
import lv.janis.kcal.db.TodayRepository.FoodEntry;

public class TodayService {

  private final TodayRepository repo;

  public TodayService(TodayRepository repo) {
    this.repo = repo;
  }

  public int getActiveKcal(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("date is required");
    }
    try {
      return repo.getActiveKcal(date);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load active kcal", e);
    }
  }

  public int getIntakeKcal(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("date is required");
    }
    try {
      return repo.getIntakeKcal(date);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load intake kcal", e);
    }
  }

  public List<FoodEntry> getFoodEntries(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("date is required");
    }
    try {
      return repo.getTodayFoodEntries(date);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load food entries", e);
    }
  }
}
