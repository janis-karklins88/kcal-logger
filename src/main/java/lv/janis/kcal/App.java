package lv.janis.kcal;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lv.janis.kcal.db.Db;
import lv.janis.kcal.db.SettingsRepository;
import lv.janis.kcal.db.TodayRepository;
import lv.janis.kcal.service.SyncService;

import java.time.LocalDate;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        TodayRepository repo = new TodayRepository();
        LocalDate today = LocalDate.now();

        SettingsRepository settings = new SettingsRepository();
        int baseline = settings.getBaselineKcalOrDefault(2000);
        int active = repo.getActiveKcal(today);
        int intake = repo.getIntakeKcal(today);

        int burnBudget = baseline + active;
        int remaining = burnBudget - intake;

        SyncService syncService = new SyncService(
                SyncService.defaultScriptPath(),
                Db.getDbPath());
        Label syncStatus = new Label("Sync status: idle");
        Button syncButton = new Button("Sync Garmin");
        syncButton.setOnAction(event -> {
            syncStatus.setText("Sync status: running...");
            Thread worker = new Thread(() -> {
                try {
                    syncService.syncTodayAndYesterday();
                    Platform.runLater(() -> syncStatus.setText("Sync status: OK"));
                } catch (RuntimeException ex) {
                    Platform.runLater(() -> syncStatus.setText("Sync failed: " + ex.getMessage()));
                }
            });
            worker.setDaemon(true);
            worker.start();
        });

        VBox root = new VBox(8,
                new Label("Baseline: " + baseline),
                new Label("Active: " + active),
                new Label("Intake: " + intake),
                new Label("Burn budget: " + burnBudget),
                new Label("Remaining: " + remaining),
                syncButton,
                syncStatus);

        Scene scene = new Scene(root, 350, 220);
        stage.setTitle("Kcal Logger");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
