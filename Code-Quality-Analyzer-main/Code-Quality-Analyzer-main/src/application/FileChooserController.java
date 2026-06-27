package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class FileChooserController {

    @FXML private Button uploadButton;
    @FXML private WebView reportView;
    @FXML private Label statusLabel;

    @FXML
    public void onUploadClick() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Java File");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));
            File file = chooser.showOpenDialog(new Stage());
            if (file == null) {
                statusLabel.setText("No file selected.");
                return;
            }

            statusLabel.setText("Analyzing: " + file.getName());
            String code = Files.readString(file.toPath());

            // Analyze metrics
            CodeMetrics metrics = MetricsAnalyzer.analyze(code);

            // Calculate scores
            Map<String, Integer> scores = ScoreCalculator.calculateScores(metrics);

            // Generate HTML
            String html = HTMLReportGenerator.generate(file.getName(), metrics, scores);

            // Display
            reportView.getEngine().loadContent(html);
            statusLabel.setText("Analysis complete: " + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}
