package com.company;

import Windows.ProjectInfoWindow;
import Windows.SettingsWindow;
import Windows.SplitWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.*;

import static Windows.OutlineWindow.outlineGrid;

/*

FEATURES TO ADD LIST

 - Add Graphic/Button to edit item title

 - Add page numbers in indicies and use text in indicies as bookmark links

 - Complete general information tab, so any entity can use the software

 - Standardize members page

 - Standardize saving location

 - Ensure any logo size fits

 - Clean code, clean libraries, and implement an installer

*/

public class SubGenApp extends Application {


    public static Stage window;
    public static Scene scene, scene1;
    public TabPane tabPane;
    public final static ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
    GridPane grid;

    public static List<PDFLineItem> contentList = new ArrayList<>();

    public static TreeItem<PDFLineItem> root;
    public static TreeView<PDFLineItem> treeView;

    public static List<String> southernList = new ArrayList<>();
    public static List<Integer> southernListIndex = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        window = primaryStage;
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab projectInfo = new Tab();
        projectInfo.setText("Create a Submittal");
        projectInfo.setContent(sizingSample());

        root = new TreeItem<PDFLineItem>(new PDFLineItem(0,"Submittal", null));
        root.setExpanded(true);
        treeView = new TreeView<PDFLineItem>(root);
        treeView.setCellFactory(param -> new CustomTreeCell());
        treeView.setEditable(true);

        Tab generalInformation = new Tab();
        generalInformation.setText("General Information");
        generalInformation.setContent(alignmentSample());

        Tab pdfSplit = new Tab();
        pdfSplit.setText("PDF Splitter");
        pdfSplit.setContent(splitSample());

        tabPane.getTabs().addAll(projectInfo, generalInformation, pdfSplit);

        FileInputStream iconStream = new FileInputStream("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\src\\SGblackLarge.png");
        window.getIcons().add(new Image(iconStream));
        window.setTitle("Submittal Generator");

        scene = new Scene(tabPane, 900, 700);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void createScene1() {

        GridPane grid = outlineGrid();
        window.setScene(scene1);
    }

    private Pane sizingSample() {

        grid = ProjectInfoWindow.createGrid();

        return grid;
    }

    private Pane alignmentSample() {

        grid = SettingsWindow.createGrid();

        return grid;
    }

    private Pane splitSample() {

        grid = SplitWindow.createGrid();

        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}







