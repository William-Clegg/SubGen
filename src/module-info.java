module SubGen {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.swing;
    requires pdfbox;
    requires poiMerge;
    requires kotlin.stdlib;
    exports com.company to javafx.graphics;
}