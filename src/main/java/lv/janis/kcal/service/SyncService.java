package lv.janis.kcal.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SyncService {

  private static final String PYTHON_CMD = "python";
  private static final int DAYS_BACK = 1;

  private final String scriptPath;
  private final String dbPath;

  public SyncService(String scriptPath, String dbPath) {
    this.scriptPath = scriptPath;
    this.dbPath = dbPath;
  }

  public String syncTodayAndYesterday() {
    List<String> command = new ArrayList<>();
    command.add(PYTHON_CMD);
    command.add(scriptPath);
    command.add("--db");
    command.add(dbPath);
    command.add("--days-back");
    command.add(Integer.toString(DAYS_BACK));

    ProcessBuilder pb = new ProcessBuilder(command);
    pb.redirectErrorStream(true);

    try {
      Process process = pb.start();
      String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
      int exitCode = process.waitFor();
      if (exitCode != 0) {
        throw new RuntimeException("Sync failed (exit " + exitCode + "): " + output.trim());
      }
      return output.trim();
    } catch (IOException | InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Sync failed: " + e.getMessage(), e);
    }
  }

  public static String defaultScriptPath() {
    Path cwd = Path.of(System.getProperty("user.dir"));
    Path script = cwd.getParent().getParent().resolve("garmin_active_to_sqlite.py");
    return script.toAbsolutePath().toString();
  }
}
