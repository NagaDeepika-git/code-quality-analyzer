package application;

import java.util.*;
import java.util.regex.*;

/**
 * MetricsAnalyzer: compute the 29 metrics from source code string.
 *
 * Heuristics are used for many metrics; this is normal for static analysis.
 */
public class MetricsAnalyzer {

    // Patterns
    private static final Pattern METHOD_DECL_PATTERN = Pattern.compile(
            "(public|private|protected|static|\\s)+[\\w<>,\\[\\]]+\\s+([\\w]+)\\s*\\([^)]*\\)\\s*\\{",
            Pattern.MULTILINE);
    private static final Pattern CLASS_DECL_PATTERN = Pattern.compile("\\bclass\\s+\\w+");
    private static final Pattern INTERFACE_DECL_PATTERN = Pattern.compile("\\binterface\\s+\\w+");
    private static final Pattern FIELD_PATTERN = Pattern.compile("(^|\\s)(private|public|protected)?\\s*(static\\s+)?[\\w<>,\\[\\]]+\\s+\\w+\\s*(=|;)",
            Pattern.MULTILINE);
    private static final Pattern IMPORT_PATTERN = Pattern.compile("^import\\s+([\\w\\.]+)", Pattern.MULTILINE);

    public static CodeMetrics analyze(String code) {
        CodeMetrics cm = new CodeMetrics();

        // Normalize line endings
        String normalized = code.replace("\r\n", "\n").replace('\r', '\n');
        String[] lines = normalized.split("\n", -1);

        cm.loc = lines.length;

        // Basic counts
        cm.emptyLines = countEmptyLines(lines);
        cm.commentLines = countCommentLines(lines);
        cm.commentToCodeRatio = (int) Math.round((cm.commentLines * 100.0) / Math.max(1, cm.loc));

        // Classes/interfaces/methods
        cm.classCount = countPattern(CLASS_DECL_PATTERN, code);
        cm.interfaceCount = countPattern(INTERFACE_DECL_PATTERN, code);
        cm.methodCount = countMethodDeclarations(code);
        cm.avgMethodLength = calculateAverageMethodLength(code);

        // Complexity
        cm.cyclomaticComplexity = estimateCyclomaticComplexity(code);
        cm.maxNestingDepth = calculateNestingDepth(code);
        cm.decisionStatements = countDecisionStatements(code);
        cm.loopCount = countLoopStatements(code);
        cm.returnCount = countRegex("\\breturn\\b", code);
        cm.recursionCount = detectRecursion(code);
        cm.exceptionHandlingCount = countRegex("\\btry\\b|\\bcatch\\b|\\bfinally\\b", code);

        // OOP metrics
        cm.fieldCount = countFieldDeclarations(code);
        cm.staticFieldCount = countRegex("\\bstatic\\s+[^;\\(]+;", code);
        cm.staticMethodCount = countRegex("\\bstatic\\s+[^\\(]+\\(", code);
        cm.inheritanceCount = countRegex("\\bextends\\b|\\bimplements\\b", code);
        cm.privateFieldCount = countRegex("\\bprivate\\s+[^;\\(]+;", code);
        cm.importCount = countUniqueImports(code);

        // Memory & resources
        cm.memoryAllocationCount = countRegex("\\bnew\\s+[A-Za-z0-9_<>]+", code) + countRegex("ArrayList\\b|HashMap\\b|HashSet\\b|LinkedList\\b", code);
        cm.stringObjectCount = countRegex("\\bnew\\s+String\\b", code);
        cm.fileIoCount = countRegex("\\bFile\\b|FileInputStream\\b|FileOutputStream\\b|BufferedReader\\b|BufferedWriter\\b|FileReader\\b|FileWriter\\b", code);
        cm.threadCount = countRegex("\\bThread\\b|\\bRunnable\\b|ExecutorService\\b|Executor\\b", code);
        cm.possibleResourceLeaks = estimateResourceLeaks(code);

        // Style & quality
        cm.longMethodCount = countLongMethods(code, 40); // >40 lines
        cm.longClassFlag = (detectLongClass(code, 500) ? 1 : 0);
        cm.duplicateBlockCount = detectDuplicateBlocks(normalized, 5); // repeated 5-line blocks

        return cm;
    }

    // ---------- helpers ----------

    private static int countEmptyLines(String[] lines) {
        int c = 0;
        for (String l : lines) if (l.trim().isEmpty()) c++;
        return c;
    }

    private static int countCommentLines(String[] lines) {
        int c = 0;
        boolean inBlock = false;
        for (String l : lines) {
            String t = l.trim();
            if (t.startsWith("/*")) inBlock = true;
            if (inBlock) c++;
            else if (t.startsWith("//")) c++;
            if (t.endsWith("*/")) inBlock = false;
        }
        return c;
    }

    private static int countPattern(Pattern p, String s) {
        Matcher m = p.matcher(s);
        int c = 0;
        while (m.find()) c++;
        return c;
    }

    private static int countMethodDeclarations(String code) {
        Matcher m = METHOD_DECL_PATTERN.matcher(code);
        int c = 0;
        while (m.find()) c++;
        return c;
    }

    private static int calculateAverageMethodLength(String code) {
        // Find method bodies by scanning braces after a method declaration
        Matcher m = METHOD_DECL_PATTERN.matcher(code);
        int totalLines = 0;
        int count = 0;
        while (m.find()) {
            int start = m.end() - 1; // position at '{'
            int end = findMatchingBrace(code, start);
            if (end > start) {
                String body = code.substring(start + 1, end);
                int lines = body.split("\n", -1).length;
                totalLines += lines;
                count++;
            }
        }
        return count == 0 ? 0 : totalLines / count;
    }

    private static int findMatchingBrace(String s, int openIndex) {
        int depth = 0;
        for (int i = openIndex; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    private static int estimateCyclomaticComplexity(String code) {
        int count = 1; // baseline
        count += countRegex("\\bif\\b", code);
        count += countRegex("\\bfor\\b", code);
        count += countRegex("\\bwhile\\b", code);
        count += countRegex("\\bcase\\b", code);
        count += countRegex("\\?\\s*", code); // ternary
        count += countRegex("&&|\\|\\|", code);
        return count;
    }

    private static int calculateNestingDepth(String code) {
        int depth = 0, max = 0;
        for (char c : code.toCharArray()) {
            if (c == '{') depth++;
            else if (c == '}') depth--;
            max = Math.max(max, depth);
        }
        return max;
    }

    private static int countDecisionStatements(String code) {
        return countRegex("\\bif\\b|\\belse if\\b|\\bswitch\\b|\\bcase\\b", code);
    }

    private static int countLoopStatements(String code) {
        return countRegex("\\bfor\\b|\\bwhile\\b|\\bdo\\b", code);
    }

    private static int countRegex(String regex, String code) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(code);
        int c = 0;
        while (m.find()) c++;
        return c;
    }

    private static int detectRecursion(String code) {
        // collect method names from declarations, then check if method body calls itself
        Set<String> methodNames = new HashSet<>();
        Matcher m = METHOD_DECL_PATTERN.matcher(code);
        while (m.find()) {
            String name = m.group(2);
            methodNames.add(name);
        }
        int rec = 0;
        for (String name : methodNames) {
            // naive: check " name(" occurrences inside a method body belonging to same name
            // count occurrences of "name(" in whole code and subtract declaration; if >1 assume recursion
            int totalCalls = countRegex("\\b" + Pattern.quote(name) + "\\s*\\(", code);
            if (totalCalls > 1) rec++;
        }
        return rec;
    }

    private static int countFieldDeclarations(String code) {
        Matcher m = FIELD_PATTERN.matcher(code);
        int c = 0;
        while (m.find()) c++;
        return c;
    }

    private static int countUniqueImports(String code) {
        Matcher m = IMPORT_PATTERN.matcher(code);
        Set<String> imports = new HashSet<>();
        while (m.find()) {
            String full = m.group(1);
            // take package level (e.g., java.util)
            String pkg = full.contains(".") ? full.substring(0, full.lastIndexOf('.')) : full;
            imports.add(pkg);
        }
        return imports.size();
    }

    private static int estimateResourceLeaks(String code) {
        int opens = countRegex("new\\s+FileInputStream|new\\s+FileOutputStream|new\\s+BufferedReader|new\\s+FileReader|openStream\\(", code);
        int closes = countRegex("\\.close\\s*\\(", code);
        return Math.max(0, opens - closes);
    }

    private static boolean detectLongClass(String code, int threshold) {
        // approximate class sizes by splitting by class declarations
        Matcher m = CLASS_DECL_PATTERN.matcher(code);
        int idx = 0;
        while (m.find()) {
            int start = m.start();
            idx++;
        }
        // fallback: if total LOC > threshold, flag
        int totalLoc = code.split("\n", -1).length;
        return totalLoc > threshold;
    }

    private static int countLongMethods(String code, int thresholdLines) {
        Matcher m = METHOD_DECL_PATTERN.matcher(code);
        int count = 0;
        while (m.find()) {
            int start = m.end() - 1;
            int end = findMatchingBrace(code, start);
            if (end > start) {
                String body = code.substring(start + 1, end);
                int lines = body.split("\n", -1).length;
                if (lines > thresholdLines) count++;
            }
        }
        return count;
    }

    private static int detectDuplicateBlocks(String normalizedCode, int blockSizeLines) {
        String[] lines = normalizedCode.split("\n", -1);
        Map<String, Integer> blockCount = new HashMap<>();
        for (int i = 0; i + blockSizeLines <= lines.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < blockSizeLines; j++) {
                sb.append(lines[i + j].trim()).append('\n');
            }
            String key = sb.toString();
            blockCount.put(key, blockCount.getOrDefault(key, 0) + 1);
        }
        int duplicates = 0;
        for (int v : blockCount.values()) if (v > 1) duplicates++;
        return duplicates;
    }
}
