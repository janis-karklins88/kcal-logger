package lv.janis.kcal.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lv.janis.kcal.db.WeeklyRepository;
import lv.janis.kcal.db.WeeklyRepository.Expendature;
import lv.janis.kcal.db.WeeklyRepository.Intake;

public class WeeklyService {

  private final WeeklyRepository repo;

  public WeeklyService(WeeklyRepository repo) {
    this.repo = repo;
  }

  // get intake per date for a range (inclusive)
  public Map<String, Integer> getIntakeByDateRange(LocalDate start, LocalDate end) {

    List<Intake> intake;
    try {
      intake = repo.getTotalIntakeForDates(start, end);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load weekly intake", e);
    }
    Map<String, Integer> intakeByDay = intake.stream()
        .collect(Collectors.toMap(Intake::date, Intake::kcal));
    return intakeByDay;
  }

  // get burned kcal per date for a range (inclusive)
  public Map<String, Integer> getBurnedByDateRange(LocalDate start, LocalDate end) {

    List<Expendature> burned;
    try {
      burned = repo.getBurnedKcalForDates(start, end);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load weekly expendature", e);
    }
    Map<String, Integer> burnedByDay = burned.stream()
        .collect(Collectors.toMap(Expendature::date, Expendature::kcal));
    return burnedByDay;
  }


  public int getCurrentWeekNetBurned() {
    // set dates, start = Monday, end = yesterday, dates are inclusive
    LocalDate today = LocalDate.now();
    LocalDate start = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate end = today.minusDays(1);
    if (end.isBefore(start)) {
      return 0;
    }

    Map<String, Integer> intake = getIntakeByDateRange(start, end);
    Map<String, Integer> burned = getBurnedByDateRange(start, end);

    int sum = 0;

    for (Map.Entry<String, Integer> entry : intake.entrySet()) {
      String date = entry.getKey();
      Integer kcal = entry.getValue();
      if (kcal > 0) {
        int burnedKcal = burned.getOrDefault(date, 0);
        sum = sum + kcal - burnedKcal;
      }
    }
    return sum;
  }

  public int getLastWeekNetBurned() {
    // set dates for last full week (Mon-Sun), dates are inclusive
    LocalDate today = LocalDate.now();
    LocalDate start = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .minusWeeks(1);
    LocalDate end = start.plusDays(6);

    Map<String, Integer> intake = getIntakeByDateRange(start, end);
    Map<String, Integer> burned = getBurnedByDateRange(start, end);

    int sum = 0;

    for (Map.Entry<String, Integer> entry : intake.entrySet()) {
      String date = entry.getKey();
      Integer kcal = entry.getValue();
      if (kcal > 0) {
        int burnedKcal = burned.getOrDefault(date, 0);
        sum = sum + kcal - burnedKcal;
      }
    }
    return sum;
  }

}
