package application;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data model to hold the 29 metrics.
 */
public class CodeMetrics {
    // Basic (8)
    public int loc;
    public int emptyLines;
    public int commentLines;
    public int commentToCodeRatio; // percentage
    public int classCount;
    public int interfaceCount;
    public int methodCount;
    public int avgMethodLength;

    // Complexity (7)
    public int cyclomaticComplexity;
    public int maxNestingDepth;
    public int decisionStatements;
    public int loopCount;
    public int returnCount;
    public int recursionCount;
    public int exceptionHandlingCount;

    // OOP (6)
    public int fieldCount;
    public int staticFieldCount;
    public int staticMethodCount;
    public int inheritanceCount; // occurrences of extends/implements
    public int privateFieldCount;
    public int importCount; // coupling indicator

    // Memory & Resource (5)
    public int memoryAllocationCount;
    public int stringObjectCount;
    public int fileIoCount;
    public int threadCount;
    public int possibleResourceLeaks;

    // Style & Quality (3)
    public int longMethodCount;
    public int longClassFlag; // 0/1
    public int duplicateBlockCount;

    public Map<String, Integer> toMap() {
        Map<String, Integer> m = new LinkedHashMap<>();
        m.put("Total Lines of Code (LOC)", loc);
        m.put("Empty Lines", emptyLines);
        m.put("Comment Lines", commentLines);
        m.put("Comment-to-Code Ratio (%)", commentToCodeRatio);
        m.put("Number of Classes", classCount);
        m.put("Number of Interfaces", interfaceCount);
        m.put("Number of Methods", methodCount);
        m.put("Average Method Length (lines)", avgMethodLength);

        m.put("Cyclomatic Complexity", cyclomaticComplexity);
        m.put("Maximum Nesting Depth", maxNestingDepth);
        m.put("Decision Statements", decisionStatements);
        m.put("Loop Count", loopCount);
        m.put("Return Statements", returnCount);
        m.put("Recursion Occurrences", recursionCount);
        m.put("Exception Handling Count", exceptionHandlingCount);

        m.put("Field Count", fieldCount);
        m.put("Static Field Count", staticFieldCount);
        m.put("Static Method Count", staticMethodCount);
        m.put("Inheritance Occurrences", inheritanceCount);
        m.put("Private Field Count", privateFieldCount);
        m.put("Imports (Coupling)", importCount);

        m.put("Memory Allocation Count", memoryAllocationCount);
        m.put("String Object Creation Count", stringObjectCount);
        m.put("File/IO Operations Count", fileIoCount);
        m.put("Thread Usage Count", threadCount);
        m.put("Possible Resource Leaks", possibleResourceLeaks);

        m.put("Long Method Count (>40 LOC)", longMethodCount);
        m.put("Long Class Flag (>500 LOC)", longClassFlag);
        m.put("Duplicate Block Count", duplicateBlockCount);

        return m;
    }
}
