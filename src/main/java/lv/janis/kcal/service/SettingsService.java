package lv.janis.kcal.service;

import lv.janis.kcal.db.SettingsRepository;

public class SettingsService {

  private final SettingsRepository repo;

  public SettingsService(SettingsRepository repo) {
    this.repo = repo;
  }

  public int getBaselineKcal() {
    try {
      return repo.getBaselineKcal();
    } catch (Exception e) {
      throw new RuntimeException("Failed to load baseline kcal", e);
    }
  }

  public int getBaselineKcalOrDefault(int defaultValue) {
    if (defaultValue <= 0) {
      throw new IllegalArgumentException("defaultValue must be > 0");
    }
    try {
      return repo.getBaselineKcalOrDefault(defaultValue);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load baseline kcal", e);
    }
  }

  public void setBaselineKcal(int kcal) {
    if (kcal <= 0) {
      throw new IllegalArgumentException("kcal must be > 0");
    }
    try {
      repo.setBaselineKcal(kcal);
    } catch (Exception e) {
      throw new RuntimeException("Failed to save baseline kcal", e);
    }
  }
}
