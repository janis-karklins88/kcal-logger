package lv.janis.kcal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lv.janis.kcal.db.TodayRepository;

import java.time.LocalDate;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        TodayRepository repo = new TodayRepository();
        LocalDate today = LocalDate.now();

        int baseline = repo.getBaselineKcal();
        int active = repo.getActiveKcal(today);
        int intake = repo.getIntakeKcal(today);

        int burnBudget = baseline + active;
        int remaining = burnBudget - intake;

        VBox root = new VBox(8,
                new Label("Baseline: " + baseline),
                new Label("Active: " + active),
                new Label("Intake: " + intake),
                new Label("Burn budget: " + burnBudget),
                new Label("Remaining: " + remaining));

        Scene scene = new Scene(root, 350, 220);
        stage.setTitle("Kcal Logger");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
