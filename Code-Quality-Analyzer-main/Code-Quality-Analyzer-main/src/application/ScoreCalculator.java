package application;

import java.util.Map;
import java.util.HashMap;

public class ScoreCalculator {

    public static Map<String, Integer> calculateScores(CodeMetrics m) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("DocumentationScore", calculateDocumentationScore(m));
        scores.put("QualityScore", calculateQualityScore(m));
        scores.put("MemoryScore", calculateMemoryScore(m));
        return scores;
    }

    private static int calculateDocumentationScore(CodeMetrics m) {
        int loc = Math.max(1, m.loc);
        int pct = (int) Math.round((m.commentLines * 100.0) / loc);
        return Math.min(100, pct);
    }

    private static int calculateQualityScore(CodeMetrics m) {
        // Start at 100 and subtract penalties for complexity, nesting and style issues
        double score = 100.0;
        score -= Math.min(40, (m.cyclomaticComplexity - 1) * 1.5); // complexity penalty
        score -= Math.min(20, m.maxNestingDepth * 1.5);
        score -= Math.min(20, m.longMethodCount * 3);
        score -= Math.min(15, m.duplicateBlockCount * 2);
        // penalize very low encapsulation (more public fields)
        int nonPrivateFields = Math.max(0, m.fieldCount - m.privateFieldCount);
        if (m.fieldCount > 0) {
            double ratio = (nonPrivateFields * 1.0) / m.fieldCount;
            score -= Math.min(10, ratio * 10);
        }
        int finalScore = (int) Math.max(5, Math.round(score));
        return Math.min(100, finalScore);
    }

    private static int calculateMemoryScore(CodeMetrics m) {
        double score = 100.0;
        score -= Math.min(60, m.memoryAllocationCount * 1.5);
        score -= Math.min(30, m.possibleResourceLeaks * 10);
        score -= Math.min(10, m.stringObjectCount * 0.5);
        int finalScore = (int) Math.max(5, Math.round(score));
        return Math.min(100, finalScore);
    }
}
