# Code-Quality-Analyzer
-> 2nd year Java Project 

-> A JavaFX-based desktop application that analyzes Java source files and computes **29 critical code quality metrics**, generates a **percentage score**, and displays a formatted **HTML dashboard report**.

This tool allows students, developers, and organizations to upload Java files and instantly view:

✔ Code quality score
✔ Readability metrics
✔ Complexity metrics
✔ Documentation metrics
✔ Best-practice violations
✔ Full HTML report inside the app

---

##  **Features**

### **1. Upload Java File**

* Simple GUI with a JavaFX FileChooser
* Supports **.java** files

### **2️. Automatic Metrics Calculation (29 Metrics)**

Includes all major categories:

####  *Basic Metrics*

* LOC (Lines of Code)
* Comment lines
* Blank lines
* Comment ratio
* Number of methods
* Method length
* Number of classes

####  *Complexity Metrics*

* Cyclomatic complexity
* Max nesting depth
* Switch-case complexity
* Exception complexity

####  *OOP Metrics*

* Inheritance depth
* Coupling
* Cohesion
* Number of fields
* Number of static members
* Encapsulation quality

####  *Readability Metrics*

* Average line length
* Indentation score
* Naming convention score

####  *Maintainability Metrics*

* Code duplication
* Magic numbers
* Long parameter lists
* Large classes / God classes

####  *Documentation Metrics*

* Javadoc count
* Javadoc completeness

(You can list all 29 here once your code is done.)

---

##  **3️. Automatic Scoring System**

The ScoreCalculator generates:

* Individual metric scores
* Weighted final score
* Color-coded interpretation
* Grade (A / B / C / D / F)

---

##  **4️. Interactive HTML Dashboard**

Generated using an internal HTML template:

* Displays all metrics
* Color-coded score bars
* Table view
* Suggestions for improvement
* Rendered inside JavaFX **WebView**

---

#  **Project Structure**

```
CodeQualityAnalyzer/
│
├── src/
│   └── application/
│        ├── Main.java
│        ├── Main.fxml
│        ├── MetricsAnalyzer.java
│        ├── ScoreCalculator.java
│        ├── HTMLReportGenerator.java
│        └── FileChooserController.java
│
├── resources/         ← **real folder (not package)**
│     └── template.html
│
└── lib/
      └── javafx jars
```


# **Technologies Used**

* Java 17
* JavaFX 17 / 21 / 25
* FXML
* HTML + CSS
* JDK + OpenJFX SDK
* Eclipse IDE

---

#  **How to Run the Project**

### **1️. Add JavaFX to Eclipse Build Path**

Right click project →
**Build Path → Configure Build Path → Add External JARs → Add all Jars from javafx-sdk/lib**

### **2️. Add VM Arguments**

Run → Run Configurations → VM Arguments:

```
--module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml,javafx.web --enable-native-access=javafx.graphics
```

### **3️. Run Main.java**

This launches the GUI with File Upload + HTML dashboard.

---

# 📄 **template.html**

Used by HTMLReportGenerator to format metrics.
You can customize colors, fonts, or add charts.

---

# 💡 **Future Improvements**

* Export report as PDF
* Show charts inside WebView
* Add multiple file comparison
* Add AI-based suggestions

---

# 🧑‍💻 **Author**

Indira Keerthana Pulla
Created for academic learning and project submission.

---

