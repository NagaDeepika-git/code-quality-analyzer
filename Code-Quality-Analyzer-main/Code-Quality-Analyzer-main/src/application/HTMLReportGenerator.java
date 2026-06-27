package application;

import java.nio.file.*;
import java.util.Map;
import java.util.stream.Collectors;

public class HTMLReportGenerator {

    public static String generate(String fileName, CodeMetrics metrics, Map<String, Integer> scores) {
        try {
            String template = Files.readString(Path.of("resources/template.html"));

            String metricsTable = buildMetricsTable(metrics.toMap());
            String scoresHtml = buildScoresHtml(scores);

            template = template.replace("{{fileName}}", escapeHtml(fileName));
            template = template.replace("{{metricsTable}}", metricsTable);
            template = template.replace("{{scores}}", scoresHtml);

            return template;
        } catch (Exception e) {
            e.printStackTrace();
            return "<html><body><h1>Error generating report</h1><pre>" + e.getMessage() + "</pre></body></html>";
        }
    }

    private static String buildMetricsTable(Map<String, Integer> m) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table><tr><th>Metric</th><th>Value</th></tr>");
        for (Map.Entry<String, Integer> e : m.entrySet()) {
            sb.append("<tr><td>").append(escapeHtml(e.getKey())).append("</td><td>").append(e.getValue()).append("</td></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private static String buildScoresHtml(Map<String, Integer> scores) {
        return "<ul>" + scores.entrySet().stream()
                .map(e -> "<li><b>" + escapeHtml(e.getKey()) + ":</b> " + e.getValue() + "%</li>")
                .collect(Collectors.joining()) + "</ul>";
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
