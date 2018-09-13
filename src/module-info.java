module SubGen {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.swing;
    requires pdfbox;
    requires poiMerge;
    requires kotlin.stdlib;
    requires xmlbeans;
    requires fr.opensagres.poi.xwpf.converter.pdf;
    exports com.company to javafx.graphics;
}